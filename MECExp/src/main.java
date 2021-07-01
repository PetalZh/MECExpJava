import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Hashtable;

import objs.BaseStation;
import objs.UserRequest;
import utilities.utils;

public class main {
	public static void main(String args[]) {
		Hashtable<String, ArrayList<UserRequest>> BSTable = readDoc();
		getBSList(BSTable);
	}
	
	private static Hashtable<String, ArrayList<UserRequest>> readDoc() 
	{
		BufferedReader reader = null;
		FileInputStream inputStream = null;
		Hashtable<String, ArrayList<UserRequest>> BSTable = new Hashtable<>();
		try {
			inputStream = new FileInputStream("shanghai15.csv");
			reader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
			String line = null;
			reader.readLine();//skip title line
			int count = 0;
			while((line=reader.readLine())!=null){
			    String item[] = line.split(",");
			    if(utils.timeFormater(item[2]) == null || utils.timeFormater(item[3]) == null || item[4].equals("")) {
			    	continue;
			    }
			    
			    UserRequest request = new UserRequest(utils.timeFormater(item[2]), utils.timeFormater(item[3]), item[5], item[4]);
			    double duration = (utils.timeFormater(item[3]).getTime() - utils.timeFormater(item[2]).getTime())/ (1000 * 60);
			    request.setDuration(duration);
			    //System.out.println(utils.timeFormater(item[2]).getTime() + " " + utils.timeFormater(item[3]).getTime()+ " " +duration);
			    
			    //request list to station
			    if(BSTable.containsKey(request.getLocation())) {
			    	BSTable.get(request.getLocation()).add(request);
			    }else {
			    	ArrayList<UserRequest> reqList = new ArrayList<>();
			    	reqList.add(request);
			    	BSTable.put(request.getLocation(), reqList);
			    }
			    
			    
			    // item loaded
//			    count ++;
//			    if(count == 1000) 
//			    {
//			    	break;
//			    }
			  }
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		return BSTable;
	}
	
	private static ArrayList<BaseStation> getBSList(Hashtable<String, ArrayList<UserRequest>> BStable){
		ArrayList<BaseStation> bsList = new ArrayList<>();
	
		for(String key: BStable.keySet()) {
			BaseStation bs = new BaseStation(key);

			ArrayList<UserRequest> requestList= BStable.get(key);
			bs.setCTMax(utils.getCTMax(requestList));
			bsList.add(bs);
			
			System.out.println(key + ": " + bs.getCTMax());
		}
		
		System.out.println("BS #"+ ": " + bsList.size());
		
		return bsList;
	}
	
}
