package is.a.amoneysharinggui;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

public class MemberDeleteActivity extends Activity implements OnClickListener{

	TextView txt, des;
	GlobalVarClass state;
	FrameLayout del, cancel;
	boolean notinvolved = true;
	boolean canfinish = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.member_delete1);
		txt = (TextView)findViewById(R.id.deletetextView1);
		des = (TextView)findViewById(R.id.deldescrip);
		state = GlobalVarClass.getInstance();
		if(state.wsf != 1)
		{
			setupmargins();
		}
		//Title bar gradient
		GradientDrawable gd = new GradientDrawable(
				GradientDrawable.Orientation.TOP_BOTTOM,
				new int[]{Color.parseColor("#355991"), Color.parseColor("#033076")});
		gd.setCornerRadius(0f);
		View title = getWindow().findViewById(android.R.id.title);
		View titleBar = (View) title.getParent();
		titleBar.setBackgroundDrawable(gd);
		titleBar.getLayoutParams().height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) (35*GlobalVarClass.getInstance().hsf), getResources().getDisplayMetrics());
		((TextView) title).setTextSize((float) (20*state.tsf));

		for(int i = 0; i < state.totprod; i++)
		{
			if(state.conts[i][state.deletingposition] != 0 || state.weights[i][state.deletingposition] != 0)
			{
				notinvolved = false;
			}
		}
		del = (FrameLayout) findViewById(R.id.deletememberbutton);
		if(notinvolved)
		{
			((TextView)findViewById(R.id.prodcost)).setTextColor(Color.TRANSPARENT);
			((TextView)findViewById(R.id.textView2)).setTextColor(Color.TRANSPARENT);
			((TextView)findViewById(R.id.textView3)).setTextColor(Color.TRANSPARENT);
			((TextView)findViewById(R.id.textView4)).setTextColor(Color.TRANSPARENT);
			txt.setText(state.Names[state.deletingposition]+" ?");
			//Add the Delete Button
			//setupbackgrnd(del);
			new SetBackGrnd(del,false, true, 0.08f, 0.8f, Color.parseColor("#ff0000"),Color.parseColor("#FF3333"));
			del.setOnClickListener(this);
		}
		else
		{
			del.setBackgroundColor(Color.TRANSPARENT);
			del.removeAllViews();
			des.setTextSize((float) (20*state.wsf));
			txt.setTextSize((float) (20*state.wsf));
			des.setText(state.Names[state.deletingposition]+" has some active transactions");
			txt.setText("So, "+state.Names[state.deletingposition]+" can't be deleted");
			((TextView)findViewById(R.id.canceltook)).setText("OK");
		}
		//Add the Cancel button
		cancel = (FrameLayout) findViewById(R.id.canceldeletebutton);
		//setupbackgrnd(cancel);
		new SetBackGrnd(cancel,false, true, 0.08f, 0.8f, Color.parseColor("#033076"),Color.parseColor("#355991"));
		cancel.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(getApplicationContext(),
				MemberEditingActivity.class);
		if(v==cancel)
		{
			canfinish = true;
			startActivity(intent);
		}
		else if(v==del)
		{
			canfinish = true;
			state.dodeletemember();
			startActivity(intent);
		}
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if(canfinish)
		{
		overridePendingTransition(R.layout.fadeoutgo, R.layout.fadeingo);
		finish();
		}
	}
	@Override
	public void onBackPressed() {
		//cancel = (Button)findViewById(R.id.canceldeletebutton);
		cancel.performClick();
	}

	private void setupmargins()
	{	
		View v;
		//modifying the paddings
		int[] views = {R.id.RelativeLayout1};
		for(int i = 0;i<views.length;i++)
		{
			v = findViewById(views[i]);
			v.setPadding((int)(v.getPaddingLeft()*state.wsf), (int)(v.getPaddingTop()*state.wsf), (int)(v.getPaddingRight()*state.wsf), (int)(v.getPaddingBottom()*state.wsf));
		}
		//modifying the margins
		views = new int[]{R.id.buttonbar,R.id.prodcost};
		for(int i = 0;i<views.length;i++)
		{
			v = findViewById(views[i]);
			ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
			p.setMargins((int) (p.leftMargin*state.wsf), (int) (p.topMargin*state.wsf), (int) (p.rightMargin*state.wsf), (int) (p.bottomMargin*state.wsf));
			v.requestLayout();

		}
		//modifying the Text Sizes
		views = new int[]{R.id.deldescrip,R.id.deletetextView1,R.id.memdelscaletext1,R.id.canceltook,R.id.prodcost,R.id.textView2,R.id.textView3,R.id.textView4};
		for(int i = 0;i<views.length;i++)
		{
			v = findViewById(views[i]);
			((TextView) v).setTextSize(TypedValue.COMPLEX_UNIT_PX,(float) (((TextView) v).getTextSize()*(float)state.tsf));
		}
		//modify height and width
		views = new int[]{R.id.deletememberbutton,R.id.canceldeletebutton};
		for(int i = 0;i<views.length;i++)
		{
			v = findViewById(views[i]);
			v.getLayoutParams().height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) (v.getLayoutParams().height*GlobalVarClass.getInstance().wsf), getResources().getDisplayMetrics());
		}
	}

}
