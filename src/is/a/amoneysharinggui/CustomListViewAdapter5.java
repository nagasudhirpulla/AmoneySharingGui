package is.a.amoneysharinggui;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomListViewAdapter5 extends ArrayAdapter<RowItem5>{

	Context context;
	GlobalVarClass state = GlobalVarClass.getInstance();
	List<RowItem5> objects;

	public CustomListViewAdapter5(Context context, int textViewResourceId,
			List<RowItem5> objects) {
		super(context, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.objects = objects;
	}

	private class ViewHolder {
		TextView txtTitle;
		TextView txtTitle1;
		TextView txtTitle2;

	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		RowItem5 rowItem = getItem(position);
		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.transactionelement, null);
			holder = new ViewHolder();
			holder.txtTitle = (TextView) convertView.findViewById(R.id.donor);
			holder.txtTitle1 = (TextView) convertView.findViewById(R.id.amount);
			holder.txtTitle2 = (TextView) convertView.findViewById(R.id.reciept);
			if(state.wsf != 1)
			{
				setupmargins(convertView);
			}
			convertView.setTag(holder);
		} else
			holder = (ViewHolder) convertView.getTag();

		holder.txtTitle.setText(state.Names[rowItem.getTitle()]);
		holder.txtTitle1.setText(rowItem.getTitle1());
		holder.txtTitle2.setText(state.Names[rowItem.getTitle2()]);		
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
			v.setPadding((int)(v.getPaddingLeft()*state.wsf), (int)(v.getPaddingTop()*state.hsf), (int)(v.getPaddingRight()*state.wsf), (int)(v.getPaddingBottom()*state.hsf));
		}
		//modifying the Text Sizes
		views = new int[]{R.id.amount,R.id.donor,R.id.reciept};
		for(int i = 0;i<views.length;i++)
		{
			v = convertView.findViewById(views[i]);
			((TextView) v).setTextSize(TypedValue.COMPLEX_UNIT_PX,(float) (((TextView) v).getTextSize()*(float)state.tsf));
		}
	}

}