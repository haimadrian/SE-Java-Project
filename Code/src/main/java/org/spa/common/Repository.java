
package org.spa.common;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Main interface for every repository class in the application<br/>
 * This interface exposes CRUD actions for models in the application.
 * @author Haim Adrian
 * @since 15-May-20
 */
public interface Repository<T> {
   /**
    * Reads all models from storage into memory
    * @return A list containing all models from storage
    */
   List<T> selectAll();

   /**
    * Selects specific models using a filter from storage
    * @param filter The filter to apply
    * @return A list containing the models that answered the specified filter
    */
   default List<T> select(Predicate<T> filter) {
      return selectAll().stream().filter(filter).collect(Collectors.toList());
   }

   /**
    * Save all specified models into the storage
    * @param items The models to save
    */
   void saveAll(Iterable<T> items);

   /**
    * Create a new model in the storage
    * @param item The model to create
    * @return A reference to the created model (the up to date one, with identifier)
    */
   T create(T item);

   /**
    * Update an existing model in the storage
    * @param item The model to update
    * @return A referenc eto the up to date model
    */
   T update(T item);

   /**
    * Remove a model from the storage
    * @param item The model to remove
    * @return A reference to the removed model
    */
   T delete(T item);
}
