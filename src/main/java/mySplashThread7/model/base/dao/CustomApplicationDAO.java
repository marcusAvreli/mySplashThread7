package mySplashThread7.model.base.dao;

import java.util.ArrayList;
import java.util.List;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mySplashThread7.model.base.entity.CustomApplication;




public class CustomApplicationDAO {
	private static final Logger logger = LoggerFactory.getLogger(CustomApplicationDAO.class);

	public static List<CustomApplication> getData() {
		//logger.info("get_data_start");
		List<CustomApplication> customApplications = new ArrayList<CustomApplication>();
		customApplications.add(new CustomApplication("Test_Mark",  "displayName_1"));
		customApplications.add(new CustomApplication("Jhonny", "displayName_2"));
		customApplications.add(new CustomApplication("Douglas", "displayName_3"));
		for(int i=0;i<100;i++) {
			customApplications.add(new CustomApplication("Douglas"+i, "displayName_"+i));
		}
		//logger.info("get_data_finish");
		return customApplications;
	
	
	}
}


