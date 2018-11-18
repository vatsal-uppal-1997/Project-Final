/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cereal.rentingapplicationre;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import listing.ListingBean;
import listing.ListingDao;
import org.bson.Document;
import user.UserBean;
import user.UserDao;

/**
 *
 * @author root
 */
@MultipartConfig
public class Listing extends HttpServlet {

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
            out.println("<title>Servlet Listing</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Listing at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param req
     * @param resp
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        HttpSession hs = request.getSession();
        PrintWriter pw = response.getWriter();
        try {
            UserBean ub = (UserBean) hs.getAttribute("user");
            ListingDao ld = new ListingDao();
            String showMy = request.getParameter("showMy");
            String showInterested = request.getParameter("showInterested");
            String showByLocal = (String) request.getParameter("locality");
            System.out.println(showByLocal);
            if (showMy != null && showInterested != null) {
                pw.println(new Document().append("message", "Invalid Request").toJson());
                return;
            }
            if (showMy != null) {
                try {
                    if (showByLocal != null) {
                        pw.println(new Document().append("listings", ld.readListingLocality("uid", ub.getId(), showByLocal)).toJson());
                    } else {
                        pw.println(new Document().append("listings", ld.readListing("uid", ub.getId())).toJson());
                    }
                    return;
                } catch (NoSuchElementException e) {
                    pw.println(new Document().append("listings", "[]").toJson());
                    return;
                }
            }
            if (showInterested != null) {
                try {
                    if (showByLocal != null) {
                        pw.println(new Document().append("listings", ld.getInterestedLocality(request.getParameter("uid"), showByLocal)).toJson());
                    } else {
                        pw.println(new Document().append("listings", ld.getInterested(request.getParameter("uid"))).toJson());
                    }
                    return;
                } catch (NoSuchElementException e) {
                    System.out.println(e);
                    pw.println(new Document().append("listings", "[]").toJson());
                    return;
                }
            }
            if (showByLocal != null) {
                pw.println(new Document().append("listings", ld.readListing("locality", showByLocal)).toJson());
            } else {
                pw.println(new Document().append("listings", ld.readListing("locality", ub.getLocality())).toJson());
            }
        } catch (NullPointerException e) {
            pw.println(new Document().append("message", "Invalid Request").toJson());
        } catch (NoSuchElementException e) {
            pw.println(new Document().append("listings", "[]").toJson());
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        HttpSession hs = request.getSession();
        PrintWriter pw = response.getWriter();
        try {
            String id = request.getParameter("id");
            if (request.getParameter("id") != null) {
                try {
                    ListingDao ld = new ListingDao();
                    Map<String, String> hm = new HashMap<>();
                    hm.put("title", request.getParameter("title"));
                    hm.put("locality", request.getParameter("locality"));
                    hm.put("description", request.getParameter("description"));
                    System.out.println(hm);
                    for (String i : hm.keySet()) {
                        if (hm.get(i) != null) {
                            ld.updateListing(id, i, hm.get(i));
                        }
                    }
                    Part image = request.getPart("image");
                    if (image != null) {
                        String path = getImagePath(image, id, request.getContextPath());
                        System.out.println("image path : " + path);
                        ld.updateListing(id, "imagePath", path);
                    }
                    pw.println(new Document().append("message", "Listing Successfully Updated").toJson());
                    return;
                } catch (NullPointerException e) {
                    pw.println(new Document().append("message", "Invalid Request").toJson());
                    return;
                }
            }
            String title = request.getParameter("title");
            String locality = request.getParameter("locality");
            String description = request.getParameter("description");
            if (title == null || locality == null || description == null) {
                System.out.println("shits null boi");
                pw.println(new Document().append("message", "Invalid Request").toJson());
                return;
            }
            ListingBean lb = new ListingBean();
            ListingDao ld = new ListingDao();
            lb.setId(UUID.randomUUID().toString());
            lb.setTitle(title);
            lb.setLocality(locality);
            lb.setDescription(description);
            lb.setActivity("0");
            lb.setUid(((UserBean) hs.getAttribute("user")).getId());
            String path = request.getContextPath() + "/Images/480x480.png";
            Part image = request.getPart("image");
            if (image != null) {
                path = getImagePath(image, lb.getId(), request.getContextPath());
            }
            lb.setImagePath(path);
            ld.createListing(lb);
            pw.println(new Document().append("message", "Listing Successfully Created").toJson());
        } catch (NullPointerException e) {
            System.out.println(e);
            pw.println(new Document().append("message", "Invalid Request").toJson());
        }
    }

    private String getImagePath(Part image, String id, String context) throws IOException {
        InputStream is = image.getInputStream();
        BufferedImage bimg = ImageIO.read(is);
        File out = new File("/home/vatsal/Linux Backups/November 12, 2018/Programming Stuff/Netbeans and Projects/NetBeansProjects/NetBeansProjects/Project/RentingApplicationRe/src/main/java/com/cereal/rentingapplicationre/src/main/webapp/Images/" + id);
        ImageIO.write(bimg, "png", out);
        return context + "/Images/" + id;
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter pw = resp.getWriter();
        String listingId = req.getParameter("id");
        String remove = req.getParameter("remove");
        if (remove == null) {
            ListingDao ld = new ListingDao();
            ld.deleteListing(listingId);
            pw.println(new Document().append("message", "listing deleted").toJson());
        } else {
            HttpSession hs = req.getSession();
            String uid = ((UserBean) hs.getAttribute("user")).getId();
            UserDao ud = new UserDao();
            ud.removeInterested(uid, listingId);
            pw.println(new Document().append("message", "listing removed").toJson());
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

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uid = req.getParameter("uid");
        String lid = req.getParameter("lid");
        UserDao ud = new UserDao();
        PrintWriter pw = resp.getWriter();
        HttpSession hs = req.getSession();
        if (!((UserBean) hs.getAttribute("user")).getId().equals(uid)) {
            pw.println(new Document().append("message", "Invalid Request").toJson());
            return;
        }
        try {
            ud.addInterested(uid, lid);
        } catch (NoSuchElementException e) {
            System.out.println(e);
            pw.println(new Document().append("message", "Invalid Request").toJson());
        }
    }

}
