package is.a.amoneysharinggui;

public class Product {
	int _id;
	String _name;
	String tag_id;

	// Empty constructor
	public Product(){

	}
	// constructor
	public Product(int id, String name, String tag_id){
		this._id = id;
		this._name = name;
		this.tag_id = tag_id;
	}
	// constructor
	public Product(String name, String tag_id){
		this._name = name;
		this.tag_id = tag_id;
	}

	// getting ID
	public int getID(){
		return this._id;
	}


	// setting id
	public void setID(int id){
		this._id = id;
	}


	// getting TagID
	public String getTagID(){
		return this.tag_id;
	}


	// setting TagId
	public void setTagID(String tag_id){
		this.tag_id = tag_id;
	}


	// getting name
	public String getName(){
		return this._name;
	}


	// setting name
	public void setName(String name){
		this._name = name;
	}


}