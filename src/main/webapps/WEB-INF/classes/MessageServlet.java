import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;
import java.util.HashMap;

import org.json.simple.JSONObject;

/* To compile:
cd C:\tomcat\webapps\Client-Server-A1\WEB-INF\classes
javac  -classpath c:\tomcat\lib\servlet-api.jar;c:\tomcat\lib\cos.jar;c:\tomcat\lib\ojdbc8.jar;c:\tomcat\lib\json-simple-1.1.1.jar MessageServlet.java 
*/

public class MessageServlet extends HttpServlet {

    public void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null) {
//            response.setStatus(302);
//            response.sendRedirect("login");
            response.sendRedirect("/Client-Server-A1/");
        }
        else {
            // Redirects to messaging html page
            response.sendRedirect("/Client-Server-A1/websocket/messaging.html");

        }

    }

    // When fetch request from messaging.js is called
    public void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.setStatus(302);
            response.sendRedirect("login");
        }

        // Get current user's name from the session
        String sessionUserID = session.getAttribute("USER_ID").toString();

        // Response sent in JSON format
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Writes output to the response
        PrintWriter out = response.getWriter();

        // Stores the response as a key value pair
        HashMap<String, String> responseOutput = new HashMap<String, String>();

        // Store session user as a key value pair that gets returned to the JavaScript client side
        responseOutput.put("sessionUser", sessionUserID);

        // Store response as a JSON object
        JSONObject responseFile = new JSONObject(responseOutput);

        // Write the JSON object to the response
        out.println(responseFile.toString()); 

        // Send response to messaging.js JavaScript file
        request.getRequestDispatcher("messaging.js");
    }
}