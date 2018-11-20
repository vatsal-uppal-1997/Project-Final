/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package listing;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author root
 */
@Entity
@Table(name="listings", uniqueConstraints={@UniqueConstraint(columnNames={"id"})})
public class ListingBean {
    
    @Id
    @Column(name="id", nullable=false,unique=true,length=40)
    private String id;
    @Column(name="uid", nullable=false,length=40)
    private String uid;
    @Column(name="title", nullable=false,length=200)
    private String title;
    @Column(name="locality", nullable=false,length=80)
    private String locality;
    @Column(name="description", nullable=false, columnDefinition="TEXT")
    private String description;
    @Column(name="imagePath", nullable=false,length=200)
    private String imagePath;
    @Column(name="activity", nullable=false,length=30)
    private String activity;

    @Override
    public boolean equals(Object obj) {
        ListingBean cast = (ListingBean) obj;
        return cast.getId().equals(this.getId());
    }

    @Override
    public int hashCode() {
        return super.hashCode(); 
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
    
    
}
