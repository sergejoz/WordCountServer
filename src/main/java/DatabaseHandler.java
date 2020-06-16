import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseHandler extends Configs {
    Connection dbConnection;

    public Connection getDbConnection()
            throws ClassCastException, SQLException {
        dbConnection = DriverManager.getConnection("jdbc:mysql://"+ dbHost +"/" + dbName, dbUser, dbPass);
        return dbConnection;
    }

    // получение из БД всех директорий и freq
    public HashMap<String, Double> getPaths(String currentWord){
        HashMap<String, Double> ResultMap = new HashMap<String,Double>();
        try{
            String query = "select DISTINCT file_path, wordfreq from FileNames\n" +
            "inner join FileWords on FileWords.file_id = FileNames.file_id\n" +
            "inner join WordsFrequency on WordsFrequency.word_id = FileWords.word_id\n" +
            "where word = ?\n" +
                    "order by word_freq desc";
            PreparedStatement prSt = getDbConnection().prepareStatement(query);
            prSt.setString(1, currentWord);
            ResultSet resultSet = prSt.executeQuery();
            prSt.close();
            while(resultSet.next()){
                ResultMap.put(resultSet.getString("file_path"), Double.parseDouble(resultSet.getString("wordfreq")));
            }
        }
        catch (SQLException exception)
        {
            System.out.println(exception.getMessage());
        }
        return ResultMap;
    }
}
