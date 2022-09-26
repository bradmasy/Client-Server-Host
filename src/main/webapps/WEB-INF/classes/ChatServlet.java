import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/* To compile:
cd C:\tomcat\webapps\Client-Server-A1\WEB-INF\classes  
javac  -classpath .;c:\tomcat\lib\websocket-api.jar ChatServlet.java 
*/

// This class connects with messaging.html and messaging.js for the chat feature
@ServerEndpoint("/hub")
public class ChatServlet {
    private static final Set<Session> sessions = Collections.newSetFromMap(
            new ConcurrentHashMap<Session, Boolean>());

    @OnOpen
    public void onOpen(Session currentSession) {
        sessions.add(currentSession);
    }

    @OnClose
    public void onClose(Session currentSession) {
        sessions.remove(currentSession);
    }

    @OnMessage
    public void onMessage(String message, Session userSession) {
        for (Session session : sessions) {
            // Send message to all users
            session.getAsyncRemote().sendText(message);
        }
    }
}