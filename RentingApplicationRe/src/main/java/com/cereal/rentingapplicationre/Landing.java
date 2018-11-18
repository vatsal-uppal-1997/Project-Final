/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cereal.rentingapplicationre;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.bson.Document;
import user.EncryptPass;
import user.KeyMaster;
import user.UserBean;
import user.UserDao;

/**
 *
 * @author root
 */
public class Landing extends HttpServlet {

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
            out.println("<title>Servlet Login</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Login at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
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
        PrintWriter pw = response.getWriter();
        HttpSession hs = request.getSession();
        try {
            boolean loggedIn = (boolean) hs.getAttribute("loggedIn");
            if (loggedIn) {
                UserBean ub = (UserBean) hs.getAttribute("user");
                pw.println((new Document()).append("uid", ub.getId()).toJson());
            } else {
                throw new Exception();
            }
        } catch (Exception e) { // Null pointer can occur or Exception
            pw.println(new Document().append("message","Invalid Request").toJson());
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        HttpSession hs = request.getSession();
        PrintWriter pw = response.getWriter();
        if (hs.getAttribute("loggedIn") == null)
            hs.setAttribute("loggedIn", false);
        String json = request.getParameter("json");
        if (json == null) 
            return;
        JsonElement jelement = new JsonParser().parse(json);
        JsonObject jobject = jelement.getAsJsonObject();
        String username;
        String password;
        if (jobject.has("logout")) {
            try {
                hs.invalidate();
            } catch (IllegalStateException e) {
                System.out.println(e);
            } finally {
                hs.setAttribute("loggedIn", false);
            }
            return;
        }
        Set<String> keys = jobject.keySet();
        if (keys.size() == 1) {
            String field = "";
            for (String i: keys)
                field = i;
            if ((new UserDao()).checkIfExists(field, jobject.get(field).getAsString()))
                pw.println(new Document().append("message",field+" already exists !").toJson());
            else
                pw.println(new Document().append("message",field+" is available !").toJson());
            return;
        }
        try {
            username = jobject.get("username").getAsString();
            password = EncryptPass.hashPass(jobject.get("password").getAsString());
        } catch (Exception e) {
            pw.println(new Document().append("message", "Invalid Request").toJson());
            return;
        }
        try {
            UserBean ub = (new UserDao()).readUser("username", username);
            String id;
            id = ub.getId();
            if ((new KeyMaster()).readKey(id, password)) {
                hs.setAttribute("loggedIn", true);
                hs.setAttribute("user", ub);
                pw.println((new Document()).append("message", "User Verified").append("uid", ub.getId()).toJson());
                
            } else {
                pw.println((new Document()).append("message", "Username or Password Invalid !").toJson());
            } 
        } catch (NoSuchElementException e) {
           pw.println((new Document()).append("message", "Username or Password Invalid !").toJson());
        } 
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
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String result[] = (java.net.URLDecoder.decode(br.readLine(), "UTF-8")).split("&");
        PrintWriter pw = response.getWriter();
        HttpSession hs = request.getSession();
        if (hs.getAttribute("loggedIn") == null)
            hs.setAttribute("loggedIn", false);
        UserBean ub = new UserBean();
        String json;
        if (result[0].split("=")[0].equals("json"))
            json = result[0].split("=")[1];
        else
            json = null;
        String password, confirm;
        System.out.println("query = "+request.getQueryString());
        if (json == null) {
            pw.println(new Document().append("message", "Invalid Request").toJson());
            return;
        }
        JsonElement jelement = new JsonParser().parse(json);
        JsonObject jobject = jelement.getAsJsonObject();
        try {
            ub.setUsername(jobject.get("username").getAsString());
            ub.setEmail(jobject.get("email").getAsString());
            ub.setMobile(jobject.get("mobile").getAsString());
            ub.setLocality(jobject.get("locality").getAsString());
            ub.setId(UUID.randomUUID().toString());
            password = jobject.get("password").getAsString();
            confirm = jobject.get("confirm").getAsString();
            if (!password.equals(confirm))
                pw.println(new Document().append("message", "Password do not match").toJson());
        } catch (Exception e) {
            System.out.println(e);
            pw.println(new Document().append("message", "Invalid Request").toJson());
            return;
        }
        try {
            (new UserDao()).createUser(ub, password);
            hs.setAttribute("loggedIn", true);
            hs.setAttribute("user", ub);
            pw.println((new Document()).append("message", "Account Created").append("uid", ub.getId()).toJson());
        } catch(IllegalArgumentException e) {
            pw.println((new Document()).append("message", e.getMessage()).toJson());
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
