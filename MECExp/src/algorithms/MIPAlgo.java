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
				//IloNumExpr server_cost = cplex.numExpr();
				
				comp_capacity = cplex.sum(comp_capacity, getSelfServerReq(bsList.get(i)));
				
				for(int j = 0; j < n; j++) 
				{
					if(i != j) 
					{
						double comp_capacity_req = getCapacityReq(bsList.get(i), bsList.get(j)); 
						double server_cost = comp_capacity_req/Constants.SINGLE_SERVER_CAPACITY * Constants.COST_SERVER;
						comp_capacity = cplex.sum(server_cost, cplex.prod(comp_capacity_req, x[i][j]));
					}
				}
				
				//server_cost = cplex.prod(1/Constants.SINGLE_SERVER_CAPACITY, comp_capacity);
				//server_cost = cplex.prod(server_cost, Constants.COST_SERVER);
				objective = cplex.sum(objective, comp_capacity);
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
			
//			for(int j = 0; j < n; j++) 
//			{
//				IloNumExpr expr1 = cplex.numExpr();
//				for(int i = 0; i < n; i ++) 
//				{
//					if(i != j) 
//					{
//						//cplex.ifThen(cplex.eq(y[i], 1.0), );
//						//expr1 = cplex.sum(expr1, cplex.prod(x[i][j], y[i]));
//						expr1 = cplex.sum(expr1, x[i][j]);
//					}
//					
//				}
//				cplex.addEq(expr1, 1);
//			}
			
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
			
			// at least one BS is selected as EN
			IloNumExpr expr_server_num = cplex.numExpr();
			for(int i = 0; i < n; i++) 
			{
				expr_server_num = cplex.sum(expr_server_num, y[i]);
			}
			cplex.addGe(expr_server_num, 1);
			
			// constraints between BS and EN
			for(int i = 0; i < n; i++) 
			{
				IloNumExpr expr_col = cplex.numExpr();
				IloNumExpr expr_row = cplex.numExpr();
				
				for(int j = 0;  j < n; j++) 
				{
					expr_col = cplex.sum(expr_col, x[j][i]);
					expr_row = cplex.sum(expr_row, x[i][j]);
				}
				
				// i is not a EN, should connect to only 1 EN
				cplex.ifThen(cplex.eq(y[i], 0), cplex.addEq(expr_col, 1));
				// i is not a EN, i should have no bs 
				//cplex.ifThen(cplex.eq(y[i], 0), cplex.addEq(expr_row, 0));
				
				cplex.ifThen(cplex.eq(y[i], 1), cplex.addGe(expr_row, 1));
				
			}
			
			
			//cplex.addEq(
			
			// cover all constraint

//			IloNumExpr expr2 = cplex.numExpr();
//			for(int i = 0; i < n; i++) 
//			{
//				//cplex.sum(expr2, y[i]);
//				for(int j = 0;  j < n; j++) 
//				{
//					if(i != j) 
//					{
//						expr2 = cplex.sum(expr2, cplex.prod( y[i], x[i][j]));
//					}
//				}
//			}
//			cplex.addEq(expr2, n);
			
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
			
			cplex.exportModel("lpex1.lp");

			
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
	
	private double getSelfServerReq(BaseStation en) 
	{
		double capacity_req = en.getCTMax() * Constants.SINGLE_TASK_SIZE / Constants.DELAY_THRESH;
		double server_cost_req = capacity_req / Constants.SINGLE_SERVER_CAPACITY * Constants.COST_SERVER;
		return server_cost_req;
		//return capacity_req;
	}
	

}
