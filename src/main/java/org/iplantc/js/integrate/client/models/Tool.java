package org.iplantc.js.integrate.client.models;

import org.iplantc.core.metadata.client.property.groups.PropertyGroupContainer;

import com.extjs.gxt.ui.client.data.BaseModelData;

/**
 * 
 * A model that represents a tool installed in DE
 * 
 * @author sriram
 *
 */
public class Tool extends BaseModelData {
    public static final String NAME_PROPERTY = "name"; //$NON-NLS-1$
    public static final String STATUS_PROPERTY = "status"; //$NON-NLS-1$
    public static final String CONTAINER_PROPERTY = "propertyGroupContainer"; //$NON-NLS-1$
    private static final long serialVersionUID = -6396192951059294894L;

    /**
     * create a new instance of Tool
     * 
     * @param container
     * @param status
     */
    public Tool(PropertyGroupContainer container, String status) {
        set(NAME_PROPERTY, container.getName());
        set(STATUS_PROPERTY, status);
        set(CONTAINER_PROPERTY, container);
    }

    /**
     * get the meta data PropertyGroupContainer
     * 
     * @return meta data PropertyGroupContainer
     */
    public PropertyGroupContainer getContainer() {
        return get(CONTAINER_PROPERTY);
    }
}
