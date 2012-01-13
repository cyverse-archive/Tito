package org.iplantc.js.integrate.client.widgets;

import java.util.Date;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.js.integrate.client.Constants;
import org.iplantc.js.integrate.client.I18N;
import org.iplantc.js.integrate.client.events.AfterTemplateLoadEvent;
import org.iplantc.js.integrate.client.images.Resources;
import org.iplantc.js.integrate.client.models.Template;
import org.iplantc.js.integrate.client.models.TitoProperties;
import org.iplantc.js.integrate.client.services.EnumerationServices;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.Format;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

public abstract class PublishButton extends Button {
    private Template template;
    private String templateId;

    public PublishButton() {
        super(I18N.DISPLAY.publish(), AbstractImagePrototype.create(Resources.ICONS.publish()));
        addSelectionListener(new PublishPrivateSelectionListener());

        setEnabled(false);
    }

    public void setTemplate(Template template) {
        this.template = template;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    /**
     * @return the template
     */
    public Template getTemplate() {
        return template;
    }

    /**
     * @return the templateId
     */
    public String getTemplateId() {
        return templateId;
    }

    private class PublishPrivateSelectionListener extends SelectionListener<ButtonEvent> {
        /**
         * Displays a confirmation dialog with a warning message if all parameters are unordered. Calls
         * publish otherwise, or if the user confirms the warning.
         */
        @Override
        public void componentSelected(ButtonEvent ce) {
            // check if any parameters are still unordered
            boolean paramsOrdered = isOrdered();

            if (paramsOrdered) {
                // the user has already ordered their parameters.
                publishToWorkspace();
            } else {
                // Display a warning to the user before publishing.
                MessageBox.confirm(I18N.DISPLAY.publish(), I18N.DISPLAY.publishOrderingWarning(),
                        new Listener<MessageBoxEvent>() {
                            @Override
                            public void handleEvent(MessageBoxEvent be) {
                                if (be.getButtonClicked().getItemId() == Dialog.YES) {
                                    // the user wishes to publish anyway
                                    publishToWorkspace();
                                } else {
                                    unorderedNoPublish();
                                }
                            }
                        });
            }
        }
    }

    private void publishToWorkspace() {
        if (template == null) {
            new EnumerationServices().getIntegrationById(templateId, new AsyncCallback<String>() {
                @Override
                public void onFailure(Throwable arg0) {
                }

                @Override
                public void onSuccess(String result) {
                    JSONArray jsonObjects = JsonUtil.getArray(JsonUtil.getObject(result), "objects"); //$NON-NLS-1$
                    if (jsonObjects != null && jsonObjects.size() > 0) {
                        template = new Template(jsonObjects.get(0).isObject());
                        EventBus.getInstance().fireEvent(new AfterTemplateLoadEvent(templateId, true));
                        publishToWorkspace(template);
                    }
                }
            });
        } else {
            publishToWorkspace(template);
        }
    }

    /**
     * Publishes this tool to the DE, saving first if necessary.
     */
    private void publishToWorkspace(Template template) {
        String origDateEdited = template.getDateEdited();
        String origDatePublished = template.getDatePublished();

        String timestamp = String.valueOf(new Date().getTime());
        template.setDatePublished(timestamp);

        JSONObject json = template.toJsonExtended();

        if (json != null) {
            EnumerationServices services = new EnumerationServices();
            services.saveAndPublish(json.toString(), new PublishServiceCallback(origDateEdited,
                    origDatePublished));
        } else {
            ErrorHandler.post(I18N.DISPLAY.publishFailure(I18N.DISPLAY.publishErrorEmptyJson()));
        }
    }

    /** Called when the user chooses not to publish after being informed the parameters are not ordered */
    protected abstract void unorderedNoPublish();

    /** Returns whether the tool parameters have been ordered */
    protected abstract boolean isOrdered();

    /** Called after a successful publish */
    protected abstract void afterPublishSucess(String result);

    private class PublishServiceCallback implements AsyncCallback<String> {
        private final String dateEditedOnFailure;
        private final String datePublishedOnFailure;

        public PublishServiceCallback(String dateEditedOnFailure, String datePublishedOnFailure) {
            this.dateEditedOnFailure = dateEditedOnFailure;
            this.datePublishedOnFailure = datePublishedOnFailure;
        }

        @Override
        public void onSuccess(String result) {
            afterPublishSucess(result);

            // build html from "success" message and a link to the DE
            String msg = Format.substitute(
                    "{0}<p/><a href=\"{1}?{2}={3}\" target=\"_blank\">{4}</a>", //$NON-NLS-1$
                    I18N.DISPLAY.publishSuccess(), TitoProperties.getInstance().getDeUrl(),
                    Constants.CLIENT.appIdParam(), template.getTitoId(),
                    I18N.DISPLAY.publishedAppLinkText());

            MessageBox.info(I18N.DISPLAY.publish(), msg, null);
        }

        @Override
        public void onFailure(Throwable caught) {
            // Reset the published and edited dates back to originals.
            template.setDateEdited(dateEditedOnFailure);
            template.setDatePublished(datePublishedOnFailure);

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
}
