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

public class CustomProdListAdapter extends ArrayAdapter<ProductListItem> {

	Context context;
	ViewHolder holder;
	List<ProductListItem> objects;


	public CustomProdListAdapter(Context context, int textViewResourceId,
			List<ProductListItem> objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
		this.objects = objects;
	}

	private static class ViewHolder {
		TextView pname;
		TextView pdesc;	
		TextView pdate;
		TextView pval;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		holder = null;
		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.glasscell, null);
			holder = new ViewHolder();
			holder.pname = (TextView) convertView.findViewById(R.id.prodind);
			holder.pdesc = (TextView) convertView.findViewById(R.id.proddisc);
			holder.pdate = (TextView) convertView.findViewById(R.id.proddate);
			holder.pval = (TextView) convertView.findViewById(R.id.prodcost);
			holder.pname.setTextSize(TypedValue.COMPLEX_UNIT_DIP,(float) (10*GlobalVarClass.getInstance().tsf));
			holder.pdesc.setTextSize(TypedValue.COMPLEX_UNIT_DIP,(float) (15*GlobalVarClass.getInstance().tsf));
			holder.pdate.setTextSize(TypedValue.COMPLEX_UNIT_DIP,(float) (7*GlobalVarClass.getInstance().tsf));
			holder.pval.setTextSize(TypedValue.COMPLEX_UNIT_DIP,(float) (9*GlobalVarClass.getInstance().tsf));
			if(GlobalVarClass.getInstance().wsf != 1)
			{
				setupmargins(convertView);
			}
			convertView.setTag(holder);
		}
		else
			holder = (ViewHolder) convertView.getTag();

		holder.pname.setText(objects.get(position).getpname());
		holder.pdesc.setText(objects.get(position).getpdisc());
		holder.pdate.setText(objects.get(position).getpdate());
		if(objects.get(position).getpval() != "0")
			holder.pval.setText(objects.get(position).getpval());
		return convertView;
	}
	private void setupmargins(View convertView) {
		View v;
		//modifying the paddings
		int[] views = {R.id.prodind,R.id.proddate,R.id.proddisc,R.id.prodcost};
		for(int i = 0;i<views.length;i++)
		{
			v = convertView.findViewById(views[i]);
			v.setPadding((int)(v.getPaddingLeft()*GlobalVarClass.getInstance().wsf), (int)(v.getPaddingTop()*GlobalVarClass.getInstance().hsf), (int)(v.getPaddingRight()*GlobalVarClass.getInstance().wsf), (int)(v.getPaddingBottom()*GlobalVarClass.getInstance().hsf));
		}
	}

}
