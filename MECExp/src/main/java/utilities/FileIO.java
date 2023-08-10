package utilities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import objs.BSDistancePair;
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
	
	
	public static void output(List<BaseStation> result, int input_size, String time, String method_name)
	{
		OutputStream fileOutputStream = null;
		PrintWriter out = null;
		
		try{
		  File file =new File("output/result.txt");
		
		  //if file doesnt exists, then create it
		  if(!file.exists()){
		   file.createNewFile();
		  }
		  
		  fileOutputStream = new FileOutputStream(file, true);
		  out = new PrintWriter(fileOutputStream);
		  
		  int totalCost = 0; 
		  for(BaseStation bs : result) 
		  {
			totalCost += Constants.COST_EN;
			int serverNum = (int)Math.ceil((bs.getWorkload()) / Constants.SINGLE_SERVER_CAPACITY);
			
			totalCost += serverNum * Constants.COST_SERVER;
		  }
		  
		  if(!Constants.isPeak) 
		  {
			  method_name = method_name + "_avg";
		  }
		  
		  out.println(method_name  + "," + Constants.DELAY_THRESH + "," 
				  + input_size + "," + result.size() + "," + totalCost + "," + time);
		  
//		  System.out.println(method_name  + "," + Constants.DELAY_THRESH + "," 
//				  + input_size + "," + result.size() + "," + totalCost + "," + time);
		 }catch(IOException e){
			 e.printStackTrace();
		 }finally 
		{
			 try {
					fileOutputStream.flush();
					out.flush();
					fileOutputStream.close();
					out.close();

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	
	public static void outputTau(ArrayList<BaseStation> result, int tau) 
	{
		//FileWriter fileWritter = null;
		BufferedWriter bufferWritter = null;
		OutputStream fileOutputStream = null;
		OutputStreamWriter outputStreamWriter = null;
		
		try{
		  File file =new File("output/result_tau.txt");
		
		  //if file doesnt exists, then create it
		  if(!file.exists()){
		   file.createNewFile();
		  }
		
		  //true = append file
//		  fileWritter = new FileWriter(file.getAbsolutePath(), true);
//		  bufferWritter = new BufferedWriter(fileWritter);
		  
		  fileOutputStream = new FileOutputStream(file, true);
		  outputStreamWriter = new OutputStreamWriter(fileOutputStream, "UTF-8");
		  bufferWritter = new BufferedWriter(outputStreamWriter);
		
		  int totalCost = 0; 
		  for(BaseStation bs : result) 
		  {
			totalCost += Constants.COST_EN;
			int serverNum = (int)Math.ceil((bs.getWorkload()) / Constants.SINGLE_SERVER_CAPACITY);
			
			totalCost += serverNum * Constants.COST_SERVER;
		  }
		  
		  bufferWritter.write(tau + "," + result.size() + "," + totalCost);
		  bufferWritter.newLine();
		
		 }catch(IOException e){
			 e.printStackTrace();
		 }finally 
		{
			 if(bufferWritter != null) 
			 {
				 try {
					outputStreamWriter.flush();
					fileOutputStream.flush();
					bufferWritter.flush();
					
					
					bufferWritter.close();
					fileOutputStream.close();
					outputStreamWriter.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 
			 }
		}
	}
	
	public static void output_mip(int cost, int en_num, int input_size,String time, String method_name) 
	{
		
		//FileWriter fileWritter = null;
		BufferedWriter bufferWritter = null;
		OutputStream fileOutputStream = null;
		OutputStreamWriter outputStreamWriter = null;
		
		try{		
		  File file =new File("output/result.txt");
		
		  //if file doesnt exists, then create it
		  if(!file.exists()){
		   file.createNewFile();
		  }
		
		  //true = append file
//		  fileWritter = new FileWriter(file.getAbsolutePath(), true);
//		  bufferWritter = new BufferedWriter(fileWritter);
		  fileOutputStream = new FileOutputStream(file, true);
		  outputStreamWriter = new OutputStreamWriter(fileOutputStream, "UTF-8");
		  bufferWritter = new BufferedWriter(outputStreamWriter);
		  
		  bufferWritter.write(method_name  + "," + Constants.DELAY_THRESH + "," 
				  + input_size + "," + en_num + "," + cost + "," + time);
		  bufferWritter.newLine();
		
		 }catch(IOException e){
			 e.printStackTrace();
		 }finally 
		{
			 if(bufferWritter != null) 
			 {
				 try {
					outputStreamWriter.flush();
					fileOutputStream.flush();
					bufferWritter.flush();
					
					
					bufferWritter.close();
					fileOutputStream.close();
					outputStreamWriter.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 
			 }
		}
	}
	
	public static void outputDistribution(ArrayList<BaseStation> result, int input_size,String method_name) 
	{
		//FileWriter fileWritter = null;
		BufferedWriter bufferWritter = null;
		OutputStream fileOutputStream = null;
		OutputStreamWriter outputStreamWriter = null;
		
		try{
		  File file =new File("output/distribution.txt");
		
		  //if file doesnt exists, then create it
		  if(!file.exists()){
		   file.createNewFile();
		  }
		
		  //true = append file
//		  fileWritter = new FileWriter(file.getAbsolutePath(), true);
//		  bufferWritter = new BufferedWriter(fileWritter);
		  fileOutputStream = new FileOutputStream(file, true);
		  outputStreamWriter = new OutputStreamWriter(fileOutputStream, "UTF-8");
		  bufferWritter = new BufferedWriter(outputStreamWriter);
		
		  int totalCost = 0; 
		  for(BaseStation bs : result) 
		  {
			  for(BSDistancePair p : bs.getAssignedBS()) 
			  {
				  double transDelay = Utils.handlePrecision(Utils.getTransDelay(p.getDistance(), p.getBS().getCTMax() * Constants.SINGLE_TASK_SIZE));
				  double comDelay = Utils.handlePrecision(Constants.DELAY_THRESH - transDelay);
				  
				  bufferWritter.write(method_name  + "," + Constants.DELAY_THRESH + "," 
						  + input_size + "," + transDelay + "," + comDelay);
				  bufferWritter.newLine();
			  }
			  
		  }
		 }catch(IOException e){
			 e.printStackTrace();
		 }finally 
		{
			 if(bufferWritter != null) 
			 {
				 try {
					outputStreamWriter.flush();
					fileOutputStream.flush();
					bufferWritter.flush();
					
					
					bufferWritter.close();
					fileOutputStream.close();
					outputStreamWriter.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 
			 }
		}
	}
	
	public static void output_cluster(ArrayList<ArrayList<BaseStation>> clusters) 
	{
		FileWriter fileWritter;
		BufferedWriter bufferWritter;
		
		try{
		  File file =new File("output/cluster.txt");
		
		  //if file doesnt exists, then create it
		  if(!file.exists()){
		   file.createNewFile();
		  }
		
		  //true = append file
		  fileWritter = new FileWriter(file.getAbsolutePath(), true);
		  bufferWritter = new BufferedWriter(fileWritter);
		  
		  for(ArrayList<BaseStation> c : clusters) 
		  {
			  for(BaseStation bs : c) 
			  {
				  bufferWritter.write(bs.getLocation() + ",");
			  }
			  bufferWritter.newLine();
		  }
		  
		  bufferWritter.flush();
		  bufferWritter.close();
		
		 }catch(IOException e){
			 e.printStackTrace();
		 }
	}
	
	public static void outputResult(ArrayList<BaseStation> result, String time, String method_name) 
	{
		OutputStream fileOutputStream = null;
		PrintWriter out = null;	
		try{
		  File file =new File("output/" + method_name + ".txt");
		
		  //if file doesnt exists, then create it
		  if(!file.exists()){
		   file.createNewFile();
		  }
		  
		  fileOutputStream = new FileOutputStream(file, true);
		  out = new PrintWriter(fileOutputStream);
		  
		  if(!Constants.isPeak) 
		  {
			  method_name = method_name + "_avg";
		  }
		  
		  out.println("-----------------------------");
		  out.println(method_name + ": " + result.size() + " items");
		
		  int totalCost = 0; 
		  for(BaseStation bs : result) 
		  {
			totalCost += Constants.COST_EN;
			int serverNum = (int)Math.ceil((bs.getWorkload()) / Constants.SINGLE_SERVER_CAPACITY);
			
			totalCost += serverNum * Constants.COST_SERVER;
		  }
		
		  out.println("Total Cost: " + totalCost);
		  out.println("Running time: " + time  + " s");
		  
		 }catch(IOException e){
			 e.printStackTrace();
		 }finally 
		{
			 try {
					fileOutputStream.flush();
					out.flush();
					fileOutputStream.close();
					out.close();

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	
//	public static void outputResult(ArrayList<BaseStation> result, String time, String method_name) 
//	{
//		//FileWriter fileWritter = null;
//		BufferedWriter bufferWritter = null;
//		OutputStream fileOutputStream = null;
//		OutputStreamWriter outputStreamWriter = null;
//		
//		try{
//		  File file =new File("output/" + method_name + ".txt");
//		
//		  //if file doesnt exists, then create it
//		  if(!file.exists()){
//		   file.createNewFile();
//		  }
//		
//		  //true = append file
////		  fileWritter = new FileWriter(file.getAbsolutePath(), true);
////		  bufferWritter = new BufferedWriter(fileWritter);
//		  fileOutputStream = new FileOutputStream(file, true);
//		  outputStreamWriter = new OutputStreamWriter(fileOutputStream, "UTF-8");
//		  bufferWritter = new BufferedWriter(outputStreamWriter);
//	  
//	  	  bufferWritter.write("-----------------------------");
//		  bufferWritter.newLine();
//		
//		  bufferWritter.write(method_name + ": " + result.size() + " items");
//		  bufferWritter.newLine();
//		
//		  int totalCost = 0; 
//		  for(BaseStation bs : result) 
//		  {
//			totalCost += Constants.COST_EN;
//			int serverNum = (int)Math.ceil((bs.getWorkload()) / Constants.SINGLE_SERVER_CAPACITY);
//			
//			totalCost += serverNum * Constants.COST_SERVER;
//			
//			//System.out.println(bs.getLocation() + " " + bs.getWorkload());
//		  }
//		
//		  bufferWritter.write("Total Cost: " + totalCost);
//		  bufferWritter.newLine();
//		  
//		  bufferWritter.write("Running time: " + time  + " s");
//		  bufferWritter.newLine();
//		
//		 }catch(IOException e){
//			 e.printStackTrace();
//		 }finally 
//		{
//			 if(bufferWritter != null) 
//			 {
//				 try {
//					outputStreamWriter.flush();
//					fileOutputStream.flush();
//					bufferWritter.flush();
//					
//					
//					bufferWritter.close();
//					fileOutputStream.close();
//					outputStreamWriter.close();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				 
//			 }
//		}
//	}
	

	

}
