package org.iplantc.core.tito.client.panels;

import org.iplantc.core.client.widgets.validator.IPlantValidator;
import org.iplantc.core.metadata.client.property.Property;
import org.iplantc.core.metadata.client.property.PropertyTypeCategory;
import org.iplantc.core.tito.client.I18N;

import com.extjs.gxt.ui.client.widget.form.TextField;

public class StringPropertyEditorPanel extends PropertyValidationEditorPanel {
    private static final String ID_FLD_DEF_STR_VAL = "idFldDefStrVal"; //$NON-NLS-1$
    private StringListboxEditorPanel pnlListBoxEditor;
    private TreeListEditorPanel pnlTreeListEditor;

    public StringPropertyEditorPanel(Property property) {
        super(property);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void buildFields() {
        super.buildFields();

        pnlDefaultValue = buildDefaultValueContainer();
        pnlListBoxEditor = new StringListboxEditorPanel(property);
        pnlTreeListEditor = new TreeListEditorPanel(property);

        setListEditorPanel();

        buildGuiEnabledCheckbox();
        buildOptionalFlagCheckbox();
        buildRequiredCheckBox();

        buildWidgetsPanel();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initFieldValues() {
        super.initFieldValues();

        pnlValidation.reset(PropertyTypeCategory.STRING);

        TextField<String> fieldDefaultValue = ((TextFieldContainer)pnlDefaultValue).getField();

        initTextField(fieldDefaultValue, property.getValue());

        cbxOptionFlag.setValue(property.isOmitIfBlank());
        initRequiredCheckBox();
        initGuiEnabledCheckBox();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void addFields() {
        super.addFields();

        add(pnlPropertyLabel);
        add(pnlDefaultValue);
        add(cbxDisplayInGui);

        pnlWidgets.add(cbxOptionFlag);
        pnlWidgets.add(cbxRequired);

        pnlWidgets.add(pnlToolTip);
        pnlWidgets.add(pnlValidationContainer);

        add(pnlWidgets);
    }

    private TextFieldContainer buildDefaultValueContainer() {
        String caption = I18N.DISPLAY.defaultValueLabel();

        TextField<String> field = buildTextField(ID_FLD_DEF_STR_VAL, 255, 255,
                new ValueEditKeyUpCommand());
        IPlantValidator.setRegexRestrictedArgValueChars(field, caption);

        return new TextFieldContainer(caption, field);
    }

    private void setListEditorPanel() {
        if ("TreeSelection".equalsIgnoreCase(property.getType())) { //$NON-NLS-1$
            pnlListEditor = pnlTreeListEditor;
        } else {
            pnlListEditor = pnlListBoxEditor;
        }
    }

    @Override
    protected void updatePanelsAfterWidgetTypeChange(boolean isSelectionWidget) {
        if (isSelectionWidget) {
            setListEditorPanel();
        }

        super.updatePanelsAfterWidgetTypeChange(isSelectionWidget);
    }
}
