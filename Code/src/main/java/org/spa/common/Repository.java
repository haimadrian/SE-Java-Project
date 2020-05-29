
package org.spa.common;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author Haim Adrian
 * @since 15-May-20
 */
public interface Repository<T> {
   List<T> selectAll() throws FileNotFoundException;
   List<T> select(Predicate<T> filter) throws FileNotFoundException;
   T create(T item);
   T update(T item);
   T delete(T item);
   void saveAll(Iterable<T> items);}