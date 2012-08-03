package org.iplantc.core.tito.client.models;

import java.util.Arrays;

import org.iplantc.core.jsonutil.JsonUtil;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

/**
 * Model to store JSON retrieved from the data_sources endpoint.
 * 
 * @author psarando
 * 
 */
public class DataSource extends BaseModelData {
    private static final long serialVersionUID = -1834169004323695445L;

    public static String ID = "id"; //$NON-NLS-1$
    public static String NAME = "name"; //$NON-NLS-1$
    public static String LABEL = "label"; //$NON-NLS-1$

    public DataSource(JSONObject json) {
        for (String key : Arrays.asList(ID, NAME, LABEL)) {
            set(key, JsonUtil.getString(json, key));
        }
    }

    public String getId() {
        return get(ID);
    }

    public String getName() {
        return get(NAME);
    }

    public String getLabel() {
        return get(LABEL);
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();

        json.put(ID, new JSONString(getId()));
        json.put(NAME, new JSONString(getName()));
        json.put(LABEL, new JSONString(getLabel()));

        return json;
    }
}
