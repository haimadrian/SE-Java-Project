package org.spa.ui.order;

import org.spa.ui.table.TableColumnIfc;
import org.spa.ui.table.editor.TextCellEditor;
import org.spa.ui.table.renderer.TextCellRenderer;

import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;


public enum OrderColumn implements TableColumnIfc {
   OrderId("OrderId", 0.3, String.class, new TextCellRenderer(), null, false, 0),
   OrderTime("Time", 0.15, String.class, new TextCellRenderer(), null, false, 1),
   Summary("Summary", 0.45, String.class, new TextCellRenderer(), null, false, 2),
   UserId("UserId", 0.1, String.class, new TextCellRenderer(), null, false, 3);

   private final String header;
   private final double cellWidth;
   private final Class<?> columnClass;
   private final TableCellRenderer renderer;
   private final TableCellEditor editor;
   private final boolean isEditable;
   private final int index;


   OrderColumn(String header, double cellWidth, Class<?> columnClass, TableCellRenderer renderer, TableCellEditor editor, boolean isEditable, int index) {
      this.header = header;
      this.cellWidth = cellWidth;
      this.columnClass = columnClass;
      this.renderer = renderer;
      this.editor = editor == null ? new TextCellEditor() : editor;
      this.isEditable = isEditable;
      this.index = index;
   }

   @Override
   public int getColIndex() {
      return index;
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
