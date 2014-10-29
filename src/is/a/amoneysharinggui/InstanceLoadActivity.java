package is.a.amoneysharinggui;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.text.Editable;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;

public class InstanceLoadActivity extends Activity implements OnClickListener {

	GlobalVarClass state;
	ListView instlist;
	FrameLayout hme,blhme;
	List<Instnmeitem> instItems;
	List<Instnmeitem> instNms;
	InstanceAdapter adapter;
	DatabaseHandler db;
	boolean canfinish = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//overridePendingTransition(R.layout.fadeout, R.layout.fadein);
		setContentView(R.layout.instanceloadscreen1);
		state = GlobalVarClass.getInstance();
		//setup the size changes
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

		//Add the Home button
		hme = (FrameLayout) findViewById(R.id.insthme);
		//setupbackgrnd(hme);
		new SetBackGrnd(hme,false, true, 0.08f, 0.8f, Color.parseColor("#00661D"),Color.parseColor("#33854A"));
		hme.setOnClickListener(this);
		//Add the Blank Instance Load button
		blhme = (FrameLayout) findViewById(R.id.blankhme);
		//setupbackgrnd(blhme);
		new SetBackGrnd(blhme,false, true, 0.08f, 0.8f, Color.parseColor("#000000"),Color.parseColor("#313131"));
		blhme.setOnClickListener(this);
		instNms = new ArrayList<Instnmeitem>();
		db = new DatabaseHandler(this.getApplicationContext());
		db.open();
		instNms = db.getAllInstances1();
		db.close();
		adapter = new InstanceAdapter(this, R.layout.instcell1, instNms);
		instlist = (ListView)findViewById(R.id.instlstVw);
		instlist.setAdapter(adapter);
		instlist.setItemsCanFocus(true);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v == hme)
		{
			state = GlobalVarClass.getInstance();
			//check if current instance is deleted
			if(state.loaded)
			{
				boolean delleted  = true;
				for(int i=0;i<adapter.getCount();i++)
				{
					if(state.curload == adapter.getItem(i).getId())
					{
						delleted = false;
					}
				}
				if(delleted)
				{
					//creating a blank unloaded case
					state.makeblank();

				}
			}
			canfinish = true;
			Intent intent = new Intent(getApplicationContext(),
					MainActivity.class);
			startActivity(intent);
		}
		else if(v == blhme)
		{
			state = GlobalVarClass.getInstance();
			if(state.loaded)
			{
				if(state.mandatorysave)
				{
					//
					new AlertDialog.Builder(this)
					.setTitle("Loading Blank Instance...")
					.setMessage("Save the Changes to Currently loaded Instance?")
					.setNegativeButton("DONT SAVE", new  DialogInterface.OnClickListener() {
						public void onClick(DialogInterface arg0, int arg1) {
							state.makeblank();
							state.mandatorysave = false;
							canfinish = true;
							Intent intent = new Intent(getApplicationContext(),
									MainActivity.class);
							startActivity(intent);
						}
					})
					.setPositiveButton("SAVE", new  DialogInterface.OnClickListener() {
						public void onClick(DialogInterface arg0, int arg1) {					
							db = new DatabaseHandler(getApplicationContext());
							db.open();
							db.saveThisInstance("",true);//true for rewriting
							db.close();
							Toast.makeText(getApplicationContext(), "Changes Saved", Toast.LENGTH_SHORT).show();
							state.makeblank();
							state.mandatorysave = false;
							canfinish = true;
							Intent intent = new Intent(getApplicationContext(),
									MainActivity.class);
							startActivity(intent);

						}
					}).create().show();
					//
				}
				else
				{
					state.makeblank();
					canfinish = true;
					Intent intent = new Intent(getApplicationContext(),
							MainActivity.class);
					startActivity(intent);
				}
			}
			else
			{
				if(state.mandatorysave)
				{
					//
					new AlertDialog.Builder(this)
					.setTitle("Loading Blank Instance...")
					.setMessage("Save the current work?")
					.setNegativeButton("DONT SAVE", new  DialogInterface.OnClickListener() {
						public void onClick(DialogInterface arg0, int arg1) {
							state.makeblank();
							canfinish = true;
							Intent intent = new Intent(getApplicationContext(),
									MainActivity.class);
							startActivity(intent);						
						}
					})
					.setPositiveButton("SAVE", new  DialogInterface.OnClickListener() {
						public void onClick(DialogInterface arg0, int arg1) {					
							//state = GlobalVarClass.getInstance();
							savenew();
						}
					}).create().show();
					//
				}
				else
				{
					state.makeblank();
					canfinish = true;
					Intent intent = new Intent(getApplicationContext(),
							MainActivity.class);
					startActivity(intent);
				}
			}

		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if(canfinish)
		{
			if(adapter.transition)
				overridePendingTransition(R.layout.fadeoutgo, R.layout.fadeingo);
			else
				overridePendingTransition(0,0);
			finish();
		}
	}
	@Override
	public void onBackPressed() {
		hme.performClick();
	}

	private void savenew() {
		state = GlobalVarClass.getInstance();		
		db = new DatabaseHandler(getApplicationContext());
		final EditText input = new EditText(this);
		input.setSingleLine();
		new AlertDialog.Builder(this)
		.setTitle("Saving Instance...")
		.setMessage("Enter the New Instance Name")
		.setView(input)
		.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				Editable value = input.getText();
				db.open();
				db.saveThisInstance(value.toString(),false);
				db.close();
				state.makeblank();
				state.mandatorysave = false;
				canfinish = true;
				Intent intent = new Intent(getApplicationContext(),
						MainActivity.class);
				startActivity(intent);			
			}
		})
		.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				//do nothing
			}
		})
		.show();
	}

	private void setupmargins()
	{	
		View v;
		//modifying the paddings
		int[] views = {R.id.loadbar};
		for(int i = 0;i<views.length;i++)
		{
			v = findViewById(views[i]);
			v.setPadding((int)(v.getPaddingLeft()*state.wsf), (int)(v.getPaddingTop()*state.hsf), (int)(v.getPaddingRight()*state.wsf), (int)(v.getPaddingBottom()*state.hsf));
		}
		//modifying the margins
		views = new int[]{R.id.loadbar};
		for(int i = 0;i<views.length;i++)
		{
			v = findViewById(views[i]);
			if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
				ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
				p.setMargins((int) (p.leftMargin*state.wsf), (int) (p.topMargin*state.hsf), (int) (p.rightMargin*state.wsf), (int) (p.bottomMargin*state.hsf));
				v.requestLayout();
			}
		}
		//modifying the Text Sizes
		views = new int[]{R.id.blanktext,R.id.hometext};
		for(int i = 0;i<views.length;i++)
		{
			v = findViewById(views[i]);
			((TextView) v).setTextSize(TypedValue.COMPLEX_UNIT_PX,(float) (((TextView) v).getTextSize()*(float)state.tsf));
		}
	}
}
