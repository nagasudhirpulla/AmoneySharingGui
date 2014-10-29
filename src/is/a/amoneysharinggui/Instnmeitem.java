package is.a.amoneysharinggui;

public class Instnmeitem {
	private String title;
	private String firstdate;
	private String lastdate;
	private int _id;
	public Instnmeitem(String title, String firstdate, String lastdate, int _id) {

		this.title = title;
		this._id = _id;
		this.firstdate = firstdate;
		this.lastdate = lastdate;
	}
	
	public Instnmeitem(String title, int _id) {

		this.title = title;
		this._id = _id;
	}
	public Instnmeitem(String title) {

		this.title = title;
	}
	
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getfirst() {
		return firstdate;
	}

	public void setfirst(String firstdate) {
		this.firstdate = firstdate;
	}
	
	public String getlast() {
		return lastdate;
	}

	public void setlast(String lastdate) {
		this.lastdate = lastdate;
	}
	
	public int getId(){
		return _id;
	}
	
	public void setId(int _id)
	{
		this._id = _id;
	}

	@Override
	public String toString() {
		return title;
	}
}
