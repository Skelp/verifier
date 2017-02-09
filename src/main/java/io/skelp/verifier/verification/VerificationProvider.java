/*
 * Copyright (C) 2017 Alasdair Mercer, Skelp
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

import io.skelp.verifier.VerifierException;
import io.skelp.verifier.service.Weighted;

/**
 * <p>
 * Provides {@link Verification} instances to be used to provide context to verifications based on a given value and
 * optional name.
 * </p>
 * <p>
 * {@code VerificationProviders} are registered via Java's SPI, so in order to register a custom
 * {@code VerificationProvider} projects should contain should create a
 * {@code io.skelp.verifier.verification.verification.VerificationProvider} file within {@code META-INF/services}
 * listing the class reference for each custom {@code VerificationProvider} (e.g.
 * {@code com.example.verifier.MyCustomVerificationProvider}) on separate lines. {@code VerificationProviders} are also
 * {@link Weighted}, which means that they are loaded in priority order (the lower the weight, the higher the priority).
 * Verifier has a built-in default {@code VerificationProvider} which is given a low priority (i.e.
 * {@value #DEFAULT_IMPLEMENTATION_WEIGHT}) to allow custom implementations to be easily ordered around it.
 * </p>
 *
 * @author Alasdair Mercer
 * @since 0.2.0
 */
public interface VerificationProvider extends Weighted {

    /**
     * <p>
     * Returns an instance of {@link Verification} based on the {@code value} and optional {@code name} provided.
     * </p>
     *
     * @param value
     *         the value being verified
     * @param name
     *         the optional name for the value being verified
     * @param <T>
     *         the type of the value being verified
     * @return The {@link Verification} instance using {@code value} and {@code name}.
     * @throws VerifierException
     *         If a problem occurs while providing the {@link Verification} instance.
     */
    <T> Verification<T> getVerification(T value, Object name);
}
