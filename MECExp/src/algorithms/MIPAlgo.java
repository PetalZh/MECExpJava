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
	
	public void test() 
	{
		try {
			IloCplex cplex = new IloCplex();
			IloNumVar x = cplex.numVar(0,  Double.MAX_VALUE);
			IloNumVar y = cplex.numVar(0, Double.MAX_VALUE);
			
			// obj
			IloLinearNumExpr obj = cplex.linearNumExpr();
			obj.addTerm(0.12, x);
			obj.addTerm(0.15, y);
			
			cplex.addMinimize(obj);
			
			// constraints
			
			cplex.addGe(cplex.sum(cplex.prod(60, x), cplex.prod(60, y)), 300);
			cplex.addGe(cplex.sum(cplex.prod(12, x), cplex.prod(6, y)), 36);
			cplex.addGe(cplex.sum(cplex.prod(10, x), cplex.prod(30, y)), 90);
			
			if(cplex.solve()) 
			{
				System.out.println("Obj: " + cplex.getObjValue());
				System.out.println("x: " + cplex.getValue(x));
				System.out.println("y: " + cplex.getValue(y));
			}
			
		} catch (IloException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void getMIP(ArrayList<BaseStation> bsList) 
	{
		try {
			
			int n = bsList.size();
			IloCplex cplex = new IloCplex();
			
//			// whether BS is selected as EN
			IloNumVar[] y = new IloNumVar[n];
			for(int i = 0; i < y.length; i++) 
			{
				y[i] = cplex.boolVar();
			}
			
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
				objective = cplex.sum(objective, cplex.prod(Constants.COST_EN, y[i]));
				IloNumExpr comp_capacity = cplex.numExpr();
				
				cplex.sum(comp_capacity, getSelfCapacityReq(bsList.get(i)));
				
				for(int j = 0; j < n; j++) 
				{
					if(i != j) 
					{
						double comp_capacity_req = getCapacityReq(bsList.get(i), bsList.get(j)); 
						comp_capacity = cplex.sum(comp_capacity, cplex.prod(comp_capacity_req, x[i][j]));
					}
				}
				
				IloNumExpr server_cost = cplex.prod(cplex.prod(1/Constants.SINGLE_SERVER_CAPACITY, comp_capacity), Constants.COST_SERVER);
				objective = cplex.sum(objective, server_cost);
			}
			
			cplex.addMinimize(objective);
			
			
			// constraints
			
			// must have server selected -- for test
//			IloNumExpr expr_server = cplex.numExpr();
//			for(int i = 0; i < n; i++) 
//			{
//				expr_server = cplex.sum(expr_server, y[i]);
//			}
//			cplex.addGe(expr_server, 5);
			
			
			// only connect to one server constraint
			
			for(int j = 0; j < n; j++) 
			{
				IloNumExpr expr1 = cplex.numExpr();
				for(int i = 0; i < n; i ++) 
				{
					if(i != j) 
					{
						//cplex.ifThen(cplex.eq(y[i], 1.0), );
						//expr1 = cplex.sum(expr1, cplex.prod(x[i][j], y[i]));
						expr1 = cplex.sum(expr1, x[i][j]);
					}
					
				}
				cplex.addEq(expr1, 1);
			}
			
//			// a bs is not allowed to be assigned to a bs
//			for(int i = 0; i < n; i++) 
//			{
//				IloNumExpr bs_sum = cplex.numExpr();
//				for(int j = 0; j < n; j++) 
//				{
//					// sum of the row
//					cplex.sum(bs_sum, x[i][j]);
//				}
//				cplex.ifThen(cplex.eq(y[i], 0), cplex.addEq(bs_sum, 0));
//			}
			
			// EN cannot be assigned to anyone
//			for(int i = 0; i < n; i++) 
//			{
//				IloNumExpr bs_sum2 = cplex.numExpr();
//				for(int j = 0; j < n; j++) 
//				{
//					// sum of the column
//					cplex.sum(bs_sum2, x[j][i]);
//				}
//				cplex.ifThen(cplex.eq(y[i], 1.0), cplex.addEq(bs_sum2, 0));
//			}
			
			
			// cover all constraint

			IloNumExpr expr2 = cplex.numExpr();
			for(int i = 0; i < n; i++) 
			{
				//cplex.sum(expr2, y[i]);
				for(int j = 0;  j < n; j++) 
				{
					if(i != j) 
					{
						expr2 = cplex.sum(expr2, cplex.prod( y[i], x[i][j]));
					}
				}
			}
			cplex.addEq(expr2, n);
			
			// delay constraint
//			for(int i = 0; i < n; i++) 
//			{
//				for(int j = 0; j < n; n++) 
//				{
//					if(i != j) 
//					{
//						IloLinearNumExpr constant3 = cplex.linearNumExpr();
//						double distance = Utils.getDistance(bsList.get(i).getLocation(), bsList.get(j).getLocation());
//						double trans_delay = Utils.getTransDelay(distance, bsList.get(j).getCTMax() * Constants.SINGLE_TASK_SIZE);
//						
//						cplex.addLe(constant3, Constants.DELAY_THRESH);
//					}
//				}
//			}
			
			if(cplex.solve()) 
			{
				System.out.println("Solved");
				System.out.println(cplex.getObjValue());
				
				for(int i = 0; i < n; i++) 
				{
					for(int j = 0; j < n; j++) 
					{
						//System.out.println(cplex.getValue(x[i][j]));
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
	
	
	private double getCapacityReq(BaseStation en, BaseStation bs) 
	{
		int task_size = bs.getCTMax() * Constants.SINGLE_TASK_SIZE;
		double distance = Utils.getDistance(en.getLocation(), bs.getLocation());
		return Utils.getCapacityRequired(distance, task_size);
	}
	
	private double getSelfCapacityReq(BaseStation en) 
	{
		return en.getCTMax() * Constants.SINGLE_TASK_SIZE / Constants.DELAY_THRESH;
	}
	

}
