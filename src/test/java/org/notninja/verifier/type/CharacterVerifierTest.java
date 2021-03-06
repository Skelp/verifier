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
package org.notninja.verifier.type;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import org.notninja.verifier.AbstractCustomVerifierTestCase;
import org.notninja.verifier.CustomVerifierTestCaseBase;
import org.notninja.verifier.message.MessageKeyEnumTestCase;
import org.notninja.verifier.type.base.BaseComparableVerifierTestCase;
import org.notninja.verifier.type.base.BaseTruthVerifierTestCase;

/**
 * <p>
 * Tests for the {@link CharacterVerifier} class.
 * </p>
 *
 * @author Alasdair Mercer
 */
@RunWith(Enclosed.class)
public class CharacterVerifierTest {

    public static class CharacterVerifierAbstractCustomVerifierTest extends AbstractCustomVerifierTestCase<Character, CharacterVerifier> {

        @Override
        protected CharacterVerifier createCustomVerifier() {
            return new CharacterVerifier(getMockVerification());
        }

        @Override
        protected Character createValueOne() {
            return new Character('a');
        }

        @Override
        protected Character createValueTwo() {
            return new Character('z');
        }

        @Override
        protected Class<?> getParentClass() {
            return Object.class;
        }

        @Override
        protected Class<?> getValueClass() {
            return Character.class;
        }
    }

    public static class CharacterVerifierBaseComparableVerifierTest extends BaseComparableVerifierTestCase<Character, CharacterVerifier> {

        @Override
        protected CharacterVerifier createCustomVerifier() {
            return new CharacterVerifier(getMockVerification());
        }

        @Override
        protected Character getBaseValue() {
            return 'm';
        }

        @Override
        protected Character getHigherValue() {
            return 't';
        }

        @Override
        protected Character getHighestValue() {
            return 'z';
        }

        @Override
        protected Character getLowerValue() {
            return 'f';
        }

        @Override
        protected Character getLowestValue() {
            return 'a';
        }
    }

    public static class CharacterVerifierBaseTruthVerifierTest extends BaseTruthVerifierTestCase<Character, CharacterVerifier> {

        @Override
        protected CharacterVerifier createCustomVerifier() {
            return new CharacterVerifier(getMockVerification());
        }

        @Override
        protected Character[] getFalsyValues() {
            return new Character[]{'0'};
        }

        @Override
        protected Character[] getTruthyValues() {
            return new Character[]{'1'};
        }
    }

    public static class CharacterVerifierMiscTest extends CustomVerifierTestCaseBase<Character, CharacterVerifier> {

        private static Character[] asciiControls;
        private static Character[] asciiLowerCaseLetters;
        private static Character[] asciiNumbers;
        private static Character[] asciiOtherPrintables;
        private static Character[] asciiUpperCaseLetters;
        private static Character[] nonAsciiLowerCaseLetters;
        private static Character[] nonAsciiNumbers;
        private static Character[] nonAsciiUpperCaseLetters;
        private static Character[] whitespace;

        @BeforeClass
        public static void setUpClass() {
            asciiControls = getAsciiCharacters(input -> input < 32 || input == 127);
            asciiLowerCaseLetters = getAsciiCharacters(input -> input > 96 && input < 123);
            asciiNumbers = getAsciiCharacters(input -> input > 47 && input < 58);
            asciiOtherPrintables = getAsciiCharacters(input -> (input > 31 && input < 48) || (input > 57 && input < 65) || (input > 90 && input < 97) || (input > 122 && input < 127));
            asciiUpperCaseLetters = getAsciiCharacters(input -> input > 64 && input < 91);
            nonAsciiLowerCaseLetters = new Character[]{'é', 'û', 'ÿ'};
            nonAsciiNumbers = new Character[]{'१', '३', '۳'};
            nonAsciiUpperCaseLetters = new Character[]{'É', 'Û', 'Ÿ'};
            whitespace = new Character[]{' ', '\r', '\n', '\t'};
        }

        private static Character[] getAsciiCharacters(Function<Integer, Boolean> matcher) {
            List<Character> characters = new ArrayList<>();
            for (int i = 0; i < 128; i++) {
                if (matcher.apply(i)) {
                    characters.add((char) i);
                }
            }

            return characters.toArray(new Character[0]);
        }

        @Test
        public void testAlphaWhenValueIsAsciiControl() {
            testAlphaHelper(asciiControls, false);
        }

        @Test
        public void testAlphaWhenValueIsAsciiLowerCaseLetter() {
            testAlphaHelper(asciiLowerCaseLetters, true);
        }

        @Test
        public void testAlphaWhenValueIsAsciiNumber() {
            testAlphaHelper(asciiNumbers, false);
        }

        @Test
        public void testAlphaWhenValueIsAsciiUpperCaseLetter() {
            testAlphaHelper(asciiUpperCaseLetters, true);
        }

        @Test
        public void testAlphaWhenValueIsNonAsciiLetter() {
            testAlphaHelper(new Character[]{'é', 'û', 'ÿ'}, true);
        }

        @Test
        public void testAlphaWhenValueIsNonAsciiNumber() {
            testAlphaHelper(new Character[]{'१', '३', '۳'}, false);
        }

        @Test
        public void testAlphaWhenValueIsNull() {
            testAlphaHelper(new Character[]{null}, false);
        }

        @Test
        public void testAlphaWhenValueIsWhitespace() {
            testAlphaHelper(whitespace, false);
        }

        private void testAlphaHelper(Character[] values, boolean expected) {
            for (Character value : values) {
                setValue(value);

                assertSame("Chains reference", getCustomVerifier(), getCustomVerifier().alpha());
            }

            verify(getMockVerification(), times(values.length)).report(expected, CharacterVerifier.MessageKeys.ALPHA);
        }

        @Test
        public void testAlphanumericWhenValueIsAsciiControl() {
            testAlphanumericHelper(asciiControls, false);
        }

        @Test
        public void testAlphanumericWhenValueIsAsciiLowerCaseLetter() {
            testAlphanumericHelper(asciiLowerCaseLetters, true);
        }

        @Test
        public void testAlphanumericWhenValueIsAsciiNumber() {
            testAlphanumericHelper(asciiNumbers, true);
        }

        @Test
        public void testAlphanumericWhenValueIsAsciiUpperCaseLetter() {
            testAlphanumericHelper(asciiUpperCaseLetters, true);
        }

        @Test
        public void testAlphanumericWhenValueIsNonAsciiLetter() {
            testAlphanumericHelper(new Character[]{'é', 'û', 'ÿ'}, true);
        }

        @Test
        public void testAlphanumericWhenValueIsNonAsciiNumber() {
            testAlphanumericHelper(new Character[]{'१', '३', '۳'}, true);
        }

        @Test
        public void testAlphanumericWhenValueIsNull() {
            testAlphanumericHelper(new Character[]{null}, false);
        }

        @Test
        public void testAlphanumericWhenValueIsWhitespace() {
            testAlphanumericHelper(whitespace, false);
        }

        private void testAlphanumericHelper(Character[] values, boolean expected) {
            for (Character value : values) {
                setValue(value);

                assertSame("Chains reference", getCustomVerifier(), getCustomVerifier().alphanumeric());
            }

            verify(getMockVerification(), times(values.length)).report(expected, CharacterVerifier.MessageKeys.ALPHANUMERIC);
        }

        @Test
        public void testAsciiWhenValueIsAsciiControl() {
            testAsciiHelper(asciiControls, true);
        }

        @Test
        public void testAsciiWhenValueIsAsciiLowerCaseLetter() {
            testAsciiHelper(asciiLowerCaseLetters, true);
        }

        @Test
        public void testAsciiWhenValueIsAsciiNumber() {
            testAsciiHelper(asciiNumbers, true);
        }

        @Test
        public void testAsciiWhenValueIsAsciiUpperCaseLetter() {
            testAsciiHelper(asciiUpperCaseLetters, true);
        }

        @Test
        public void testAsciiWhenValueIsNonAsciiLowerCaseLetter() {
            testAsciiHelper(nonAsciiLowerCaseLetters, false);
        }

        @Test
        public void testAsciiWhenValueIsNonAsciiNumber() {
            testAsciiHelper(nonAsciiNumbers, false);
        }

        @Test
        public void testAsciiWhenValueIsNonAsciiUpperCaseLetter() {
            testAsciiHelper(nonAsciiUpperCaseLetters, false);
        }

        @Test
        public void testAsciiWhenValueIsNull() {
            testAsciiHelper(new Character[]{null}, false);
        }

        private void testAsciiHelper(Character[] values, boolean expected) {
            for (Character value : values) {
                setValue(value);

                assertSame("Chains reference", getCustomVerifier(), getCustomVerifier().ascii());
            }

            verify(getMockVerification(), times(values.length)).report(expected, CharacterVerifier.MessageKeys.ASCII);
        }

        @Test
        public void testAsciiAlphaWhenValueIsAsciiControl() {
            testAsciiAlphaHelper(asciiControls, false);
        }

        @Test
        public void testAsciiAlphaWhenValueIsAsciiLowerCaseLetter() {
            testAsciiAlphaHelper(asciiLowerCaseLetters, true);
        }

        @Test
        public void testAsciiAlphaWhenValueIsAsciiNumber() {
            testAsciiAlphaHelper(asciiNumbers, false);
        }

        @Test
        public void testAsciiAlphaWhenValueIsAsciiUpperCaseLetter() {
            testAsciiAlphaHelper(asciiUpperCaseLetters, true);
        }

        @Test
        public void testAsciiAlphaWhenValueIsNonAsciiLowerCaseLetter() {
            testAsciiAlphaHelper(nonAsciiLowerCaseLetters, false);
        }

        @Test
        public void testAsciiAlphaWhenValueIsNonAsciiNumber() {
            testAsciiAlphaHelper(nonAsciiNumbers, false);
        }

        @Test
        public void testAsciiAlphaWhenValueIsNonAsciiUpperCaseLetter() {
            testAsciiAlphaHelper(nonAsciiUpperCaseLetters, false);
        }

        @Test
        public void testAsciiAlphaWhenValueIsNull() {
            testAsciiAlphaHelper(new Character[]{null}, false);
        }

        private void testAsciiAlphaHelper(Character[] values, boolean expected) {
            for (Character value : values) {
                setValue(value);

                assertSame("Chains reference", getCustomVerifier(), getCustomVerifier().asciiAlpha());
            }

            verify(getMockVerification(), times(values.length)).report(expected, CharacterVerifier.MessageKeys.ASCII_ALPHA);
        }

        @Test
        public void testAsciiAlphaLowerCaseWhenValueIsAsciiControl() {
            testAsciiAlphaLowerCaseHelper(asciiControls, false);
        }

        @Test
        public void testAsciiAlphaLowerCaseWhenValueIsAsciiLowerCaseLetter() {
            testAsciiAlphaLowerCaseHelper(asciiLowerCaseLetters, true);
        }

        @Test
        public void testAsciiAlphaLowerCaseWhenValueIsAsciiNumber() {
            testAsciiAlphaLowerCaseHelper(asciiNumbers, false);
        }

        @Test
        public void testAsciiAlphaLowerCaseWhenValueIsAsciiUpperCaseLetter() {
            testAsciiAlphaLowerCaseHelper(asciiUpperCaseLetters, false);
        }

        @Test
        public void testAsciiAlphaLowerCaseWhenValueIsNonAsciiLowerCaseLetter() {
            testAsciiAlphaLowerCaseHelper(nonAsciiLowerCaseLetters, false);
        }

        @Test
        public void testAsciiAlphaLowerCaseWhenValueIsNonAsciiNumber() {
            testAsciiAlphaLowerCaseHelper(nonAsciiNumbers, false);
        }

        @Test
        public void testAsciiAlphaLowerCaseWhenValueIsNonAsciiUpperCaseLetter() {
            testAsciiAlphaLowerCaseHelper(nonAsciiUpperCaseLetters, false);
        }

        @Test
        public void testAsciiAlphaLowerCaseWhenValueIsNull() {
            testAsciiAlphaLowerCaseHelper(new Character[]{null}, false);
        }

        private void testAsciiAlphaLowerCaseHelper(Character[] values, boolean expected) {
            for (Character value : values) {
                setValue(value);

                assertSame("Chains reference", getCustomVerifier(), getCustomVerifier().asciiAlphaLowerCase());
            }

            verify(getMockVerification(), times(values.length)).report(expected, CharacterVerifier.MessageKeys.ASCII_ALPHA_LOWER_CASE);
        }

        @Test
        public void testAsciiAlphaUpperCaseWhenValueIsAsciiControl() {
            testAsciiAlphaUpperCaseHelper(asciiControls, false);
        }

        @Test
        public void testAsciiAlphaUpperCaseWhenValueIsAsciiLowerCaseLetter() {
            testAsciiAlphaUpperCaseHelper(asciiLowerCaseLetters, false);
        }

        @Test
        public void testAsciiAlphaUpperCaseWhenValueIsAsciiNumber() {
            testAsciiAlphaUpperCaseHelper(asciiNumbers, false);
        }

        @Test
        public void testAsciiAlphaUpperCaseWhenValueIsAsciiUpperCaseLetter() {
            testAsciiAlphaUpperCaseHelper(asciiUpperCaseLetters, true);
        }

        @Test
        public void testAsciiAlphaUpperCaseWhenValueIsNonAsciiLowerCaseLetter() {
            testAsciiAlphaUpperCaseHelper(nonAsciiLowerCaseLetters, false);
        }

        @Test
        public void testAsciiAlphaUpperCaseWhenValueIsNonAsciiNumber() {
            testAsciiAlphaUpperCaseHelper(nonAsciiNumbers, false);
        }

        @Test
        public void testAsciiAlphaUpperCaseWhenValueIsNonAsciiUpperCaseLetter() {
            testAsciiAlphaUpperCaseHelper(nonAsciiUpperCaseLetters, false);
        }

        @Test
        public void testAsciiAlphaUpperCaseWhenValueIsNull() {
            testAsciiAlphaUpperCaseHelper(new Character[]{null}, false);
        }

        private void testAsciiAlphaUpperCaseHelper(Character[] values, boolean expected) {
            for (Character value : values) {
                setValue(value);

                assertSame("Chains reference", getCustomVerifier(), getCustomVerifier().asciiAlphaUpperCase());
            }

            verify(getMockVerification(), times(values.length)).report(expected, CharacterVerifier.MessageKeys.ASCII_ALPHA_UPPER_CASE);
        }

        @Test
        public void testAsciiAlphanumericWhenValueIsAsciiControl() {
            testAsciiAlphanumericHelper(asciiControls, false);
        }

        @Test
        public void testAsciiAlphanumericWhenValueIsAsciiLowerCaseLetter() {
            testAsciiAlphanumericHelper(asciiLowerCaseLetters, true);
        }

        @Test
        public void testAsciiAlphanumericWhenValueIsAsciiNumber() {
            testAsciiAlphanumericHelper(asciiNumbers, true);
        }

        @Test
        public void testAsciiAlphanumericWhenValueIsAsciiUpperCaseLetter() {
            testAsciiAlphanumericHelper(asciiUpperCaseLetters, true);
        }

        @Test
        public void testAsciiAlphanumericWhenValueIsNonAsciiLowerCaseLetter() {
            testAsciiAlphanumericHelper(nonAsciiLowerCaseLetters, false);
        }

        @Test
        public void testAsciiAlphanumericWhenValueIsNonAsciiNumber() {
            testAsciiAlphanumericHelper(nonAsciiNumbers, false);
        }

        @Test
        public void testAsciiAlphanumericWhenValueIsNonAsciiUpperCaseLetter() {
            testAsciiAlphanumericHelper(nonAsciiUpperCaseLetters, false);
        }

        @Test
        public void testAsciiAlphanumericWhenValueIsNull() {
            testAsciiAlphanumericHelper(new Character[]{null}, false);
        }

        private void testAsciiAlphanumericHelper(Character[] values, boolean expected) {
            for (Character value : values) {
                setValue(value);

                assertSame("Chains reference", getCustomVerifier(), getCustomVerifier().asciiAlphanumeric());
            }

            verify(getMockVerification(), times(values.length)).report(expected, CharacterVerifier.MessageKeys.ASCII_ALPHANUMERIC);
        }

        @Test
        public void testAsciiControlWhenValueIsAsciiControl() {
            testAsciiControlHelper(asciiControls, true);
        }

        @Test
        public void testAsciiControlWhenValueIsAsciiLowerCaseLetter() {
            testAsciiControlHelper(asciiLowerCaseLetters, false);
        }

        @Test
        public void testAsciiControlWhenValueIsAsciiNumber() {
            testAsciiControlHelper(asciiNumbers, false);
        }

        @Test
        public void testAsciiControlWhenValueIsAsciiUpperCaseLetter() {
            testAsciiControlHelper(asciiUpperCaseLetters, false);
        }

        @Test
        public void testAsciiControlWhenValueIsNonAsciiLowerCaseLetter() {
            testAsciiControlHelper(nonAsciiLowerCaseLetters, false);
        }

        @Test
        public void testAsciiControlWhenValueIsNonAsciiNumber() {
            testAsciiControlHelper(nonAsciiNumbers, false);
        }

        @Test
        public void testAsciiControlWhenValueIsNonAsciiUpperCaseLetter() {
            testAsciiControlHelper(nonAsciiUpperCaseLetters, false);
        }

        @Test
        public void testAsciiControlWhenValueIsNull() {
            testAsciiControlHelper(new Character[]{null}, false);
        }

        private void testAsciiControlHelper(Character[] values, boolean expected) {
            for (Character value : values) {
                setValue(value);

                assertSame("Chains reference", getCustomVerifier(), getCustomVerifier().asciiControl());
            }

            verify(getMockVerification(), times(values.length)).report(expected, CharacterVerifier.MessageKeys.ASCII_CONTROL);
        }

        @Test
        public void testAsciiNumericWhenValueIsAsciiControl() {
            testAsciiNumericHelper(asciiControls, false);
        }

        @Test
        public void testAsciiNumericWhenValueIsAsciiLowerCaseLetter() {
            testAsciiNumericHelper(asciiLowerCaseLetters, false);
        }

        @Test
        public void testAsciiNumericWhenValueIsAsciiNumber() {
            testAsciiNumericHelper(asciiNumbers, true);
        }

        @Test
        public void testAsciiNumericWhenValueIsAsciiUpperCaseLetter() {
            testAsciiNumericHelper(asciiUpperCaseLetters, false);
        }

        @Test
        public void testAsciiNumericWhenValueIsNonAsciiLowerCaseLetter() {
            testAsciiNumericHelper(nonAsciiLowerCaseLetters, false);
        }

        @Test
        public void testAsciiNumericWhenValueIsNonAsciiNumber() {
            testAsciiNumericHelper(nonAsciiNumbers, false);
        }

        @Test
        public void testAsciiNumericWhenValueIsNonAsciiUpperCaseLetter() {
            testAsciiNumericHelper(nonAsciiUpperCaseLetters, false);
        }

        @Test
        public void testAsciiNumericWhenValueIsNull() {
            testAsciiNumericHelper(new Character[]{null}, false);
        }

        private void testAsciiNumericHelper(Character[] values, boolean expected) {
            for (Character value : values) {
                setValue(value);

                assertSame("Chains reference", getCustomVerifier(), getCustomVerifier().asciiNumeric());
            }

            verify(getMockVerification(), times(values.length)).report(expected, CharacterVerifier.MessageKeys.ASCII_NUMERIC);
        }

        @Test
        public void testAsciiPrintableWhenValueIsAsciiControl() {
            testAsciiPrintableHelper(asciiControls, false);
        }

        @Test
        public void testAsciiPrintableWhenValueIsAsciiLowerCaseLetter() {
            testAsciiPrintableHelper(asciiLowerCaseLetters, true);
        }

        @Test
        public void testAsciiPrintableWhenValueIsAsciiNumber() {
            testAsciiPrintableHelper(asciiNumbers, true);
        }

        @Test
        public void testAsciiPrintableWhenValueIsAsciiUpperCaseLetter() {
            testAsciiPrintableHelper(asciiUpperCaseLetters, true);
        }

        @Test
        public void testAsciiPrintableWhenValueIsOtherAsciiPrintable() {
            testAsciiPrintableHelper(asciiOtherPrintables, true);
        }

        @Test
        public void testAsciiPrintableWhenValueIsNonAsciiLowerCaseLetter() {
            testAsciiPrintableHelper(nonAsciiLowerCaseLetters, false);
        }

        @Test
        public void testAsciiPrintableWhenValueIsNonAsciiNumber() {
            testAsciiPrintableHelper(nonAsciiNumbers, false);
        }

        @Test
        public void testAsciiPrintableWhenValueIsNonAsciiUpperCaseLetter() {
            testAsciiPrintableHelper(nonAsciiUpperCaseLetters, false);
        }

        @Test
        public void testAsciiPrintableWhenValueIsNull() {
            testAsciiPrintableHelper(new Character[]{null}, false);
        }

        private void testAsciiPrintableHelper(Character[] values, boolean expected) {
            for (Character value : values) {
                setValue(value);

                assertSame("Chains reference", getCustomVerifier(), getCustomVerifier().asciiPrintable());
            }

            verify(getMockVerification(), times(values.length)).report(expected, CharacterVerifier.MessageKeys.ASCII_PRINTABLE);
        }

        @Test
        public void testLowerCaseWhenValueIsAsciiControl() {
            testLowerCaseHelper(asciiControls, false);
        }

        @Test
        public void testLowerCaseWhenValueIsAsciiLowerCaseLetter() {
            testLowerCaseHelper(asciiLowerCaseLetters, true);
        }

        @Test
        public void testLowerCaseWhenValueIsAsciiNumber() {
            testLowerCaseHelper(asciiNumbers, false);
        }

        @Test
        public void testLowerCaseWhenValueIsAsciiUpperCaseLetter() {
            testLowerCaseHelper(asciiUpperCaseLetters, false);
        }

        @Test
        public void testLowerCaseWhenValueIsNonAsciiLowerCaseLetter() {
            testLowerCaseHelper(nonAsciiLowerCaseLetters, true);
        }

        @Test
        public void testLowerCaseWhenValueIsNonAsciiNumber() {
            testLowerCaseHelper(nonAsciiNumbers, false);
        }

        @Test
        public void testLowerCaseWhenValueIsNonAsciiUpperCaseLetter() {
            testLowerCaseHelper(nonAsciiUpperCaseLetters, false);
        }

        @Test
        public void testLowerCaseWhenValueIsNull() {
            testLowerCaseHelper(new Character[]{null}, false);
        }

        @Test
        public void testLowerCaseWhenValueIsWhitespace() {
            testLowerCaseHelper(whitespace, false);
        }

        private void testLowerCaseHelper(Character[] values, boolean expected) {
            for (Character value : values) {
                setValue(value);

                assertSame("Chains reference", getCustomVerifier(), getCustomVerifier().lowerCase());
            }

            verify(getMockVerification(), times(values.length)).report(expected, CharacterVerifier.MessageKeys.LOWER_CASE);
        }

        @Test
        public void testNumericWhenValueIsAsciiControl() {
            testNumericHelper(asciiControls, false);
        }

        @Test
        public void testNumericWhenValueIsAsciiLowerCaseLetter() {
            testNumericHelper(asciiLowerCaseLetters, false);
        }

        @Test
        public void testNumericWhenValueIsAsciiNumber() {
            testNumericHelper(asciiNumbers, true);
        }

        @Test
        public void testNumericWhenValueIsAsciiUpperCaseLetter() {
            testNumericHelper(asciiUpperCaseLetters, false);
        }

        @Test
        public void testNumericWhenValueIsNonAsciiLowerCaseLetter() {
            testNumericHelper(nonAsciiLowerCaseLetters, false);
        }

        @Test
        public void testNumericWhenValueIsNonAsciiNumber() {
            testNumericHelper(nonAsciiNumbers, true);
        }

        @Test
        public void testNumericWhenValueIsNonAsciiUpperCaseLetter() {
            testNumericHelper(nonAsciiUpperCaseLetters, false);
        }

        @Test
        public void testNumericWhenValueIsNull() {
            testNumericHelper(new Character[]{null}, false);
        }

        @Test
        public void testNumericWhenValueIsWhitespace() {
            testNumericHelper(whitespace, false);
        }

        private void testNumericHelper(Character[] values, boolean expected) {
            for (Character value : values) {
                setValue(value);

                assertSame("Chains reference", getCustomVerifier(), getCustomVerifier().numeric());
            }

            verify(getMockVerification(), times(values.length)).report(expected, CharacterVerifier.MessageKeys.NUMERIC);
        }

        @Test
        public void testUpperCaseWhenValueIsAsciiControl() {
            testUpperCaseHelper(asciiControls, false);
        }

        @Test
        public void testUpperCaseWhenValueIsAsciiLowerCaseLetter() {
            testUpperCaseHelper(asciiLowerCaseLetters, false);
        }

        @Test
        public void testUpperCaseWhenValueIsAsciiNumber() {
            testUpperCaseHelper(asciiNumbers, false);
        }

        @Test
        public void testUpperCaseWhenValueIsAsciiUpperCaseLetter() {
            testUpperCaseHelper(asciiUpperCaseLetters, true);
        }

        @Test
        public void testUpperCaseWhenValueIsNonAsciiLowerCaseLetter() {
            testUpperCaseHelper(nonAsciiLowerCaseLetters, false);
        }

        @Test
        public void testUpperCaseWhenValueIsNonAsciiNumber() {
            testUpperCaseHelper(nonAsciiNumbers, false);
        }

        @Test
        public void testUpperCaseWhenValueIsNonAsciiUpperCaseLetter() {
            testUpperCaseHelper(nonAsciiUpperCaseLetters, true);
        }

        @Test
        public void testUpperCaseWhenValueIsNull() {
            testUpperCaseHelper(new Character[]{null}, false);
        }

        @Test
        public void testUpperCaseWhenValueIsWhitespace() {
            testUpperCaseHelper(whitespace, false);
        }

        private void testUpperCaseHelper(Character[] values, boolean expected) {
            for (Character value : values) {
                setValue(value);

                assertSame("Chains reference", getCustomVerifier(), getCustomVerifier().upperCase());
            }

            verify(getMockVerification(), times(values.length)).report(expected, CharacterVerifier.MessageKeys.UPPER_CASE);
        }

        @Test
        public void testWhitespaceWhenValueIsAsciiLowerCaseLetter() {
            testWhitespaceHelper(asciiLowerCaseLetters, false);
        }

        @Test
        public void testWhitespaceWhenValueIsAsciiNumber() {
            testWhitespaceHelper(asciiNumbers, false);
        }

        @Test
        public void testWhitespaceWhenValueIsAsciiUpperCaseLetter() {
            testWhitespaceHelper(asciiUpperCaseLetters, false);
        }

        @Test
        public void testWhitespaceWhenValueIsNonAsciiLowerCaseLetter() {
            testWhitespaceHelper(nonAsciiLowerCaseLetters, false);
        }

        @Test
        public void testWhitespaceWhenValueIsNonAsciiNumber() {
            testWhitespaceHelper(nonAsciiNumbers, false);
        }

        @Test
        public void testWhitespaceWhenValueIsNonAsciiUpperCaseLetter() {
            testWhitespaceHelper(nonAsciiUpperCaseLetters, false);
        }

        @Test
        public void testWhitespaceWhenValueIsNull() {
            testWhitespaceHelper(new Character[]{null}, false);
        }

        @Test
        public void testWhitespaceWhenValueIsWhitespace() {
            testWhitespaceHelper(whitespace, true);
        }

        private void testWhitespaceHelper(Character[] values, boolean expected) {
            for (Character value : values) {
                setValue(value);

                assertSame("Chains reference", getCustomVerifier(), getCustomVerifier().whitespace());
            }

            verify(getMockVerification(), times(values.length)).report(expected, CharacterVerifier.MessageKeys.WHITESPACE);
        }

        @Override
        protected CharacterVerifier createCustomVerifier() {
            return new CharacterVerifier(getMockVerification());
        }
    }

    public static class CharacterVerifierMessageKeysTest extends MessageKeyEnumTestCase<CharacterVerifier.MessageKeys> {

        @Override
        protected Class<? extends Enum> getEnumClass() {
            return CharacterVerifier.MessageKeys.class;
        }

        @Override
        protected Map<String, String> getMessageKeys() {
            Map<String, String> messageKeys = new HashMap<>();
            messageKeys.put("ALPHA", "org.notninja.verifier.type.CharacterVerifier.alpha");
            messageKeys.put("ALPHANUMERIC", "org.notninja.verifier.type.CharacterVerifier.alphanumeric");
            messageKeys.put("ASCII", "org.notninja.verifier.type.CharacterVerifier.ascii");
            messageKeys.put("ASCII_ALPHA", "org.notninja.verifier.type.CharacterVerifier.asciiAlpha");
            messageKeys.put("ASCII_ALPHA_LOWER_CASE", "org.notninja.verifier.type.CharacterVerifier.asciiAlphaLowerCase");
            messageKeys.put("ASCII_ALPHA_UPPER_CASE", "org.notninja.verifier.type.CharacterVerifier.asciiAlphaUpperCase");
            messageKeys.put("ASCII_ALPHANUMERIC", "org.notninja.verifier.type.CharacterVerifier.asciiAlphanumeric");
            messageKeys.put("ASCII_CONTROL", "org.notninja.verifier.type.CharacterVerifier.asciiControl");
            messageKeys.put("ASCII_NUMERIC", "org.notninja.verifier.type.CharacterVerifier.asciiNumeric");
            messageKeys.put("ASCII_PRINTABLE", "org.notninja.verifier.type.CharacterVerifier.asciiPrintable");
            messageKeys.put("LOWER_CASE", "org.notninja.verifier.type.CharacterVerifier.lowerCase");
            messageKeys.put("NUMERIC", "org.notninja.verifier.type.CharacterVerifier.numeric");
            messageKeys.put("UPPER_CASE", "org.notninja.verifier.type.CharacterVerifier.upperCase");
            messageKeys.put("WHITESPACE", "org.notninja.verifier.type.CharacterVerifier.whitespace");

            return messageKeys;
        }
    }
}
