package org.iplantc.core.tito.client.panels;

import org.iplantc.core.metadata.client.property.DataObject;

import com.extjs.gxt.ui.client.widget.Label;

/**
 * A form panel to collect output DataObject
 * 
 * @author sriram
 *
 */
public class OutputDataObjectFormPanel extends DataObjectFormPanel {

    public OutputDataObjectFormPanel(DataObject obj) {
        super(obj);
    }

    @Override
    protected void addFields() {
        add(new Label(outputFileNameField.getFieldLabel() + ":")); //$NON-NLS-1$
        add(outputFileNameField);
        add(new Label(multiplicityGroup.getFieldLabel() + ":"));
        add(multiplicityGroup);
        add(new Label(infoTypeField.getFieldLabel()+":"));
        add(infoTypeField);
    }
}