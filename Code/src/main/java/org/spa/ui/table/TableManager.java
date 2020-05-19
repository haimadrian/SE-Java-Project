package org.spa.ui.table;

import org.spa.ui.util.Fonts;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * @author hadrian
 * @since 12-May-20
 */
public class TableManager<Column extends TableColumnIfc, Model extends TableModelIfc> {
   private final JPanel mainPanel;
   private final JTable table;
   private JScrollPane scrollPane;
   private TableModel tableModel;

   private final java.util.List<Column> columns;
   private java.util.List<Model> tableModelList;

   private final TableConfig tableConfig;

   @SuppressWarnings("unused")
   private TableTooltipHandler tableTooltipHandler;

   /**
    * Constructs a new {@link TableManager}
    * @param columns List of columns in this table. See {@link TableColumnIfc}
    * @param tableModelList List of rows in this table. See {@link TableModelIfc}
    * @param tableConfig The {@link TableConfig} to use for customizations of the table. May be <code>null</code> to use defaults.
    */
   public TableManager(java.util.List<Column> columns, java.util.List<Model> tableModelList, TableConfig tableConfig) {
      this.columns = columns;
      this.tableModelList = tableModelList;
      this.tableConfig = tableConfig == null ? TableConfig.getDefault() : tableConfig;

      mainPanel = new JPanel();
      mainPanel.setMinimumSize(new Dimension(200, 100));
      table = new JTable();

      createScrollPane();
      initTable();

      mainPanel.setLayout(new BorderLayout());
      mainPanel.add(scrollPane);
      mainPanel.addComponentListener(new ComponentAdapter() {
         @Override
         public void componentResized(ComponentEvent e) {
            onResize();
         }
      });
   }

   private void initTable() {
      table.setDoubleBuffered(true);
      table.setBackground(Color.DARK_GRAY);
      table.setForeground(Color.white);
      table.setFont(Fonts.PLAIN_FONT);
      table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
      table.setColumnSelectionAllowed(false);
      table.setRowSelectionAllowed(true);
      //noinspection MagicConstant
      table.setSelectionMode(tableConfig.getSelectionMode());
      table.setCellSelectionEnabled(true);
      table.setMinimumSize(new Dimension(200, 100));
      table.getTableHeader().setFont(Fonts.BOLD_FONT);
      table.getTableHeader().setBackground(Color.GRAY);
      table.getTableHeader().setForeground(Color.BLACK);
      table.getTableHeader().setReorderingAllowed(tableConfig.isColumnReorderingAllowed());
      table.getTableHeader().setResizingAllowed(tableConfig.isColumnResizingAllowed());

      int rowHeight = tableConfig.getRowHeight();
      // If there is a linesInRow customization, calculate row height based on that
      if (tableConfig.getLinesInRow() > 0) {
         double linesInRow = tableConfig.getLinesInRow();
         int lineHeight = table.getFontMetrics(table.getFont()).getHeight();
         rowHeight = (int) (linesInRow * lineHeight) + (lineHeight / 2);
      }
      table.setRowHeight(rowHeight);

      TableColumnModel tableColumnModel = new DefaultTableColumnModel();
      for (Column column : columns) {
         TableColumn tableColumn = new TableColumn(column.getColIndex());
         tableColumn.setIdentifier(Integer.valueOf(column.getColIndex()));
         tableColumn.setHeaderValue(column.getHeader());
         tableColumn.setCellRenderer(column.getCellRenderer());
         int colWidth = (int)(column.getWidth() * (mainPanel.getWidth() - scrollPane.getVerticalScrollBar().getWidth()));
         tableColumn.setPreferredWidth(colWidth);
         tableColumn.setWidth(colWidth);
         tableColumn.setMinWidth(colWidth / 3);

         if (column.isEditable()) {
            tableColumn.setCellEditor(column.getCellEditor());
         }

         tableColumnModel.addColumn(tableColumn);
      }
      table.setColumnModel(tableColumnModel);

      table.setAutoCreateColumnsFromModel(false);
      tableModel = new EditTableModel();

      table.setModel(tableModel);

      if (tableConfig.isTooltipAllowed()) {
         tableTooltipHandler = new TableTooltipHandler(table);
      }

      // Make sure row is selected when clicking it so we can use the popup menu on selected row
      table.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseReleased(MouseEvent e) {
            int r = table.rowAtPoint(e.getPoint());
            if (r >= 0 && r < table.getRowCount()) {
               table.setRowSelectionInterval(r, r);
            } else {
               table.clearSelection();
            }

            super.mouseReleased(e);
         }
      });
   }

   private void createScrollPane() {
      if (tableConfig.isBorderDisplayed()) {
         scrollPane = new JScrollPane(table);
      } else {
         scrollPane = new JScrollPane(table) {
            // Overriding getBorder() seems to be the only way I can force the table's border to be null.
            @Override
            public Border getBorder() {
               return null;
            }

            @Override
            public Insets getInsets() {
               return new Insets(0, 0, 0, 0);
            }
         };

         scrollPane.setViewportBorder(null);
         scrollPane.setBorder(null);
         scrollPane.getViewport().setBorder(null);
         table.setBorder(null);
      }

      scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
      scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
      scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
      scrollPane.setMinimumSize(new Dimension(200, Integer.MAX_VALUE));
      scrollPane.setPreferredSize(new Dimension(20, Integer.MAX_VALUE));
      scrollPane.getViewport().setBackground(Color.DARK_GRAY);
      scrollPane.getViewport().addChangeListener(e -> onResize());
   }

   /**
    * @param popupAdapter A customized adapter to support actions when right clicking the table.
    */
   public void setPopupAdapter(PopupAdapter popupAdapter) {
      if (popupAdapter != null) {
         popupAdapter.setComponent(table);
         table.addMouseListener(popupAdapter);
      }
   }

   /**
    * Outside user can update the table user list by using this method.<br/>
    * In case the list is the same reference that this {@link TableManager} was initialized with and there is only a
    * modification in it, e.g. new item or data update inside the items, you can call {@link #refresh()} and the table will
    * be refreshed to display the up to date ata
    * @param modelList The new list
    */
   public void replaceModelList(java.util.List<Model> modelList) {
      this.tableModelList = modelList;
      refresh();
   }

   /**
    * @return The main panel containing table and scrollbar, so it can be sit inside a frame
    */
   public JComponent getMainPanel() {
      return mainPanel;
   }

   /**
    * Call this method when there is a value update that the table should display. A value update can be
    * either new item or deleted item from the list that this {@link TableManager} was initialized with, or
    * a data inside the items of that list.<br/>
    * In case you need to change the list, use {@link #replaceModelList(List)}.
    */
   public void refresh() {
      table.revalidate();
      table.repaint();
      scrollPane.revalidate();
   }

   /**
    * Get a reference to the selected model in the table. May be <code>null</code> if there is no selection
    * @return The selected model in table
    */
   public Model getSelectedModel() {
      int row = table.getSelectedRow();

      if (row < 0 || row >= tableModelList.size()) {
         return null;
      }

      return tableModelList.get(row);
   }

   private void onResize() {
      // Recalculate column width
      for (Column column : columns) {
         TableColumn tableColumn = table.getColumn(Integer.valueOf(column.getColIndex()));
         int parentWidth = scrollPane.getParent().getWidth();
         if (scrollPane.getVerticalScrollBar().isVisible()) {
            parentWidth -= scrollPane.getVerticalScrollBar().getWidth();
         }
         int colWidth = (int)(column.getWidth() * parentWidth);
         tableColumn.setPreferredWidth(colWidth);
         tableColumn.setWidth(colWidth);
      }
   }

   /**
    * A table model that supports editing cells
    */
   private class EditTableModel extends DefaultTableModel {
      @Override
      public int getColumnCount() {
         return columns.size();
      }

      @Override
      public int getRowCount() {
         return tableModelList.size();
      }

      @Override
      public boolean isCellEditable(int row, int col) {
         Column column = TableManager.this.columns.get(col);
         return tableConfig.isEditable() && column.isEditable();
      }

      @Override
      public Object getValueAt(int row, int col) {
         if (row < 0 || row >= tableModelList.size())
            return "";

         Model model = tableModelList.get(row);
         Column column = columns.get(col);

         return column.formatValueForTable(model.getAttributeValue(column.getAttributeName()));
      }

      @Override
      public void setValueAt(Object value, int row, int col) {
         if (row < 0 || row >= tableModelList.size()) {
            return;
         }

         Model model = tableModelList.get(row);
         Column column = columns.get(col);

         model.setAttributeValue(column.getAttributeName(), value);
      }

      @Override
      public Class<?> getColumnClass(int col) {
         return columns.get(col).getColumnClass();
      }
   }
}
