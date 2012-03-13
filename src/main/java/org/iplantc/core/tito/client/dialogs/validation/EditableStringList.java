package org.iplantc.core.tito.client.dialogs.validation;

import java.util.ArrayList;
import java.util.List;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.metadata.client.validation.MetaDataRule;
import org.iplantc.core.tito.client.I18N;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Command;

public class EditableStringList extends BasicEditableList {
    protected static final String NEW_ITEM = "New item";

    public EditableStringList(JSONArray values, Command updatePropertyWithList) {
        super(values, updatePropertyWithList);
    }

    private class AddSelectionListener extends SelectionListener<ButtonEvent> {
        @Override
        public void componentSelected(ButtonEvent ce) {
            GridRow row = new GridRow(NEW_ITEM, NEW_NAME, NEW_VALUE, false);
            ListStore<GridRow> store = grid.getStore();

            grid.stopEditing();

            store.insert(row, store.getCount());

            grid.startEditing(store.indexOf(row), 1);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @seeorg.iplantc.js.integrate.client.dialogs.validation.BasicEditableList#
     * setAddSelectionListener()
     */
    @Override
    protected void setAddSelectionListener() {
        btnAdd.addSelectionListener(new AddSelectionListener());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.iplantc.js.integrate.client.widgets.validation.PropertyEditor#setValue(java .lang.Object)
     */
    @Override
    public void setValue(JSONArray arr) {
        if (arr != null && arr.size() > 0) {
            ListStore<GridRow> store = grid.getStore();
            store.removeAll();

            for (int i = 0; i < arr.size(); i++) {
                JSONObject obj = arr.get(i).isObject();

                String name = JsonUtil.getString(obj, MetaDataRule.NAME);
                String value = JsonUtil.getString(obj, MetaDataRule.VALUE);
                String item = JsonUtil.getString(obj, MetaDataRule.DISPLAY);
                boolean isDefault = JsonUtil.getBoolean(obj, MetaDataRule.IS_DEFAULT, false);

                store.add(new GridRow(item, name, value, isDefault));
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.iplantc.js.integrate.client.widgets.validation.PropertyEditor#getValue()
     */
    @Override
    public JSONArray getValue() {
        JSONArray arr = new JSONArray();
        ListStore<GridRow> store = grid.getStore();
        JSONObject obj;
        for (int i = 0,len = store.getCount(); i < len; i++) {
            GridRow row = store.getAt(i);

            obj = new JSONObject();
            obj.put(MetaDataRule.DISPLAY, new JSONString(row.getStringItem()));
            obj.put(MetaDataRule.NAME, new JSONString(row.getName()));
            obj.put(MetaDataRule.VALUE, new JSONString(row.getStringValue()));
            obj.put(MetaDataRule.IS_DEFAULT, JSONBoolean.getInstance(row.isDefault()));

            arr.set(i, obj);
        }

        return arr;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected List<ColumnConfig> buildColumns() {

        ColumnConfig item = new ColumnConfig(GridRow.STRING_ITEM, I18N.DISPLAY.display(), 200);
        item.setEditor(buildStringCellEditor());
        item.setMenuDisabled(true);
        item.setSortable(false);
        item.setAlignment(HorizontalAlignment.LEFT);

        ColumnConfig name = new ColumnConfig(GridRow.STRING_NAME, I18N.DISPLAY.parameter(), 200);
        name.setEditor(buildNameCellEditor());
        name.setMenuDisabled(true);
        name.setSortable(false);
        name.setAlignment(HorizontalAlignment.LEFT);

        ColumnConfig val = new ColumnConfig(GridRow.STRING_VALUE, I18N.DISPLAY.values(), 200);
        val.setEditor(buildValueCellEditor());
        val.setMenuDisabled(true);
        val.setSortable(false);
        val.setAlignment(HorizontalAlignment.LEFT);

        ColumnConfig defaultVal = new ColumnConfig(GridRow.NUMBER_ITEM, I18N.DISPLAY.defaultVal(), 60);
        defaultVal.setMenuDisabled(true);
        defaultVal.setSortable(false);
        defaultVal.setAlignment(HorizontalAlignment.LEFT);
        defaultVal.setRenderer(new DefaultValueCheckBoxCellRenderer());

        List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
        columns.add(item);
        columns.add(name);
        columns.add(val);
        columns.add(defaultVal);
        return columns;
    }
}
