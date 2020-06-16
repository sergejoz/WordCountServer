import java.sql.*;
import java.util.ArrayList;

public class DatabaseHandler extends Configs {
    Connection dbConnection;

    public Connection getDbConnection()
            throws ClassCastException, SQLException {
        dbConnection = DriverManager.getConnection("jdbc:mysql://"+ dbHost +"/" + dbName, dbUser, dbPass);
        return dbConnection;
    }

    // получение из БД всех директорий
    public ArrayList<String> getPaths(String currentWord){
        ArrayList<String> paths = new ArrayList<String>();
        try{
            String query = "select file_path from FileNames\n" +
            "inner join FileWords on FileWords.file_id = FileNames.file_id\n" +
            "inner join WordsFrequency on WordsFrequency.word_id = FileWords.word_id\n" +
            "where word = ?";
            PreparedStatement prSt = getDbConnection().prepareStatement(query);
            prSt.setString(1, currentWord);
            ResultSet resultSet = prSt.executeQuery();
            prSt.close();
            while(resultSet.next()){
                paths.add(resultSet.getString("file_path"));
            }
        }
        catch (SQLException exception)
        {
            System.out.println(exception.getMessage());
        }
        return paths;
    }
}
