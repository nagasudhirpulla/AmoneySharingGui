package is.a.amoneysharinggui;

public class valueCell {
	int _id;
	int _benf;
	int _cont;

	// Empty constructor
	public valueCell(){

	}

	// constructor
	public valueCell(int id, int _benf, int _cont){
		this._id = id;
		this._benf = _benf;
		this._cont = _cont;
	}
	// constructor
	public valueCell(int _benf, int _cont){
		this._benf = _benf;
		this._cont = _cont;
	}

	// constructor
	public valueCell(int _benf, float _cont){
		this._benf = _benf;
		this._cont = (int) _cont;
	}

	// getting ID
	public int getID(){
		return this._id;
	}


	// setting id
	public void setID(int id){
		this._id = id;
	}

	// getting Beneficiary or not
	public int getBenfrnt(){
		return this._benf;
	}


	// setting Beneficiary or not
	public void setBenfrnt(int _benf){
		this._benf = _benf;
	}

	// getting Contribution
	public int getContr(){
		return this._cont;
	}


	// setting Contribution
	public void setContr(int _cont){
		this._cont = _cont;
	}

}