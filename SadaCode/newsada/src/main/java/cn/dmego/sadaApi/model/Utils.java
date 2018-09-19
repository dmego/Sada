package cn.dmego.sadaApi.model;

import java.lang.reflect.Field;
import java.util.Properties;

import org.hyperledger.fabric.sdk.helper.Config;

public class Utils {

    private Utils() {
    }

    /**
     * Sets the value of a field on an object
     *
     * @param o The object that contains the field
     * @param fieldName The name of the field
     * @param value The new value
     * @return The previous value of the field
     */
    public static Object setField(Object o, String fieldName, Object value) {
        Object oldVal = null;
        try {
            final Field field = o.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            oldVal = field.get(o);
            field.set(o, value);
        } catch (Exception e) {
            throw new RuntimeException("Cannot get value of field " + fieldName, e);
        }
        return oldVal;
    }

    /**
     * Sets a Config property value
     *
     * The Config instance is initialized once on startup which means that
     * its properties don't change throughout its lifetime.
  
     *
     * @param key The key of the property (eg Config.LOGGERLEVEL)
     * @param value The new value
     * @return The previous value
     */
    public static String setConfigProperty(String key, String value) throws Exception {

        String oldVal = null;

        try {
            Config config = Config.getConfig();

            final Field sdkPropertiesInstance = config.getClass().getDeclaredField("sdkProperties");
            sdkPropertiesInstance.setAccessible(true);

            final Properties sdkProperties = (Properties) sdkPropertiesInstance.get(config);
            oldVal = sdkProperties.getProperty(key);
            sdkProperties.put(key, value);

        } catch (Exception e) {
            throw new RuntimeException("Failed to set Config property " + key, e);
        }

        return oldVal;
    }


}
