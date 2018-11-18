/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package listing;

/**
 *
 * @author root
 */
public class Driver {
    public static void main(String[] args) {
        ListingBean lb = new ListingBean();
        ListingDao ld = new ListingDao();
        System.out.println(ld.readListing("uid", "2"));
    }
}
