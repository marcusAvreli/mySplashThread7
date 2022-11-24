package mySplashThread7.events;

public class UpdateFindings extends Event {

    
    
    public UpdateFindings(Object source,Integer type, Object payload) {
        super(source,Event.TYPE_UPDATE_FINDINGS,payload);
    
        
    }

    

    @Override
    public boolean isUiUpdateTrigger() {
        return true;
    }


}