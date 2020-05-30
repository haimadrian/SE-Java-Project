package org.spa.ui;

import org.spa.common.SPAApplication;
import org.spa.controller.item.WarehouseItem;
import org.spa.ui.util.Fonts;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CategoryTree
{
    private JTree categoryTree;
    private  List<WarehouseItem> itemsRepository;
    private Set<String> itemsCategories;
    public CategoryTree(Window owner) {

        itemsRepository = SPAApplication.getInstance().getItemsWarehouse().getItems();
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");
        itemsCategories = itemsRepository.stream().map(WarehouseItem::getCategory).collect(Collectors.toSet());

        for (String category : itemsCategories) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(category);
            root.add(node);
        }

        categoryTree = new JTree(root);
        categoryTree.setShowsRootHandles(true);
        categoryTree.setRootVisible(false);
        categoryTree.setPreferredSize(new Dimension(200, owner.getPreferredSize().height - 250));
        categoryTree.setFont(Fonts.BIG_FONT);
        categoryTree.setBorder(BorderFactory.createLineBorder(Color.gray, 1));

        DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) categoryTree.getCellRenderer();
        renderer.setLeafIcon(null);
    }

    public JTree getCategoryTree() {
        return categoryTree;
    }
}
