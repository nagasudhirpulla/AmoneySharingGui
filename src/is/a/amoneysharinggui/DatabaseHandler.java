package is.a.amoneysharinggui;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

	GlobalVarClass state;

	private static Context cont;
	private SQLiteDatabase db;
	private static DatabaseHandler minstance;
	private int opinst = 0;

	public static DatabaseHandler getInstance()
	{
		return minstance;
	}

	public void open() throws SQLException {
		db = this.getWritableDatabase();
	}

	public void close() {
		db.close();
	}


	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "Debtonator";

	// Members table name
	static final String TABLE_MEMBERS = "members";

	// Products table name
	static final String TABLE_PRODUCTS = "products";

	// User Preferences table name
	static final String TABLE_PREFS = "prefs";

	// Instances table name
	static final String TABLE_INSTS = "insts";

	// Instance variables table name
	static final String TABLE_INSTVARS = "instvars";


	// Members Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_MEMBER_NAME = "name";

	// Products Table Columns names
	private static final String KEY_PRODUCT_NAME = "name";
	private static final String KEY_PRODUCT_TAGNAME = "tabletag";

	//Product Value Columns names
	private static final String KEY_BENIFICIARY = "benf";
	private static final String KEY_CONTRIBUTION = "cont";

	//Preference table column names
	private static final String KEY_NUMINST = "ninst";
	private static final String KEY_NINIT = "ninit";

	//Instance name table column names
	private static final String KEY_INSTNM = "instnm";
	private static final String KEY_INSTANCE_TAGNAME = "instancetag";

	//Instance variables table column names
	private static final String KEY_INSTCURRPROD = "instcurrprod";
	private static final String KEY_INSTTOTPROD = "insttotprod";
	private static final String KEY_FIRSTDATE = "instfirstdate";
	private static final String KEY_LASTDATE = "instlastdate";

	//Constructor
	public DatabaseHandler(Context conte) {
		super(conte, DATABASE_NAME, null, DATABASE_VERSION);
		cont = conte;
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		minstance = this;
		String CREATE_MEMBERS_TABLE = "CREATE TABLE " + TABLE_MEMBERS + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_MEMBER_NAME + " TEXT"
				+ ")";
		db.execSQL(CREATE_MEMBERS_TABLE);

		String CREATE_PRODUCTS_TABLE = "CREATE TABLE " + TABLE_PRODUCTS + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_PRODUCT_NAME + " TEXT,"
				+ KEY_PRODUCT_TAGNAME + " TEXT" + ")";
		db.execSQL(CREATE_PRODUCTS_TABLE);

		String CREATE_PREFS_TABLE = "CREATE TABLE " + TABLE_PREFS + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_NUMINST + " INTEGER,"
				+ KEY_NINIT + " INTEGER" + ")";
		db.execSQL(CREATE_PREFS_TABLE);

		String CREATE_INSTS_TABLE = "CREATE TABLE " + TABLE_INSTS + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_INSTNM + " TEXT,"
				+ KEY_INSTANCE_TAGNAME + " INTEGER"+")";
		db.execSQL(CREATE_INSTS_TABLE);

		String Insert_Prefs = "INSERT INTO " + TABLE_PREFS + "(" + KEY_NUMINST + "," + KEY_NINIT + ")" + "VALUES(0, 5)"; 
		db.execSQL(Insert_Prefs);
		//Log.d("Message:", "CREATED");
	}

	void setInitialPrefs() {
		// TODO Auto-generated method stub
		//SQLiteDatabase db = this.getWritableDatabase();

		//Setting the number of people as 5
		ContentValues values = new ContentValues();
		values.put(KEY_NINIT, 5);
		// Inserting Row
		db.insert(TABLE_PREFS, null, values);
		db.close(); // Closing database connection
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEMBERS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);

		// Create tables again
		onCreate(db);
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	// Adding new Member
	void addMember(Member member) {
		//SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_MEMBER_NAME, member.getName()); // Member Name

		// Inserting Row
		db.insert(TABLE_MEMBERS+getNumInst(), null, values);/////EDITED
		//db.close(); // Closing database connection
	}

	// Adding new Product
	void addProduct(Product product) {
		//SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_PRODUCT_NAME, product.getName()); // Product Description
		values.put(KEY_PRODUCT_TAGNAME, product.getTagID()); // Product table tag
		// Inserting Row
		db.insert(TABLE_PRODUCTS + getNumInst(), null, values);/////EDITED
		//db.close(); // Closing database connection
		//createTable("I"+getNumInst()+getProductsCount());
	}

	// Getting single member
	Member getMember(int id) {
		//SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_MEMBERS+getNumInst(), new String[] { KEY_ID,
			KEY_MEMBER_NAME }, KEY_ID + "=?",
			new String[] { String.valueOf(id) }, null, null, null, null);/////EDITED
		if (cursor != null)
			cursor.moveToFirst();

		Member member = new Member(Integer.parseInt(cursor.getString(0)),
				cursor.getString(1));
		cursor.close();

		// return member
		return member;
	}

	// Getting single product
	Product getProduct(int id) {
		//SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_PRODUCTS + getNumInst(), new String[] { KEY_ID,
			KEY_PRODUCT_NAME, KEY_PRODUCT_TAGNAME }, KEY_ID + "=?",
			new String[] { String.valueOf(id) }, null, null, null, null);////EDITED
		if (cursor != null)
			cursor.moveToFirst();

		Product product = new Product(Integer.parseInt(cursor.getString(0)),
				cursor.getString(1), cursor.getString(2));
		cursor.close();

		// return product
		return product;

	}

	// Getting All Members
	public List<Member> getAllMembers() {
		List<Member> memberList = new ArrayList<Member>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_MEMBERS + getNumInst();

		//SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Member member = new Member();
				member.setID(Integer.parseInt(cursor.getString(0)));
				member.setName(cursor.getString(1));
				// Adding member to list
				memberList.add(member);
			} while (cursor.moveToNext());
		}

		cursor.close();

		// return Member list
		return memberList;
	}

	// Getting All Products
	public List<Product> getAllProducts() {
		List<Product> productList = new ArrayList<Product>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_PRODUCTS + getNumInst();

		//SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Product product = new Product();
				product.setID(Integer.parseInt(cursor.getString(0)));
				product.setName(cursor.getString(1));
				product.setTagID(cursor.getString(2));
				// Adding product to list
				productList.add(product);
			}while (cursor.moveToNext());
		}

		cursor.close();

		// return Product list
		return productList;

	}

	// Updating single Member
	public int updateMember(Member member) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_MEMBER_NAME, member.getName());

		// updating row
		return db.update(TABLE_MEMBERS, values, KEY_ID + " = ?",
				new String[] { String.valueOf(member.getID()) });
	}

	// Updating single Product
	public int updateProduct(Product product) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_PRODUCT_NAME, product.getName());
		values.put(KEY_PRODUCT_TAGNAME, product.getTagID());

		// updating row
		return db.update(TABLE_PRODUCTS, values, KEY_ID + " = ?",
				new String[] { String.valueOf(product.getID()) });
	}

	// Deleting single Member
	public void deleteMember(Member member) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_MEMBERS, KEY_ID + " = ?",
				new String[] { String.valueOf(member.getID()) });
		db.close();
	}

	// Deleting single Product
	public void deleteProduct(Member product) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_PRODUCTS, KEY_ID + " = ?",
				new String[] { String.valueOf(product.getID()) });
		db.close();
	}


	// Getting members Count
	public int getMembersCount() {
		String countQuery = "SELECT  * FROM " + TABLE_MEMBERS + getNumInst();
		//SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int n = cursor.getCount();
		cursor.close();

		// return count
		return n;
	}

	// Getting products Count
	public int getProductsCount() {
		String countQuery = "SELECT  * FROM " + TABLE_PRODUCTS + getNumInst();////EDITED
		//SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int n = cursor.getCount();
		cursor.close();

		// return count
		return n;
	}

	// Delete all members
	public void deleteallMembers() {
		// TODO Auto-generated method stub
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_MEMBERS, null,
				null);
		db.close();
	}

	// Delete all products
	public void deleteallProducts() {
		// TODO Auto-generated method stub
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_PRODUCTS, null,
				null);
		db.close();
	}

	public static void deletedb() {
		cont.deleteDatabase(DATABASE_NAME);
		// TODO Auto-generated method stub

	}

	//Creating a table specified by table name for a product
	public void createProductsValueTables(String str) {
		state = GlobalVarClass.getInstance();
		//SQLiteDatabase db = this.getWritableDatabase();
		String CREATE_PRODUCT_VALUE_TABLE = "";
		for(int j=0;j<state.totprod;j++)
		{
			str  = "I"+getNumInst()+j;
			//Log.d("Table created: ",str);
			db.execSQL("DROP TABLE IF EXISTS " + str);
			CREATE_PRODUCT_VALUE_TABLE = "CREATE TABLE " + str + "("
					+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_BENIFICIARY + " INTEGER,"
					+ KEY_CONTRIBUTION + " INTEGER" + ")";
			db.execSQL(CREATE_PRODUCT_VALUE_TABLE);
			//Inserting values into the corresponding product table
			for(int i=0;i<state.Names.length;i++)
			{
				insertValueTable(str, new valueCell((int)state.weights[j][i],state.conts[j][i]*100));			
			}
		}
		//db.close();
	}

	//Inserting values into the table corresponding to each product
	public void insertValueTable(String tablnme, valueCell cell){
		//SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_BENIFICIARY, cell.getBenfrnt()); // Product Description
		values.put(KEY_CONTRIBUTION, cell.getContr()); // Product table tag
		// Inserting Row
		db.insert(tablnme, null, values);
		//db.close(); // Closing database connection
	}

	// Getting All values of a product value Table
	public List<valueCell> getAllValueTables(String str) {
		List<valueCell> valueList = new ArrayList<valueCell>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + str;

		//SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				valueCell cell = new valueCell();
				cell.setID(Integer.parseInt(cursor.getString(0)));
				cell.setBenfrnt(Integer.parseInt(cursor.getString(1)));
				cell.setContr(Integer.parseInt(cursor.getString(2)));
				// Adding product to list
				valueList.add(cell);
			}while (cursor.moveToNext());
		}
		cursor.close();

		// return Product list
		return valueList;
	}

	// Updating single row of a value table of a product specified by the name
	public int updateValueProduct(String str,valueCell cell) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_BENIFICIARY, cell.getBenfrnt());
		values.put(KEY_CONTRIBUTION, cell.getContr());

		// updating row
		return db.update(str, values, KEY_ID + " = ?",
				new String[] { String.valueOf(cell.getID()) });
	}

	// Deleting single row of a product value table
	public void deleteValueRow(String str,valueCell cell) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(str, KEY_ID + " = ?",
				new String[] { String.valueOf(cell.getID()) });
		db.close();
	}

	public void addInstanceVars() {
		// TODO Auto-generated method stub
		state = GlobalVarClass.getInstance();
		//SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_INSTCURRPROD, state.currentproduct); // Current Product
		values.put(KEY_INSTTOTPROD, state.totprod); // Total Number of Products
		values.put(KEY_FIRSTDATE, state.firstdate);
		values.put(KEY_LASTDATE, state.lastdate);
		// Inserting Row
		db.insert(TABLE_INSTVARS+getNumInst(), null, values);/////EDITED
		//db.close(); // Closing database connection		
	}

	public void printInstanceVars() {
		// TODO Auto-generated method stub
		//SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_INSTVARS + getNumInst(), new String[] { KEY_ID,
			KEY_INSTCURRPROD, KEY_INSTTOTPROD, KEY_FIRSTDATE, KEY_LASTDATE }, KEY_ID + "=?",
			new String[] { String.valueOf(1) }, null, null, null, null);////EDITED
		if (cursor != null)
			cursor.moveToFirst();
		//Log.d("CURRENT PRODUCT:", cursor.getString(1));
		//Log.d("TOTAL PRODUCTS:", cursor.getString(2));
		cursor.close();

	}

	public void printProductcellData() {

		// Reading all values of all products
		//Log.d("Reading: ", "Reading all values of all products..");
		for(int i = 0;i<getProductsCount();i++)
		{
			//Log.d("cell Content of Product: ", ""+(i+1));
			List<valueCell> celllist = getAllValueTables("I"+getNumInst()+i);

			for (valueCell cn : celllist) {
				String log = "Id: "+cn.getID()+" ,Benificiary?: " + cn.getBenfrnt() + " ,Contribution: " + (float)cn.getContr()/100 ;
				// Writing value table of the product to log
				//Log.d("cell Content: ", log);
			}
		}
	}



	int getNumInit()
	{
		//SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_PREFS, new String[] { KEY_ID,
				KEY_NUMINST, KEY_NINIT }, KEY_ID + "=?",
				new String[] { String.valueOf(1) }, null, null, null, null);
		//Remember that indexing starts with 1.
		if (cursor != null)
			cursor.moveToFirst();
		int ans = Integer.parseInt(cursor.getString(2));

		// return number of initial people to start with
		//Log.d("Number of Initials: ",cursor.getString(2));
		cursor.close();		
		return ans;

	}

	int getNumInst()
	{		
		return opinst;
	}
	boolean saveThisInstance(String nme, boolean rewrte)
	{
		state = GlobalVarClass.getInstance();
		//SQLiteDatabase db = this.getWritableDatabase();

		/////Insert a new instance into the instance name table
		//String sql = "INSERT INTO " + TABLE_INSTS + "(" + KEY_INSTNM + ")" + "VALUES('Instance1')"; 
		//db.execSQL(sql);
		Cursor cursor;
		if(rewrte)
		{
			//Assigning the to be rewritten instance tag
			cursor = db.query(TABLE_INSTS, new String[] { KEY_INSTANCE_TAGNAME }, KEY_ID + "=?",
					new String[] { String.valueOf(state.curload) }, null, null, null, null);/////EDITED
			if (cursor != null)
				cursor.moveToFirst();

			opinst = Integer.parseInt(cursor.getString(0));
			cursor.close();			
		}
		else
		{
			//get vacant instance tag
			opinst = getVacInstTag();
			ContentValues values = new ContentValues();
			if(nme.length() == 0)
				values.put(KEY_INSTNM, "Instance"+getNumInst()); // Instance Name
			else
				values.put(KEY_INSTNM, nme); // Instance Name
			values.put(KEY_INSTANCE_TAGNAME, getNumInst());
			// Inserting Row in instance name table
			db.insert(TABLE_INSTS, null, values);

			//updating state.curload
			cursor = db.query(TABLE_INSTS, new String[] { KEY_ID }, KEY_INSTANCE_TAGNAME + "=?",
					new String[] { String.valueOf(opinst) }, null, null, null, null);/////EDITED
			if (cursor != null)
				cursor.moveToFirst();
			state.curload = Integer.parseInt(cursor.getString(0));
			cursor.close();
		}

		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEMBERS + getNumInst());

		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS + getNumInst());

		db.execSQL("DROP TABLE IF EXISTS " + TABLE_INSTVARS + getNumInst());

		//Creating the table for saving the member names of this instance
		String CREATE_MEMBERS_TABLE = "CREATE TABLE " + TABLE_MEMBERS + getNumInst() + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_MEMBER_NAME + " TEXT"
				+ ")";
		db.execSQL(CREATE_MEMBERS_TABLE);

		//Creating the table for saving the product descriptions of this instance 
		String CREATE_PRODUCTS_TABLE = "CREATE TABLE " + TABLE_PRODUCTS + getNumInst() + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_PRODUCT_NAME + " TEXT,"
				+ KEY_PRODUCT_TAGNAME + " TEXT" + ")";
		db.execSQL(CREATE_PRODUCTS_TABLE);

		//Creating the table for saving the instance variables of this instance 
		String CREATE_INSTVARS_TABLE = "CREATE TABLE " + TABLE_INSTVARS + getNumInst() + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_INSTCURRPROD + " INTEGER,"
				+ KEY_INSTTOTPROD + " INTEGER," + KEY_FIRSTDATE + " TEXT," + KEY_LASTDATE + " TEXT" + ")";
		db.execSQL(CREATE_INSTVARS_TABLE);


		//ADD MEMBER NAMES FROM THE CURRENT INSTANCE
		for(int i = 0;i<state.Names.length;i++)
		{
			addMember(new Member(state.Names[i]));
			//Log.d("MEMBER ADDED :", getMember(i+1).getName());			
		}

		//ADD THE PRODUCT DESCRIPTIONS AND DATES FROM THE CURRENT INSTANCE
		String str;
		for(int i = 0;i<state.discs.length;i++)
		{			
			if(state.discs[i].length()==0)
				str = "\t";
			else
				str = state.discs[i];
			addProduct(new Product(str,state.dates[i]));
			//Log.d("PRODUCT ADDED :", getProduct(i+1).getName());			
		}
		createProductsValueTables("");

		//ADD CURRENT INSTANCE VARIABLES TO TABLE_INSTVARS
		addInstanceVars();
		printInstanceVars();
		printProductcellData();

		//db.close(); // Closing database connection

		return false;		
	}


	public boolean loadThisInstance(int instidtoload) {
		state = GlobalVarClass.getInstance();
		/////////////////Solve no database problem

		//Getting and setting the current database tag number
		Cursor cursor = db.query(TABLE_INSTS, new String[] { KEY_INSTANCE_TAGNAME }, KEY_ID + "=?",
				new String[] { String.valueOf(instidtoload) }, null, null, null, null);/////EDITED
		if (cursor != null)
			cursor.moveToFirst();
		opinst = Integer.parseInt(cursor.getString(0));
		cursor.close();

		//opinst = instidtoload;
		//Reading all the member names
		List<Member> memberlist = getAllMembers();
		String[] tempNames = new String[getMembersCount()];
		for (Member cn : memberlist) {
			String log = "Id: "+cn.getID()+" ,Name: " + cn.getName();
			// Writing value table of the product to log
			//Log.d("Members: ", log);
			//modifying state
			tempNames[cn.getID()-1] = cn.getName();
		}
		//modifying state
		state.Names = tempNames;

		//Reading all the Instance variables
		cursor = db.query(TABLE_INSTVARS + getNumInst(), new String[] { KEY_ID,
			KEY_INSTCURRPROD, KEY_INSTTOTPROD, KEY_FIRSTDATE, KEY_LASTDATE }, KEY_ID + "=?",
			new String[] { String.valueOf(1) }, null, null, null, null);////EDITED
		if (cursor != null)
			cursor.moveToFirst();
		//Log.d("CURRENT PRODUCT:", cursor.getString(1));
		//Log.d("TOTAL PRODUCTS:", cursor.getString(2));
		//Log.d("FIRST DATE:", cursor.getString(3));
		//Log.d("LAST DATE:", cursor.getString(4));
		//modifying state
		state.currentproduct = Integer.parseInt(cursor.getString(1));
		state.totprod = Integer.parseInt(cursor.getString(2));
		state.firstdate = cursor.getString(3);
		state.lastdate = cursor.getString(4);
		cursor.close();


		//Reading all the product descriptions and dates
		List<Product> productList = getAllProducts();
		state.discs = new String[state.totprod];
		state.dates = new String[state.totprod];
		state.Products = new String[state.totprod];
		for (Product cn : productList) {
			String log = "Id: "+cn.getID()+" ,Name: " + cn.getName();
			// Writing value table of the product to log
			//Log.d("Product Descriptions: ", log);
			//modifying state
			if(cn.getName().charAt(cn.getName().length()-1) == '\t')
				state.discs[cn.getID()-1] = "";
			else
				state.discs[cn.getID()-1] = cn.getName();
			state.Products[cn.getID()-1] = ""+cn.getID();
			state.dates[cn.getID()-1] = cn.getTagID();
		}


		// Reading all values of all products
		//Log.d("Reading: ", "Reading all values of all products..");
		for(int i = 0;i<getProductsCount();i++)
		{
			//Log.d("cell Content of Product: ", ""+(i+1));
			List<valueCell> celllist = getAllValueTables("I"+ getNumInst() +i);

			for (valueCell cn : celllist) {
				String log = "Id: "+cn.getID()+" ,Benificiary?: " + cn.getBenfrnt() + " ,Contribution: " + (float)cn.getContr()/100 ;
				// Writing value table of the product to log
				//Log.d("cell Content: ", log);
				//modifying state
				state.conts[i][cn.getID()-1] = (float)cn.getContr()/100;
				state.weights[i][cn.getID()-1] = cn.getBenfrnt();
				state.refresh();
			}
		}
		return true;

	}

	private int getVacInstTag() {
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_INSTS;

		Cursor cursor = db.rawQuery(selectQuery, null);
		int curtag = 0, tabtag;

		// looping through all rows and finding match for vacant tag
		if (cursor.moveToFirst()) {
			while (true) {
				tabtag = Integer.parseInt(cursor.getString(2));
				if(tabtag == curtag){
					curtag++;
					cursor.moveToFirst();
				}
				else
				{
					if(!cursor.moveToNext())
						break;
				}
			}
		}
		cursor.close();
		//Log.d("Free tag is", ""+curtag);
		return curtag;		
	}

	// Getting All Instance Names and ids
	public List<Instnmeitem> getAllInstances() {
		List<Instnmeitem> InstList = new ArrayList<Instnmeitem>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_INSTS;

		//SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				// Adding member to list
				InstList.add(new Instnmeitem(cursor.getString(1), Integer.parseInt(cursor.getString(0))));
			} while (cursor.moveToNext());
		}
		cursor.close();

		// return Instance list
		return InstList;
	}

	// Getting All Instance Names, ids and dates
	public List<Instnmeitem> getAllInstances1() {
		List<Instnmeitem> InstList = new ArrayList<Instnmeitem>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_INSTS;			

		//SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);


		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				String selectQuery1 = "SELECT  * FROM " + TABLE_INSTVARS + cursor.getString(2);
				Cursor cursor1 = db.rawQuery(selectQuery1, null);
				cursor1.moveToFirst();
				// Adding member to list
				InstList.add(new Instnmeitem(cursor.getString(1),cursor1.getString(3), cursor1.getString(4), Integer.parseInt(cursor.getString(0))));
				cursor1.close();
			} while (cursor.moveToNext());
		}
		cursor.close();

		// return Instance list
		return InstList;
	}

	// Updating single Instance Name
	public void updateInstance(String str, int id) {
		//SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_INSTNM, str);
		//values.put(KEY_INSTANCE_TAGNAME, tag);

		// updating row
		db.update(TABLE_INSTS, values, KEY_ID + " = ?",
				new String[] { String.valueOf(id) });
	}

	// Deleting single Member
	public void deleteInstance(int instId) {
		db.delete(TABLE_INSTS, KEY_ID + " = ?",
				new String[] { String.valueOf(instId) });
	}

	public String getcurinstname() {
		// TODO Auto-generated method stub
		state = GlobalVarClass.getInstance();
		String str = null;
		Cursor cursor = db.query(TABLE_INSTS, new String[] { KEY_INSTNM }, KEY_ID + "=?",
				new String[] { String.valueOf(state.curload) }, null, null, null, null);/////EDITED
		if (cursor != null)
			cursor.moveToFirst();

		str = cursor.getString(0);
		cursor.close();
		return str;
	}

}