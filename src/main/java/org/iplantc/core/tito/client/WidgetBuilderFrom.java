package org.iplantc.core.tito.client;

import org.iplantc.core.client.widgets.utils.ComponentValueTable;
import org.iplantc.core.metadata.client.JSONMetaDataObject;

import com.google.gwt.user.client.ui.Widget;

/**
 * Simple interface to generate widget from a JSONMetaDataObject.
 * 
 * @author amuir
 * 
 * @param <T>
 */
public interface WidgetBuilderFrom<T extends JSONMetaDataObject> {
    /**
     * Build a widget from meta data template.
     * 
     * @param metadataObject meta data template for the widget.
     * @param tblComponentVals table to register widget with.
     * @return newly created widget.
     */
    Widget build(T metadataObject, ComponentValueTable tblComponentVals);
}