package library.module;

import javafx.beans.property.SimpleStringProperty;

public class Rank 
{  
    private final SimpleStringProperty rabname; 
    private final SimpleStringProperty raisbn; 
    private final SimpleStringProperty ratotal;
   
    public Rank(String bt, String bs, String rt) 
    {
    	this.rabname = new SimpleStringProperty(bt);
        this.raisbn = new SimpleStringProperty(bs);
        this.ratotal = new SimpleStringProperty(rt);
    }  
   
    public String getRabname() {  
        return rabname.get();  
    }  
    public void setRabname(String fName) {  
    	rabname.set(fName);  
    }
    
    public String getRaisbn() {  
        return raisbn.get();  
    }  
    public void setRaisbn(String fName) {  
    	raisbn.set(fName);  
    }
          
    public String getRatotal() {  
        return ratotal.get();  
    }  
    public void setRatotal(String fName) {  
    	ratotal.set(fName);  
    }
}  