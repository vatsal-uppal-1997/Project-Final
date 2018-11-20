/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package user;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import org.bson.Document;

/**
 *
 * @author cereal
 */
public class UserDao {

    // CREATE READ UPDATE DELETE
    private final MongoClient mc;
    private final MongoDatabase md;
    private final MongoCollection<Document> coll;
    private final List<String>validFields;
    public UserDao() {
        this.mc = MongoSingleton.getMongoClient();
        this.md = this.mc.getDatabase("project");
        this.coll = this.md.getCollection("users");
        this.validFields = new ArrayList<>();
        this.validFields.add("id");
        this.validFields.add("username");
        this.validFields.add("locality");
        this.validFields.add("email");
        this.validFields.add("mobile");
        this.validFields.add("interested");
    }

    //Documents are stuff that reside in a mongo db collection (tables)
    private Document getDocument(UserBean ub) {
        Document doc = new Document();
        doc.append("_id", ub.getId());
        doc.append("username", ub.getUsername());
        doc.append("locality", ub.getLocality());
        doc.append("email", ub.getEmail());
        doc.append("mobile", ub.getMobile());
        return doc;
    }

    // Create --- Creates a user entry
    public boolean createUser(UserBean ub, String password) throws IllegalArgumentException {
        if (checkIfExists("username", ub.getUsername())) 
            throw new IllegalArgumentException("The username alreay exists");
        if (checkIfExists("email", ub.getEmail()))
            throw new IllegalArgumentException("The email alreay exists");
        if (checkIfExists("mobile", ub.getMobile()))
            throw new IllegalArgumentException("The mobile number alreay exists");
        Document doc = getDocument(ub);
        coll.insertOne(doc);
        (new KeyMaster()).createKey(ub.getId(), password);
        return true;
    }

    // Read --- Returns a user bean, Given the user id
    public UserBean readUser(String getBy, String value) throws NoSuchElementException {
        UserBean temp = new UserBean();
        Boolean found = false;
        getBy = getBy.toLowerCase();
        //value = value.toLowerCase();
        for (Document i : coll.find(new Document().append(getBy, value))) {
            temp.setId(i.getString("_id"));
            temp.setUsername(i.getString("username"));
            temp.setLocality(i.getString("locality"));
            temp.setMobile(i.getString("mobile"));
            temp.setEmail(i.getString("email"));
            found = true;
            break;
        }
        if (found == false)
            throw new NoSuchElementException("User having "+getBy+" = "+value+" not found");
        return temp;
    }
    

    public boolean checkIfExists(String field, String toFind) {
        
        if (!this.validFields.contains(field))
            return false;
        if (field.equals("id"))
            field = "_id";
        for (Document i : coll.find(new Document().append(field, toFind))) 
            return true;
        return false;
        
    }

    public boolean updateUser(String id, String updateField, String updateWith) throws NoSuchElementException {
        
        if (!this.validFields.contains(updateField))
            throw new IllegalArgumentException("Invalid Field Data !");
        
        if (updateField.equals("id"))
            return false;
        
        UserBean getUser;
        
        try {
            getUser = readUser("id", id);
        } catch (NoSuchElementException e) {
            throw e;
        }
        
        updateField = updateField.toLowerCase();
        
        
        
        Document newValues = new Document();
        Document oldValues = new Document();
        Document updateQuery = new Document();
        oldValues.put("_id", getUser.getId());
        newValues.put(updateField, updateWith);
        updateQuery.put("$set", newValues);
        
        /*
        {
            "$set" : {
                        "someValue" : "xyz"
            }
        }
        */
        coll.updateOne(oldValues, updateQuery);
        return true;
    }
    
    public boolean addInterested(String uid, String lid) throws NoSuchElementException {
        if (uid == null || lid == null)
            throw new IllegalArgumentException("uid or lid is null");
        Document user = null;
        boolean exists = false;
        for (Document i : this.coll.find(new Document().append("_id", uid))) {
            user = i;
            exists = true;
        }
        if (!exists)
            throw new NoSuchElementException();
        List <String> lids = null;
        boolean changed = false;
        if (user != null && user.containsKey("interested")) {
            lids = user.get("interested", List.class);
            if (!lids.contains(lid)) {
                lids.add(lid);
                changed = true;
            }
        } else if (user != null) {
            lids = new LinkedList<>();
            lids.add(lid);
            changed = true;
        }
        if (changed)
            this.coll.updateOne(new Document().append("_id", uid), new Document().append("$set", new Document().append("interested", lids)));
        return true;
    }
    
    public List getInterested(String uid) throws NoSuchElementException {
        boolean exists = false;
        List<String> listings = new LinkedList<>();
        for (Document i: this.coll.find(new Document().append("_id", uid))) {
            listings = i.get("interested", List.class);
            exists = true;
        }
        if (!exists)
            throw new NoSuchElementException();
        return listings;
    }
    
    public void removeInterested(String uid, String lid) {
       // db.users.update({"_id" : "298e464e-ec43-482c-95eb-b3bc79f970b8"}, { $pull : { "interested": "270a67b7-3219-40f6-bb8f-ff87a2dd2089" } })
       this.coll.updateOne(new Document().append("_id", uid), 
               new Document().append("$pull", 
                       new Document().append("interested", lid)));
    }
    public boolean deleteUser(String id) {
        Document doc = new Document();
        doc.put("_id", id);
        DeleteResult dr = coll.deleteOne(doc);
        return dr.getDeletedCount() != 0 && (new KeyMaster()).deleteKey(id);
    }

}
