package org.iplantc.core.tito.client.views;

import org.iplantc.core.client.widgets.I18N;
import org.iplantc.core.client.widgets.validator.IPlantValidator;
import org.iplantc.core.client.widgets.views.IFolderSelector;
import org.iplantc.core.uidiskresource.client.models.Folder;
import org.iplantc.core.uidiskresource.client.models.Permissions;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.gwt.user.client.Command;

/**
 * 
 * A class that simulated FolderSelector widget. Used for preview purpose only
 * 
 * @author sriram
 *
 */
public class DummyFolderSelector implements IFolderSelector {

    private final String DUMMY_FOLDER_NAME = "dummy_folder"; //$NON-NLS-1$
    private final String DUMMY_FOLDER_SELECTOR = "You will make folder selection here!"; //$NON-NLS-1$

    protected Folder selectedFolder;
    protected String currentFolderId;

    protected TextField<String> txtFoldername;
    protected Command cmdChange;
    private HorizontalPanel composite = new HorizontalPanel();
    private Button btnLaunch;

    private Dialog dlgFolderSelect;

    /**
     * Instantiate a new DummyFileSelector
     */
    public DummyFolderSelector() {
        this(null);
    }

    /**
     * Instantiate with command for when value changes.
     * 
     * @param cmdChange command to fire.
     */
    public DummyFolderSelector(Command cmdChange) {
        this.cmdChange = cmdChange;
        initWidgets();
    }

    /**
     * Initialize widgets to display.
     */
    protected void initWidgets() {
        txtFoldername = new TextField<String>();
        txtFoldername.setId("idFodlerName"); //$NON-NLS-1$
        txtFoldername.setReadOnly(true);
        txtFoldername.setWidth(254);

        btnLaunch = new Button(I18N.DISPLAY.browse());
        btnLaunch.setStyleAttribute("padding-left", "20px"); //$NON-NLS-1$ //$NON-NLS-2$
        btnLaunch.setId("idBtnLaunch"); //$NON-NLS-1$
        btnLaunch.addListener(Events.OnClick, new Listener<BaseEvent>() {
            public void handleEvent(BaseEvent be) {
                launchDialog();
            }
        });

        btnLaunch.focus();

        composite.add(txtFoldername);
        composite.add(btnLaunch);
    }

    protected void launchDialog() {
        dlgFolderSelect = new Dialog();

        dlgFolderSelect.setHeading(I18N.DISPLAY.selectFolder());
        dlgFolderSelect.setSize(300, 150);
        dlgFolderSelect.setButtons(Dialog.OKCANCEL);
        dlgFolderSelect.setHideOnButtonClick(true);
        dlgFolderSelect.addText(DUMMY_FOLDER_SELECTOR);

        Button okBtn = dlgFolderSelect.getButtonById(Dialog.OK);
        okBtn.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                txtFoldername.setValue(DUMMY_FOLDER_NAME);
                if (cmdChange != null) {
                    cmdChange.execute();
                }
            }
        });

        dlgFolderSelect.show();
    }

    @Override
    public void setId(String id) {
        composite.setId(id);

    }

    @Override
    public String getId() {
        return composite.getId();
    }

    @Override
    public Component getWidget() {

        return composite;
    }

    @Override
    public void setSpacing(int spacing) {
        composite.setSpacing(spacing);

    }

    @Override
    public void setButtonText(String text) {
        btnLaunch.setText(text);

    }

    @Override
    public void setValidator(IPlantValidator validator) {
        if (validator != null) {
            txtFoldername.setValidator(validator);
            txtFoldername.setValidateOnBlur(true);
            txtFoldername.setAllowBlank(!validator.isRequired());
        }

    }

    @Override
    public String getCurrentFolderId() {
        return "dummy_folder_id"; //$NON-NLS-1$
    }

    @Override
    public void setCurrentFolderId(String currentFolderId) {
        this.currentFolderId = currentFolderId;

    }

    @Override
    public boolean hasSelectedFolder() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Folder getSelectedFolder() {
        return new Folder("dummy_id", "dummy_folder_name", false, new Permissions(true, false, false)); //$NON-NLS-1$ //$NON-NLS-2$
    }

    @Override
    public void setSelectedFolder(Folder folder) {
        selectedFolder = folder;

    }

    @Override
    public String getSelectedFolderId() {
        return "dummy_id"; //$NON-NLS-1$
    }

    @Override
    public void displayFolderName(String name) {
        txtFoldername.setValue(name);

    }

}
