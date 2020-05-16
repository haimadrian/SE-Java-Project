package org.spa.ui.table;

/**
 * @author hadrian
 * @since 12-May-20
 */
public interface TableModelIfc {
   Object getAttributeValue(String attributeName);
   void setAttributeValue(String attributeName, Object value);
}
