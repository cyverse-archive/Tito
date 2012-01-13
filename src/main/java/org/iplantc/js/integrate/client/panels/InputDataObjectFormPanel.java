package org.iplantc.js.integrate.client.panels;

import org.iplantc.core.metadata.client.property.DataObject;

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
        add(multiplicityGroup);
        add(infoTypeField);
    }
}
