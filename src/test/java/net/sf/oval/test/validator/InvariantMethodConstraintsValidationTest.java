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
import net.sf.oval.configuration.annotation.IsInvariant;
import net.sf.oval.constraint.Length;
import net.sf.oval.constraint.NotNull;
import net.sf.oval.guard.PostValidateThis;
import net.sf.oval.guard.PreValidateThis;
import net.sf.oval.guard.SuppressOValWarnings;

/**
 * @author Sebastian Thomschke
 */
public class InvariantMethodConstraintsValidationTest {
   public static class TestEntity {
      public String name;

      @IsInvariant
      @NotNull(message = "NOT_NULL")
      @Length(max = 4, message = "LENGTH")
      public String getName() {
         return name;
      }
   }

   @SuppressWarnings("unused")
   public static class TestEntityInvalidConfig extends TestEntity {
      /**
       * the @NotNull annotation should lead to a "OVal API usage violation 7" warning by the ApiUsageAuditor
       * because class is not guarded
       */
      public TestEntityInvalidConfig(@NotNull final String defaultValue) {
         //
      }

      /**
       * the @NotNull annotation should lead to a "OVal API usage violation 1" warning by the ApiUsageAuditor
       * because class is not guarded
       */
      @NotNull
      public void doSomething_1() {

      }

      /**
       * the @NotNull annotation should NOT lead to a "OVal API usage violation 1" warning by the ApiUsageAuditor
       */
      @SuppressOValWarnings
      @NotNull
      public void doSomething_1_WithSuppressedWarning() {

      }

      /**
       * the @NotNull annotation should lead to a "OVal API usage violation 2" warning by the ApiUsageAuditor
       * because class is not guarded
       */
      @NotNull
      public String doSomething_2(final String value) {
         return null;
      }

      /**
       * the @NotNull annotation should NOT lead to a "OVal API usage violation 2" warning by the ApiUsageAuditor
       */
      @NotNull
      @SuppressOValWarnings
      public String doSomething_2_WithSuppressedWarning(final String value) {
         return null;
      }

      /**
       * the @NotNull annotation should lead to a "OVal API usage violation 3" warning by the ApiUsageAuditor
       * because @IsInvariant is missing
       */
      @NotNull
      public String doSomething_3() {
         return null;
      }

      /**
       * the @NotNull annotation should NOT lead to a "OVal API usage violation 3" warning by the ApiUsageAuditor
       */
      @NotNull
      @SuppressOValWarnings
      public String doSomething_3_WithSuppressedWarning() {
         return null;
      }

      /**
       * the @NotNull annotation should lead to a "OVal API usage violation 4" warning by the ApiUsageAuditor
       * because class is not guarded
       */
      @PreValidateThis
      public void doSomething_4() {

      }

      /**
       * the @NotNull annotation should NOT lead to a "OVal API usage violation 4" warning by the ApiUsageAuditor
       */
      @PreValidateThis
      @SuppressOValWarnings
      public void doSomething_4_WithSuppressedWarning() {

      }

      /**
       * the @NotNull annotation should lead to a "OVal API usage violation 5" warning by the ApiUsageAuditor
       * because class is not guarded
       */
      @PostValidateThis
      public void doSomething_5() {

      }

      /**
       * the @NotNull annotation should NOT lead to a "OVal API usage violation 5" warning by the ApiUsageAuditor
       */
      @PostValidateThis
      @SuppressOValWarnings
      public void doSomething_5_WithSuppressedWarning() {

      }

      /**
       * the @NotNull annotation should lead to a "OVal API usage violation 6" warning by the ApiUsageAuditor
       * because class is not guarded
       */
      public String doSomething_6(final String value, @NotNull final String defaultValue) {
         return null;
      }

      /**
       * the @NotNull annotation should NOT lead to a "OVal API usage violation 6" warning by the ApiUsageAuditor
       */
      @SuppressOValWarnings
      public String doSomething_6_WithSuppressedWarning(final String value, @NotNull final String defaultValue) {
         return null;
      }
   }

   @Test
   public void testMethodReturnValueConstraintValidation() {
      final Validator validator = new Validator();

      {
         final TestEntity t = new TestEntity();

         List<ConstraintViolation> violations = validator.validate(t);
         assertThat(violations).hasSize(1);
         assertThat(violations.get(0).getMessage()).isEqualTo("NOT_NULL");

         t.name = "wqerwqer";
         violations = validator.validate(t);
         assertThat(violations).hasSize(1);
         assertThat(violations.get(0).getMessage()).isEqualTo("LENGTH");
      }
   }
}
