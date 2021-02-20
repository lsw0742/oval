/*
 * Copyright 2005-2021 by Sebastian Thomschke and contributors.
 * SPDX-License-Identifier: EPL-2.0
 */
package net.sf.oval.expression;

import java.util.Map;
import java.util.Map.Entry;

import bsh.EvalError;
import bsh.Interpreter;
import net.sf.oval.exception.ExpressionEvaluationException;
import net.sf.oval.internal.Log;

/**
 * @author Sebastian Thomschke
 */
public class ExpressionLanguageBeanShellImpl extends AbstractExpressionLanguage {
   private static final Log LOG = Log.getLog(ExpressionLanguageBeanShellImpl.class);

   @Override
   public Object evaluate(final String expression, final Map<String, ?> values) throws ExpressionEvaluationException {
      LOG.debug("Evaluating BeanShell expression: {1}", expression);
      try {
         final Interpreter interpreter = new Interpreter();
         interpreter.eval("setAccessibility(true)"); // turn off access restrictions
         for (final Entry<String, ?> entry : values.entrySet()) {
            interpreter.set(entry.getKey(), entry.getValue());
         }
         return interpreter.eval(expression);
      } catch (final EvalError ex) {
         throw new ExpressionEvaluationException("Evaluating BeanShell expression failed: " + expression, ex);
      }
   }
}
