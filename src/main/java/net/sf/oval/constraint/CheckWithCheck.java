/*
 * Copyright 2005-2021 by Sebastian Thomschke and contributors.
 * SPDX-License-Identifier: EPL-2.0
 */
package net.sf.oval.constraint;

import static net.sf.oval.Validator.*;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Map;

import net.sf.oval.ValidationCycle;
import net.sf.oval.Validator;
import net.sf.oval.configuration.annotation.AbstractAnnotationCheck;
import net.sf.oval.context.OValContext;
import net.sf.oval.exception.ReflectionException;
import net.sf.oval.internal.util.Assert;

/**
 * @author Sebastian Thomschke
 */
public class CheckWithCheck extends AbstractAnnotationCheck<CheckWith> {

   public interface SimpleCheck extends Serializable {
      boolean isSatisfied(Object validatedObject, Object value, OValContext context, Validator validator);
   }

   public interface SimpleCheckWithMessageVariables extends SimpleCheck {
      Map<String, ? extends Serializable> createMessageVariables();
   }

   private static final long serialVersionUID = 1L;

   private boolean ignoreIfNull;
   private SimpleCheck simpleCheck;

   @Override
   public void configure(final CheckWith constraintAnnotation) {
      super.configure(constraintAnnotation);
      setSimpleCheck(constraintAnnotation.value());
      setIgnoreIfNull(constraintAnnotation.ignoreIfNull());
   }

   @Override
   public Map<String, ? extends Serializable> createMessageVariables() {
      final Map<String, Serializable> messageVariables = getCollectionFactory().createMap(4);

      if (simpleCheck instanceof SimpleCheckWithMessageVariables) {
         final Map<String, ? extends Serializable> simpleCheckMessageVariables = ((SimpleCheckWithMessageVariables) simpleCheck).createMessageVariables();
         if (simpleCheckMessageVariables != null) {
            messageVariables.putAll(simpleCheckMessageVariables);
         }
      }
      messageVariables.put("ignoreIfNull", Boolean.toString(ignoreIfNull));
      messageVariables.put("simpleCheck", simpleCheck.getClass().getName());
      return messageVariables;
   }

   public SimpleCheck getSimpleCheck() {
      return simpleCheck;
   }

   public boolean isIgnoreIfNull() {
      return ignoreIfNull;
   }

   @Override
   public boolean isSatisfied(final Object validatedObject, final Object valueToValidate, final ValidationCycle cycle) throws ReflectionException {
      if (valueToValidate == null && ignoreIfNull)
         return true;

      return simpleCheck.isSatisfied(validatedObject, valueToValidate, cycle.getContext(), cycle.getValidator());
   }

   public void setIgnoreIfNull(final boolean ignoreIfNull) {
      this.ignoreIfNull = ignoreIfNull;
      requireMessageVariablesRecreation();
   }

   /**
    * @throws IllegalArgumentException if <code>simpleCheckType == null</code>
    */
   public void setSimpleCheck(final Class<? extends SimpleCheck> simpleCheckType) throws ReflectionException, IllegalArgumentException {
      Assert.argumentNotNull("simpleCheckType", simpleCheckType);

      try {
         final Constructor<? extends SimpleCheck> ctor = simpleCheckType.getDeclaredConstructor((Class<?>[]) null);
         ctor.setAccessible(true);
         simpleCheck = ctor.newInstance();
      } catch (final Exception ex) {
         throw new ReflectionException("Cannot instantiate an object of type  " + simpleCheckType.getName(), ex);
      }
      requireMessageVariablesRecreation();
   }

   /**
    * @throws IllegalArgumentException if <code>simpleCheck == null</code>
    */
   public void setSimpleCheck(final SimpleCheck simpleCheck) throws IllegalArgumentException {
      Assert.argumentNotNull("simpleCheck", simpleCheck);

      this.simpleCheck = simpleCheck;
      requireMessageVariablesRecreation();
   }
}
