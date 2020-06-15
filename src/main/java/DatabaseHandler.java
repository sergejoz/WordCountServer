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
                    "inner join FileWords on FileWords.file_id = FileNames.file_id and FileWords.word_id = (select word_id from WordsFrequency where word = '" + currentWord + "')";
            PreparedStatement prSt = getDbConnection().prepareStatement(query);
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
