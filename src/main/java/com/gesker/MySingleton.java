package com.gesker;

import java.io.Serial;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MySingleton implements Serializable {
    public static final String WILDFLY_PERSISTENCE_UNIT = "MyPersistenceUnit";
    public static final String WILDFLY_DATASOURCE = "java:jboss/datasources/PostgreSQLDS";
    private static final Logger LOGGER = Logger.getLogger(MySingleton.class.getName());
    @Serial
    private static final long serialVersionUID = 1L;
    private static MySingleton instance = null;


    protected MySingleton() {
        //Defeat Instantiation
        LOGGER.log(Level.INFO, "Load new MyConstant (Singleton) Pojo Object");
    }

    public static MySingleton getInstance() {
        if (instance == null) {
            synchronized (MySingleton.class) {
                if (instance == null) {
                    instance = new MySingleton();
                }
            }
        }
        return instance;
    }

    public String getUnixTimeFormattedRfc1123(long unixTime) {
        Date time = new Date(TimeUnit.MILLISECONDS.convert(unixTime, TimeUnit.SECONDS));
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat.format(time.getTime());
    }


}
