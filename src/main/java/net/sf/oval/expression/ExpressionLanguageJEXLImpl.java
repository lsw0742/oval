/*
 * Copyright 2005-2021 by Sebastian Thomschke and contributors.
 * SPDX-License-Identifier: EPL-2.0
 */
package net.sf.oval.expression;

import java.util.Map;

import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.JexlExpression;
import org.apache.commons.jexl3.MapContext;

import net.sf.oval.exception.ExpressionEvaluationException;
import net.sf.oval.internal.Log;
import net.sf.oval.internal.util.ObjectCache;

/**
 * @author Sebastian Thomschke
 */
public class ExpressionLanguageJEXLImpl extends AbstractExpressionLanguage {
   private static final Log LOG = Log.getLog(ExpressionLanguageJEXLImpl.class);

   private static final JexlEngine JEXL = new JexlBuilder() //
      .strict(true) //
      .create();

   private final ObjectCache<String, JexlExpression> expressionCache = new ObjectCache<>(JEXL::createExpression);

   @Override
   @SuppressWarnings("unchecked")
   public Object evaluate(final String expression, final Map<String, ?> values) throws ExpressionEvaluationException {
      LOG.debug("Evaluating JEXL expression: {1}", expression);
      try {
         final JexlExpression expr = expressionCache.get(expression);
         return expr.evaluate(new MapContext((Map<String, Object>) values));
      } catch (final Exception ex) {
         throw new ExpressionEvaluationException("Evaluating JEXL expression failed: " + expression, ex);
      }
   }
}
