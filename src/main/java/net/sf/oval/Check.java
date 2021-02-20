/*
 * Copyright 2005-2021 by Sebastian Thomschke and contributors.
 * SPDX-License-Identifier: EPL-2.0
 */
package net.sf.oval;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import net.sf.oval.context.OValContext;
import net.sf.oval.exception.OValException;
import net.sf.oval.internal.util.CollectionUtils;

/**
 * interface for classes that can check/validate if a constraint is satisfied
 *
 * @author Sebastian Thomschke
 */
public interface Check extends Serializable {
   /**
    * <p>
    * In case the constraint is declared for an array, collection or map this controls how the constraint is applied to it and it's child objects.
    *
    * <p>
    * <b>Default:</b> ConstraintTarget.CONTAINER
    *
    * <p>
    * <b>Note:</b> This setting is ignored for object types other than array, map and collection.
    */
   ConstraintTarget[] getAppliesTo();

   /**
    * @return Returns the context where the constraint was declared.
    *
    * @see net.sf.oval.context.ClassContext
    * @see net.sf.oval.context.FieldContext
    * @see net.sf.oval.context.MethodEntryContext
    * @see net.sf.oval.context.MethodExitContext
    * @see net.sf.oval.context.MethodParameterContext
    * @see net.sf.oval.context.MethodReturnValueContext
    */
   OValContext getContext();

   /**
    * @return the error code that will be used in a corresponding ConstraintViolation object
    */
   String getErrorCode();

   /**
    * gets the default message that is displayed if a corresponding message key
    * is not found in the messages properties file
    * <br>
    * default processed place holders are:
    * <ul>
    * <li>{context} => specifies which getter, method parameter or field was validated
    * <li>{invalidValue} => string representation of the validated value
    * </ul>
    */
   String getMessage();

   /**
    * values that are used to fill place holders when rendering the error message.
    * A key "min" with a value "4" will replace the place holder {min} in an error message
    * like "Value cannot be smaller than {min}" with the string "4".
    */
   Map<String, ? extends Serializable> getMessageVariables();

   /**
    * @return the profiles, may return null
    */
   String[] getProfiles();

   int getSeverity();

   /**
    * An expression to specify where in the object graph relative from this object the expression
    * should be applied.
    * <p>
    * Examples:
    * <li>"owner" would apply this constraint to the current object's property <code>owner</code>
    * <li>"owner.id" would apply this constraint to the current object's <code>owner</code>'s property <code>id</code>
    * <li>"jxpath:owner/id" would use the JXPath implementation to traverse the object graph to locate the object where this constraint should be applied.
    */
   String getTarget();

   /**
    * Formula returning <code>true</code> if this constraint shall be evaluated and
    * <code>false</code> if it shall be ignored for the current validation.
    * <p>
    * <b>Important:</b> The formula must be prefixed with the name of the scripting language that is used.
    * E.g. <code>groovy:_this.amount > 10</code>
    * <p>
    * Available context variables are:<br>
    * <b>_this</b> -&gt; the validated bean<br>
    * <b>_value</b> -&gt; the value to validate (e.g. the field value, parameter value, method return value,
    * or the validated bean for object level constraints)
    *
    * @return the formula
    */
   String getWhen();

   /**
    * @param validatedObject the object/bean to validate the value against, for static fields or methods this is the class
    * @param valueToValidate the value to validate, may be null when validating pre conditions for static methods
    * @return <code>true</code> if this check is active and must be satisfied
    *
    * @since 3.1.0
    */
   default boolean isActive(final Object validatedObject, final Object valueToValidate, final ValidationCycle cycle) {
      return isActive(validatedObject, valueToValidate, cycle.getValidator());
   }

   /**
    * @param validatedObject the object/bean to validate the value against, for static fields or methods this is the class
    * @param valueToValidate the value to validate, may be null when validating pre conditions for static methods
    * @param validator the calling validator
    * @return <code>true</code> if this check is active and must be satisfied
    *
    * @deprecated use {@link #isActive(Object, Object, ValidationCycle)}
    */
   @Deprecated
   default boolean isActive(final Object validatedObject, final Object valueToValidate, final Validator validator) {
      return isActive(validatedObject, valueToValidate, new ValidationCycle() {
         @Override
         public void addConstraintViolation(final ConstraintViolation violation) {
            throw new UnsupportedOperationException();
         }

         @Override
         public List<OValContext> getContextPath() {
            return null;
         }

         @Override
         public Object getRootObject() {
            return validatedObject;
         }

         @Override
         public Validator getValidator() {
            return validator;
         }
      });
   }

   /**
    * This method implements the validation logic
    *
    * @param validatedObject the object/bean to validate the value against, for static fields or methods this is the class
    * @param valueToValidate the value to validate, may be null when validating pre conditions for static methods
    * @param context the validation context (e.g. a field, a constructor parameter or a method parameter)
    * @param validator the calling validator
    * @return true if the value satisfies the checked constraint
    * @deprecated use {@link #isSatisfied(Object, Object, ValidationCycle)}
    */
   @Deprecated
   default boolean isSatisfied(final Object validatedObject, final Object valueToValidate, final OValContext context, final Validator validator)
      throws OValException {
      return isSatisfied(validatedObject, valueToValidate, new ValidationCycle() {
         @Override
         public void addConstraintViolation(final ConstraintViolation violation) {
            throw new UnsupportedOperationException();
         }

         @Override
         public List<OValContext> getContextPath() {
            return null;
         }

         @Override
         public Object getRootObject() {
            return validatedObject;
         }

         @Override
         public Validator getValidator() {
            return validator;
         }
      });
   }

   /**
    * This method implements the validation logic
    *
    * @param validatedObject the object/bean to validate the value against, for static fields or methods this is the class
    * @param valueToValidate the value to validate, may be null when validating pre conditions for static methods
    * @return true if the value satisfies the checked constraint
    *
    * @since 3.1.0
    */
   default boolean isSatisfied(final Object validatedObject, final Object valueToValidate, final ValidationCycle cycle) throws OValException {
      return isSatisfied(validatedObject, valueToValidate, CollectionUtils.getLast(cycle.getContextPath()), cycle.getValidator());
   }

   void setAppliesTo(ConstraintTarget... target);

   void setContext(OValContext context);

   void setErrorCode(String errorCode);

   /**
    * sets the default message that is displayed if a corresponding message key
    * is not found in the messages properties file
    *
    * <br>
    * default processed place holders are:
    * <ul>
    * <li>{context} => specifies which getter, method parameter or field was validated
    * <li>{invalidValue} => string representation of the validated value
    * </ul>
    */
   void setMessage(String message);

   void setProfiles(String... profiles);

   void setSeverity(int severity);

   /**
    * Sets an expression to specify where in the object graph relative from this object the expression
    * should be applied.
    * <p>
    * Examples:
    * <li>"owner" would apply this constraint to the current object's property <code>owner</code>
    * <li>"owner.id" would apply this constraint to the current object's <code>owner</code>'s property <code>id</code>
    * <li>"jxpath:owner/id" would use the JXPath implementation to traverse the object graph to locate the object where this constraint should be applied.
    */
   void setTarget(String target);

   /**
    * Sets the formula returning <code>true</code> if this constraint shall be evaluated and
    * <code>false</code> if it shall be ignored for the current validation.
    * <p>
    * <b>Important:</b> The formula must be prefixed with the name of the scripting language that is used.
    * E.g. <code>groovy:_this.amount > 10</code>
    * <p>
    * Available context variables are:<br>
    * <b>_this</b> -&gt; the validated bean<br>
    * <b>_value</b> -&gt; the value to validate (e.g. the field value, parameter value, method return value,
    * or the validated bean for object level constraints)
    *
    * @param when formula calculating if this check is active
    */
   void setWhen(String when);
}
