/*
 * Copyright 2005-2021 by Sebastian Thomschke and contributors.
 * SPDX-License-Identifier: EPL-2.0
 */
package net.sf.oval.internal.util;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;

import net.sf.oval.internal.Log;

/**
 * Serializable Wrapper for java.lang.reflect.Field objects since they do not implement Serializable
 *
 * @author Sebastian Thomschke
 */
public final class SerializableField implements Serializable {
   private static final Log LOG = Log.getLog(SerializableField.class);

   private static final long serialVersionUID = 1L;

   private final Class<?> declaringClass;
   private transient Field field;
   private final String name;

   public SerializableField(final Field field) {
      this.field = field;
      name = field.getName();
      declaringClass = field.getDeclaringClass();
   }

   public Class<?> getDeclaringClass() {
      return declaringClass;
   }

   public Field getField() {
      return field;
   }

   public String getName() {
      return name;
   }

   private void readObject(final java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
      in.defaultReadObject();
      try {
         field = declaringClass.getDeclaredField(name);
      } catch (final NoSuchFieldException ex) {
         LOG.debug("Unexpected NoSuchFieldException occurred.", ex);
         throw new IOException(ex.getMessage());
      }
   }
}
