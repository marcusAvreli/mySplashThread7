package mySplashThread7.settings;

import java.io.File;
import java.io.FileInputStream;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mySplashThread7.events.Event;
import mySplashThread7.events.EventBroker;
import mySplashThread7.events.EventConsumer;
import mySplashThread7.nepxion.util.locale.LocaleConstants;
import mySplashThread7.nepxion.util.locale.LocaleContext;


public class ClientSettings implements EventConsumer {
	private static final Logger logger = LoggerFactory.getLogger(ClientSettings.class);
	  public static final String CONF_LASTSERVER="connection.lastserver";
	    public static final String CONF_LASTSERVERLIST="connection.lastserverlist";
	    public static final String CONF_LASTPORT="connection.lastport";
	    // in case of SSH tunneling, the client determines an available port automatically, which must not be saved for future connections --> dedicated property
	    public static final String CONF_LASTPORTDYN="connection.lastportdyn";
	    public static final String CONF_LASTUSER="connection.lastuser";
	    public static final String CONF_LASTSSHPWD="connection.lastsshpwd";
	    public static final String CONF_LASTSSHHOST="connection.lastsshhost";
	    public static final String CONF_THEME="client.theme";
	    public static final String CONF_UI_FONTSIZEOFFSET="client.ui.fontsizeoffset";
	    public static final String CONF_HEIGHT="client.height";
	    public static final String CONF_WIDTH="client.width";
	 // name of the connection profile that was last used successfully
	    public static final String CONF_LASTCONNECTION="connection.lastconnection";
	    // deprecated! use LASTSECMODE instead
	    
	    public static final String CONF_LASTSERVERSSL="connection.lastserverssl";
	    public static final String CONF_LASTSECMODE="connection.lastsecmode";
	    ResourceBundle bundleManager = null;
	    private  String tokenUrl;
	    private  String baseUrl;
	    private String  secret;
	    private String token;
	    private int numberOfRowsPerPage;
	    public int getNumberOfRowsPerPage() {
			return numberOfRowsPerPage;
		}

		public void setNumberOfRowsPerPage() {
			String strNumber = (String)clientConfiguration.get("numberOfRowsPerPage");
			if(null != strNumber && !strNumber.isEmpty()) {
				Integer localNumber = Integer.valueOf( strNumber);
		    	//logger.info("localNumber:"+localNumber);
		    	if(null != localNumber) {
		    		this.numberOfRowsPerPage = localNumber;
		    	}
			}else {
				this.numberOfRowsPerPage = 5;
			}
	    	//logger.info("localNumber:"+numberOfRowsPerPage);
		}

		public String getToken() {
			return token;
		}

		public void setToken(String token) {
			logger.info("setToken:"+token);
			this.token = token;
		}


	//	public ModuleMetadata rootModule;
	    

	
	    
	

	

	public String translateLabel (String key,Object[] inputArgs) {
		String translatedLabel = bundleManager.getString(key);
		String resultString = bundleManager.getString(key);
		if(null != inputArgs) {
			
        	MessageFormat form = new MessageFormat(translatedLabel);
        	 resultString = form.format(inputArgs);
		}
	return resultString;
	}



	 private Properties clientConfiguration=null;
	    private static ClientSettings instance=null;

	  public ClientSettings() {
		  EventBroker b = EventBroker.getInstance();
          
        //  b.subscribeConsumer(this, Event.TYPE_TOKEN_RECEIVED);
	        this.clientConfiguration=new Properties();
	        String clientConfFileLocation=System.getProperty("user.home") + System.getProperty("file.separator") + ".j-lawyer-client" + System.getProperty("file.separator") + "clientConfiguration.properties";
	        logger.info("client configuration will be created in:"+clientConfFileLocation);
	        File clientConfFile=new File(clientConfFileLocation);
	        if(!clientConfFile.exists()) {
	            try {
	                clientConfFile.createNewFile();
	            } catch (Exception ex) {
	                logger.error("Could not create new client configuration file", ex);
	            }
	        }
	        try (FileInputStream fis=new FileInputStream(clientConfFile)) {
	            this.clientConfiguration.load(fis);	            
	        } catch (Exception ex) {
	            logger.error("Could not load client configuration file", ex);
	        }
	        setNumberOfRowsPerPage();
	    }
	  
	  public static synchronized ClientSettings getInstance() {
	        if(instance==null) {
	            instance=new ClientSettings();
	        }
	        return instance;
	    }
	  public String getConfiguration(String key, String defaultValue) {
	        String value=this.clientConfiguration.getProperty(key);
	        if(value==null)
	            value=defaultValue;
	        return value;
	    }
	  
	  
	    public void setConfiguration(String key, String value) {
	        this.clientConfiguration.setProperty(key, value);
	    }
	    
		public String getTokenUrl() {
			return tokenUrl;
		}

		public void setTokenUrl() {
			this.tokenUrl = getBaseUrl()+"/oauth2/generateToken";
		}

		public String getBaseUrl() {
			return baseUrl;
		}

		public void setBaseUrl(String baseUrl) {
			this.baseUrl = baseUrl;
		}

		public String getSecret() {
			return secret;
		}

		public void setSecret(String secret) {
			this.secret = secret;
		}
		

			@Override
			public void onEvent(Event e) {
				// TODO Auto-generated method stub
				//if(e instanceof TokenReceivedEvent) {
					Object token = e.getPayload();
					
					//setToken(String.valueOf(token));
				//}
			}
	    public void registerLocale() {
	    	String localeStr = (String) clientConfiguration.get("locale");
	    //	logger.info("locale:"+localeStr);
	    	if(null != localeStr && !localeStr.isEmpty()) {
	    		String localeId=localeStr.split("_")[0];
	    		String countryId = localeStr.split("_")[1];
	    		Locale locale = new Locale(localeId,countryId);
	    	//	logger.info("setting locale");
	    		LocaleContext.registerLocale(locale);
	    	}
	    }
	    public Locale getLocale() {
	    	return LocaleContext.getLocale();
	    }
	    
}
