/*
 * Copyright 2005-2021 by Sebastian Thomschke and contributors.
 * SPDX-License-Identifier: EPL-2.0
 */
package net.sf.oval.constraint;

import static net.sf.oval.Validator.*;

import java.util.Map;

import net.sf.oval.ConstraintTarget;
import net.sf.oval.ValidationCycle;
import net.sf.oval.configuration.annotation.AbstractAnnotationCheck;

/**
 * @author Sebastian Thomschke
 */
public class MaxCheck extends AbstractAnnotationCheck<Max> {
   private static final long serialVersionUID = 1L;

   private boolean inclusive = true;
   private double max;

   @Override
   public void configure(final Max constraintAnnotation) {
      super.configure(constraintAnnotation);
      setMax(constraintAnnotation.value());
      setInclusive(constraintAnnotation.inclusive());
   }

   @Override
   protected Map<String, String> createMessageVariables() {
      final Map<String, String> messageVariables = getCollectionFactory().createMap(2);
      messageVariables.put("max", Double.toString(max));
      return messageVariables;
   }

   @Override
   protected ConstraintTarget[] getAppliesToDefault() {
      return new ConstraintTarget[] {ConstraintTarget.VALUES};
   }

   public double getMax() {
      return max;
   }

   public boolean isInclusive() {
      return inclusive;
   }

   @Override
   public boolean isSatisfied(final Object validatedObject, final Object valueToValidate, final ValidationCycle cycle) {
      if (valueToValidate == null)
         return true;

      if (valueToValidate instanceof Number) {
         final double doubleValue = ((Number) valueToValidate).doubleValue();
         if (inclusive)
            return doubleValue <= max;
         return doubleValue < max;
      }

      final String stringValue = valueToValidate.toString();
      try {
         final double doubleValue = Double.parseDouble(stringValue);
         if (inclusive)
            return doubleValue <= max;
         return doubleValue < max;
      } catch (final NumberFormatException e) {
         return false;
      }
   }

   public void setInclusive(final boolean inclusive) {
      this.inclusive = inclusive;
   }

   public void setMax(final double max) {
      this.max = max;
      requireMessageVariablesRecreation();
   }
}
