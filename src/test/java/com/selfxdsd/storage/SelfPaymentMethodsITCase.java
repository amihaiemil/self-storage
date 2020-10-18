/**
 * Copyright (c) 2020, Self XDSD Contributors
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
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Integration tests for {@link SelfPaymentMethods}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.13
 */
public final class SelfPaymentMethodsITCase {

    /**
     * Method ofWallet returns the PaymentMethods of a Wallet.
     */
    @Test
    public void returnsPaymentMethodsOfWallet() {
        final Storage storage = new SelfJooq(new H2Database());
        final PaymentMethods all = storage.paymentMethods();

        final PaymentMethods ofWallet = all.ofWallet(
            this.mockWallet(
                "amihaiemil/docker-java-api",
                Provider.Names.GITHUB,
                Wallet.Type.STRIPE
            )
        );
        MatcherAssert.assertThat(
            ofWallet,
            Matchers.iterableWithSize(
                Matchers.greaterThanOrEqualTo(2)
            )
        );
    }

    /**
     * Method ofWallet returns empty.
     */
    @Test
    public void ofWalletReturnsEmpty() {
        final Storage storage = new SelfJooq(new H2Database());
        final PaymentMethods all = storage.paymentMethods();

        final PaymentMethods ofWallet = all.ofWallet(
            this.mockWallet(
                "amihaiemil/docker-java-api",
                Provider.Names.GITHUB,
                Wallet.Type.FAKE
            )
        );
        MatcherAssert.assertThat(
            ofWallet,
            Matchers.emptyIterable()
        );
    }

    /**
     * SelfPaymentMethods can register a Stripe PaymentMethod
     * for a Stripe wallet.
     */
    @Test
    public void registersStripePaymentMethod() {
        final Storage storage = new SelfJooq(new H2Database());
        final Project project = storage.projects().getProjectById(
            "johndoe/stripe_repo", Provider.Names.GITHUB
        );
        final Wallet wallet = project.wallets().active();

        final PaymentMethods all = storage.paymentMethods();

        final PaymentMethods methods = all.ofWallet(wallet);
        MatcherAssert.assertThat(
            methods,
            Matchers.emptyIterable()
        );

        final PaymentMethod stripe = all.register(
            wallet,
            "stripe_pm_123"
        );

        MatcherAssert.assertThat(
            stripe.active(),
            Matchers.is(Boolean.FALSE)
        );
        MatcherAssert.assertThat(
            stripe.identifier(),
            Matchers.equalTo("stripe_pm_123")
        );
        MatcherAssert.assertThat(
            stripe.wallet(),
            Matchers.is(wallet)
        );
        MatcherAssert.assertThat(
            wallet.paymentMethods(),
            Matchers.iterableWithSize(1)
        );
    }

    /**
     * Mock a wallet (as an alternative to having to select
     * a real one from the DB).
     * @param repoFullName Project's repo full name.
     * @param provider Project's provider.
     * @param type Wallet's type.
     * @return Wallet.
     */
    private Wallet mockWallet(
        final String repoFullName,
        final String provider,
        final String type
    ) {
        final Project project = Mockito.mock(Project.class);
        Mockito.when(project.repoFullName()).thenReturn(repoFullName);
        Mockito.when(project.provider()).thenReturn(provider);
        final Wallet wallet = Mockito.mock(Wallet.class);
        Mockito.when(wallet.project()).thenReturn(project);
        Mockito.when(wallet.type()).thenReturn(type);
        return wallet;
    }

}