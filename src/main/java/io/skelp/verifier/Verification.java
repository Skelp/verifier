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

import io.skelp.verifier.message.MessageFormatter;

/**
 * TODO: Document
 *
 * @author Alasdair Mercer
 */
public class Verification {

  private final MessageFormatter messageFormatter;
  private final Object name;
  private boolean negated;
  private final Object value;

  /**
   * TODO: Document
   *
   * @param messageFormatter
   * @param value
   * @param name
   */
  public Verification(final MessageFormatter messageFormatter, final Object value, final Object name) {
    this.messageFormatter = messageFormatter;
    this.value = value;
    this.name = name;
  }

  /**
   * TODO: Document
   *
   * @param result
   * @param message
   * @param args
   * @return
   * @throws VerifierException
   */
  public Verification check(final boolean result, final String message, final Object... args) {
    if (result && negated || !result && !negated) {
      throw new VerifierException(messageFormatter.format(this, message, args));
    }

    negated = false;

    return this;
  }

  /**
   * TODO: Document
   *
   * @return
   */
  public MessageFormatter getMessageFormatter() {
    return messageFormatter;
  }

  /**
   * TODO: Document
   *
   * @return
   */
  public Object getName() {
    return name;
  }

  /**
   * TODO: Document
   *
   * @return
   */
  public boolean isNegated() {
    return negated;
  }

  /**
   * TODO: Document
   *
   * @param negated
   */
  public void setNegated(final boolean negated) {
    this.negated = negated;
  }

  /**
   * TODO: Document
   *
   * @return
   */
  public Object getValue() {
    return value;
  }
}
