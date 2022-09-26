import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletException;
import java.awt.image.*;
import javax.imageio.*;
import java.util.ArrayList;
import java.nio.*;
import java.sql.*;
import java.text.*;
import java.util.Date;
import java.util.Locale;

/* To compile:
cd C:\tomcat\webapps\Client-Server-A1\WEB-INF\classes
cd C:\tomcat\bin
javac  -classpath c:\tomcat\lib\servlet-api.jar;c:\tomcat\lib\cos.jar;c:\temp\ojdbc8.jar;c:\tomcat\lib\json-simple-1.1.1.jar GalleryServlet.java
*/

public class GalleryServlet extends HttpServlet {

    public static int imageIndex = 0; // Stores current image's index in the Arraylist
    public static boolean autoClicked = false; // Stores value of whether or not the auto button is clicked

    // Helper Function to insert Row into Table
    public static ArrayList<String> getAllImages(HttpServletRequest request, HttpServletResponse response) {
        Connection con = null;
        ArrayList<String> chld = new ArrayList<String>();
        HttpSession session = request.getSession(false);

        try {
            Class.forName("oracle.jdbc.OracleDriver");
        } catch (Exception ex) {
            System.out.println("Message: " + ex.getMessage());
            return null;
        }
        try {
            Class.forName("oracle.jdbc.OracleDriver");
        } catch (Exception ex) {
            System.out.println("Message: " + ex.getMessage());
            return null;
            }
            try {
                con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "system", "oracle1");
                  String fixedDate = (String)session.getAttribute("SEARCH_DATE");
                  // System.out.println("Date searched:" + fixedDate);
                  PreparedStatement statement;
                  if(!fixedDate.isEmpty()){
                        String stateStr = "select * from photos where ((description LIKE ?) AND (datetaken LIKE ?) AND (userID LIKE ?))";
                        // System.out.println(stateStr);
                        statement = con.prepareStatement(stateStr);
                        statement.setString(1, "%" + session.getAttribute("SEARCH_CAPTION") + "%");
                        statement.setDate(2, java.sql.Date.valueOf(fixedDate));
                        statement.setString(3, (String)session.getAttribute("USER_ID"));
                  } else {
                        // System.out.println("Caption searched:" + session.getAttribute("SEARCH_CAPTION"));
                        String stateStr = "select * from photos where ((description LIKE ?)AND (userID LIKE ?))";
                        // System.out.println(stateStr);
                        statement = con.prepareStatement(stateStr);
                        statement.setString(1, "%" + session.getAttribute("SEARCH_CAPTION") + "%");
                        statement.setString(2, (String)session.getAttribute("USER_ID"));
                  }
                  ResultSet results = statement.executeQuery();
                  String imgPath;
                  while (results.next()){
                        imgPath = results.getString(3);
                        chld.add(imgPath);
                  }
                  // for(String s : chld){
                  //       System.out.println("In chld: " + s);
                  // }
            } catch (Exception ex){
                  ex.printStackTrace();
            }

        return chld;
    }

    // When previous button is clicked
    public static String previous(ArrayList<String> imageNames, int imageCount) {

        // If the current image is at the first index, get the last image
        if (imageIndex == 0) {
            imageIndex = imageCount;

        } else {
            // Get index of the previous image
            imageIndex--;
        }

        // Return the previous image name
        return imageNames.get(imageIndex);
    }

    // When next button is clicked
    public static String next(ArrayList<String> imageNames, int imageCount) {

        // If the current image is at the last index, get the first image
        if (imageIndex == imageCount) {
            imageIndex = 0;

        } else {
            // Get index of the next image
            imageIndex++;
        }

        // Return the next image name
        return imageNames.get(imageIndex);
    }

    public void deleteImg(ArrayList<String> imageNames, HttpServletRequest request){
      Connection con = null;
      HttpSession session = request.getSession(false);
      try {
            Class.forName("oracle.jdbc.OracleDriver");
      } catch (Exception ex) {
            System.out.println("Message: " + ex.getMessage());
      }
      try {
            System.gc();
            File fileToDel = new File(System.getProperty("catalina.base") + "/webapps/Client-Server-A1/images/" + imageNames.get(imageIndex)); 
            //The file deletion seems to be working, although file management is notorious for not working as intended.
            //I promise that at the time of writing this comment, the files consistently delete successfully.
            if (fileToDel.delete()) { 
                  System.out.println("Deleted the file: " + fileToDel.getName());
            } else {
                  System.out.println("Failed to delete the file:" + fileToDel.getName());
            }
            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "system", "oracle1");
            PreparedStatement statement;
            String stateStr = "delete from photos where (name LIKE ?)";
            // System.out.println(stateStr);
            statement = con.prepareStatement(stateStr);
            statement.setString(1, imageNames.get(imageIndex));
            statement.executeUpdate();
            imageNames.remove(imageNames.get(imageIndex));
      } catch (Exception ex){
            ex.printStackTrace();
      }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        // Check if a user is logged in
        if (session == null) {
//            response.setStatus(302);
//            response.sendRedirect("login");
            response.sendRedirect("/Client-Server-A1/"); // redirect to login
        } else {

//        HttpSession session = request.getSession();

            // Get all image names in the database and store it in an ArrayList
            ArrayList<String> imageNames = getAllImages(request, response);
            if (imageIndex >= imageNames.size()) {
                imageIndex = 0;
            }

            // Stores the number of images in the database
            int imageCount = imageNames.size() - 1;

            // Create HTML page to send for the response
            response.setContentType("text/html");
            response.setCharacterEncoding("UTF-8");

            // Get previous, next, and auto buttons from the HTML page
            String previousBtn = request.getParameter("previousBtn");
            String nextBtn = request.getParameter("nextBtn");
            String autoBtn = request.getParameter("autoBtn");
            String delBtn = request.getParameter("deleteBtn");

            if (previousBtn != null) {
                // Previous button was pressed
                // Get previous image name
                previous(imageNames, imageCount);
                autoClicked = false;

            } else if (nextBtn != null) {
                // Next button was pressed
                // Get next image name
                next(imageNames, imageCount);
                autoClicked = false;

            } else {
                // No button was pressed
            }

            if (delBtn != null) {
                deleteImg(imageNames, request);
                if (imageNames.size() >= 1) {
                    imageIndex = 0;
                } else if (imageNames.size() < 1) {
                    response.sendRedirect("gallery");
                }
            }

            String autoRefresh = "";
            String stopBtn = request.getParameter("stopButton");
            // If auto button was clicked
            if (autoBtn != null) {
                autoClicked = true;
            }

            // If stop button was clicked, atop auto gallery
            if (stopBtn != null) {
                autoClicked = false;
            }

            if (autoClicked == true) {
                // If auto button was clicked, refresh the page every 3 seconds to show the next
                // image
                autoRefresh = "<meta http-equiv=\"refresh\" content=\"3\">";

                // Get the next image
                previous(imageNames, imageCount);

            } else {
                // Don't refresh the page
                autoRefresh = "";
            }


            PrintWriter out = response.getWriter();

            // HTML page
            out.println("<html>");
            out.println("<meta charset='UTF-8'>");

            // Only refresh is auto button was clicked
            out.println(autoRefresh);

            out.println("<body>");

            if (imageNames.size() > 0) {
                // Get the current image in the arraylist
                String img_src = imageNames.get(imageIndex);
                String alt_text = "image";

                // String img_src = imageNames.get(0);
                System.out.println("Arraylist first file is: " + img_src);
                // String alt_text = "SOME IMAGE";
                out.println("<div>");
                out.println("<form action='/Client-Server-A1/gallery' method='GET'>");
                out.println("<div>");
                out.println(
                        "<img id = \"img_src\" src=./images/" + img_src + " alt=" + alt_text + " width=200 height=150>");
                out.println("<div>");
                out.println("<br>");
                out.println("</form>");

                out.println("<div class='button'>");
                out.println("<form action=\"/Client-Server-A1/gallery\" method=\"GET\" enctype=\"multipart/form-data\">");
                out.println(
                        "<input type=\"submit\" class='button' id=\"previousBtn\" name=\"previousBtn\" value=\"Previous\" / >");
                out.println("<input type=\"submit\" class='button' id=\"nextBtn\" name=\"nextBtn\" value=\"Next\" / >");
                out.println("<br /><br />");

                out.println("<input type=\"submit\" class='button' id=\"autoBtn\" name=\"autoBtn\" value=\"Auto\" / >");
                out.println("<input type=\"submit\" class='button' id=\"stopButton\" name=\"stopButton\" value=\"Stop\" / >");
                out.println("<input type=\"submit\" class='button' id=\"deleteButton\" name=\"deleteBtn\" value=\"Delete Image\" / >");
                out.println("</form>");
                out.println("</div>");

            } else {
                out.println("<div>");
                out.println("<h1 id='ErrorHeading'>No Results found!</h1>");
                out.println(
                        "<p class='ErrorText'>We couldn't find anything for that search. Try changing your search terms!</p>");
                out.println(
                        "<p class='ErrorText'>Alternatively, if you haven't uploaded any images, you'll need to do that first.</p>");
                out.println("</div>");
            }

            out.println("<div>");
            out.println("<form action='main' method='GET'>");
            out.println("<button class='button' id='main'>Main</button>");
            out.println("</div><br>");

            // out.println("</form>");
            // out.println("</div>");

            // out.println("<script src=\"/Client-Server-A1/gallery.js\"></script>");
            out.println("</body></html>");
            out.close();
        }
    }
    private boolean isLoggedIn(HttpServletRequest req) {
        HttpSession session = req.getSession(false);

        if (session == null || !req.isRequestedSessionIdValid()) {
            return false;
        } else {
            return true;
        }
    }
}
