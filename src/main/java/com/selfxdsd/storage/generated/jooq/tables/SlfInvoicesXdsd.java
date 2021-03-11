/*
 * This file is generated by jOOQ.
 */
package com.selfxdsd.storage.generated.jooq.tables;


import com.selfxdsd.storage.generated.jooq.Indexes;
import com.selfxdsd.storage.generated.jooq.Keys;
import com.selfxdsd.storage.generated.jooq.SelfXdsd;
import com.selfxdsd.storage.generated.jooq.tables.records.SlfInvoicesXdsdRecord;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row11;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class SlfInvoicesXdsd extends TableImpl<SlfInvoicesXdsdRecord> {

    private static final long serialVersionUID = 1109559127;

    /**
     * The reference instance of <code>self_xdsd.slf_invoices_xdsd</code>
     */
    public static final SlfInvoicesXdsd SLF_INVOICES_XDSD = new SlfInvoicesXdsd();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<SlfInvoicesXdsdRecord> getRecordType() {
        return SlfInvoicesXdsdRecord.class;
    }

    /**
     * The column <code>self_xdsd.slf_invoices_xdsd.invoiceId</code>.
     */
    public final TableField<SlfInvoicesXdsdRecord, Integer> INVOICEID = createField(DSL.name("invoiceId"), org.jooq.impl.SQLDataType.INTEGER.nullable(false).identity(true), this, "");

    /**
     * The column <code>self_xdsd.slf_invoices_xdsd.repo_fullname</code>.
     */
    public final TableField<SlfInvoicesXdsdRecord, String> REPO_FULLNAME = createField(DSL.name("repo_fullname"), org.jooq.impl.SQLDataType.VARCHAR(256).nullable(false), this, "");

    /**
     * The column <code>self_xdsd.slf_invoices_xdsd.username</code>.
     */
    public final TableField<SlfInvoicesXdsdRecord, String> USERNAME = createField(DSL.name("username"), org.jooq.impl.SQLDataType.VARCHAR(100).nullable(false), this, "");

    /**
     * The column <code>self_xdsd.slf_invoices_xdsd.provider</code>.
     */
    public final TableField<SlfInvoicesXdsdRecord, String> PROVIDER = createField(DSL.name("provider"), org.jooq.impl.SQLDataType.VARCHAR(50).nullable(false), this, "");

    /**
     * The column <code>self_xdsd.slf_invoices_xdsd.role</code>.
     */
    public final TableField<SlfInvoicesXdsdRecord, String> ROLE = createField(DSL.name("role"), org.jooq.impl.SQLDataType.VARCHAR(32).nullable(false), this, "");

    /**
     * The column <code>self_xdsd.slf_invoices_xdsd.createdAt</code>.
     */
    public final TableField<SlfInvoicesXdsdRecord, LocalDateTime> CREATEDAT = createField(DSL.name("createdAt"), org.jooq.impl.SQLDataType.LOCALDATETIME.nullable(false), this, "");

    /**
     * The column <code>self_xdsd.slf_invoices_xdsd.billedBy</code>.
     */
    public final TableField<SlfInvoicesXdsdRecord, String> BILLEDBY = createField(DSL.name("billedBy"), org.jooq.impl.SQLDataType.VARCHAR(512), this, "");

    /**
     * The column <code>self_xdsd.slf_invoices_xdsd.billedTo</code>.
     */
    public final TableField<SlfInvoicesXdsdRecord, String> BILLEDTO = createField(DSL.name("billedTo"), org.jooq.impl.SQLDataType.VARCHAR(512), this, "");

    /**
     * The column <code>self_xdsd.slf_invoices_xdsd.billedByCountry</code>.
     */
    public final TableField<SlfInvoicesXdsdRecord, String> BILLEDBYCOUNTRY = createField(DSL.name("billedByCountry"), org.jooq.impl.SQLDataType.VARCHAR(32), this, "");

    /**
     * The column <code>self_xdsd.slf_invoices_xdsd.billedToCountry</code>.
     */
    public final TableField<SlfInvoicesXdsdRecord, String> BILLEDTOCOUNTRY = createField(DSL.name("billedToCountry"), org.jooq.impl.SQLDataType.VARCHAR(32), this, "");

    /**
     * The column <code>self_xdsd.slf_invoices_xdsd.eurToRon</code>.
     */
    public final TableField<SlfInvoicesXdsdRecord, BigInteger> EURTORON = createField(DSL.name("eurToRon"), org.jooq.impl.SQLDataType.DECIMAL_INTEGER.precision(20).defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.DECIMAL_INTEGER)), this, "");

    /**
     * Create a <code>self_xdsd.slf_invoices_xdsd</code> table reference
     */
    public SlfInvoicesXdsd() {
        this(DSL.name("slf_invoices_xdsd"), null);
    }

    /**
     * Create an aliased <code>self_xdsd.slf_invoices_xdsd</code> table reference
     */
    public SlfInvoicesXdsd(String alias) {
        this(DSL.name(alias), SLF_INVOICES_XDSD);
    }

    /**
     * Create an aliased <code>self_xdsd.slf_invoices_xdsd</code> table reference
     */
    public SlfInvoicesXdsd(Name alias) {
        this(alias, SLF_INVOICES_XDSD);
    }

    private SlfInvoicesXdsd(Name alias, Table<SlfInvoicesXdsdRecord> aliased) {
        this(alias, aliased, null);
    }

    private SlfInvoicesXdsd(Name alias, Table<SlfInvoicesXdsdRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    public <O extends Record> SlfInvoicesXdsd(Table<O> child, ForeignKey<O, SlfInvoicesXdsdRecord> key) {
        super(child, key, SLF_INVOICES_XDSD);
    }

    @Override
    public Schema getSchema() {
        return SelfXdsd.SELF_XDSD;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.SLF_INVOICES_XDSD_FKCONTRACT_IDX, Indexes.SLF_INVOICES_XDSD_INVOICECONTRACTFK_IDX);
    }

    @Override
    public Identity<SlfInvoicesXdsdRecord, Integer> getIdentity() {
        return Keys.IDENTITY_SLF_INVOICES_XDSD;
    }

    @Override
    public UniqueKey<SlfInvoicesXdsdRecord> getPrimaryKey() {
        return Keys.KEY_SLF_INVOICES_XDSD_PRIMARY;
    }

    @Override
    public List<UniqueKey<SlfInvoicesXdsdRecord>> getKeys() {
        return Arrays.<UniqueKey<SlfInvoicesXdsdRecord>>asList(Keys.KEY_SLF_INVOICES_XDSD_PRIMARY);
    }

    @Override
    public List<ForeignKey<SlfInvoicesXdsdRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<SlfInvoicesXdsdRecord, ?>>asList(Keys.FKCONTRACT);
    }

    public SlfContractsXdsd slfContractsXdsd() {
        return new SlfContractsXdsd(this, Keys.FKCONTRACT);
    }

    @Override
    public SlfInvoicesXdsd as(String alias) {
        return new SlfInvoicesXdsd(DSL.name(alias), this);
    }

    @Override
    public SlfInvoicesXdsd as(Name alias) {
        return new SlfInvoicesXdsd(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public SlfInvoicesXdsd rename(String name) {
        return new SlfInvoicesXdsd(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public SlfInvoicesXdsd rename(Name name) {
        return new SlfInvoicesXdsd(name, null);
    }

    // -------------------------------------------------------------------------
    // Row11 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row11<Integer, String, String, String, String, LocalDateTime, String, String, String, String, BigInteger> fieldsRow() {
        return (Row11) super.fieldsRow();
    }
}
