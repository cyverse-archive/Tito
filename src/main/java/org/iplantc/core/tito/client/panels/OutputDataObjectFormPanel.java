package org.iplantc.core.tito.client.panels;

import org.iplantc.core.metadata.client.property.DataObject;
import org.iplantc.core.metadata.client.property.Property;
import org.iplantc.core.tito.client.I18N;

import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.gwt.user.client.Command;

/**
 * A form panel to collect output DataObject
 * 
 * @author sriram
 *
 */
public class OutputDataObjectFormPanel extends DataObjectFormPanel {
    private static final String ID_FLD_OP_NAME = "idFldOpName"; //$NON-NLS-1$

    protected TextField<String> outputFileNameField;
    private Command outputFilenameChangeCommand;

    public OutputDataObjectFormPanel(Property property) {
        super(property);
    }

    @Override
    protected String getMultiplicityLabel() {
        return I18N.DISPLAY.outPutMultiplicityOption();
    }

    @Override
    protected void addFields() {
        add(new Label(outputFileNameField.getFieldLabel() + ":")); //$NON-NLS-1$
        add(outputFileNameField);

        super.addFields();
    }

    /**
     * Sets a command that will be called when the "output file" field changes.
     * 
     * @param cmd
     */
    public void setOutputFilenameChangeCommand(Command cmd) {
        outputFilenameChangeCommand = cmd;
    }

    /**
     * set the form field values from the DataObject
     * 
     * @param obj an instance of DataObject to be loaded into this form
     */
    @Override
    protected void initFieldValues() {
        DataObject obj = getDataObject();

        if (obj != null) {
            initTextField(outputFileNameField, obj.getOutputFilename());
        }

        super.initFieldValues();
    }

    @Override
    protected void buildFields() {
        buildOutputFileNameField();

        super.buildFields();
    }

    public String getOutputFilename() {
        String filename = outputFileNameField.getValue();
        return filename == null ? "" : filename; //$NON-NLS-1$
    }

    private void buildOutputFileNameField() {
        outputFileNameField = buildTextField(ID_FLD_OP_NAME, 100, 100,
                new OutputFilenameKeyUpCommand());

        outputFileNameField.setFieldLabel(I18N.DISPLAY.outputFileName());
        outputFileNameField.setAllowBlank(false);
        outputFileNameField.setStyleAttribute("padding-bottom", "5px"); //$NON-NLS-1$ //$NON-NLS-2$
    }

    private class OutputFilenameKeyUpCommand implements KeyUpCommand {
        @Override
        public void execute(String value) {
            getDataObject().setOutputFilename(value);
            outputFilenameChangeCommand.execute();
        }

        @Override
        public void handleNullInput() {
            getDataObject().setOutputFilename(""); //$NON-NLS-1$
            outputFilenameChangeCommand.execute();
        }
    }
}
