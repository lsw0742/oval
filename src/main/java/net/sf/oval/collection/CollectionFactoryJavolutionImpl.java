/*
 * Copyright 2005-2021 by Sebastian Thomschke and contributors.
 * SPDX-License-Identifier: EPL-2.0
 */
package net.sf.oval.collection;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

import javolution.util.FastMap;
import javolution.util.FastSet;
import javolution.util.FastTable;

/**
 * @author Sebastian Thomschke
 */
public class CollectionFactoryJavolutionImpl implements CollectionFactory {

   @Override
   public <KeyType, ValueType> ConcurrentMap<KeyType, ValueType> createConcurrentMap() {
      return new FastMap<>();
   }

   @Override
   public <KeyType, ValueType> ConcurrentMap<KeyType, ValueType> createConcurrentMap(final int initialCapacity) {
      return new FastMap<>();
   }

   @Override
   public <ItemType> List<ItemType> createList() {
      return new FastTable<>();
   }

   @Override
   public <ItemType> List<ItemType> createList(final int initialCapacity) {
      return new FastTable<>();
   }

   @Override
   public <KeyType, ValueType> Map<KeyType, ValueType> createMap() {
      return new FastMap<>();
   }

   @Override
   public <KeyType, ValueType> Map<KeyType, ValueType> createMap(final int initialCapacity) {
      return new FastMap<>();
   }

   @Override
   public <ItemType> Set<ItemType> createSet() {
      return new FastSet<>();
   }

   @Override
   public <ItemType> Set<ItemType> createSet(final int initialCapacity) {
      return new FastSet<>();
   }
}
