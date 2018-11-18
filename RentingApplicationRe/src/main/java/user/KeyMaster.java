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
import java.util.List;
import org.bson.Document;

/**
 *
 * @author root
 */

/*
    Handles passwords. 
    Not a good idea to set password in session, better verify password server side
*/
public class KeyMaster {
    
    private final MongoClient mc;
    private final MongoDatabase md;
    private final MongoCollection<Document> coll;
    private final List<String>validFields;
    private final UserDao ud;
    
    public KeyMaster() {
        this.mc = MongoSingleton.getMongoClient();
        this.md = this.mc.getDatabase("project");
        this.coll = this.md.getCollection("passwords");
        this.ud = new UserDao();
        this.validFields = new ArrayList<>();
        this.validFields.add("id");
        this.validFields.add("password");
    }
    
    // CRUD
    
    boolean createKey(String id, String password) {
        Document doc = new Document();
        doc.append("_id", id);
        doc.append("password", EncryptPass.hashPass(password));
        try {
            this.coll.insertOne(doc);
        } catch (Exception e){
          System.out.println(e); // I dont know what went wrong, if anyone gets this
          return false;
        }
        return true;
    }
    
    // Return yes or no if the id and password combination exists
    public boolean readKey(String id, String password) {
        for (Document i: coll.find((new Document()).append("_id", id).append("password", password)))
            return true;
        return false;
    }
    
    public boolean updateKey(String id, String password) {
        
        if (!readKey(id, password))
            return false; // If username and password combo does not already exists return false
        
        Document updateQuery = new Document().append("$set", 
                                                    new Document().append("password", password));
        this.coll.updateOne(new Document().append("_id", id), 
                            updateQuery);
        return true;
    }
    
    // UserDao delete method calls this
    boolean deleteKey(String id) {
        DeleteResult dr = this.coll.deleteOne(new Document().append("_id", id));
        return dr.getDeletedCount() != 0;
    }
    
}
