package is.a.amoneysharinggui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;

public class ProductEditingActivity extends Activity implements OnClickListener, OnFocusChangeListener,
OnEditorActionListener {

	boolean dlt = false;

	public static final Integer[] images = { R.drawable.cross, R.drawable.tick };
	public static final Integer[] alimages = { R.drawable.no_alert_small, R.drawable.alert_small, R.drawable.indicator_input_error };
	ListView plist;
	List<RowItem2> rowItems;
	String[] titles;
	float[] conts;
	RowItem2 it;
	CustomProductAdapter adapter;
	TextView maxproducts;
	ImageButton up,down;
	LinearLayout date;
	TextView dte;
	FrameLayout dlte,add;
	FrameLayout ok, calculate;
	EditText text, descriptionvar;
	GlobalVarClass state;
	static final int DATE_PICKER_ID = 1111;
	public static final String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		state = ((GlobalVarClass) getApplicationContext());
		setContentView(R.layout.productediting2);
		if(state.wsf != 1)
		{
			setupmargins();
		}
		setupUI(findViewById(R.id.LinearLayoutP));
		GradientDrawable gd = new GradientDrawable(
				GradientDrawable.Orientation.TOP_BOTTOM,
				new int[]{Color.parseColor("#335F6D"),Color.parseColor("#003748")});
		gd.setCornerRadius(0f);
		View title = getWindow().findViewById(android.R.id.title);
		View titleBar = (View) title.getParent();
		titleBar.setBackgroundDrawable(gd);
		titleBar.getLayoutParams().height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) (35*GlobalVarClass.getInstance().hsf), getResources().getDisplayMetrics());
		((TextView) title).setTextSize((float) (20*state.tsf));
		state.mandatorysave = true;
		date = (LinearLayout) findViewById(R.id.button3);
		dte = (TextView) findViewById(R.id.dte);
		dte.setText(state.dates[state.currentproduct-1]);
		date.setOnClickListener(this);
		//Add the home button
		ok = (FrameLayout) findViewById(R.id.productok);
		ok.setTag("HOME");
		new SetBackGrnd(ok,true, true, 0.08f, 0.8f, Color.parseColor("#000000"),Color.parseColor("#313131"));
		//Add the Delete Product button
		dlte = (FrameLayout) findViewById(R.id.deletebutton);
		new SetBackGrnd(dlte,false, true, 0.1f, 0.8f, Color.parseColor("#dd0000"),Color.parseColor("#dd3131"));
		dlte.setOnClickListener(this);
		//Add the Add Product button
		add = (FrameLayout) findViewById(R.id.addebutton);
		new SetBackGrnd(add,false, true, 0.1f, 0.8f, Color.parseColor("#004E63"),Color.parseColor("#337182"));
		add.setOnClickListener(this);
		maxproducts = (TextView) findViewById(R.id.maxprod);
		maxproducts.setText("" + state.totprod);
		text = (EditText) findViewById(R.id.currentprod);
		text.setText("" + state.currentproduct);
		text.setSelection(text.length());
		text.setOnFocusChangeListener(this);
		text.setOnClickListener(this);
		text.setOnEditorActionListener(this);
		titles = state.Names;
		rowItems = new ArrayList<RowItem2>();
		String str;
		conts = state.conts[state.currentproduct - 1];
		for (int i = 0; i < titles.length; i++) {			
			if(conts[i] == 0)
				str = "";
			else 
				str = "" + conts[i];
			if(str.endsWith(".0"))
			{
				str = str.substring(0, str.length()-2);
			}
			if (state.weights[state.currentproduct - 1][i] == 0) {
				RowItem2 item = new RowItem2(str, titles[i],images[0], alimages[0]);
				rowItems.add(item);
			} else {
				RowItem2 item = new RowItem2(str, titles[i],images[1], alimages[0]);
				rowItems.add(item);
			}
		}
		plist = (ListView) findViewById(R.id.membercont);
		adapter = new CustomProductAdapter(this, R.layout.cell1,
				rowItems);
		plist.setAdapter(adapter);

		plist.setItemsCanFocus(true);

		ok.setOnClickListener(adapter);

		//Add the Calculate Button
		calculate = (FrameLayout) findViewById(R.id.producthome);
		calculate.setTag("CALCULATE");

		calculate.setOnClickListener(adapter);
		new SetBackGrnd(calculate,true, false, 0.08f, 0.9f, Color.parseColor("#243E16"),Color.parseColor("#506545"));
		descriptionvar = (EditText) findViewById(R.id.discedtTxt);
		descriptionvar.setText(state.discs[state.currentproduct - 1]);
		descriptionvar.setSelection(descriptionvar.length());
		descriptionvar.addTextChangedListener(new TextWatcher()
		{

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				state = ((GlobalVarClass) getApplicationContext());
				// TODO Auto-generated method stub
				state.discs[state.currentproduct - 1] = s.toString();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

		});
		up = (ImageButton) findViewById(R.id.button2);
		down = (ImageButton) findViewById(R.id.button1);
		new SetBackGrnd(up,false, true, 0.1f, 0.8f, Color.parseColor("#004E63"),Color.parseColor("#337182"));
		new SetBackGrnd(down,false, true, 0.1f, 0.8f, Color.parseColor("#004E63"),Color.parseColor("#337182"));
		up.setOnClickListener(this);
		down.setOnClickListener(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if(adapter.canfinish)
		{
			overridePendingTransition(adapter.enteranim, adapter.exitanim);
			finish();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		state = GlobalVarClass.getInstance();
		if (v.getId() == R.id.productok) {

		}
		else if (v.getId() == R.id.currentprod) {
			//v.requestFocus();			
		}
		else if (v == dlte) {
			if(state.totprod > 1)
			{	
				new AlertDialog.Builder(this)
				.setTitle("Delete Product?")
				.setMessage("Are you sure you want to delete this product?")
				.setNegativeButton(android.R.string.no, null)
				.setPositiveButton("Delete", new  DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
						dlt = true;
						ProductEditingActivity.this.performdeletion();
					}
				}).create().show();				
			}
		}
		else if(v==add)
		{
			if(adapter.dopassanalysis())
			{	
				state.inc_totprod();
				state.currentproduct = state.totprod;
				for (int i = 0; i < state.Names.length; i++) {
					state.conts[state.totprod-1][i] = 0;
					state.weights[state.totprod-1][i] = 1;
				}
				state.numberofbenf[state.totprod-1] = state.Names.length;
				text.setText("" + state.currentproduct);
				text.setSelection(text.length());
				dte.setText(state.dates[state.currentproduct-1]);
				descriptionvar.setText(state.discs[state.currentproduct - 1]);
				descriptionvar.setSelection(descriptionvar.length());
				maxproducts.setText("" + state.totprod);
				rowItems = new ArrayList<RowItem2>();
				String str;
				conts = state.conts[state.currentproduct - 1];
				for (int i = 0; i < titles.length; i++) {			
					if(conts[i] == 0)
						str = "";
					else 
						str = "" + conts[i];
					if(str.endsWith(".0"))
					{
						str = str.substring(0, str.length()-2);
					}
					if (state.weights[state.currentproduct - 1][i] == 0) {
						RowItem2 item = new RowItem2(str, titles[i],images[0], alimages[0]);
						rowItems.add(item);
					} else {
						RowItem2 item = new RowItem2(str, titles[i],images[1], alimages[0]);
						rowItems.add(item);
					}
				}				
				plist = (ListView) findViewById(R.id.membercont);
				adapter = new CustomProductAdapter(this, R.layout.cell,
						rowItems);
				plist.setAdapter(adapter);
				plist.setItemsCanFocus(true);
				ok.setOnClickListener(adapter);
				calculate.setOnClickListener(adapter);			
				Toast.makeText(this.getApplicationContext(), "New Product Added", Toast.LENGTH_SHORT).show();
			}
		}
		else if(v==up||v==down)
		{
			if((v==down&&state.currentproduct>1)||(v==up&&state.currentproduct<state.totprod))
			{
				if(adapter.dopassanalysis())
				{
					if(v==up)
						state.currentproduct = state.currentproduct+1;
					else
						state.currentproduct = state.currentproduct-1;
					text.setText("" + state.currentproduct);
					text.setSelection(text.length());
					dte.setText(state.dates[state.currentproduct-1]);
					descriptionvar.setText(state.discs[state.currentproduct - 1]);
					descriptionvar.setSelection(descriptionvar.length());
					rowItems = new ArrayList<RowItem2>();
					String str;
					conts = state.conts[state.currentproduct - 1];
					for (int i = 0; i < titles.length; i++) {			
						if(conts[i] == 0)
							str = "";
						else 
							str = "" + conts[i];
						if(str.endsWith(".0"))
						{
							str = str.substring(0, str.length()-2);
						}
						if (state.weights[state.currentproduct - 1][i] == 0) {
							RowItem2 item = new RowItem2(str, titles[i],images[0], alimages[0]);
							rowItems.add(item);
						} else {
							RowItem2 item = new RowItem2(str, titles[i],images[1], alimages[0]);
							rowItems.add(item);
						}
					}				
					plist = (ListView) findViewById(R.id.membercont);
					adapter = new CustomProductAdapter(this, R.layout.cell,
							rowItems);
					plist.setAdapter(adapter);
					plist.setItemsCanFocus(true);
					ok.setOnClickListener(adapter);
					calculate.setOnClickListener(adapter);
				}
			}

		}
		else if(v == date)
		{
			showDialog(DATE_PICKER_ID);
		}
	}

	protected void performdeletion() {
		state = GlobalVarClass.getInstance();
		Toast.makeText(this.getApplicationContext(), "DELETED", Toast.LENGTH_SHORT).show();
		state.deleteproductfun();
		descriptionvar = (EditText) findViewById(R.id.discedtTxt);
		descriptionvar.setText(state.discs[state.currentproduct - 1]);
		descriptionvar.setSelection(descriptionvar.length());
		text = (EditText) findViewById(R.id.currentprod);
		text.setText("" + state.currentproduct);
		text.setSelection(text.length());
		dte.setText(state.dates[state.currentproduct-1]);
		maxproducts = (TextView) findViewById(R.id.maxprod);
		maxproducts.setText("" + state.totprod);		
		titles = state.Names;
		String str;
		conts = state.conts[state.currentproduct - 1];
		rowItems = new ArrayList<RowItem2>();
		for (int i = 0; i < titles.length; i++) {			
			if(conts[i] == 0)
				str = "";
			else 
				str = "" + conts[i];
			if(str.endsWith(".0"))
			{
				str = str.substring(0, str.length()-2);
			}
			if (state.weights[state.currentproduct - 1][i] == 0) {
				RowItem2 item = new RowItem2(str, titles[i],images[0], alimages[0]);
				rowItems.add(item);
			} else {
				RowItem2 item = new RowItem2(str, titles[i],images[1], alimages[0]);
				rowItems.add(item);
			}
		}
		plist = (ListView) findViewById(R.id.membercont);
		adapter = new CustomProductAdapter(this, R.layout.cell,
				rowItems);
		plist.setAdapter(adapter);

		plist.setItemsCanFocus(true);

		//ok = (Button) findViewById(R.id.productok);

		ok.setOnClickListener(adapter);

		//calculate = (Button) findViewById(R.id.producthome);

		calculate.setOnClickListener(adapter);


	}

	@Override
	public void onFocusChange(View v, boolean focus) {
		// TODO Auto-generated method stub
		if (focus == false) {
			if (v.getId() == R.id.currentprod) {
				state = GlobalVarClass.getInstance();
				EditText text = (EditText) v;
				text.setText("" + state.currentproduct);				
			}
		}

	}

	@Override
	public boolean onEditorAction(TextView arg0, int actionId, KeyEvent event) {
		// TODO Auto-generated method stub
		if ((actionId == EditorInfo.IME_ACTION_DONE||event.getKeyCode() == KeyEvent.KEYCODE_ENTER) && arg0.getId() == R.id.currentprod) {
			state = GlobalVarClass.getInstance();
			EditText text = (EditText) arg0;
			int t = 0;
			if(text.length() != 0)
				t = Integer.parseInt(text.getText().toString());

			if (t > state.totprod || t<1 || adapter.isgood()==false) {
				text.setText("" + state.currentproduct);
				text.setSelection(text.length());
				adapter.notifyDataSetChanged();
			} else {
				state.currentproduct = t;
				descriptionvar = (EditText) findViewById(R.id.discedtTxt);
				descriptionvar.setText(state.discs[state.currentproduct - 1]);
				dte.setText(state.dates[state.currentproduct-1]);
				titles = state.Names;
				rowItems = new ArrayList<RowItem2>();
				String str;
				conts = state.conts[state.currentproduct - 1];
				for (int i = 0; i < titles.length; i++) {			
					if(conts[i] == 0)
						str = "";
					else 
						str = "" + conts[i];
					if(str.endsWith(".0"))
					{
						str = str.substring(0, str.length()-2);
					}
					if (state.weights[state.currentproduct - 1][i] == 0) {
						RowItem2 item = new RowItem2(str, titles[i],images[0], alimages[0]);
						rowItems.add(item);
					} else {
						RowItem2 item = new RowItem2(str, titles[i],images[1], alimages[0]);
						rowItems.add(item);
					}
				}
				plist = (ListView) findViewById(R.id.membercont);
				adapter = new CustomProductAdapter(this, R.layout.cell,
						rowItems);
				plist.setAdapter(adapter);

				plist.setItemsCanFocus(true);

				//ok = (Button) findViewById(R.id.productok);

				ok.setOnClickListener(adapter);

				//calculate = (Button) findViewById(R.id.producthome);

				calculate.setOnClickListener(adapter);
			}
		}
		return false;
	}
	@Override
	public void onBackPressed() {
		//ok = (Button) findViewById(R.id.productok);
		ok.performClick();
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_PICKER_ID:
			// open datepicker dialog. 
			// set date picker for current date 
			// add pickerListener listner to date picker 
			return new DatePickerDialog(this, pickerListener, Integer.parseInt(state.dates[state.currentproduct-1].substring(7, 11)), Arrays.asList(MONTHS).indexOf(state.dates[state.currentproduct-1].substring(3, 6)),Integer.parseInt(state.dates[state.currentproduct-1].substring(0, 2)));
		}
		return null;
	}

	@Override
	public void onPrepareDialog(int dialogId,Dialog d){
		super.onPrepareDialog(dialogId, d);
		// change something inside already created Dialogs here
		switch (dialogId) {
		case DATE_PICKER_ID:
			// open datepicker dialog. 
			// set date picker for current date 
			// add pickerListener listner to date picker
			((DatePickerDialog)d).updateDate(Integer.parseInt(state.dates[state.currentproduct-1].substring(7, 11)), Arrays.asList(MONTHS).indexOf(state.dates[state.currentproduct-1].substring(3, 6)),Integer.parseInt(state.dates[state.currentproduct-1].substring(0, 2)));
		}
	}

	private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {
		// when dialog box is closed, below method will be called.
		@Override
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {
			boolean needed = false;
			if(state.dates[state.currentproduct-1] == state.lastdate || state.dates[state.currentproduct-1] == state.firstdate)
				needed = true;
			// Show selected date
			state.dates[state.currentproduct-1] = new StringBuilder().append(String.format("%02d", selectedDay))
					.append("/").append(MONTHS[selectedMonth]).append("/").append(selectedYear).toString();
			dte.setText(state.dates[state.currentproduct-1]);
			if(needed)
				state.updatedatevals();
			else
				state.checkdate(state.dates[state.currentproduct-1]);
		}
	};

	public void setupUI(View view) {
		//Set up touch listener for non-text box views to hide keyboard.
		if(!(view instanceof EditText)) {

			view.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
					// TODO Auto-generated method stub
					hideSoftKeyboard();
					return false;
				}

			});
		}
		//If a layout container, iterate over children and seed recursion.
		if (view instanceof ViewGroup) {

			for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

				View innerView = ((ViewGroup) view).getChildAt(i);

				setupUI(innerView);
			}
		}
	}

	protected void hideSoftKeyboard() {
		// TODO Auto-generated method stub
		InputMethodManager inputMethodManager = (InputMethodManager)  this.getSystemService(Activity.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
	}

	private void setupmargins()
	{	
		View v;
		//modifying the paddings
		int[] views = {R.id.productscaleimage1,R.id.button2,R.id.button1,R.id.prodcost,R.id.discedtTxt,R.id.listlyt,R.id.productscalelay1};
		for(int i = 0;i<views.length;i++)
		{
			v = findViewById(views[i]);
			v.setPadding((int)(v.getPaddingLeft()*state.wsf), (int)(v.getPaddingTop()*state.hsf), (int)(v.getPaddingRight()*state.wsf), (int)(v.getPaddingBottom()*state.hsf));
		}
		//modifying the Text Sizes
		views = new int[]{R.id.productscaletext1,R.id.prodcost,R.id.maxprod,R.id.productscaletext2,R.id.productscaletext3,R.id.productscaletext4,R.id.productscaletext5,R.id.productscaletext6,R.id.productscaletext7,R.id.productscaletext8,R.id.dte,R.id.discedtTxt,R.id.currentprod};
		for(int i = 0;i<views.length;i++)
		{
			v = findViewById(views[i]);
			((TextView) v).setTextSize(TypedValue.COMPLEX_UNIT_PX,(float) (((TextView) v).getTextSize()*(float)state.tsf));
		}
		//modifying the margins
		views = new int[]{R.id.maxprod,R.id.productok,R.id.producthome,R.id.dte,R.id.currentprod};
		for(int i = 0;i<views.length;i++)
		{
			v = findViewById(views[i]);
			ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
			p.setMargins((int) (p.leftMargin*state.wsf), (int) (p.topMargin*state.hsf), (int) (p.rightMargin*state.wsf), (int) (p.bottomMargin*state.hsf));
			v.requestLayout();
		}
	}

}