package org.iplantc.core.tito.client.dialogs;

import org.iplantc.core.client.widgets.dialogs.IFileSelectDialog;
import org.iplantc.core.uidiskresource.client.models.File;
import org.iplantc.core.uidiskresource.client.models.Permissions;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;

/**
 * A dialog to display DummyFileSelctor
 * 
 * @author sriram
 *
 */
public class DummyFileSelectDialog extends Dialog implements IFileSelectDialog {
    /**
     * Instantiate from a tag and caption.
     * 
     * @param tag unique tag for this dialog.
     * @param caption caption to display.
     * @param file the selected file
     * @param currentFolderId id of the folder where the selected file resides
     */
    public DummyFileSelectDialog(String tag, String caption, File file, String currentFolderId) {
        this(tag, caption, 565, file, currentFolderId);
    }

    /**
     * Instantiate from tag, caption and width.
     * 
     * @param tag unique tag for this dialog.
     * @param caption caption to display.
     * @param file the selected file
     * @param currentFolderId id of the folder where the selected file resides
     */
    public DummyFileSelectDialog(String tag, String caption, int width, File file, String currentFolderId) {
        setResizable(false);
        setButtons(Dialog.OKCANCEL);
        setButtonAlign(HorizontalAlignment.RIGHT);
        setLayout(new FitLayout());
        setResizable(false);
        setModal(true);
        setHideOnButtonClick(true);
        setHeading(caption);
        setWidth(width);
    }

    /**
     * Set the argument file to be selected in the dialog.
     * 
     * @param file the file to be selected.
     */
    public void select(File file) {
        // do nothing intentionally
    }

    /**
     * Retrieve selected file.
     * 
     * @return file the user has selected.
     */
    public File getSelectedFile() {
        return new File("dummy_id", "dummy_file_name", new Permissions(true, false, false)); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * Set the argument folder id to which the file belongs to.
     * 
     * @param id the id of the folder where the file is present
     */

    public void setCurrentFolder(String id) {
        // do nothing intentionally
    }

    /**
     * Set the argument folder id to which the file belongs to.
     */

    public String getCurrentFolder() {
        return "dummy_folder_id"; //$NON-NLS-1$
    }

    @Override
    public void addOkClickHandler(SelectionListener<ButtonEvent> handler) {
        // do nothing intentionally

    }

}
