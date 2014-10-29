package is.a.amoneysharinggui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.couchbase.lite.*;
import com.couchbase.lite.android.AndroidContext;



public class TransactionActivity extends Activity implements OnClickListener{

	FrameLayout ext,clip;
	FrameLayout finish;
	ListView trans;
	GlobalVarClass state;
	List<RowItem5> rowItems5;
	CustomListViewAdapter5 adapter5;
	DatabaseHandler db;
	//ClipboardManager myClipboard;
	List<RowItem3> rowItems;
	boolean canfinish = false;
	int numtr;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		state = GlobalVarClass.getInstance();
		setContentView(R.layout.transaction1);
		if(state.wsf != 1)
		{
			setupmargins();
		}
		GradientDrawable gd = new GradientDrawable(
				GradientDrawable.Orientation.TOP_BOTTOM,
				new int[]{Color.parseColor("#3A912D"),Color.parseColor("#3A912D")});
		gd.setCornerRadius(0f);
		View title = getWindow().findViewById(android.R.id.title);
		View titleBar = (View) title.getParent();
		titleBar.setBackgroundDrawable(gd);
		titleBar.getLayoutParams().height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) (35*GlobalVarClass.getInstance().hsf), getResources().getDisplayMetrics());
		((TextView) title).setTextSize((float) (20*state.tsf));
		GradientDrawable gd2 = new GradientDrawable(
				GradientDrawable.Orientation.TOP_BOTTOM,
				new int[]{Color.parseColor("#3A912D"),Color.parseColor("#3A912D")}); 
		findViewById(R.id.finalcontr).setBackgroundDrawable(gd2);
		//Do all the final contribution calculations
		state.calculatetotalconts();
		rowItems5 = new ArrayList<RowItem5>();
		trans = (ListView) findViewById(R.id.tranlist);
		//Add the home button 
		finish = (FrameLayout) findViewById(R.id.finishButton);
		//setupbackgrnd(finish);
		new SetBackGrnd(finish,true, true, 0.08f, 0.8f, Color.parseColor("#000000"),Color.parseColor("#313131"));
		finish.setOnClickListener(this);
		//Add the exit button
		ext = (FrameLayout) findViewById(R.id.exitButton);
		//setupbackgrnd(ext);
		new SetBackGrnd(ext,false, true, 0.08f, 0.8f, Color.parseColor("#E85B01"),Color.parseColor("#ED7C34"));
		ext.setOnClickListener(this);
		//Add the copy to clipboard button
		clip = (FrameLayout) findViewById(R.id.clipboard);
		new SetBackGrnd(clip,false, true, 0.08f, 0.8f, Color.parseColor("#2e7424"),Color.parseColor("#65cb57"));
		clip.setOnClickListener(this);

		//myClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);


		//Classes required are Quicksort.java, Individual.java, Transaction.java
		//Create a,pos,neg tr, npos1,nneg1,numtr1 arrays
		int n = state.Names.length;
		Individual[] a = new Individual[n];
		Individual[] pos = new Individual[n];
		Individual[] neg = new Individual[n];
		Transaction[] tr = new Transaction[50];
		int[] npos1 = new int[1];
		int[] nneg1 = new int[1];
		int[] numtr1 = new int[1];

		//Initialize a only
		for(int i=0;i<n;i++)
		{
			a[i] = new Individual();
			a[i].nperson = i;
			a[i].amt = state.totalconts[i];
		}

		//Use Quicksort class to do Quick sort and to compute transactions		
		Quicksort.sort(n,a,pos,neg,tr,npos1,nneg1, numtr1);

		//Get the integers
		numtr = numtr1[0];

		if(numtr!=0)
		{
			for (int i = 0; i < numtr; i++) {
				RowItem5 item = new RowItem5(tr[i].pgiv,String.format("%.2f", tr[i].amt),tr[i].ptke);
				rowItems5.add(item);
			}

			adapter5 = new CustomListViewAdapter5(this, R.layout.transactionelement,
					rowItems5);
			trans.setAdapter(adapter5);
		}
		else
		{
			trans.setAdapter(new ArrayAdapter<String>(this, R.layout.no_transaction_item, new String[]{"No Transactions Required"}));

			trans.setDivider(null);
		}

		//Final Contributions
		ListView result = (ListView)findViewById(R.id.tranlist2);
		rowItems = new ArrayList<RowItem3>();		
		for (int i = 0; i < state.Names.length; i++) {
			RowItem3 item = new RowItem3(state.Names[i], state.totalconts[i]);
			rowItems.add(item);
		}
		CustomListViewAdapter3 adapter = new CustomListViewAdapter3(this, R.layout.resultelement,
				rowItems);
		result.setAdapter(adapter);
		/*try {
			JSONObject productListObj = new JSONObject();
			productListObj.put("nProducts",""+state.totprod);
			for(int j=0;j<state.discs.length;j++)
			{				
				JSONObject productDiscObj = new JSONObject();
				productDiscObj.put("prodDesc", state.discs[j]).put("prodDate", state.dates[j]);
				for(int i=0;i<state.Names.length;i++)
				{						
					productDiscObj.put(""+i,new JSONObject().put("name", ""+i).put("conts", ""+state.conts[j][i]).put("cweight", ""+state.weights[j][i]));
				}
				productListObj.put(""+j, productDiscObj);				
			}			
			createdbsetup(productListObj);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}

	private void createdbsetup(JSONObject js) throws JSONException {
		// create a manager
		Manager manager;
		try {
		    manager = new Manager(new AndroidContext(this), Manager.DEFAULT_OPTIONS);
		    Log.d ("TAG", "Manager created");
		} catch (IOException e) {
		    Log.e("TAG", "Cannot create manager object");
		    return;
		}
		String dbname = "hello";
		if (!Manager.isValidDatabaseName(dbname)) {
		    Log.e("TAG", "Bad database name");
		    return;
		}
		// create a new database
		Database database;
		try {
		    database = manager.getDatabase(dbname);
		    Log.d ("TAG", "Database created");
		} catch (CouchbaseLiteException e) {
		    Log.e("TAG", "Cannot get database");
		    return;
		}
		// create an empty document
		Document document = database.createDocument();
		// add content to document and write the document to the database
		//JSONObject docContent = new JSONObject().put("first", "jgsdjf");
		try {
		    document.putProperties(JsonHelper.toMap(js));
		    Log.d ("TAG", "Document written to database named " + dbname + " with ID = " + document.getId());
		} catch (CouchbaseLiteException e) {
		    Log.e("TAG", "Cannot write document to database", e);
		}
		// save the ID of the new document
		String docID = document.getId();
		// retrieve the document from the database
		Document retrievedDocument = database.getDocument(docID);
		// display the retrieved document
		Log.d("TAG", "retrievedDocument=" + String.valueOf(retrievedDocument.getProperties()));
	}

	@Override
	public void onClick(View v) {
		if(v == finish)
		{
			canfinish = true;
			Intent intent = new Intent(getApplicationContext(),
					MainActivity.class);
			startActivity(intent);
		}
		else if(v==clip)
		{
			String str = "";
			try {
				// create a File object for the parent directory
				File RDirectory = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Debtonator/");
				// have the object build the directory structure, if needed.
				RDirectory.mkdirs();
				File myFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Debtonator/FinalTransactionsfile.txt");
				FileOutputStream fOut;
				if(!myFile.createNewFile())
				{
					//Toast.makeText(getBaseContext(), "Overwriting the existing fie...",Toast.LENGTH_SHORT).show();
					fOut = new FileOutputStream(myFile,false);
					fOut.write(str.getBytes());
					fOut.close();
				}
				
				JSONObject metadata = new JSONObject();
				JSONObject resultNames = new JSONObject();
				JSONObject resultConts = new JSONObject();
				JSONObject resultTrans = new JSONObject();
				metadata.put("title1","Total Contributions");
				metadata.put("title2","Final Transactions");
				
				str = "Total Contributions\n*******************\n";
				fOut = new FileOutputStream(myFile,true);
				fOut.write(str.getBytes());
				for(int i=0;i<rowItems.size();i++)
				{
					str = rowItems.get(i).getTitle() + " >>>  " + rowItems.get(i).getamount() + "\n\n";
					fOut.write(str.getBytes());
					
					resultNames.put(""+i, rowItems.get(i).getTitle());
					resultConts.put(""+i, rowItems.get(i).getamount());

				}				
				str = "\n\nFinal Transactions\n******************\n";
				fOut.write(str.getBytes());
				for(int i=0;i<rowItems5.size();i++)
				{
					str = state.Names[rowItems5.get(i).getTitle()] + "-->  " + rowItems5.get(i).getTitle1() + " --> " + state.Names[rowItems5.get(i).getTitle2()] + "\n\n";
					fOut.write(str.getBytes());
					
					resultTrans.putOpt(""+i,new JSONObject().put("give",rowItems5.get(i).getTitle()).put("amount",rowItems5.get(i).getTitle1()).put("take",rowItems5.get(i).getTitle2()));

				}
				
				fOut.write(new JSONArray().put(metadata).put(resultNames).put(resultConts).put(resultTrans).toString(2).getBytes());
				
				fOut.close();
				Toast.makeText(getBaseContext(),
						"Stored to "+Environment.getExternalStorageDirectory().getAbsolutePath()+"/Debtonator/FinalTransactionsfile.txt and Productsfile.txt",
						Toast.LENGTH_LONG).show();
				myFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Debtonator/Productsfile.txt");
				if(!myFile.createNewFile())
				{
					//Toast.makeText(getBaseContext(), "Overwriting the existing file...",Toast.LENGTH_SHORT).show();
					str = "";
					fOut = new FileOutputStream(myFile,false);
					fOut.write(str.getBytes());
					fOut.close();
				}
				str = "Summary\n========\n";
				fOut = new FileOutputStream(myFile,true);
				fOut.write(str.getBytes());
				
				JSONObject productListObj = new JSONObject();
				productListObj.put("nProducts",""+state.totprod);
				productListObj.put("names",resultNames);
				str = "Number of Products = " + state.totprod+"\n\n";
				fOut.write(str.getBytes());
				str = "Product Data\n===========\n";	
				fOut.write(str.getBytes());
				for(int j=0;j<state.discs.length;j++)
				{
					str = "Product"+(j+1)+" - "+state.discs[j]+"\n********************************\n";
					
					JSONObject productDiscObj = new JSONObject();
					productDiscObj.put("prodDesc", state.discs[j]).put("prodDate", state.dates[j]);
					
					fOut.write(str.getBytes());
					for(int i=0;i<state.Names.length;i++)
					{						
						str = state.Names[i] + " -->  " + state.conts[j][i] + " --> " + ((state.weights[j][i] == 1)?"Consumer":"Not Consumer") + "\n";
						fOut.write(str.getBytes());
						
						productDiscObj.put(""+i,new JSONObject().put("name", ""+i).put("conts", ""+state.conts[j][i]).put("cweight", ""+state.weights[j][i]));
						
					}
					str = "\n";
					fOut.write(str.getBytes());
					
					productListObj.put(""+j, productDiscObj);
					
				}
				
				fOut.write(productListObj.toString(2).getBytes());
				//Log.d("list", productListObj.toString(2));
				
				fOut.close();
				Intent i = new Intent(Intent.ACTION_SEND_MULTIPLE);
				i.setType("text/plain");
				i.putExtra(Intent.EXTRA_EMAIL  , new String[]{""});
				i.putExtra(Intent.EXTRA_SUBJECT, "Instance Results and Product Data");
				i.putExtra(Intent.EXTRA_TEXT   , "This is the instance data generated by DEBT-O-NATOR...");
				ArrayList<Uri> uris = new ArrayList<Uri>();
				File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Debtonator/Productsfile.txt");
				File file1 = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Debtonator/FinalTransactionsfile.txt");
				if ((!file.exists() || !file.canRead())&&(!file1.exists() || !file1.canRead())) {

				}
				else
				{
					uris.add(Uri.fromFile(file));
					uris.add(Uri.fromFile(file1));
				}
				i.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
				try {
					startActivity(Intent.createChooser(i, "Share Via..."));
				} catch (android.content.ActivityNotFoundException ex) {
					Toast.makeText(TransactionActivity.this, "There are no sharing clients installed.", Toast.LENGTH_SHORT).show();
				}

			} catch (Exception e) {
				Toast.makeText(getBaseContext(), "Unable to access SD Card...\nSorry, could not save...\n",
						Toast.LENGTH_SHORT).show();
			}
			//myClipboard.setText(str);
		}
		else if(v == ext)
		{
			if(state.mandatorysave)
			{
				new AlertDialog.Builder(this)
				.setTitle("Really Exit?")
				.setMessage("Save Work Before Exit?")
				.setNegativeButton("Cancel", null)
				.setNeutralButton("Save",new  DialogInterface.OnClickListener() {

					public void onClick(DialogInterface arg0, int arg1) {
						saveBeforeExit();
					}
				})
				.setPositiveButton("Dont Save", new  DialogInterface.OnClickListener() {

					public void onClick(DialogInterface arg0, int arg1) {
						canfinish = true;
						finish();
					}
				}).create().show();
			}
			else
			{
				new AlertDialog.Builder(this)
				.setTitle("Exiting Application...")
				.setMessage("Really Exit?")
				.setNegativeButton("Cancel", null)
				.setPositiveButton("Exit", new  DialogInterface.OnClickListener() {

					public void onClick(DialogInterface arg0, int arg1) {
						canfinish = true;
						finish();
					}
				}).create().show();
			}
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if(canfinish)
		{
			overridePendingTransition(R.layout.lefttorightin, R.layout.lefttorightout);
			finish();
		}
	}
	@Override
	public void onBackPressed() {
		//finish = (Button) findViewById(R.id.finishButton);
		finish.performClick();
	}

	private void setupmargins()
	{	
		View v;
		//modifying the paddings
		int[] views = {R.id.tranlist1,R.id.alertView1,R.id.imageView2,R.id.finalcontr};
		for(int i = 0;i<views.length;i++)
		{
			v = findViewById(views[i]);
			v.setPadding((int)(v.getPaddingLeft()*state.wsf), (int)(v.getPaddingTop()*state.hsf), (int)(v.getPaddingRight()*state.wsf), (int)(v.getPaddingBottom()*state.hsf));
		}
		//modifying the Text Sizes
		views = new int[]{R.id.transscaletext1,R.id.textView2,R.id.textView3,R.id.finalcontr,R.id.transscaletext2,R.id.transscaletext3};
		for(int i = 0;i<views.length;i++)
		{
			v = findViewById(views[i]);
			((TextView) v).setTextSize(TypedValue.COMPLEX_UNIT_PX,(float) (((TextView) v).getTextSize()*(float)state.tsf));
		}
		//modifying the margins
		views = new int[]{R.id.finishBar};
		for(int i = 0;i<views.length;i++)
		{
			v = findViewById(views[i]);
			ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
			p.setMargins((int) (p.leftMargin*state.wsf), (int) (p.topMargin*state.hsf), (int) (p.rightMargin*state.wsf), (int) (p.bottomMargin*state.hsf));
			v.requestLayout();
		}
	}

	private void saveBeforeExit() {
		state = GlobalVarClass.getInstance();
		if(state.loaded)
		{
			db = new DatabaseHandler(this.getApplicationContext());
			new AlertDialog.Builder(this)
			.setTitle("Saving Instance...")
			.setMessage("Overwrite the Existing Instance?")
			.setPositiveButton("Overwrite and Exit", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					//Overwrite the current
					db.open();
					db.saveThisInstance("",true);//true for rewriting
					db.close();
					Toast.makeText(TransactionActivity.this, "Overwritten and Saved", Toast.LENGTH_SHORT).show();
					canfinish = true;
					finish();
				}
			})
			.setNeutralButton("Save as New Instance", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					//Create the new instance save dialog
					savenewExit();
				}
			})
			.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					//do nothing
				}
			})
			.show();
			//DatabaseHandler.deletedb();
		}
		else
			savenewExit();
	}

	private void savenewExit() {
		state = GlobalVarClass.getInstance();
		db = new DatabaseHandler(this.getApplicationContext());
		final EditText input = new EditText(this);
		input.setSingleLine();
		new AlertDialog.Builder(this)
		.setTitle("Saving Instance...")
		.setMessage("Enter the New Instance Name")
		.setView(input)
		.setPositiveButton("Save", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				Editable value = input.getText();
				db.open();
				db.saveThisInstance(value.toString(),false);
				db.close();
				state.loaded = true;
				//updateinstname();
				Toast.makeText(TransactionActivity.this, "New Instance Saved", Toast.LENGTH_SHORT).show();
				canfinish = true;
				finish();
			}
		})
		.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				//do nothing
			}
		})
		.show();
	}


}
