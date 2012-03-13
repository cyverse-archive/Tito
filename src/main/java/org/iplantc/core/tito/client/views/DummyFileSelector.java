package org.iplantc.core.tito.client.views;

import org.iplantc.core.client.widgets.I18N;
import org.iplantc.core.client.widgets.validator.IPlantValidator;
import org.iplantc.core.client.widgets.views.IFileSelector;
import org.iplantc.core.uidiskresource.client.models.File;
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
 * A class that simulated FileSelector widget. Used for preview purpose only
 * 
 * @author sriram
 *
 */
public class DummyFileSelector implements IFileSelector {
    private final String DUMMY_FILE_NAME = "dummy_file"; //$NON-NLS-1$
    private final String DUMMY_FILE_SELECTOR = "You will make file selection here!"; //$NON-NLS-1$

    protected File selectedFile;
    protected String currentFolderId;

    protected TextField<String> txtFilename;
    protected Command cmdChange;
    private HorizontalPanel composite = new HorizontalPanel();
    private Button btnLaunch;

    private Dialog dlgFileSelect;

    /**
     * Instantiate a new DummyFileSelector
     */
    public DummyFileSelector() {
        this(null);
    }

    /**
     * Instantiate with command for when value changes.
     * 
     * @param cmdChange command to fire.
     */
    public DummyFileSelector(Command cmdChange) {
        this.cmdChange = cmdChange;
        initWidgets();
    }

    /**
     * Initialize widgets to display.
     */
    protected void initWidgets() {
        txtFilename = new TextField<String>();
        txtFilename.setId("idFileName"); //$NON-NLS-1$
        txtFilename.setReadOnly(true);
        txtFilename.setWidth(254);

        btnLaunch = new Button(I18N.DISPLAY.browse());
        btnLaunch.setStyleAttribute("padding-left", "20px"); //$NON-NLS-1$ //$NON-NLS-2$
        btnLaunch.setId("idBtnLaunch"); //$NON-NLS-1$
        btnLaunch.addListener(Events.OnClick, new Listener<BaseEvent>() {
            public void handleEvent(BaseEvent be) {
                launchDialog();
            }
        });

        btnLaunch.focus();

        composite.add(txtFilename);
        composite.add(btnLaunch);
    }

    protected void launchDialog() {
        dlgFileSelect = new Dialog();

        dlgFileSelect.setHeading(I18N.DISPLAY.selectFile());
        dlgFileSelect.setSize(300, 150);
        dlgFileSelect.setButtons(Dialog.OKCANCEL);
        dlgFileSelect.setHideOnButtonClick(true);
        dlgFileSelect.addText(DUMMY_FILE_SELECTOR);

        Button okBtn = dlgFileSelect.getButtonById(Dialog.OK);
        okBtn.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                txtFilename.setValue(DUMMY_FILE_NAME);
                if (cmdChange != null) {
                    cmdChange.execute();
                }
            }
        });

        dlgFileSelect.show();
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
    public boolean hasSelectedFile() {
        // always true
        return true;
    }

    @Override
    public File getSelectedFile() {
        return new File("dummy_id", "dummy_file_name", new Permissions(true, false, false)); //$NON-NLS-1$ //$NON-NLS-2$
    }

    @Override
    public void setSelectedFile(File file) {
        selectedFile = file;

    }

    @Override
    public String getSelectedFileId() {

        return "dummy_id"; //$NON-NLS-1$
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
    public void displayFilename(String name) {
        txtFilename.setValue(name);

    }

    @Override
    public void setValidator(IPlantValidator validator) {
        if (validator != null) {
            txtFilename.setValidator(validator);
            txtFilename.setValidateOnBlur(true);
            txtFilename.setAllowBlank(!validator.isRequired());
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

}
