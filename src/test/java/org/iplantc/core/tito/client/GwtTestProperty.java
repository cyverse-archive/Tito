package org.iplantc.core.tito.client;

import java.util.List;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.metadata.client.property.Property;
import org.iplantc.core.metadata.client.validation.MetaDataRule;
import org.iplantc.core.metadata.client.validation.MetaDataValidator;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.junit.client.GWTTestCase;

public class GwtTestProperty extends GWTTestCase {
    @Override
    public String getModuleName() {
        return "org.iplantc.core.tito"; //$NON-NLS-1$
    }

    private Property allocateProperty(String json) {
        JSONObject objJson = JsonUtil.getObject(json);

        return new Property(objJson);
    }

    private void validateRuleBase(MetaDataRule rule, String testType, int numParams) {
        assertNotNull(rule);
        String type = rule.getType();
        assertTrue(testType.equals(type));

        assertEquals(rule.getNumParams(), numParams);
    }

    public void testPropertyNoValidator() {
        String json = "{\"name\":\"testproperty\", \"desc\":\"Some description\", \"propertytype\":\"string\", \"value\":\"test string\"}"; //$NON-NLS-1$
        Property property = allocateProperty(json);

        MetaDataValidator validator = property.getValidator();
        assertNull(validator);
    }

    public void testPropertyValidatorName() {
        String json = "{\"name\":\"testproperty\", \"desc\":\"Some description\", \"propertytype\":\"string\", \"value\":\"test string\", " //$NON-NLS-1$
                + "\"validator\" : {\"name\" : \"validator name\" }}"; //$NON-NLS-1$

        Property property = allocateProperty(json);

        MetaDataValidator validator = property.getValidator();
        assertNotNull(validator);

        String name = validator.getName();
        assertTrue(name.equals("validator name")); //$NON-NLS-1$

        assertFalse(validator.isRequired());
    }

    public void testPropertyValidatorOneRuleNoParam() {
        StringBuffer buf = new StringBuffer();

        buf.append("{\"name\":\"testproperty\", \"desc\":\"Some description\", \"propertytype\":\"string\", \"value\":\"test string\", "); //$NON-NLS-1$
        buf.append("\"validator\" : {\"name\" : \"validator name\", "); //$NON-NLS-1$
        buf.append("\"rules\" : [{\"rule1\" : []}]"); //$NON-NLS-1$
        buf.append("}}"); //$NON-NLS-1$

        Property property = allocateProperty(buf.toString());

        MetaDataValidator validator = property.getValidator();
        assertNotNull(validator);

        assertEquals(validator.getNumRules(), 1);

        List<MetaDataRule> rules = validator.getRules();
        assertNotNull(rules);

        MetaDataRule rule = rules.get(0);
        assertNotNull(rule);
        String name = rule.getType();
        assertTrue(name.equals("rule1")); //$NON-NLS-1$

        assertEquals(rule.getNumParams(), 0);
    }

    public void testPropertyValidatorOneRuleTwoParams() {
        StringBuffer buf = new StringBuffer();

        buf.append("{\"name\":\"testproperty\", \"desc\":\"Some description\", \"propertytype\":\"string\", \"value\":\"test string\", "); //$NON-NLS-1$
        buf.append("\"validator\" : {\"name\" : \"validator name\", "); //$NON-NLS-1$
        buf.append("\"rules\" : [{\"rule1\" : [1,100]}]"); //$NON-NLS-1$
        buf.append("}}"); //$NON-NLS-1$

        Property property = allocateProperty(buf.toString());

        MetaDataValidator validator = property.getValidator();
        assertNotNull(validator);

        assertEquals(validator.getNumRules(), 1);

        List<MetaDataRule> rules = validator.getRules();
        assertNotNull(rules);

        MetaDataRule rule = rules.get(0);
        validateRuleBase(rule, "rule1", 2); //$NON-NLS-1$

        List<JSONValue> params = rule.getParams();
        int paramTest = (int)params.get(0).isNumber().doubleValue();
        assertEquals(paramTest, 1);

        paramTest = (int)params.get(1).isNumber().doubleValue();
        assertEquals(paramTest, 100);
    }

    public void testPropertyValidatorTwoRules() {
        StringBuffer buf = new StringBuffer();

        buf.append("{\"name\":\"testproperty\", \"desc\":\"Some description\", \"propertytype\":\"string\", \"value\":\"test string\", "); //$NON-NLS-1$
        buf.append("\"validator\" : {\"name\" : \"validator name\", "); //$NON-NLS-1$
        buf.append("\"rules\" : [{\"rule1\" : [1,100]}, "); //$NON-NLS-1$
        buf.append("{\"rule2\" : [5,25,75]}]"); //$NON-NLS-1$
        buf.append("}}"); //$NON-NLS-1$

        Property property = allocateProperty(buf.toString());

        MetaDataValidator validator = property.getValidator();
        assertNotNull(validator);

        assertEquals(validator.getNumRules(), 2);

        List<MetaDataRule> rules = validator.getRules();
        assertNotNull(rules);

        MetaDataRule rule = rules.get(0);
        validateRuleBase(rule, "rule1", 2); //$NON-NLS-1$

        List<JSONValue> params = rule.getParams();
        int paramTest = (int)params.get(0).isNumber().doubleValue();
        assertEquals(paramTest, 1);

        paramTest = (int)params.get(1).isNumber().doubleValue();
        assertEquals(paramTest, 100);

        rule = rules.get(1);
        validateRuleBase(rule, "rule2", 3); //$NON-NLS-1$

        params = rule.getParams();
        paramTest = (int)params.get(0).isNumber().doubleValue();
        assertEquals(paramTest, 5);

        paramTest = (int)params.get(1).isNumber().doubleValue();
        assertEquals(paramTest, 25);

        paramTest = (int)params.get(2).isNumber().doubleValue();
        assertEquals(paramTest, 75);
    }

    public void testPropertyValidatorRequired() {
        String json = "{\"name\":\"testproperty\", \"desc\":\"Some description\", \"propertytype\":\"string\", \"value\":\"test string\", " //$NON-NLS-1$
                + "\"validator\" : {\"name\" : \"validator name\", \"required\" : true}}"; //$NON-NLS-1$
        Property property = allocateProperty(json);

        MetaDataValidator validator = property.getValidator();
        assertNotNull(validator);
        assertTrue(validator.isRequired());
    }

    public void testPropertyValidatorNotRequired() {
        String json = "{\"name\":\"testproperty\", \"desc\":\"Some description\", \"propertytype\":\"string\", \"value\":\"test string\", " //$NON-NLS-1$
                + "\"validator\" : {\"name\" : \"validator name\", \"required\" : false}}"; //$NON-NLS-1$
        Property property = allocateProperty(json);

        MetaDataValidator validator = property.getValidator();
        assertNotNull(validator);
        assertFalse(validator.isRequired());
    }

    public void testToJson() {
        JSONObject json = JSONParser
                .parseStrict(
                        "{\"id\":\"FastxBarcodeSplitter_sequencefile\",\"isVisible\":true,\"validator\":{\"id\":\"\",\"name\":\"\",\"label\":\"\",\"desc\":\"\",\"type\":\"\",\"isVisible\":true,\"required\":true,\"rules\":[]},\"name\":\" < \",\"label\":\"Select file:\",\"type\":\"FileInput\",\"desc\":\"\",\"value\":\"\"}") //$NON-NLS-1$
                .isObject();
        Property property = new Property(json);
        assertTrue(UnitTestUtil.equalsIgnoreOrder(json, property.toJson()));
    }

}
