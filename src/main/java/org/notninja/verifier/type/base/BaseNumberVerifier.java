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
package org.notninja.verifier.type.base;

import org.notninja.verifier.VerifierException;
import org.notninja.verifier.message.MessageKey;

/**
 * <p>
 * An extension of {@link BaseTruthVerifier} which includes methods for verifying numeric values.
 * </p>
 * <p>
 * All implementations of {@code BaseNumberVerifier} should implement the {@link BaseTruthVerifier} methods in a way
 * that the numerical representation of zero is <b>always</b> considered to be falsy and the numerical representation of
 * one is <b>always</b> considered to be truthy. As with all {@link BaseTruthVerifier BaseTruthVerifiers},
 * {@literal null} will also be considered to be falsy.
 * </p>
 *
 * @param <T>
 *         the {@code Number} type of the value being verified
 * @param <V>
 *         the type of the {@link BaseNumberVerifier} for chaining purposes
 * @author Alasdair Mercer
 */
public interface BaseNumberVerifier<T extends Number, V extends BaseNumberVerifier<T, V>> extends BaseTruthVerifier<T, V> {

    /**
     * <p>
     * Verifies that the value is even (i.e. divisible by two without a remainder).
     * </p>
     * <pre>
     * Verifier.verify((Integer) null).even() =&gt; FAIL
     * Verifier.verify(0).even()              =&gt; PASS
     * Verifier.verify(1).even()              =&gt; FAIL
     * Verifier.verify(2).even()              =&gt; PASS
     * Verifier.verify(12).even()             =&gt; PASS
     * </pre>
     *
     * @return A reference to this {@link BaseNumberVerifier} for chaining purposes.
     * @throws VerifierException
     *         If the verification fails while not negated or passes while negated.
     * @see #odd()
     */
    V even();

    /**
     * <p>
     * Verifies that the value is negative (i.e. less that zero).
     * </p>
     * <pre>
     * Verifier.verify((Integer) null).negative() =&gt; FAIL
     * Verifier.verify(0).negative()              =&gt; FAIL
     * Verifier.verify(1).negative()              =&gt; FAIL
     * Verifier.verify(-1).negative()             =&gt; PASS
     * </pre>
     *
     * @return A reference to this {@link BaseNumberVerifier} for chaining purposes.
     * @throws VerifierException
     *         If the verification fails while not negated or passes while negated.
     * @see #positive()
     */
    V negative();

    /**
     * <p>
     * Verifies that the value is odd (i.e. has one left over as a remainder when divided by two).
     * </p>
     * <pre>
     * Verifier.verify((Integer) null).odd() =&gt; FAIL
     * Verifier.verify(0).odd()              =&gt; FAIL
     * Verifier.verify(1).odd()              =&gt; PASS
     * Verifier.verify(2).odd()              =&gt; FAIL
     * Verifier.verify(13).odd()             =&gt; PASS
     * </pre>
     *
     * @return A reference to this {@link BaseNumberVerifier} for chaining purposes.
     * @throws VerifierException
     *         If the verification fails while not negated or passes while negated.
     * @see #even()
     */
    V odd();

    /**
     * <p>
     * Verifies that the value is one.
     * </p>
     * <pre>
     * Verifier.verify((Integer) null).one() =&gt; FAIL
     * Verifier.verify(0).one()              =&gt; FAIL
     * Verifier.verify(1).one()              =&gt; PASS
     * Verifier.verify(-1).one()             =&gt; FAIL
     * Verifier.verify(2).one()              =&gt; FAIL
     * </pre>
     *
     * @return A reference to this {@link BaseNumberVerifier} for chaining purposes.
     * @throws VerifierException
     *         If the verification fails while not negated or passes while negated.
     * @see #zero()
     */
    V one();

    /**
     * <p>
     * Verifies that the value is positive (i.e. greater than or equal to zero).
     * </p>
     * <pre>
     * Verifier.verify((Integer) null).positive() =&gt; FAIL
     * Verifier.verify(0).positive()              =&gt; PASS
     * Verifier.verify(1).positive()              =&gt; PASS
     * Verifier.verify(-1).positive()             =&gt; FAIL
     * </pre>
     *
     * @return A reference to this {@link BaseNumberVerifier} for chaining purposes.
     * @throws VerifierException
     *         If the verification fails while not negated or passes while negated.
     * @see #negative()
     */
    V positive();

    /**
     * <p>
     * Verifies that the value is zero.
     * </p>
     * <pre>
     * Verifier.verify((Integer) null).zero() =&gt; FAIL
     * Verifier.verify(0).zero()              =&gt; PASS
     * Verifier.verify(1).zero()              =&gt; FAIL
     * Verifier.verify(-1).zero()             =&gt; FAIL
     * </pre>
     *
     * @return A reference to this {@link BaseNumberVerifier} for chaining purposes.
     * @throws VerifierException
     *         If the verification fails while not negated or passes while negated.
     * @see #one()
     */
    V zero();

    /**
     * <p>
     * The {@link MessageKey MessageKeys} that are used by {@link BaseNumberVerifier}.
     * </p>
     *
     * @since 0.2.0
     */
    enum MessageKeys implements MessageKey {

        EVEN("org.notninja.verifier.type.base.BaseNumberVerifier.even"),
        NEGATIVE("org.notninja.verifier.type.base.BaseNumberVerifier.negative"),
        ODD("org.notninja.verifier.type.base.BaseNumberVerifier.odd"),
        ONE("org.notninja.verifier.type.base.BaseNumberVerifier.one"),
        POSITIVE("org.notninja.verifier.type.base.BaseNumberVerifier.positive"),
        ZERO("org.notninja.verifier.type.base.BaseNumberVerifier.zero");

        private final String code;

        MessageKeys(final String code) {
            this.code = code;
        }

        @Override
        public String code() {
            return code;
        }
    }
}
