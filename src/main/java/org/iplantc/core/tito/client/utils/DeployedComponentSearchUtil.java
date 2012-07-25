package org.iplantc.core.tito.client.utils;

import java.util.ArrayList;
import java.util.List;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.tito.client.I18N;
import org.iplantc.core.tito.client.events.ExecutableChangeEvent;
import org.iplantc.core.tito.client.services.DeployedComponentSearchServiceFacade;
import org.iplantc.core.tito.client.widgets.form.MyComboBox;
import org.iplantc.core.uiapplications.client.models.Analysis;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.core.uicommons.client.models.DeployedComponent;
import org.iplantc.core.uicommons.client.models.JsDeployedComponent;

import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.BaseListLoadConfig;
import com.extjs.gxt.ui.client.data.BaseListLoader;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.ListLoader;
import com.extjs.gxt.ui.client.data.LoadEvent;
import com.extjs.gxt.ui.client.data.ModelKeyProvider;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.LoadListener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.store.StoreSorter;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.ListModelPropertyEditor;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * 
 * A util class that build advanced search combo for DC
 * 
 * @author sriram
 * 
 */
public class DeployedComponentSearchUtil {
    private String lastQueryText = "";
    private static final String ID_FLD_D_COMP = "idFldDComp";

    /**
     * Builds a combo box for searching all DC, filtered by the user's combo text, and displayed in the
     * combo's drop-down list.
     * 
     * @return A combo box of DC models, remotely loaded and filtered by the user's combo text.
     */
    public MyComboBox<DeployedComponent> buildSearchField() {
        final ListStore<DeployedComponent> store = buildStore();

        final ModelKeyProvider<DeployedComponent> storeKeyProvider = getModelKeyProvider();

        store.setKeyProvider(storeKeyProvider);

        // Use the custom key provider for model lookups from the raw text in the combo's text field.
        ListModelPropertyEditor<DeployedComponent> propertyEditor = new ListModelPropertyEditor<DeployedComponent>() {
            @Override
            public String getStringValue(DeployedComponent value) {
                return storeKeyProvider.getKey(value);
            }

            @Override
            public DeployedComponent convertStringValue(String value) {
                return store.findModel(value);
            }
        };

        final MyComboBox<DeployedComponent> combo = new MyComboBox<DeployedComponent>();
        combo.setId(ID_FLD_D_COMP);
        combo.setItemSelector("div.search-item"); //$NON-NLS-1$
        combo.setTemplate(getTemplate());
        combo.setStore(store);
        combo.setPropertyEditor(propertyEditor);
        combo.setTriggerStyle("x-form-search-trigger");
        combo.setEmptyText(I18N.DISPLAY.search());
        combo.setMinChars(3);
        combo.setFireChangeEventOnSetValue(true);
        combo.setPropertyEditor(new ListModelPropertyEditor<DeployedComponent>(DeployedComponent.NAME));

        combo.addSelectionChangedListener(new SearchComboSelectionChangeListener());

        // Since we don't want our custom key provider's string to display after a user selects a search
        // result, reset the raw text field to the cached user query string after a selection is made.
        combo.addListener(Events.Select, new SearchComboSelectEventListener());

        return combo;
    }

    private ModelKeyProvider<DeployedComponent> getModelKeyProvider() {
        // We need to use a custom key string that will allow the combobox to find the correct model if 2
        // apps in different groups have the same name, since the combo's SelectionChange event will find
        // the first model that matches the raw text in the combo's text field.
        final ModelKeyProvider<DeployedComponent> storeKeyProvider = new ModelKeyProvider<DeployedComponent>() {
            @Override
            public String getKey(DeployedComponent model) {
                return model.getId();
            }
        };
        return storeKeyProvider;
    }

    private ListStore<DeployedComponent> buildStore() {
        // Create a loader with our custom RpcProxy.
        ListLoader<ListLoadResult<DeployedComponent>> loader = new BaseListLoader<ListLoadResult<DeployedComponent>>(
                buildSearchProxy());

        // Create the store
        final ListStore<DeployedComponent> store = new ListStore<DeployedComponent>(loader);

        // Add a load listener that sorts the store's search results.
        loader.addLoadListener(new LoadListener() {
            @Override
            public void loaderLoad(LoadEvent le) {
                store.sort(Analysis.NAME, SortDir.ASC);
            }
        });

        // Set a custom store sorter to order Apps with names that match the search before description
        // matches.
        store.setStoreSorter(new CustomStoreSorter());
        return store;
    }

    /**
     * Builds an RpcProxy for the search ComboBox that will call the searchAnalysis service, then process
     * the JSON results into an Analysis list for the combo's store.
     * 
     * @return An RpcProxy for a ListLoader for the search ComboBox's store.
     */
    private RpcProxy<List<DeployedComponent>> buildSearchProxy() {
        RpcProxy<List<DeployedComponent>> proxy = new RpcProxy<List<DeployedComponent>>() {
            @Override
            protected void load(Object loadConfig, final AsyncCallback<List<DeployedComponent>> callback) {

                DeployedComponentSearchServiceFacade facade = new DeployedComponentSearchServiceFacade();

                // Get the combo's search params.
                BaseListLoadConfig config = (BaseListLoadConfig)loadConfig;

                // Create a callback for the AppTemplateServiceFacade.
                AsyncCallback<String> searchCallback = new AsyncCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        callback.onSuccess(parseJson(result));
                    }

                    @Override
                    public void onFailure(Throwable caught) {
                        ErrorHandler.post(caught);
                        callback.onFailure(caught);
                    }
                };

                // cache the query text
                lastQueryText = (String)config.get("query"); //$NON-NLS-1$

                // Call the searchAnalysis service with the combo's query.
                facade.searchDeployedComponents(lastQueryText, searchCallback);
            }
        };

        return proxy;
    }

    /**
     * @return An string of html for the search ComboBox's list results.
     */
    private String getTemplate() {
        StringBuilder template = new StringBuilder();

        template.append("<tpl for=\".\"><div class=\"search-item\">"); //$NON-NLS-1$

        template.append("<h3>"); //$NON-NLS-1$
        template.append("<b>{name}</b>"); //$NON-NLS-1$
        template.append("</h3>");//$NON-NLS-1$
        template.append("<p>"); //$NON-NLS-1$
        template.append("<b>path</b>: {location}</p>");
        // Description line
        template.append("<h4>"); //$NON-NLS-1$
        template.append("<tpl if=\"description\"><p>{description}</p></tpl>"); //$NON-NLS-1$
        template.append("</h4>"); //$NON-NLS-1$
        template.append("</div></tpl>"); //$NON-NLS-1$

        return template.toString();
    };

    public static ArrayList<DeployedComponent> parseJson(String result) {
        ArrayList<DeployedComponent> components = new ArrayList<DeployedComponent>();

        JSONArray jsonComponents = JsonUtil.getArray(JsonUtil.getObject(result), "components"); //$NON-NLS-1$
        if (jsonComponents != null) {
            JsArray<JsDeployedComponent> jscomps = JsonUtil.asArrayOf(jsonComponents.toString());
            for (int i = 0; i < jscomps.length(); i++) {
                JsDeployedComponent jsComponent = jscomps.get(i);

                DeployedComponent dc = new DeployedComponent(jsComponent.getId(), jsComponent.getName(),
                        jsComponent.getType(), jsComponent.getDescription(),
                        jsComponent.getAttribution(), jsComponent.getLocation(),
                        jsComponent.getVersion());

                components.add(dc);
            }
        }

        return components;
    }

    private final class SearchComboSelectEventListener implements Listener<FieldEvent> {
        @Override
        public void handleEvent(FieldEvent event) {
            @SuppressWarnings("unchecked")
            ComboBox<DeployedComponent> combo = (ComboBox<DeployedComponent>)event.getSource();
            combo.setRawValue(combo.getValue().getName());
        }
    }

    private final class SearchComboSelectionChangeListener extends
            SelectionChangedListener<DeployedComponent> {
        @Override
        public void selectionChanged(SelectionChangedEvent<DeployedComponent> se) {
            @SuppressWarnings("unchecked")
            ComboBox<DeployedComponent> combo = (ComboBox<DeployedComponent>)se.getSource();
            if (combo.getValue() != null) {
                DeployedComponent dc = combo.getValue();
                ExecutableChangeEvent event = new ExecutableChangeEvent(dc.getId(), dc.getName());
                EventBus.getInstance().fireEvent(event);
            }
        }
    }

    private final class CustomStoreSorter extends StoreSorter<DeployedComponent> {
        @Override
        public int compare(Store<DeployedComponent> store, DeployedComponent dc1,
                DeployedComponent app2, String property) {
            if (dc1 != null && app2 != null) {
                String searchTerm = lastQueryText.toLowerCase();

                boolean dc11NameMatches = dc1.getName().toLowerCase().contains(searchTerm);
                boolean dc2NameMatches = app2.getName().toLowerCase().contains(searchTerm);

                if (dc11NameMatches && !dc2NameMatches) {
                    // Only app1's name contains the search term, so order it before app2
                    return -1;
                }
                if (!dc11NameMatches && dc2NameMatches) {
                    // Only app2's name contains the search term, so order it before app1
                    return 1;
                }
            }

            // If both or neither app contains the search term in the app name, order them according
            // to the sort called above (by App name, ascending)
            return super.compare(store, dc1, app2, property);
        }
    }

}
