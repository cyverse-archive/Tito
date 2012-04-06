package org.iplantc.core.tito.client.panels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.iplantc.core.client.widgets.BoundedTextArea;
import org.iplantc.core.client.widgets.BoundedTextField;
import org.iplantc.core.client.widgets.utils.FormLabel;
import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.tito.client.I18N;
import org.iplantc.core.tito.client.events.ExecutableChangeEvent;
import org.iplantc.core.tito.client.events.TemplateNameChangeEvent;
import org.iplantc.core.tito.client.events.ToolSelectedEvent;
import org.iplantc.core.tito.client.models.DeployedComponent;
import org.iplantc.core.tito.client.models.JsDeployedComponent;
import org.iplantc.core.tito.client.models.Template;
import org.iplantc.core.tito.client.services.EnumerationServices;
import org.iplantc.core.tito.client.utils.DeployedComponentSorter;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.events.EventBus;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.FormPanel.LabelAlign;
import com.extjs.gxt.ui.client.widget.form.HiddenField;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * 
 * A panel to enable editing for Template info
 * 
 * @author sriram
 *
 */
public class TemplateInfoEditorPanel extends ContentPanel {
    private static final String ID_BTN_BROWSE = "idBtnBrowse";

	private static final String ID_FLD_DESC = "idFldDesc";

	private static final String ID_FLD_D_COMP = "idFldDComp";

	private static final String ID_FLD_NAME = "idFldName";

	private Template template;

    private FormData formData;
    private FormPanel panel;

    private TextField<String> nameField;
    private TextField<String> descField;
    private TextField<String> componentField;
    private MultiTextFieldPanel refPanel;
    private HiddenField<String> idField;
    private HiddenField<String> idComponentField;
    private HiddenField<String> idtito;

    private Grid<DeployedComponent> grid;

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
        if (comp != null) {
            componentField.setValue(comp);

            if (comp.isEmpty()) {
                fireToolSelectedEvent(false);
            } else {
                fireToolSelectedEvent(true);
            }

        }

        String compId = template.getCompId();
        if (compId != null && !compId.isEmpty()) {
            idComponentField.setValue(compId);
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
        setStyleAttribute("padding-bottom", "10px"); //$NON-NLS-1$ //$NON-NLS-2$

        panel = new FormPanel();
        panel.setSize(800, 600);
        panel.setScrollMode(Scroll.AUTOY);
        panel.setBodyBorder(false);
        panel.setFrame(true);
        panel.setHeaderVisible(false);
        panel.setBodyBorder(false);
        panel.setLabelAlign(LabelAlign.TOP);
        formData = new FormData("-20"); //$NON-NLS-1$

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

        componentField = buildComponentTextField();

        panel.add(buildComponentLabel(), formData);
        panel.add(buildToolLookUpPanel(), formData);
        panel.add(new Html("<br/>"), formData); //$NON-NLS-1$
        panel.add(nameField, formData);

        panel.add(new Html("<br/>"), formData); //$NON-NLS-1$
        descField = buildComponentDescriptionField();
        panel.add(descField, formData);
        
        panel.add(new Html("<br/>"), formData); //$NON-NLS-1$
        refPanel = buildRefPanel();
        panel.add(new FormLabel(I18N.DISPLAY.referencesLabel()));
        panel.add(refPanel);
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

        return field;
    }

    private TextField<String> buildComponentTextField() {
        final TextField<String> field = new TextField<String>();
        field.setId(ID_FLD_D_COMP);
        field.setReadOnly(true);
        field.setStyleAttribute("padding-bottom", "5px"); //$NON-NLS-1$ //$NON-NLS-2$
        field.setEmptyText(I18N.DISPLAY.componentFieldEmptyText());

        field.setFireChangeEventOnSetValue(true);
        field.addListener(Events.Change, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                ExecutableChangeEvent event = new ExecutableChangeEvent(field.getValue());
                EventBus.getInstance().fireEvent(event);
            }
        });
        
        return field;
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
        pnl.setWidth(748);
        return pnl;
    }
    
    public Template getTemplate() {
        String id = idField.getValue();
        String name = nameField.getValue();
        String desc = descField.getValue();
        String compId = idComponentField.getValue();
        String comp = componentField.getValue();
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

    private void buildcomponentLookupGrid() {
        ListStore<DeployedComponent> store = new ListStore<DeployedComponent>();
        grid = new Grid<DeployedComponent>(store, buildColumnModel());
        grid.setAutoExpandColumn("name"); //$NON-NLS-1$
        grid.getView().setEmptyText(I18N.DISPLAY.noComponents());
        grid.setBorders(false);
        grid.setStripeRows(true);
        grid.setColumnLines(true);
        grid.getStore().setStoreSorter(new DeployedComponentSorter());
    }

    private void initGridSelectionModel(final Button btnOk) {
        grid.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        grid.getSelectionModel().addListener(Events.SelectionChange, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                btnOk.enable();
            }
        });
    }

    private ColumnConfig buildColumnConfig(final String key, final String caption, int width) {
        ColumnConfig ret = new ColumnConfig(key, caption, width);

        ret.setSortable(true);
        ret.setMenuDisabled(true);

        return ret;
    }

    private ColumnModel buildColumnModel() {
        List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

        ColumnConfig name = buildColumnConfig(DeployedComponent.NAME, I18N.DISPLAY.name(), 250);
        name.setMenuDisabled(true);
        ColumnConfig version = buildColumnConfig(DeployedComponent.VERSION,
                I18N.DISPLAY.versionColumnHeader(), 150);
        version.setSortable(false);
        version.setMenuDisabled(true);

        configs.add(name);
        configs.add(version);

        return new ColumnModel(configs);
    }

    private void showLookUpDialog() {
        Dialog dialog = new Dialog();
        dialog.setTopComponent(buildToolBar());
        dialog.setButtons(Dialog.OKCANCEL);

        Button bntOk = dialog.getButtonById(Dialog.OK);
        bntOk.addSelectionListener(new DialogOkBtnSelectionListenerImpl());
        bntOk.disable();

        dialog.setHideOnButtonClick(true);
        dialog.setHeading(I18N.DISPLAY.component());
        dialog.setLayout(new FitLayout());
        dialog.setSize(460, 500);
        buildcomponentLookupGrid();
        initGridSelectionModel(bntOk);

        getDeployedComponents();
        dialog.add(grid);
        dialog.setScrollMode(Scroll.AUTOY);
        dialog.show();
    }

    private TextField<String> buildFilterField() {
        final TextField<String> str = new TextField<String>() {
            @Override
            public void onKeyUp(FieldEvent fe) {
                String filter = getValue();
                if (filter != null && !filter.isEmpty()) {
                    grid.getStore().filter("name", filter); //$NON-NLS-1$
                } else {
                    grid.getStore().clearFilters();
                }

            }
        };

        str.setEmptyText(I18N.DISPLAY.filterEmptyText());

        return str;
    }

    private ToolBar buildToolBar() {
        ToolBar tool = new ToolBar();
        tool.add(buildFilterField());
        return tool;
    }

    private void setCurrentCompSelection() {
        List<DeployedComponent> comps = grid.getStore().getModels();

        if (idComponentField.getValue() != null && !idComponentField.getValue().equals("")) { //$NON-NLS-1$
            for (DeployedComponent dc : comps) {
                if (dc.getId().equals(idComponentField.getValue())) {
                    grid.getSelectionModel().select(false, dc);
                    break;
                }
            }
        }
    }

    private class DialogOkBtnSelectionListenerImpl extends SelectionListener<ButtonEvent> {
        @Override
        public void componentSelected(ButtonEvent ce) {
            DeployedComponent dc = grid.getSelectionModel().getSelectedItem();
            if (dc != null) {
                componentField.setValue(dc.getName());
                idComponentField.setValue(dc.getId());
                fireToolSelectedEvent(true);
            } else {
                componentField.setValue(""); //$NON-NLS-1$
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
        panel.setSpacing(5);
        componentField.setWidth(684);
        panel.add(componentField);
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

    private void getDeployedComponents() {
        EnumerationServices services = new EnumerationServices();
        services.getDeployedComponents(new AsyncCallback<String>() {
            @Override
            public void onSuccess(String result) {
                ArrayList<DeployedComponent> components = parseJson(result);
                grid.getStore().add(components);
                grid.getStore().sort(DeployedComponent.NAME, SortDir.ASC);
                setCurrentCompSelection();
            }

            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(I18N.DISPLAY.cantLoadDeployedComponents(), caught);
            }
        });
    }

    private ArrayList<DeployedComponent> parseJson(String result) {
        ArrayList<DeployedComponent> components = new ArrayList<DeployedComponent>();

        JSONArray jsonComponents = JsonUtil.getArray(JsonUtil.getObject(result), "components"); //$NON-NLS-1$
        if (jsonComponents != null) {
            JsArray<JsDeployedComponent> jscomps = JsonUtil.asArrayOf(jsonComponents.toString());
            for (int i = 0; i < jscomps.length(); i++) {
                JsDeployedComponent jsComponent = jscomps.get(i);

                DeployedComponent dc = new DeployedComponent(jsComponent.getId(), jsComponent.getName(),
                        jsComponent.getType(), jsComponent.getDescription(),
                        jsComponent.getAttribution(), jsComponent.getLocation(),
                        jsComponent.getVersion());

                components.add(dc);
            }
        }

        return components;
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
        valid &= componentField.isValid();

        return valid;
    }

    public void setTitoId(String tito) {
        idtito.setValue(tito);
    }

    public void setTemplateId(String id) {
        idField.setValue(id);
    }

    protected String getComponent() {
        return componentField.getValue();
    }
}
