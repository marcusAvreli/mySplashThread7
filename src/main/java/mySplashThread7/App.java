package mySplashThread7;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mySplashThread7.gui.MainFrame;
import mySplashThread7.settings.ClientSettings;



//https://github.com/NCIP/cab2b
//https://github.com/Nepxion/Marvel
//https://www.codeproject.com/Articles/36170/Simple-and-Powerful-TableModel-with-Reflection
//j-lawyer-org/j-lawyer-server-entities/src/java/com/jdimension/jlawyer/persistence/AppOptionGroupBean.java
//https://www.oracle.com/technical-resources/articles/javase/locale.html
//https://github.com/semantic-web-software/dynagent/tree/master/Elecom
///https://github.com/semantic-web-software/dynagent/blob/a0d356169ef34f3d2422e235fed7866e3dda6d8a/Elecom/src/gdev/gawt/GTable.java
public class App {
	private static final Logger logger = LoggerFactory.getLogger(App.class);

	public static void main(String[] args) {		
		System.setProperty("http.agent", "j-lawyer Client v16");
		App main = new App();
        ClientSettings settings = ClientSettings.getInstance();
        settings.registerLocale();
		main.showSplash(settings);
	}

	private void showSplash(ClientSettings settings) {		
		new Thread(new SplashThread(new MainFrame(settings))).start();
	}
}
