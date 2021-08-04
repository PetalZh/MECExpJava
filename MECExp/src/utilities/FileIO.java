package utilities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import objs.BaseStation;

public class FileIO {
	
	public static void writeToFile(ArrayList<BaseStation> bsList, String fileName) 
	{
		FileWriter fileWritter;
		BufferedWriter bufferWritter;
		
		try{
	      String data = " This content will append to the end of the file";

	      File file =new File(fileName);

	      //if file doesnt exists, then create it
	      if(!file.exists()){
	       file.createNewFile();
	      }

	      //true = append file
	      fileWritter = new FileWriter(file.getName(), false);
	      bufferWritter = new BufferedWriter(fileWritter);
	      
	      for(BaseStation bs: bsList) 
	      {
	    	  bufferWritter.write(bs.getLocation());
		      bufferWritter.newLine();
	      }
//	      bufferWritter.write(data);
//	      bufferWritter.newLine();
	      
	      
	      bufferWritter.flush();
	      bufferWritter.close();
	      System.out.println("Done");

	     }catch(IOException e){
	    	 e.printStackTrace();
	     }
	}
	
	

}
