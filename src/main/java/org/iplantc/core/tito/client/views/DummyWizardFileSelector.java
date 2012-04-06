package org.iplantc.core.tito.client.views;

import org.iplantc.core.client.widgets.utils.ComponentValueTable;
import org.iplantc.core.client.widgets.utils.GeneralTextFormatter;
import org.iplantc.core.client.widgets.validator.IPlantValidator;
import org.iplantc.core.metadata.client.property.Property;
import org.iplantc.core.metadata.client.validation.MetaDataValidator;
import org.iplantc.core.uidiskresource.client.models.File;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.google.gwt.user.client.Command;

public class DummyWizardFileSelector extends DummyFileSelector {

    private ComponentValueTable tblComponentVals;

    /**
     * Instantiate from a property and component value table.
     * 
     * @param property template for instantiation.
     * @param tblComponentVals table to register with.
     */
    public DummyWizardFileSelector(final Property property, final ComponentValueTable tblComponentVals) {
        this.tblComponentVals = tblComponentVals;

        setId(property.getId());
        initValidator(property);
        tblComponentVals.setFormatter(getId(), new GeneralTextFormatter());

        cmdChange = new Command() {
            @Override
            public void execute() {
                handleSelectedFileChange();
            }
        };
    }

    private void initValidator(final Property property) {
        IPlantValidator validator = buildValidator(property);

        if (validator != null) {
            setValidator(validator);

            tblComponentVals.setValidator(getId(), validator);
        }
    }

    private IPlantValidator buildValidator(final Property property) {
        IPlantValidator ret = null; // assume failure
        MetaDataValidator validator = property.getValidator();

        if (validator != null) {
            ret = new IPlantValidator(tblComponentVals, validator);
        }

        return ret;
    }

    private void handleSelectedFileChange() {
        tblComponentVals.setValue(getId(), txtFilename.getValue());

        // after we update the table, we need to validate the entire table
        tblComponentVals.validate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initWidgets() {
        super.initWidgets();

        txtFilename.addListener(Events.OnClick, new Listener<BaseEvent>() {
            public void handleEvent(final BaseEvent be) {
                launchDialog();
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSelectedFile(File file) {
        super.setSelectedFile(file);

        handleSelectedFileChange();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void displayFilename(String name) {
        super.displayFilename(name);

        tblComponentVals.setValue(getId(), name);

        tblComponentVals.validate();
    }

}
