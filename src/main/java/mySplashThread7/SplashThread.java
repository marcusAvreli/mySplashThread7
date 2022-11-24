package mySplashThread7;

import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class SplashThread implements Runnable {
	
	
	private static final Logger logger = LoggerFactory.getLogger(SplashThread.class);

    private int loadedMods = 0;
    private int numberOfMods = 0;
    private JFrame owner = null;
	   

	 public SplashThread(JFrame owner) {       
        this.owner = owner;
	 }
	public void run() {       
	   SwingUtilities.invokeLater(() -> {
		 
		  // owner.setTitle("Test");	           
		   owner.pack();
		   owner.doLayout();
		   owner.setVisible(true);
	       
	   });		
	}
	
	
}
