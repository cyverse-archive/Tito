package org.iplantc.core.tito.client.controllers;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.tito.client.ApplicationLayout;
import org.iplantc.core.tito.client.I18N;
import org.iplantc.core.tito.client.events.AfterTemplateLoadEvent;
import org.iplantc.core.tito.client.events.NavigateToHomeEvent;
import org.iplantc.core.tito.client.events.NavigateToHomeEventHandler;
import org.iplantc.core.tito.client.events.NewProjectEvent;
import org.iplantc.core.tito.client.events.NewProjectEventHandler;
import org.iplantc.core.tito.client.events.TemplateLoadEvent;
import org.iplantc.core.tito.client.events.TemplateLoadEventHandler;
import org.iplantc.core.tito.client.panels.TemplateTabPanel;
import org.iplantc.core.tito.client.services.EnumerationServices;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.events.EventBus;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * A controller class that initializes the layout and event handlers
 * 
 * @author sriram
 *
 */
public class ApplicationController {
    private static ApplicationController instance;
    private ApplicationLayout layout;

    private TemplateTabPanel template;

    private ApplicationController() {
        initListeners();
        disableBrowserContextMenu();
    }

    public static ApplicationController getInstance() {
        if (instance == null) {
            instance = new ApplicationController();
        }

        return instance;
    }

    private void initListeners() {
        EventBus eventbus = EventBus.getInstance();

        eventbus.addHandler(NewProjectEvent.TYPE, new NewProjectEventHandlerImpl());
        eventbus.addHandler(TemplateLoadEvent.TYPE, new TemplateLoadEventHandlerImpl());
        eventbus.addHandler(NavigateToHomeEvent.TYPE, new NavigateToHomeEventHandlerImpl());
    }

    public void init(ApplicationLayout layout) {
        this.layout = layout;
    }

    /**
     * Disable the context menu of the browser using native JavaScript.
     * 
     * This disables the user's ability to right-click on this widget and get the browser's context menu
     */
    private native void disableBrowserContextMenu()
    /*-{
		$doc.oncontextmenu = function() {
			return false;
		};
    }-*/;

    private class NewProjectEventHandlerImpl implements NewProjectEventHandler {
        @Override
        public void newInterface() {
            layout.reset();
        }

        @Override
        public void newTool() {
            template = new TemplateTabPanel();
            layout.replaceCenterPanel(template);
        }

        @Override
        public void newWorkflow() {
            layout.reset();
        }
    }

    private class TemplateLoadEventHandlerImpl implements TemplateLoadEventHandler {
        @Override
        public void onLoad(TemplateLoadEvent event) {
            if (event.getMode().equals(TemplateLoadEvent.MODE.EDIT)) {
                load(event.getIdTemplate());
            } else {
                copy(event.getIdTemplate());
            }
        }

        private void load(final String id) {
            EnumerationServices services = new EnumerationServices();
            services.getIntegrationById(id, new AsyncCallback<String>() {

                @Override
                public void onSuccess(String result) {
                    JSONArray jsonObjects = JsonUtil.getArray(JsonUtil.getObject(result), "objects"); //$NON-NLS-1$
                    if (jsonObjects != null && jsonObjects.size() > 0) {
                        template = new TemplateTabPanel(jsonObjects.get(0).isObject(), false);
                        layout.replaceCenterPanel(template);
                        EventBus.getInstance().fireEvent(new AfterTemplateLoadEvent(id, true));
                    }
                    else {
                        EventBus.getInstance().fireEvent(new AfterTemplateLoadEvent(id, false));
                    }
                }

                @Override
                public void onFailure(Throwable caught) {
                    EventBus.getInstance().fireEvent(new AfterTemplateLoadEvent(id, false));
                    ErrorHandler.post(I18N.DISPLAY.cantLoadTemplate(), caught);
                }
            });
        }
        
        
        private void copy(final String id) {
            EnumerationServices services = new EnumerationServices();
            services.getIntegrationById(id, new AsyncCallback<String>() {

                @Override
                public void onSuccess(String result) {
                    JSONArray arr = JsonUtil.getArray(JsonUtil.getObject(result), "objects"); //$NON-NLS-1$
                    if (arr != null && arr.size() > 0) {
                        JSONObject obj = arr.get(0).isObject();
                        String temp_name = JsonUtil.getString(obj, "name"); //$NON-NLS-1$
                        if (temp_name != null && !temp_name.isEmpty()) {
                            temp_name = I18N.DISPLAY.copyOfAppName(temp_name);
                        }

                        // change and remove tito id
                        obj.put("name", new JSONString(temp_name)); //$NON-NLS-1$
                        obj.put("tito", new JSONString("")); //$NON-NLS-1$ //$NON-NLS-2$

                        template = new TemplateTabPanel(obj, true);
                        layout.replaceCenterPanel(template);
                        EventBus.getInstance().fireEvent(new AfterTemplateLoadEvent(id, true));
                    }
                }

                @Override
                public void onFailure(Throwable caught) {
                    EventBus.getInstance().fireEvent(new AfterTemplateLoadEvent(id, false));
                    ErrorHandler.post(I18N.DISPLAY.cantLoadTemplate(), caught);
                }
            });
        }
    }

    private class NavigateToHomeEventHandlerImpl implements NavigateToHomeEventHandler {
        @Override
        public void onHome() {
            if (template != null) {
                template.cleanup();
            }
        }
    }
}
