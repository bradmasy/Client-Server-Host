import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.time.LocalDate;
import java.util.ArrayList;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.StringBuilder;
import java.sql.*;
import java.text.*;
import java.nio.*;
import java.sql.Date;   
import java.util.UUID;

@MultipartConfig
public class FileUploadServlet extends HttpServlet {
    private int photoIdCounter = 0;

    public static byte[] asBytes(UUID uuid) {
    		ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
    		bb.putLong(uuid.getMostSignificantBits());
    		bb.putLong(uuid.getLeastSignificantBits());
    		return bb.array();
  	}

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);
       boolean isLoggedIn = isLoggedIn(request);

        if (session == null) {
//           response.setStatus(302);
//           response.sendRedirect("login");
            response.sendRedirect("/Client-Server-A1/");

        }else {
           PrintWriter writer = response.getWriter();
                  writer.append("<!DOCTYPE html>\r\n")
                   .append("<html>\r\n")
                   .append("    <head>\r\n")
                   .append("        <title>File Upload Form</title>\r\n")
                   .append("    </head>\r\n")
                   .append("    <body>\r\n");
           writer.append("<h1>Upload file</h1>\r\n");
           writer.append("<form method=\"POST\" action=\"upload\" ")
                   .append("enctype=\"multipart/form-data\">\r\n");
           writer.append("<input type=\"file\" name=\"fileName\"/><br/><br/>\r\n");
           writer.append("Caption: <input type=\"text\" name=\"caption\"<br/><br/>\r\n");
           writer.append("<br />\n");
           writer.append("Date: <input type=\"date\" name=\"date\"<br/><br/>\r\n");
           writer.append("<br />\n");
           writer.append("<input type=\"submit\" value=\"Submit\"/>\r\n");
           writer.append("</form>\r\n");
           writer.append("</body>\r\n").append("</html>\r\n");
	   }
    }

    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Part filePart = request.getPart("fileName");
        String captionName = request.getParameter("caption");
        String formDate = request.getParameter("date");
        String fileName = filePart.getSubmittedFileName();

        System.out.println(filePart);

        if(fileName.equals("")){
            response.setStatus(302);
            response.sendRedirect("upload");
            return;
        }

        if(formDate.equals("")) formDate = "2020-10-10";
        Date formSqlDate = Date.valueOf(formDate);
        if(captionName.equals("")) captionName = "No caption"; 

        filePart.write(System.getProperty("catalina.base") + "/webapps/Client-Server-A1/images/" + fileName);
        Connection con = null;
        	try {
			Class.forName("oracle.jdbc.OracleDriver");
		} catch (Exception ex) {
			System.out.println("Message: " + ex.getMessage ()); 
			return;
        }
        	try {
                HttpSession session = request.getSession(false);
        		con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "system", "oracle1");
        		PreparedStatement preparedStatement = con.prepareStatement("INSERT INTO photos (ID, userID, name, description, picture, datetaken) VALUES (?,?,?,?,?,?)");
			    UUID uuid = UUID.randomUUID();
			    preparedStatement.setBytes(1,asBytes(uuid));
                preparedStatement.setString(2, (String)session.getAttribute("USER_ID"));
			    preparedStatement.setString(3, fileName);
                preparedStatement.setString(4, captionName);
                try {
				    FileInputStream fin = new FileInputStream(System.getProperty("catalina.base") + "/webapps/Client-Server-A1/images/" + fileName);
      				preparedStatement.setBinaryStream(5, fin);
			    } catch (Exception ex) { }
			    Date sqlDate = Date.valueOf(formDate);
			    preparedStatement.setDate(6, sqlDate);
			    int row = preparedStatement.executeUpdate();
			    preparedStatement.close();
            } catch(SQLException ex) {
            	   while (ex != null) { 
                	System.out.println("Message: " + ex.getMessage ()); 
                	System.out.println("SQLState: " + ex.getSQLState ()); 
                	System.out.println("ErrorCode: " + ex.getErrorCode ()); 
                	ex = ex.getNextException(); 
                	System.out.println("");
            	  } 
            }
            response.setContentType("text/html");
            response.setCharacterEncoding("UTF-8");
            response.sendRedirect("main");
    }
    private String getListing(String path) {
     String dirList =  null;
      File dir = new File(path);
      String[] chld = dir.list();
      return dirList;
    } 

    
	private boolean isLoggedIn(HttpServletRequest req) {
		HttpSession session = req.getSession(false);

		if (session == null || !req.isRequestedSessionIdValid()) {
			return false;
		}else{
			return true;
		}

	}   
    
}


