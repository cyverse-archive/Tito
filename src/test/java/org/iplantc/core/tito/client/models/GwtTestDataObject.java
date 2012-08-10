package org.iplantc.core.tito.client.models;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.metadata.client.property.DataObject;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.junit.client.GWTTestCase;

public class GwtTestDataObject extends GWTTestCase {

    @Override
    public String getModuleName() {
        return "org.iplantc.core.tito"; //$NON-NLS-1$
    }

    public void testEmpty() {
        DataObject obj = new DataObject();
        JSONObject jsonObj = obj.toJson();
        assertNotNull(jsonObj);
        assertEquals("", JsonUtil.trim(jsonObj.get(DataObject.ID).toString())); //$NON-NLS-1$
        assertEquals("", JsonUtil.trim(jsonObj.get(DataObject.NAME).toString())); //$NON-NLS-1$
        assertEquals("One", JsonUtil.trim(jsonObj.get(DataObject.MULTIPLICITY).toString())); //$NON-NLS-1$
        assertEquals(-1, Integer.parseInt(jsonObj.get(DataObject.ORDER).toString()));
        assertEquals("", JsonUtil.trim(jsonObj.get(DataObject.CMD_SWITCH).toString())); //$NON-NLS-1$
        assertEquals("", JsonUtil.trim(jsonObj.get(DataObject.OUTPUT_FILENAME).toString())); //$NON-NLS-1$
        assertEquals(false, jsonObj.get(DataObject.REQUIRED).isBoolean().booleanValue());
        JSONArray arr = jsonObj.get(DataObject.FORMAT_ID).isArray();
        assertEquals(0, arr.size());
    }

    public void testToJson() {
        JSONObject jsonData = new JSONObject();
        jsonData.put(DataObject.ID, new JSONString("345")); //$NON-NLS-1$
        jsonData.put(DataObject.NAME, new JSONString("test")); //$NON-NLS-1$
        jsonData.put(DataObject.MULTIPLICITY, new JSONString("Many")); //$NON-NLS-1$
        jsonData.put(DataObject.ORDER, new JSONNumber(1));
        jsonData.put(DataObject.CMD_SWITCH, new JSONString("-r")); //$NON-NLS-1$
        jsonData.put(DataObject.OUTPUT_FILENAME, new JSONString("blah.txt")); //$NON-NLS-1$
        jsonData.put(DataObject.REQUIRED, JSONBoolean.getInstance(true));

        JSONArray formatIds = new JSONArray();
        formatIds.set(0, new JSONNumber(1));
        formatIds.set(1, new JSONNumber(2));
        jsonData.put(DataObject.FORMAT_ID, formatIds);

        JSONArray formats = new JSONArray();
        formats.set(0, new JSONString("NEXUS")); //$NON-NLS-1$
        formats.set(1, new JSONString("NEWICK")); //$NON-NLS-1$
        jsonData.put(DataObject.FORMAT, formats);

        DataObject obj = new DataObject(jsonData);
        JSONObject jsonObj = obj.toJson();
        assertNotNull(jsonObj);

        assertEquals("345", JsonUtil.trim(jsonObj.get(DataObject.ID).toString())); //$NON-NLS-1$
        assertEquals("test", JsonUtil.trim(jsonObj.get(DataObject.NAME).toString())); //$NON-NLS-1$
        assertEquals("Many", JsonUtil.trim(jsonObj.get(DataObject.MULTIPLICITY).toString())); //$NON-NLS-1$
        assertEquals(1, Integer.parseInt(jsonObj.get(DataObject.ORDER).toString()));
        assertEquals("-r", JsonUtil.trim(jsonObj.get(DataObject.CMD_SWITCH).toString())); //$NON-NLS-1$
        assertEquals("blah.txt", JsonUtil.trim(jsonObj.get(DataObject.OUTPUT_FILENAME).toString())); //$NON-NLS-1$
        JSONArray arr = jsonObj.get(DataObject.FORMAT_ID).isArray();
        assertEquals(1, Integer.parseInt(arr.get(0).isString().stringValue()));
        assertEquals(2, Integer.parseInt(arr.get(1).isString().stringValue()));
        JSONArray arr1 = jsonObj.get(DataObject.FORMAT).isArray();
        assertEquals(arr1.get(0).isString().stringValue(), "NEXUS"); //$NON-NLS-1$
        assertEquals(arr1.get(1).isString().stringValue(), "NEWICK"); //$NON-NLS-1$
        assertEquals(true, jsonObj.get(DataObject.REQUIRED).isBoolean().booleanValue());
    }

}
