/*
 * Copyright 2005-2021 by Sebastian Thomschke and contributors.
 * SPDX-License-Identifier: EPL-2.0
 */
package net.sf.oval.integration.guice;

import com.google.inject.Injector;

import net.sf.oval.Check;
import net.sf.oval.configuration.CheckInitializationListener;
import net.sf.oval.constraint.CheckWithCheck;

/**
 * Injects Guice bean dependencies into {@link Check} instances.
 *
 * Required dependencies must be annotated with <code>@Autowired</code> within {@link Check} the class.
 *
 * @author Sebastian Thomschke
 */
public class GuiceCheckInitializationListener implements CheckInitializationListener {
   private final Injector injector;

   public GuiceCheckInitializationListener(final Injector injector) {
      this.injector = injector;
   }

   @Override
   public void onCheckInitialized(final Check check) {
      injector.injectMembers(check);

      if (check instanceof CheckWithCheck) {
         final CheckWithCheck checkWith = (CheckWithCheck) check;
         injector.injectMembers(checkWith.getSimpleCheck());
      }
   }
}
