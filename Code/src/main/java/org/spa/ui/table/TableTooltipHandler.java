package org.spa.ui.table;

import org.apache.commons.text.StringEscapeUtils;
import org.spa.common.util.StringUtils;
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
            String cellText = cellValueToString(cellValue);
            if (cellText == null) {
               cellText = cellValue.getClass().getSimpleName();
            }

            int stringWidth = table.getFontMetrics(table.getFont()).stringWidth(cellText);
            int width = Math.min(stringWidth, MAX_TOOLTIP_WIDTH);

            // @formatter:off
            String html = "<html><font face=\"sans-serif\"><div style=\"width:" + width + ";\">" +
                  StringUtils.replaceWildcardWithHTMLStyle(StringEscapeUtils.escapeHtml4(StringUtils.replaceHTMLStyleWithWildcard(cellText))).replace("\n", "<br/>") +
                  "</div></font></html>";
            // formatter:on
            setTooltipText(html);
         }
      }
   }

   private String cellValueToString(Object cellValue) {
      if (cellValue instanceof ImageIcon) {
         return ((ImageIcon)cellValue).getDescription();
      } else if (cellValue instanceof JButton) {
         return ((JButton)cellValue).getText();
      } else if (cellValue instanceof JCheckBox) {
         return String.valueOf(((JCheckBox)cellValue).isSelected());
      } else {
         return String.valueOf(cellValue);
      }
   }

   private void setTooltipText(String text) {
      logger.debug(() -> "Updating table tooltip to: '" + text + "'");
      table.setToolTipText(text);
   }
}
