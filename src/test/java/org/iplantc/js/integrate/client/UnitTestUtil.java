package org.iplantc.js.integrate.client;

import org.junit.Test;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;

public class UnitTestUtil {

    /** This method only exists to make Maven/JUnit happy */
    @Test
    public void testNothing() {
    }

    public static boolean equalsIgnoreOrder(JSONValue json1, JSONValue json2) {
        if (json1 == null || json2 == null) {
            System.out
                    .println("JSON values differ: value1=\"" + json1 + "\" + value2=\"" + json2 + "\""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            return false;
        }

        JSONObject obj1 = json1.isObject();
        JSONObject obj2 = json2.isObject();
        if (obj1 != null && obj2 != null) {
            return compareOneWay(obj1, obj2) && compareOneWay(obj2, obj1);
        }
        JSONArray arr1 = json1.isArray();
        JSONArray arr2 = json2.isArray();
        if (arr1 != null && arr2 != null) {
            return compareOneWay(arr1, arr2) && compareOneWay(arr2, arr1);
        }

        // compare simple JSON values
        if (json1.equals(json2)) {
            return true;
        } else {
            System.out
                    .println("JSON values differ: value1=\"" + json1 + "\" + value2=\"" + json2 + "\""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            return false;
        }
    }

    /**
     * Tests recursively if all elements in json1 are in json2 as well.
     * 
     * @param json1
     * @param json2
     * @return
     */
    private static boolean compareOneWay(JSONObject json1, JSONObject json2) {
        for (String key : json1.keySet()) {
            if (!json2.containsKey(key)) {
                System.out.println("JSON key \"" + key + "\"only exists in one object"); //$NON-NLS-1$ //$NON-NLS-2$
                return false;
            } else {
                JSONValue value1 = json1.get(key);
                JSONValue value2 = json2.get(key);
                return equalsIgnoreOrder(value1, value2);
            }
        }
        return true;
    }

    /** tests whether two non-null JSON arrays are equal */
    private static boolean compareOneWay(JSONArray arr1, JSONArray arr2) {
        if (arr1.size() == 0 && arr2.size() == 0) {
            return true;
        } else if (arr1.size() == 0 && arr2.size() == 0) {
            System.out.println("Array sizes differ: " + arr1.size() + " vs " + arr2.size()); //$NON-NLS-1$ //$NON-NLS-2$
            return false;
        } else {
            // we want an order-insensitive comparison, so JSONArray.equals() can't be used here
            for (int i = 0; i < arr1.size(); i++) {
                for (int j = 0; j < arr2.size(); j++) {
                    if (equalsIgnoreOrder(arr1.get(i), arr2.get(j))) {
                        break;
                    }
                }
                System.out.println("JSON array element only exists in one array: \"" + arr1.get(i) //$NON-NLS-1$
                        + "\""); //$NON-NLS-1$
            }
            return true;
        }
    }
}
