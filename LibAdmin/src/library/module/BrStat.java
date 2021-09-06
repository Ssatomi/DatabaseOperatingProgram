package library.module;

import javafx.beans.property.SimpleStringProperty;

public class BrStat 
{  
    private final SimpleStringProperty btotal; 
    private final SimpleStringProperty bsucc; 
    private final SimpleStringProperty rtotal;
    private final SimpleStringProperty rsucc;
   
    public BrStat(String bt, String bs, String rt,String rs) 
    {
    	this.btotal = new SimpleStringProperty(bt);
        this.bsucc = new SimpleStringProperty(bs);
        this.rtotal = new SimpleStringProperty(rt);
        this.rsucc = new SimpleStringProperty(rs);
    }  
   
    public String getBtotal() {  
        return btotal.get();  
    }  
    public void setBtotal(String fName) {  
    	btotal.set(fName);  
    }
    
    public String getBsucc() {  
        return bsucc.get();  
    }  
    public void setBsucc(String fName) {  
    	bsucc.set(fName);  
    }
          
    public String getRtotal() {  
        return rtotal.get();  
    }  
    public void setRtotal(String fName) {  
    	rtotal.set(fName);  
    }  
      
    public String getRsucc() {  
        return rsucc.get();  
    }  
    public void setRsucc(String fName) {  
    	rsucc.set(fName);  
    }
}  