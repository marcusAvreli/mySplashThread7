package mySplashThread7.dynagent.common.utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Auxiliar {
	  public static boolean equals(Object valA , Object valB)
	   {   boolean result = false;
	  
	   	if(valA instanceof Integer[] && valB instanceof Integer[]){
		   valA= Arrays.asList((Integer[])valA);
		   Collections.sort((List)valA);
		   valB= Arrays.asList((Integer[])valB);
		   Collections.sort((List)valB);
	   	}
	   
	   if (valA == null && valB != null)
			return false;
		if (valA != null && valB == null)
			return false;
		if (valA == null && valB == null)
			return true;
	   
		if(valA.equals(valB))
			return true;		   		  
		   return result;
	   }
	  public static boolean hasDoubleValue(String value){
			try{
				new Double(value);
				return true;
			}
			catch(Exception ex){
				return false;
			}
		}
}
