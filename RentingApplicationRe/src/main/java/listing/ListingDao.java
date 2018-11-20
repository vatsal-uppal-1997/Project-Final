/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package listing;

import com.google.gson.Gson;
import hibercfg.HiberUtil;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import user.UserDao;

/**
 *
 * @author root
 */
public class ListingDao {
    // CREATE READ UPDATE DELETE

    private Session session;
    private final SessionFactory sf;
    private final List<String> validFields;

    public ListingDao() {
        this.sf = HiberUtil.getSessionAnnotationFactory();
        this.session = this.sf.getCurrentSession();
        this.validFields = new ArrayList<>();
        this.validFields.add("id");
        this.validFields.add("uid");
        this.validFields.add("title");
        this.validFields.add("locality");
        this.validFields.add("description");
        this.validFields.add("imagePath");
        this.validFields.add("activity");
    }
    
    private void refreshSession() {
        if (!this.session.isOpen()) {
            this.session = this.sf.openSession();
            session.beginTransaction();
        } else {
            session.close();
            refreshSession();
        }
    }
    // C R U D
    public boolean createListing(ListingBean lb) {
        refreshSession();
        this.session.save(lb);
        this.session.getTransaction().commit();
        return true;
    }

    public String readListing(String getBy, String value) throws NoSuchElementException {
        refreshSession();
        getBy = getBy.toLowerCase();
        value = value.toLowerCase();
        if (!this.validFields.contains(getBy)) {
            throw new IllegalArgumentException("Invalid Field Data !");
        }
        List<ListingBean> toConvert;
        String hql = "FROM ListingBean lb WHERE lb."+getBy+" = :value";
        System.out.println(hql);
        Query query = this.session.createQuery(hql).setParameter("value", value);
        toConvert = query.list();
        if (toConvert.isEmpty()) {
            throw new NoSuchElementException("No listings found with "+getBy+" = "+value);
        }
        Gson gs = new Gson();
        String json = gs.toJson(toConvert);
        System.out.println(json);
        this.session.close();
        return json;
    }
    
    public String readListingLocality(String getBy, String value, String locality) throws NoSuchElementException {
        refreshSession();
        getBy = getBy.toLowerCase();
        value = value.toLowerCase();
        if (!this.validFields.contains(getBy)) {
            throw new IllegalArgumentException("Invalid Field Data !");
        }
        List<ListingBean> toConvert;
        String hql = "FROM ListingBean lb WHERE lb."+getBy+" = :value AND lb.locality = :locality";
        Query query = this.session.createQuery(hql).setParameter("value", value).setParameter("locality", locality);
        toConvert = query.list();
        if (toConvert.isEmpty()) {
            throw new NoSuchElementException("No listings found with "+getBy+" = "+value+" and locality = "+locality);
        }
        Gson gs = new Gson();
        String json = gs.toJson(toConvert);
        System.out.println(json);
        this.session.close();
        return json;
    }

    public String getInterested(String uid) 
        throws NoSuchElementException {
        refreshSession();
        UserDao ud = new UserDao();
        List<String> listings = ud.getInterested(uid);
        List<ListingBean> toConvert = new LinkedList<>();
        if (listings.isEmpty()) {
            throw new NoSuchElementException("The 'interested' List is empty");
        }
        for (String i : listings) {
            toConvert.add((listing.ListingBean) this.session.get(ListingBean.class, i));
        }
        Gson gs = new Gson();
        String json = gs.toJson(toConvert);
        this.session.close();
        return json;
    }
    
    public String getInterestedLocality(String uid, String locality) 
        throws NoSuchElementException {
        refreshSession();
        UserDao ud = new UserDao();
        List<String> listings = ud.getInterested(uid);
        List<ListingBean> toConvert = new LinkedList<>();
        if (listings.isEmpty()) {
            throw new NoSuchElementException("The 'interested' List is empty");
        }
        for (String i : listings) {
            ListingBean temp = (listing.ListingBean) this.session.get(ListingBean.class, i);
            if (temp.getLocality().equals(locality))
                toConvert.add(temp);
        }
        Gson gs = new Gson();
        String json = gs.toJson(toConvert);
        this.session.close();
        return json;
    }

    public boolean checkIfExists(String getBy, String value) throws NoSuchElementException {
        refreshSession();
        return !this.session
                   .createQuery("FROM ListingBean lb WHERE "+getBy+" = :value")
                   .setParameter("value", value)
                   .list()
                   .isEmpty();
    }

    public boolean updateListing(String id, String updateField, String updateWith) throws NoSuchElementException {
        refreshSession();
        if (!this.validFields.contains(updateField)) {
            throw new IllegalArgumentException("Invalid Field Data !");
        }
        try {
            System.out.println(id);
            checkIfExists("id", id);
        } catch (NoSuchElementException e) {
            throw e;
        }
        if (updateField.equals("id") || updateField.equals("uid")) {
            return false;
        }
        System.out.println("session is "+this.session.isOpen());
        ListingBean listing = (ListingBean) this.session.get(ListingBean.class, id);
        switch(updateField) {
            case "title":
                listing.setTitle(updateWith);
                break;
            case "locality":
                listing.setLocality(updateWith);
                break;
            case "description":
                listing.setDescription(updateWith);
                break;
            case "imagePath":
                listing.setImagePath(updateWith);
                break;
            case "activity":
                listing.setActivity(updateWith);
                break;
        }
        this.session.saveOrUpdate(listing);
        this.session.getTransaction().commit();
        return true;
    }

    public boolean deleteListing(String id) {
        refreshSession();
        try {
            this.session.createQuery("DELETE ListingBean lb WHERE lb.id = :id").setParameter("id", id).executeUpdate();
            this.session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            System.out.println(e);
            this.session.close();
            return false;
        }
    }

    public void deleteAll(String uid) {
        refreshSession();
        this.session.createQuery("DELETE ListingBean lb WHERE lb.uid = :uid").setParameter("uid", uid).executeUpdate();
        this.session.getTransaction().commit();
    }
    
    public void closeFactory() {
       this.sf.close();
    }
}
