package org.spa.ui;

import org.spa.common.SPAApplication;
import org.spa.controller.item.ItemsWarehouse;
import org.spa.controller.item.ItemsWarehouseObserver;
import org.spa.controller.item.WarehouseItem;
import org.spa.ui.util.Fonts;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CategoryTree implements ItemsWarehouseObserver {
   private JTree categoryTree;
   private List<WarehouseItem> itemsRepository;
   private ItemsWarehouse itemsWarehouse;
   private Set<String> itemsCategories;

   public CategoryTree() {
      itemsWarehouse = SPAApplication.getInstance().getItemsWarehouse();
      itemsWarehouse.registerObserver(this);
      categoryTree = new JTree();
      createCategoryTree();
      categoryTree.setShowsRootHandles(true);
      categoryTree.setRootVisible(false);
      categoryTree.setFont(Fonts.BIG_FONT);

      DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) categoryTree.getCellRenderer();
      renderer.setLeafIcon(null);
   }

   private void createCategoryTree() {
      itemsRepository = SPAApplication.getInstance().getItemsWarehouse().getItems();
      itemsCategories = itemsRepository.stream().map(WarehouseItem::getCategory).collect(Collectors.toSet());
      DefaultTreeModel model = (DefaultTreeModel) categoryTree.getModel();
      DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");
      for (String category : itemsCategories) {
         DefaultMutableTreeNode node = new DefaultMutableTreeNode(category);
         root.add(node);
      }
      model.setRoot(root);
      model.reload(root);
   }

   public void clear() {
      categoryTree.clearSelection();
   }

   public JTree getCategoryTree() {
      return categoryTree;
   }

   @Override
   public void deleteItem(WarehouseItem item) {
      createCategoryTree();
   }

   @Override
   public void updateItem(WarehouseItem item) {
      createCategoryTree();
   }

   @Override
   public void addItem(WarehouseItem item) {
      createCategoryTree();
   }

}
