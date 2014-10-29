package is.a.amoneysharinggui;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MemlistAdapter extends ArrayAdapter<String> {

	Context context;
	ViewHolder holder;
	List<String> objects;


	public MemlistAdapter(Context context, int textViewResourceId,
			List<String> objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
		this.objects = objects;
	}

	private static class ViewHolder {
		TextView memnme;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		holder = null;
		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.member_cell, null);
			holder = new ViewHolder();
			holder.memnme = (TextView) convertView.findViewById(R.id.memdi);
			holder.memnme.setTextSize(TypedValue.COMPLEX_UNIT_DIP,(float) (16*GlobalVarClass.getInstance().tsf));
			if(GlobalVarClass.getInstance().wsf != 1)
			{
				setupmargins(convertView);
			}
			convertView.setTag(holder);
		}
		else
			holder = (ViewHolder) convertView.getTag();

		holder.memnme.setText(objects.get(position));

		return convertView;
	}
	
	private void setupmargins(View convertView) {
		View v;
		//modifying the paddings
		int[] views = {R.id.memdi};
		for(int i = 0;i<views.length;i++)
		{
			v = convertView.findViewById(views[i]);
			v.setPadding((int)(v.getPaddingLeft()*GlobalVarClass.getInstance().wsf), (int)(v.getPaddingTop()*GlobalVarClass.getInstance().hsf), (int)(v.getPaddingRight()*GlobalVarClass.getInstance().wsf), (int)(v.getPaddingBottom()*GlobalVarClass.getInstance().hsf));
		}
	}

}
