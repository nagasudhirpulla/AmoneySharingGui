package is.a.amoneysharinggui;

public class Member {

	//private variables
	int _id;
	String _name;

	// Empty constructor
	public Member(){

	}
	// constructor
	public Member(int id, String name){
		this._id = id;
		this._name = name;
	}
	// constructor
	public Member(String name){
		this._name = name;
	}


	// getting ID
	public int getID(){
		return this._id;
	}


	// setting id
	public void setID(int id){
		this._id = id;
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