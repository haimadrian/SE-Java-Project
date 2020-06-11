package org.spa.view.table;

import org.spa.view.table.editor.TextCellEditor;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 * An interface that declares all methods that needed by a {@link TableManager} to operate. See the methods
 *
 * @author Haim Adrian
 * @since 12-May-20
 */
public interface TableColumnIfc {
   /**
    * Columns are not movable, here you should specify the index of a column. Columns will be displayed
    * with the order defined by the indexing
    *
    * @return index of a column
    */
   int getColIndex();

   /**
    * A header to display for that column. Used by table headers
    *
    * @return column's header
    */
   String getHeader();

   /**
    * Name of the attribute that a column represents. This will be used by value handlers to
    * be able to find, in reflection or whatever method you prefer, e.g. switch case, what property
    * should be retrieved for get/set operations. For example, let's assume a column represents 'quantity'
    * of an item, hence this method should return "Quantity", and the model should have "setQuantity" and "getQuantity"
    * methods.
    *
    * @return Name of the attribute for get/set operations
    */
   String getAttributeName();

   /**
    * @return The preferred width of a column, in percents. Value between 0 to 1
    */
   double getWidth();

   /**
    * @return For editable tables, this method should return whether a column is editable or not
    */
   boolean isEditable();

   /**
    * This method created in order to let custom formats for different columns. For example an image column will
    * present the image, and a date column will format a date time according to its preferred format.<br/>
    * We use this method when a value is about to be draw.
    *
    * @param value The value to format. Safe cast it to the value that this column represents
    * @return The value after formatting it
    */
   Object formatValueForTable(Object value);

   /**
    * To support default cell renderer that renders column by its value type, we need the class of the values
    * that are going to be inside this column.
    *
    * @return The class of the values that are going to fit in this column
    */
   Class<?> getColumnClass();

   /**
    * Implementors can implement this to return the table cell editor which is relevant to this column.<br/>
    * It can be any custom editor you would like to use, e.g. numeric. By default we use a {@link TextCellEditor}.
    *
    * @return The cell editor for this column
    */
   default TableCellEditor getCellEditor() {
      return new TextCellEditor();
   }

   /**
    * Implementors can implement this to return the table cell renderer which is relevant to this column.<br/>
    * It can be any custom editor you would like to use, e.g. numeric. By default we use a {@link javax.swing.table.DefaultTableCellRenderer}.
    *
    * @return The cell renderer for this column
    */
   default TableCellRenderer getCellRenderer() {
      return new DefaultTableCellRenderer();
   }
}
