package org.iplantc.js.integrate.server;

import java.io.IOException;
import java.util.Properties;

/**
 * Provides access to configuration properties for the tool integration
 * 
 * @author Sriram
 */
public class TitoProperties {
    
    // The name of the properties file.
    public static final String PROPERTIES_FILE = "/tito.properties"; //$NON-NLS-1$

   
    // The prefix for all of the properties.
    public static final String PREFIX = "org.iplantc.tito"; //$NON-NLS-1$

    public static final String TITO_DEFAULT_BUILD_NUMBER = PREFIX + ".about.defaultBuildNumber"; //$NON-NLS-1$
    public static final String TITO_RELEASE_VERSION = PREFIX + ".about.releaseVersion"; //$NON-NLS-1$

  
    /**
     * The list of required properties.
     */
    private static final String[] REQUIRED_PROPERTIES = {};

    /**
     * The properties. Place any default values in the initializer.
     */
    private static Properties properties = new Properties();
       

    static {
        loadProperties();
        validateProperties(REQUIRED_PROPERTIES);
    }

    /**
     * Validates that we have values for all required properties.
     */
    private static void validateProperties(String[] propertyNames) {
        for (String propertyName : propertyNames) {
            String propertyValue = properties.getProperty(propertyName);
            if (propertyValue == null || propertyValue.equals("")) { //$NON-NLS-1$
                throw new ExceptionInInitializerError("missing required property: " + propertyName); //$NON-NLS-1$
            }
        }
    }

    /**
     * Loads the discovery environment properties. If an error occurs while loading the file, we log the
     * message, but do not throw an exception; the property validation will catch any required properties
     * that are missing.
     */
    private static void loadProperties() {
        try {
            properties.load(TitoProperties.class.getResourceAsStream(PROPERTIES_FILE));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the default build number.
     * 
     * When a build number is not available, this value will be provided.
     * 
     * @return a string representing the default build number.
     */
    public static String getDefaultBuildNumber() {
        return properties.getProperty(TITO_DEFAULT_BUILD_NUMBER);
    }

    /**
     * Gets the release version for the Discovery Environment.
     * 
     * This will be displayed in about text or provided as context.
     * 
     * @return a string representing the release version of the Discovery Environment.
     */
    public static String getReleaseVersion() {
        return properties.getProperty(TITO_RELEASE_VERSION);
    }

   }
