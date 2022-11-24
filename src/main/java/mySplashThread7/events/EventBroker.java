package mySplashThread7.events;

import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.SwingUtilities;

import org.slf4j.LoggerFactory;


import org.slf4j.Logger;


/**
 *
 * @author jens
 */
public class EventBroker {

    private static EventBroker instance = null;

    private static final Logger logger = LoggerFactory.getLogger(EventBroker.class);
    
    private HashMap<Integer, ArrayList<EventConsumer>> consumerMap = new HashMap<Integer, ArrayList<EventConsumer>>();

    public static synchronized EventBroker getInstance() {

        if (instance == null) {
            instance = new EventBroker();
        }

        return instance;

    }

    public void subscribeConsumer(EventConsumer consumer, Integer eventType) {
        if (!consumerMap.containsKey(eventType)) {
            consumerMap.put(eventType, new ArrayList<EventConsumer>());
        }
        ArrayList<EventConsumer> consumers = consumerMap.get(eventType);
        
        consumers.add(consumer);
    }
    
    public void unSubscribeConsumer(EventConsumer consumer, Integer eventType) {
        if (!consumerMap.containsKey(eventType)) {
            consumerMap.put(eventType, new ArrayList<EventConsumer>());
        }
        ArrayList<EventConsumer> consumers = consumerMap.get(eventType);
       
        consumers.remove(consumer);
    }

    public void publishEvent(Event e) {
        
        
        if (consumerMap.containsKey(e.getType())) {
            ArrayList<EventConsumer> consumers = consumerMap.get(e.getType());
          
            for (EventConsumer c : consumers) {           	
                if (e.isUiUpdateTrigger()) {
                    SwingUtilities.invokeLater(
                        new Runnable() {

                        public void run() {
                            c.onEvent(e);
                        }
                    });
                } else {
                    c.onEvent(e);
                }
            }
        }
    }

}