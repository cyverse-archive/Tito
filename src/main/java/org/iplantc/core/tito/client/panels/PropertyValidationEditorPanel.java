package org.iplantc.core.tito.client.panels;

import org.iplantc.core.metadata.client.PropertyType;
import org.iplantc.core.metadata.client.property.Property;

import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.ui.ListBox;

public class PropertyValidationEditorPanel extends PropertySubTypeEditorPanel {
    protected LayoutContainer pnlDefaultValue;
    protected VerticalPanel pnlValidationContainer;
    protected ValidationPanel pnlValidation;
    protected ListEditorPanel pnlListEditor;

    public PropertyValidationEditorPanel(Property property) {
        super(property);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void buildFields() {
        super.buildFields();

        buildBottomPanel();

        pnlValidation = new ValidationPanel(property);
    }

    private void buildBottomPanel() {
        pnlValidationContainer = new VerticalPanel();
        pnlValidationContainer.setLayout(new FitLayout());
        pnlValidationContainer.setBorders(true);
        pnlValidationContainer.setSpacing(8);
    }

    @Override
    protected void handleWidgetTypeChange() {
        ListBox listPropertyType = pnlPropertyTypeList.getListBox();
        boolean isSelectionWidget = false;

        if (propertyTypes != null && listPropertyType.getItemCount() > 0) {

            PropertyType type = propertyTypes.get(listPropertyType.getSelectedIndex());

            // TODO: find alternatives for hardcoding
            String typeName = type.getName();
            if (typeName.equalsIgnoreCase("selection") || typeName.equalsIgnoreCase("valueselection")) { //$NON-NLS-1$ //$NON-NLS-2$
                isSelectionWidget = true;
            }

            property.setType(typeName);
        }

        updatePanelsAfterWidgetTypeChange(isSelectionWidget);
    }

    protected void updatePanelsAfterWidgetTypeChange(boolean isSelectionWidget) {
        pnlCommandLineOption.setEnabled(!isSelectionWidget);

        if (pnlDefaultValue != null) {
            pnlDefaultValue.setEnabled(!isSelectionWidget);
        }

        pnlValidationContainer.removeAll();

        pnlValidationContainer.add(pnlPropertyTypeList);

        if (isSelectionWidget) {
            // Selection types should not be hidden in the GUI, since they are not required to define
            // default values needed for hidden parameters.
            cbxDisplayInGui.setValue(true);
            cbxDisplayInGui.setEnabled(false);

            pnlValidationContainer.add(pnlListEditor);
        } else {
            cbxDisplayInGui.setEnabled(true);
            pnlValidationContainer.add(pnlValidation);
        }

        pnlValidationContainer.layout();
    }
}
