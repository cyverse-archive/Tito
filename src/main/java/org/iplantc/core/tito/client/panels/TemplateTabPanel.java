package org.iplantc.core.tito.client.panels;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.iplantc.core.client.widgets.utils.UsefulTextArea;
import org.iplantc.core.client.widgets.utils.ValidatorHelper;
import org.iplantc.core.client.widgets.validator.IPlantValidator;
import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.metadata.client.property.Property;
import org.iplantc.core.metadata.client.property.groups.PropertyGroupContainer;
import org.iplantc.core.tito.client.I18N;
import org.iplantc.core.tito.client.dialogs.PreviewDialog;
import org.iplantc.core.tito.client.events.CommandLineArgumentChangeEvent;
import org.iplantc.core.tito.client.events.CommandLineArgumentChangeEventHandler;
import org.iplantc.core.tito.client.events.ExecutableChangeEvent;
import org.iplantc.core.tito.client.events.ExecutableChangeEventHandler;
import org.iplantc.core.tito.client.events.NavigationTreeDeleteEvent;
import org.iplantc.core.tito.client.events.NavigationTreeDeleteEventHandler;
import org.iplantc.core.tito.client.events.NewToolRequestSubmitEvent;
import org.iplantc.core.tito.client.events.NewToolRequestSubmitEventHandler;
import org.iplantc.core.tito.client.events.TemplateNameChangeEvent;
import org.iplantc.core.tito.client.events.TemplateNameChangeEventHandler;
import org.iplantc.core.tito.client.events.TemplateSaveEvent;
import org.iplantc.core.tito.client.events.TemplateSaveEventHandler;
import org.iplantc.core.tito.client.events.ToolSelectedEvent;
import org.iplantc.core.tito.client.events.ToolSelectedEventHandler;
import org.iplantc.core.tito.client.images.Resources;
import org.iplantc.core.tito.client.models.Template;
import org.iplantc.core.tito.client.services.EnumerationServices;
import org.iplantc.core.tito.client.widgets.PublishButton;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.events.EventBus;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * 
 * A tab panel containing tabs for template integration
 * 
 * @author sriram
 * 
 */
public class TemplateTabPanel extends ContentPanel {
    private static final String ID_BTN_NEW_TOOL_BTN = "idBtnNewToolBtn"; //$NON-NLS-1$
    private static final String ID_BTN_PUB = "idBtnPub"; //$NON-NLS-1$
    private static final String ID_MU_ITM_JSON_PREV = "idMuItmJsonPrev"; //$NON-NLS-1$
    private static final String ID_MU_ITM_UI_PREV = "idMuItmUiPrev"; //$NON-NLS-1$
    private static final String ID_BTN_PREVIEW = "idBtnPreview"; //$NON-NLS-1$
    private static final String ID_MU_PREVIEW = "idMuPreview"; //$NON-NLS-1$
    private static final String ID_BTN_CMD_LINE = "idBtnCmdLine"; //$NON-NLS-1$
    private static final String ID_BACK = "idBtnBack"; //$NON-NLS-1$

    private ArrayList<HandlerRegistration> handlers;
    private TabPanel panel;
    private final ContentPanel pnlContents;
    private TemplateInfoEditorPanel templateInfo;
    private WidgetPanel pnlWidgetsdObj;
    private Window newToolRequestWin;

    private PublishButton btnPublish;
  
    private byte[] hash;

    private final String UNTITLED = I18N.DISPLAY.untitled();

    public TemplateTabPanel() {
        this(null, false);
    }

    /**
     * @param json the tool to display, or null for a new tool
     * @param forceSave when set to true, it will prompt users to save the changes before they navigate
     *            away from this page
     */
    public TemplateTabPanel(JSONObject json, boolean forceSave) {
        init();

        pnlContents = new ContentPanel();
        setSize(800, 600);
        pnlContents.setSize(800, 600);
        pnlContents.setLayout(new FitLayout());
        pnlContents.setHeaderVisible(true);
        pnlContents.setBodyBorder(false);

        String heading = JsonUtil.getString(json, "name"); //$NON-NLS-1$
        if (heading.isEmpty()) {
            heading = UNTITLED;
        }
        pnlContents.setHeading(heading);

        add(pnlContents);
        addHandlers();
        buildToolBar();
        initTabs(json, forceSave);
        btnPublish.setTemplate(templateInfo.getTemplate());
    }
    
    public String getTitoId() {
    	return templateInfo.getTitoId();
    }

    private void init() {
        setBodyBorder(false);
        setHeaderVisible(false);
        setLayout(new FitLayout());

    }

    private PropertyGroupContainer buildDefaultContainer() {
        PropertyGroupContainer ret = new PropertyGroupContainer(
                "{\"id\":\"--root-PropertyGroupContainer--\"}"); //$NON-NLS-1$

        return ret;
    }

    private void initTabs(JSONObject json, boolean forceSave) {
        pnlContents.removeAll();

        Template t = new Template(json);

        if (t.getContainer() == null) {
            t.setContainer(buildDefaultContainer());
        }

        pnlWidgetsdObj = new WidgetPanel(t.getContainer());
        // create this after WidgetPanel because it fires an event WidgetPanel listens to
        templateInfo = new TemplateInfoEditorPanel(t);

        addToTabs();

        if (!forceSave) {
            updateHash();
        }
    }

    private void addToTabs() {
        panel = new TabPanel();
        final TabItem templateItem = new TabItem(I18N.DISPLAY.template());
        templateItem.add(templateInfo);

        panel.add(templateItem);

        final TabItem paramItem = new TabItem(I18N.DISPLAY.arguments());
        paramItem.setLayout(new FitLayout());
        paramItem.add(pnlWidgetsdObj);
        panel.add(paramItem);

        pnlContents.add(panel);
    }

    /**
     * Validates all input on the currently selected tab.
     * 
     * @return true if valid, false otherwise
     */
    private boolean validateCurrentTab() {
        TabItem tab = panel.getSelectedItem();
        if (tab == null) {
            return true;
        }

        if (tab.getItems().contains(templateInfo)) {
            return templateInfo.validate();
        } else if (tab.getItems().contains(pnlWidgetsdObj)) {
            return pnlWidgetsdObj.validate();
        } else {
            return true;
        }
    }

    private void addHandlers() {
        EventBus bus = EventBus.getInstance();
        handlers = new ArrayList<HandlerRegistration>();

        handlers.add(bus.addHandler(TemplateSaveEvent.TYPE, new TemplateSaveEventHandlerImpl()));
        handlers.add(bus.addHandler(TemplateNameChangeEvent.TYPE,
                new TemplateNameChangeEventHandlerImpl()));
        handlers.add(bus.addHandler(ExecutableChangeEvent.TYPE, new ExecutableChangeEventHandlerImpl()));
        handlers.add(bus.addHandler(ToolSelectedEvent.TYPE, new ToolSelectedEventHandlerImpl()));
        handlers.add(bus.addHandler(NewToolRequestSubmitEvent.TYPE,
                new NewToolRequestSubmitEventHandlerImpl()));
        handlers.add(bus.addHandler(CommandLineArgumentChangeEvent.TYPE,
                new CommandLineArgumentChangeEventHandlerImpl()));
        handlers.add(bus.addHandler(NavigationTreeDeleteEvent.TYPE,
                new NavigationTreeDeleteEventHandlerImpl()));
    }

    private void buildToolBar() {
        ToolBar tool = new ToolBar();

        tool.add(buildPublishButton());
        tool.add(buildNewToolRequestButton());
        tool.add(buildCmdLineOrderButton());
        tool.add(buildPreviewMenu());

        pnlContents.setTopComponent(tool);
    }


    private Button buildPublishButton() {
        btnPublish = new PublishButton() {
            @Override
            protected void unorderedNoPublish() {
                // the user does not want to publish yet, display the ordering grid.
                showOrderingGrid();
            }

            @Override
            protected boolean isOrdered() {
                for (Property param : pnlWidgetsdObj.getProperties()) {
                    if (param.getOrder() < 1) {
                        return false;
                    }
                }
                return true;
            }

            @Override
            protected void afterPublishSucess(String result) {
                updateTemplateIdentifiers(result);
            }
        };

        btnPublish.setId(ID_BTN_PUB);

        return btnPublish;
    }

    private Button buildCmdLineOrderButton() {
        Button cmdLineOrder = new Button(I18N.DISPLAY.commandLineOrder());
        cmdLineOrder.setId(ID_BTN_CMD_LINE);
        cmdLineOrder.addSelectionListener(new CmdLineOrderSelectionListenerImpl());
        cmdLineOrder.setIcon(AbstractImagePrototype.create(Resources.ICONS.listItems()));
        return cmdLineOrder;
    }

    private Button buildPreviewMenu() {
        Menu menuPreview = new Menu();
        menuPreview.setId(ID_MU_PREVIEW);

        MenuItem preview_ui = new MenuItem(I18N.DISPLAY.previewUI(), new PreviewSelectionListener());
        preview_ui.setId(ID_MU_ITM_UI_PREV);
		menuPreview.add(preview_ui);
      
		MenuItem preview_json = new MenuItem(I18N.DISPLAY.jsonPreview(), new JSONPreviewSelectionListener());
		preview_json.setId(ID_MU_ITM_JSON_PREV);
		menuPreview.add(preview_json);

        Button btnPreview = new Button(I18N.DISPLAY.preview(),
                AbstractImagePrototype.create(Resources.ICONS.preview()));
        btnPreview.setId(ID_BTN_PREVIEW);

        btnPreview.setMenu(menuPreview);

        return btnPreview;
    }

    private Button buildNewToolRequestButton() {
        Button newToolBtn = new Button(I18N.DISPLAY.requestNewTool());
        newToolBtn.setIcon(AbstractImagePrototype.create(Resources.ICONS.add()));
        newToolBtn.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                showToolRequestDialog();
            }
        });
        newToolBtn.setId(ID_BTN_NEW_TOOL_BTN);
        return newToolBtn;
    }

    private void showToolRequestDialog() {
        newToolRequestWin = new Window();

        ToolRequestFormPanel requestForm = new ToolRequestFormPanel();
        requestForm.getSubmitButton()
                .addSelectionListener(new NewToolSelectionListenerImpl(requestForm));
        requestForm.getCancelButton().addSelectionListener(new NewToolSelectionCancelListenerImpl());

        newToolRequestWin.setHeading(I18N.DISPLAY.requestNewTool());
        newToolRequestWin.setLayout(new FitLayout());
        newToolRequestWin.setSize(500, 500);
        newToolRequestWin.setResizable(false);
        newToolRequestWin.add(requestForm);
        newToolRequestWin.show();
    }

    private void displayPreview() {
        JSONObject json = toJson();

        EnumerationServices services = new EnumerationServices();
        services.getPreview(json, new AsyncCallback<String>() {

            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject response = JsonUtil.getObject(result);
                    if (response == null) {
                        throw new Exception("Invalid response: " + result); //$NON-NLS-1$
                    }

                    // look for the analyses array
                    JSONValue analyses = response.get("analyses"); //$NON-NLS-1$
                    if (analyses != null && analyses.isArray() != null) {
                        response = JsonUtil.getObjectAt(analyses.isArray(), 0);
                    }

                    PreviewDialog dlg = new PreviewDialog(response);

                    dlg.show();
                } catch (Exception e) {
                    onFailure(e);
                }
            }

            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(I18N.DISPLAY.previewFailure(), caught);
            }
        });

    }

    private class PreviewSelectionListener extends SelectionListener<MenuEvent> {
        @Override
        public void componentSelected(MenuEvent ce) {
            displayPreview();
        }
    }

    private class TemplateSaveEventHandlerImpl implements TemplateSaveEventHandler {

        @Override
        public void onSave(TemplateSaveEvent event) {
            save();
        }

    }

    private class TemplateNameChangeEventHandlerImpl implements TemplateNameChangeEventHandler {

        @Override
        public void onSelectionChange(TemplateNameChangeEvent event) {
            String newTemplateName = event.getNewValue();
            pnlContents.setHeading(newTemplateName);

            boolean templateHasName = newTemplateName != null && !newTemplateName.isEmpty();
            boolean templateHasInfo = templateInfo != null && templateInfo.getComponent() != null
                    && !templateInfo.getComponent().isEmpty();

            btnPublish.setEnabled(templateHasName && templateHasInfo && validateProperties());
        }

    }

    private class ExecutableChangeEventHandlerImpl implements ExecutableChangeEventHandler {

        @Override
        public void onChange(ExecutableChangeEvent event) {
            if (templateInfo != null) {
                btnPublish.setEnabled(templateInfo.validate() & validateProperties());
            }
        }

    }

    private class CmdLineOrderSelectionListenerImpl extends SelectionListener<ButtonEvent> {

        @Override
        public void componentSelected(ButtonEvent ce) {
            showOrderingGrid();
        }

    }

    private class JSONPreviewSelectionListener extends SelectionListener<MenuEvent> {
        @Override
        public void componentSelected(MenuEvent ce) {
            JSONObject json = toJson();
            String print = prettyPrint(json.toString(), null, '\t');
            showJsonPreview(print);
        }

    }

    private class ToolSelectedEventHandlerImpl implements ToolSelectedEventHandler {
        @Override
        public void onSelection(ToolSelectedEvent event) {
            String heading = pnlContents.getHeading();
            btnPublish.setEnabled(event.isSelected() && heading != null && !heading.isEmpty()
                    && !heading.equals(UNTITLED) & validateProperties());
        }

    }

    public boolean templateChanged() {
        return !arraysEqual(hash, generateHash(toJson().toString()));
    }

    private boolean arraysEqual(byte[] arr1, byte[] arr2) {
        boolean retval = false;
        if (arr1 != null && arr2 != null && arr1.length == arr2.length) {
            retval = true;
            for (int i = 0; i < arr1.length; i++) {
                if (arr1[i] != arr2[i]) {
                    retval = false;
                    break;
                }
            }
        }
        return retval;
    }

    private void save() {
        if (!validateCurrentTab()) {
            return;
        }

        templateInfo.getTemplate().setDateEdited(String.valueOf(new Date().getTime()));

        JSONObject json = toJson();
        if (json != null) {
            if (!JsonUtil.getString(json, "tito").isEmpty()) { //$NON-NLS-1$
                doSave(json);
            } else {
                doNew(json);
            }
        } else {
            ErrorHandler.post(I18N.DISPLAY.saveFailed());
        }
    }

    private void doNew(final JSONObject json) {
        EnumerationServices services = new EnumerationServices();
        services.addIntegration(json, new AsyncCallback<String>() {
            @Override
            public void onSuccess(String result) {
                updateTemplateIdentifiers(result);
                MessageBox.info(I18N.DISPLAY.saved(), I18N.DISPLAY.templateSaved(), null);
            }

            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(I18N.DISPLAY.saveFailed(), caught);
            }
        });
    }

    private void doSave(final JSONObject json) {
        EnumerationServices services = new EnumerationServices();
        services.saveIntegration(json, new AsyncCallback<String>() {
            @Override
            public void onSuccess(String result) {
                updateHash();
                MessageBox.info(I18N.DISPLAY.saved(), I18N.DISPLAY.templateSaved(), null);
            }

            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(I18N.DISPLAY.saveFailed(), caught);
            }
        });
    }

    /**
     * Shows the command line ordering grids.
     */
    private void showOrderingGrid() {
        // build the containing dialog window.
        Dialog dlgSetCmdLineOrder = new Dialog();
        dlgSetCmdLineOrder.setHeading(I18N.DISPLAY.commandLineOrder());
        dlgSetCmdLineOrder.setLayout(new FitLayout());
        dlgSetCmdLineOrder.setSize(640, 480);
        dlgSetCmdLineOrder.setResizable(false);
        dlgSetCmdLineOrder.setHideOnButtonClick(true);
        dlgSetCmdLineOrder.setModal(true);

        // change the default "OK" button text to "Done"
        Button okButton = (Button)dlgSetCmdLineOrder.getButtonBar().getItemByItemId(Dialog.OK);
        okButton.setText(I18N.DISPLAY.done());

        // add the cmd line ordering panel to the dialog window.
        CommandLineOrderingGridPanel pnlCmdLineOrder = new CommandLineOrderingGridPanel(
                pnlWidgetsdObj.getProperties());
        pnlCmdLineOrder.setSize(640, 480);
        dlgSetCmdLineOrder.add(pnlCmdLineOrder);

        // show the dialog
        dlgSetCmdLineOrder.show();
        dlgSetCmdLineOrder.layout(true);
    }

    private void showJsonPreview(String print) {
        UsefulTextArea panel = new UsefulTextArea();
        panel.setReadOnly(true);
        panel.setValue(print);
        panel.setSize(400, 280);
        Dialog d = new Dialog();
        d.setHeading(I18N.DISPLAY.jsonPreview());
        d.setResizable(false);
        d.setLayout(new FitLayout());
        d.add(panel);
        d.setSize(500, 350);
        d.setHideOnButtonClick(true);
        d.show();
    }

    /**
     * 
     * A native method that calls java script method to pretty print json.
     * 
     * @param json the json to pretty print
     * @param replacer
     * @param space the char to used for formatting
     * @return the pretty print version of json
     */
    private native String prettyPrint(String json, String replacer, char space) /*-{
		return $wnd.JSON.stringify($wnd.JSON.parse(json), replacer, space);
    }-*/;

    /**
     * get json representation of the current template
     * 
     * @return JSONobject json representation of the current template
     */
    public JSONObject toJson() {
        JSONObject json = templateInfo.getTemplate().toJsonExtended();
        String newTemplateName = templateInfo.getTemplate().getName();
        boolean templateHasName = newTemplateName != null && !newTemplateName.isEmpty();
        boolean templateHasInfo = templateInfo != null && templateInfo.getComponent() != null
                && !templateInfo.getComponent().isEmpty();
        boolean canPublish = templateHasName && templateHasInfo && validateProperties();
        json.put("is_publishable", JSONBoolean.getInstance(canPublish)); //$NON-NLS-1$
        return json;
    }

    public void cleanup() {
        for (HandlerRegistration hanlder : handlers) {
            hanlder.removeHandler();
        }

        pnlWidgetsdObj.cleanup();
    }

    private void updateHash() {
        hash = generateHash(toJson().toString());
    }

    private byte[] generateHash(String json) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5"); //$NON-NLS-1$
            md.reset();
            md.update(json.getBytes());
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    private void updateTemplateIdentifiers(String identifier) {
        templateInfo.setTitoId(identifier);
        templateInfo.setTemplateId(identifier);
        updateHash();
    }
    
    /**
     * Returns true if there is at least one property, and all property names and default values are
     * valid.
     */
    private boolean validateProperties() {
        List<Property> properties = pnlWidgetsdObj.getProperties();
        if (properties == null || properties.isEmpty())
            return false;

        for (Property property : properties) {
            if (!validate(property)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Returns true if the name and default value of a property are valid.
     * XXX this method duplicates functionality from WizardTextField and WizardNumberField,
     * and it also relies on the fact that a property is valid if cmd line and default value are valid.
     * @param property
     * @return
     */
    private boolean validate(Property property) {
        String cmdLineRegex = IPlantValidator.buildRestrictedCmdLineCharSetRegex();

        String cmdLineOption = property.getName();
        if (cmdLineOption != null && !cmdLineOption.matches(cmdLineRegex)) {
            return false;
        }
        
        String defaultValue = property.getValue();
        if (defaultValue != null && !defaultValue.isEmpty()) {
            String type = property.getType();

            if ("Number".equals(type) && !ValidatorHelper.isDouble(defaultValue)) { //$NON-NLS-1$
                return false;
            }

            String argValueRegex = IPlantValidator.buildRestrictedArgValueCharSetRegex();

            if ("Text".equals(type) && !defaultValue.matches(argValueRegex)) { //$NON-NLS-1$
                return false;
            }
        }
        
        return true;
    }

    /** tests if the "tool" and "name" fields on the "tool description" tab have been filled in */
    private boolean validateToolInfo() {
        String templateName = templateInfo.getTemplate().getName();
        boolean templateHasName = templateName != null && !templateName.isEmpty();
        boolean templateHasInfo = templateInfo != null && templateInfo.getComponent() != null
                && !templateInfo.getComponent().isEmpty();
        return (templateHasName && templateHasInfo);
    }
    

    private class NewToolSelectionListenerImpl extends SelectionListener<ButtonEvent> {
        private final ToolRequestFormPanel requestForm;

        public NewToolSelectionListenerImpl(ToolRequestFormPanel requestForm) {
            this.requestForm = requestForm;
        }

        @Override
        public void componentSelected(ButtonEvent ce) {
            if (requestForm.validate()) {
                requestForm.submit();
            }
        }
    }

    private class NewToolSelectionCancelListenerImpl extends SelectionListener<ButtonEvent> {
        @Override
        public void componentSelected(ButtonEvent ce) {
            newToolRequestWin.hide();
        }
    }

    private class NewToolRequestSubmitEventHandlerImpl implements NewToolRequestSubmitEventHandler {
        @Override
        public void onRequestComplete(NewToolRequestSubmitEvent event) {
            if (newToolRequestWin != null) {
                newToolRequestWin.hide();
            }
        }
    }
    
    /** Validates input fields and enables/disables the publish button. */
    private class CommandLineArgumentChangeEventHandlerImpl implements CommandLineArgumentChangeEventHandler {
        @Override
        public void onChange(CommandLineArgumentChangeEvent event) {
            // check property values
            if (!validateProperties()) {
                btnPublish.disable();
                return;
            }
            
            // check name/description
            if (!validateToolInfo()) {
                btnPublish.disable();
                return;
            }
            
            // all valid
            btnPublish.enable();
        }
    }

    private class NavigationTreeDeleteEventHandlerImpl implements NavigationTreeDeleteEventHandler {
        @Override
        public void onDelete(NavigationTreeDeleteEvent event) {
            List<Property> properties = event.getTreePanel().getPropertyGroupContainer().getProperties();
            btnPublish.setEnabled(!properties.isEmpty());
            if (properties.size() == 1
                    && properties.get(0).getId().equals(event.getSelectedItem().getId())) {
                btnPublish.disable();
            }
        }
    }
}
