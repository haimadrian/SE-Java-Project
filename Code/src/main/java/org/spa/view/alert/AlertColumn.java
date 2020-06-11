package org.spa.view.alert;

import org.spa.view.table.TableColumnIfc;
import org.spa.view.table.editor.TextCellEditor;
import org.spa.view.table.renderer.DateCellRenderer;
import org.spa.view.table.renderer.ImageCellRenderer;
import org.spa.view.table.renderer.TextCellRenderer;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 * @author Haim Adrian
 * @since 15-May-20
 */
public enum AlertColumn implements TableColumnIfc {
   Severity("Severity", 0.13, ImageIcon.class, new ImageCellRenderer(), null, false),
   Message("Description", 0.64, String.class, new TextCellRenderer(), new TextCellEditor(true), true),
   Date("Time", 0.23, java.util.Date.class, new DateCellRenderer(), null, false);

   private final String header;
   private final double cellWidth;
   private final Class<?> columnClass;
   private final TableCellRenderer renderer;
   private final TableCellEditor editor;
   private final boolean isEditable;

   AlertColumn(String header, double cellWidth, Class<?> columnClass, TableCellRenderer renderer, TableCellEditor editor, boolean isEditable) {
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
