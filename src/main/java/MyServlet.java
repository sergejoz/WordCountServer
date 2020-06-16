import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

@WebServlet(name = "MyServlet")
public class MyServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        try {
            request.setCharacterEncoding("UTF-8");
            String word = request.getParameter("word");
            DatabaseHandler dbHandler = new DatabaseHandler();
            HashMap<String, Double> ResultMap = dbHandler.getPaths(word);
            if (ResultMap.size() != 0)
                ResultMap.forEach((k,v) -> {
                    try {
                        // при использовании прямых путей выдавало ошибку недопустимых символов в URL, поэтому добавил URLEncode
                        out.println("<a href=find?path=" + URLEncoder.encode(k, "UTF-8") + ">" + k + ": " + v + " раз(а)" + "</a><br>");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                });
            else
                out.println("Файлы с таким словом не найдены!");
        } catch (Exception exception) {
            out.println("Произошла ошибка: \n" + exception);
        }
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getParameter("path");
        File file = new File(path);

        /* сохраняет файл на диск, но нет анимации скачивания в браузере
            String fileText =  Files.readAllLines(Paths.get(path)).toString();
            String savePath = "E:\\" + file.getName();
            Files.write(Paths.get(savePath), fileText.getBytes());
*/

        String fileText = Files.readAllLines(Paths.get(path)).toString();
        response.setContentType("text/plain");
        response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());
        try {
            OutputStream outputStream = response.getOutputStream();
            String outputResult = fileText;
            outputStream.write(outputResult.getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

