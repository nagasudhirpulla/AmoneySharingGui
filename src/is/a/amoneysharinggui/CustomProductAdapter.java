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
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

public class CustomProductAdapter extends ArrayAdapter<RowItem2> implements OnClickListener, OnEditorActionListener {

	Context context;
	ViewHolder holder;
	alertViewHolder altholder;
	GlobalVarClass state = GlobalVarClass.getInstance();
	List<RowItem2> objects;
	int enteranim,exitanim;
	boolean canfinish = false;

	public CustomProductAdapter(Context context, int textViewResourceId,
			List<RowItem2> objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
		this.objects = objects;
		notifyDataSetChanged();
	}



	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		holder = null;
		altholder = null;
		state = GlobalVarClass.getInstance();
		//state.currentcontselected = position;
		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.cell1, null);
			//modify margins, text sizes and paddings
			if(state.wsf != 1)
			{
				setupmargins(convertView);
			}
			holder = new ViewHolder();
			holder.txtTitle = (EditText) convertView.findViewById(R.id.edittxtview);
			holder.img = (ImageView) convertView.findViewById(R.id.imgview);
			holder.nme = (TextView) convertView.findViewById(R.id.txtview);
			holder.alimg = (ImageView) convertView.findViewById(R.id.alrtView);
			////////////////
			altholder = new alertViewHolder();
			altholder.alimg = (ImageView) convertView.findViewById(R.id.alrtView);
			altholder.position = position;
			////////////////

			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
			altholder = (alertViewHolder) holder.txtTitle.getTag();
		}
		// Fill EditText with the value you have in data source
		holder.txtTitle.setText(objects.get(position).getAmount());
		holder.img.setImageResource(objects.get(position).getImageId());
		holder.nme.setText(objects.get(position).getName());
		holder.alimg.setImageResource(objects.get(position).getAlertid());
		////////////////
		altholder.alimg.setImageResource(objects.get(position).getAlertid());
		altholder.position = position;
		////////////////		
		holder.txtTitle.setTag(altholder);
		holder.img.setTag(position);
		holder.nme.setTag(position);
		holder.alimg.setTag(position);
		holder.img.setOnClickListener(this);
		//holder.nme.setOnClickListener(this);
		holder.alimg.setOnClickListener(this);
		//update the objects after the editing is done
		//holder.txtTitle.setOnFocusChangeListener(this);
		holder.txtTitle.setOnEditorActionListener(this);
		holder.txtTitle.addTextChangedListener(new MyTextWatcher(holder.txtTitle));
		return convertView;
	}



	private static class ViewHolder {
		EditText txtTitle;
		ImageView img;
		TextView nme;
		ImageView alimg;
	}

	private static class alertViewHolder{
		int position;
		ImageView alimg;
	}



	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		// TODO Auto-generated method stub
		if ((actionId == EditorInfo.IME_ACTION_DONE
				|| event.getKeyCode() == KeyEvent.KEYCODE_ENTER)&&v.getId()==R.id.edittxtview) {
			InputMethodManager imm = (InputMethodManager) v.getContext()
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
			final int position = ((alertViewHolder) v.getTag()).position;
			final EditText Caption = (EditText) v;
			String str = Caption.getText().toString();
			///////////not required
			((alertViewHolder) v.getTag()).alimg.setImageResource(R.drawable.indicator_input_error);//actually we should keep indicator input error
			objects.get(position).setAlertid(R.drawable.indicator_input_error);
			///////////
			if(checksemantics(str))
			{
				state = GlobalVarClass.getInstance();
				if(str.length()!=0)
				{
					if (str.endsWith("*") || str.endsWith("/")) {
						str = str.concat("1");
					} else if (str.endsWith("+") || str.endsWith("-")) {
						str = str.concat("0");
					}
					str = String.valueOf(new Infix()
					.infix(str));
					if(Double.parseDouble(str) == 0)
						str = "0";
					else if(str.endsWith(".0"))
					{
						str = str.substring(0, str.length()-2);
					}
					Caption.setText(str);
					Caption.setSelection(Caption.length());
					///////////
					((alertViewHolder) v.getTag()).alimg.setImageResource(R.drawable.no_alert_small);
					objects.get(position).setAlertid(R.drawable.no_alert_small);
					///////////
					addtoobjects(str,position);//update the objects
					state.conts[state.currentproduct - 1][position] = (float) new Infix()
					.infix(str);					
				}
				else
				{
					///////////
					((alertViewHolder) v.getTag()).alimg.setImageResource(R.drawable.no_alert_small);
					objects.get(position).setAlertid(R.drawable.no_alert_small);
					///////////
				}
			}
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.imgview)
		{
			state = GlobalVarClass.getInstance();

			if(objects.get((Integer) v.getTag()).getImageId() == R.drawable.cross)
			{
				objects.get((Integer) v.getTag()).setImageId(R.drawable.tick);
				state.numberofbenf[state.currentproduct - 1]++;
				state.weights[state.currentproduct - 1][(Integer) v.getTag()] = 1;
			}
			else
			{
				objects.get((Integer) v.getTag()).setImageId(R.drawable.cross);
				state.numberofbenf[state.currentproduct - 1]--;
				state.weights[state.currentproduct - 1][(Integer) v.getTag()] = 0;
			}
			notifyDataSetChanged();
		}
		else if(v.getTag() == "HOME")
		{
			state = GlobalVarClass.getInstance();
			boolean passed = true;
			String t = "";
			for (int position = 0; position < state.Names.length; position++) {
				String str = objects.get(position).getAmount();
				if (str.length() != 0) {
					if (str.endsWith("*") || str.endsWith("/")) {
						str = str.concat("1");
					} else if (str.endsWith("+") || str.endsWith("-")) {
						str = str.concat("0");
					}
				} else {
					str = "0";
				}
				if(checksemantics(str))
				{
					state.conts[state.currentproduct - 1][position] = (float) new Infix()
					.infix(str);
				}
				else
				{
					passed = false;
					if(t.length() == 0)
					{
						t = "Defective input at position "+ (position+1);
					}else
					{
						t = t + ", " + (position+1);
					}
					objects.get(position).setAlertid(R.drawable.indicator_input_error);

				}

			}
			if(passed)
			{
				canfinish = true;
				Intent intent = new Intent(context.getApplicationContext(),
						MainActivity.class);
				enteranim = R.layout.lefttorightin;
				exitanim = R.layout.lefttorightout;
				context.startActivity(intent);
			}
			else
			{
				Toast.makeText(context, t, Toast.LENGTH_SHORT).show();
				notifyDataSetChanged();
			}
		}
		else if(v.getTag() == "CALCULATE")
		{
			state = GlobalVarClass.getInstance();
			String t = "";
			boolean passed = true;
			for (int position = 0; position < state.Names.length; position++) {
				String str = objects.get(position).getAmount();
				if (str.length() != 0) {
					if (str.endsWith("*") || str.endsWith("/")) {
						str = str.concat("1");
					} else if (str.endsWith("+") || str.endsWith("-")) {
						str = str.concat("0");
					}
				} else {
					str = "0";
				}
				if(checksemantics(str))
				{
					state.conts[state.currentproduct - 1][position] = (float) new Infix()
					.infix(str);
				}
				else
				{
					passed = false;
					if(t.length() == 0)
					{
						t= "Defective input at position "+ (position+1);
					}else
					{
						t = t + ", " + (position+1);
					}
					objects.get(position).setAlertid(R.drawable.indicator_input_error);

				}

			}
			if(passed)
			{
				canfinish = true;
				Intent intent = new Intent(context.getApplicationContext(),
						TransactionActivity.class);
				enteranim = R.layout.righttoleftin;
				exitanim = R.layout.righttoleftout;
				context.startActivity(intent);
			}
			else
			{
				Toast.makeText(context, t, Toast.LENGTH_SHORT).show();
				notifyDataSetChanged();
			}
		}
	}

	boolean belongsto(String str, int pos)
	{
		if(pos>=0)//do if cursor is not at the starting
		{
			if ((str.charAt(pos) < '0' || str.charAt(pos) > '9')
					&& str.charAt(pos) != '+'
					&& str.charAt(pos) != '-'
					&& str.charAt(pos) != '*'
					&& str.charAt(pos) != '.'
					&& str.charAt(pos) != '/')
			{
				return false;
			}

			else
			{
				return true;
			}
		}
		else
			return true;
	}

	boolean belongstosigndot(String str, int pos)
	{
		if(pos>=0)//do if cursor is not at the starting
		{
			if ( str.charAt(pos) == '+'
					|| str.charAt(pos) == '-'
					|| str.charAt(pos) == '*'
					|| str.charAt(pos) == '.'
					|| str.charAt(pos) == '/')
			{
				return true;
			}

			else
			{
				return false;
			}
		}
		else
			return false;//default is false
	}

	boolean belongstosign(String str, int pos)
	{
		if(pos>=0)//do if cursor is not at the starting
		{
			if ( str.charAt(pos) == '+'
					|| str.charAt(pos) == '-'
					|| str.charAt(pos) == '*'
					|| str.charAt(pos) == '/')
			{
				return true;
			}

			else
			{
				return false;
			}
		}
		else
			return false;//default is false
	}

	void addtoobjects(String str, int position)
	{
		if (str.length() > 0) {
			objects.get(position).setAmount(str);
		} else {
			objects.get(position).setAmount("");
		}
	}

	String removecharat(String str, int pos)
	{
		if (pos == str.length()) {
			str = str.substring(0, pos - 1);
		} else {
			str = str.substring(0, pos - 1)
					+ str.substring(pos);
		}
		return str;
	}

	private String checkconsec(String str, int pos) {
		// TODO Auto-generated method stub
		if(pos==str.length())
		{
			if(pos==1)//we also check for * and / at the beginning with on character in the  field
			{
				switch(str.charAt(pos-1))
				{
				case '*':
				case '/':
					str = "";
					break;
				case '.':
					//str = "0.";
					break;
				}
			}
			else if(belongstosigndot(str,pos-1)&&belongstosigndot(str,pos-2))
			{
				str = str.substring(0,pos-1);
			}
		}
		return str;
	}

	boolean checksemantics(String str) {

		if (str.length() != 0) {
			//check for * or / at the beginning
			switch(str.charAt(0))
			{
			case '*':
			case '/':
				return false;
			}
			//check for the consecutive operators for strings with length>1
			if(str.length()>1)
			{
				int index = 0;
				index = findfirstop(str,1);
				while(index < str.length() - 1 && index != -1)
				{				
					if(belongstosign(str,index-1)||belongstosign(str,index+1))
						return false;
					index = findfirstop(str,index+1);
				}
			}
			//check for invalid decimal points
			int firstpos = 0, operpos = 0, secpos = 0;
			firstpos = str.indexOf('.', firstpos);
			while (firstpos < str.length() - 1 && firstpos != -1) {
				secpos = str.indexOf('.', firstpos + 1);
				if (secpos == -1) {
					firstpos = -1;
				} else {
					operpos = str.indexOf('+', firstpos);
					if (operpos == -1) {
						operpos = str.indexOf('-', firstpos);
					}
					if (operpos == -1) {
						operpos = str.indexOf('*', firstpos);
					}
					if (operpos == -1) {
						operpos = str.indexOf('/', firstpos);
					}
					if (operpos > secpos || operpos == -1) {
						return false;
					}
					firstpos = secpos + 1;
				}

			}
		}
		return true;
	}



	private int findfirstop(String str, int index1) {
		// TODO Auto-generated method stub
		int result = -1;

		int var = str.indexOf('*', index1);
		if(var>=index1)
			if(result ==-1 || var<result)
				result = var;

		var = str.indexOf('+', index1);
		if(var>=index1)
			if(result ==-1 || var<result)
				result = var;

		var = str.indexOf('-', index1);
		if(var>=index1)
			if(result ==-1 || var<result)
				result = var;

		var = str.indexOf('/', index1);
		if(var>=index1)
			if(result ==-1 || var<result)
				result = var;

		return result;
	}

	private class MyTextWatcher implements TextWatcher{

		private EditText view;
		private MyTextWatcher(EditText view) {
			this.view = view;

		}

		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			//do nothing

		}
		public void onTextChanged(CharSequence s, int start, int before, int count) {



		}
		public void afterTextChanged(Editable s) {
			if(view.hasFocus()){
				((alertViewHolder) view.getTag()).alimg.setImageResource(R.drawable.alert_small);
				objects.get(((alertViewHolder) view.getTag()).position).setAlertid(R.drawable.alert_small);
				dosomething(view);
				return;
			}
		}

	}

	boolean isgood()
	{
		state = GlobalVarClass.getInstance();
		String t = "";
		boolean passed = true;
		for (int position = 0; position < state.Names.length; position++) {
			String str = objects.get(position).getAmount();
			if (str.length() != 0) {
				if (str.endsWith("*") || str.endsWith("/")) {
					str = str.concat("1");
				} else if (str.endsWith("+") || str.endsWith("-")) {
					str = str.concat("0");
				}
			} else {
				str = "0";
			}
			if(checksemantics(str))
			{
				state.conts[state.currentproduct - 1][position] = (float) new Infix()
				.infix(str);
			}
			else
			{
				passed = false;
				if(t.length() == 0)
				{
					t = "Defective input at position "+ (position+1);
				}else
				{
					t = t + ", " + (position+1);
				}
				objects.get(position).setAlertid(R.drawable.indicator_input_error);
			}
		}
		if(!passed)
		{
			Toast.makeText(context, t, Toast.LENGTH_SHORT).show();
		}
		return passed;
	}

	public void dosomething(EditText view) {
		// TODO Auto-generated method stub
		int position = ((alertViewHolder) view.getTag()).position;
		EditText Caption = view;
		String str = Caption.getText().toString();//getting the string from the edittext
		int pos = Caption.getSelectionStart();   //cursor position
		//objects.get(position).setAmount(str);    //update the objects
		//First Manipulate the edittext view
		if(str.length()!=0)
		{
			if(belongsto(str, pos-1) == false) //here we give index starts from 0;belongsto and affiliated functions use index only 
			{
				str = removecharat(str,pos);  //here we give position starts from 1;					
				Caption.setText(str);
				Caption.setSelection(pos - 1);
			}
			else
			{
				if(checkconsec(str,pos) != str)//check if str changed and returned
				{
					str = checkconsec(str,pos);
					Caption.setText(str);
					Caption.setSelection(pos - 1);
				}					
			}
		}
		else
		{
			str = "";				
		}
		addtoobjects(str,position);//update the objects
	}



	public boolean dopassanalysis()
	{
		boolean passed = true;
		String t = "";
		for (int position = 0; position < state.Names.length; position++) {
			String str = objects.get(position).getAmount();
			if (str.length() != 0) {
				if (str.endsWith("*") || str.endsWith("/")) {
					str = str.concat("1");
				} else if (str.endsWith("+") || str.endsWith("-")) {
					str = str.concat("0");
				}
			} else {
				str = "0";
			}
			if(checksemantics(str))
			{
				state.conts[state.currentproduct - 1][position] = (float) new Infix()
				.infix(str);
			}
			else
			{				
				passed = false;
				if(t.length() == 0)
				{
					t = "Defective input at position "+ (position+1);
				}else
				{
					t = t + ", " + (position+1);
				}
				objects.get(position).setAlertid(R.drawable.indicator_input_error);
			}
		}
		if(!passed)
		{
			Toast.makeText(context, t, Toast.LENGTH_SHORT).show();
			notifyDataSetChanged();
		}
		return passed;
	}

	private void setupmargins(View convertView) {
		View v;
		//modifying the paddings
		int[] views = {R.id.RelativeLayout1,R.id.txtview,R.id.prodadapscalelay1};
		for(int i = 0;i<views.length;i++)
		{
			v = convertView.findViewById(views[i]);
			v.setPadding((int)(v.getPaddingLeft()*state.wsf), (int)(v.getPaddingTop()*state.hsf), (int)(v.getPaddingRight()*state.wsf), (int)(v.getPaddingBottom()*state.hsf));
		}
		//modifying the Text Sizes
		views = new int[]{R.id.txtview,R.id.edittxtview};
		for(int i = 0;i<views.length;i++)
		{
			v = convertView.findViewById(views[i]);
			((TextView) v).setTextSize(TypedValue.COMPLEX_UNIT_PX,(float) (((TextView) v).getTextSize()*(float)state.tsf));
		}
		//modifying the height of the view
	}
}