package org.spa.ui.table;

/**
 * @author Haim Adrian
 * @since 23-May-20
 */
public interface FocusedTableRowChangedListener<Model extends TableModelIfc> {
   void onFocusedRowChanged(int rowNumber, Model selectedModel);
}
