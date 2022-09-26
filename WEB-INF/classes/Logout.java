import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import com.oreilly.servlet.MultipartRequest;
import org.json.simple.JSONObject;  
import org.json.simple.JSONValue;  
import netscape.javascript.JSObject;
import javax.servlet.*;
import java.sql.*;
import java.util.HashMap;

public class Logout extends HttpServlet{
    
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Logout POST Request Recieved.");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        out.print("doPost");

        response.sendRedirect("/Client-Server-A1");

        HttpSession session = request.getSession(false);
        System.out.println("Invalidating Session... Logging out" + session.getAttribute("UserFirstName"));

        if (session != null){

            session.invalidate(); ///  look at this line and why its not invalidating
        }

    }     

}
