package org.spa.ui.table;

import javax.swing.*;

/**
 * Defines table configuration to be used when working with {@link TableManager}
 * @author hadrian
 * @since 12-May-20
 */
public class TableConfig {
   private boolean isTooltipAllowed = true;
   private boolean isEditable = false;
   private boolean isBorderDisplayed = true;
   private boolean isColumnReorderingAllowed = false;
   private boolean isColumnResizingAllowed = false;
   private int rowHeight = 20;
   private int selectionMode = ListSelectionModel.SINGLE_SELECTION; // Single line selection in table
   private double linesInRow = 0; // Overrides rowHeight by calculating the height based on amount of lines in a row.

   private TableConfig() {
   }

   public static TableConfigBuilder create() {
      return new TableConfigBuilder();
   }

   public static TableConfig getDefault() {
      return new TableConfigBuilder().build();
   }

   public boolean isTooltipAllowed() {
      return isTooltipAllowed;
   }

   public boolean isEditable() {
      return isEditable;
   }

   public boolean isBorderDisplayed() {
      return isBorderDisplayed;
   }

   public boolean isColumnReorderingAllowed() {
      return isColumnReorderingAllowed;
   }

   public boolean isColumnResizingAllowed() {
      return isColumnResizingAllowed;
   }

   public int getRowHeight() {
      return rowHeight;
   }

   public int getSelectionMode() {
      return selectionMode;
   }

   public double getLinesInRow() {
      return linesInRow;
   }

   /**
    * A builder utility to ease custom creations of {@link TableConfig}
    */
   public static class TableConfigBuilder {
      private final TableConfig tableConfig;

      public TableConfigBuilder() {
         tableConfig = new TableConfig();
      }

      public TableConfig build() {
         return tableConfig;
      }

      public TableConfigBuilder withPopup(boolean isPopupDisplayed) {
         tableConfig.isTooltipAllowed = isPopupDisplayed;
         return this;
      }

      public TableConfigBuilder withEditable(boolean isEditable) {
         tableConfig.isEditable = isEditable;
         return this;
      }

      public TableConfigBuilder withBorder(boolean isDisplayBorder) {
         tableConfig.isBorderDisplayed = isDisplayBorder;
         return this;
      }

      public TableConfigBuilder withColumnReordering(boolean isColumnReorderingAllowed) {
         tableConfig.isColumnReorderingAllowed = isColumnReorderingAllowed;
         return this;
      }

      public TableConfigBuilder withColumnResizing(boolean isColumnResizingAllowed) {
         tableConfig.isColumnResizingAllowed = isColumnResizingAllowed;
         return this;
      }

      public TableConfigBuilder withRowHeight(int rowHeight) {
         tableConfig.rowHeight = rowHeight;
         return this;
      }

      public TableConfigBuilder withSelectionMode(int selectionMode) {
         tableConfig.selectionMode = selectionMode;
         return this;
      }

      public TableConfigBuilder withLinesInRow(double linesInRow) {
         tableConfig.linesInRow = linesInRow;
         return this;
      }
   }
}
