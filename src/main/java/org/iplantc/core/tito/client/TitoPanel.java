package org.iplantc.core.tito.client;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.tito.client.panels.TemplateTabPanel;
import org.iplantc.core.tito.client.services.EnumerationServices;
import org.iplantc.core.uiapplications.client.events.AppSearchResultSelectedEvent;
import org.iplantc.core.uicommons.client.ErrorHandler;

import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Defines the overall layout for the root panel of the web application.
 * 
 * @author sriram
 */
public class TitoPanel extends LayoutContainer {
    private TemplateTabPanel pnlAppTemplate;
    public static String tag;

    /**
     * Default constructor.
     * 
     * @param tag a window tag for this panel
     */
    public TitoPanel(String winTag) {
        tag = winTag;
        setLayout(new FitLayout());
    }
    
    public void cleanup() {
        cleanupTemplateTabPanel();
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
    public void replaceContent(Component view) {
        removeAll();

        if (view != null) {
            add(view);
        }

        if (isRendered()) {
            layout();
        }
    }

    public void newTool() {
    	pnlAppTemplate = new TemplateTabPanel();
    	replaceContent(pnlAppTemplate);
    }
    
    public void loadFromJson(JSONObject obj) {
    	 pnlAppTemplate = new TemplateTabPanel(obj, true);
    	 replaceContent(pnlAppTemplate);
    }

    public boolean isDirty() {
        if (pnlAppTemplate != null) {
            return pnlAppTemplate.templateChanged();
        }

        return false;
    }

    public void load(final String id) {
        EnumerationServices services = new EnumerationServices();
        services.getIntegrationById(id, new AsyncCallback<String>() {

            @Override
            public void onSuccess(String result) {
                JSONArray jsonObjects = JsonUtil.getArray(JsonUtil.getObject(result), "objects"); //$NON-NLS-1$
                if (jsonObjects != null && jsonObjects.size() > 0) {
                    pnlAppTemplate = new TemplateTabPanel(jsonObjects.get(0).isObject(), false);
                    replaceContent(pnlAppTemplate);
                    AppSearchResultSelectedEvent event = new AppSearchResultSelectedEvent(tag, null, id);
                    org.iplantc.core.uicommons.client.events.EventBus.getInstance().fireEvent(event);
                }
            }

            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(I18N.DISPLAY.cantLoadTemplate(), caught);
            }
        });
    }


    /**
     * 
     * get current tito config for state management
     * 
     * @return
     */
    public JSONObject getTitoConfig() {
    	if(pnlAppTemplate != null) {
    		return pnlAppTemplate.toJson();
    	}
    	return null;
    }

    @Override
    public String getId() {
        if (pnlAppTemplate != null) {
            return pnlAppTemplate.getTitoId();
        }

        return null;
    }

}
