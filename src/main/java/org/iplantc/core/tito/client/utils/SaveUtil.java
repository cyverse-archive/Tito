package org.iplantc.core.tito.client.utils;

import java.util.List;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.metadata.client.property.Property;
import org.iplantc.core.tito.client.I18N;
import org.iplantc.core.tito.client.TitoPanel;
import org.iplantc.core.tito.client.events.TemplateSaveEvent;
import org.iplantc.core.tito.client.models.Template;
import org.iplantc.core.tito.client.panels.CommandLineOrderingGridPanel;
import org.iplantc.core.tito.client.services.EnumerationServices;
import org.iplantc.core.uiapplications.client.events.AppGroupCountUpdateEvent;
import org.iplantc.core.uiapplications.client.events.AppSearchResultSelectedEvent;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.events.EventBus;

import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * A util class for saving and publishing apps
 * 
 * @author sriram
 * 
 */
public class SaveUtil {
    public SaveUtil() {
  
    }

    /**
     * Publishes this tool to the DE, saving first if necessary.
     */
    public void publishToWorkspace(JSONObject json) {
        if (json != null) {
            EnumerationServices services = new EnumerationServices();
            // if titoid is nullm then its a new app
            String titoId = JsonUtil.getString(json, Template.ID);
            services.saveAndPublish(json.toString(),
                    new PublishServiceCallback(JsonUtil.getString(json, Template.NAME), titoId));
        } else {
            ErrorHandler.post(I18N.DISPLAY.publishFailure(I18N.DISPLAY.publishErrorEmptyJson()));
        }
    }

    private class PublishServiceCallback implements AsyncCallback<String> {

        private String name;
        private String titoId;

        PublishServiceCallback(String name, String titoId) {
            this.name = name;
            this.titoId = titoId;
        }

        @Override
        public void onSuccess(String result) {
            EventBus.getInstance().fireEvent(new TemplateSaveEvent(result));
            AppSearchResultSelectedEvent event = new AppSearchResultSelectedEvent(TitoPanel.tag, null,
                    result);
            EventBus.getInstance().fireEvent(event);
            Info.display(I18N.DISPLAY.save(), name + " " + I18N.DISPLAY.saved());
            if (titoId == null || titoId.isEmpty()) {
                org.iplantc.core.uicommons.client.events.EventBus.getInstance().fireEvent(
                        new AppGroupCountUpdateEvent(true, null));
            }
        }

        @Override
        public void onFailure(Throwable caught) {

            // Display a message to the user about this error.
            String errMsg = I18N.DISPLAY.publishFailureDefaultMessage();

            JSONObject jsonError = parseJsonError(caught);
            if (jsonError != null) {
                String message = JsonUtil.getString(jsonError, "message"); //$NON-NLS-1$

                if (!message.isEmpty()) {
                    errMsg = message;
                }
            }

            ErrorHandler.post(I18N.DISPLAY.publishFailure(errMsg), caught);
        }

        private JSONObject parseJsonError(Throwable caught) {
            try {
                return JsonUtil.getObject(caught.getMessage());
            } catch (Exception e) {
                return null;
            }
        }
    }
    
    
    /**
     * Shows the command line ordering grids.
     */
    public void showOrderingGrid(List<Property> properties) {
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
                properties);
        pnlCmdLineOrder.setSize(640, 480);
        dlgSetCmdLineOrder.add(pnlCmdLineOrder);

        // show the dialog
        dlgSetCmdLineOrder.show();
        dlgSetCmdLineOrder.layout(true);
    }
}
