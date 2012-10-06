package org.iplantc.core.tito.client.panels;

import org.iplantc.core.metadata.client.property.Property;
import org.iplantc.core.tito.client.dialogs.validation.BasicEditableList;
import org.iplantc.core.tito.client.dialogs.validation.EditableStringList;

import com.google.gwt.json.client.JSONArray;

/**
 * A ListboxEditorPanel that uses an EditableStringList.
 * 
 * @author psarando
 * 
 */
public class StringListboxEditorPanel extends ListEditorPanel {

    /**
     * @param property
     */
    public StringListboxEditorPanel(Property property) {
        super(property);
    }

    @Override
    protected BasicEditableList buildList(JSONArray values) {
        return new EditableStringList(values, new UpdatePropertyWithList());
    }
}
