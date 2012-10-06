package org.iplantc.core.tito.client.panels;

import org.iplantc.core.metadata.client.property.Property;
import org.iplantc.core.tito.client.dialogs.validation.BasicEditableList;
import org.iplantc.core.tito.client.dialogs.validation.EditableNumberList;

import com.google.gwt.json.client.JSONArray;

/**
 * A ListboxEditorPanel that uses an EditableNumberList.
 * 
 * @author psarando
 * 
 */
public class NumberListboxEditorPanel extends ListEditorPanel {

    /**
     * @param property
     */
    public NumberListboxEditorPanel(Property property) {
        super(property);
    }

    @Override
    protected BasicEditableList buildList(JSONArray values) {
        return new EditableNumberList(values, new UpdatePropertyWithList());
    }

}
