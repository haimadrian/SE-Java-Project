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
   T add(T item);
   List<T> saveAll(List<T> items);
}
