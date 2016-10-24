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
package io.skelp.verifier.factory;

import io.skelp.verifier.CustomVerifier;
import io.skelp.verifier.verification.Verification;

/**
 * A factory for creating instances of {@link CustomVerifier} from their class based on a given {@link Verification}.
 *
 * @author Alasdair Mercer
 */
public interface CustomVerifierFactory {

    /**
     * Creates an instance of the specified {@link CustomVerifier} class based on the {@code verification} provided.
     *
     * @param cls
     *         the {@link CustomVerifier} class from which an instance is to be created
     * @param verification
     *         the {@link Verification} to be passed to the instance
     * @param <T>
     *         the type of the value to be verified
     * @param <V>
     *         the type of the {@link CustomVerifier}
     * @return The newly created {@link CustomVerifier} based on {@code cls} and using {@code verification}.
     * @throws VerifierFactoryException
     *         If a problem occurs while creating the {@link CustomVerifier} instance.
     */
    <T, V extends CustomVerifier<T, V>> V create(Class<V> cls, Verification<T> verification) throws VerifierFactoryException;
}
