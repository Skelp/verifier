/*
 * Copyright (C) 2016 Alasdair Mercer, Skelp
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
package io.skelp.verifier.verification;

import java.util.function.Supplier;

import io.skelp.verifier.VerifierException;
import io.skelp.verifier.message.MessageKey;
import io.skelp.verifier.message.MessageSource;
import io.skelp.verifier.message.locale.LocaleContext;

/**
 * <p>
 * A simple immutable implementation of {@link Verification}.
 * </p>
 *
 * @param <T>
 *         the type of the value being verified
 * @author Alasdair Mercer
 * @since 0.2.0
 */
public final class SimpleVerification<T> implements Verification<T> {

    private final LocaleContext localeContext;
    private final MessageSource messageSource;
    private final Object name;
    private boolean negated;
    private final T value;

    /**
     * <p>
     * Creates an instance of {@link SimpleVerification} based on the {@code value} and optional {@code name} provided.
     * </p>
     *
     * @param localeContext
     *         the current {@link LocaleContext}
     * @param messageSource
     *         the {@link MessageSource} to be used to lookup and/or format the messages for any {@link
     *         VerifierException VerifierExceptions}
     * @param value
     *         the value being verified
     * @param name
     *         the optional name used to represent {@code value}
     */
    public SimpleVerification(final LocaleContext localeContext, final MessageSource messageSource, final T value, final Object name) {
        this.localeContext = localeContext;
        this.messageSource = messageSource;
        this.value = value;
        this.name = name;
    }

    @Override
    public SimpleVerification<T> check(final boolean result, final MessageKey key, final Object... args) {
        return check(result, () -> getMessageSource().getMessage(this, key, args));
    }

    @Override
    public SimpleVerification<T> check(final boolean result, final String message, final Object... args) {
        return check(result, () -> getMessageSource().getMessage(this, message, args));
    }

    private SimpleVerification<T> check(final boolean result, final Supplier<String> messageSupplier) {
        if (result && isNegated() || !result && !isNegated()) {
            final String errorMessage = messageSupplier.get();
            setNegated(false);

            throw new VerifierException(errorMessage);
        }

        setNegated(false);

        return this;
    }

    @Override
    public LocaleContext getLocaleContext() {
        return localeContext;
    }

    @Override
    public MessageSource getMessageSource() {
        return messageSource;
    }

    @Override
    public Object getName() {
        return name;
    }

    @Override
    public boolean isNegated() {
        return negated;
    }

    @Override
    public void setNegated(final boolean negated) {
        this.negated = negated;
    }

    @Override
    public T getValue() {
        return value;
    }
}