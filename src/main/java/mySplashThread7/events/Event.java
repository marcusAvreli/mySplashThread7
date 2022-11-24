package mySplashThread7.events;

import java.util.EventObject;

/**
*
* @author jens
*/
public abstract class Event extends EventObject{
   
   public static final Integer TYPE_DEFAULT=0;
   public static final Integer TYPE_AUTOUPDATE=10;
   public static final Integer TYPE_NEWS=20;
   public static final Integer TYPE_SYSTEMSTATUS=30;
   public static final Integer TYPE_SCANNERSTATUS=40;
   public static final Integer TYPE_FAXSTATUS=50;
   public static final Integer TYPE_FAXFAILED=60;
   public static final Integer TYPE_MAILSTATUS=70;
   public static final Integer TYPE_BEASTATUS=80;
   public static final Integer TYPE_DREBISSTATUS=90;
   public static final Integer TYPE_ALLCASETAGS=100;
   public static final Integer TYPE_ALLDOCUMENTTAGS=100;
   public static final Integer TYPE_DOCUMENTADDED=110;
   public static final Integer TYPE_CONTACTUPDATED=120;
   public static final Integer TYPE_REVIEWADDED=130;
   public static final Integer TYPE_TOKEN_RECEIVED=131;
   public static final Integer TYPE_CUSTOM_APPLICATION_GET_ALL=132;
   public static final Integer  TYPE_LOAD_PANE_VIEW=133;
   
   
   
   //data grid events   
   public static final Integer EVENT_ID_SELECTIONCHANGED = 134;
   public static final Integer EVENT_ID_DOUBLECLICKED = 135;
   public static final Integer TYPE_UPDATE_FINDINGS=136;
  // public static final Integer EVENT_ID_FILTERED_ITEM=137;
   
   private Integer type=TYPE_DEFAULT;
   private Object payload=null;
   
   public Event(Object source,Integer type) {
	   super(source);
       this.type=type;
       
   }
   
   public Event(Object source,Integer type, Object payload) {
	   super(source);
       this.type=type;
       this.payload=payload;
       
   }
   
   public abstract boolean isUiUpdateTrigger();

   /**
    * @return the type
    */
   public Integer getType() {
       return type;
   }

   /**
    * @param type the type to set
    */
   public void setType(Integer type) {
       this.type = type;
   }

   /**
    * @return the payload
    */
   public Object getPayload() {
       return payload;
   }

   /**
    * @param payload the payload to set
    */
   public void setPayload(Object payload) {
       this.payload = payload;
   }
   
}