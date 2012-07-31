package org.iplantc.core.tito.client.utils;

import org.iplantc.core.metadata.client.property.DataObject;
import org.iplantc.core.metadata.client.property.Property;

/**
 * Utility class for Property metadata models.
 * 
 * @author psarando
 * 
 */
public class PropertyUtil {
    public static final String TYPE_STATIC_TEXT = "Info"; //$NON-NLS-1$

    /**
     * Checks if the given property should be ordered in order to be used by an App at analysis
     * execution. For example, static text is never used at execution, but input parameters need an order
     * on the command line to be used by the App.
     * 
     * @param prop
     * @return true if the property can be used at analysis execution but needs an order.
     */
    public static boolean orderingRequired(Property prop) {
        if (prop == null) {
            return false;
        }

        String propertyType = prop.getType();

        if (TYPE_STATIC_TEXT.equals(propertyType)) {
            return false;
        }

        DataObject dataObject = prop.getDataObject();
        if (DataObject.OUTPUT_TYPE.equals(propertyType) && dataObject != null && dataObject.isImplicit()) {
            return false;
        }

        return true;
    }

}
