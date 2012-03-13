package org.iplantc.core.tito.client;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.metadata.client.JSONMetaDataObject;
import org.iplantc.core.metadata.client.property.groups.PropertyGroup;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.junit.client.GWTTestCase;

public class GwtTestPropertyGroup extends GWTTestCase {
    @Override
    public String getModuleName() {
        return "org.iplantc.core.tito"; //$NON-NLS-1$
    }

    // it appears this method creates a PropertyGroup from JSON and checks that the group has count properties
    private PropertyGroup validateSize(String json, int count) {
        JSONObject objJson = JsonUtil.getObject(json);

        PropertyGroup ret = new PropertyGroup(objJson.toString());

        assertEquals(ret.getNumProperties(), count);

        return ret;
    }

    private void validateBaseClassInitialization(PropertyGroup group, String name, String desc,
            String type) {
        assertNotNull(group);

        String test = group.getName();
        assertTrue(test.equals(name));

        test = group.getDescription();
        assertTrue(test.equals(desc));

        test = group.getType();
        assertTrue(test.equals(type));
    }

    public void testPropertyGroupNullJson() {
        PropertyGroup group = new PropertyGroup((String)null);

        assertEquals(group.getNumProperties(), 0);
    }

    public void testPropertyGroupName() {
        String json = "{\"name\" : \"testname\"}"; //$NON-NLS-1$
        PropertyGroup grp = validateSize(json, 0);

        validateBaseClassInitialization(grp, "testname", "", ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }

    public void testPropertyGroupDesc() {
        String json = "{\"description\" : \"some description\"}"; //$NON-NLS-1$
        PropertyGroup grp = validateSize(json, 0);

        validateBaseClassInitialization(grp, "", "some description", ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }

    public void testPropertyGroupType() {
        String json = "{\"type\" : \"foo\"}"; //$NON-NLS-1$
        PropertyGroup grp = validateSize(json, 0);

        validateBaseClassInitialization(grp, "", "", "foo"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }

    public void testPropertyGroupBaseClassSet() {
        String json = "{\"name\" : \"testname\", \"description\" : \"some description\", \"type\" : \"foo\"}"; //$NON-NLS-1$
        PropertyGroup grp = validateSize(json, 0);

        validateBaseClassInitialization(grp, "testname", "some description", "foo"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }

    public void testPropertyGroupInvalidFetch() {
        String json = "{\"properties\":[{\"name\":\"testproperty\", \"description\":\"Some description\", \"propertytype\":\"boolean\"}]}"; //$NON-NLS-1$
        PropertyGroup grp = validateSize(json, 1);

        validateBaseClassInitialization(grp, "", "", ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        JSONMetaDataObject prop = grp.getElement("foo"); //$NON-NLS-1$
        assertNull(prop);
    }

    public void testPropertyGroupMultipleProperties() {
        StringBuffer buf = new StringBuffer();

        buf.append("{\"properties\":["); //$NON-NLS-1$
        buf.append("{\"name\":\"testproperty\", \"description\":\"Some description\", \"propertytype\":\"string\"}, "); //$NON-NLS-1$
        buf.append("{\"name\":\"testproperty2\", \"description\":\"Some description\", \"propertytype\":\"integer\"}, "); //$NON-NLS-1$
        buf.append("{\"name\":\"testproperty3\", \"description\":\"Some description\", \"propertytype\":\"boolean\"}"); //$NON-NLS-1$
        buf.append("]}"); //$NON-NLS-1$

        PropertyGroup grp = validateSize(buf.toString(), 3);

        validateBaseClassInitialization(grp, "", "", ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        JSONMetaDataObject prop = grp.getElement("testproperty"); //$NON-NLS-1$
        assertNotNull(prop);

        prop = grp.getElement("testproperty2"); //$NON-NLS-1$
        assertNotNull(prop);

        prop = grp.getElement("testproperty3"); //$NON-NLS-1$
        assertNotNull(prop);
    }

    public void testToJson() {
        String json = "{\"id\":\"idPanel0\",\"name\":\"Manage Barcodes\",\"label\":\"Manage Barcodes\",\"properties\":[{\"id\":\"FastxBarcodeSplitter_barcodeEntryOption\",\"isVisible\":true,\"name\":\"--bcfile\",\"label\":\"Enter barcode:\",\"type\":\"BarcodeSelector\"},{\"id\":\"FastxBarcodeSplitter_numberOfAllowedMismatches\",\"isVisible\":true,\"validator\":{\"id\":\"\",\"name\":\"Allowed mismatches field\",\"label\":\"\",\"required\":true,\"rules\":[{\"IntRange\":[1,100]}]},\"name\":\" --mismatches \",\"value\":\"1\",\"label\":\"Number of allowed mismatches:\",\"type\":\"Number\"}],\"type\":\"step\"}"; //$NON-NLS-1$
        PropertyGroup grp = new PropertyGroup(json);
        JSONObject expected = JSONParser.parseStrict(json).isObject();
        JSONObject actual = grp.toJson();
        assertTrue(UnitTestUtil.equalsIgnoreOrder(expected, actual));
    }
}
