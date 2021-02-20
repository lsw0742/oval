/*
 * Copyright 2005-2021 by Sebastian Thomschke and contributors.
 * SPDX-License-Identifier: EPL-2.0
 */
package net.sf.oval.test.validator;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.Test;

import net.sf.oval.ConstraintViolation;
import net.sf.oval.Validator;
import net.sf.oval.constraint.AssertValid;
import net.sf.oval.constraint.NotNull;

/**
 * @author Sebastian Thomschke
 */
public class ObjectGraphTest {

   protected static class ClassA {
      @AssertValid
      ClassB classB;

      @AssertValid
      ClassC classC;
   }

   protected static class ClassB {
      @AssertValid
      ClassC classC;
   }

   protected static class ClassC {
      @AssertValid
      ClassA classA;

      @NotNull
      String name;
   }

   @Test
   public void testObjectGraph() {
      final ClassA classA = new ClassA();
      classA.classB = new ClassB();
      classA.classC = new ClassC();
      classA.classC.classA = classA;
      classA.classB.classC = classA.classC;

      final Validator validator = new Validator();
      final List<ConstraintViolation> violations = validator.validate(classA);
      assertThat(violations).hasSize(1);
   }

}
