import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import com.oreilly.servlet.MultipartRequest;
import javax.servlet.*;
import java.sql.*;

public class MyUploadServlet extends HttpServlet {

    // Initialize Table
    public void initTable(){
        try {
			try {
				Class.forName("oracle.jdbc.OracleDriver");
			} catch (Exception ex) { }
				Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "system", "oracle1");
				Statement stmt = con.createStatement();
				stmt.executeUpdate("CREATE TABLE USERS (FirstName char(20))");
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
    public void insertUser(String input){
        String insert = "INSERT INTO USERS VALUES (\'"+input+"\')";
        try {
			try {
				Class.forName("oracle.jdbc.OracleDriver");
			} catch (Exception ex) { }
				Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "system", "oracle1");
				Statement stmt = con.createStatement();
                System.out.println(insert);

                ResultSet rs = stmt.executeQuery("select COUNT(*) AS TOTAL from USERS where FirstName =\'"+input+"\'");
                rs.next();
                int numRows = rs.getInt("TOTAL");

                if (numRows == 0){
                    System.out.println("Execute INSERT \'"+input+"\'");
                    stmt.executeUpdate(insert);
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
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        MultipartRequest m = new MultipartRequest(request,"c:/tomcat/webapps/Client-Server-A1/images");
        out.println("successfully uploaded");
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();  

        // Create SQL Table
        initTable();
        
        // Check for session associated with this request, create one if necessary.
        HttpSession session = request.getSession();

        // Check the Mapped Parameters of this request.  
        if (request.getParameterMap().containsKey("UserFirstName")){

            // Sets the specified attribute to given value of the second parameter.
            session.setAttribute("UserFirstName", request.getParameter("UserFirstName"));
        }

        // Reading and storing from session for identification
        String UserFirstName = (String)session.getAttribute("UserFirstName");  

        // Checks session
        if (UserFirstName == null){
            // "Not Logged in"
            System.out.println("Not logged in");
        } else {
            // "Logged in"
            insertUser(UserFirstName);
            System.out.println(UserFirstName);
            out.println("<h3>Welcome, " + UserFirstName + "</h3>");

            //response.sendRedirect(request.getContextPath() + "/index2.html");
        }

    }

}