package org.spa.ui.item;

import org.spa.ui.table.TableColumnIfc;
import org.spa.ui.table.renderer.CurrencyCellRenderer;
import org.spa.ui.table.renderer.StretchedImageCellRenderer;
import org.spa.ui.table.renderer.TextCellRenderer;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;

/**
 * @author hadrian
 * @since 15-May-20
 */
public enum ItemColumn implements TableColumnIfc {
   Image("Image", 0.15, ImageIcon.class, new StretchedImageCellRenderer(10), false),
   Name("Name", 0.15, String.class, new TextCellRenderer(), true),
   Description("Description", 0.65, String.class, new TextCellRenderer(), true),
   Price("Price", 0.05, Double.class, new CurrencyCellRenderer(), true),
   Count("Count", 0.05, Integer.class, new TextCellRenderer(), true);

   private final String header;
   private final double cellWidth;
   private final Class<?> columnClass;
   private final TableCellRenderer renderer;
   private final boolean isEditable;

   ItemColumn(String header, double cellWidth, Class<?> columnClass, TableCellRenderer renderer, boolean isEditable) {
      this.header = header;
      this.cellWidth = cellWidth;
      this.columnClass = columnClass;
      this.renderer = renderer;
      this.isEditable = isEditable;
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
      return isEditable;
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
