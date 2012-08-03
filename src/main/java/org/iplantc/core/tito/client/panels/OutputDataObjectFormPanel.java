package org.iplantc.core.tito.client.panels;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.metadata.client.property.DataObject;
import org.iplantc.core.metadata.client.property.Property;
import org.iplantc.core.tito.client.I18N;
import org.iplantc.core.tito.client.models.DataSource;
import org.iplantc.core.tito.client.services.EnumerationServices;
import org.iplantc.core.uicommons.client.ErrorHandler;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * A form panel to collect output DataObject
 * 
 * @author sriram
 * 
 */
public class OutputDataObjectFormPanel extends DataObjectFormPanel {
    private static final String ID_IMPLICIT_OPT_CBX = "idImplicitOptCbx"; //$NON-NLS-1$
    private static final String ID_FLD_OP_NAME = "idFldOpName"; //$NON-NLS-1$
    private static final String ID_DATA_SRC_CBO = "idDataSrcType"; //$NON-NLS-1$

    private CheckBox cbxImplicitOutput;
    protected TextField<String> outputFileNameField;
    protected ComboBox<DataSource> comboDataSources;

    public OutputDataObjectFormPanel(Property property) {
        super(property);

        initDataSources();
    }

    /**
     * Populates the DataSource combo box.
     * 
     * @param selected
     */
    private void initDataSources() {
        EnumerationServices services = new EnumerationServices();

        services.getDataSources(new AsyncCallback<String>() {
            @Override
            public void onSuccess(String result) {
                JSONArray sources = JsonUtil.getArray(JsonUtil.getObject(result), "data_sources"); //$NON-NLS-1$

                if (sources != null) {
                    ListStore<DataSource> store = comboDataSources.getStore();

                    for (int i = 0; i < sources.size(); i++) {
                        store.add(new DataSource(JsonUtil.getObjectAt(sources, i)));
                    }

                    selectDataSource();
                } else {
                    onFailure(new Exception(result));
                }
            }

            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(I18N.DISPLAY.cantLoadDataSources(), caught);
            }
        });
    }

    private void selectDataSource() {
        DataObject dataObj = getDataObject();
        if (dataObj != null) {
            // find the DataSource by name.
            ListStore<DataSource> store = comboDataSources.getStore();
            DataSource value = store.findModel(DataSource.NAME, dataObj.getDataSource());

            if (value == null) {
                value = store.findModel(DataSource.NAME, "file"); //$NON-NLS-1$
            }

            if (value != null) {
                comboDataSources.setValue(value);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getMultiplicityLabel() {
        return I18N.DISPLAY.outPutMultiplicityOption();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void init() {
        super.init();

        property.setType(DataObject.OUTPUT_TYPE);
        property.setVisible(false);
        property.setOmit_if_blank(true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void buildFields() {
        super.buildFields();

        buildImplicitOutputCheckbox();
        buildDataSourcesComboBox();
        buildOutputFileNameField();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initFieldValues() {
        super.initFieldValues();

        DataObject obj = getDataObject();

        if (obj != null) {
            cbxImplicitOutput.setValue(obj.isImplicit());
            initTextField(outputFileNameField, obj.getOutputFilename());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void addFields() {
        super.addFields();

        add(cbxImplicitOutput);

        add(new Label(comboDataSources.getFieldLabel() + ":")); //$NON-NLS-1$
        add(comboDataSources);
        add(new Label(outputFileNameField.getFieldLabel() + ":")); //$NON-NLS-1$
        add(outputFileNameField);

        add(new Label(multiplicityGroup.getFieldLabel() + ":")); //$NON-NLS-1$
        add(multiplicityGroup);
        add(new Label(infoTypeField.getFieldLabel() + ":")); //$NON-NLS-1$
        add(infoTypeField);
    }

    private void buildImplicitOutputCheckbox() {
        cbxImplicitOutput = buildCheckBox(ID_IMPLICIT_OPT_CBX, I18N.DISPLAY.implicitOutput(),
                new Listener<BaseEvent>() {
                    @Override
                    public void handleEvent(BaseEvent be) {
                        if (getDataObject() != null) {
                            getDataObject().setImplicit(cbxImplicitOutput.getValue());
                        }

                    }
                });
        cbxImplicitOutput.setStyleAttribute("padding-bottom", "5px"); //$NON-NLS-1$ //$NON-NLS-2$
    }

    private void buildDataSourcesComboBox() {
        comboDataSources = new ComboBox<DataSource>();
        comboDataSources.setId(ID_DATA_SRC_CBO);
        comboDataSources.setWidth(250);
        comboDataSources.setForceSelection(true);
        comboDataSources.setFieldLabel(I18N.DISPLAY.dataSourceLabel());
        comboDataSources.setDisplayField(DataSource.LABEL);
        comboDataSources.setTriggerAction(TriggerAction.ALL);
        comboDataSources.setFireChangeEventOnSetValue(true);
        comboDataSources.setAllowBlank(false);

        // enable auto-complete
        comboDataSources.setEditable(true);
        comboDataSources.setTypeAhead(true);
        comboDataSources.setQueryDelay(1000);

        comboDataSources.setStore(new ListStore<DataSource>());

        comboDataSources.addSelectionChangedListener(new SelectionChangedListener<DataSource>() {
            @Override
            public void selectionChanged(SelectionChangedEvent<DataSource> sce) {
                DataSource source = sce.getSelectedItem();
                if (source != null) {
                    String srcName = source.getName();

                    if (srcName != null && !srcName.isEmpty()) {
                        getDataObject().setDataSource(srcName);
                    }
                }
            }
        });
    }

    private void buildOutputFileNameField() {
        outputFileNameField = buildTextField(ID_FLD_OP_NAME, 100, 100, new OutputFilenameKeyUpCommand());

        outputFileNameField.setFieldLabel(I18N.DISPLAY.outputFileName());
        outputFileNameField.setAllowBlank(false);
        outputFileNameField.setStyleAttribute("padding-bottom", "5px"); //$NON-NLS-1$ //$NON-NLS-2$
    }

    private class OutputFilenameKeyUpCommand extends LabelEditKeyUpCommand {
        @Override
        public void execute(String value) {
            if (value != null) {
                getDataObject().setOutputFilename(value);
            }

            super.execute(value);
        }

        @Override
        public void handleNullInput() {
            getDataObject().setOutputFilename(DEFAULT_STRING);

            super.handleNullInput();
        }
    }
}
