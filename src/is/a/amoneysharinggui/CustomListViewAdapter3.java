package is.a.amoneysharinggui;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomListViewAdapter3 extends ArrayAdapter<RowItem3> {

	Context context;

	public CustomListViewAdapter3(Context context, int textViewResourceId,
			List<RowItem3> objects) {
		super(context, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	private class ViewHolder {
		TextView txtamount;
		TextView txtTitle;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		RowItem3 rowItem = getItem(position);

		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.resultelement, null);
			holder = new ViewHolder();
			holder.txtTitle = (TextView) convertView.findViewById(R.id.donor);
			holder.txtamount = (TextView) convertView.findViewById(R.id.reciept);
			if(GlobalVarClass.getInstance().wsf != 1)
			{
				setupmargins(convertView);
			}
			convertView.setTag(holder);
		} else
			holder = (ViewHolder) convertView.getTag();

		holder.txtTitle.setText(rowItem.getTitle());
		holder.txtamount.setText(String.format("%.2f", rowItem.getamount()));
		if(Float.parseFloat(holder.txtamount.getText().toString())>0)
		{
			holder.txtamount.setText("+"+holder.txtamount.getText());
			holder.txtamount.setTextColor(Color.parseColor("#007A00"));
		}
		if(position%2==0)
		{
			convertView.setBackgroundColor(Color.parseColor("#33000000"));
		}
		return convertView;
	}

	private void setupmargins(View convertView) {
		View v;
		//modifying the paddings
		int[] views = {R.id.donor,R.id.reciept};
		for(int i = 0;i<views.length;i++)
		{
			v = convertView.findViewById(views[i]);
			v.setPadding((int)(v.getPaddingLeft()*GlobalVarClass.getInstance().wsf), (int)(v.getPaddingTop()*GlobalVarClass.getInstance().hsf), (int)(v.getPaddingRight()*GlobalVarClass.getInstance().wsf), (int)(v.getPaddingBottom()*GlobalVarClass.getInstance().hsf));
		}
		//modifying the Text Sizes
		views = new int[]{R.id.donor,R.id.reciept};
		for(int i = 0;i<views.length;i++)
		{
			v = convertView.findViewById(views[i]);
			((TextView) v).setTextSize(TypedValue.COMPLEX_UNIT_PX,(float) (((TextView) v).getTextSize()*(float)GlobalVarClass.getInstance().tsf));
		}
	}

}