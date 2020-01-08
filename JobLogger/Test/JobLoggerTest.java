import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by Darwin Palma on 06/01/2020
 */

class JobLoggerTest {
    @Test
    public void testLogMessage(){
        Map<String,String> dbParamsMap = new HashMap<String,String>();
        dbParamsMap.put("userName","user01");
        dbParamsMap.put("password","testpassword");
        dbParamsMap.put("dbms","mysql");
        dbParamsMap.put("serverName","qa");
        dbParamsMap.put("portNumber","3306");
        dbParamsMap.put("logFileFolder","logFileFolder");

        boolean logToFileParam = true;
        boolean logToConsoleParam = true;
        boolean logToDatabaseParam = true;
        boolean logMessageParam = true;
        boolean logWarningParam = true;
        boolean logErrorParam = true;

        JobLogger logger = new JobLogger(logToFileParam, logToConsoleParam, logToDatabaseParam,
                logMessageParam, logWarningParam, logErrorParam, dbParamsMap);

        String messageText = "Register Log";
        boolean message = true;
        boolean warning = true;
        boolean error = true;
        try {
            JobLogger.LogMessage(messageText, message, warning, error);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFileLogger(){
        Map<String,String> dbParamsMap = new HashMap<String,String>();
        dbParamsMap.put("userName","user");
        dbParamsMap.put("password","password");
        dbParamsMap.put("dbms","mysql");
        dbParamsMap.put("serverName","serverName");
        dbParamsMap.put("portNumber","portNumber");
        dbParamsMap.put("logFileFolder","logFileFolder");

        boolean logToFileParam = true;
        boolean logToConsoleParam = true;
        boolean logToDatabaseParam = true;
        boolean logMessageParam = true;
        boolean logWarningParam = true;
        boolean logErrorParam = true;

        JobLogger logger = new JobLogger(logToFileParam, logToConsoleParam, logToDatabaseParam,
                logMessageParam, logWarningParam, logErrorParam, dbParamsMap);

        String messageText = "Register File Log";
        boolean message = true;
        boolean warning = true;
        boolean error = true;
        try {
            JobLogger.fileLogger(messageText, message, warning, error);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDBLogger(){
        Map<String,String> dbParamsMap = new HashMap<String,String>();
        dbParamsMap.put("userName","user");
        dbParamsMap.put("password","password");
        dbParamsMap.put("dbms","mysql");
        dbParamsMap.put("serverName","serverName");
        dbParamsMap.put("portNumber","portNumber");
        dbParamsMap.put("logFileFolder","logFileFolder");

        boolean logToFileParam = true;
        boolean logToConsoleParam = true;
        boolean logToDatabaseParam = true;
        boolean logMessageParam = true;
        boolean logWarningParam = true;
        boolean logErrorParam = true;

        JobLogger logger = new JobLogger(logToFileParam, logToConsoleParam, logToDatabaseParam,
                logMessageParam, logWarningParam, logErrorParam, dbParamsMap);

        String messageText = "Register Database Log";
        boolean message = true;
        boolean warning = true;
        boolean error = true;
        try {
            JobLogger.connectionDBLogger(message, warning, error);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}