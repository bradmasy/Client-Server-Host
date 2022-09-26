import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;

public class MainServlet extends HttpServlet {
  
  public void doGet(HttpServletRequest request,
      HttpServletResponse response)
      throws ServletException, IOException {
      HttpSession session = request.getSession(false);
      if (session == null) {
//        response.setStatus(302);
//		response.sendRedirect("login");
		response.sendRedirect("/Client-Server-A1/");
	  }
	  else {


		  String title = "Logged in as: ";
		  title += session.getAttribute("USER_ID");

		  session.setAttribute("SEARCH_CAPTION", "");
		  session.setAttribute("SEARCH_DATE", "");

		  response.setContentType("text/html");
		  String docType = "<!doctype html public \"-//w3c//dtd html 4.0 " + "transitional//en\">\n";
		  String html = docType + "<html>\n" + "<head><title>" + title + "</title></head>\n"
				  + "<body bgcolor=\"#f0f0f0\">\n" + "<h1 align=\"center\">" + title + "</h1>\n" +
				  "<div style=\"text-align: center;\">\n" +
				  "<form action=\"upload\" method=\"GET\">\n" +
				  "<input type=\"submit\" value=\"UPLOAD\" />\n" +
				  "</form>\n" +
				  "</div>\n" +
				  "<div style=\"text-align: center;\">\n" +
				  "<form action=\"gallery\" method=\"GET\">\n" +
				  "<input type=\"submit\" value=\"GALLERY\" />\n" +
				  "</form>\n" +
				  "</div>\n" +
				  "<div style=\"text-align: center;\">\n" +
				  "<form action=\"search\" method=\"GET\">\n" +
				  "<input type=\"submit\" value=\"SEARCH\" />\n" +
				  "</form>\n" +
				  "</div>\n" +
				  "<div style=\"text-align: center;\">\n" +
				  "<form action=\"/Client-Server-A1/websocket/messaging.html\">\n" +
				  "<input type=\"submit\" value=\"MESSAGE\" /> \n" +
				  "</form>\n" +
				  "</div>\n" +
				  "<div style=\"text-align: center;\">\n" +
				  "<form action=\"event.preventDefault();\" method=\"GET\">\n" +
				  "<input id=\"LogOutButton\" type=\"submit\" value=\"LOGOUT\" />\n" +
				  "</form>\n" +
				  "</div>\n" +
				  "<script src=\"index2.js\"></script>\n" +
				  "</body></html>";

		  PrintWriter out = response.getWriter();
		  out.println(html);
	  }
  }
}