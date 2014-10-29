package is.a.amoneysharinggui;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;

public class InfoActivity extends Activity implements OnClickListener{

	GlobalVarClass state;
	boolean canfinish = false;

	@SuppressLint("DefaultLocale")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		state = ((GlobalVarClass) getApplicationContext());
		//overridePendingTransition(R.layout.fadeout, R.layout.fadein);
		setContentView(R.layout.activity_info);
		if(state.wsf != 1)
		{
			setupmargins();
		}
		TextView valueTV;
		TextView valueTV2;
		setTitle(state.Names[state.currentmemedit]+"'s Analysis");
		//Title and heading bars gradient
		GradientDrawable gd = new GradientDrawable(
				GradientDrawable.Orientation.TOP_BOTTOM,
				new int[]{Color.parseColor("#444444"), Color.parseColor("#111111"),Color.BLACK});
		gd.setCornerRadius(0f);
		View title = getWindow().findViewById(android.R.id.title);
		View titleBar = (View) title.getParent();
		titleBar.setBackgroundDrawable(gd);
		titleBar.getLayoutParams().height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) (35*GlobalVarClass.getInstance().hsf), getResources().getDisplayMetrics());
		((TextView) title).setTextSize((float) (20*state.tsf));


		//Add the MemberEdit Button
		final FrameLayout add_mem = (FrameLayout) findViewById(R.id.editmemana);
		//setupbackgrnd(add_mem);
		new SetBackGrnd(add_mem,false, true, 0f, 0.8f, Color.parseColor("#033076"),Color.parseColor("#355991"));
		add_mem.setOnClickListener(this);

		findViewById(R.id.owestats).setBackgroundDrawable(new GradientDrawable(
				GradientDrawable.Orientation.TOP_BOTTOM,
				new int[]{Color.parseColor("#444444"), Color.parseColor("#111111"),Color.BLACK}));
		findViewById(R.id.conschrt).setBackgroundDrawable(new GradientDrawable(
				GradientDrawable.Orientation.TOP_BOTTOM,
				new int[]{Color.parseColor("#444444"), Color.parseColor("#111111"),Color.BLACK}));
		findViewById(R.id.consdta).setBackgroundDrawable(new GradientDrawable(
				GradientDrawable.Orientation.TOP_BOTTOM,
				new int[]{Color.parseColor("#444444"), Color.parseColor("#111111"),Color.BLACK}));
		TextView richTextView = (TextView)findViewById(R.id.conttext);
		//TextView owestats = (TextView)findViewById(R.id.owestats);
		state.calculatetotalconts();
		LinearLayout ll = (LinearLayout) findViewById(R.id.conslist);

		LinearLayout ll2;
		LinearLayout.LayoutParams layoutParams;
		ImageView img;

		float val = Math.abs(state.totalconts[state.currentmemedit]);		

		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setAlpha(100);
		paint.setStrokeWidth((float) (2*state.tsf));
		paint.setTextSize((float) (25*state.tsf));
		paint.setShadowLayer(0, 0, 0, Color.TRANSPARENT);
		paint.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));

		//Bitmap variables
		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.rect);
		List<PieItem> list = new ArrayList<PieItem>();

		//getting the consumption data
		float weightsum;
		float consumed = 0;
		float prodval;
		int n;
		for(int i = 0;i<state.totprod;i++)
		{
			if(state.weights[i][state.currentmemedit] == 1)
			{
				//Log.d("Consumer in:", "Product "+(i+1));
				weightsum = 0;
				prodval = 0;				
				//Find the cost of the product i
				for (int p = 0; p < state.Names.length;p++)
				{
					prodval = prodval + state.conts[i][p];
				}
				if(prodval!=0)
				{
					//Find the number of people using this product
					for (int p = 0; p < state.Names.length;p++)
					{
						weightsum = weightsum + state.weights[i][p];
					}
					consumed = consumed + prodval/weightsum;
					list.add(new PieItem(i,prodval/weightsum));
					//Log.d("Product added to list:", "Product "+(i+1));
				}
			}
		}

		float bgxp = linearLayout.getLayoutParams().width*2;
		float bgyp = linearLayout.getLayoutParams().height*2;
		float bmpts = 7;//Number of bitmap parts
		Bitmap bg = Bitmap.createBitmap((int)bgxp, (int)bgyp, Bitmap.Config.ARGB_8888);
		//Initializing the canvas with the above bitmap
		Canvas canvas = new Canvas(bg);

		//Draw a frame around the graph
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(Color.parseColor("#333333"));
		RectF rectF = new RectF(0, 0, bgxp, bgyp);
		//canvas.drawRect(rectF, paint);

		if(consumed == 0)
		{
			valueTV = new TextView(this);
			valueTV.setText("No Consumption Done");
			valueTV.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
			valueTV.setGravity(Gravity.CENTER);
			valueTV.setTextSize((float) (10*state.tsf));
			linearLayout.addView(valueTV);
			valueTV2 = new TextView(this);
			valueTV2.setText("No Consumption Done");
			valueTV2.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
			valueTV2.setGravity(Gravity.CENTER);
			valueTV2.setTextSize((float) (10*state.tsf));
			ll.addView(valueTV2);
		}
		else
		{
			//Defining the Pie Chart Circle boundaries
			rectF = new RectF((bgxp/2)-bgyp*((bmpts-2)/(bmpts*2)), bgyp/bmpts, (bgxp/2)+bgyp*((bmpts-2)/(bmpts*2)), (bgyp*(bmpts-1))/bmpts);

			//Drawing the Pie chart
			n = list.size();
			float sweeped = 0;
			float startX,startY,stopX,stopY;
			float rad = bgyp*((bmpts-2)/(2*bmpts));
			double lineangle;
			String disc;
			for(int i = 0; i<n ; i++)
			{
				//Draw solid sector
				paint.setStyle(Paint.Style.FILL);
				paint.setColor(Color.HSVToColor(new float[]{(360*i)/n, 1f, 0.96f}));			
				canvas.drawArc(rectF, sweeped, (list.get(i).amount*360)/consumed, true, paint);
				//Draw the sector outline
				paint.setStyle(Paint.Style.STROKE);
				paint.setColor(Color.parseColor("#FFFFFF"));
				canvas.drawArc(rectF, sweeped, (list.get(i).amount*360)/consumed, true, paint);

				////Draw the line in the middle of the sectors
				paint.setColor(Color.parseColor("#555555"));
				lineangle = (sweeped*Math.PI)/180 + (list.get(i).amount*Math.PI)/consumed; 
				startX = bgxp/2 + rad*0.9f*(float)Math.cos(lineangle);
				startY = bgyp/2 + rad*0.9f*(float)Math.sin(lineangle);
				stopX = bgxp/2 + rad*1.1f*(float)Math.cos(lineangle);
				stopY = bgyp/2 + rad*1.1f*(float)Math.sin(lineangle);             
				canvas.drawLine(startX, startY, stopX, stopY, paint);
				//Write the Product Description at the end of the line
				disc = state.discs[list.get(i).prindex];
				if(disc.replace(" ", "") == "" || disc.length()==0)
				{
					disc = "Product"+(list.get(i).prindex+1);
				}
				//Log.d("Description is:", disc);
				paint.setTextSize((float) (36*state.tsf));
				paint.setStyle(Paint.Style.FILL);
				if(lineangle>Math.PI/2&&lineangle<Math.PI*(1.5))
				{            	
					canvas.drawLine(stopX, stopY, (float) (stopX-rad*0.2), stopY, paint);
					paint.setColor(Color.parseColor("#000000"));
					canvas.drawText(disc, (float) (stopX-rad*0.22-paint.measureText(disc)), stopY, paint);
				}
				else
				{
					canvas.drawLine(stopX, stopY, (float) (stopX+rad*0.2), stopY, paint);
					paint.setColor(Color.parseColor("#000000"));
					canvas.drawText(disc, (float) (stopX+rad*0.22), stopY, paint);
				}

				//Draw the amount in the middle of the sector
				/*
				paint.setColor(Color.parseColor("#000000"));
				paint.setTextSize(20);
				canvas.save();
				canvas.rotate((float)((lineangle*180)/Math.PI),bgxp/2,bgyp/2);
				startX = bgxp/2 + rad*0.4f*(float)Math.cos(lineangle);
				startY = bgyp/2 + rad*0.4f*(float)Math.sin(lineangle);
				canvas.drawText(String.format("%.2f", list.get(i).amount), bgxp/2 + rad*0.4f, bgyp/2, paint);
				canvas.restore();
				 */
				sweeped = sweeped + (list.get(i).amount*360)/consumed;

				//Populating the consumption list
				//Adding a Linear Layout
				ll2 = new LinearLayout(this);
				if(i%2==0)
					ll2.setBackgroundColor(Color.parseColor("#DDDDDD"));
				else
					ll2.setBackgroundColor(Color.TRANSPARENT);
				ll2.setOrientation(LinearLayout.HORIZONTAL);
				layoutParams = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
				ll2.setWeightSum(1.0f);
				ll2.setLayoutParams(layoutParams);				
				//creating the text view
				layoutParams = new LinearLayout.LayoutParams(
						0, LinearLayout.LayoutParams.WRAP_CONTENT);
				layoutParams.weight = 0.4f;
				valueTV = new TextView(this);
				valueTV.setText(disc+" ("+String.format("%.2f", (list.get(i).amount*100)/consumed)+"%)");
				valueTV.setTextColor(Color.BLACK);
				valueTV.setTextSize((float) (12*state.tsf));
				valueTV.setGravity(Gravity.LEFT);
				valueTV.setLayoutParams(layoutParams);				
				//adding the image
				paint.setTextSize((float) (12*state.tsf));
				layoutParams = new LinearLayout.LayoutParams(
						0, LinearLayout.LayoutParams.WRAP_CONTENT);
				layoutParams.weight = 0.05f;
				Rect bounds =  new Rect();
				paint.getTextBounds("as", 0, 2, bounds);
				layoutParams.height = (int) (20*state.wsf);
				layoutParams.setMargins(0, 0, (int) (5*state.wsf), 0);
				img = new ImageView(this);
				img.setVisibility(View.VISIBLE);
				img.setBackgroundColor(Color.HSVToColor(new float[]{(360*i)/n, 1f, 0.96f}));
				img.setLayoutParams(layoutParams);
				ll2.addView(img);
				//adding the text view
				ll2.addView(valueTV);
				//adding the text view
				layoutParams = new LinearLayout.LayoutParams(
						0, LinearLayout.LayoutParams.FILL_PARENT);
				layoutParams.weight = 0.25f;
				valueTV = new TextView(this);
				valueTV.setText(state.dates[list.get(i).prindex]);
				valueTV.setTextColor(Color.BLACK);
				valueTV.setTextSize((float) (10*state.tsf));
				valueTV.setGravity(Gravity.LEFT|Gravity.CENTER);
				valueTV.setLayoutParams(layoutParams);				
				ll2.addView(valueTV);				
				//adding the text view
				layoutParams = new LinearLayout.LayoutParams(
						0, LinearLayout.LayoutParams.WRAP_CONTENT);
				layoutParams.weight = 0.3f;
				valueTV = new TextView(this);
				valueTV.setText(String.format("%.2f", list.get(i).amount));
				valueTV.setTextColor(Color.BLACK);
				valueTV.setTextSize((float) (12*state.tsf));
				valueTV.setGravity(Gravity.LEFT);
				valueTV.setLayoutParams(layoutParams);				
				ll2.addView(valueTV);
				ll.addView(ll2);
			}
			//Total Consumption done			
			valueTV = new TextView(this);
			valueTV.setTypeface(null, Typeface.BOLD);
			valueTV.setText("Total Consumption - "+String.format("%.2f", consumed));
			valueTV.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
			valueTV.setGravity(Gravity.LEFT);
			valueTV.setTextSize((float) (12*state.tsf));
			ll.addView(valueTV);			
		}
		//Write canvas code before this
		linearLayout.setBackgroundDrawable(new BitmapDrawable(bg));


		float[] colorv = new float[]{24f,1f,0.90f};
		String getorlose = "owes";

		if(state.totalconts[state.currentmemedit]<0)
		{
			//owestats.setText("People who owe to this person");
			colorv[0] = 120f;
			colorv[1] = 1f;
			colorv[2] = 0.48f;
			getorlose = "gets";
		}
		SpannableString text = new SpannableString(state.Names[state.currentmemedit]+" "+getorlose+" a total amount of "+String.format("%.2f", val));
		//text.setSpan(new ForegroundColorSpan(Color.HSVToColor(new float[]{240f,0.9f,1f})), 0, state.Names[state.currentmemedit].length(), 0);
		text.setSpan(new ForegroundColorSpan(Color.HSVToColor(colorv)), text.toString().lastIndexOf(String.format("%.2f", val), text.toString().length()-1), text.toString().length(), 0);
		richTextView.setText(text, BufferType.SPANNABLE);

		//Classes required are Quicksort.java, Individual.java, Transaction.java
		//Create a,pos,neg tr, npos1,nneg1,numtr1 arrays
		n = state.Names.length;		
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

		//Find the number of transactions in which this member is present
		int numtr = 0 ;
		int[] indarr = new int[numtr1[0]];
		for(int i = 0 ; i<numtr1[0] ; i++)
		{
			if(tr[i].pgiv == state.currentmemedit||tr[i].ptke == state.currentmemedit)
			{
				indarr[numtr++] = i;				
			}
		}
		linearLayout = (LinearLayout) findViewById(R.id.scrvwlnlyt);
		if(numtr == 0)
		{
			valueTV = new TextView(this);
			valueTV.setText("No Transactions Required for this person");
			valueTV.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
			valueTV.setGravity(Gravity.CENTER);
			valueTV.setTextSize((float) (10*state.tsf));
			((LinearLayout) linearLayout).addView(valueTV);
		}
		int ind = 0;
		//Find the biggest transaction amount
		for(int i = 0; i<numtr; i++)
		{
			if(ind < tr[indarr[i]].amt)
				ind = (int) tr[indarr[i]].amt;
		}
		String str = String.format("%.2f", (float)ind);
		int length = str.length();
		int diff;
		//Log.d("Max length:", ""+length);
		for(int i = 0; i<numtr; i++)
		{
			//Linear Layout for rows
			ll2 = new LinearLayout(this);
			if(i%2==0)
				ll2.setBackgroundColor(Color.parseColor("#DDDDDD"));
			else
				ll2.setBackgroundColor(Color.TRANSPARENT);
			ll2.setOrientation(LinearLayout.HORIZONTAL);
			layoutParams = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			ll2.setWeightSum(1.0f);
			ll2.setLayoutParams(layoutParams);
			ind = indarr[i];			
			if(tr[ind].pgiv == state.currentmemedit)
			{
				layoutParams = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
				valueTV = new TextView(this);
				valueTV.setLayoutParams(layoutParams);
				valueTV.setPadding((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) (5*GlobalVarClass.getInstance().wsf), this.getResources().getDisplayMetrics()), 0, 0, 0);
				valueTV.setText(" Give ");
				valueTV.setTextColor(Color.BLACK);
				valueTV.setTextSize((float) (15*state.tsf));
				valueTV.setGravity(Gravity.CENTER_HORIZONTAL);
				ll2.addView(valueTV);

				layoutParams = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
				valueTV = new TextView(this);
				valueTV.setLayoutParams(layoutParams);
				valueTV.setTextSize((float) (15*state.tsf));
				str = String.format("%.2f", tr[indarr[i]].amt);
				diff = length-str.length();
				for(int k=0;k<diff;k++)
					str = "0"+str;
				text = new SpannableString(" "+str+" ");				
				text.setSpan(new ForegroundColorSpan(Color.TRANSPARENT), 1, 2+diff , 0);				
				text.setSpan(new ForegroundColorSpan(Color.HSVToColor(colorv)), 1+diff ,text.length()-1, 0);				
				valueTV.setText(text, BufferType.SPANNABLE);
				valueTV.setGravity(Gravity.CENTER_HORIZONTAL);
				ll2.addView(valueTV);

				layoutParams = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
				valueTV = new TextView(this);
				valueTV.setLayoutParams(layoutParams);
				valueTV.setText(" to " + state.Names[tr[ind].ptke]);
				valueTV.setTextColor(Color.BLACK);
				valueTV.setTextSize((float) (15*state.tsf));
				ll2.addView(valueTV);

			}
			else
			{
				layoutParams = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
				valueTV = new TextView(this);
				valueTV.setLayoutParams(layoutParams);
				valueTV.setPadding((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) (5*GlobalVarClass.getInstance().wsf), this.getResources().getDisplayMetrics()), 0, 0, 0);
				valueTV.setText(" Get ");
				valueTV.setTextColor(Color.BLACK);
				valueTV.setTextSize((float) (15*state.tsf));
				valueTV.setGravity(Gravity.CENTER_HORIZONTAL);
				ll2.addView(valueTV);

				layoutParams = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
				valueTV = new TextView(this);
				valueTV.setLayoutParams(layoutParams);
				valueTV.setTextSize((float) (15*state.tsf));
				str = String.format("%.2f", tr[indarr[i]].amt);
				diff = length-str.length();
				for(int k=0;k<diff;k++)
					str = "0"+str;
				text = new SpannableString(" "+str+" ");				
				text.setSpan(new ForegroundColorSpan(Color.TRANSPARENT), 1, 2+diff , 0);				
				text.setSpan(new ForegroundColorSpan(Color.HSVToColor(colorv)), 1+diff ,text.length()-1, 0);				
				valueTV.setText(text, BufferType.SPANNABLE);
				valueTV.setGravity(Gravity.CENTER_HORIZONTAL);
				ll2.addView(valueTV);

				layoutParams = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
				valueTV = new TextView(this);
				valueTV.setLayoutParams(layoutParams);
				valueTV.setText(" from " + state.Names[tr[ind].pgiv]);
				valueTV.setTextColor(Color.BLACK);
				valueTV.setTextSize((float) (15*state.tsf));
				ll2.addView(valueTV);
			}
			((LinearLayout) linearLayout).addView(ll2);
		}
	}

	private void setupmargins() {
		View v;
		//modifying the paddings
		int[] views = {R.id.consdta,R.id.conslist,R.id.conttext,R.id.owestats,R.id.scrvwlnlyt,R.id.conschrt,R.id.consdta,R.id.conslist};
		for(int i = 0;i<views.length;i++)
		{
			v = findViewById(views[i]);
			v.setPadding((int)(v.getPaddingLeft()*state.wsf), (int)(v.getPaddingTop()*state.hsf), (int)(v.getPaddingRight()*state.wsf), (int)(v.getPaddingBottom()*state.hsf));
		}
		//modifying the Text Sizes
		views = new int[]{R.id.conttext,R.id.owestats,R.id.conschrt,R.id.consdta,R.id.infoscale4};
		for(int i = 0;i<views.length;i++)
		{
			v = findViewById(views[i]);
			((TextView) v).setTextSize(TypedValue.COMPLEX_UNIT_PX,(float) (((TextView) v).getTextSize()*(float)state.tsf));
		}
		//modifying the margins
		views = new int[]{R.id.consdta,R.id.rect};
		for(int i = 0;i<views.length;i++)
		{
			v = findViewById(views[i]);
			ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
			p.setMargins((int) (p.leftMargin*state.wsf), (int) (p.topMargin*state.hsf), (int) (p.rightMargin*state.wsf), (int) (p.bottomMargin*state.hsf));
			v.requestLayout();
		}
		//modify the heights of the views
		v = findViewById(R.id.rect);
		v.getLayoutParams().height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) (200*GlobalVarClass.getInstance().hsf), getResources().getDisplayMetrics());
		v.getLayoutParams().width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) (320*GlobalVarClass.getInstance().wsf), getResources().getDisplayMetrics());

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
		// TODO Auto-generated method stub
		canfinish = true;
		Intent intent = new Intent(getApplicationContext(),
				MainActivity.class);
		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		canfinish = true;
		Intent intent = new Intent(getApplicationContext(),
				MemberEditingActivity.class);
		startActivity(intent);
	}

}
