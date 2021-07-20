package algorithms;
import java.util.ArrayList;

import ilog.concert.*;
import ilog.cplex.*;
import objs.BaseStation;
import utilities.Constants;
import utilities.Utils;

public class MIPAlgo {

	public MIPAlgo() {
		super();
	}
	
	public void getMIP(ArrayList<BaseStation> bsList) 
	{
		try {
			
			int n = bsList.size();
			IloCplex cplex = new IloCplex();
			
//			// whether BS is selected as EN
//			IloNumVar[] c = new IloNumVar[n];
//			for(int i = 0; i < c.length; i++) 
//			{
//				c[i] = cplex.boolVar();
//			}
			
			// assignment metrix
			IloNumVar[][] x = new IloNumVar[n][n];
			for(int i = 0; i < x.length; i++) 
			{
				x[i] = cplex.boolVarArray(n);
			}
			
			// objective
			//IloLinearNumExpr objective = cplex.linearNumExpr();
			IloNumExpr objective = cplex.numExpr();
			
			for(int i = 0; i < n; i++) 
			{
				IloNumExpr comp_capacity = cplex.numExpr();
				boolean isEN = false;
				
				for(int j = 0; j < n; j++) 
				{
//					if(x[j][i].getLB() == 1) 
//					{
//						isEN = true;
//					}
					double comp_capacity_req = bsList.get(j).getCTMax() * Constants.SINGLE_TASK_SIZE;//getCapacityReq(bsList.get(i), bsList.get(j)); 
					comp_capacity = cplex.sum(comp_capacity, cplex.prod(comp_capacity_req, x[j][i]));
				}
				IloNumExpr server_cost = cplex.prod(cplex.prod(1/Constants.SINGLE_SERVER_CAPACITY, comp_capacity), Constants.COST_SERVER);
				objective = cplex.sum(objective, server_cost);
//				if(isEN == true) 
//				{
//					objective = cplex.sum((double)Constants.COST_EN, objective);
//				}
					
			}
			
			cplex.addMinimize(objective);
			
			// constraints
			
			// only connect to one server constraint
			
			for(int i = 0; i < n; i++) 
			{
				IloNumExpr expr1 = cplex.numExpr();
				for(int j = 0;  j < n; j++) 
				{
					if(i != j) 
					{
						expr1 = cplex.sum(x[i][j], expr1);
					}
				}
				cplex.addEq(expr1, 1.0);
			}
//			
//			
//			// cover all constraint
//			
			IloNumExpr expr2 = cplex.numExpr();
			for(int i = 0; i < n; i++) 
			{
				
				for(int j = 0;  j < n; j++) 
				{
					if(i != j) 
					{
						expr2 = cplex.sum(x[i][j], expr2);
						
					}
				}
			}
			cplex.addEq(expr2, n);
			
			// delay constraint
			for(int i = 0; i < n; i++) 
			{
				for(int j = 0; j < n; n++) 
				{
					if(i != j) 
					{
						IloLinearNumExpr constant3 = cplex.linearNumExpr();
						double distance = Utils.getDistance(bsList.get(i).getLocation(), bsList.get(j).getLocation());
						double trans_delay = Utils.getTransDelay(distance, bsList.get(j).getCTMax() * Constants.SINGLE_TASK_SIZE);
						
						cplex.addLe(constant3, Constants.DELAY_THRESH);
					}
				}
			}
			
			if(cplex.solve()) 
			{
				System.out.println("Solved");
				System.out.println(cplex.getObjValue());
				
				for(int i = 0; i < n; i++) 
				{
					for(int j = 0; j < n; j++) 
					{
						System.out.println(cplex.getValue(x[i][j]));
					}
				}
			}else 
			{
				System.out.println("Fail");
			}
			//System.out.println(cplex.getValue(objective));
			
			//cplex.end();
			
		} catch (IloException e) {
			e.printStackTrace();
		}
		
	}
	
	
	private double getCapacityReq(BaseStation s, BaseStation b) 
	{
		int task_size = b.getCTMax() * Constants.SINGLE_TASK_SIZE;
		double distance = Utils.getDistance(s.getLocation(), b.getLocation());
		return Utils.getCapacityRequired(distance, task_size);
	}
	

}
