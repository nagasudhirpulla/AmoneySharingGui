package is.a.amoneysharinggui;
import java.lang.String;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.text.Editable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnItemClickListener,
OnClickListener {

	private static MainActivity mainact = null;
	TextView txt;
	ListView ProductList, MemberList;
	GlobalVarClass state;
	float weightsum = 0;
	List<ProductListItem> rowItems;
	DatabaseHandler db;
	int enteranim = R.layout.lefttorightin;
	int exitanim = R.layout.lefttorightout;
	int width,height,dens;
	double wsf;
	boolean canfinish = false;

	public static MainActivity getInstance() {
		return mainact;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//opening transition animations
		//overridePendingTransition(enteranim, exitanim);
		mainact = this;
		state = ((GlobalVarClass) getApplicationContext());
		setContentView(R.layout.welcomescr1);		
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		width=dm.widthPixels;
		height=dm.heightPixels;
		dens=dm.densityDpi;
		double wi=(double)width/(double)dens;//width in inches
		double hi=(double)height/(double)dens;//height in inches
		wsf = ((double)width*240)/(480*(double)dens);
		double hsf = ((double)height*240)/(800*(double)dens);
		state.wsf = wsf;
		state.hsf = hsf;
		state.tsf = (hsf<wsf?hsf:wsf);
		//Log.d("Screen DPI",String.valueOf(dens));
		//Log.d("Screen Height",String.valueOf(height));
		//Log.d("Screen Width",String.valueOf(width));
		//Log.d("Height",String.valueOf(hi));
		//Log.d("Width",String.valueOf(wi));
		//Log.d("wsf",String.valueOf(wsf));
		//Log.d("hsf",String.valueOf(hsf));
		if(state.wsf != 1)
		{
			setupmargins();
		}		
		//Title bar gradient
		GradientDrawable gd = new GradientDrawable(
				GradientDrawable.Orientation.TOP_BOTTOM,
				new int[]{Color.parseColor("#444444"), Color.parseColor("#111111"),Color.BLACK});
		gd.setCornerRadius(0f);
		View title = getWindow().findViewById(android.R.id.title);
		View titleBar = (View) title.getParent();
		titleBar.setBackgroundDrawable(gd);
		titleBar.getLayoutParams().height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) (35*GlobalVarClass.getInstance().hsf), getResources().getDisplayMetrics());
		((TextView) title).setTextSize((float) (20*state.tsf));
		ProductList = (ListView) findViewById(R.id.productlist);
		MemberList = (ListView) findViewById(R.id.memberlist);
		//Add the AddProduct Button
		final FrameLayout add = (FrameLayout) findViewById(R.id.productadd);
		//setupbackgrnd(add);
		new SetBackGrnd(add,false, true, 0.08f, 0.8f, Color.parseColor("#003748"),Color.parseColor("#335F6D"));
		add.setOnClickListener(this);
		//Add the MemberEdit Button
		final FrameLayout add_mem = (FrameLayout) findViewById(R.id.editmem);
		//setupbackgrnd(add_mem);
		new SetBackGrnd(add_mem,false, true, 0.08f, 0.8f, Color.parseColor("#033076"),Color.parseColor("#355991"));
		add_mem.setOnClickListener(this);
		//Add the save button
		final FrameLayout save = (FrameLayout) findViewById(R.id.savebutton);
		//setupbackgrnd(save);
		new SetBackGrnd(save,false, true, 0.08f, 0.8f, Color.parseColor("#2F073B"),Color.parseColor("#593962"));
		save.setOnClickListener(this);
		//Add the load button
		final FrameLayout load = (FrameLayout) findViewById(R.id.loadbutton);
		//setupbackgrnd(load);
		new SetBackGrnd(load,false, true, 0.08f, 0.8f, Color.parseColor("#2F073B"),Color.parseColor("#593962"));
		load.setOnClickListener(this);
		//Add the Solve button
		final FrameLayout solve = (FrameLayout) findViewById(R.id.solvebutton);
		//setupbackgrnd(solve);
		new SetBackGrnd(solve,false, true, 0.08f, 0.8f, Color.parseColor("#243E16"),Color.parseColor("#506545"));
		solve.setOnClickListener(this);
		setupUI(findViewById(R.id.LinearLayoutW));
		String[] Names = state.Names;
		rowItems = new ArrayList<ProductListItem>();
		for(int i=0;i<state.totprod;i++)
		{
			double value = 0;
			for(int j=0;j<state.Names.length;j++)
			{
				value =  value + state.conts[i][j];
			}
			if(state.discs[i].length()==0)
			{

				if(value != 0)
				{
					ProductListItem item = new ProductListItem(""+(i+1), String.format("%.2f", value), state.dates[i]);
					rowItems.add(item);
					//Product[i] = (i+1) + " \n$"+ String.format("%.2f", value);
				}
				else
				{
					ProductListItem item = new ProductListItem(""+(i+1), "Description", state.dates[i]);
					rowItems.add(item);
					//Product[i] = (i+1) + "\n";
				}
			}
			else
			{
				if(value != 0)
				{
					ProductListItem item = new ProductListItem(""+(i+1), ""+state.discs[i], state.dates[i], String.format("%.2f", value));
					rowItems.add(item);
					//Product[i] = (i+1) + " \n"+ state.discs[i];
				}
				else
				{
					ProductListItem item = new ProductListItem(""+(i+1), ""+state.discs[i], state.dates[i]);
					rowItems.add(item);
				}
			}

		}
		ProductList.setAdapter(new CustomProdListAdapter(this, R.layout.glasscell, rowItems));
		ProductList.setOnItemClickListener(this);
		MemberList.setAdapter(new MemlistAdapter(this,R.layout.member_cell, new ArrayList<String>(Arrays.asList(Names))));
		MemberList.setOnItemClickListener(this);		
		updateinstname();
		/*db = new DatabaseHandler(this.getApplicationContext());
		DatabaseHandler.deletedb();*/
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		GlobalVarClass state = ((GlobalVarClass) getApplicationContext());
		if (arg0.getId() == R.id.productlist) {

			canfinish = true;
			Intent intent = new Intent(getApplicationContext(),
					ProductEditingActivity.class);			
			state.currentproduct = arg2 + 1;
			enteranim = R.layout.righttoleftin;
			exitanim = R.layout.righttoleftout;
			startActivity(intent);
		}
		if (arg0.getId() == R.id.memberlist) {
			canfinish = true;
			Intent intent = new Intent(getApplicationContext(),
					InfoActivity.class);
			state.currentmemedit = arg2;
			enteranim = R.layout.fadeout;
			exitanim = R.layout.fadein;
			startActivity(intent);
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		//closing transition animations
		if(canfinish)
		{
			overridePendingTransition(enteranim, exitanim);
			finish();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent;
		if(v.getId() == R.id.solvebutton)
		{
			canfinish = true;
			intent = new Intent(getApplicationContext(),
					TransactionActivity.class);
			enteranim = R.layout.righttoleftin;
			exitanim = R.layout.righttoleftout;
			startActivity(intent);
		}
		else if(v.getId() == R.id.savebutton)
		{
			state = GlobalVarClass.getInstance();
			if(state.loaded)
			{
				db = new DatabaseHandler(this.getApplicationContext());
				new AlertDialog.Builder(this)
				.setTitle("Saving Instance...")
				.setMessage("Overwrite the Existing Instance?")
				.setPositiveButton("Yes, Overwrite", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						//Overwrite the current
						db.open();
						db.saveThisInstance("",true);//true for rewriting
						db.close();
						state.mandatorysave = false;
						Toast.makeText(MainActivity.this, "Overwritten and Saved", Toast.LENGTH_SHORT).show();
						updateinstname();
					}
				})
				.setNeutralButton("No, Create New", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						//Create the new instance save dialog
						savenew();
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
				savenew();

		}
		else if(v.getId() == R.id.loadbutton)
		{
			boolean x = true;
			//DatabaseHandler db1 = new DatabaseHandler(this.getApplicationContext());
			//db1.open();
			//boolean x = db1.loadThisInstance(0);
			//db1.close();
			//DatabaseHandler.deletedb();			
			if(x)
			{
				canfinish = true;
				intent = new Intent(getApplicationContext(),
						InstanceLoadActivity.class);
				enteranim = R.layout.fadeout;
				exitanim = R.layout.fadein;
				startActivity(intent);
			}
			else
			{
				Toast.makeText(this.getApplicationContext(), "NO DATABASE EXISTS", Toast.LENGTH_SHORT).show();
			}
		}
		else if(v.getId()==R.id.productadd)
		{
			canfinish = true;
			state = GlobalVarClass.getInstance();
			intent = new Intent(getApplicationContext(),
					ProductEditingActivity.class);			
			state.inc_totprod();
			state.currentproduct = state.totprod;
			for (int i = 0; i < state.Names.length; i++) {
				state.conts[state.totprod-1][i] = 0;
				state.weights[state.totprod-1][i] = 1;
			}
			state.numberofbenf[state.totprod-1] = state.Names.length;
			enteranim = R.layout.righttoleftin;
			exitanim = R.layout.righttoleftout;
			startActivity(intent);
		}
		else if(v.getId() == R.id.editmem) 
		{
			canfinish = true;
			intent = new Intent(getApplicationContext(),
					MemberEditingActivity.class);
			enteranim = R.layout.righttoleftin;
			exitanim = R.layout.righttoleftout;
			startActivity(intent);
		}
	}

	@Override
	public void onBackPressed() {
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

	private void savenew() {
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
				try{
					db.open();
					db.saveThisInstance(value.toString(),false);
					db.close();
				}
				catch(Exception e)
				{
					DatabaseHandler.deletedb();
				}
				state.loaded = true;
				state.mandatorysave = false;
				updateinstname();
				Toast.makeText(MainActivity.this, "New Instance Saved", Toast.LENGTH_SHORT).show();
			}
		})
		.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				//do nothing
			}
		})
		.show();
	}

	private void updateinstname()
	{
		txt = (TextView) findViewById(R.id.curinstnme);
		if(state.loaded)
		{
			db = new DatabaseHandler(this.getApplicationContext());
			db.open();			
			String str = db.getcurinstname();
			if(state.mandatorysave)
				str = "*"+str;
			db.close();
			txt.setText("Instance Loaded : " + str);			
		}
		else
		{
			txt.setText("No Instance loaded");
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
					Toast.makeText(MainActivity.this, "Overwritten and Saved", Toast.LENGTH_SHORT).show();
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
				updateinstname();
				Toast.makeText(MainActivity.this, "New Instance Saved", Toast.LENGTH_SHORT).show();
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
	public void setupUI(View view) {
		//Set up touch listener for non-text box views to hide keyboard.
		if((view instanceof TextView)) {
			((TextView) view).setTextSize(TypedValue.COMPLEX_UNIT_PX,(float) (((TextView) view).getTextSize()*(float)state.tsf));
		}
		//If a layout container, iterate over children and seed recursion.
		if (view instanceof ViewGroup) {

			for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
				View innerView = ((ViewGroup) view).getChildAt(i);
				setupUI(innerView);
			}
		}
	}

	private void setupmargins()
	{	
		View v;
		//modifying the paddings
		int[] views = {R.id.LinearLayoutW};
		for(int i = 0;i<views.length;i++)
		{
			v = findViewById(views[i]);
			v.setPadding((int)(v.getPaddingLeft()*state.wsf), (int)(v.getPaddingTop()*state.hsf), (int)(v.getPaddingRight()*state.wsf), (int)(v.getPaddingBottom()*state.hsf));
		}		
	}

}
