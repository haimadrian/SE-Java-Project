package org.spa.ui.item;

import org.spa.common.SPAApplication;
import org.spa.controller.cart.ShoppingCart;
import org.spa.controller.cart.ShoppingCartException;
import org.spa.controller.item.WarehouseItem;
import org.spa.ui.table.editor.ButtonColumn;
import org.spa.ui.table.TableColumnIfc;
import org.spa.ui.table.editor.CountCellEditor;
import org.spa.ui.table.editor.TextCellEditor;
import org.spa.ui.table.renderer.SpinnerCellRenderer;
import org.spa.ui.table.renderer.StretchedImageCellRenderer;
import org.spa.ui.table.renderer.TextCellRenderer;
import org.spa.ui.util.Dialogs;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 * @author Haim Adrian
 * @since 15-May-20
 */
public enum ItemColumn implements TableColumnIfc {
   Image("Image", 0.15, ImageIcon.class, new StretchedImageCellRenderer(10, ItemViewInfo.ADS_ATTRIBUTE_NAME), null, false, 0),
   Name("Name", 0.15, String.class, new TextCellRenderer(), null, false, 1),
   Description("Description", 0.46, String.class, new TextCellRenderer(), null, false, 2),
   Price("Price", 0.1, Double.class, new TextCellRenderer(), null, false, 3),
   Count("Count", 0.06, Integer.class, new SpinnerCellRenderer(), new CountCellEditor(), true,4),
   Cart("Cart", 0.07, String.class, Constants.CART_BUTTON, Constants.CART_BUTTON, true, 4),
   Delete("Delete", 0.07, String.class, Constants.DELETE_BUTTON, Constants.DELETE_BUTTON, true, 5);

   private final String header;
   private final double cellWidth;
   private final Class<?> columnClass;
   private final TableCellRenderer renderer;
   private final TableCellEditor editor;
   private final boolean isEditable;
   private final int index;


   ItemColumn(String header, double cellWidth, Class<?> columnClass, TableCellRenderer renderer, TableCellEditor editor, boolean isEditable, int index) {
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


   private static class Constants {
      public static final ButtonColumn DELETE_BUTTON = new ButtonColumn(table -> {
         // TODO: Implement RemoveFromWarehouseAction
         WarehouseItem selection = SPAApplication.getInstance().getItemsWarehouse().getSelectionModel().getSelection();
         if (selection != null) {
            SPAApplication.getInstance().getItemsWarehouse().removeItem(selection.getId());
            //            refreshTable();

         }
         /*ShoppingCart shoppingCart = SPAApplication.getInstance().getShoppingCart();
         int modelRow = table.getSelectedRow();
         String itemId = String.valueOf(table.getModel().getValueAt(modelRow, 0));
         try {
            shoppingCart.add(itemId, 0);
         } catch (ShoppingCartException ex) {
            SwingUtilities.invokeLater(() -> Dialogs.showErrorDialog(null, ex.getMessage(), "Error"));
         }
         ((DefaultTableModel) table.getModel()).removeRow(modelRow);*/
      });
      public static final ButtonColumn CART_BUTTON = new ButtonColumn(table -> {
         new Thread(() -> {
            try {
               Thread.sleep(100);
            } catch (InterruptedException ignore) {
            }

            SwingUtilities.invokeLater(() -> {
               WarehouseItem selection = SPAApplication.getInstance().getItemsWarehouse().getSelectionModel().getSelection();
               if (selection != null) {
                  String selectedItemId = selection.getId();
                  ShoppingCart shoppingCart = SPAApplication.getInstance().getShoppingCart();
                  try {
                     WarehouseItem shoppingCartItem = shoppingCart.getItems().stream().filter(item -> item.getId().equals(selectedItemId)).findFirst().orElse(null);
                     shoppingCart.add(selectedItemId, shoppingCartItem == null ? 1 : shoppingCartItem.getCount() + 1);
                  } catch (ShoppingCartException ex) {
                     SwingUtilities.invokeLater(() -> Dialogs.showErrorDialog(null, ex.getMessage(), "Error"));
                  }
               }
            });
         }, "AddToCartThread").start();
      });
   }
}
