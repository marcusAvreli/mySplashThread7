package mySplashThread7.gui;

import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.table.TableModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mySplashThread7.DemoPaginationBar;
import mySplashThread7.MagTable;
import mySplashThread7.gdev.gawt.GTableModelReduction;
import mySplashThread7.model.base.dao.CustomApplicationDAO;
import mySplashThread7.model.base.entity.CustomApplication;
import mySplashThread7.modulebar.ModuleButton;
import mySplashThread7.nepxion.swing.locale.SwingLocale;
import mySplashThread7.nepxion.swing.pagination.PaginationContext;
import mySplashThread7.settings.ClientSettings;




public class MainFrame extends JFrame  {

	private static final Logger logger = LoggerFactory.getLogger(MainFrame.class);
	private JPanel mainPanel;
	private JMenu fileMenu;
	private JMenu helpMenu;
	private JMenuBar mainMenuBar;
	private JMenuItem exitMenuItem;
	//private StateObserverMenuItem affiliationsManagerMenuItem;

	private JMenuItem loginMenuItem;
	private JMenuItem settingsMenuItem;
    public MainFrame(ClientSettings settings) {
		logger.info("start_create_JKanzleiGUI");
		  // set frame site
        setMinimumSize(new Dimension(800, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       
		initComponents();
		add(this.jPanel1);		
		 applyComponentOrientation(ComponentOrientation
	                .getOrientation(settings.getLocale()));
		logger.info("finish_create_JKanzleiGUI");
    }
    
    private void initComponents() {
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	mainPanel = new JPanel();
		fileMenu = new JMenu();
		helpMenu = new JMenu();
		mainMenuBar = new JMenuBar();
		exitMenuItem = new JMenuItem();
		loginMenuItem = new JMenuItem(); 
		settingsMenuItem = new JMenuItem();
 		
		
 		




		loginMenuItem.addActionListener(new ActionListener() {
       	 public void actionPerformed(ActionEvent evt) {
               // loginMenuItemActionPerformed(evt);
              }

       	
       });
        
        
       
        
      
        
        exitMenuItem.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
        	  exitMenuItemActionPerformed(evt);
          }
        });
        
        
        settingsMenuItem.addActionListener(new ActionListener() {
        	 public void actionPerformed(ActionEvent evt) {
        		 settingsMenuItemActionPerformed(evt);
             }
        });
        
        loginMenuItem.setText(SwingLocale.getString("Login"));
       // affiliationsManagerMenuItem.setText("Custom Applications manager");        
        exitMenuItem.setText(SwingLocale.getString("Exit"));
        fileMenu.add(loginMenuItem);
        //fileMenu.add(affiliationsManagerMenuItem);
        fileMenu.add(exitMenuItem);
        
        settingsMenuItem.setText(SwingLocale.getString("Settings"));
        helpMenu.add(settingsMenuItem);
        
        fileMenu.setText(SwingLocale.getString("File"));
        helpMenu.setText(SwingLocale.getString("Help"));
        mainMenuBar.add(fileMenu);
        mainMenuBar.add(helpMenu);
        setJMenuBar(mainMenuBar);
    	
    	
    	jPanel1 = new ModuleButton();
    	
    	
    	
    	
    	
    	
	    jPanel1.actionPerformed();
    	
        pack();
    }
  
	private void exitMenuItemActionPerformed(ActionEvent evt) {
		finish();
	}
	private void  settingsMenuItemActionPerformed(ActionEvent evt) {
		
	}
		
	private void finish() {
		dispose();
		logger.info("Finish");
		System.exit(0);
	}      
	
    private ModuleButton jPanel1;
   
	
}