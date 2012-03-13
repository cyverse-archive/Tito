package org.iplantc.core.tito.client.models;

import java.util.ArrayList;
import java.util.List;

import org.iplantc.core.jsonutil.JsonUtil;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;

/**
 * 
 * A model that represents the RuleType
 * 
 * @author sriram
 *
 */
public class RuleType extends BaseModelData {
    /**
	 * 
	 */
    private static final long serialVersionUID = -8067165423027703868L;
    public static String ID = "id"; //$NON-NLS-1$
    public static String NAME = "name"; //$NON-NLS-1$
    public static String DESCRIPTION = "description"; //$NON-NLS-1$
    public static String CATEGORIES = "categories"; // rule type category //$NON-NLS-1$
    public static String FORMAT = "rule_description_format"; //$NON-NLS-1$
    public static String SUBTYPE = "subtype"; //$NON-NLS-1$

    /**
     * create a new instance of RuleType
     * 
     * @param json
     */
    public RuleType(JSONObject json) {
        set(ID, json.get(ID).isString().stringValue());
        set(NAME, json.get(NAME).isString().stringValue());
        set(DESCRIPTION, json.get(DESCRIPTION).isString().stringValue());
        set(CATEGORIES, JsonUtil.getArray(json, "value_types")); //$NON-NLS-1$
        set(FORMAT, json.get(FORMAT).isString().stringValue());
        set(SUBTYPE, json.get(SUBTYPE).isString().stringValue());
    }

    private String getString(final String key) {
        String value = get(key);

        return (value == null) ? "" : value.toString(); //$NON-NLS-1$
    }

    /**
     * get the id
     * 
     * @return id
     */
    public String getId() {
        return getString(ID);
    }

    /**
     * get the name
     * 
     * @return
     */
    public String getName() {
        return getString(NAME);
    }

    /**
     * get the description
     * 
     * @return
     */
    public String getDescription() {
        return getString(DESCRIPTION);
    }

    /**
     * get the format
     * 
     * @return
     */
    public String getFormat() {
        return getString(FORMAT);
    }

    /**
     * get the rule subtype
     * 
     * @return
     */
    public String getSubtype() {
        return getString(SUBTYPE);
    }

    /**
     * get the rule categories
     * 
     * @return categories as JSONArray
     */
    public JSONArray getCategories() {
        if (get(CATEGORIES) != null) {
            return get(CATEGORIES);
        } else {
            return new JSONArray();
        }
    }

    /**
     * get the rule categories
     * 
     * @return categories as List
     */
    public List<String> getCategoriesAsList() {
        JSONArray categories = getCategories();

        List<String> list = new ArrayList<String>();

        for (int i = 0; i < categories.size(); i++) {
            list.add(categories.get(i).isString().stringValue());
        }

        return list;
    }
}
