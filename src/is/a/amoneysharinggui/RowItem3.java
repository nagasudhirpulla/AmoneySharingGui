package is.a.amoneysharinggui;

public class RowItem3 {
	private float amount;
    private String title;
 
    public RowItem3(String title, float amount) {
        this.amount = -amount;
        this.title = title;
    }
    public float getamount() {
        return amount;
    }
    public void setamount(float amount) {
        this.amount = amount;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    @Override
    public String toString() {
        return title ;
    }

}
