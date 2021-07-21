package objs;

public class Connection {
	private BaseStation bs;
	private BaseStation en;
	
	public Connection(BaseStation bs, BaseStation en) {
		super();
		this.bs = bs;
		this.en = en;
	}
	public BaseStation getBs() {
		return bs;
	}
	public void setBs(BaseStation bs) {
		this.bs = bs;
	}
	public BaseStation getEn() {
		return en;
	}
	public void setEn(BaseStation en) {
		this.en = en;
	}
	
	

}
