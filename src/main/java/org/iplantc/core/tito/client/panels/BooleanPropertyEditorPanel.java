package org.iplantc.core.tito.client.panels;

import org.iplantc.core.metadata.client.property.Property;
import org.iplantc.core.tito.client.I18N;

import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.ListBox;

public class BooleanPropertyEditorPanel extends PropertyTypeEditorPanel {
    public static final String PROPERTY_BOOLEAN_TRUE = "true"; //$NON-NLS-1$
    public static final String PROPERTY_BOOLEAN_FALSE = "false"; //$NON-NLS-1$
    public static final String DEFAULT_BOOLEAN = PROPERTY_BOOLEAN_FALSE;

    private BooleanDefaultValuePanel pnlDefaultValue;
    private TextFieldContainer pnlPropertyLabel;
    private TextFieldContainer pnlToolTip;

    protected BooleanPropertyEditorPanel(Property property) {
        super(property);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void buildFields() {
        super.buildFields();

        pnlDefaultValue = new BooleanDefaultValuePanel();

        pnlPropertyLabel = buildLabelFieldContainer();
        pnlToolTip = buildToolTipFieldContainer();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initFieldValues() {
        super.initFieldValues();

        initBooleanListBox(pnlDefaultValue.getListBox(), property.getValue());

        initTextField(pnlPropertyLabel.getField(), property.getLabel());
        initTextField(pnlToolTip.getField(), property.getDescription());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void addFields() {
        super.addFields();

        add(pnlPropertyLabel);
        add(pnlDefaultValue);

        add(pnlToolTip);
    }

    private void initBooleanListBox(final ListBox field, final String value) {
        int idxSelected = 0;

        if (!PROPERTY_BOOLEAN_TRUE.equals(value)) {
            updatePropertyValue(PROPERTY_BOOLEAN_FALSE);
            idxSelected = 1;
        }

        field.setSelectedIndex(idxSelected);
    }

    private class BooleanDefaultValuePanel extends VerticalPanel {
        final ListBox field;

        public BooleanDefaultValuePanel() {
            field = buildBooleanListBox();

            add(new Label(I18N.DISPLAY.defaultValue()));
            add(field);
        }

        public ListBox getListBox() {
            return field;
        }

        private ListBox buildBooleanListBox() {
            final ListBox ret = new ListBox();

            ret.setWidth("140px"); //$NON-NLS-1$

            ret.addItem(I18N.DISPLAY.propertyEditorTrue());
            ret.addItem(I18N.DISPLAY.propertyEditorFalse());

            ret.addChangeHandler(new ChangeHandler() {
                @Override
                public void onChange(ChangeEvent ce) {
                    String text = ret.getItemText(ret.getSelectedIndex());

                    text = (PROPERTY_BOOLEAN_TRUE.equals(text)) ? PROPERTY_BOOLEAN_TRUE
                            : PROPERTY_BOOLEAN_FALSE;

                    updatePropertyValue(text);
                }
            });

            return ret;
        }
    }
}
