/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package userTemp;

import listing.ListingDao;
import listing.ListingBean;
import java.util.UUID;

/**
 *
 * @author vatsal
 */
public class Driver {
    public static void main(String[] args) {
        ListingBean lb = new ListingBean();
        lb.setId(UUID.randomUUID().toString());
        lb.setUid(UUID.randomUUID().toString());
        lb.setTitle("popnigga");
        lb.setLocality("panchkula");
        lb.setImagePath("/some/random/path.jpeg");
        lb.setDescription("some text");
        lb.setActivity("0");
        ListingDao ld = new ListingDao();
        ld.createListing(lb);
        System.out.println("Check if exists = "+ld.checkIfExists("locality", "panchkula"));
        //System.out.println("Get Interested (uid) = "+ld.getInterested(lb.getUid()));
        //System.out.println("Get Interested Locality (uid) = "+ld.getInterestedLocality(lb.getUid(), "panchkula"));
        System.out.println("Read Listing = "+ld.readListing("description", "some text"));
        System.out.println("Read Listing Locality = "+ld.readListingLocality("activity", "0", "panchkula"));
        ld.updateListing(lb.getId(), "activity", "1");
        System.out.println("Read Listing = "+ld.readListing("activity", "1"));
        //ld.deleteAll(lb.getUid());
        ld.deleteListing("5f397c16-e923-4228-8135-3f9affa94733");
        ld.deleteAll("c39d53c2-3b02-4627-9d44-704a5a854391");
        ld.closeFactory();
        
    }
}
