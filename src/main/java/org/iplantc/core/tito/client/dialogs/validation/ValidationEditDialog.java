package org.iplantc.core.tito.client.dialogs.validation;

import static org.iplantc.core.tito.client.I18N.DISPLAY;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.iplantc.core.metadata.client.property.Property;
import org.iplantc.core.metadata.client.property.PropertyTypeCategory;
import org.iplantc.core.metadata.client.property.groups.PropertyGroupContainer;
import org.iplantc.core.metadata.client.validation.MetaDataRule;
import org.iplantc.core.tito.client.I18N;
import org.iplantc.core.tito.client.command.RuleCommand;
import org.iplantc.core.tito.client.models.RuleType;
import org.iplantc.core.tito.client.widgets.validation.FieldRefDropDown;
import org.iplantc.core.tito.client.widgets.validation.NumberEditor;
import org.iplantc.core.tito.client.widgets.validation.PropertyChangeListener;
import org.iplantc.core.tito.client.widgets.validation.PropertyEditor;
import org.iplantc.core.tito.client.widgets.validation.RuleTypeDropDown;
import org.iplantc.core.tito.client.widgets.validation.StringEditor;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.store.StoreEvent;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Command;

/**
 * A dialog for editing one validation rule.
 * 
 * @author hariolf
 * 
 */
public class ValidationEditDialog extends Dialog {
    private MetaDataRule rule;
    private MetaDataRule ruleCopy;
    private PropertyTypeCategory category;
    private EditRulePanel valuePanel;
    private LayoutContainer valuePanelContainer;
    private Property property;
    private RuleCommand onOkClick;
    private PropertyGroupContainer pgContainer;
    private RuleTypeDropDown ruleTypeDropDown;
    private Map<String, String> formats;

    /**
     * Creates a new ValidationEditDialog.
     * 
     * @param rule the rule to edit
     * @param property
     * @param category
     * @param pgContainer for accessing the other properties associated with this property
     * @param showAboveBelowRules
     * @param onOkClick
     */
    public ValidationEditDialog(MetaDataRule rule, Property property, PropertyTypeCategory category,
            PropertyGroupContainer pgContainer, boolean showAboveBelowRules, RuleCommand onOkClick) {
        this.property = property;
        this.category = category;
        this.pgContainer = pgContainer;
        this.onOkClick = onOkClick;

        this.rule = rule;

        init(showAboveBelowRules);
    }

    private MetaDataRule buildDefaultNumberMetadataRule() {
        return new MetaDataRule("IntRange"); //$NON-NLS-1$
    }

    private MetaDataRule buildDefaultStringMetadataRule() {
        return new MetaDataRule("MustContain"); //$NON-NLS-1$
    }

    private MetaDataRule buildDefaultMetaDataRule() {
        MetaDataRule ret = null; // assume failure

        switch (category) {
            case NUMBER:
                ret = buildDefaultNumberMetadataRule();
                break;

            case STRING:
                ret = buildDefaultStringMetadataRule();
                break;

            default:
                break;
        }

        return ret;
    }

    private void init(final boolean showAboveBelowRules) {
        setHeading(DISPLAY.validationEditDialogHeading());
        setLayout(new FitLayout());
        setModal(true);
        setResizable(false);

        setButtons(Dialog.OKCANCEL);
        buildCancelButtonHandler();
        buildOkButtonHandler();

        valuePanelContainer = new VerticalPanel();
        compose(buildTopPanel(showAboveBelowRules, new Command() {

            @Override
            public void execute() {
                if (rule == null) {
                    String type = ruleTypeDropDown.getValue().stringValue();
                    if (type != null) {
                        ruleCopy = rule = new MetaDataRule(type);
                    }
                    ruleTypeDropDown.setFilter(category, showAboveBelowRules);
                } else {
                    ruleCopy = rule.clone();
                    ruleTypeDropDown.setFilter(category, showAboveBelowRules);
                    setRuleType(rule.getType());
                    valuePanel.updateEditors();
                }
            }
        }));

        setSize(430, 300);
    }

    private Button getOkButton() {
        return getButtonById(Dialog.OK);
    }

    private Button getCancelButton() {
        return getButtonById(Dialog.CANCEL);
    }

    private void buildOkButtonHandler() {
        getOkButton().addListener(Events.OnClick, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                if (valuePanel != null && valuePanel.validate()) {
                    updateRule();

                    if (onOkClick != null) {
                        // overwrite the original rule with the changes and do the callback
                        rule.setProperties(ruleCopy.getProperties());
                        onOkClick.execute(rule);
                    }
                    hide();
                }
            }
        });
    }

    private void buildCancelButtonHandler() {
        getCancelButton().addListener(Events.OnClick, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                hide();
            }
        });
    }
    
    private void initFormats() {
        formats = new HashMap<String, String>();
        for (RuleType type : ruleTypeDropDown.getValues()) {
            formats.put(type.getName(), type.getFormat());
        }
    }

    private EditRulePanel buildValuePanel(RuleType type) {
        String typeName = type.getName();
        return new EditRulePanel(new MetaDataRule(typeName), type, formats.get(typeName));
    }

    private ComboBox<RuleType> buildTypeBox(boolean showAboveBelowRules, Command afterLoad) {
        ruleTypeDropDown = new RuleTypeDropDown(afterLoad);
        ruleTypeDropDown.setFilter(category, showAboveBelowRules);

        @SuppressWarnings("unchecked")
        ComboBox<RuleType> combo = (ComboBox<RuleType>)ruleTypeDropDown.getEditorComponent();
        combo.addSelectionChangedListener(new SelectionChangedListenerImpl());

        combo.getStore().addListener(Store.Add, new Listener<StoreEvent<RuleType>>() {

            @Override
            public void handleEvent(StoreEvent<RuleType> be) {
                initFormats();
            }
        });

        return combo;
    }

    /**
     * Creates a PropertyEditor and sets a value on it.
     * 
     * @param dataType the type of data to edit
     * @param value the value to set
     * @return
     */
    @SuppressWarnings("unchecked")
    private <T extends JSONValue> PropertyEditor<T> createEditor(String dataType, String subtype, T value) {
        if ("Number".equals(dataType)) { //$NON-NLS-1$
            if ("Integer".equals(subtype)) { //$NON-NLS-1$
                return (PropertyEditor<T>)new NumberEditor(value == null ? null : value.isNumber(),
                        false);
            } else // subtype = "Double"
            {
                return (PropertyEditor<T>)new NumberEditor(value == null ? null : value.isNumber(), true);
            }
        } else if ("List".equals(dataType)) { //$NON-NLS-1$
            if (category == PropertyTypeCategory.STRING) {
                JSONArray arr = (value == null) ? null : value.isArray();
                return (PropertyEditor<T>)new EditableStringList(arr, null);
            } else {
                JSONArray arr = (value == null) ? null : value.isArray();
                return (PropertyEditor<T>)new EditableNumberList(arr, null);
            }
        } else if ("FieldRef".equals(dataType)) { //$NON-NLS-1$
            JSONString str = (value == null) ? null : value.isString();
            FieldRefDropDown dropDown = new FieldRefDropDown(pgContainer, property,
                    str == null ? new JSONString("") : str); //$NON-NLS-1$
            dropDown.getEditorComponent().setEnabled(!dropDown.isEmpty());
            return (PropertyEditor<T>)dropDown;
        } else {
            JSONString str = (value == null) ? null : value.isString();
            StringEditor editor = new StringEditor(str == null ? new JSONString("") : str); //$NON-NLS-1$
            editor.setAllowBlank(false);
            return (PropertyEditor<T>)editor;
        }
    }

    /**
     * Check box and buttons must be instantiated before calling this.
     * 
     * @return
     */
    private LayoutContainer buildTopPanel(boolean showAboveBelowRules, Command afterLoad) {
        VerticalPanel ret = new VerticalPanel();
        ret.setStyleAttribute("padding", "5px"); //$NON-NLS-1$ //$NON-NLS-2$

        ret.add(new Label(I18N.DISPLAY.ruleType() + ":")); //$NON-NLS-1$
        ret.add(buildTypeBox(showAboveBelowRules, afterLoad));

        return ret;
    }

    private void compose(LayoutContainer topPanel) {
        setTopComponent(topPanel);
        add(valuePanelContainer);
    }

    private class EditRulePanel extends LayoutContainer {
        private List<PropertyEditor<JSONValue>> editors;

        /**
         * @param rule
         * @param type
         * @param format The text to display; also controls placement of text fields which are coded as
         *            {Number}, {Text}, etc. in the string. The text between the {} is the data type for
         *            the rule parameter.
         */
        EditRulePanel(MetaDataRule rule, RuleType type, String format) {
            setStyleAttribute("padding", "8px"); //$NON-NLS-1$ //$NON-NLS-2$
            setLayout(new FlowLayout());
            editors = new ArrayList<PropertyEditor<JSONValue>>();

            List<JSONValue> params = rule.getParams();
            int paramIndex = 0;
            while (true) {
                String[] splitStr = format.split("\\{", 2); //$NON-NLS-1$

                if (splitStr.length < 2) {
                    break;
                }

                String displayText = splitStr[0];
                Label label = new Label(displayText);

                add(label);

                format = splitStr[1];
                splitStr = format.split("\\}", 2); //$NON-NLS-1$
                if (splitStr.length < 2) {
                    GWT.log("Error in format string: No closing '}'"); //$NON-NLS-1$
                    break;
                }
                String editorType = splitStr[0];

                JSONValue value = (paramIndex >= params.size()) ? null : rule.getParams()
                        .get(paramIndex);

                paramIndex++;
                PropertyEditor<JSONValue> editor = createEditor(editorType, type.getSubtype(), value);
                editor.addChangeListener(new PropertyChangeListener<JSONValue>() {
                    @Override
                    public void propertyChange(JSONValue newValue) {
                        updateOkButton();
                    }
                });

                editors.add(editor);

                Component editorComponent = editor.getEditorComponent();
                add(editorComponent);
                format = splitStr[1];
            }
        }

        /** Sets visibility of the OK button */
        private void updateOkButton() {
            Button okButton = getOkButton();
            for (PropertyEditor<JSONValue> editor : editors) {
                if (!editor.validate()) {
                    okButton.setEnabled(false);
                    return;
                }
            }
            okButton.setEnabled(true);
        }

        List<JSONValue> getValues() {
            List<JSONValue> values = new ArrayList<JSONValue>();

            for (PropertyEditor<JSONValue> editor : editors) {
                JSONValue val = editor.getValue();

                if (val instanceof JSONArray) {
                    JSONArray arr = (JSONArray)val;

                    for (int i = 0,len = arr.size(); i < len; i++) {
                        values.add(arr.get(i));
                    }
                } else {
                    values.add(val);
                }
            }

            return values;
        }

        void updateEditors() {

            if (ruleCopy.getType().equals("MustContain") && ruleCopy.getParams().size() > 0) { //$NON-NLS-1$
                JSONArray arr = new JSONArray();

                for (int i = 0,len = ruleCopy.getParams().size(); i < len; i++) {
                    arr.set(i, ruleCopy.getParams().get(i));
                }

                editors.get(0).setValue(arr);
            } else {
                for (int i = 0,len = ruleCopy.getParams().size(); i < len; i++) {
                    editors.get(i).setValue(ruleCopy.getParams().get(i));
                }
            }
        }

        boolean validate() {
            boolean ret = true; // assume success

            for (PropertyEditor<JSONValue> editor : editors) {
                if (!editor.validate()) {
                    ret = false;
                    break;
                }
            }
            return ret;
        }
    }

    private void setRuleType(String type) {
        setRuleType(ruleTypeDropDown.getRuleTypeByName(type));
    }

    private void setRuleType(RuleType type) {
        ruleTypeDropDown.setValue(type.getName());

        boolean sameType = ruleCopy.getType().equals(type.getName());

        valuePanelContainer.removeAll();
        ruleCopy.setType(type.getName());
        valuePanel = buildValuePanel(type);
        valuePanelContainer.add(valuePanel);

        if (sameType) {
            valuePanel.updateEditors();
        }

        layout();
    }

    private void updateRule() {
        ruleCopy.setType(ruleTypeDropDown.getValue().isString().stringValue());
        ruleCopy.setParams(valuePanel.getValues());
    }

    private class SelectionChangedListenerImpl extends SelectionChangedListener<RuleType> {
        @Override
        public void selectionChanged(SelectionChangedEvent<RuleType> se) {
            setRuleType(se.getSelectedItem());
        }
    }
}
