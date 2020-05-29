package org.spa.ui.item;

import org.spa.ui.table.TableColumnIfc;
import org.spa.ui.table.editor.CountCellEditor;
import org.spa.ui.table.editor.TextCellEditor;
import org.spa.ui.table.renderer.SpinnerCellRenderer;
import org.spa.ui.table.renderer.StretchedImageCellRenderer;
import org.spa.ui.table.renderer.TextCellRenderer;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 * @author Haim Adrian
 * @since 15-May-20
 */
public enum ItemColumn implements TableColumnIfc {
   Image("Image", 0.15, ImageIcon.class, new StretchedImageCellRenderer(10, ItemViewInfo.ADS_ATTRIBUTE_NAME), null, false),
   Name("Name", 0.15, String.class, new TextCellRenderer(), null, false),
   Description("Description", 0.54, String.class, new TextCellRenderer(), null, false),
   Count("Count", 0.06, Integer.class, new SpinnerCellRenderer(), new CountCellEditor(), true),
   Price("Price", 0.1, Double.class, new TextCellRenderer(), null, false);

   private final String header;
   private final double cellWidth;
   private final Class<?> columnClass;
   private final TableCellRenderer renderer;
   private final TableCellEditor editor;
   private final boolean isEditable;

   ItemColumn(String header, double cellWidth, Class<?> columnClass, TableCellRenderer renderer, TableCellEditor editor, boolean isEditable) {
      this.header = header;
      this.cellWidth = cellWidth;
      this.columnClass = columnClass;
      this.renderer = renderer;
      this.editor = editor == null ? new TextCellEditor() : editor;
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

   @Override
   public TableCellEditor getCellEditor() {
      return editor;
   }


}
