package org.iplantc.core.tito.client.models;

import com.extjs.gxt.ui.client.data.BaseModelData;

/**
 * Represents categories for Tito integrations.
 * @author hariolf
 *
 */
public class ToolCategory extends BaseModelData {
    private static final long serialVersionUID = 477261380297981443L;

    /** the field that stores the name of the category */
    public static final String NAME_PROPERTY = "name"; //$NON-NLS-1$
    /** the field that stores the category id */
    public static final String ID_PROPERTY = "id"; //$NON-NLS-1$

    /**
     * Creates a new ToolCategory.
     * @param name the display name
     * @param id
     */
    public ToolCategory(String name, String id) {
        set(NAME_PROPERTY, name);
        set(ID_PROPERTY, name);
    }
    
    @Override
    public String toString() {
        return get(ID_PROPERTY);
    }
}
