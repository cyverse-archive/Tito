package org.iplantc.js.integrate.client.models;

import java.util.Map;

public class TitoProperties {

    private static TitoProperties instance;
    private static final String PREFIX = "org.iplantc.tito.";
    private static final String DE_BASE_URL = PREFIX + "deBaseUrl";
    private static final String CONTEXT_CLICK_ENABLED = PREFIX + "contextMenu.enabled";
    private static final String KEEPALIVE_PREFIX = PREFIX + "keepalive.";
    private static final String KEEPALIVE_TARGET = KEEPALIVE_PREFIX + "target";
    private static final String KEEPALIVE_INTERVAL = KEEPALIVE_PREFIX + "interval";

    /**
     * The base URL for the Discovery Environment.
     */
    private String deUrl;

    /**
     * Context click option
     */
    private boolean contextClickEnabled;

    /**
     * The URL to send CAS session keepalive requests to.
     */
    private String keepaliveTarget;

    /**
     * The number of minutes between CAS session keepalive requests.
     */
    private int keepaliveInterval;

    /**
     * @return the deUrl
     */
    public String getDeUrl() {
        return deUrl;
    }

    /**
     * @param deUrl the deUrl to set
     */
    public void setDeUrl(String deUrl) {
        this.deUrl = deUrl;
    }

    private TitoProperties() {
  
    }

    public static TitoProperties getInstance() {
        if(instance == null) {
            instance = new TitoProperties();
        }
        return instance;
    }

    /**
     * Initializes this class from the given set of properties.
     * 
     * @param properties the properties that were fetched from the server.
     */
    public void initialize(Map<String, String> properties) {
        deUrl = properties.get(DE_BASE_URL);
        setContextClickEnabled(getBooleanProperty(properties, CONTEXT_CLICK_ENABLED, false));
        keepaliveTarget = properties.get(KEEPALIVE_TARGET);
        keepaliveInterval = getIntProperty(properties, KEEPALIVE_INTERVAL, 60);
    }

    /**
     * Gets a property value as a Boolean value.
     * 
     * @param props the properties map.
     * @param propName the name of the property to retrieve.
     * @param defaultValue the default property value.
     * @return the property value or the default property value if the actual value can't be retrieved.
     */
    private boolean getBooleanProperty(Map<String, String> props, String propName, boolean defaultValue) {
        try {
            return Boolean.parseBoolean(props.get(propName));
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * Gets a property value as an integer.
     * 
     * @param props the properties map.
     * @param propName the name of the property to retrieve.
     * @param defaultValue the default property value.
     * @return the property value or the default value if the actual value can't be retrieved.
     */
    private int getIntProperty(Map<String, String> props, String propName, int defaultValue) {
        try {
            return Integer.parseInt(props.get(propName));
        }
        catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * @param contextClickEnabled the contextClickEnabled to set
     */
    public void setContextClickEnabled(boolean contextClickEnabled) {
        this.contextClickEnabled = contextClickEnabled;
    }

    /**
     * @return the contextClickEnabled
     */
    public boolean isContextClickEnabled() {
        return contextClickEnabled;
    }

    /**
     * @return the URL to send CAS session keepalive requests to.
     */
    public String getKeepaliveTarget() {
        return keepaliveTarget;
    }

    /**
     * @return the number of minutes between CAS session keepalive requests.
     */
    public int getKeepaliveInterval() {
        return keepaliveInterval;
    }
}
