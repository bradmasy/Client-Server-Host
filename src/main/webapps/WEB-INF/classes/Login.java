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

public class Login extends HttpServlet {

    HttpSession session; //declaration

    // Initialize Table
    public void initTable(){
        try {
			try {
				Class.forName("oracle.jdbc.OracleDriver");
			} catch (Exception ex) { }
				Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "system", "oracle1");
				Statement stmt = con.createStatement();
				stmt.executeUpdate("CREATE TABLE USERS (FirstName char(20), PasswordValue char(20))");
				stmt.close();
				con.close();
		} 
		catch(SQLException ex) { 
			System.out.println("\n--- SQLException caught ---\n"); 
			while (ex != null) { 
				System.out.println("Message: " + ex.getMessage ()); 
				System.out.println("SQLState: " + ex.getSQLState ()); 
				System.out.println("ErrorCode: " + ex.getErrorCode ()); 
				ex = ex.getNextException(); 
				System.out.println("");
			} 
		} 
    }

    // Helper Function to insert Row into Table
    public void insertUser(String input, String passwordInput){
        String insert = "INSERT INTO USERS VALUES (\'"+input+"\',\'"+passwordInput+"\')";
        try {
			try {
				Class.forName("oracle.jdbc.OracleDriver");
			} catch (Exception ex) { }
				Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "system", "oracle1");
				Statement stmt = con.createStatement();
                System.out.println(insert);

                ResultSet rs = stmt.executeQuery("select COUNT(*) AS TOTAL from USERS where FirstName =\'"+input+"\' AND PasswordValue=\'"+passwordInput+"\'");
                rs.next();
                int numRows = rs.getInt("TOTAL");

                if (numRows == 0){
                    System.out.println("No such User:"+input+" in DB.");
                    System.out.println("Execute INSERT \'"+input+"\',\'"+passwordInput+"\'");
                    stmt.executeUpdate(insert);
                } else {
                    System.out.println("User already in DB.");
                }
                

				stmt.close();
				con.close();
		} 
		catch(SQLException ex) { 
			System.out.println("\n--- SQLException caught ---\n"); 
			while (ex != null) { 
				System.out.println("Message: " + ex.getMessage ()); 
				System.out.println("SQLState: " + ex.getSQLState ()); 
				System.out.println("ErrorCode: " + ex.getErrorCode ()); 
				ex = ex.getNextException(); 
				System.out.println("");
			} 
		} 
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Post Request Recieved.");

        // BufferedReader reads the incoming data that is retrieved from the body of the request as character data
        BufferedReader Reader = request.getReader();
        
        // Create Object to contain the parsed data from Reader.
        Object obj = JSONValue.parse(Reader);
        
        // Cast Object to JSONObject.
        JSONObject jsonObject = (JSONObject) obj;

        // Retrieve data from JSONObject
        String user = (String) jsonObject.get("UserFirstName");
        String password = (String) jsonObject.get("UserPassword");

        // Print 
        System.out.println(user);
        System.out.println(password);

        // Create SQL Table
        initTable();

        // Checks and Inserts User into DB.
        insertUser(user, password);

        // User is definitely in our DB now, create session for User.
        // Check for session associated with this request, create one if necessary.
        session = request.getSession();

        // Sets the specified attribute to given value of the second parameter.
        session.setAttribute("USER_ID", user);

        // Reading and storing from session for identification
        String UserFirstName = (String)session.getAttribute("USER_ID"); 

        // Checks session
        if (UserFirstName == null){
            // "Not Logged in"
            System.out.println("Not logged in");
            response.sendRedirect("/Client-Server-A1");
        } else {
            // "Logged in"
            System.out.println("Welcome, " + UserFirstName);

            // Create Session Cookie
            // Cookie sessionCookie = new Cookie("UserNameValue",user);
            // response.addCookie(sessionCookie);

            // Send Response
            if (session != null){
                session.setAttribute("USER_ID", user);
                response.sendRedirect(request.getContextPath() + "/main");                
            }
        }

        // response.setStatus(HttpServletResponse.SC_OK);
        // RequestDispatcher rd = request.getRequestDispatcher("/index2.html");
        // rd.forward(request, response);
        System.out.println(request.getContextPath()+ "/main");
        // response.sendRedirect(request.getContextPath() + "/index2.html");
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("GET Request Recieved.");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // need to retrieve the session data here

        // response.setHeader("UserNameValue", (String)session.getAttribute("UserFirstName"));

        PrintWriter out = response.getWriter();
        //out.write("UserFirstNameValue="+(String)session.getAttribute("UserFirstName"));

        HashMap<String,String> responseOutput = new HashMap<String,String>();
        responseOutput.put("UserFirstNameValue", (String)session.getAttribute("UserFirstName"));
        JSONObject jsonObj = new JSONObject(responseOutput);

        out.print(jsonObj);
    }

}
