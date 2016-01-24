/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.ipsen3.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import nl.ipsen3.ApiConfiguration;
import nl.ipsen3.database.Database;
import nl.ipsen3.model.Offer;


/**
 *
 * @author Jamie
 */
public class OfferDAO {
    
    private final List<Offer> offers;
    private final Database databaseInstance;
    
    public OfferDAO() {
        this.databaseInstance = Database.getInstance();
        this.offers = getAllFromDatabase();
    }
    
    public List<Offer> getAll() {
          return this.offers;
    }
    
    public Offer get(int id) {
        try {
            for(Offer offer : offers) {
                if(offer.getId() == id) {
                    return offer;
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
        
        return null;
    }
    
    public void add(Offer offer){
        offer = this.addOfferToDatabase(offer);
        offers.add(offer);
    }
    
    public void update(int id, Offer offer){
        Offer oldOffer = get(id);
        oldOffer.setId(id);
        
        this.updateOfferFromDatabase(offer);
        int idInList = offers.indexOf(offer);
        offers.set(idInList, oldOffer);
    }
    
    public void delete(int id) {
        Offer offer = get(id);
        this.removeOfferFromDatabase(offer);
        offers.remove(offer);
    }
    
    private List<Offer> getAllFromDatabase() {
        List<Offer> offerList = new ArrayList<>();
        ResultSet results = databaseInstance.select("offer");
        
        try {
            while(results.next()) {
                Offer offer = new Offer();
                offer.setId(results.getInt("id"));
                offer.setName(results.getString("name"));
                offer.setStartDate(results.getDate("start_date"));
                offer.setEndDate(results.getDate("end_date"));
                
                offerList.add(offer);  
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        
        return offerList;
    }
    
    private Offer addOfferToDatabase(Offer offer){
        HashMap databaseData = new HashMap();
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        databaseData.put("name", offer.getName());
        databaseData.put("start_date", sdf.format(offer.getStartDate()));
        databaseData.put("end_date", sdf.format(offer.getEndDate()));
        
        
        int id = databaseInstance.insertInto("offer", databaseData);
        offer.setId(id);
        
        return offer;
    }
    
    private void removeOfferFromDatabase(Offer offer) {
        databaseInstance.delete("offer", offer.getId());
    }
    
    private void updateOfferFromDatabase(Offer offer){
        HashMap databaseData = new HashMap();
        
        databaseData.put("id", offer.getId());
        databaseData.put("name", offer.getName());
        databaseData.put("start_date", offer.getStartDate());
        databaseData.put("end_date", offer.getEndDate());
          
        databaseInstance.update("offer", offer.getId(), databaseData);
    }
    
}
