//package utilities;
//
//public class Constants {
//	  public static double DELAY_THRESH = 14; // total delay threshold
//	  public static final int COST_EN = 400; // EN construction cost
//	  public static final int COST_SERVER = 100; // server cost
//	  //public static final int DISTANCE_THRESH = 2000; // distance threshold for connection 5000
//	  public static final int SINGLE_SERVER_CAPACITY = 100; // single server capacity
//	  public static final double ALPHA = 0.003; // noise-distance coefficient 0.3
//	  public static final int BANDWIDTH = 5; // bandwidth 200
//	  public static final int CANNEL_SIGNAL_POWER = 35; // channel signal power 35
//	  public static final int SINGLE_TASK_SIZE = 15; //
//
//	  public static int DISTANCE_THRESH = 2000;
//	  public static int CTMAX = 0;
//	  public static boolean isPeak = true;
//}


package utilities;

public class Constants {
	public static double DELAY_THRESH = 0.1; // total delay threshold
	public static final int COST_EN = 40; // EN construction cost
	public static final int COST_SERVER = 100; // server cost
	//public static final int DISTANCE_THRESH = 2000; // distance threshold for connection 5000
	public static final long SINGLE_SERVER_CAPACITY = 400_000_000_000L; // single server capacity
	public static final double ALPHA = 0.1; // noise-distance coefficient 0.3
	public static final double BANDWIDTH = 5.00E6; // bandwidth 200
	public static final int CANNEL_SIGNAL_POWER = 35; // channel signal power 35
	public static final int SINGLE_TASK_SIZE = (25*60*60*2)/8; //

	public static int DISTANCE_THRESH = 2000;
	public static int CTMAX = 0;
	public static boolean isPeak = true;
}
