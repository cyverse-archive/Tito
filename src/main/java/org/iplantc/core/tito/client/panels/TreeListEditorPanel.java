package org.iplantc.core.tito.client.panels;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.metadata.client.property.Property;
import org.iplantc.core.metadata.client.validation.ListRuleArgument;
import org.iplantc.core.metadata.client.validation.ListRuleArgumentFactory;
import org.iplantc.core.metadata.client.validation.ListRuleArgumentGroup;
import org.iplantc.core.tito.client.dialogs.validation.ListRuleArgumentEditor;
import org.iplantc.core.tito.client.widgets.validation.ListEditor;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.sencha.gxt.data.shared.event.StoreAddEvent;
import com.sencha.gxt.data.shared.event.StoreClearEvent;
import com.sencha.gxt.data.shared.event.StoreDataChangeEvent;
import com.sencha.gxt.data.shared.event.StoreFilterEvent;
import com.sencha.gxt.data.shared.event.StoreHandlers;
import com.sencha.gxt.data.shared.event.StoreRecordChangeEvent;
import com.sencha.gxt.data.shared.event.StoreRemoveEvent;
import com.sencha.gxt.data.shared.event.StoreSortEvent;
import com.sencha.gxt.data.shared.event.StoreUpdateEvent;
import com.sencha.gxt.widget.core.client.WidgetComponent;

/**
 * A ListEditorPanel for editing hierarchical (tree) selection lists.
 * 
 * @author psarando
 * 
 */
public class TreeListEditorPanel extends ListEditorPanel {
    private ListRuleArgumentFactory factory;

    public TreeListEditorPanel(Property property) {
        super(property);
    }

    @Override
    protected void allocateList() {
        factory = GWT.create(ListRuleArgumentFactory.class);

        super.allocateList();
    }

    @Override
    protected ListEditor buildList(JSONArray values) {
        ListRuleArgumentGroup root = convertJsonToListRuleArgs(values);
        ListRuleArgumentEditor widget = new ListRuleArgumentEditor(root);

        return new ListRuleArgumentEditorWrapper(widget);
    }

    private ListRuleArgumentGroup convertJsonToListRuleArgs(JSONArray value) {
        if (value != null) {
            JSONObject jsonRoot = JsonUtil.getObjectAt(value, 0);

            if (jsonRoot != null) {
                return AutoBeanCodex.decode(factory, ListRuleArgumentGroup.class, jsonRoot.toString())
                        .as();
            }
        }

        return null;
    }

    private JSONArray convertListRuleArgsToJson(ListRuleArgumentGroup group) {
        JSONArray ret = new JSONArray();

        AutoBean<ListRuleArgumentGroup> bean = AutoBeanUtils.getAutoBean(group);
        ret.set(0, JSONParser.parseStrict(AutoBeanCodex.encode(bean).getPayload()));

        return ret;
    }

    private class ListRuleArgumentEditorWrapper extends WidgetComponent implements ListEditor {
        private final ListRuleArgumentEditor editor;

        public ListRuleArgumentEditorWrapper(ListRuleArgumentEditor widget) {
            super(widget);

            this.editor = widget;

            addUpdateCommand();
        }

        /**
         * These store handlers need to be added after the initial values are added to the editor.
         */
        private void addUpdateCommand() {
            final UpdatePropertyWithList updateCmd = new UpdatePropertyWithList();

            editor.addStoreHandlers(new StoreHandlers<ListRuleArgument>() {
                @Override
                public void onAdd(StoreAddEvent<ListRuleArgument> event) {
                    updateCmd.execute();
                }

                @Override
                public void onRemove(StoreRemoveEvent<ListRuleArgument> event) {
                    updateCmd.execute();
                }

                @Override
                public void onFilter(StoreFilterEvent<ListRuleArgument> event) {
                    updateCmd.execute();
                }

                @Override
                public void onClear(StoreClearEvent<ListRuleArgument> event) {
                    updateCmd.execute();
                }

                @Override
                public void onUpdate(StoreUpdateEvent<ListRuleArgument> event) {
                    updateCmd.execute();
                }

                @Override
                public void onDataChange(StoreDataChangeEvent<ListRuleArgument> event) {
                    updateCmd.execute();
                }

                @Override
                public void onRecordChange(StoreRecordChangeEvent<ListRuleArgument> event) {
                    updateCmd.execute();
                }

                @Override
                public void onSort(StoreSortEvent<ListRuleArgument> event) {
                    updateCmd.execute();
                }
            });
        }

        @Override
        public void setValue(JSONArray value) {
            editor.setValues(convertJsonToListRuleArgs(value));
        }

        @Override
        public JSONArray getValue() {
            return convertListRuleArgsToJson(editor.getValues());
        }
    }
}
