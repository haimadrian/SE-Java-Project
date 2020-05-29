package org.spa.ui;

import org.spa.common.SPAApplication;
import org.spa.controller.item.ItemsWarehouse;
import org.spa.controller.item.WarehouseItem;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.io.File;
import java.util.List;
import java.util.Map;

public class CategoryTree
{
    final String path = new File("src\\main\\resources\\org\\spa\\ui\\homepagestuff").getAbsolutePath();
    private JTree categoryTree;
    private final List<ItemsWarehouse> itemRepo;
    private Map<String, WarehouseItem> itemsWH;
    public CategoryTree()
    {
        itemRepo = (List<ItemsWarehouse>) SPAApplication.getInstance().getItemsWarehouse();

        //create the root node
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");
        //create the child nodes
        itemRepo.stream().forEach(item -> );
        DefaultMutableTreeNode cpu = new DefaultMutableTreeNode("CPU");
        DefaultMutableTreeNode gpu = new DefaultMutableTreeNode("GPU");
        DefaultMutableTreeNode ssd = new DefaultMutableTreeNode("SSD");
        DefaultMutableTreeNode hdd = new DefaultMutableTreeNode("HDD");
        DefaultMutableTreeNode screens = new DefaultMutableTreeNode("Screens");

        //add the child nodes to the root node
        root.add(cpu);
        root.add(gpu);
        root.add(ssd);
        root.add(hdd);
        root.add(screens);

        //create the tree by passing in the root node
        categoryTree = new JTree(root);
        categoryTree.setShowsRootHandles(true);
        categoryTree.setRootVisible(false);

        ImageIcon imageIcon = new ImageIcon(path+"\\leaf.gif","Leaf");
        DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) categoryTree.getCellRenderer();
        renderer.setLeafIcon(imageIcon);
    }

    public JTree getCategoryTree() {
        return categoryTree;
    }
}
