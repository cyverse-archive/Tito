package org.iplantc.core.tito.client;

import java.util.ArrayList;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.tito.client.events.AfterTemplateLoadEvent;
import org.iplantc.core.tito.client.events.NavigateToHomeEvent;
import org.iplantc.core.tito.client.events.NavigateToHomeEventHandler;
import org.iplantc.core.tito.client.panels.TemplateTabPanel;
import org.iplantc.core.tito.client.services.EnumerationServices;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.events.EventBus;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.event.BorderLayoutEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.Viewport;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Defines the overall layout for the root panel of the web application.
 * 
 * @author sriram
 */
public class ApplicationLayout extends Viewport {
    private ArrayList<HandlerRegistration> handlers;
    private TemplateTabPanel pnlAppTemplate;
    private Component center;

    private final FlowLayout layout;

    /**
     * Default constructor.
     */
    public ApplicationLayout() {
        // build top level layout
        layout = new FlowLayout(0);

        // make sure we re-draw when a panel expands
        layout.addListener(Events.Expand, new Listener<BorderLayoutEvent>() {
            @Override
            public void handleEvent(BorderLayoutEvent be) {
                layout();
            }
        });

        setLayout(layout);

        addListeners();
    }

    private void addListeners() {
        EventBus eventbus = EventBus.getInstance();
        handlers = new ArrayList<HandlerRegistration>();

        handlers.add(eventbus.addHandler(NavigateToHomeEvent.TYPE, new NavigateToHomeEventHandler() {
            @Override
            public void onHome() {
                cleanupTemplateTabPanel();
            //    addListOfTools();
            }
        }));
    }

    public void cleanup() {
        cleanupTemplateTabPanel();

        for (HandlerRegistration hanlder : handlers) {
            hanlder.removeHandler();
        }
    }

    private void cleanupTemplateTabPanel() {
        if (pnlAppTemplate != null) {
            pnlAppTemplate.cleanup();
        }
    }

    /**
     * Replace the contents of the center panel.
     * 
     * @param view a new component to set in the center of the BorderLayout.
     */
    public void replaceCenterPanel(Component view) {
        if (center != null) {
            remove(center);
        }

        center = view;

        BorderLayoutData data = new BorderLayoutData(LayoutRegion.CENTER);
        data.setMargins(new Margins(0));

        if (center != null) {
            add(center, data);
        }

        if (isRendered()) {
            layout();
        }
    }

    public void reset() {
        // clear our center
        if (center != null) {
            remove(center);
        }

        center = null;
    }

    public void newTool() {
    	pnlAppTemplate = new TemplateTabPanel();
    	replaceCenterPanel(pnlAppTemplate);
    }

    public void newInterface() {
        reset();
    }

    public void newWorkflow() {
        reset();
    }

    public void load(final String id) {
        EnumerationServices services = new EnumerationServices();
        services.getIntegrationById(id, new AsyncCallback<String>() {

            @Override
            public void onSuccess(String result) {
                JSONArray jsonObjects = JsonUtil.getArray(JsonUtil.getObject(result), "objects"); //$NON-NLS-1$
                if (jsonObjects != null && jsonObjects.size() > 0) {
                    pnlAppTemplate = new TemplateTabPanel(jsonObjects.get(0).isObject(), false);
                    replaceCenterPanel(pnlAppTemplate);
                    EventBus.getInstance().fireEvent(new AfterTemplateLoadEvent(id, true));
                } else {
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

    public void copy(final String id) {
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

                    pnlAppTemplate = new TemplateTabPanel(obj, true);
                    replaceCenterPanel(pnlAppTemplate);
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
