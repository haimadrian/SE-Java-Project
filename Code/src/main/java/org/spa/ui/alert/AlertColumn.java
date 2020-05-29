package org.spa.ui.alert;

import org.spa.ui.table.TableColumnIfc;
import org.spa.ui.table.renderer.DateCellRenderer;
import org.spa.ui.table.renderer.ImageCellRenderer;
import org.spa.ui.table.renderer.TextCellRenderer;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;

/**
 * @author Haim Adrian
 * @since 15-May-20
 */
public enum AlertColumn implements TableColumnIfc {
   Severity("Severity", 0.13, ImageIcon.class, new ImageCellRenderer()),
   Message("Description", 0.64, String.class, new TextCellRenderer()),
   Date("Time", 0.23, java.util.Date.class, new DateCellRenderer());

   private final String header;
   private final double cellWidth;
   private final Class<?> columnClass;
   private final TableCellRenderer renderer;

   AlertColumn(String header, double cellWidth, Class<?> columnClass, TableCellRenderer renderer) {
      this.header = header;
      this.cellWidth = cellWidth;
      this.columnClass = columnClass;
      this.renderer = renderer;
   }

   @Override
   public int getColIndex() {
      return ordinal();
   }

   @Override
   public String getHeader() {
      return header;
   }

   @Override
   public String getAttributeName() {
      return name();
   }

   @Override
   public double getWidth() {
      return cellWidth;
   }

   @Override
   public boolean isEditable() {
      return false;
   }

   @Override
   public Object formatValueForTable(Object value) {
      return value;
   }

   @Override
   public Class<?> getColumnClass() {
      return columnClass;
   }

   @Override
   public TableCellRenderer getCellRenderer() {
      return renderer;
   }
}
