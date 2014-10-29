package is.a.amoneysharinggui;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

public class MemberEditingActivity extends Activity implements OnClickListener{
	GlobalVarClass state;
	FrameLayout memadd;
	FrameLayout ok;
	ListView memlist;
	List<RowItem4> rowItems4;
	CustomListViewAdapter4 adapter4;
	boolean canfinish = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		state = GlobalVarClass.getInstance();
		setContentView(R.layout.memberediting1);
		if(state.wsf != 1)
		{
			setupmargins();
		}
		setupUI(findViewById(R.id.RelativeLayoutM));
		GradientDrawable gd = new GradientDrawable(
				GradientDrawable.Orientation.TOP_BOTTOM,
				new int[]{Color.parseColor("#355991"),Color.parseColor("#033076")});
		gd.setCornerRadius(0f);
		View title = getWindow().findViewById(android.R.id.title);
		View titleBar = (View) title.getParent();
		titleBar.setBackgroundDrawable(gd);
		titleBar.getLayoutParams().height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) (35*GlobalVarClass.getInstance().hsf), getResources().getDisplayMetrics());
		((TextView) title).setTextSize((float) (20*state.tsf));
		state.mandatorysave = true;
		//Add the Home Button
		ok = (FrameLayout) findViewById(R.id.memberok);
		//setupbackgrnd(ok);
		new SetBackGrnd(ok,true, true, 0.08f, 0.8f, Color.parseColor("#000000"),Color.parseColor("#313131"));
		ok.setOnClickListener(this);
		//Add the Member Add button
		memadd = (FrameLayout) findViewById(R.id.memaddbutton);
		//setupbackgrnd(memadd);
		new SetBackGrnd(memadd,false, true, 0.08f, 0.8f, Color.parseColor("#033076"),Color.parseColor("#355991"));
		memadd.setOnClickListener(this);
		rowItems4 = new ArrayList<RowItem4>();
		for (int i = 0; i < state.Names.length; i++) {
			RowItem4 item = new RowItem4(state.Names[i]);
			rowItems4.add(item);
		}
		adapter4 = new CustomListViewAdapter4(this, R.layout.smallname,
				rowItems4);
		memlist = (ListView) findViewById(R.id.namelist);
		memlist.setAdapter(adapter4);
		memlist.setItemsCanFocus(true);



	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v==memadd)
		{
			state.addmember();
			rowItems4 = new ArrayList<RowItem4>();
			for (int i = 0; i < state.Names.length; i++) {
				RowItem4 item = new RowItem4(state.Names[i]);
				rowItems4.add(item);
				adapter4 = new CustomListViewAdapter4(this, R.layout.smallname,
						rowItems4);
				memlist.setAdapter(adapter4);
				memlist.setItemsCanFocus(true);
				memlist.smoothScrollToPosition(adapter4.getCount());
			}
		}
		else if (v == ok) {
			canfinish = true;
			Intent intent = new Intent(getApplicationContext(),
					MainActivity.class);
			startActivity(intent);
		}
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		overridePendingTransition(adapter4.enteranim, adapter4.exitanim);
		super.onPause();
		if(canfinish)
		finish();
	}
	@Override
	public void onBackPressed() {
		//ok = (Button) findViewById(R.id.memberok);
		ok.performClick();
	}

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
		int[] views = {R.id.RelativeLayoutM};
		for(int i = 0;i<views.length;i++)
		{
			v = findViewById(views[i]);
			v.setPadding((int)(v.getPaddingLeft()*state.wsf), (int)(v.getPaddingTop()*state.hsf), (int)(v.getPaddingRight()*state.wsf), (int)(v.getPaddingBottom()*state.hsf));
		}
		//modifying the Text Sizes
		views = new int[]{R.id.memberscaletext1,R.id.memberscaletext2};
		for(int i = 0;i<views.length;i++)
		{
			v = findViewById(views[i]);
			((TextView) v).setTextSize(TypedValue.COMPLEX_UNIT_PX,(float) (((TextView) v).getTextSize()*(float)state.tsf));
		}
	}

}
