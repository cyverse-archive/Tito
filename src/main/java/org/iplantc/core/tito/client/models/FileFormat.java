package org.iplantc.core.tito.client.models;

import com.extjs.gxt.ui.client.data.BaseModelData;

/**
 * A model that represents a file format
 * 
 * @author sriram
 *
 */
public class FileFormat extends BaseModelData {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public static String ID = "id"; //$NON-NLS-1$
    public static String NAME = "name"; //$NON-NLS-1$
    public static String DESCRIPTION = "description"; //$NON-NLS-1$

    /**
     * create a new instance if FileFormat
     * 
     * @param id
     * @param name
     * @param description
     */
    public FileFormat(String id, String name, String description) {
        set(ID, id);
        set(NAME, name);
        set(DESCRIPTION, description);
    }

    /**
     * get the id
     * 
     * @return id
     */
    public String getId() {
        return get(ID).toString();
    }

    /**
     * get the name
     * 
     * @return name
     */
    public String getName() {
        return get(NAME).toString();
    }

    /**
     * get the description 
     * 
     * @return description
     */
    public String getDescription() {
        return get(DESCRIPTION).toString();
    }

}
