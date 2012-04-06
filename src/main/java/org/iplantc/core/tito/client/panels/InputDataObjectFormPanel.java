package org.iplantc.core.tito.client.panels;

import org.iplantc.core.metadata.client.property.DataObject;

import com.extjs.gxt.ui.client.widget.Label;

/**
 * A form panel to collect input DataObject
 * 
 * @author sriram
 *
 */
public class InputDataObjectFormPanel extends DataObjectFormPanel {

    public InputDataObjectFormPanel(DataObject obj) {
        super(obj);
    }

    @Override
    protected void addFields() {
    	add(new Label(multiplicityGroup.getFieldLabel() + ":"));
        add(multiplicityGroup);
        add(new Label(infoTypeField.getFieldLabel()+":"));
        add(infoTypeField);
    }
}
