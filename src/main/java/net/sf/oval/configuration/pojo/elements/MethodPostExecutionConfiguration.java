/*
 * Copyright 2005-2021 by Sebastian Thomschke and contributors.
 * SPDX-License-Identifier: EPL-2.0
 */
package net.sf.oval.configuration.pojo.elements;

import java.util.List;

import net.sf.oval.guard.PostCheck;

/**
 * @author Sebastian Thomschke
 */
public class MethodPostExecutionConfiguration extends ConfigurationElement {
   private static final long serialVersionUID = 1L;

   /**
    * checks that need to be verified after method execution
    */
   public List<PostCheck> checks;
}
