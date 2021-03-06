/**
 * Copyright (c) 2020-2021, Self XDSD Contributors
 * All rights reserved.
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"),
 * to read the Software only. Permission is hereby NOT GRANTED to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software.
 * <p>
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY,
 * OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT
 * OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package com.selfxdsd.storage;

import com.selfxdsd.api.*;
import com.selfxdsd.api.storage.Storage;
import com.selfxdsd.core.contracts.invoices.ContractInvoices;
import com.selfxdsd.core.contracts.invoices.StoredInvoice;
import com.selfxdsd.core.contracts.invoices.StoredPayment;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.selfxdsd.storage.generated.jooq.Tables.*;

/**
 * Invoices in Self.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.4
 */
public final class SelfInvoices implements Invoices {

    /**
     * Parent Storage.
     */
    private final Storage storage;

    /**
     * Database.
     */
    private final Database database;

    /**
     * Ctor.
     * @param storage Parent Storage.
     * @param database Database.
     */
    public SelfInvoices(
        final Storage storage,
        final Database database
    ) {
        this.storage = storage;
        this.database = database;
    }

    @Override
    public Invoice getById(final int id) {
        final Result<Record> result = this.database.jooq()
            .select()
            .from(SLF_INVOICES_XDSD)
            .leftJoin(SLF_PAYMENTS_XDSD)
            .on(SLF_INVOICES_XDSD.INVOICEID.eq(SLF_PAYMENTS_XDSD.INVOICEID))
            .where(SLF_INVOICES_XDSD.INVOICEID.eq(id))
            .orderBy(SLF_PAYMENTS_XDSD.PAYMENT_TIMESTAMP.desc())
            .fetch();
        if(!result.isEmpty()) {
            return this.buildInvoice(result.get(0));
        }
        return null;
    }

    @Override
    public Invoice createNewInvoice(final Contract.Id contractId) {
        final Contract contract = this.storage
            .contracts().findById(contractId);
        if(contract == null) {
            throw new IllegalStateException(
                "The specified Contract does not exist, "
              + "can't create an Invoice for it."
            );
        }
        final LocalDateTime createdAt = LocalDateTime.now();
        final int invoiceId = this.database.jooq().insertInto(
            SLF_INVOICES_XDSD,
            SLF_INVOICES_XDSD.REPO_FULLNAME,
            SLF_INVOICES_XDSD.USERNAME,
            SLF_INVOICES_XDSD.PROVIDER,
            SLF_INVOICES_XDSD.ROLE,
            SLF_INVOICES_XDSD.CREATEDAT
        ).values(
            contractId.getRepoFullName(),
            contractId.getContributorUsername(),
            contractId.getProvider(),
            contractId.getRole(),
            createdAt
        ).returning(SLF_INVOICES_XDSD.INVOICEID)
            .fetchOne()
            .getValue(SLF_INVOICES_XDSD.INVOICEID);
        return new StoredInvoice(
            invoiceId,
            contract,
            createdAt,
            null,
            null,
            null,
            null,
            null,
            BigDecimal.valueOf(0),
            this.storage
        );
    }

    @Override
    public Invoice active() {
        throw new UnsupportedOperationException(
            "Cannot get the active Invoice out of all of them. "
          + "Call #ofContract(...) first."
        );
    }

    @Override
    public Invoices ofContract(final Contract.Id id) {
        final Contract contract = this.storage.contracts().findById(id);
        final Supplier<Stream<Invoice>> ofContract = () -> this.database.jooq()
            .select()
            .from(SLF_INVOICES_XDSD)
            .leftJoin(SLF_PAYMENTS_XDSD)
            .on(SLF_INVOICES_XDSD.INVOICEID.eq(SLF_PAYMENTS_XDSD.INVOICEID))
            .where(SLF_INVOICES_XDSD.REPO_FULLNAME.eq(id.getRepoFullName())
                .and(SLF_INVOICES_XDSD.USERNAME.eq(id.getContributorUsername()))
                .and(SLF_INVOICES_XDSD.PROVIDER.eq(id.getProvider()))
                .and(SLF_INVOICES_XDSD.ROLE.eq(id.getRole()))
            )
            .orderBy(SLF_PAYMENTS_XDSD.PAYMENT_TIMESTAMP.desc())
            .stream()
            .map(record -> buildInvoice(record, contract))
            .collect(Collectors.toSet())
            .stream();
        return new ContractInvoices(id, ofContract, this.storage);
    }

    /**
     * {@inheritDoc}
     * <br>
     *
     * Insert the successful Payment and update the Invoice billing data.
     * If it's a <b>real payment</b> insert the PlatformInvoice as well.
     *
     * @param invoice Paid invoice.
     * @param contributorVat Vat which Self takes from the Contributor.
     * @param eurToRon Euro to RON (Romanian Leu) conversion rate.
     *  For example, if the value is 487, it means 1 EUR = 4,87 RON.
     * @return Successful Payment.
     */
    @Override
    public Payment registerAsPaid(
        final Invoice invoice,
        final BigDecimal contributorVat,
        final BigDecimal eurToRon
    ) {
        if(!invoice.isPaid()) {
            throw new IllegalArgumentException(
                "Invoice #" + invoice.invoiceId() + " is not paid!"
            );
        }
        final Payment success = invoice.latest();
        if(success.transactionId().startsWith("fake_payment_")) {
            final DSLContext jooq = this.database.jooq();
            jooq.transaction(
                configuration -> {
                    jooq.insertInto(
                        SLF_PAYMENTS_XDSD,
                        SLF_PAYMENTS_XDSD.INVOICEID,
                        SLF_PAYMENTS_XDSD.TRANSACTIONID,
                        SLF_PAYMENTS_XDSD.PAYMENT_TIMESTAMP,
                        SLF_PAYMENTS_XDSD.VALUE,
                        SLF_PAYMENTS_XDSD.STATUS,
                        SLF_PAYMENTS_XDSD.FAILREASON
                    ).values(
                        invoice.invoiceId(),
                        success.transactionId(),
                        success.paymentTime(),
                        success.value().toBigIntegerExact(),
                        success.status(),
                        success.failReason()
                    ).execute();
                    jooq.update(SLF_INVOICES_XDSD).set(
                        SLF_INVOICES_XDSD.BILLEDBY,
                        invoice.billedBy()
                    ).set(
                        SLF_INVOICES_XDSD.BILLEDTO,
                        invoice.billedTo()
                    ).set(
                        SLF_INVOICES_XDSD.BILLEDBYCOUNTRY,
                        invoice.billedByCountry()
                    ).set(
                        SLF_INVOICES_XDSD.BILLEDTOCOUNTRY,
                        invoice.billedToCountry()
                    ).set(
                        SLF_INVOICES_XDSD.EURTORON,
                        invoice.eurToRon().toBigIntegerExact()
                    ).where(
                        SLF_INVOICES_XDSD.INVOICEID.eq(invoice.invoiceId())
                    ).execute();
                }
            );
        } else {
            final DSLContext jooq = this.database.jooq();
            jooq.transaction(
                configuration -> {
                    final String contributorBilling = invoice.billedBy();
                    jooq.insertInto(
                        SLF_PAYMENTS_XDSD,
                        SLF_PAYMENTS_XDSD.INVOICEID,
                        SLF_PAYMENTS_XDSD.TRANSACTIONID,
                        SLF_PAYMENTS_XDSD.PAYMENT_TIMESTAMP,
                        SLF_PAYMENTS_XDSD.VALUE,
                        SLF_PAYMENTS_XDSD.STATUS,
                        SLF_PAYMENTS_XDSD.FAILREASON
                    ).values(
                        invoice.invoiceId(),
                        success.transactionId(),
                        success.paymentTime(),
                        success.value().toBigIntegerExact(),
                        success.status(),
                        success.failReason()
                    ).execute();
                    jooq.update(SLF_INVOICES_XDSD).set(
                        SLF_INVOICES_XDSD.BILLEDBY,
                        contributorBilling
                    ).set(
                        SLF_INVOICES_XDSD.BILLEDTO,
                        invoice.billedTo()
                    ).set(
                        SLF_INVOICES_XDSD.BILLEDBYCOUNTRY,
                        invoice.billedByCountry()
                    ).set(
                        SLF_INVOICES_XDSD.BILLEDTOCOUNTRY,
                        invoice.billedToCountry()
                    ).set(
                        SLF_INVOICES_XDSD.EURTORON,
                        invoice.eurToRon().toBigIntegerExact()
                    ).where(
                        SLF_INVOICES_XDSD.INVOICEID.eq(invoice.invoiceId())
                    ).execute();
                    jooq.insertInto(
                        SLF_PLATFORMINVOICES_XDSD,
                        SLF_PLATFORMINVOICES_XDSD.CREATEDAT,
                        SLF_PLATFORMINVOICES_XDSD.BILLEDTO,
                        SLF_PLATFORMINVOICES_XDSD.COMMISSION,
                        SLF_PLATFORMINVOICES_XDSD.VAT,
                        SLF_PLATFORMINVOICES_XDSD.TRANSACTIONID,
                        SLF_PLATFORMINVOICES_XDSD.PAYMENT_TIMESTAMP,
                        SLF_PLATFORMINVOICES_XDSD.INVOICEID,
                        SLF_PLATFORMINVOICES_XDSD.EURTORON
                    ).values(
                        LocalDateTime.now(),
                        contributorBilling,
                        invoice.projectCommission()
                            .add(invoice.contributorCommission())
                            .toBigIntegerExact(),
                        contributorVat.toBigIntegerExact(),
                        success.transactionId(),
                        success.paymentTime(),
                        invoice.invoiceId(),
                        eurToRon.toBigIntegerExact()
                    ).execute();
                }
            );
        }
        return success;
    }

    @Override
    public Iterator<Invoice> iterator() {
        throw new UnsupportedOperationException(
            "You cannot iterate over all Invoices. "
          + "Call #ofContract(...) first."
        );
    }

    /**
     * Builds an Invoice from a {@link Record}.
     * @param record Record.
     * @return Invoice.
     */
    private Invoice buildInvoice(final Record record) {
        final Contract contract = this.storage.contracts().findById(
            new Contract.Id(
                record.getValue(SLF_INVOICES_XDSD.REPO_FULLNAME),
                record.getValue(SLF_INVOICES_XDSD.USERNAME),
                record.getValue(SLF_INVOICES_XDSD.PROVIDER),
                record.getValue(SLF_INVOICES_XDSD.ROLE)
            )
        );
        return this.buildInvoice(record, contract);
    }

    /**
     * Builds an Invoice from a {@link Record} and a {@link Contract}.
     *
     * @param record Record.
     * @param contract Contract.
     * @return Invoice.
     */
    private Invoice buildInvoice(final Record record, final Contract contract) {
        final Payment latest;
        if(record.getValue(SLF_PAYMENTS_XDSD.STATUS) != null) {
            latest = new StoredPayment(
                record.getValue(SLF_INVOICES_XDSD.INVOICEID),
                record.getValue(SLF_PAYMENTS_XDSD.TRANSACTIONID),
                record.getValue(SLF_PAYMENTS_XDSD.PAYMENT_TIMESTAMP),
                BigDecimal.valueOf(
                    record.getValue(SLF_PAYMENTS_XDSD.VALUE).longValue()
                ),
                record.getValue(SLF_PAYMENTS_XDSD.STATUS),
                record.getValue(SLF_PAYMENTS_XDSD.FAILREASON),
                this.storage
            );
        } else {
            latest = null;
        }
        return new StoredInvoice(
            record.getValue(SLF_INVOICES_XDSD.INVOICEID),
            contract,
            record.getValue(SLF_INVOICES_XDSD.CREATEDAT),
            latest,
            record.getValue(SLF_INVOICES_XDSD.BILLEDBY),
            record.getValue(SLF_INVOICES_XDSD.BILLEDTO),
            record.getValue(SLF_INVOICES_XDSD.BILLEDBYCOUNTRY),
            record.getValue(SLF_INVOICES_XDSD.BILLEDTOCOUNTRY),
            BigDecimal.valueOf(
                record.getValue(SLF_INVOICES_XDSD.EURTORON).longValue()
            ),
            this.storage
        );
    }

}
