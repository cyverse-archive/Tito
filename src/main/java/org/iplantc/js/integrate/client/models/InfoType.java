package org.iplantc.js.integrate.client.models;

import com.extjs.gxt.ui.client.data.BaseModelData;

/**
 * A model that represents a info type
 * 
 * @author sriram
 *
 */
public class InfoType extends BaseModelData {
    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public static String ID = "id"; //$NON-NLS-1$
    public static String NAME = "name"; //$NON-NLS-1$
    public static String DESCRIPTION = "description"; //$NON-NLS-1$

    /**
     * create a new instance of info type
     * 
     * @param id
     * @param name
     * @param description
     */
    public InfoType(String id, String name, String description) {
        set(ID, id);
        set(NAME, name);
        set(DESCRIPTION, description);
    }

    /**
     * 
     * get the id 
     * 
     * @return id
     */
    public String getId() {
        if (get(ID) == null) {
            return ""; //$NON-NLS-1$
        } else {
            return get(ID).toString();
        }
    }

    /**
     * 
     * get the name
     * 
     * @return name
     */
    public String getName() {
        if (get(NAME) == null) {
            return ""; //$NON-NLS-1$
        } else {
            return get(NAME).toString();
        }
    }

    /**
     * get the description
     * 
     * 
     * @return description
     */
    public String getDescription() {
        if (get(DESCRIPTION) == null) {
            return ""; //$NON-NLS-1$
        } else {
            return get(DESCRIPTION).toString();
        }
    }
}
