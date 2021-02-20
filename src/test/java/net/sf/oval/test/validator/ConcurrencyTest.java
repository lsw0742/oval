/*
 * Copyright 2005-2021 by Sebastian Thomschke and contributors.
 * SPDX-License-Identifier: EPL-2.0
 */
package net.sf.oval.test.validator;

import static org.assertj.core.api.Assertions.*;

import org.junit.Test;

import net.sf.oval.Validator;
import net.sf.oval.configuration.annotation.IsInvariant;
import net.sf.oval.constraint.MaxLength;
import net.sf.oval.constraint.NotNull;

/**
 * @author Sebastian Thomschke
 */
public class ConcurrencyTest {

   public static final class TestEntity1 {
      @NotNull
      @MaxLength(5)
      public String name;

      public String getName() {
         return name;
      }
   }

   public static final class TestEntity2 {
      public String name;

      @NotNull
      @MaxLength(5)
      @IsInvariant
      public String getName() {
         return name;
      }
   }

   private static final class TestRunner implements Runnable {
      private final boolean[] failed;
      private final Validator validator;
      private final TestEntity1 sharedEntity;

      TestRunner(final Validator validator, final TestEntity1 sharedEntity, final boolean[] failed) {
         this.validator = validator;
         this.sharedEntity = sharedEntity;
         this.failed = failed;
      }

      @Override
      public void run() {
         try {
            final TestEntity1 entity = new TestEntity1();

            for (int i = 0; i < 100; i++) {
               assertThat(validator.validate(sharedEntity)).hasSize(1);

               entity.name = null;
               assertThat(validator.validate(entity)).hasSize(1);

               entity.name = "1234";
               assertThat(validator.validate(entity)).isEmpty();

               entity.name = "123456";
               assertThat(validator.validate(entity)).hasSize(1);

               try {
                  Thread.sleep(5);
               } catch (final InterruptedException e) {
                  Thread.currentThread().interrupt();
               }
            }
         } catch (final RuntimeException ex) {
            ex.printStackTrace();
            failed[0] = true;
         }
      }
   }

   @Test
   public void testConcurrency() throws InterruptedException {
      final Validator validator = new Validator();

      final TestEntity1 sharedEntity = new TestEntity1();

      final boolean[] failed = {false};

      final Thread thread1 = new Thread(new TestRunner(validator, sharedEntity, failed));
      final Thread thread2 = new Thread(new TestRunner(validator, sharedEntity, failed));

      thread1.start();
      thread2.start();
      thread1.join();
      thread2.join();
      assertThat(failed[0]).isFalse();
   }
}
