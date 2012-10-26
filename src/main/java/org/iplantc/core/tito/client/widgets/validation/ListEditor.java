package org.iplantc.core.tito.client.widgets.validation;

import com.google.gwt.json.client.JSONArray;

public interface ListEditor {

    void setValue(JSONArray value);

    JSONArray getValue();
}
