/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import message.MessageExchange;
import message.MessageSocket;
import org.bson.Document;
import user.UserBean;
import user.UserDao;

/**
 *
 * @author vatsal
 */
public class Message extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Message</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Message at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    SocketIOServer sis = null;

    @Override
    public void init() throws ServletException {
        MessageSocket ms = new MessageSocket();
        final Map<String, String> mappings = new HashMap<>();
        final UserDao ud = new UserDao();
        sis = ms.getServer();
        System.out.println("socket created");
        this.sis.addConnectListener(new ConnectListener() {
            @Override
            public void onConnect(SocketIOClient sioc) {
                sioc.sendEvent("sendUid");
            }
        });
        this.sis.addEventListener("getUid", String.class, new DataListener<String>() {
            @Override
            public void onData(SocketIOClient client, String input, AckRequest ack) throws Exception {
                mappings.put(Document.parse(input).getString("uid"), client.getSessionId().toString());
                System.out.println(mappings.toString());
            }
        });
        this.sis.addEventListener("getAllMessages", String.class, new DataListener<String>() {
            @Override
            public void onData(SocketIOClient client, String input, AckRequest ack) throws Exception {
                Document doc = Document.parse(input);
                String uid = doc.getString("uid");
                client.sendEvent("receiveAllMessages", (new MessageExchange()).getMessages(uid).toJson());
            }

        });
        this.sis.addEventListener("postAMessage", String.class, new DataListener<String>() {
            @Override
            public void onData(SocketIOClient client, String input, AckRequest ack) throws Exception {
                try {
                    Document doc = Document.parse(input);
                    UserBean userTo;
                    UserBean userFrom;
                    userTo = ud.readUser("username", doc.getString("userTo"));
                    userFrom = ud.readUser("username", doc.getString("userFrom"));
                    String messageReceived = processReq(doc, userTo, userFrom);
                    System.out.println("Send message to " + mappings.get(userTo.getId()));
                    sis.getClient(UUID.fromString(mappings.get(userTo.getId()))).sendEvent("gotAMessage", messageReceived);
                } catch (Exception e) {
                    System.out.println(e);
                }
            }

        });
        sis.start();
    }

    String processReq(Document doc, UserBean userTo, UserBean userFrom) {
        MessageExchange mx = new MessageExchange();
        String message = doc.getString("message");
        String messageReceived = "@" + userFrom.getUsername() + " " + message;
        String messageSent = "@" + userTo.getUsername() + " " + message;
        String ts = Long.toString(doc.get("timestamp", Long.class));
        System.out.println("Got data " + doc.toJson() + " " + mx.toString());
        mx.putMessage(userTo.getId(), userFrom.getId(), messageSent, messageReceived, ts);
        System.out.println("control reaches here");
        return messageReceived;
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        String uid = request.getParameter("uid");
        PrintWriter out = response.getWriter();
        HttpSession hs = request.getSession(false);
        if (!((UserBean) hs.getAttribute("user")).getId().equals(uid)) {
            out.println(new Document().append("message", "Invalid Request").toJson());
            return;
        }
        MessageExchange mx = new MessageExchange();
        out.println(mx.getMessages(uid).toJson());
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        HttpSession hs = request.getSession(false);
        PrintWriter out = response.getWriter();
        UserDao ud = new UserDao();
        // to from messageSent messageReceived timestamp(ts)
        UserBean userTo;
        try {
            userTo = ud.readUser("username", request.getParameter("username"));
        } catch (NoSuchElementException e) {
            out.println(new Document().append("message", "Username not found").toJson());
            return;
        }
        UserBean userFrom = (UserBean) hs.getAttribute("user");
        String message = request.getParameter("message");
        String messageReceived = "@" + userFrom.getUsername() + " " + message;
        String messageSent = "@" + userTo.getUsername() + " " + message;
        String ts = request.getParameter("timestamp");
        try {
            MessageExchange mx = new MessageExchange();
            mx.putMessage(userTo.getId(), userFrom.getId(), messageSent, messageReceived, ts);
        } catch (Exception e) {
            out.println(new Document().append("message", "some error occured").toJson());
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
