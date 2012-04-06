package org.iplantc.core.tito.client.utils;

import org.iplantc.core.metadata.client.JSONMetaDataObject;
import org.iplantc.core.metadata.client.property.Property;
import org.iplantc.core.metadata.client.property.groups.PropertyGroup;
import org.iplantc.core.metadata.client.property.groups.PropertyGroupContainer;
import org.iplantc.core.tito.client.models.MetaDataTreeModel;
import org.iplantc.core.tito.client.models.MetaDataTreeModel.TreeElementType;

import com.extjs.gxt.ui.client.store.TreeStore;

/**
 * 
 * A util class for parsing PropertyGroupContainer
 * 
 * @author sriram
 *
 */
public class PropertyGroupContainerUtil {

    /**
     * builds a store from the given PropertyGroupContainer
     * 
     * @param container instance of PropertyGroupContainer
     * @return a store containing MetaDataTreeModel
     */
    public static TreeStore<MetaDataTreeModel> buildStore(PropertyGroupContainer container) {
        MetaDataTreeModel parent = buildGroupContainerTreeModel(container);

        TreeStore<MetaDataTreeModel> store = new TreeStore<MetaDataTreeModel>();
        store.add(parent, true);

        return store;
    }

    private static MetaDataTreeModel buildGroupContainerTreeModel(PropertyGroupContainer container) {
        MetaDataTreeModel containerModel = new MetaDataTreeModel(TreeElementType.CONTAINER, container);

        // loop through our groups to get at our properties
        for (PropertyGroup group : container.getGroups()) {
            MetaDataTreeModel groupModel = buildGroupTreeModel(group);

            containerModel.add(groupModel);
            groupModel.setParent(containerModel);
        }

        return containerModel;
    }

    private static MetaDataTreeModel buildGroupTreeModel(PropertyGroup group) {
        MetaDataTreeModel groupModel = new MetaDataTreeModel(TreeElementType.GROUP, group);

        // loop through our properties to seed our table
        for (JSONMetaDataObject property : group.getElements()) {
            MetaDataTreeModel propertyModel = new MetaDataTreeModel(TreeElementType.PARAMETER, property);

            propertyModel.setParent(groupModel);
            groupModel.add(propertyModel);
        }

        return groupModel;
    }
    
    /**
     * check if the JSONMetaDataObject is instance of PropertyGroupContainer
     * 
     * @param obj a  JSONMetaDataObject
     * @return boolean
     */
    public static boolean isInstanceOfPropertyGroupContainer(JSONMetaDataObject obj) {
        return obj instanceof PropertyGroupContainer;
    }

    /**
     * check if the JSONMetaDataObject is instance of PropertyGroup
     * 
     * @param obj  a  JSONMetaDataObject
     * @return boolean
     */
    public static boolean isInstanceOfPropertyGroup(JSONMetaDataObject obj) {
        return obj instanceof PropertyGroup;
    }

    /**
     * check if the JSONMetaDataObject is instance of Property
     * 
     * @param obj  a  JSONMetaDataObject
     * @return boolean
     */
    public static boolean isInstanceOfProperty(JSONMetaDataObject obj) {
        return obj instanceof Property;
    }

}
