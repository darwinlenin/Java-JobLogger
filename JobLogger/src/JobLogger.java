import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
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

        Properties connectionProps = new Properties();
        connectionProps.put("user", _dbParams.get("userName"));
        connectionProps.put("password", _dbParams.get("password"));

        int t;
        Statement stmt;
        try (Connection connection = DriverManager.getConnection("jdbc:" + _dbParams.get("dbms")
                + "://" + _dbParams.get("serverName")
                + ":" + _dbParams.get("portNumber") + "/", connectionProps)) {

            t = 0;
            if (message && _logMessage) {
                t = 1;
            }

            if (error && _logError) {
                t = 2;
            }

            if (warning && _logWarning) {
                t = 3;
            }

            stmt = connection.createStatement();
            if(_logToDatabase) stmt.executeUpdate("insert into Log_Values('" + message + "', " + String.valueOf(t) + ")");
        }

        String l = "", fileName ="/logFile.txt";
        File logFile = new File(_dbParams.get("logFileFolder") + fileName);
        if (!logFile.exists()) logFile.createNewFile();

        FileHandler fh = new FileHandler(_dbParams.get("logFileFolder") + fileName);
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
}