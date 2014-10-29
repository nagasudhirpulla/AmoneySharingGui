package is.a.amoneysharinggui;

public class RowItem5 {
	private int title,title2;
	private String title1;

	public RowItem5(int title,String title1,int title2) {

		this.title = title;
		this.title1 = title1;
		this.title2 = title2;
	}

	public int getTitle() {
		return title;
	}

	public void setTitle(int title) {
		this.title = title;
	}
	public String getTitle1() {
		return title1;
	}

	public void setTitle1(String title1) {
		this.title1 = title1;
	}
	public int getTitle2() {
		return title2;
	}

	public void setTitle2(int title2) {
		this.title2 = title2;
	}
	@Override
	public String toString() {
		return title + "-->" + title1 + "-->" + title2;
	}
}
