package org.spa.ui;

import org.spa.ui.table.TableCellValue;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Consumer;

/**
 * The ButtonColumn class provides a renderer and an editor that looks like a
 * JButton. The renderer and editor will then be used for a specified column
 * in the table. The TableModel will contain the String to be displayed on
 * the button.
 * <p>
 * The button can be invoked by a mouse click or by pressing the space bar
 * when the cell has focus. Optionally a mnemonic can be set to invoke the
 * button. When the button is invoked the provided Action is invoked. The
 * source of the Action will be the table. The action command will contain
 * the model row number of the button that was clicked.
 */

public class ButtonColumn extends DefaultCellEditor implements TableCellRenderer, TableCellEditor {
    private final Consumer<JTable> listener;
    private int mnemonic;
    private Border originalBorder;
    private Border focusBorder;
    private JButton renderButton;
    private JButton editButton;
    private Object editorValue;
    private JTable table;

    /**
     * Create the ButtonColumn to be used as a renderer and editor. The
     * renderer and editor will automatically be installed on the TableColumn
     * of the specified column.
     *
     */
    public ButtonColumn(Consumer<JTable> listener) {
        super(new JTextField(""));
        this.listener = listener;
        renderButton = new JButton();
        editButton = new JButton();
        editButton.setFocusPainted(false);
        editButton.addActionListener(actionEvent -> listener.accept(table));
        originalBorder = editButton.getBorder();
        setFocusBorder(new LineBorder(Color.BLUE));
        setClickCountToStart(1);
    }


    /**
     * Get foreground color of the button when the cell has focus
     *
     * @return the foreground color
     */
    public Border getFocusBorder() {
        return focusBorder;
    }

    /**
     * The foreground color of the button when the cell has focus
     *
     * @param focusBorder the foreground color
     */
    public void setFocusBorder(Border focusBorder) {
        this.focusBorder = focusBorder;
        editButton.setBorder(focusBorder);
    }

    public int getMnemonic() {
        return mnemonic;
    }

    /**
     * The mnemonic to activate the button when the cell has focus
     *
     * @param mnemonic the mnemonic
     */
    public void setMnemonic(int mnemonic) {
        this.mnemonic = mnemonic;
        renderButton.setMnemonic(mnemonic);
        editButton.setMnemonic(mnemonic);
    }

    @Override
    public Component getTableCellEditorComponent(
            JTable table, Object value, boolean isSelected, int row, int column) {
        this.table = table;

        handleValueForButton(value, editButton);

        this.editorValue = value;
        return editButton;
    }

    @Override
    public Object getCellEditorValue() {
        if (editorValue == null) {
            return "";
        } else if (editorValue instanceof Icon) {
            return editorValue;
        } else if (editorValue instanceof TableCellValue) {
            return ((TableCellValue<?>)editorValue).getValue();
        } else {
            return editorValue;
        }
    }

    //
//  Implement TableCellRenderer interface
//
    public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        this.table = table;
        if (isSelected) {
            renderButton.setForeground(table.getSelectionForeground());
            renderButton.setBackground(table.getSelectionBackground());
        } else {
            renderButton.setForeground(table.getForeground());
            renderButton.setBackground(UIManager.getColor("Button.background"));
        }

        if (hasFocus) {
            renderButton.setBorder(focusBorder);
        } else {
            renderButton.setBorder(originalBorder);
        }

//		renderButton.setText( (value == null) ? "" : value.toString() );
        handleValueForButton(value, renderButton);

        return renderButton;
    }

    private void handleValueForButton(Object value, JButton renderButton) {
        if (value == null) {
            renderButton.setText("");
            renderButton.setIcon(null);
        } else if (value instanceof Icon) {
            renderButton.setText("");
            renderButton.setIcon((Icon) value);
        } else if (value instanceof TableCellValue) {
            renderButton.setText("");
            renderButton.setIcon((Icon) ((TableCellValue<?>) value).getValue());
        } else {
            renderButton.setText("");
            renderButton.setIcon(null);
        }
    }

}