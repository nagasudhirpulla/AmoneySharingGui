package is.a.amoneysharinggui;


import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class InstanceAdapter extends ArrayAdapter<Instnmeitem> implements OnClickListener{
	Context context;
	GlobalVarClass state;
	List<Instnmeitem> objects;
	DatabaseHandler db;
	boolean transition = true;
	public InstanceAdapter(Context context, int textViewResourceId,
			List<Instnmeitem> objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
		this.objects = objects;
		state = GlobalVarClass.getInstance();
		notifyDataSetChanged();
	}

	private class ViewHolder {
		TextView txtTitle;
		FrameLayout loadbut;
		FrameLayout renbut;
		ImageButton delbut;
		TextView fstdte;
		TextView lstdte;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		Instnmeitem rowItem = getItem(position);

		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.instcell1, parent, false);
			holder = new ViewHolder();
			holder.txtTitle = (TextView) convertView.findViewById(R.id.insttxtVw);
			holder.loadbut = (FrameLayout) convertView.findViewById(R.id.instldbtn);
			holder.renbut = (FrameLayout) convertView.findViewById(R.id.instrnmebtn);
			holder.delbut = (ImageButton) convertView.findViewById(R.id.instdltebtn);
			holder.fstdte = (TextView) convertView.findViewById(R.id.prodcost);
			holder.lstdte = (TextView) convertView.findViewById(R.id.textView2);
			//modify margins, text sizes and paddings
			if(state.wsf != 1)
			{
				setupmargins(convertView);
			}
			new SetBackGrnd(holder.loadbut,false, false, 0.1f, 0.1f, Color.parseColor("#631878"),Color.parseColor("#824693"));
			new SetBackGrnd(holder.renbut,false, false, 0.1f, 0.1f, Color.parseColor("#2450bb"),Color.parseColor("#5073C9"));
			new SetBackGrnd(holder.delbut,false, false, 0.1f, 0.1f, Color.parseColor("#ff0000"),Color.parseColor("#ff3131"));
			convertView.setTag(holder);
			convertView.getLayoutParams().height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) (90*GlobalVarClass.getInstance().hsf), context.getResources().getDisplayMetrics());
		} else
			holder = (ViewHolder) convertView.getTag();
		holder.renbut.setTag(position);
		holder.loadbut.setTag(position);
		holder.delbut.setTag(position);
		holder.txtTitle.setText(rowItem.getTitle());
		holder.fstdte.setText("From:"+rowItem.getfirst());
		holder.lstdte.setText("To:"+rowItem.getlast());
		holder.loadbut.setOnClickListener(this);
		holder.renbut.setOnClickListener(this);
		holder.loadbut.setOnClickListener(this);
		holder.delbut.setOnClickListener(this);

		//find the late and early dates and total value of all the products of this instance 


		return convertView;		
	}

	private void setupmargins(View convertView) {
		View v;
		//modifying the paddings
		int[] views = {R.id.LinearLayout1,R.id.scalelinear,R.id.instdltebtn};
		for(int i = 0;i<views.length;i++)
		{
			v = convertView.findViewById(views[i]);
			v.setPadding((int)(v.getPaddingLeft()*state.wsf), (int)(v.getPaddingTop()*state.hsf), (int)(v.getPaddingRight()*state.wsf), (int)(v.getPaddingBottom()*state.hsf));
		}
		//modifying the margins
		views = new int[]{R.id.instdltebtn};
		for(int i = 0;i<views.length;i++)
		{
			v = convertView.findViewById(views[i]);
			if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
				ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
				p.setMargins((int) (p.leftMargin*state.wsf), (int) (p.topMargin*state.hsf), (int) (p.rightMargin*state.wsf), (int) (p.bottomMargin*state.hsf));
				v.requestLayout();
			}
		}
		//modifying the Text Sizes
		views = new int[]{R.id.scaleText1,R.id.scaleText2,R.id.insttxtVw,R.id.textView3,R.id.prodcost,R.id.textView2};
		for(int i = 0;i<views.length;i++)
		{
			v = convertView.findViewById(views[i]);
			((TextView) v).setTextSize(TypedValue.COMPLEX_UNIT_PX,(float) (((TextView) v).getTextSize()*(float)state.tsf));
		}
		//modifying the height of the view
		//
	}

	@Override
	public void onClick(final View v) {
		// TODO Auto-generated method stub
		if(v.getId() == R.id.instrnmebtn)
		{
			//state = GlobalVarClass.getInstance();			
			final EditText input = new EditText(context);
			input.setText(objects.get((Integer) v.getTag()).getTitle());
			input.setSingleLine();
			new AlertDialog.Builder(context)
			.setTitle("Reaniming Instance...")
			.setMessage("Enter the new Instance Name")
			.setView(input)
			.setPositiveButton("RENAME", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					Editable value = input.getText();
					objects.get((Integer) v.getTag()).setTitle(value.toString());
					//state.Names[(Integer) v.getTag()] = value.toString();
					db = new DatabaseHandler(context.getApplicationContext());
					db.open();
					db.updateInstance(value.toString(), objects.get((Integer) v.getTag()).getId());
					db.close();
					notifyDataSetChanged();
				}
			}).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					// Do nothing.
				}
			}).show();
		}
		if(v.getId() == R.id.instldbtn)
		{
			//state = GlobalVarClass.getInstance();
			if(state.loaded)
			{
				if(state.mandatorysave)
				{
					new AlertDialog.Builder(context)
					.setTitle("Loading Instance...")
					.setMessage("Save the Changes to Currently loaded Instance?")
					.setNegativeButton("DONT SAVE", new  DialogInterface.OnClickListener() {
						public void onClick(DialogInterface arg0, int arg1) {
							//state = GlobalVarClass.getInstance();
							state.loaded = true;
							state.mandatorysave = false;
							state.curload = objects.get((Integer) v.getTag()).getId();
							db = new DatabaseHandler(context.getApplicationContext());
							db.open();
							db.loadThisInstance(state.curload);
							db.close();
							Intent intent = new Intent(context.getApplicationContext(),
									MainActivity.class);
							context.startActivity(intent);
							((Activity) context).finish();
						}
					})
					.setPositiveButton("SAVE & LOAD", new  DialogInterface.OnClickListener() {
						public void onClick(DialogInterface arg0, int arg1) {					
							//state = GlobalVarClass.getInstance();
							state.loaded = true;
							state.mandatorysave = false;
							db = new DatabaseHandler(context.getApplicationContext());
							db.open();
							db.saveThisInstance("",true);//true for rewriting
							//db.close();
							Toast.makeText(context, "Changes Saved", Toast.LENGTH_SHORT).show();
							state.curload = objects.get((Integer) v.getTag()).getId();
							//db.open();
							db.loadThisInstance(state.curload);
							db.close();
							Intent intent = new Intent(context.getApplicationContext(),
									MainActivity.class);
							context.startActivity(intent);
							((Activity) context).finish();
						}
					}).create().show();
				}
				else
				{
					state.loaded = true;
					//state.mandatorysave = false;
					state.curload = objects.get((Integer) v.getTag()).getId();
					db = new DatabaseHandler(context.getApplicationContext());
					db.open();
					db.loadThisInstance(state.curload);
					db.close();
					Intent intent = new Intent(context.getApplicationContext(),
							MainActivity.class);
					context.startActivity(intent);
					((Activity) context).finish();
				}
			}
			else
			{
				if(state.mandatorysave){
					new AlertDialog.Builder(context)
					.setTitle("Loading Instance...")
					.setMessage("Save the current work?")
					.setNegativeButton("DONT SAVE", new  DialogInterface.OnClickListener() {
						public void onClick(DialogInterface arg0, int arg1) {
							//state = GlobalVarClass.getInstance();
							state.loaded = true;
							state.mandatorysave = false;
							state.curload = objects.get((Integer) v.getTag()).getId();
							db = new DatabaseHandler(context.getApplicationContext());
							db.open();
							db.loadThisInstance(state.curload);
							db.close();
							Intent intent = new Intent(context.getApplicationContext(),
									MainActivity.class);
							context.startActivity(intent);
							((Activity) context).finish();
						}
					})
					.setPositiveButton("SAVE & LOAD", new  DialogInterface.OnClickListener() {
						public void onClick(DialogInterface arg0, int arg1) {					
							//state = GlobalVarClass.getInstance();
							savenew(objects.get((Integer) v.getTag()).getId());
						}
					}).create().show();
				}
				else
				{
					state.loaded = true;
					state.curload = objects.get((Integer) v.getTag()).getId();
					db = new DatabaseHandler(context.getApplicationContext());
					db.open();
					db.loadThisInstance(state.curload);
					db.close();
					Intent intent = new Intent(context.getApplicationContext(),
							MainActivity.class);
					context.startActivity(intent);
					((Activity) context).finish();
				}
			}
		}
		if(v.getId() == R.id.instdltebtn)
		{
			new AlertDialog.Builder(context)
			.setTitle("Delete Instance?")
			.setMessage("Are you sure you want to delete this Instance?")
			.setNegativeButton("CANCEL", null)
			.setPositiveButton("DELETE", new  DialogInterface.OnClickListener() {
				public void onClick(DialogInterface arg0, int arg1) {
					db = new DatabaseHandler(context.getApplicationContext());
					db.open();
					db.deleteInstance(objects.get((Integer) v.getTag()).getId());
					db.close();
					transition  = false;
					Intent intent = new Intent(context.getApplicationContext(),
							InstanceLoadActivity.class);
					context.startActivity(intent);
					((Activity) context).finish();
				}
			}).create().show();

		}
	}
	private void savenew(final int cur) {
		//state = GlobalVarClass.getInstance();		
		final DatabaseHandler db = new DatabaseHandler(context.getApplicationContext());
		final EditText input = new EditText(context);
		input.setSingleLine();
		new AlertDialog.Builder(context)
		.setTitle("Saving Instance...")
		.setMessage("Enter the New Instance Name")
		.setView(input)
		.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				Editable value = input.getText();
				db.open();
				db.saveThisInstance(value.toString(),false);
				//db.close();
				state.loaded = true;
				state.mandatorysave = false;
				state.curload = cur;
				Toast.makeText(context, "New Instance Created", Toast.LENGTH_SHORT).show();
				//db.open();
				db.loadThisInstance(state.curload);
				db.close();
				Intent intent = new Intent(context.getApplicationContext(),
						MainActivity.class);
				context.startActivity(intent);
				((Activity) context).finish();
			}
		})
		.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				//do nothing
			}
		})
		.show();
	}
}
