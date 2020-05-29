package org.spa.ui.table;

import org.spa.ui.item.ItemViewInfo;

/**
 * This class created in order to get the ability to have a reference to the current table model (row)
 * from within the renderers and editors.<br/>
 * We need this because there are columns that depend on other columns, for example, the image column
 * can present the discount over the image, and the price column should calculate the actual price
 * based on profit and discount. Hence we cannot just work with table values, and instead, we use a
 * wrapping Item value, that all it does is to hold the actual value of the column, for example this value can be
 * the name of the item as string, or the image representing the item, and it will also hold a reference to the
 * {@link ItemViewInfo} that returned this ItemValue.
 *
 * @author Haim Adrian
 * @since 29-May-20
 */
public class TableCellValue<T extends TableModelIfc> {
   private Object value;
   private T item;

   /**
    * Constructs a new {@link TableCellValue}
    * @param value The actual value, e.g. image or name
    * @param item A reference to the item which constructing this item value reference.
    */
   public TableCellValue(Object value, T item) {
      this.value = value;
      this.item = item;
   }

   public Object getValue() {
      return value;
   }

   public void setValue(Object value) {
      this.value = value;
   }

   public T getItem() {
      return item;
   }

   public void setItem(T item) {
      this.item = item;
   }

   @Override
   public String toString() {
      // It is very important to return the corresponding value here because the ItemViewInfo.setAttributeValue uses String.valueOf
      // on the cell's value, which is an ItemValue object.
      return String.valueOf(value);
   }
}
