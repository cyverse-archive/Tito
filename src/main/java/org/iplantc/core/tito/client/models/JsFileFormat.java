package org.iplantc.core.tito.client.models;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * 
 * A class with native methods to access fields from an json array of FileFormats
 * 
 * @author sriram
 *
 */

public class JsFileFormat extends JavaScriptObject {
    /**
     * Default constructor.
     */
    protected JsFileFormat() {
    }

    // Define JSNI methods to get File info from the native JavaScript Object
    /**
     * Gets the name value of the file from the object.
     * 
     * @return a string representing the name of the file
     */
    public final native String getName() /*-{
                                         return this.name;
                                         }-*/;

    public final native String getDescription() /*-{
                                                return this.description;
                                                }-*/;

    /**
     * Gets an internal identifier value for the file from the object.
     * 
     * @return a string representing a unique identifier for the file
     */
    public final native String getId() /*-{
                                       return this.id;
                                       }-*/;
}
