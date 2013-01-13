package org.iplantc.core.tito.client.panels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.iplantc.core.tito.client.I18N;
import org.iplantc.core.tito.client.dialogs.DCLookUpDialog;
import org.iplantc.core.tito.client.events.ExecutableChangeEvent;
import org.iplantc.core.tito.client.events.ExecutableChangeEventHandler;
import org.iplantc.core.tito.client.events.TemplateNameChangeEvent;
import org.iplantc.core.tito.client.events.ToolSelectedEvent;
import org.iplantc.core.tito.client.models.Template;
import org.iplantc.core.tito.client.utils.DeployedComponentSearchUtil;
import org.iplantc.core.tito.client.widgets.form.MyComboBox;
import org.iplantc.core.tito.client.widgets.form.MyTriggerField;
import org.iplantc.core.uiapplications.client.util.AnalysisUtil;
import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.core.uicommons.client.models.DeployedComponent;
import org.iplantc.core.uicommons.client.widgets.BoundedTextArea;
import org.iplantc.core.uicommons.client.widgets.BoundedTextField;
import org.iplantc.core.uicommons.client.widgets.FormLabel;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.FormPanel.LabelAlign;
import com.extjs.gxt.ui.client.widget.form.HiddenField;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * 
 * A panel to enable editing for Template info
 * 
 * 
 * @author sriram
 * 
 */
public class TemplateInfoEditorPanel extends ContentPanel {
    private static final String ID_BTN_BROWSE = "idBtnBrowse"; //$NON-NLS-1$
    private static final String ID_FLD_DESC = "idFldDesc"; //$NON-NLS-1$
    private static final String ID_FLD_NAME = "idFldName"; //$NON-NLS-1$

    private static final String TRIGGER_DELETE_STYLE = "x-form-remove-trigger";
    private static final String TRIGGER_ADD_STYLE = "x-form-add-trigger";

    private Template template;

    private FormData formData;
    private FormPanel panel;

    private TextField<String> nameField;
    private TextField<String> descField;
    private HiddenField<String> idField;
    private HiddenField<String> idComponentField;
    private HiddenField<String> idtito;
    private DCLookUpDialog dialog;
    private MyComboBox<DeployedComponent> dcCombo;

    private ArrayList<HandlerRegistration> handlers;
    private ArrayList<MyTriggerField<String>> refTriggerList;


    /**
     * Creates a new TemplateInfoEditorPanel
     * 
     * @param template
     */
    public TemplateInfoEditorPanel(Template template) {
        this.template = template;

        init();
        addFields();
        add(panel);

        setFieldValues();
    }

    private void setFieldValues() {
        if (template == null) {
            return;
        }

        String name = template.getName();
        if (name != null && !name.isEmpty()) {
            nameField.setValue(name);
            TemplateNameChangeEvent e = new TemplateNameChangeEvent(nameField.getValue());
            EventBus.getInstance().fireEvent(e);
        }

        String desc = template.getDescription();
        if (desc != null && !desc.isEmpty()) {
            descField.setValue(desc);
        }

        String comp = template.getComp();
        String compId = template.getCompId();
        if (comp != null && compId != null && !compId.isEmpty()) {
            idComponentField.setValue(compId);
            dcCombo.setValue(new DeployedComponent(compId, comp, null, null, null, null, null));
            fireToolSelectedEvent(!comp.isEmpty());
        }

        List<String> references = template.getReferences();
        if (references != null && !references.isEmpty()) {
            addReferenceFields(references);
        }
        
        String id = template.getId();
        if (id != null && !id.isEmpty()) {
            idField.setValue(id);
        }

        String titoId = template.getTitoId();
        if (titoId != null && !titoId.isEmpty()) {
            idtito.setValue(titoId);
        }
    }

    private void init() {
        setHeaderVisible(false);
        setBodyBorder(false);
        setLayout(new FitLayout());

        panel = new FormPanel();
        panel.setScrollMode(Scroll.AUTOY);
        panel.setBodyBorder(false);
        panel.setFrame(true);
        panel.setHeaderVisible(false);
        panel.setBodyBorder(false);
        panel.setLabelAlign(LabelAlign.TOP);
        formData = new FormData("-20"); //$NON-NLS-1$
        addListeners();

        refTriggerList = new ArrayList<MyTriggerField<String>>();
    }

    private void addListeners() {
        handlers = new ArrayList<HandlerRegistration>();

        handlers.add(EventBus.getInstance().addHandler(ExecutableChangeEvent.TYPE,
                new ExecutableChangeEventHandler() {
                    @Override
                    public void onChange(ExecutableChangeEvent event) {
                        idComponentField.setValue(event.getId());
                        fireToolSelectedEvent(true);
                    }
                }));
    }

    private void addFields() {

        initIdFields();
        panel.add(idField);
        panel.add(idtito);
        panel.add(idComponentField);

        initComponentField();
        dcCombo.setFieldLabel(I18N.DISPLAY.selectedTool());
        panel.add(dcCombo, formData);
        panel.add(new Html("<br/>"), formData); //$NON-NLS-1$

        nameField = buildNameTextField();
        // fire TemplateNameChange so the tree can be updated
        nameField.addListener(Events.OnKeyUp, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                TemplateNameChangeEvent e = new TemplateNameChangeEvent(nameField.getValue());
                EventBus.getInstance().fireEvent(e);
            }
        });
        panel.add(nameField, formData);
        panel.add(new Html("<br/>"), formData); //$NON-NLS-1$

        descField = buildComponentDescriptionField();
        panel.add(descField, formData);
        panel.add(new Html("<br/>"), formData); //$NON-NLS-1$

        panel.add(new FormLabel(I18N.DISPLAY.referencesLabel()), formData);
        List<String> references = template.getReferences();
        if ((references == null) || references.isEmpty()) {
            addReferenceField("", TRIGGER_ADD_STYLE);
        }

    }

    private void initIdFields() {
        idField = new HiddenField<String>();
        idField.setId(Template.ID);
        
        idtito = new HiddenField<String>();
        idtito.setId(Template.TITO_ID);

        idComponentField = new HiddenField<String>();
        idComponentField.setId(Template.COMPONENT_ID);
        
    }

    private TextField<String> buildNameTextField() {
        TextField<String> field = new BoundedTextField<String>();
        field.setMaxLength(64);
        field.setId(ID_FLD_NAME);
        field.setFieldLabel(I18N.DISPLAY.templateName());
        field.setEmptyText(I18N.DISPLAY.nameFieldEmptyText());
        field.setAllowBlank(false);
        field.setStyleAttribute("padding-bottom", "5px"); //$NON-NLS-1$ //$NON-NLS-2$
        field.setAutoValidate(true);

        // Set restricted characters in this field's regex validation.
        AnalysisUtil.setAppNameRegexValidation(field);

        return field;
    }

    private void initComponentField() {
        DeployedComponentSearchUtil util = new DeployedComponentSearchUtil();
        dcCombo = util.buildSearchField();
        dcCombo.setTriggerId(ID_BTN_BROWSE);

        dcCombo.addListener(Events.TriggerClick, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                showLookUpDialog();
            }
        });
    }

    private TextArea buildComponentDescriptionField() {
        TextArea field = new BoundedTextArea();
        field.setId(ID_FLD_DESC);
        field.setFieldLabel(I18N.DISPLAY.description());
        field.setAutoValidate(true);
        field.setMaxLength(255);
        field.setStyleAttribute("padding-bottom", "5px"); //$NON-NLS-1$ //$NON-NLS-2$

        return field;
    }

    public Template getTemplate() {
        String id = idField.getValue();
        String name = nameField.getValue();
        String desc = descField.getValue();
        String compId = idComponentField.getValue();
        String comp = null;
        if (dcCombo.getValue() != null) {
            comp = dcCombo.getValue().getName();
        }
        List<String> references = getReferenceValues();
        String tito = idtito.getValue();
        String type = ""; //$NON-NLS-1$

        if (id == null) {
            id = ""; //$NON-NLS-1$
        }

        if (name == null) {
            name = ""; //$NON-NLS-1$
        }

        if (desc == null) {
            desc = ""; //$NON-NLS-1$
        }

        if (comp == null) {
            comp = ""; //$NON-NLS-1$
        }

        if (compId == null) {
            compId = ""; //$NON-NLS-1$
        }
        
        if (references == null) {
            references = Collections.emptyList();
        }

        if (tito == null) {
            tito = ""; //$NON-NLS-1$
        }

        if (template == null) {
            template = new Template(id, name, desc, compId, comp, type, references, "", tito, null); //$NON-NLS-1$
        } else {
            template.setId(id);
            template.setName(name);
            template.setDescription(desc);
            template.setCompId(compId);
            template.setComp(comp);
            template.setCompType(type);
            template.setReferences(references);
            template.setTitoId(tito);
        }

        return template;
    }

    private List<String> getReferenceValues() {
        List<String> retList = new ArrayList<String>();
        for (MyTriggerField<String> trigger : refTriggerList) {
            String value = trigger.getValue();
            retList.add(value == null ? "" : value);
        }
        return retList;
    }

    private class DialogOkBtnSelectionListenerImpl extends SelectionListener<ButtonEvent> {
        @Override
        public void componentSelected(ButtonEvent ce) {
            DeployedComponent dc = dialog.getSelectedItem();
            if (dc != null) {
                dcCombo.setValue(dc);
                idComponentField.setValue(dc.getId());
                fireToolSelectedEvent(true);
            } else {
                dcCombo.setValue(null);
                idComponentField.setValue(""); //$NON-NLS-1$
                fireToolSelectedEvent(false);
            }
        }
    }

    private void fireToolSelectedEvent(boolean selected) {
        ToolSelectedEvent event = new ToolSelectedEvent(selected);
        EventBus.getInstance().fireEvent(event);
    }

    private void showLookUpDialog() {
        dialog = new DCLookUpDialog(new DialogOkBtnSelectionListenerImpl(), idComponentField.getValue());
        dialog.show();
    }

    /**
     * Validates all input fields and highlights any invalid ones.
     * 
     * @return
     */
    public boolean validate() {
        boolean valid = true;

        valid &= nameField.isValid();
        valid &= descField.isValid();
        valid &= idComponentField.getValue() != null && !idComponentField.getValue().isEmpty();

        return valid;
    }

    public void setTitoId(String tito) {
        idtito.setValue(tito);
    }

    public String getTitoId() {
    	return idtito.getValue();
    }
    
    
    public void setTemplateId(String id) {
        idField.setValue(id);
    }

    protected String getComponent() {
        if (dcCombo.getValue() != null) {
            return dcCombo.getValue().getName();
        } else {
            return null;
        }
    }

    public void cleanup() {
        for (HandlerRegistration handler : handlers) {
            handler.removeHandler();
        }

        handlers.clear();
    }

    private void addReferenceField(final String text, final String triggerStyle) {
        MyTriggerField<String> referenceField = new MyTriggerField<String>();
        referenceField.setValue(text);
        referenceField.setHideLabel(true);
        referenceField.setTriggerStyle(triggerStyle);
        if (triggerStyle.equals(TRIGGER_ADD_STYLE)) {
            // Add the "add" listener
            referenceField.addListener(Events.TriggerClick, new AddReferenceListener());
        } else {
            // Add the "remove" listener
            referenceField.addListener(Events.TriggerClick, new RemoveReferenceListener());
        }
        refTriggerList.add(referenceField);
        panel.add(referenceField, formData);
        panel.layout();
        referenceField.focus();
    }

    private void addReferenceFields(final List<String> references) {
        int count = references.size();
        boolean wasTriggerListEmpty = refTriggerList.isEmpty();
        for (String text : references) {
            count--;
            /*
             * If the trigger list was empty when we started, the last reference field we add should have
             * an "add" trigger
             */
            if (wasTriggerListEmpty && count == 0) {
                addReferenceField(text, TRIGGER_ADD_STYLE);
            } else {
                addReferenceField(text, TRIGGER_DELETE_STYLE);
            }

        }
    }

    private class AddReferenceListener implements Listener<BaseEvent> {
        @Override
        public void handleEvent(BaseEvent be) {
            if (be.getSource() instanceof MyTriggerField<?>) {
                @SuppressWarnings("unchecked")
                MyTriggerField<String> currTrigger = (MyTriggerField<String>)be.getSource();
                // Remove the AddReferenceListener and add the RemoveReferenceListener
                currTrigger.removeAllListeners();
                currTrigger.addListener(Events.TriggerClick, new RemoveReferenceListener());
                // Reset the trigger style from "add" to "delete"
                currTrigger.resetTriggerStyle(TRIGGER_DELETE_STYLE);

                addReferenceField("", TRIGGER_ADD_STYLE);
            }
        }
    }

    private class RemoveReferenceListener implements Listener<BaseEvent> {
        @Override
        public void handleEvent(BaseEvent be) {
            if (be.getSource() instanceof MyTriggerField<?>) {
                @SuppressWarnings("unchecked")
                MyTriggerField<String> triggerToBeRemoved = (MyTriggerField<String>)be.getSource();
                triggerToBeRemoved.removeAllListeners();
                // Remove trigger field from internal ref collection
                refTriggerList.remove(triggerToBeRemoved);
                triggerToBeRemoved.removeFromParent();
                panel.layout();
            }
        }
    };


}
