import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import com.oreilly.servlet.MultipartRequest;
//import org.json.simple.JSONObject;
//import org.json.simple.JSONValue;

import netscape.javascript.JSObject;

import javax.servlet.*;
import java.sql.*;
import java.util.HashMap;

public class Index2Servlet extends HttpServlet {

//    HttpSession session;

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        System.out.println("SESSION VARIABLE: " + session);

        if(session != null) {
            response.sendRedirect("/Client-Server-A1/index2.html"); // redirect to Index2
        }
        else {
            response.sendRedirect("/Client-Server-A1/"); // redirect to login
        }
    }


    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {




    }

}