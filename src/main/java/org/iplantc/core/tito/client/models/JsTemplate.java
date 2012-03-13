package org.iplantc.core.tito.client.models;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * 
 * A class with native methods to access fields from an json array of Template
 * 
 * @author sriram
 * 
 */
public class JsTemplate extends JavaScriptObject {

    protected JsTemplate() {

    }

    /**
     * Gets the name value of the template from the object.
     * 
     * @return a string representing the name of the template
     */
    public final native String getName() /*-{
		return this.name;
    }-*/;

    public final native String getStatus() /*-{
		return this.status;
    }-*/;

    /**
     * Gets an internal identifier value for the template from the object.
     * 
     * @return a string representing a unique identifier for the template
     */
    public final native String getId() /*-{
		return this.tito;
    }-*/;

    public final native String getLastEditedDate() /*-{
		return this.edited_date;
    }-*/;

    public final native String getPublishedDate() /*-{
		return this.published_date;
    }-*/;

    public final native boolean isPUblic() /*-{
		return this.is_public;
    }-*/;

    public final native boolean isPublishable() /*-{
		return this.is_publishable;
    }-*/;

    public final native boolean isOrdered() /*-{
		return this.is_ordered;
    }-*/;
}
