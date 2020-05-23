package org.spa.common;

import java.util.List;
import java.util.function.Predicate;

/**
 * @author Haim Adrian
 * @since 15-May-20
 */
public interface Repository<T> {
   List<T> selectAll();
   List<T> select(Predicate<T> filter);
   T add(T item);
   List<T> saveAll(List<T> items);
}
