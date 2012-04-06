package org.iplantc.core.tito.client.models;

import org.iplantc.core.metadata.client.JSONMetaDataObject;

import com.extjs.gxt.ui.client.data.BaseTreeModel;

/**
 * 
 * A model class to represent JSON meta-data of a tool in a tree structure.
 * 
 * @author sriram
 * 
 */
public class MetaDataTreeModel extends BaseTreeModel {
    public enum TreeElementType {
        /** An input or output parameter to a tool */
        PARAMETER,
        /** Descriptive text to display in the tool */
        STATIC_TEXT,
        /** A container for parameters and static text */
        GROUP,
        /** The root of the parameter tree; contains groups */
        CONTAINER
    };

    public static final String LABEL_PROPERTY = "label"; //$NON-NLS-1$
    public static final String ID_PROPERTY = "id"; //$NON-NLS-1$
    public static final String TYPE_PROPERTY = "type"; //$NON-NLS-1$
    public static final String OBJECT_PROPERTY = "object"; //$NON-NLS-1$

    /**
	 * 
	 */
    private static final long serialVersionUID = -1345749176147788434L;

    /**
     * 
     * create a new instance of MetaDataTreeModel
     * @param type
     * @param object
     */
    public MetaDataTreeModel(TreeElementType type, JSONMetaDataObject object) {
        set(ID_PROPERTY, object.getId());
        set(LABEL_PROPERTY, object.getLabel());
        set(TYPE_PROPERTY, type);
        set(OBJECT_PROPERTY, object);
    }

    /**
     * get the id
     * 
     * @return
     */
    public String getId() {
        return (String)get(ID_PROPERTY);
    }

    /**
     * set the label
     * 
     * @param label
     */
    public void setLabel(String label) {
        set(LABEL_PROPERTY, label);
    }

    /**
     * get the label
     * 
     * @return
     */
    public String getLabel() {
        return (String)get(LABEL_PROPERTY);
    }

    /**
     * get the type
     * 
     * @return type
     */
    public TreeElementType getType() {
        return get(TYPE_PROPERTY);
    }

    /**
     * Sets the OBJECT_PROPERTY property and property values derived from it.
     * 
     * @param object
     */
    public void setObject(JSONMetaDataObject object) {
        set(OBJECT_PROPERTY, object);
        set(LABEL_PROPERTY, object.getLabel());
        set(ID_PROPERTY, object.getId());
    }

    /**
     * get the meta-data object
     * 
     * @return an meta-data object
     */
    public JSONMetaDataObject getObject() {
        return get(OBJECT_PROPERTY);
    }
}
