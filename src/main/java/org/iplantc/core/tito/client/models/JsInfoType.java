package org.iplantc.core.tito.client.models;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * 
 * A class with native methods to access fields from an json array of InfoType
 * 
 * @author sriram
 *
 */
public class JsInfoType extends JavaScriptObject {
    protected JsInfoType() {

    }

    /**
     * Gets the name value of the info type from the object.
     * 
     * @return a string representing the name of the info type
     */
    public final native String getName() /*-{
                                         return this.name;
                                         }-*/;

    public final native String getDescription() /*-{
                                                return this.label;
                                                }-*/;

    /**
     * Gets an internal identifier value for the info type from the object.
     * 
     * @return a string representing a unique identifier for the info type
     */
    public final native String getId() /*-{
                                       return this.id;
                                       }-*/;
}
