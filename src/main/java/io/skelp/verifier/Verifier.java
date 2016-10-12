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
package io.skelp.verifier;

import io.skelp.verifier.message.DefaultMessageFormatter;
import io.skelp.verifier.message.MessageFormatter;
import io.skelp.verifier.type.TypeVerifier;

/**
 * TODO: Document
 *
 * @author Alasdair Mercer
 */
public final class Verifier {

  private static MessageFormatter defaultMessageFormatter = new DefaultMessageFormatter();

  /**
   * TODO: Document
   *
   * @param value
   * @param name
   * @param type
   * @param <T>
   * @return
   * @throws VerifierException
   */
  public static <T extends TypeVerifier> T verify(final Object value, final Object name, final Class<T> type) {
    return verify(value, name, type, null);
  }

  /**
   * TODO: Document
   *
   * @param value
   * @param name
   * @param type
   * @param messageFormatter
   * @param <T>
   * @return
   * @throws VerifierException
   */
  public static <T extends TypeVerifier> T verify(final Object value, final Object name, final Class<T> type, final MessageFormatter messageFormatter) {
    if (type == null) {
      throw new VerifierException("type must not be null");
    }

    final Verification verification = new Verification(messageFormatter != null ? messageFormatter : defaultMessageFormatter, value, name);
    final T verifier;
    try {
      verifier = type.getConstructor(Verification.class).newInstance(verification);
    } catch (ReflectiveOperationException e) {
      throw new VerifierException("Unable to instantiate TypeVerifier", e);
    }

    return verifier;
  }

  /**
   * TODO: Document
   *
   * @return
   */
  public static MessageFormatter getDefaultMessageFormatter() {
    return defaultMessageFormatter;
  }

  /**
   * TODO: Document
   *
   * @param messageFormatter
   */
  public static void setDefaultMessageFormatter(final MessageFormatter messageFormatter) {
    defaultMessageFormatter = messageFormatter != null ? messageFormatter : new DefaultMessageFormatter();
  }

  Verifier() {
  }
}
