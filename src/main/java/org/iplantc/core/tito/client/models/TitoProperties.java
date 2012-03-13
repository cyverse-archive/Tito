package org.iplantc.core.tito.client.models;

import java.util.Map;

public class TitoProperties {

    private static TitoProperties instance;
    private static final String DE_BASE_URL = "org.iplantc.tito.deBaseUrl";
    private static final String CONTEXT_CLICK_ENABLED = "org.iplantc.tito.contextMenu.enabled";
    
    
    private String deUrl;
    /**
     * Context click option
     */
    private boolean contextClickEnabled;
    
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
        try {
            setContextClickEnabled(Boolean.parseBoolean(properties.get(CONTEXT_CLICK_ENABLED)));
        } catch (Exception e) {
            setContextClickEnabled(false);
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
    
    
}
