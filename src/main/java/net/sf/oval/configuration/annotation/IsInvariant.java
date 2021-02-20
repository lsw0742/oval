/*
 * Copyright 2005-2021 by Sebastian Thomschke and contributors.
 * SPDX-License-Identifier: EPL-2.0
 */
package net.sf.oval.configuration.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies that the return value of the given method (usually a getter method)
 * should be checked whenever the object is validated.<br>
 *
 * <b>Important 1:</b> This is only supported for non-void, non-parameterized methods.<br>
 * <b>Important 2:</b> To retrieve the return value of the method OVal has to invoke
 * the method during validation. Therefore you need to ensure that calling the method
 * does not change the object state.<br>
 *
 * @author Sebastian Thomschke
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface IsInvariant {
   //
}
