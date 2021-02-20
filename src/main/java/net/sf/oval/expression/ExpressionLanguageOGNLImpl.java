/*
 * Copyright 2005-2021 by Sebastian Thomschke and contributors.
 * SPDX-License-Identifier: EPL-2.0
 */
package net.sf.oval.expression;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.oval.exception.ExpressionEvaluationException;
import net.sf.oval.internal.Log;
import net.sf.oval.internal.util.ObjectCache;
import net.sf.oval.internal.util.ReflectionUtils;
import ognl.MemberAccess;
import ognl.Ognl;
import ognl.OgnlContext;
import ognl.OgnlException;

/**
 * @author Sebastian Thomschke
 */
public class ExpressionLanguageOGNLImpl extends AbstractExpressionLanguage {

   private static final Log LOG = Log.getLog(ExpressionLanguageOGNLImpl.class);

   private static final MemberAccess MEMBER_ACCESS = new MemberAccess() {

      @Override
      public boolean isAccessible(final Map context, final Object target, final Member member, final String propertyName) {
         return true;
      }

      @Override
      public void restore(final Map context, final Object target, final Member member, final String propertyName, final Object oldAccessibleState) {
         if (!(Boolean) oldAccessibleState) {
            final AccessibleObject accessible = (AccessibleObject) member;
            ReflectionUtils.setAccessible(accessible, false);
         }
      }

      @Override
      public Boolean setup(final Map context, final Object target, final Member member, final String propertyName) {
         final AccessibleObject accessible = (AccessibleObject) member;
         final boolean oldAccessibleState = accessible.isAccessible();

         if (!oldAccessibleState) {
            ReflectionUtils.setAccessible(accessible, true);
         }
         return oldAccessibleState;
      }
   };

   private final ObjectCache<String, Object> expressionCache = new ObjectCache<>(expression -> {
      try {
         return Ognl.parseExpression(expression);
      } catch (final OgnlException ex) {
         throw new ExpressionEvaluationException("Parsing MVEL expression failed: " + expression, ex);
      }
   });

   @Override
   public Object evaluate(final String expression, final Map<String, ?> values) throws ExpressionEvaluationException {
      LOG.debug("Evaluating OGNL expression: {1}", expression);
      try {
         final OgnlContext ctx = (OgnlContext) Ognl.createDefaultContext(null, MEMBER_ACCESS);

         for (final Entry<String, ?> entry : values.entrySet()) {
            ctx.put(entry.getKey(), entry.getValue());
         }

         final Object expr = expressionCache.get(expression);
         return Ognl.getValue(expr, ctx, ctx, (Class<?>) null);
      } catch (final OgnlException ex) {
         throw new ExpressionEvaluationException("Evaluating MVEL expression failed: " + expression, ex);
      }
   }
}
