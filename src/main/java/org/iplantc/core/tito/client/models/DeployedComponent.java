package org.iplantc.core.tito.client.models;

import org.iplantc.core.jsonutil.JsonUtil;

import com.extjs.gxt.ui.client.data.BaseModelData;

@SuppressWarnings("serial")
/**
 * A model that represents a tool or workflow or a pipe line that is deployed into the discovery environment
 * 
 */
public class DeployedComponent extends BaseModelData {

    public static final String ID = "id";
    public static final String NAME = "name"; //$NON-NLS-1$
    public static final String LOCATION = "location"; //$NON-NLS-1$
    public static final String VERSION = "version"; //$NON-NLS-1$
    public static final String DESC = "description";
    public static final String TYPE = "type";
    public static final String ATTRIB = "attribution";

    /**
     * create a new instance of DeployedComponent
     * 
     * @param id
     * @param name
     * @param type
     * @param desc
     * @param attribution
     * @param location
     * @param version
     */
    public DeployedComponent(final String id, final String name, final String type, final String desc,
            final String attribution, final String location, final String version) {
        init(id, name, type, desc, attribution, location, version);
    }

    
    private void init(final String id, final String name, final String type, final String desc,
            final String attribution, final String location, final String version) {
        set(ID, id); //$NON-NLS-1$
        set(NAME, name);
        set(TYPE, type); //$NON-NLS-1$
        set(DESC, desc); //$NON-NLS-1$
        set(ATTRIB, attribution); //$NON-NLS-1$
        set(LOCATION, location);
        set(VERSION, version);
    }

    /**
     * get the id
     * 
     * @return id
     */
    public String getId() {
        return get(ID); //$NON-NLS-1$
    }

    /**
     * get the name
     * 
     * @return name
     */
    public String getName() {
        return get(NAME);
    }

    /**
     * get the type
     * 
     * @return type
     */
    public String getType() {
        return get(TYPE); //$NON-NLS-1$
    }

    /**
     * get the description
     * 
     * @return description
     */
    public String getDescription() {
        return get(DESC); //$NON-NLS-1$
    }

    /**
     * get the location of the tool
     * 
     * @return location
     */
    public String getLocation() {
        return get(LOCATION);
    }

    /**
     * get the tool attribution
     * 
     * @return tool attribution
     */
    public String getAttribution() {
        return get(ATTRIB); //$NON-NLS-1$
    }

    /**
     * get the tool version
     * 
     * @return version
     */
    public String getVersion() {
        return get(VERSION);
    }

    private String buildJsonString(final String key, boolean addComma) {
        StringBuffer ret = new StringBuffer("\"" + key + "\": "); //$NON-NLS-1$ //$NON-NLS-2$

        String val = get(key);

        // escape quotes and new lines
        val = JsonUtil.escapeQuotes(val);
        val = JsonUtil.escapeNewLine(val);

        // add our value
        ret.append("\"" + val + "\""); //$NON-NLS-1$ //$NON-NLS-2$

        // do we need to add a comma?
        if (addComma) {
            ret.append(", "); //$NON-NLS-1$
        }

        return ret.toString();
    }

    /**
     * covert this model into a json
     * 
     * @return a json string representation of DeployedComponent
     */
    public String toJsonString() {
        StringBuffer ret = new StringBuffer();

        ret.append("{"); //$NON-NLS-1$

        ret.append(buildJsonString("id", true)); //$NON-NLS-1$
        ret.append(buildJsonString(NAME, true));
        ret.append(buildJsonString("type", true)); //$NON-NLS-1$
        ret.append(buildJsonString("description", true)); //$NON-NLS-1$
        ret.append(buildJsonString("attribution", true)); //$NON-NLS-1$
        ret.append(buildJsonString(LOCATION, true));
        ret.append(buildJsonString(VERSION, false));

        ret.append("}"); //$NON-NLS-1$

        return ret.toString();
    }

}
