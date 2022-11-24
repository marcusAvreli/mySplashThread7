package mySplashThread7.events;

public class OnLoadPaneView extends Event {

    
    
    public OnLoadPaneView(Object source) {
        super(source,Event.TYPE_LOAD_PANE_VIEW);
    
        
    }

    

    @Override
    public boolean isUiUpdateTrigger() {
        return true;
    }


}