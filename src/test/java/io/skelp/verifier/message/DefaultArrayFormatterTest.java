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
package io.skelp.verifier.message;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * <p>
 * Tests for the {@link DefaultArrayFormatter} class.
 * </p>
 *
 * @author Alasdair Mercer
 */
public class DefaultArrayFormatterTest {

    @Test
    public void testFormat() {
        String expected = "['1', '2', '3']";
        String actual = new DefaultArrayFormatter<>(new Integer[]{1, 2, 3}).format();

        assertEquals("Formats array", expected, actual);
    }

    @Test
    public void testFormatWhenArrayContainsCircularReference() {
        Object[] array = new Object[3];
        array[0] = 1;
        array[1] = new Integer[]{2, 3, 4};
        array[2] = array;

        String expected = "['1', ['2', '3', '4'], {Circular}]";
        String actual = new DefaultArrayFormatter<>(array).format();

        assertEquals("Formats array containing circular reference", expected, actual);
    }

    @Test
    public void testFormatWhenArrayContainsNull() {
        String expected = "['1', null, '3']";
        String actual = new DefaultArrayFormatter<>(new Integer[]{1, null, 3}).format();

        assertEquals("Formats array containing null", expected, actual);
    }

    @Test
    public void testFormatWhenArrayHasSingleItem() {
        String expected = "['1']";
        String actual = new DefaultArrayFormatter<>(new Integer[]{1}).format();

        assertEquals("Formats array with single item", expected, actual);
    }

    @Test
    public void testFormatWhenArrayIsDeep() {
        String expected = "[['1', '2', '3'], ['4', '5', '6'], ['7', '8', '9']]";
        String actual = new DefaultArrayFormatter<>(new Integer[][]{new Integer[]{1, 2, 3}, new Integer[]{4, 5, 6}, new Integer[]{7, 8, 9}}).format();

        assertEquals("Formats deep array", expected, actual);
    }

    @Test
    public void testFormatWhenArrayIsDeepAndContainsCircularReference() {
        Object[] array = new Object[3];
        array[0] = 1;
        array[1] = new Integer[]{2, 3, 4};

        Object[] nestedArray = new Object[2];
        nestedArray[0] = 5;
        nestedArray[1] = array;

        array[2] = nestedArray;

        String expected = "['1', ['2', '3', '4'], ['5', {Circular}]]";
        String actual = new DefaultArrayFormatter<>(array).format();

        assertEquals("Formats deep array containing circular reference", expected, actual);
    }

    @Test
    public void testFormatWhenArrayIsEmpty() {
        String expected = "[]";
        String actual = new DefaultArrayFormatter<>(new Object[0]).format();

        assertEquals("Formats empty array", expected, actual);
    }

    @Test
    public void testFormatWhenArrayIsNull() {
        String expected = "null";
        String actual = new DefaultArrayFormatter<>(null).format();

        assertEquals("Formats null array", expected, actual);
    }

    @Test
    public void testToString() {
        DefaultArrayFormatter<Integer> formatter = new DefaultArrayFormatter<>(new Integer[]{1, 2, 3});
        String expected = formatter.format();
        String actual = formatter.toString();

        assertEquals("Formats array", expected, actual);
    }
}
