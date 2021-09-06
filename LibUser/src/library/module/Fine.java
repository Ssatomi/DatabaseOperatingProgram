package library.module;

import javafx.beans.property.SimpleStringProperty;

public class Fine 
{  
    private final SimpleStringProperty fineNum; 
    private final SimpleStringProperty fine; 
    private final SimpleStringProperty sake;
    private final SimpleStringProperty fstate;
    private final SimpleStringProperty fbnum;
    private final SimpleStringProperty fbname;
    private final SimpleStringProperty fbtime;
    private final SimpleStringProperty frtime;
   
    public Fine(String fnum, String fin, String sak,String st,String bnum,String bname,String bt,String rt) 
    {
    	this.fineNum = new SimpleStringProperty(fnum);
        this.fine = new SimpleStringProperty(fin);
        this.sake = new SimpleStringProperty(sak);
        this.fstate = new SimpleStringProperty(st);  
        this.fbnum = new SimpleStringProperty(bnum);
        this.fbname = new SimpleStringProperty(bname);
        this.fbtime = new SimpleStringProperty(bt);  
        this.frtime = new SimpleStringProperty(rt);
    }  
   
    public String getFineNum() {
        return fineNum.get();  
    }  
    public void setFineNum(String fName) {
    	fineNum.set(fName);  
    }
    
    public String getFine() {  
        return fine.get();  
    }  
    public void setFine(String fName) {
    	fine.set(fName);  
    }
          
    public String getSake(){  
        return sake.get();  
    }  
    public void setSake(String fName) {
    	sake.set(fName);  
    }  
      
    public String getFstate() {  
        return fstate.get();  
    }  
    public void setFstate(String fName) {
    	fstate.set(fName);  
    }
    
    public String getFbnum() {  
        return fbnum.get();  
    }  
    public void setFbnum(String fName) {
    	fbnum.set(fName);  
    }
    
    public String getFbname(){  
        return fbname.get();  
    }  
    public void setFbname(String fName) {
    	fbname.set(fName);  
    }  
      
    public String getFbtime() {  
        return fbtime.get();  
    }  
    public void setFbtime(String fName) {
    	fbtime.set(fName);  
    }
    
    public String getFrtime() {  
        return frtime.get();  
    }  
    public void setFrtime(String fName) {
    	frtime.set(fName);  
    }   
}