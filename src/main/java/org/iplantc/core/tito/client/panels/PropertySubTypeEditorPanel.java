package org.iplantc.core.tito.client.panels;

import java.util.List;

import org.iplantc.core.metadata.client.PropertyType;
import org.iplantc.core.metadata.client.property.Property;
import org.iplantc.core.tito.client.I18N;

import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.ListBox;

public abstract class PropertySubTypeEditorPanel extends PropertyTypeEditorPanel {
    private static final String ID_ARG_TYPE = "idArgType"; //$NON-NLS-1$

    protected List<PropertyType> propertyTypes;
    protected WidgetTypeDropdownPanel pnlPropertyTypeList;

    public PropertySubTypeEditorPanel(Property property) {
        super(property);
    }

    protected abstract void handleWidgetTypeChange();

    public void updatePropertyTypesToListBox(List<PropertyType> propertyTypes) {
        this.propertyTypes = propertyTypes;

        ListBox listPropertyType = pnlPropertyTypeList.getListBox();

        // reset our list box
        listPropertyType.clear();

        String typeDest = property.getType();

        int idx = 0; // keep track of index for setting selection
        int idxSelected = 0;

        for (PropertyType type : propertyTypes) {
            // add our item to the listbox
            listPropertyType.addItem(type.getDescription());

            // is this our desired selection?
            if (type.getName().equals(typeDest)) {
                idxSelected = idx;
            }

            idx++;
        }

        // set our selection
        if (listPropertyType.getItemCount() > 0) {
            listPropertyType.setSelectedIndex(idxSelected);
        }

        // force type change
        handleWidgetTypeChange();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void buildFields() {
        super.buildFields();

        pnlPropertyTypeList = new WidgetTypeDropdownPanel();
    }

    protected class WidgetTypeDropdownPanel extends VerticalPanel {
        final ListBox field;

        public WidgetTypeDropdownPanel() {
            setLayout(new FitLayout());

            field = buildPropertyTypeListBox();

            add(new Label(I18N.DISPLAY.typeOfFieldNeeded() + ": ")); //$NON-NLS-1$
            add(field);
        }

        public ListBox getListBox() {
            return field;
        }

        private ListBox buildPropertyTypeListBox() {
            final ListBox ret = new ListBox();

            ret.getElement().setId(ID_ARG_TYPE);
            ret.addChangeHandler(new ChangeHandler() {
                @Override
                public void onChange(ChangeEvent event) {
                    handleWidgetTypeChange();
                }
            });

            return ret;
        }
    }
}
