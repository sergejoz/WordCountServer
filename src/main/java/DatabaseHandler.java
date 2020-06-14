import java.sql.*;
import java.util.ArrayList;

public class DatabaseHandler extends Configs {
    Connection dbConnection;

    public Connection getDbConnection()
            throws ClassCastException, SQLException {
        //  String connectionString = "jdbc:mysql://" + dbHost + ":"
        //          + dbPort + "/" + dbName + "?" + "useUnicode=true&useSSL=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";

        //  dbConnection = DriverManager.getConnection(connectionString, dbUser, dbPass);

        dbConnection = DriverManager.getConnection("jdbc:mysql://"+ dbHost +"/" + dbName, dbUser, dbPass);

        return dbConnection;
    }

    // получение из БД всех директорий
    public ArrayList<String> getPaths(String currentword){
        ArrayList<String> paths = new ArrayList<String>();

        try{
            String query = "select filePath, frequency from FileNames, \n" +
                    "(select fileId, frequency from FileWords, WordFreq\n" +
                    "where FileWords.wordFreqId = WordFreq.wordId and WordFreq.wordKey = ?) as Table1\n" +
                    "where FileNames.fileId = Table1.fileId\n" +
                    "order by frequency desc";
            PreparedStatement prSt = getDbConnection().prepareStatement(query);
            prSt.setString(1, currentword);
            ResultSet resultSet = prSt.executeQuery();
            prSt.close();

            while(resultSet.next()){
                String filePath = resultSet.getString("filePath");
                paths.add(filePath);
            }
        }
        catch (SQLException exception)
        {
            System.out.println(exception.getMessage());
        }

        return paths;
    }

    /*
    public void addFileName(String filename) {
        String insert = "INSERT INTO FileNames (Names) VALUES ('" + filename + "')";
        connectDB(insert);
    }

    public void addWordFreq(String word, String freq) {
        String insert = "INSERT INTO WordsFrequency (word, frequency) VALUES ('" + word + "','" + freq + "')";
        connectDB(insert);
    }

    private void connectDB(String command) {
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(command);
            prSt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    */
}
