package is.a.amoneysharinggui;

public class RowItem2 {
	private String mname;
	private String mamount;
	private int imageid;
	private int alertid;
	

	public RowItem2(String mamount, String mname, int imageid, int alertid)
	{
		this.mamount = mamount;
		this.mname = mname;
		this.imageid = imageid;
		this.alertid = alertid;
	}

	public String getAmount() {
		return mamount;
	}
	public void setAmount(String mamount) {
		this.mamount = mamount;
	}
	public String getName() {
		return mname;
	}	
	public void setName(String mname) {
		this.mname = mname;
	}
	public int getImageId() {
		return imageid;
	}	
	public void setImageId(int imageid) {
		this.imageid = imageid;
	}
	
	public void setAlertid(int alertid){
		this.alertid = alertid;
	}
	
	public int getAlertid(){
		return alertid;
	}

	@Override
	public String toString() {
		return mamount;
	}

}

