/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package message;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketConfig;
import com.corundumstudio.socketio.SocketIOServer;



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
       conf.setOrigin("http://192.168.100.96:8080");
       SocketConfig socketConfig = conf.getSocketConfig();
       socketConfig.setReuseAddress(true);
       conf.setSocketConfig(socketConfig);
       this.sis = new SocketIOServer(conf);
    }
    
    public SocketIOServer getServer() {
        return this.sis;
    }
}
