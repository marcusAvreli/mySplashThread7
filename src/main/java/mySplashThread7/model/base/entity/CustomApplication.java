package mySplashThread7.model.base.entity;

import java.io.Serializable;

public class CustomApplication implements Serializable, Comparable<CustomApplication>, Cloneable {
	private String name;
	private String displayName;
	

	
	public CustomApplication() {
		
	}
	
	public CustomApplication(String name,String displayName) {
		this.name = name;
		this.displayName = displayName;
	}
	
	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	

	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int compareTo(CustomApplication arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
}
