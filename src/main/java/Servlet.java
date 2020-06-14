import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.ArrayList;

// класс сервлета
public class Servlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        // поиск файла
        String filePath = httpServletRequest.getParameter("filePath");
        File file = new File(filePath);
        httpServletResponse.setContentType("text/plain");
        httpServletResponse.setHeader("Content-disposition", "attachment; filename=" + file.getName());
        if (file.exists())
        {
            try(FileInputStream inputStream = new FileInputStream(file);
                OutputStream outputStream = httpServletResponse.getOutputStream()){
                byte[] buffer = new byte[4096];
                int bytesRead = -1;

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }
            catch (Exception exception){
                System.out.println(exception.getMessage());
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        try{
            httpServletResponse.setContentType("text/html; charset=UTF-8");

            //извлечение значения из строки ввода
            PrintWriter printWriter = httpServletResponse.getWriter();
            String key = httpServletRequest.getParameter("word");
            System.out.println(httpServletRequest.getParameter("word"));

            // подключение к БД
            DatabaseHandler dbHandler = new DatabaseHandler();
            ArrayList<String> paths = dbHandler.getPaths(key);

            // ответ клиенту
            if (paths.size() != 0){
                for (String path:paths) {
                    String link = "<p><a href=\"servlet?filePath=" + path + "\">" + path +"</a>";
                    printWriter.println(link);
                }
            }
            else
                printWriter.println("Cлово \"" + key + "\" не навйдено");
        }
        catch (Exception exception){
            System.out.println(exception.getMessage());
        }
    }

}
