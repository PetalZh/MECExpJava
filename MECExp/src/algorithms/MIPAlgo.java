package algorithms;
import java.lang.reflect.Array;
import java.util.ArrayList;

import ilog.concert.*;
import ilog.cplex.*;
import ilog.cplex.IloCplex.UnknownObjectException;
import objs.BaseStation;
import utilities.Constants;
import utilities.Utils;

public class MIPAlgo {

	public MIPAlgo() {
		super();
	}
	
	private int cost;
	private int en_num;
	private ArrayList<BaseStation> enList;
	
	public void getMIP(ArrayList<BaseStation> bsList) 
	{
		try {
			
			int n = bsList.size();
			//System.out.println("List size: " + n);
			IloCplex cplex = new IloCplex();
			
//			cplex.setParam(IloCplex.Param.Simplex.Tolerances.Optimality, 1e-9);
//			cplex.setParam(IloCplex.Param.MIP.Tolerances.MIPGap, 1e-9);
			cplex.setParam(IloCplex.DoubleParam.TimeLimit, 600);
			cplex.setOut(null);
			
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
				objective = cplex.sum(objective, getSelfServerReq(bsList.get(i)));
				
				for(int j = 0; j < n; j++) 
				{
					double comp_capacity_req = getCapacityReq(bsList.get(i), bsList.get(j)); 
					double server_cost = (comp_capacity_req/Constants.SINGLE_SERVER_CAPACITY) * Constants.COST_SERVER;
					//System.out.println("server cost: " + server_cost);
					//server_cost_expr = cplex.prod(server_cost, x[i][j]);
					if(server_cost > 0) 
					{
						objective = cplex.sum(objective, cplex.prod(server_cost, x[i][j]));
					}
					
//					objective = cplex.sum(objective, cplex.prod(server_cost, x[i][j]));
				}
				
			}
			
			cplex.addMinimize(objective);
			
			
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
				cplex.add(cplex.ifThen(cplex.eq(y[i], 0), cplex.eq(expr_col, 1)));
				
				// i is not a EN, i should have no bs 
				cplex.add(cplex.ifThen(cplex.eq(y[i], 0), cplex.eq(expr_row, 0)));
				
				// i is an EN, i should have more than one bs connect
				//cplex.add(cplex.ifThen(cplex.eq(y[i], 1), cplex.ge(expr_row, 1)));
				
				//i is an EN, i should not connect to any other en
				cplex.add(cplex.ifThen(cplex.eq(y[i], 1), cplex.eq(expr_col, 0)));
				
				// each bs should connect to at least one EN
				
			}
			
			// delay constraint
			for(int i = 0; i < n; i++) 
			{
				for(int j = 0; j < n; j++) 
				{
					if(i != j) 
					{
						double distance = Utils.getDistance(bsList.get(i).getLocation(), bsList.get(j).getLocation());
						double trans_delay = Utils.getTransDelay(distance, bsList.get(j).getCTMax() * Constants.SINGLE_TASK_SIZE);
						//System.out.println("Trans Delay: " + trans_delay);
						//System.out.println("Distance: " + distance + " workload: " +  bsList.get(j).getCTMax() * Constants.SINGLE_TASK_SIZE);
						if(trans_delay > Constants.DELAY_THRESH) //distance > Constants.DISTANCE_THRESH
						{
							cplex.addEq(x[i][j], 0);
						}
						
//						if(trans_delay >= Constants.DELAY_THRESH) 
//						{
//							cplex.addEq(x[i][j], 0);
//						}
					}else {
						cplex.addEq(x[i][j], 0);
					}
				}
			}
			
			
			if(cplex.solve()) 
			{
				System.out.println("------------------------------------------");
				System.out.println("MIP Solved");
				System.out.println(cplex.getObjValue());
				
				this.cost = (int)cplex.getObjValue();
				
				//printX(cplex, x, n);
				printY(cplex, y, n);
				this.enList = formulateResult(bsList, cplex, x, y, n);
			}else 
			{
				System.out.println("Fail");
			}
			
			cplex.exportModel("lpex1.lp");
			cplex.end();
			
		} catch (IloException e) {
			e.printStackTrace();
		}
		
	}

	private ArrayList<BaseStation> formulateResult(ArrayList<BaseStation> bsList, IloCplex cplex, IloNumVar[][] x, IloNumVar[] y, int n) throws UnknownObjectException, IloException
	{
		ArrayList<BaseStation> enList = new ArrayList<>();
		for(int i = 0; i < n; i++)
		{
			if(cplex.getValue(y[i]) != 0)
			{
				BaseStation en = bsList.get(i);
				for(int j = 0; j < n; j++)
				{
					if(cplex.getValue(x[i][j]) > 0)
					{
						BaseStation bs = bsList.get(j);
						en.addBS(bs, Utils.getDistance(en.getLocation(), bs.getLocation()), Constants.isPeak);
					}
				}
			enList.add(en);
			}
		}
		return enList;
	}
	
	private void printX(IloCplex cplex, IloNumVar[][] x, int n) throws UnknownObjectException, IloException 
	{
		for(int i = 0; i < n; i++) 
		{
			//System.out.println(cplex.getValue(y[i]));
			for(int j = 0; j < n; j++) 
			{
				System.out.print(cplex.getValue(x[i][j]) + " ");
			}
			System.out.println();
		}
	}
	
	private void printY(IloCplex cplex, IloNumVar[] y, int n) throws UnknownObjectException, IloException 
	{
		int num_en_selected = 0;
		
		//System.out.println("EN distribution:");
		for(int i = 0; i < n; i++) 
		{
			//System.out.print(cplex.getValue(y[i]) + " ");
			if(cplex.getValue(y[i]) != 0)
			{
				num_en_selected++;
			}
		}
		System.out.println();
		System.out.println("# of EN selected: " + num_en_selected);
		this.en_num = num_en_selected;
	}
	
	
	private double getCapacityReq(BaseStation en, BaseStation bs) 
	{
		int task_size = (int)Math.round(bs.getCTMax() * Constants.SINGLE_TASK_SIZE);
		double distance = Utils.getDistance(en.getLocation(), bs.getLocation());
		double capacity_req = Utils.getCapacityRequired(distance, task_size);
//		if(capacity_req < 0) 
//		{
//			capacity_req = 0;
//		}
		return capacity_req;
	}
	
	private double getSelfServerReq(BaseStation en) 
	{
		double capacity_req = en.getCTMax() * Constants.SINGLE_TASK_SIZE / Constants.DELAY_THRESH;
		double server_cost_req = capacity_req / Constants.SINGLE_SERVER_CAPACITY * Constants.COST_SERVER;
		return server_cost_req;
		//return capacity_req;
	}

	public int getCost() {
		return cost;
	}

	public int getEn_num() {
		return en_num;
	}

	public ArrayList<BaseStation> getEnList(){
		return this.enList;
	}


	
	
}
