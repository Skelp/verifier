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

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import org.notninja.verifier.AbstractCustomVerifierTestCase;
import org.notninja.verifier.CustomVerifierTestCaseBase;
import org.notninja.verifier.message.MessageKeyEnumTestCase;

/**
 * <p>
 * Tests for the {@link ClassVerifier} class.
 * </p>
 *
 * @author Alasdair Mercer
 */
@RunWith(Enclosed.class)
public class ClassVerifierTest {

    private static final Class<?>[] PRIMITIVES = {Boolean.TYPE, Byte.TYPE, Character.TYPE, Double.TYPE, Float.TYPE, Integer.TYPE, Long.TYPE, Short.TYPE, Void.TYPE};
    private static final Class<?>[] PRIMITIVE_WRAPPERS = {Boolean.class, Byte.class, Character.class, Double.class, Float.class, Integer.class, Long.class, Short.class, Void.TYPE};

    public static class ClassVerifierAbstractCustomVerifierTest extends AbstractCustomVerifierTestCase<Class, ClassVerifier> {

        @Override
        protected ClassVerifier createCustomVerifier() {
            return new ClassVerifier(getMockVerification());
        }

        @Override
        protected Class createValueOne() {
            return AnnotationOne.class;
        }

        @Override
        protected Class createValueTwo() {
            return AnnotationTwo.class;
        }

        @Override
        protected boolean isEqualValueSame() {
            return true;
        }

        @Override
        protected Class<?> getParentClass() {
            return Object.class;
        }

        @Override
        protected Class<?> getValueClass() {
            return Class.class;
        }
    }

    public static class ClassVerifierMiscTest extends CustomVerifierTestCaseBase<Class, ClassVerifier> {

        @Override
        protected ClassVerifier createCustomVerifier() {
            return new ClassVerifier(getMockVerification());
        }

        @Test
        public void testAnnotatedWhenValueIsAnnotated() {
            testAnnotatedHelper(TypeWithAnnotationOne.class, true);
        }

        @Test
        public void testAnnotatedWhenValueIsNotAnnotated() {
            testAnnotatedHelper(TypeWithNoAnnotations.class, false);
        }

        @Test
        public void testAnnotatedWhenValueIsNull() {
            testAnnotatedHelper(null, false);
        }

        private void testAnnotatedHelper(Class<?> value, boolean expected) {
            setValue(value);

            assertSame("Chains reference", getCustomVerifier(), getCustomVerifier().annotated());

            verify(getMockVerification()).report(expected, ClassVerifier.MessageKeys.ANNOTATED);
        }

        @Test
        public void testAnnotatedWithWhenTypeIsNull() {
            testAnnotatedWithHelper(TypeWithAnnotationOne.class, null, false);
        }

        @Test
        public void testAnnotatedWithWhenValueIsAnnotatedWithDifferentAnnotation() {
            testAnnotatedWithHelper(TypeWithAnnotationOne.class, AnnotationTwo.class, false);
        }

        @Test
        public void testAnnotatedWithWhenValueIsAnnotatedWithMultipleAnnotations() {
            testAnnotatedWithHelper(TypeWithAnnotationOneAndTwo.class, AnnotationTwo.class, true);
        }

        @Test
        public void testAnnotatedWithWhenValueIsAnnotatedWithNoAnnotations() {
            testAnnotatedWithHelper(TypeWithNoAnnotations.class, AnnotationOne.class, false);
        }

        @Test
        public void testAnnotatedWithWhenValueIsAnnotatedWithSameAnnotation() {
            testAnnotatedWithHelper(TypeWithAnnotationOne.class, AnnotationOne.class, true);
        }

        @Test
        public void testAnnotatedWithWhenValueIsNull() {
            testAnnotatedWithHelper(null, AnnotationOne.class, false);
        }

        private void testAnnotatedWithHelper(Class<?> value, Class<? extends Annotation> type, boolean expected) {
            setValue(value);

            assertSame("Chains reference", getCustomVerifier(), getCustomVerifier().annotatedWith(type));

            verify(getMockVerification()).report(eq(expected), eq(ClassVerifier.MessageKeys.ANNOTATED_WITH), getArgsCaptor().capture());

            assertSame("Passes type for message formatting", type, getArgsCaptor().getValue());
        }

        @Test
        public void testAnnotatedWithAllWhenNoTypes() {
            testAnnotatedWithAllHelper(TypeWithAnnotationOneAndTwo.class, createEmptyArray(Class.class), true);
        }

        @Test
        public void testAnnotatedWithAllWhenNoTypesAndValueHasNoAnnotations() {
            testAnnotatedWithAllHelper(TypeWithNoAnnotations.class, createEmptyArray(Class.class), true);
        }

        @Test
        public void testAnnotatedWithAllWhenTypeIsNull() {
            testAnnotatedWithAllHelper(TypeWithAnnotationOne.class, createArray((Class<? extends Annotation>) null), false);
        }

        @Test
        public void testAnnotatedWithAllWhenTypesIsNull() {
            testAnnotatedWithAllHelper(TypeWithNoAnnotations.class, null, true);
        }

        @Test
        public void testAnnotatedWithAllWhenValueContainsAllTypes() {
            testAnnotatedWithAllHelper(TypeWithAnnotationOneAndTwo.class, createArray(AnnotationOne.class, AnnotationTwo.class), true);
        }

        @Test
        public void testAnnotatedWithAllWhenValueContainsSomeTypes() {
            testAnnotatedWithAllHelper(TypeWithAnnotationOne.class, createArray(AnnotationOne.class, AnnotationTwo.class), false);
        }

        @Test
        public void testAnnotatedWithAllWhenValueDoesNotContainType() {
            testAnnotatedWithAllHelper(TypeWithNoAnnotations.class, createArray(AnnotationOne.class, AnnotationTwo.class), false);
        }

        @Test
        public void testAnnotatedWithAllWhenValueIsNull() {
            testAnnotatedWithAllHelper(null, createArray(AnnotationOne.class), false);
        }

        @Test
        public void testAnnotatedWithAllWhenValueHasNoAnnotations() {
            testAnnotatedWithAllHelper(TypeWithNoAnnotations.class, createArray(AnnotationOne.class), false);
        }

        private void testAnnotatedWithAllHelper(Class<?> value, Class<? extends Annotation>[] types, boolean expected) {
            setValue(value);

            assertSame("Chains reference", getCustomVerifier(), getCustomVerifier().annotatedWithAll(types));

            verify(getMockVerification()).report(expected, ClassVerifier.MessageKeys.ANNOTATED_WITH_ALL, (Object) types);
        }

        @Test
        public void testAnnotatedWithAnyWhenNoTypes() {
            testAnnotatedWithAnyHelper(TypeWithAnnotationOneAndTwo.class, createEmptyArray(Class.class), false);
        }

        @Test
        public void testAnnotatedWithAnyWhenNoTypesAndValueHasNoAnnotations() {
            testAnnotatedWithAnyHelper(TypeWithNoAnnotations.class, createEmptyArray(Class.class), false);
        }

        @Test
        public void testAnnotatedWithAnyWhenTypeIsNull() {
            testAnnotatedWithAnyHelper(TypeWithAnnotationOne.class, createArray((Class<? extends Annotation>) null), false);
        }

        @Test
        public void testAnnotatedWithAnyWhenTypesIsNull() {
            testAnnotatedWithAnyHelper(TypeWithNoAnnotations.class, null, false);
        }

        @Test
        public void testAnnotatedWithAnyWhenValueContainsAllTypes() {
            testAnnotatedWithAnyHelper(TypeWithAnnotationOneAndTwo.class, createArray(AnnotationOne.class, AnnotationTwo.class), true);
        }

        @Test
        public void testAnnotatedWithAnyWhenValueContainsSomeTypes() {
            testAnnotatedWithAnyHelper(TypeWithAnnotationOne.class, createArray(AnnotationTwo.class, AnnotationOne.class), true);
        }

        @Test
        public void testAnnotatedWithAnyWhenValueDoesNotContainType() {
            testAnnotatedWithAnyHelper(TypeWithNoAnnotations.class, createArray(AnnotationOne.class, AnnotationTwo.class), false);
        }

        @Test
        public void testAnnotatedWithAnyWhenValueIsNull() {
            testAnnotatedWithAnyHelper(null, createArray(AnnotationOne.class), false);
        }

        @Test
        public void testAnnotatedWithAnyWhenValueHasNoAnnotations() {
            testAnnotatedWithAnyHelper(TypeWithNoAnnotations.class, createArray(AnnotationOne.class), false);
        }

        private void testAnnotatedWithAnyHelper(Class<?> value, Class<? extends Annotation>[] types, boolean expected) {
            setValue(value);

            assertSame("Chains reference", getCustomVerifier(), getCustomVerifier().annotatedWithAny(types));

            verify(getMockVerification()).report(expected, ClassVerifier.MessageKeys.ANNOTATED_WITH_ANY, (Object) types);
        }

        @Test
        public void testAnnotationWhenValueIsAnnotation() {
            testAnnotationHelper(AnnotationOne.class, true);
        }

        @Test
        public void testAnnotationWhenValueIsNotAnnotation() {
            testAnnotationHelper(TypeWithAnnotationOne.class, false);
        }

        @Test
        public void testAnnotationWhenValueIsNull() {
            testAnnotationHelper(null, false);
        }

        private void testAnnotationHelper(Class<?> value, boolean expected) {
            setValue(value);

            assertSame("Chains reference", getCustomVerifier(), getCustomVerifier().annotation());

            verify(getMockVerification()).report(expected, ClassVerifier.MessageKeys.ANNOTATION);
        }

        @Test
        public void testAnonymousWhenValueIsAnonymous() {
            testAnonymousHelper(new AnInterface() {
            }.getClass(), true);
        }

        @Test
        public void testAnonymousWhenValueIsNotAnonymous() {
            testAnonymousHelper(ClassVerifierTest.class, false);
        }

        @Test
        public void testAnonymousWhenValueIsNull() {
            testAnonymousHelper(null, false);
        }

        private void testAnonymousHelper(Class<?> value, boolean expected) {
            setValue(value);

            assertSame("Chains reference", getCustomVerifier(), getCustomVerifier().anonymous());

            verify(getMockVerification()).report(expected, ClassVerifier.MessageKeys.ANONYMOUS);
        }

        @Test
        public void testArrayWhenValueIsArray() {
            testArrayHelper(Object[].class, true);
        }

        @Test
        public void testArrayWhenValueIsNotArray() {
            testArrayHelper(ClassVerifierTest.class, false);
        }

        @Test
        public void testArrayWhenValueIsNull() {
            testArrayHelper(null, false);
        }

        private void testArrayHelper(Class<?> value, boolean expected) {
            setValue(value);

            assertSame("Chains reference", getCustomVerifier(), getCustomVerifier().array());

            verify(getMockVerification()).report(expected, ClassVerifier.MessageKeys.ARRAY);
        }

        @Test
        public void testAssignableFromWhenTypeIsNull() {
            testAssignableFromHelper(Object.class, null, false);
        }

        @Test
        public void testAssignableFromWhenValueIsAssignableFromType() {
            testAssignableFromHelper(Object.class, ClassVerifierTest.class, true);
        }

        @Test
        public void testAssignableFromWhenValueIsNotAssignableFromType() {
            testAssignableFromHelper(ClassVerifierTest.class, Object.class, false);
        }

        @Test
        public void testAssignableFromWhenValueIsNull() {
            testAssignableFromHelper(null, Object.class, false);
        }

        @Test
        public void testAssignableFromWhenValueIsType() {
            testAssignableFromHelper(ClassVerifierTest.class, ClassVerifierTest.class, true);
        }

        private void testAssignableFromHelper(Class<?> value, Class<?> type, boolean expected) {
            setValue(value);

            assertSame("Chains reference", getCustomVerifier(), getCustomVerifier().assignableFrom(type));

            verify(getMockVerification()).report(eq(expected), eq(ClassVerifier.MessageKeys.ASSIGNABLE_FROM), getArgsCaptor().capture());

            assertSame("Passes type for message formatting", type, getArgsCaptor().getValue());
        }

        @Test
        public void testAssignableFromAllWhenNoTypes() {
            testAssignableFromAllHelper(Collection.class, createEmptyArray(Class.class), true);
        }

        @Test
        public void testAssignableFromAllWhenTypeIsNull() {
            testAssignableFromAllHelper(Collection.class, createArray((Class) null), false);
        }

        @Test
        public void testAssignableFromAllWhenTypesIsNull() {
            testAssignableFromAllHelper(Collection.class, null, true);
        }

        @Test
        public void testAssignableFromAllWhenValueIsAssignableFromAllTypes() {
            testAssignableFromAllHelper(Collection.class, createArray(List.class, Set.class, ArrayList.class), true);
        }

        @Test
        public void testAssignableFromAllWhenValueIsAssignableFromSomeTypes() {
            testAssignableFromAllHelper(Collection.class, createArray(List.class, Map.class, ArrayList.class), false);
        }

        @Test
        public void testAssignableFromAllWhenValueIsNotAssignableFromAnyType() {
            testAssignableFromAllHelper(Collection.class, createArray(Boolean.class, Map.class, URI.class), false);
        }

        @Test
        public void testAssignableFromAllWhenValueIsNull() {
            testAssignableFromAllHelper(null, createArray(Object.class), false);
        }

        private void testAssignableFromAllHelper(Class<?> value, Class<?>[] types, boolean expected) {
            setValue(value);

            assertSame("Chains reference", getCustomVerifier(), getCustomVerifier().assignableFromAll(types));

            verify(getMockVerification()).report(expected, ClassVerifier.MessageKeys.ASSIGNABLE_FROM_ALL, (Object) types);
        }

        @Test
        public void testAssignableFromAnyWhenNoTypes() {
            testAssignableFromAnyHelper(Collection.class, createEmptyArray(Class.class), false);
        }

        @Test
        public void testAssignableFromAnyWhenTypeIsNull() {
            testAssignableFromAnyHelper(Collection.class, createArray((Class) null), false);
        }

        @Test
        public void testAssignableFromAnyWhenTypesIsNull() {
            testAssignableFromAnyHelper(Collection.class, null, false);
        }

        @Test
        public void testAssignableFromAnyWhenValueIsAssignableFromAllTypes() {
            testAssignableFromAnyHelper(Collection.class, createArray(List.class, Set.class, ArrayList.class), true);
        }

        @Test
        public void testAssignableFromAnyWhenValueIsAssignableFromSomeTypes() {
            testAssignableFromAnyHelper(Collection.class, createArray(List.class, Map.class, ArrayList.class), true);
        }

        @Test
        public void testAssignableFromAnyWhenValueIsNotAssignableFromAnyType() {
            testAssignableFromAnyHelper(Collection.class, createArray(Boolean.class, Map.class, URI.class), false);
        }

        @Test
        public void testAssignableFromAnyWhenValueIsNull() {
            testAssignableFromAnyHelper(null, createArray(Object.class), false);
        }

        private void testAssignableFromAnyHelper(Class<?> value, Class<?>[] types, boolean expected) {
            setValue(value);

            assertSame("Chains reference", getCustomVerifier(), getCustomVerifier().assignableFromAny(types));

            verify(getMockVerification()).report(expected, ClassVerifier.MessageKeys.ASSIGNABLE_FROM_ANY, (Object) types);
        }

        @Test
        public void testEnumerationWhenValueIsEnum() {
            testEnumerationHelper(AnEnum.class, true);
        }

        @Test
        public void testEnumerationWhenValueIsNotEnum() {
            testEnumerationHelper(ClassVerifierTest.class, false);
        }

        @Test
        public void testEnumerationWhenValueIsNull() {
            testEnumerationHelper(null, false);
        }

        private void testEnumerationHelper(Class<?> value, boolean expected) {
            setValue(value);

            assertSame("Chains reference", getCustomVerifier(), getCustomVerifier().enumeration());

            verify(getMockVerification()).report(expected, ClassVerifier.MessageKeys.ENUMERATION);
        }

        @Test
        public void testInterfacedWhenValueIsInterface() {
            testInterfacedHelper(AnInterface.class, true);
        }

        @Test
        public void testInterfacedWhenValueIsNotInterface() {
            testInterfacedHelper(ClassVerifierTest.class, false);
        }

        @Test
        public void testInterfacedWhenValueIsNull() {
            testInterfacedHelper(null, false);
        }

        private void testInterfacedHelper(Class<?> value, boolean expected) {
            setValue(value);

            assertSame("Chains reference", getCustomVerifier(), getCustomVerifier().interfaced());

            verify(getMockVerification()).report(expected, ClassVerifier.MessageKeys.INTERFACED);
        }

        @Test
        public void testNestedWhenValueIsNested() {
            testNestedHelper(TypeWithNoAnnotations.class, true);
        }

        @Test
        public void testNestedWhenValueIsNotNested() {
            testNestedHelper(ClassVerifierTest.class, false);
        }

        @Test
        public void testNestedWhenValueIsNull() {
            testNestedHelper(null, false);
        }

        private void testNestedHelper(Class<?> value, boolean expected) {
            setValue(value);

            assertSame("Chains reference", getCustomVerifier(), getCustomVerifier().nested());

            verify(getMockVerification()).report(expected, ClassVerifier.MessageKeys.NESTED);
        }

        @Test
        public void testPrimitiveWhenValueIsNotPrimitiveOrWrapper() {
            testPrimitiveHelper(new Class<?>[]{Object.class}, false);
        }

        @Test
        public void testPrimitiveWhenValueIsNull() {
            testPrimitiveHelper(new Class<?>[]{null}, false);
        }

        @Test
        public void testPrimitiveWhenValueIsPrimitive() {
            testPrimitiveHelper(PRIMITIVES, true);
        }

        @Test
        public void testPrimitiveWhenValueIsPrimitiveWrapper() {
            List<Class<?>> primitiveWrappers = new ArrayList<>(Arrays.asList(PRIMITIVE_WRAPPERS));
            primitiveWrappers.remove(Void.TYPE);

            testPrimitiveHelper(primitiveWrappers.toArray(new Class<?>[0]), false);
        }

        private void testPrimitiveHelper(Class<?>[] values, boolean expected) {
            for (Class<?> value : values) {
                setValue(value);

                assertSame("Chains reference", getCustomVerifier(), getCustomVerifier().primitive());
            }

            verify(getMockVerification(), times(values.length)).report(expected, ClassVerifier.MessageKeys.PRIMITIVE);
        }

        @Test
        public void testPrimitiveOrWrapperWhenValueIsNotPrimitiveOrWrapper() {
            testPrimitiveOrWrapperHelper(new Class<?>[]{Object.class}, false);
        }

        @Test
        public void testPrimitiveOrWrapperWhenValueIsNull() {
            testPrimitiveOrWrapperHelper(new Class<?>[]{null}, false);
        }

        @Test
        public void testPrimitiveOrWrapperWhenValueIsPrimitive() {
            testPrimitiveOrWrapperHelper(PRIMITIVES, true);
        }

        @Test
        public void testPrimitiveOrWrapperWhenValueIsPrimitiveWrapper() {
            testPrimitiveOrWrapperHelper(PRIMITIVE_WRAPPERS, true);
        }

        private void testPrimitiveOrWrapperHelper(Class<?>[] values, boolean expected) {
            for (Class<?> value : values) {
                setValue(value);

                assertSame("Chains reference", getCustomVerifier(), getCustomVerifier().primitiveOrWrapper());
            }

            verify(getMockVerification(), times(values.length)).report(expected, ClassVerifier.MessageKeys.PRIMITIVE_OR_WRAPPER);
        }

        @Test
        public void testPrimitiveWrapperWhenValueIsNotPrimitiveOrWrapper() {
            testPrimitiveWrapperHelper(new Class<?>[]{Object.class}, false);
        }

        @Test
        public void testPrimitiveWrapperWhenValueIsNull() {
            testPrimitiveWrapperHelper(new Class<?>[]{null}, false);
        }

        @Test
        public void testPrimitiveWrapperWhenValueIsPrimitive() {
            List<Class<?>> primitives = new ArrayList<>(Arrays.asList(PRIMITIVES));
            primitives.remove(Void.TYPE);

            testPrimitiveWrapperHelper(primitives.toArray(new Class<?>[0]), false);
        }

        @Test
        public void testPrimitiveWrapperWhenValueIsPrimitiveWrapper() {
            testPrimitiveWrapperHelper(PRIMITIVE_WRAPPERS, true);
        }

        private void testPrimitiveWrapperHelper(Class<?>[] values, boolean expected) {
            for (Class<?> value : values) {
                setValue(value);

                assertSame("Chains reference", getCustomVerifier(), getCustomVerifier().primitiveWrapper());
            }

            verify(getMockVerification(), times(values.length)).report(expected, ClassVerifier.MessageKeys.PRIMITIVE_WRAPPER);
        }
    }

    public static class ClassVerifierMessageKeysTest extends MessageKeyEnumTestCase<ClassVerifier.MessageKeys> {

        @Override
        protected Class<? extends Enum> getEnumClass() {
            return ClassVerifier.MessageKeys.class;
        }

        @Override
        protected Map<String, String> getMessageKeys() {
            Map<String, String> messageKeys = new HashMap<>();
            messageKeys.put("ANNOTATED", "org.notninja.verifier.type.ClassVerifier.annotated");
            messageKeys.put("ANNOTATED_WITH", "org.notninja.verifier.type.ClassVerifier.annotatedWith");
            messageKeys.put("ANNOTATED_WITH_ALL", "org.notninja.verifier.type.ClassVerifier.annotatedWithAll");
            messageKeys.put("ANNOTATED_WITH_ANY", "org.notninja.verifier.type.ClassVerifier.annotatedWithAny");
            messageKeys.put("ANNOTATION", "org.notninja.verifier.type.ClassVerifier.annotation");
            messageKeys.put("ANONYMOUS", "org.notninja.verifier.type.ClassVerifier.anonymous");
            messageKeys.put("ARRAY", "org.notninja.verifier.type.ClassVerifier.array");
            messageKeys.put("ASSIGNABLE_FROM", "org.notninja.verifier.type.ClassVerifier.assignableFrom");
            messageKeys.put("ASSIGNABLE_FROM_ALL", "org.notninja.verifier.type.ClassVerifier.assignableFromAll");
            messageKeys.put("ASSIGNABLE_FROM_ANY", "org.notninja.verifier.type.ClassVerifier.assignableFromAny");
            messageKeys.put("ENUMERATION", "org.notninja.verifier.type.ClassVerifier.enumeration");
            messageKeys.put("INTERFACED", "org.notninja.verifier.type.ClassVerifier.interfaced");
            messageKeys.put("NESTED", "org.notninja.verifier.type.ClassVerifier.nested");
            messageKeys.put("PRIMITIVE", "org.notninja.verifier.type.ClassVerifier.primitive");
            messageKeys.put("PRIMITIVE_OR_WRAPPER", "org.notninja.verifier.type.ClassVerifier.primitiveOrWrapper");
            messageKeys.put("PRIMITIVE_WRAPPER", "org.notninja.verifier.type.ClassVerifier.primitiveWrapper");

            return messageKeys;
        }
    }

    @AnnotationOne
    private static class TypeWithAnnotationOne {
    }

    @AnnotationOne
    @AnnotationTwo
    private static class TypeWithAnnotationOneAndTwo {
    }

    private static class TypeWithNoAnnotations {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE})
    private @interface AnnotationOne {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE})
    private @interface AnnotationTwo {
    }

    private enum AnEnum {}

    private interface AnInterface {
    }
}
