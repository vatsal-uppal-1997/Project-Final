/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package message;

import user.UserBean;

/**
 *
 * @author vatsal
 */
public class MessageBean {
    private UserBean uid; // to
    private UserBean from;
    private String message;
    private String mid; // message id 
    private String type;
    private String timestamp;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    
    public MessageBean() {
    }

    public MessageBean(String mid, UserBean uid, UserBean from, String message, String type) {
        this.uid = uid;
        this.from = from;
        this.message = message;
        this.mid = mid;
        this.type = type;
    }

    public UserBean getUid() {
        return uid;
    }

    public void setUid(UserBean uid) {
        this.uid = uid;
    }

    public UserBean getFrom() {
        return from;
    }

    public void setFrom(UserBean from) {
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }
    
}
