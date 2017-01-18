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
package io.skelp.verifier.type;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import io.skelp.verifier.AbstractCustomVerifierTestCase;
import io.skelp.verifier.CustomVerifierTestCaseBase;

/**
 * <p>
 * Tests for the {@link ThrowableVerifier} class.
 * </p>
 *
 * @author Alasdair Mercer
 */
@RunWith(Enclosed.class)
public class ThrowableVerifierTest {

    public static class ThrowableVerifierAbstractCustomVerifierTest extends AbstractCustomVerifierTestCase<Throwable, ThrowableVerifier> {

        @Override
        protected ThrowableVerifier createCustomVerifier() {
            return new ThrowableVerifier(getMockVerification());
        }

        @Override
        protected Throwable createValueOne() {
            return new CheckedException("foo", null);
        }

        @Override
        protected Throwable createValueTwo() {
            return new CheckedException("bar", null);
        }

        @Override
        protected Class<?> getParentClass() {
            return Exception.class;
        }

        @Override
        protected Class<?> getValueClass() {
            return CheckedException.class;
        }
    }

    public static class ThrowableVerifierMiscTest extends CustomVerifierTestCaseBase<Throwable, ThrowableVerifier> {

        @Test
        public void testCheckedWhenValueIsChecked() {
            testCheckedHelper(new CheckedException(null, null), true);
        }

        @Test
        public void testCheckedWhenValueIsNull() {
            testCheckedHelper(null, false);
        }

        @Test
        public void testCheckedWhenValueIsUnchecked() {
            testCheckedHelper(new UncheckedException(null, null), false);
        }

        private void testCheckedHelper(Throwable value, boolean expected) {
            setValue(value);

            assertSame("Chains reference", getCustomVerifier(), getCustomVerifier().checked());

            verify(getMockVerification()).check(expected, "be checked");
        }

        @Test
        public void testCausedByWithClassWhenValueHasCircularCause() {
            testCausedByHelper(new CheckedException(null, new CircularException()), CircularException.class, true);
        }

        @Test
        public void testCausedByWithClassWhenValueHasCausesWithDifferentType() {
            testCausedByHelper(new CheckedException(null, new NullPointerException()), UncheckedException.class, false);
        }

        @Test
        public void testCausedByWithClassWhenValueHasCausesWithSameType() {
            testCausedByHelper(new CheckedException(null, new UncheckedException(null, null)), UncheckedException.class, true);
        }

        @Test
        public void testCausedByWithClassWhenValueHasDifferentTypeAndNoCause() {
            testCausedByHelper(new CheckedException(null, null), UncheckedException.class, false);
        }

        @Test
        public void testCausedByWithClassWhenValueHasSameTypeAndNoCause() {
            testCausedByHelper(new CheckedException(null, null), CheckedException.class, true);
        }

        @Test
        public void testCausedByWithClassWhenValueIsNull() {
            testCausedByHelper(null, Exception.class, false);
        }

        private void testCausedByHelper(Throwable value, Class<?> type, boolean expected) {
            setValue(value);

            assertSame("Chains reference", getCustomVerifier(), getCustomVerifier().causedBy(type));

            verify(getMockVerification()).check(eq(expected), eq("have been caused by '%s'"), getArgsCaptor().capture());

            assertSame("Passes type for message formatting", type, getArgsCaptor().getValue());
        }

        @Test
        public void testCausedByWithThrowableWhenValueHasCircularCause() {
            Throwable cause = new CircularException();

            testCausedByHelper(new CheckedException(null, cause), cause, true);
        }

        @Test
        public void testCausedByWithThrowableWhenValueHasDifferentCauses() {
            testCausedByHelper(new CheckedException(null, new NullPointerException()), new UncheckedException(null, null), false);
        }

        @Test
        public void testCausedByWithThrowableWhenValueHasSameCause() {
            Throwable cause = new UncheckedException(null, null);

            testCausedByHelper(new CheckedException(null, cause), cause, true);
        }

        @Test
        public void testCausedByWithThrowableWhenValueIsDifferentAndHasNoCause() {
            testCausedByHelper(new CheckedException(null, null), new UncheckedException(null, null), false);
        }

        @Test
        public void testCausedByWithThrowableWhenValueIsSameAndHasNoCause() {
            Throwable cause = new CheckedException(null, null);

            testCausedByHelper(cause, cause, true);
        }

        @Test
        public void testCausedByWithThrowableWhenValueIsNull() {
            testCausedByHelper(null, new Exception(), false);
        }

        private void testCausedByHelper(Throwable value, Throwable cause, boolean expected) {
            setValue(value);

            assertSame("Chains reference", getCustomVerifier(), getCustomVerifier().causedBy(cause));

            verify(getMockVerification()).check(eq(expected), eq("have been caused by '%s'"), getArgsCaptor().capture());

            assertSame("Passes cause for message formatting", cause, getArgsCaptor().getValue());
        }

        @Test
        public void testCausedByWithThrowableAndNameWhenValueHasCircularCause() {
            Throwable cause = new CircularException();

            testCausedByHelper(new CheckedException(null, cause), cause, "cause", true);
        }

        @Test
        public void testCausedByWithThrowableAndNameWhenValueHasDifferentCauses() {
            testCausedByHelper(new CheckedException(null, new NullPointerException()), new UncheckedException(null, null), "cause", false);
        }

        @Test
        public void testCausedByWithThrowableAndNameWhenValueHasSameCause() {
            Throwable cause = new UncheckedException(null, null);

            testCausedByHelper(new CheckedException(null, cause), cause, "cause", true);
        }

        @Test
        public void testCausedByWithThrowableAndNameWhenValueIsDifferentAndHasNoCause() {
            testCausedByHelper(new CheckedException(null, null), new UncheckedException(null, null), "cause", false);
        }

        @Test
        public void testCausedByWithThrowableAndNameWhenValueIsSameAndHasNoCause() {
            Throwable cause = new CheckedException(null, null);

            testCausedByHelper(cause, cause, "cause", true);
        }

        @Test
        public void testCausedByWithThrowableAndNameWhenValueIsNull() {
            testCausedByHelper(null, new Exception(), "cause", false);
        }

        private void testCausedByHelper(Throwable value, Throwable cause, Object name, boolean expected) {
            setValue(value);

            assertSame("Chains reference", getCustomVerifier(), getCustomVerifier().causedBy(cause, name));

            verify(getMockVerification()).check(eq(expected), eq("have been caused by '%s'"), getArgsCaptor().capture());

            assertSame("Passes name for message formatting", name, getArgsCaptor().getValue());
        }

        @Test
        public void testMessageWhenValueHasDifferentMessage() {
            testMessageHelper(new CheckedException("foo", null), "bar", false);
        }

        @Test
        public void testMessageWhenValueIsNull() {
            testMessageHelper(null, "foo", false);
        }

        @Test
        public void testMessageWhenValueHasNullMessageAndMessageIsNotNull() {
            testMessageHelper(new CheckedException(null, null), "foo", false);
        }

        @Test
        public void testMessageWhenValueHasNullMessageAndMessageIsNull() {
            testMessageHelper(new CheckedException(null, null), null, true);
        }

        @Test
        public void testMessageWhenValueHasSameMessage() {
            testMessageHelper(new CheckedException("foo", null), "foo", true);
        }

        private void testMessageHelper(Throwable value, String message, boolean expected) {
            setValue(value);

            assertSame("Chains reference", getCustomVerifier(), getCustomVerifier().message(message));

            verify(getMockVerification()).check(eq(expected), eq("have message '%s'"), getArgsCaptor().capture());

            assertSame("Passes message for message formatting", message, getArgsCaptor().getValue());
        }

        @Test
        public void testUncheckedWhenValueIsChecked() {
            testUncheckedHelper(new CheckedException(null, null), false);
        }

        @Test
        public void testUncheckedWhenValueIsNull() {
            testUncheckedHelper(null, false);
        }

        @Test
        public void testUncheckedWhenValueIsUnchecked() {
            testUncheckedHelper(new UncheckedException(null, null), true);
        }

        private void testUncheckedHelper(Throwable value, boolean expected) {
            setValue(value);

            assertSame("Chains reference", getCustomVerifier(), getCustomVerifier().unchecked());

            verify(getMockVerification()).check(expected, "be unchecked");
        }

        @Override
        protected ThrowableVerifier createCustomVerifier() {
            return new ThrowableVerifier(getMockVerification());
        }
    }

    private static class CheckedException extends Exception {

        CheckedException(String message, Throwable cause) {
            super(message, cause);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (obj == this) {
                return true;
            }
            if (obj.getClass() != getClass()) {
                return false;
            }

            CheckedException other = (CheckedException) obj;
            return getMessage() == null ? other.getMessage() == null : getMessage().equals(other.getMessage());
        }

        @Override
        public int hashCode() {
            return getMessage() == null ? 0 : getMessage().hashCode();
        }
    }

    private static class CircularException extends Exception {

        CircularException() {
            super();
        }

        @Override
        public synchronized Throwable getCause() {
            return this;
        }
    }

    private static class UncheckedException extends RuntimeException {

        UncheckedException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
