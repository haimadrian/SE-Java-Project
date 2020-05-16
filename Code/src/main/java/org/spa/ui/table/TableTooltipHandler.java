package org.spa.ui.table;

import org.spa.common.util.log.Logger;
import org.spa.common.util.log.factory.LoggerFactory;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * This is used to display tooltips with cell data when mouse is above certain cell. Very useful when cell
 * contents exceeds cell boundaries.
 * @author hadrian
 * @since 12-May-20
 */
public class TableTooltipHandler extends MouseAdapter {
   private static final Logger logger = LoggerFactory.getLogger(TableTooltipHandler.class);
   private static final int MAX_TOOLTIP_WIDTH = 400;
   private static final int MAX_TOOLTIP_ROWS = 20;

   private int row = -1;
   private int col = -1;
   private final JTable table;

   static {
      ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);
   }

   /**
    * Constructs a new {@link TableTooltipHandler}
    * @param table The table to implement a tooltip for
    */
   public TableTooltipHandler(JTable table) {
      this.table = table;

      table.addMouseListener(this);
      table.addMouseMotionListener(this);
   }

   @Override
   public void mouseMoved(MouseEvent e) {
      int row = table.rowAtPoint(e.getPoint());
      int col = table.columnAtPoint(e.getPoint());

      if (this.row != row || this.col != col) {
         this.row = row;
         this.col = col;
         updateToolTip();
      }
   }

   @Override
   public void mouseEntered(MouseEvent e) {
      row = table.rowAtPoint(e.getPoint());
      col = table.columnAtPoint(e.getPoint());
      updateToolTip();
   }

   @Override
   public void mouseExited(MouseEvent e) {
      // hide tooltip
      table.setToolTipText("");

      // reset coordinates
      row = col = -1;
   }

   private void updateToolTip() {
      int modelCol = table.convertColumnIndexToModel(col);
      if ((table.getModel() != null) && (row >= 0 && row < table.getRowCount())) {
         Object cellValue = table.getModel().getValueAt(row, modelCol);
         if (cellValue == null) {
            setTooltipText("<null>");
         } else {
            String cellText = cellValue.toString();
            int stringWidth = table.getFontMetrics(table.getFont()).stringWidth(cellText);
            if (stringWidth > MAX_TOOLTIP_WIDTH) {
               int totalRows = (int)Math.floor((double)stringWidth / MAX_TOOLTIP_WIDTH + 0.5);
               int rowLength = cellText.length() / totalRows;

               StringBuilder html = new StringBuilder("<html><font face='sansserif' >");
               int spaceIndex = cellText.indexOf(" ");
               int fromIndex = 0;
               int splitIndex = rowLength;

               // Split the text to rows.
               int rows = 0;
               while (spaceIndex >= 0 && rows < MAX_TOOLTIP_ROWS) {
                  if (spaceIndex > splitIndex) {
                     html.append(cellText.substring(fromIndex, spaceIndex).replace("<", "&lt;").replace(">", "&gt;"));
                     html.append("<br>");
                     fromIndex = spaceIndex + 1;
                     splitIndex += rowLength;
                     ++rows;
                  }
                  spaceIndex = cellText.indexOf(" ", spaceIndex + 1);
               }

               if (spaceIndex >= 0 && rows == MAX_TOOLTIP_ROWS) {
                  html.append("...<br>");
               } else if (fromIndex < cellText.length()) {
                  html.append(cellText.substring(fromIndex).replace("<", "&lt;").replace(">", "&gt;"));
               }

               html.append("</font></html>");
               setTooltipText(html.toString());
            } else {
               setTooltipText(cellText);
            }
         }
      }
   }

   private void setTooltipText(String text) {
      logger.debug(() -> "Updating table tooltip to: '" + text + "'");
      table.setToolTipText(text);
   }
}
