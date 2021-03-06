/*
 * Copyright (C) 2017 Alasdair Mercer, !ninja
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.notninja.verifier;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Locale;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import org.notninja.verifier.message.ResourceBundleMessageSource;
import org.notninja.verifier.message.formatter.DefaultFormatterProvider;
import org.notninja.verifier.message.locale.SimpleLocaleContext;
import org.notninja.verifier.service.Weighted;
import org.notninja.verifier.verification.SimpleVerification;
import org.notninja.verifier.verification.TestVerificationProvider;
import org.notninja.verifier.verification.Verification;
import org.notninja.verifier.verification.VerificationProvider;
import org.notninja.verifier.verification.report.DefaultReportExecutorProvider;

/**
 * <p>
 * Tests for the {@link DefaultCustomVerifierProvider} class.
 * </p>
 *
 * @author Alasdair Mercer
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultCustomVerifierProviderTest {

    @Mock
    private Verification<Object> mockVerification;
    @Mock
    private VerificationProvider mockVerificationProvider;

    private DefaultCustomVerifierProvider provider;

    @Before
    public void setUp() {
        when(mockVerificationProvider.getVerification(any(), any())).thenAnswer(invocation -> new SimpleVerification<>(new SimpleLocaleContext(Locale.ENGLISH), new ResourceBundleMessageSource(), new DefaultFormatterProvider(), new DefaultReportExecutorProvider().getReportExecutor(), invocation.getArguments()[0], invocation.getArguments()[1]));

        TestVerificationProvider.setDelegate(mockVerificationProvider);

        provider = new DefaultCustomVerifierProvider();
    }

    @After
    public void tearDown() {
        TestVerificationProvider.setDelegate(null);
    }

    @Test
    public void testGetCustomVerifier() {
        TestCustomVerifierImpl customVerifier = provider.getCustomVerifier(TestCustomVerifierImpl.class, mockVerification);

        assertNotNull("Never returns null", customVerifier);
        assertSame("Passed verification", mockVerification, customVerifier.verification);
        assertNotSame("Never returns same instance", customVerifier, provider.getCustomVerifier(TestCustomVerifierImpl.class, mockVerification));
    }

    @Test(expected = VerifierException.class)
    public void testGetCustomVerifierThrowsIfClassCannotBeInstantiated() {
        provider.getCustomVerifier(TestCustomVerifier.class, mockVerification);
    }

    @Test(expected = VerifierException.class)
    public void testGetCustomVerifierThrowsIfClassIsNull() {
        provider.getCustomVerifier(null, mockVerification);
    }

    @Test
    public void testGetWeight() {
        assertEquals("Has default implementation weight", Weighted.DEFAULT_IMPLEMENTATION_WEIGHT, provider.getWeight());
    }

    private interface TestCustomVerifier<T, V extends TestCustomVerifier<T, V>> extends CustomVerifier<T, V> {
    }

    private static class TestCustomVerifierImpl extends AbstractCustomVerifier<Object, TestCustomVerifierImpl> implements TestCustomVerifier<Object, TestCustomVerifierImpl> {

        final Verification<Object> verification;

        public TestCustomVerifierImpl(Verification<Object> verification) {
            super(verification);

            this.verification = verification;
        }
    }
}
