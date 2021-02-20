/*
 * Copyright 2005-2021 by Sebastian Thomschke and contributors.
 * SPDX-License-Identifier: EPL-2.0
 */
package net.sf.oval.integration.spring;

import java.util.ListIterator;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;

import net.sf.oval.ConstraintViolation;
import net.sf.oval.Validator;
import net.sf.oval.context.FieldContext;
import net.sf.oval.context.OValContext;
import net.sf.oval.exception.ValidationFailedException;
import net.sf.oval.internal.Log;

/**
 * @author Sebastian Thomschke
 */
public class SpringValidator implements org.springframework.validation.Validator, InitializingBean {
   private static final Log LOG = Log.getLog(SpringValidator.class);

   private Validator validator;

   public SpringValidator() {
   }

   public SpringValidator(final Validator validator) {
      this.validator = validator;
   }

   @Override
   public void afterPropertiesSet() throws Exception {
      Assert.notNull(validator, "Property [validator] must be set");
   }

   public Validator getValidator() {
      return validator;
   }

   public void setValidator(final Validator validator) {
      this.validator = validator;
   }

   @Override
   public boolean supports(@SuppressWarnings("rawtypes") final Class clazz) {
      return true;
   }

   @Override
   public void validate(final Object objectToValidate, final Errors errors) {
      try {
         for (final ConstraintViolation violation : validator.validate(objectToValidate)) {
            final String errorCode = violation.getErrorCode();
            final String errorMessage = violation.getMessage();

            final ListIterator<OValContext> listIterator = violation.getContextPath().listIterator(violation.getContextPath().size());
            OValContext ctx = null;
            boolean hasFieldContext = false;
            while (listIterator.hasPrevious()) {
               ctx = listIterator.previous();
               if (ctx instanceof FieldContext) {
                  hasFieldContext = true;
                  break;
               }
            }

            if (hasFieldContext) {
               @SuppressWarnings("null")
               final String fieldName = ((FieldContext) ctx).getField().getName();
               errors.rejectValue(fieldName, errorCode, errorMessage);
            } else {
               errors.reject(errorCode, errorMessage);
            }
         }
      } catch (final ValidationFailedException ex) {
         LOG.error("Unexpected error during validation", ex);

         errors.reject(ex.getMessage());
      }
   }
}
