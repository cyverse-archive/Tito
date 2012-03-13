package org.iplantc.core.tito.client;

import org.iplantc.core.metadata.client.property.Property;
import org.iplantc.core.metadata.client.property.groups.PropertyGroup;
import org.iplantc.core.metadata.client.property.groups.PropertyGroupContainer;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.junit.client.GWTTestCase;

public class GwtTestPropertyGroupContainer extends GWTTestCase {
    @Override
    public String getModuleName() {
        return "org.iplantc.core.tito"; //$NON-NLS-1$
    }

    private PropertyGroupContainer validateSize(String json, int count) {
        PropertyGroupContainer ret = new PropertyGroupContainer(json);

        assertEquals(count, ret.getNumGroups());

        return ret;
    }

    private void validateBaseClassInitialization(PropertyGroupContainer container, String name,
            String desc, String type, String templateId) {
        assertNotNull(container);

        String test = container.getName();
        assertTrue(test.equals(name));

        test = container.getDescription();
        assertTrue(test.equals(desc));

        test = container.getType();
        assertTrue(test.equals(type));

        test = container.getId();
        assertTrue(test.equals(templateId));
    }

    public void testPropertyGroupNullJson() {
        PropertyGroupContainer container = validateSize(null, 0);
        validateBaseClassInitialization(container, "", "", "", ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
    }

    public void testPropertyGroupEmptyJson() {
        PropertyGroupContainer container = validateSize("", 0); //$NON-NLS-1$
        validateBaseClassInitialization(container, "", "", "", ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
    }

    public void testPropertyGroupBaseInitialization() {
        String json = "{\"name\" : \"testname\", \"description\" : \"some description\", \"type\" : \"foo\", \"id\" : \"edsfsdef1212323dfd\"}"; //$NON-NLS-1$

        PropertyGroupContainer container = validateSize(json, 0);
        validateBaseClassInitialization(container, "testname", "some description", "foo", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                "edsfsdef1212323dfd"); //$NON-NLS-1$
    }

    public void testPropertyGroupOneGroup() {
        String json = "{\"groups\":[{\"properties\":[{\"name\":\"testproperty\", \"description\":\"Some description\", \"type\":\"integer\"}]}]}"; //$NON-NLS-1$
        PropertyGroupContainer container = validateSize(json, 1);
        validateBaseClassInitialization(container, "", "", "", ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
    }

    public void testPropertyGroupMultipleGroups() {
        StringBuffer json = new StringBuffer();
        json.append("{\"groups\":["); //$NON-NLS-1$
        json.append("{\"name\":\"group1\", \"properties\":[{\"name\":\"testproperty\", \"description\":\"Test one\", \"type\":\"integer\"}]},"); //$NON-NLS-1$
        json.append("{\"name\":\"group2\", \"properties\":[{\"name\":\"testproperty2\", \"description\":\"Test two\", \"type\":\"boolean\"}]},"); //$NON-NLS-1$
        json.append("{\"name\":\"group3\", \"properties\":[{\"name\":\"testproperty3\", \"description\":\"Test three\", \"type\":\"string\"}]}"); //$NON-NLS-1$
        json.append("]}"); //$NON-NLS-1$

        PropertyGroupContainer container = validateSize(json.toString(), 3);
        validateBaseClassInitialization(container, "", "", "", ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
    }

    public void testToJson() {
        PropertyGroupContainer container1 = new PropertyGroupContainer("{}"); //$NON-NLS-1$
        PropertyGroup group = new PropertyGroup();
        Property property = new Property(
                JSONParser
                        .parseStrict(
                                "{\"id\":\"blah-test-1.304116363485E12\",\"name\":\"\",\"label\":\"my widget\",\"description\":\"\",\"type\":\"\",\"isVisible\":true,\"value\":\"\",\"order\":0}") //$NON-NLS-1$
                        .isObject());
        group.add(property);
        container1.add(group);

        JSONObject json1 = container1.toJson();
        PropertyGroupContainer container2 = new PropertyGroupContainer(json1.toString());
        JSONObject json2 = container2.toJson();
        UnitTestUtil.equalsIgnoreOrder(json1, json2);
    }
}
