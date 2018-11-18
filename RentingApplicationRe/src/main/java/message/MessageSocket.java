/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package message;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import com.google.gson.Gson;
import org.bson.Document;



/**
 *
 * @author vatsal
 */
public class MessageSocket {
    
    private final SocketIOServer sis;
    
    public MessageSocket() {
       Configuration conf = new Configuration();
       conf.setHostname("0.0.0.0");
       conf.setPort(5010);
       conf.setOrigin("http://124.253.18.54:8080");
       this.sis = new SocketIOServer(conf);
    }
    
    public SocketIOServer getServer() {
        return this.sis;
    }
}
