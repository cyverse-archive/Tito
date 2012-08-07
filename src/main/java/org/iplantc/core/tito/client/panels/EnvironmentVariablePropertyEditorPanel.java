package org.iplantc.core.tito.client.panels;

import org.iplantc.core.metadata.client.property.Property;
import org.iplantc.core.tito.client.I18N;

/**
 * Panel for editing EnvironmentVariable Property types. EnvironmentVariable properties use the same
 * editor fields and validation rules as "Text" String types, except the "name" field is used for the
 * name of the Environment Variable rather than a command line option.
 * 
 * @author psarando
 * 
 */
public class EnvironmentVariablePropertyEditorPanel extends StringPropertyEditorPanel {

    public EnvironmentVariablePropertyEditorPanel(Property property) {
        super(property);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void init() {
        super.init();

        property.setOrder(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void buildFields() {
        super.buildFields();

        pnlCommandLineOption.setLabel(I18N.DISPLAY.environmentVariableName());
    }
}
