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
   Name("Name", 0.15, String.class, new TextCellRenderer(), new TextCellEditor(true), true, 1),
   Description("Description", 0.46, String.class, new TextCellRenderer(), new TextCellEditor(true), true, 2),
   Price("Price", 0.1, Double.class, new TextCellRenderer(), null, false, 3),
   Count("Count", 0.06, Integer.class, new SpinnerCellRenderer(), new CountCellEditor(), true,4),
   Cart("Cart", 0.07, String.class, Constants.CART_BUTTON, Constants.CART_BUTTON, true, 4);

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
      public static final ButtonColumn CART_BUTTON = new ButtonColumn(table -> SwingUtilities.invokeLater(() -> {
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
         }));
   }
}
