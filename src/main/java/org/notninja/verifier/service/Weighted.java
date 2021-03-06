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
package org.notninja.verifier.service;

import java.util.Comparator;

/**
 * <p>
 * An interface intended to be implemented by classes whose instances can have varying priorities, indicated by their
 * weight. Instances with a lower weight than others are considered more important.
 * </p>
 *
 * @author Alasdair Mercer
 * @since 0.2.0
 */
public interface Weighted {

    /**
     * <p>
     * The recommended weight for default implementations. It's high to increase likelihood of other implementations
     * being found.
     * </p>
     */
    int DEFAULT_IMPLEMENTATION_WEIGHT = 1000;

    /**
     * <p>
     * Returns the weight of this {@link Weighted} instance.
     * </p>
     * <p>
     * The lower the weight the greater the importance.
     * </p>
     *
     * @return The weight.
     */
    int getWeight();

    /**
     * <p>
     * A comparator that can be used for comparing {@link Weighted} instances based on their weight.
     * </p>
     */
    class WeightedComparator implements Comparator<Weighted> {

        @Override
        public int compare(final Weighted o1, final Weighted o2) {
            return Comparator.<Integer>naturalOrder().compare(o1.getWeight(), o2.getWeight());
        }
    }
}
