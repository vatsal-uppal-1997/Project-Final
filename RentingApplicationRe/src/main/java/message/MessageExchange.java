/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package message;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import org.bson.Document;
import user.UserBean;
import user.UserDao;

/**
 *
 * @author vatsal
 */
public class MessageExchange {
    private final MongoClient mc;
    private final MongoDatabase md;
    private final MongoCollection<Document> coll;
    
    public MessageExchange() {
        this.mc = MongoSingleton.getMongoClient();
        this.md = this.mc.getDatabase("project");
        this.coll = this.md.getCollection("messages");
    }
    
    public boolean putMessage(String to, String from, String messageSent, String messageReceived, String ts) {
        System.out.println("called "+to+" "+from+" "+messageSent+" "+messageReceived+" "+ts);
        this.coll.insertOne(new Document().append("uid", to).append("from", from).append("message", messageReceived).append("type", "Received").append("timestamp", Long.parseLong(ts)));
        this.coll.insertOne(new Document().append("uid", from).append("to", to).append("message", messageSent).append("type", "Sent").append("timestamp", Long.parseLong(ts)));
        return true;
    }
    
    public Document getMessages(String uid) {
        Document json = new Document();
        List<Document> docs = new LinkedList<>();
        for (Document i: this.coll.find(new Document().append("uid", uid)).sort(new Document().append("timestamp", 1))) {
            docs.add(i);
        }
        json.append("messages", docs);
        //System.out.println(json.toJson());
        return json;
    }
    
    public boolean removeListing(String uid, String id) {
        boolean removed = true;
        for (Document i: this.coll.find(new Document().append("_id", id))) {
            if (i.getString("uid").equals(uid)) {
                if (i.getString("type").equals("Received"))
                    removed = false;
            } else {
                removed = false;
            }
        }
        if (removed)
            this.coll.deleteOne(new Document().append("_id", id));
        return removed;
    }
    
}
