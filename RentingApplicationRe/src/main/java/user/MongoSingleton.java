/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package user;

import com.mongodb.MongoClient;

/**
 *
 * @author cereal
 */
public class MongoSingleton {

    static MongoClient mongo = null;
    
    private MongoSingleton() {
        
    }
    
    static MongoClient getMongoClient() {
        if (mongo == null) 
            mongo = new MongoClient( "localhost" , 27017 );
        return mongo;
    }
    
}
