/**
 * 
 */
package org.iplantc.js.integrate.client.dialogs.validation;

import java.util.ArrayList;
import java.util.List;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.metadata.client.validation.MetaDataRule;
import org.iplantc.js.integrate.client.I18N;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Command;

/**
 * @author sriram
 * 
 */
public class EditableNumberList extends BasicEditableList {
    protected static final Number NEW_ITEM = 0;

    /**
     * @param values
     * @param updatePropertyWithList
     */
    public EditableNumberList(JSONArray values, Command updatePropertyWithList) {
        super(values, updatePropertyWithList);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.iplantc.js.integrate.client.widgets.validation.PropertyEditor#setValue(java .lang.Object)
     */
    @Override
    public void setValue(JSONArray arr) {
        ListStore<GridRow> store = grid.getStore();

        store.removeAll();

        for (int i = 0; i < arr.size(); i++) {
            JSONObject obj = arr.get(i).isObject();

            Number display;
            String name = JsonUtil.getString(obj, MetaDataRule.NAME);
            String value = JsonUtil.getString(obj, MetaDataRule.VALUE);
            JSONNumber jsonNumber = obj.get(MetaDataRule.DISPLAY).isNumber();
            boolean isDefault = JsonUtil.getBoolean(obj, MetaDataRule.IS_DEFAULT, false);

            try {
                // initialize the GridRow with an int if the original value is an int
                display = Integer.parseInt(jsonNumber.toString());
            } catch (Exception e) {
                // The string didn't parse as an int, so set value as a double instead.
                display = jsonNumber.doubleValue();
            }

            store.add(new GridRow(display, name, value, isDefault));
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
        for (int i = 0; i < store.getCount(); i++) {
            GridRow row = store.getAt(i);

            obj = new JSONObject();
            obj.put(MetaDataRule.DISPLAY, new JSONNumber(row.getNumberItem().doubleValue()));
            obj.put(MetaDataRule.NAME, new JSONString(row.getName()));
            obj.put(MetaDataRule.VALUE, new JSONString(row.getStringValue()));
            obj.put(MetaDataRule.IS_DEFAULT, JSONBoolean.getInstance(row.isDefault()));

            arr.set(i, obj);
        }

        return arr;
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

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<ColumnConfig> buildColumns() {
        ColumnConfig display = new ColumnConfig(GridRow.NUMBER_ITEM, I18N.DISPLAY.display(), 200);
        display.setEditor(buildNumberCellEditor());
        display.setMenuDisabled(true);
        display.setSortable(false);
        display.setAlignment(HorizontalAlignment.LEFT);

        ColumnConfig name = new ColumnConfig(GridRow.STRING_NAME, I18N.DISPLAY.parameter(), 200);
        name.setEditor(buildNameCellEditor());
        name.setMenuDisabled(true);
        name.setSortable(false);
        name.setAlignment(HorizontalAlignment.LEFT);

        ColumnConfig value = new ColumnConfig(GridRow.STRING_VALUE, I18N.DISPLAY.values(), 200);
        value.setEditor(buildValueCellEditor());
        value.setMenuDisabled(true);
        value.setSortable(false);
        value.setAlignment(HorizontalAlignment.LEFT);

        ColumnConfig defaultVal = new ColumnConfig(GridRow.NUMBER_ITEM, I18N.DISPLAY.defaultVal(), 60);
        defaultVal.setMenuDisabled(true);
        defaultVal.setSortable(false);
        defaultVal.setAlignment(HorizontalAlignment.LEFT);
        defaultVal.setRenderer(new DefaultValueCheckBoxCellRenderer());

        List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
        configs.add(display);
        configs.add(name);
        configs.add(value);
        configs.add(defaultVal);

        return configs;
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
}
