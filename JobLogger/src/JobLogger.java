import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JobLogger {
    private static boolean _logToFile, _logToConsole,_logMessage,_logWarning,_logError,_logToDatabase;
    private static Map _dbParams;
    private static Logger _logger;

    public JobLogger(boolean logToFileParam, boolean logToConsoleParam, boolean logToDatabaseParam,
                     boolean logMessageParam, boolean logWarningParam, boolean logErrorParam, Map dbParamsMap) {
        _logger = Logger.getLogger("MyLog");
        _logError = logErrorParam;
        _logMessage = logMessageParam;
        _logWarning = logWarningParam;
        _logToDatabase = logToDatabaseParam;
        _logToFile = logToFileParam;
        _logToConsole = logToConsoleParam;
        _dbParams = dbParamsMap;
    }

    public static void LogMessage(String messageText, boolean message, boolean warning, boolean error) throws Exception {
        messageText.trim();
        if (messageText == null || messageText.length() == 0) return;

        if (!_logToConsole && !_logToFile && !_logToDatabase) throw new Exception("Invalid configuration");

        if ((!_logError && !_logMessage && !_logWarning)
                || (!message && !warning && !error))
            throw new Exception("Error or Warning or Message must be specified");

        connectionDBLogger(message,warning,error);
        fileLogger(messageText,message,warning,error);
    }

    public static void fileLogger(String messageText, boolean message, boolean warning, boolean error) throws Exception {
        String l = "", fileName ="/logFile.txt";
        File logFile = new File(_dbParams.get("logFileFolder") + fileName);
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                throw new Exception("Error creating new file: "+e);
            }
        }

        FileHandler fh = null;
        try {
            fh = new FileHandler(_dbParams.get("logFileFolder") + fileName);
        } catch (IOException e) {
            throw new Exception("Error handling the file folder: "+e);
        }
        ConsoleHandler ch = new ConsoleHandler();

        if (error && _logError)
            l = l + "error " + DateFormat.getDateInstance(DateFormat.LONG).format(new Date()) + messageText;

        if (warning && _logWarning)
            l = l + "warning " + DateFormat.getDateInstance(DateFormat.LONG).format(new Date()) + messageText;

        if (message && _logMessage)
            l = l + "message " + DateFormat.getDateInstance(DateFormat.LONG).format(new Date()) + messageText;

        if(_logToFile) {
            _logger.addHandler(fh);
            _logger.log(Level.INFO, messageText);
        }

        if(_logToConsole) {
            _logger.addHandler(ch);
            _logger.log(Level.INFO, messageText);
        }
    }

    public static void connectionDBLogger(boolean message, boolean warning, boolean error) throws Exception {
        Properties connectionProps = new Properties();
        connectionProps.put("user", _dbParams.get("userName"));
        connectionProps.put("password", _dbParams.get("password"));
        int t;
        Statement stmt = null;
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:" + _dbParams.get("dbms")
                + "://" + _dbParams.get("serverName")
                + ":" + _dbParams.get("portNumber") + "/", connectionProps);

            t = 0;
            if (message && _logMessage) t = 1;

            if (error && _logError) t = 2;

            if (warning && _logWarning) t = 3;

            stmt = connection.createStatement();
            if(_logToDatabase) stmt.executeUpdate("insert into Log_Values('" + message + "', " + String.valueOf(t) + ")");
        } catch (SQLException sqle){
            sqle.printStackTrace();
            throw new Exception("SQLState: "
                    + sqle.getSQLState()
                    +" SQLErrorCode: "
                    + sqle.getErrorCode());
        } catch (Exception e){
            throw new Exception("Error in database execution connection: "+e);
        } finally {
            if (connection != null) {
                try{
                    stmt.close();
                    connection.close();
                } catch(Exception e){
                    throw new Exception("Error in database close connection: "+e);
                }
            }
        }
    }
}