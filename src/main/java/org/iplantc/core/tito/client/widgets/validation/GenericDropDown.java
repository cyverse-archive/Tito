package org.iplantc.core.tito.client.widgets.validation;

import java.util.List;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.StoreFilter;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.google.gwt.json.client.JSONValue;

public abstract class GenericDropDown<T extends ModelData, V extends JSONValue> extends
        AbstractPropertyEditor<V> {
    protected ComboBox<T> combo;
    protected String displayField;

    public GenericDropDown(List<T> values, String displayField) {
        initCombo();
        ListStore<T> store = new ListStore<T>();
        store.add(values);
        combo.setStore(store);
        this.displayField = displayField;
        combo.setDisplayField(this.displayField);
    }

    private void initCombo() {
        combo = new ComboBox<T>() {
            // fix for filters being cleared when a force selection is used.
            @Override
            public T getValue() {
                List<StoreFilter<T>> filters = store.getFilters();
                T obj = super.getValue();
                if (filters != null) {
                    for (StoreFilter<T> storeFilter : filters) {
                        store.addFilter(storeFilter);
                    }
                    store.applyFilters(null);
                }
                return obj;
            }
        };
        combo.setTriggerAction(TriggerAction.ALL); // Always show all values
        combo.setEditable(false);
    }

    @Override
    public void setValue(V value) {
        for (T t : combo.getStore().getModels()) {
            if (equals(t, value)) {
                combo.setValue(t);
            }
        }

    }

    protected abstract boolean equals(T element, V value);

    @Override
    public abstract V getValue();

    @Override
    public Component getEditorComponent() {
        return combo;
    }

    @Override
    public boolean validate() {
        return combo.getSelectionLength() > 0;
    }

    public void addToStore(List<T> values) {
        combo.getStore().add(values);
    }

    public List<T> getValues() {
        return combo.getStore().getModels();
    }

    /** Overridden to listen to Select events fired by ComboBox */
    @Override
    public void addChangeListener(PropertyChangeListener<V> listener) {
        addChangeListener(Events.Select, listener);
    }
}
