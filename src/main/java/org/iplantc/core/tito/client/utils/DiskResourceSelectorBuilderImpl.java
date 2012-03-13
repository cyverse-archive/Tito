package org.iplantc.core.tito.client.utils;

import org.iplantc.core.client.widgets.dialogs.IFileSelectDialog;
import org.iplantc.core.client.widgets.utils.ComponentValueTable;
import org.iplantc.core.client.widgets.utils.IDiskResourceSelectorBuilder;
import org.iplantc.core.client.widgets.views.IFileSelector;
import org.iplantc.core.client.widgets.views.IFolderSelector;
import org.iplantc.core.metadata.client.property.Property;
import org.iplantc.core.tito.client.dialogs.DummyFileSelectDialog;
import org.iplantc.core.tito.client.views.DummyFileSelector;
import org.iplantc.core.tito.client.views.DummyFolderSelector;
import org.iplantc.core.tito.client.views.DummyWizardFileSelector;
import org.iplantc.core.tito.client.views.DummyWizardFolderSelector;
import org.iplantc.core.uidiskresource.client.models.File;

import com.google.gwt.user.client.Command;

/**
 * 
 * A Disk Resource selector builder that knows to build a right Implementation of IFileSelector
 * implementation
 * 
 * @author sriram
 * 
 */
public class DiskResourceSelectorBuilderImpl implements IDiskResourceSelectorBuilder {
    /**
     * {@inheritDoc}
     */
    @Override
    public IFileSelector buildFileSelector(Command cmdChange) {
        return new DummyFileSelector(cmdChange);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IFileSelector buildFileSelector(Property property, ComponentValueTable tblComponentVals) {
        return new DummyWizardFileSelector(property, tblComponentVals);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IFileSelectDialog buildFileSelectorDialog(String tag, String caption, File file,
            String currentFolderId) {
        return new DummyFileSelectDialog(tag, caption, file, currentFolderId);
    }

    @Override
    public IFolderSelector buildFolderSelector(Command cmdChange) {
        return new DummyFolderSelector(cmdChange);
    }

    @Override
    public IFolderSelector buildFolderSelector(Property property, ComponentValueTable tblComponentVals) {
        return new DummyWizardFolderSelector(property, tblComponentVals);
    }
}
