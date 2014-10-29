package is.a.amoneysharinggui;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class CustomListViewAdapter4 extends ArrayAdapter<RowItem4> implements OnEditorActionListener, OnLongClickListener, OnFocusChangeListener, OnClickListener{

	Context context;
	GlobalVarClass state = GlobalVarClass.getInstance();
	List<RowItem4> objects;
	int enteranim,exitanim;

	public CustomListViewAdapter4(Context context, int textViewResourceId,
			List<RowItem4> objects) {
		super(context, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.objects = objects;
		this.enteranim = R.layout.lefttorightin;
		this.exitanim = R.layout.lefttorightout;
	}

	private class ViewHolder {
		EditText txtTitle;
		ImageButton crossbut;
		TextView serial;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		RowItem4 rowItem = getItem(position);

		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.smallname, null);
			if(state.wsf != 1)
			{
				setupmargins(convertView);
			}
			holder = new ViewHolder();
			holder.txtTitle = (EditText) convertView.findViewById(R.id.nameinlist);
			holder.crossbut = (ImageButton) convertView.findViewById(R.id.memberdeletebutton1);
			holder.serial = (TextView) convertView.findViewById(R.id.srl);
			convertView.setTag(holder);

		} else
			holder = (ViewHolder) convertView.getTag();

		holder.serial.setText(""+(position+1));
		holder.crossbut.setOnClickListener(this);
		holder.crossbut.setId(position);
		holder.txtTitle.setText(rowItem.getTitle());
		holder.txtTitle.setId(position);
		holder.txtTitle.setOnEditorActionListener(this);
		holder.txtTitle.setOnLongClickListener(this);
		holder.txtTitle.setOnFocusChangeListener(this);
		holder.txtTitle.addTextChangedListener(new TextWatcher()
		{

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

		});
		return convertView;
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		// TODO Auto-generated method stub
		if (actionId == EditorInfo.IME_ACTION_DONE || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
			InputMethodManager imm = (InputMethodManager) v.getContext()
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
			final int position = v.getId();
			final EditText Caption = (EditText) v;
			String str = Caption.getText().toString();
			//state = GlobalVarClass.getInstance();
			if (str.length() != 0) {
				objects.get(position).setTitle(str);
			} else {
				str = state.Names[position];
				objects.get(position).setTitle(str);
			}
			state.Names[position] = objects.get(position).getTitle();
			Caption.setText(state.Names[position]);
			Caption.setSelection(Caption.length());
		}
		return false;
	}

	@Override
	public boolean onLongClick(View v) {
		/*
		// TODO Auto-generated method stub
		Intent intent = new Intent(context.getApplicationContext(),
				MemberDeleteActivity.class);
		final int position = v.getId();
		state = GlobalVarClass.getInstance();
		state.deletingposition = position;
		context.startActivity(intent);
		 */
		return false;		
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		// TODO Auto-generated method stub
		final int position = v.getId();
		final EditText Caption = (EditText) v;
		String str = Caption.getText().toString();
		//state = GlobalVarClass.getInstance();
		if (str.length() != 0) {
			objects.get(position).setTitle(str);
			state.Names[position] = str;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(context.getApplicationContext(),
				MemberDeleteActivity.class);
		final int position = v.getId();
		//state = GlobalVarClass.getInstance();
		state.deletingposition = position;
		enteranim = R.layout.fadeout;
		exitanim = R.layout.fadein;
		context.startActivity(intent);
	}

	private void setupmargins(View convertView) {
		View v;
		//modifying the paddings
		int[] views = {R.id.RelativeLayout1,R.id.srl,R.id.nameinlist,R.id.memberdeletebutton1};
		for(int i = 0;i<views.length;i++)
		{
			v = convertView.findViewById(views[i]);
			v.setPadding((int)(v.getPaddingLeft()*state.wsf), (int)(v.getPaddingTop()*state.hsf), (int)(v.getPaddingRight()*state.wsf), (int)(v.getPaddingBottom()*state.hsf));
		}
		//modifying the Text Sizes
		views = new int[]{R.id.srl,R.id.nameinlist};
		for(int i = 0;i<views.length;i++)
		{
			v = convertView.findViewById(views[i]);
			((TextView) v).setTextSize(TypedValue.COMPLEX_UNIT_PX,(float) (((TextView) v).getTextSize()*(float)state.tsf));
		}
		//modifying the height of the view
		//
	}
}