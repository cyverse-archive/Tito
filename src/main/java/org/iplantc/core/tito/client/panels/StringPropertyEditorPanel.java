package org.iplantc.core.tito.client.panels;

import org.iplantc.core.client.widgets.validator.IPlantValidator;
import org.iplantc.core.metadata.client.property.Property;
import org.iplantc.core.metadata.client.property.PropertyTypeCategory;
import org.iplantc.core.tito.client.I18N;

import com.extjs.gxt.ui.client.widget.form.TextField;

public class StringPropertyEditorPanel extends PropertyValidationEditorPanel {
    private static final String ID_FLD_DEF_STR_VAL = "idFldDefStrVal"; //$NON-NLS-1$

    private TextFieldContainer pnlPropertyLabel;
    private TextFieldContainer pnlToolTip;

    public StringPropertyEditorPanel(Property property) {
        super(property, PropertyTypeCategory.STRING);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void buildFields() {
        super.buildFields();

        pnlPropertyLabel = buildLabelFieldContainer();
        pnlDefaultValue = buildDefaultValueContainer();
        pnlToolTip = buildToolTipFieldContainer();

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

        initTextField(pnlPropertyLabel.getField(), property.getLabel());
        initTextField(fieldDefaultValue, property.getValue());
        initTextField(pnlToolTip.getField(), property.getDescription());

        cbxOptionFlag.setValue(property.isOmit_if_blank());
        initRequiredCheckBox();

        cbxDisplayInGui.setValue(property.isVisible());
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

        return buildTextFieldContainer(caption, field);
    }
}
