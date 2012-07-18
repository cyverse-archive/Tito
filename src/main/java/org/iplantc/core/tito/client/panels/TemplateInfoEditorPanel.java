package org.iplantc.core.tito.client.panels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.iplantc.core.client.widgets.BoundedTextArea;
import org.iplantc.core.client.widgets.BoundedTextField;
import org.iplantc.core.client.widgets.utils.FormLabel;
import org.iplantc.core.tito.client.I18N;
import org.iplantc.core.tito.client.dialogs.DCLookUpDialog;
import org.iplantc.core.tito.client.events.ExecutableChangeEvent;
import org.iplantc.core.tito.client.events.ExecutableChangeEventHandler;
import org.iplantc.core.tito.client.events.TemplateNameChangeEvent;
import org.iplantc.core.tito.client.events.ToolSelectedEvent;
import org.iplantc.core.tito.client.models.Template;
import org.iplantc.core.tito.client.utils.DeployedComponentSearchUtil;
import org.iplantc.core.uiapplications.client.util.AnalysisUtil;
import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.core.uicommons.client.models.DeployedComponent;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
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
 * @author sriram
 *
 */
public class TemplateInfoEditorPanel extends ContentPanel {
    private static final String ID_BTN_BROWSE = "idBtnBrowse"; //$NON-NLS-1$
    private static final String ID_FLD_DESC = "idFldDesc"; //$NON-NLS-1$
    private static final String ID_FLD_NAME = "idFldName"; //$NON-NLS-1$

    private Template template;

    private FormData formData;
    private FormPanel panel;

    private TextField<String> nameField;
    private TextField<String> descField;
    private MultiTextFieldPanel refPanel;
    private HiddenField<String> idField;
    private HiddenField<String> idComponentField;
    private HiddenField<String> idtito;
    private DCLookUpDialog dialog;
    private ComboBox<DeployedComponent> dcCombo;

    private ArrayList<HandlerRegistration> handlers;


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
            refPanel.addFields(references);
        }
        else {
            refPanel.addField();
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
        nameField = buildNameTextField();
        // fire TemplateNameChange so the tree can be updated
        nameField.addListener(Events.OnKeyUp, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                TemplateNameChangeEvent e = new TemplateNameChangeEvent(nameField.getValue());
                EventBus.getInstance().fireEvent(e);
            }
        });

        buildTemplateIdField();
        panel.add(idField);

        buildTitoIdField();
        panel.add(idtito);

        buildCompIdField();
        panel.add(idComponentField);

        initComponentField();

        panel.add(buildComponentLabel(), formData);
        panel.add(buildToolLookUpPanel(), formData);
        panel.add(new Html("<br/>"), formData); //$NON-NLS-1$
        panel.add(nameField, formData);

        panel.add(new Html("<br/>"), formData); //$NON-NLS-1$
        descField = buildComponentDescriptionField();
        panel.add(descField, formData);
        
        panel.add(new Html("<br/>"), formData); //$NON-NLS-1$
        refPanel = buildRefPanel();
        panel.add(new FormLabel(I18N.DISPLAY.referencesLabel()), formData);
        panel.add(refPanel, formData);
    }

    private Label buildComponentLabel() {
        Label l = new Label(I18N.DISPLAY.selectedTool() + ":"); //$NON-NLS-1$

        l.setStyleAttribute("color", "black"); //$NON-NLS-1$ //$NON-NLS-2$
        l.setStyleAttribute("font-size", "11px"); //$NON-NLS-1$ //$NON-NLS-2$
        l.setStyleAttribute("padding-bottom", "5px"); //$NON-NLS-1$ //$NON-NLS-2$

        return l;
    }

    private void buildTemplateIdField() {
        idField = new HiddenField<String>();
        idField.setId(Template.ID);
    }

    private void buildTitoIdField() {
        idtito = new HiddenField<String>();
        idtito.setId(Template.TITO_ID);
    }

    private void buildCompIdField() {
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

    @SuppressWarnings("unchecked")
    private void initComponentField() {
        DeployedComponentSearchUtil util = new DeployedComponentSearchUtil();
        dcCombo = (ComboBox<DeployedComponent>)util.buildSearchField();
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

    private MultiTextFieldPanel buildRefPanel() {
        MultiTextFieldPanel pnl = new MultiTextFieldPanel();
        pnl.setWidth(750);
        return pnl;
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
        List<String> references = refPanel.getValues();
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

    private HorizontalPanel buildToolLookUpPanel() {
        HorizontalPanel panel = new HorizontalPanel();
        panel.setLayout(new FitLayout());
        dcCombo.setWidth(640);
        panel.setSpacing(5);
        panel.add(dcCombo);
        Button lookup = new Button(I18N.DISPLAY.browse(),
                new SelectionListener<ButtonEvent>() {
                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        showLookUpDialog();
                    }
                });
        lookup.setId(ID_BTN_BROWSE);
        panel.add(lookup);
        panel.setBorders(true);

        return panel;
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
}
