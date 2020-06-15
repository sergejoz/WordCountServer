import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

@WebServlet(name = "MyServlet")
public class MyServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        try {
            request.setCharacterEncoding("UTF-8");
            String word = request.getParameter("word");
            DatabaseHandler dbHandler = new DatabaseHandler();
            ArrayList<String> pathsList = dbHandler.getPaths(word);
            if (pathsList.size() != 0)
                // возвращает линк на путь к файлу сразу с file:///
                pathsList.forEach(k -> out.println("<a href=" + k + "\">" + k + "</a><br>"));
            else
                out.println("Файлы с таким словом не найдены!");
        } catch (Exception exception) {
            out.println("Произошла ошибка: \n" + exception);
        }
    }
}

