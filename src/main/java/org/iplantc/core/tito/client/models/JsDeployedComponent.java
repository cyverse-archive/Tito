package org.iplantc.core.tito.client.models;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * 
 * A class with native methods to access fields from an json array of DeployedComponents
 * 
 * @author sriram
 *
 */
public class JsDeployedComponent extends JavaScriptObject {

    protected JsDeployedComponent() {

    }

    /**
     * Gets the name value of the dc from the object.
     * 
     * @return a string representing the name of the dc
     */
    public final native String getName() /*-{
                                         return this.name;
                                         }-*/;

    /**
     * Gets the description value of the dc from the object.
     * 
     * @return a string representing the description of the dc
     */
    public final native String getDescription() /*-{
                                                return this.description;
                                                }-*/;

    /**
     * Gets an internal identifier value for the dc from the object.
     * 
     * @return a string representing a unique identifier for the dc
     */
    public final native String getId() /*-{
                                       return this.id;
                                       }-*/;

    /**
     * Gets the name value of the dc from the object.
     * 
     * @return a string representing the name of the dc
     */
    public final native String getType() /*-{
                                         return this.type
                                         }-*/;

    /**
     * Gets the location value of the dc from the object.
     * 
     * @return a string representing the location of the dc
     */
    public final native String getLocation() /*-{
                                             return this.location;
                                             }-*/;

    /**
     * Gets the version value of the dc from the object.
     * 
     * @return a string representing the version of the dc
     */
    public final native String getVersion() /*-{
                                            return this.version;
                                            }-*/;

    /**
     * Gets the attribution value of the dc from the object.
     * 
     * @return a string representing the attribution of the dc
     */
    public final native String getAttribution() /*-{
                                                return this.attribution;
                                                }-*/;

}
