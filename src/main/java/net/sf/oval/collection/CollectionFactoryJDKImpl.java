/*
 * Copyright 2005-2021 by Sebastian Thomschke and contributors.
 * SPDX-License-Identifier: EPL-2.0
 */
package net.sf.oval.collection;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Sebastian Thomschke
 */
public class CollectionFactoryJDKImpl implements CollectionFactory {

   @Override
   public <KeyType, ValueType> ConcurrentMap<KeyType, ValueType> createConcurrentMap() {
      return new ConcurrentHashMap<>();
   }

   @Override
   public <KeyType, ValueType> ConcurrentMap<KeyType, ValueType> createConcurrentMap(final int initialCapacity) {
      return new ConcurrentHashMap<>(initialCapacity);
   }

   @Override
   public <ValueType> List<ValueType> createList() {
      return new ArrayList<>();
   }

   @Override
   public <ValueType> List<ValueType> createList(final int initialCapacity) {
      return new ArrayList<>(initialCapacity);
   }

   @Override
   public <KeyType, ValueType> Map<KeyType, ValueType> createMap() {
      return new LinkedHashMap<>();
   }

   @Override
   public <KeyType, ValueType> Map<KeyType, ValueType> createMap(final int initialCapacity) {
      return new LinkedHashMap<>(initialCapacity);
   }

   @Override
   public <ValueType> Set<ValueType> createSet() {
      return new LinkedHashSet<>();
   }

   @Override
   public <ValueType> Set<ValueType> createSet(final int initialCapacity) {
      return new LinkedHashSet<>(initialCapacity);
   }
}
