package org.spa.view;

import org.spa.controller.SPAApplication;
import org.spa.controller.item.ItemsWarehouse;
import org.spa.controller.item.ItemsWarehouseObserver;
import org.spa.controller.item.WarehouseItem;
import org.spa.view.util.Fonts;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CategoryTree implements ItemsWarehouseObserver {
   private final JTree categoryTree;
   private final JScrollPane scrollPane;
   private final ItemsWarehouse itemsWarehouse;
   private List<WarehouseItem> itemsRepository;
   private Set<String> itemsCategories;

   public CategoryTree() {
      itemsWarehouse = SPAApplication.getInstance().getItemsWarehouse();
      itemsWarehouse.registerObserver(this);
      categoryTree = new JTree();
      createCategoryTree();
      categoryTree.setShowsRootHandles(true);
      categoryTree.setRootVisible(false);
      categoryTree.setFont(Fonts.BIG_FONT);

      scrollPane = new JScrollPane(categoryTree);

      scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
      scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
      scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
      scrollPane.setMinimumSize(new Dimension(200, Integer.MAX_VALUE));
      scrollPane.setPreferredSize(new Dimension(20, 600));

      DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) categoryTree.getCellRenderer();
      renderer.setLeafIcon(null);
   }

   private void createCategoryTree() {
      itemsRepository = SPAApplication.getInstance().getItemsWarehouse().getItems();
      itemsCategories = itemsRepository.stream().map(WarehouseItem::getCategory).collect(Collectors.toSet());
      DefaultTreeModel model = (DefaultTreeModel) categoryTree.getModel();
      DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");
      itemsCategories.stream().sorted().forEachOrdered(category -> {
         DefaultMutableTreeNode node = new DefaultMutableTreeNode(category);
         root.add(node);
      });
      model.setRoot(root);
      model.reload(root);
   }

   public void clear() {
      categoryTree.clearSelection();
   }

   public JTree getCategoryTree() {
      return categoryTree;
   }

   public JComponent getContainer() {
      return scrollPane;
   }

   @Override
   public void onItemDeleted(WarehouseItem item) {
      createCategoryTree();
   }

   @Override
   public void onItemUpdated(WarehouseItem item) {
      createCategoryTree();
   }

   @Override
   public void onItemAdded(WarehouseItem item) {
      createCategoryTree();
   }

}
