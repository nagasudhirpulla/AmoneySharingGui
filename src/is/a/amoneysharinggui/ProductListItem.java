package is.a.amoneysharinggui;

public class ProductListItem {
	private String pname;
	private String pdisc;
	private String pdate;
	private String value = "0";

	public ProductListItem(String pname, String pdisc, String pdate, String value)
	{
		this.pname = pname;
		this.pdisc = pdisc;
		this.pdate = pdate;
		this.value = value;
	}
	public ProductListItem(String pname, String pdisc, String pdate)
	{
		this.pname = pname;
		this.pdisc = pdisc;
		this.pdate = pdate;
	}

	public String getpname() {
		return pname;
	}

	public String getpdisc() {
		return pdisc;
	}

	public String getpdate() {
		return pdate;
	}
	
	public String getpval() {
		return value;
	}

	public void setpname(String pname) {
		this.pname = pname;
	}

	public void setpdisc(String pdisc) {
		this.pdisc = pdisc;
	}

	public void setpdate(String pdate) {
		this.pdate = pdate;
	}
	
	public void setpval(String value) {
		this.value = value;
	}

}

