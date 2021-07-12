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
			
			// whether BS is selected as EN
			IloNumVar[] c = new IloNumVar[n];
			for(int i = 0; i < c.length; i++) 
			{
				c[i] = cplex.boolVar();
				System.out.println(c[i].getLB());
			}
			
			IloNumVar[][] x = new IloNumVar[n][n];
			for(int i = 0; i < x.length; i++) 
			{
				x[i] = cplex.boolVarArray(n);
			}
			
			// objective
			IloLinearNumExpr objective = cplex.linearNumExpr();
			for(int i = 0; i < n; i++) 
			{
				double comp_capacity_req = 0;
				for(int j = 0; j < 0; j++) 
				{
					if(c[i].getLB() == 1)
					{
						comp_capacity_req += getCapacityReq(bsList.get(i), bsList.get(j)); 
					}
				}
				objective.addTerm((double)Constants.COST_EN, c[i]);
				int serverNum = (int)Math.ceil(comp_capacity_req / Constants.SINGLE_SERVER_CAPACITY);
				objective.addTerm((double) serverNum * Constants.COST_SERVER, c[i]);	
			}
			
			cplex.addMinimize(objective);
			
			// constraints
			
			// only connect to one server constraint
			
			for(int i = 0; i < n; i++) 
			{
				IloLinearNumExpr constraint1 = cplex.linearNumExpr();
				if(c[i].getLB() == 1) 
				{
					for(int j = 0;  j < n; j++) 
					{
						if(i != j) 
						{
							constraint1.addTerm(1.0, x[i][j]);
						}
					}
				}
				cplex.addEq(constraint1, 1.0);
			}
			
			
			// cover all constraint
			
			IloLinearNumExpr constraint2 = cplex.linearNumExpr();
			for(int i = 0; i < n; i++) 
			{
				
				if(c[i].getLB() == 1) 
				{
					for(int j = 0;  j < n; j++) 
					{
						if(i != j) 
						{
							constraint2.addTerm(1.0, x[i][j]);
						}
					}
				}
			}
			cplex.addEq(constraint2, n);
			
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
			
			
			cplex.solve();
			cplex.end();
			
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
