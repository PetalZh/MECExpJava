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
	
	public static void writeText(String method_name, String text) 
	{
		FileWriter fileWritter;
		BufferedWriter bufferWritter;
		
		try{
		  File file =new File("output/" + method_name + ".txt");
		
		  //if file doesnt exists, then create it
		  if(!file.exists()){
		   file.createNewFile();
		  }
		
		  //true = append file
		  fileWritter = new FileWriter(file.getAbsolutePath(), true);
		  bufferWritter = new BufferedWriter(fileWritter);
		  
		  bufferWritter.write("-----------------------------");
		  bufferWritter.newLine();
		  
		  bufferWritter.write(text);
		  bufferWritter.newLine();
	  
	  	  bufferWritter.write("-----------------------------");
		  bufferWritter.newLine();
		  
		  bufferWritter.flush();
		  bufferWritter.close();
		
		 }catch(IOException e){
			 e.printStackTrace();
		 }
	}
	
	
	public static void output(ArrayList<BaseStation> result, int input_size,String time, String method_name) 
	{
		FileWriter fileWritter;
		BufferedWriter bufferWritter;
		
		try{
		  File file =new File("output/result.txt");
		
		  //if file doesnt exists, then create it
		  if(!file.exists()){
		   file.createNewFile();
		  }
		
		  //true = append file
		  fileWritter = new FileWriter(file.getAbsolutePath(), true);
		  bufferWritter = new BufferedWriter(fileWritter);
		
		  int totalCost = 0; 
		  for(BaseStation bs : result) 
		  {
			totalCost += Constants.COST_EN;
			int serverNum = (int)Math.ceil((bs.getWorkload()) / Constants.SINGLE_SERVER_CAPACITY);
			
			totalCost += serverNum * Constants.COST_SERVER;
		  }
		  
		  bufferWritter.write(method_name  + "," + Constants.DELAY_THRESH + "," 
				  + input_size + "," + result.size() + "," + totalCost + "," + time);
		  bufferWritter.newLine();
		  
		  
		  bufferWritter.flush();
		  bufferWritter.close();
		
		 }catch(IOException e){
			 e.printStackTrace();
		 }
	}
	
	public static void outputResult(ArrayList<BaseStation> result, String time, String method_name) 
	{
		FileWriter fileWritter;
		BufferedWriter bufferWritter;
		
		try{
		  File file =new File("output/" + method_name + ".txt");
		
		  //if file doesnt exists, then create it
		  if(!file.exists()){
		   file.createNewFile();
		  }
		
		  //true = append file
		  fileWritter = new FileWriter(file.getAbsolutePath(), true);
		  bufferWritter = new BufferedWriter(fileWritter);
		  
	  
	  	  bufferWritter.write("-----------------------------");
		  bufferWritter.newLine();
		
		  bufferWritter.write(method_name + ": " + result.size() + " items");
		  bufferWritter.newLine();
		
		  int totalCost = 0; 
		  for(BaseStation bs : result) 
		  {
			totalCost += Constants.COST_EN;
			int serverNum = (int)Math.ceil((bs.getWorkload()) / Constants.SINGLE_SERVER_CAPACITY);
			
			totalCost += serverNum * Constants.COST_SERVER;
			
			//System.out.println(bs.getLocation() + " " + bs.getWorkload());
		  }
		
		  bufferWritter.write("Total Cost: " + totalCost);
		  bufferWritter.newLine();
		  
		  bufferWritter.write("Running time: " + time  + " s");
		  bufferWritter.newLine();
		  
		  bufferWritter.flush();
		  bufferWritter.close();
		
		 }catch(IOException e){
			 e.printStackTrace();
		 }
	}
	
	private void writeResult() 
	{
		
	}
	

}
