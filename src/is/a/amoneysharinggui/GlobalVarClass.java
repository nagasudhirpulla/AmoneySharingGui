package is.a.amoneysharinggui;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import android.app.Application;

public class GlobalVarClass extends Application {
	int totprod = 1;
	String[] Names = { "Joe", "Ben", "Tim", "Bob", "Ian" };
	String[] tempNames = new String[50];
	String[] Products = {"1"};
	String[] discs = {""};
	String[] dates = {(new SimpleDateFormat("dd/MMM/yyyy")).format(new Date(System.currentTimeMillis()))};	
	String firstdate = dates[0];
	String lastdate = dates[0];
	float[][] weights = new float[100][100];//bank
	float[][] conts = new float[100][100];//bank
	float[][] trantable = new float[100][100];
	float[] totalconts = new float[100];
	float[][] newwgts = new float[100][100];
	float[][] newconts = new float[100][100];
	int[] numberofbenf = new int[100];
	int currentproduct = 1;
	int prevclicked = 0;
	int currentcontselected = 1;
	int currentmemedit = 0;
	int deletingposition = 0;

	double wsf,hsf,tsf;

	boolean loaded = false;
	int curload;
	boolean mandatorysave = false;

	private static GlobalVarClass singleton;

	public static GlobalVarClass getInstance() {
		return singleton;
	}

	public void inc_totprod() {
		++totprod;
		String[] prevdiscs = discs;
		discs = new String[totprod];
		Products = new String[totprod];
		for (int i = 0; i<totprod-1; i++)
		{
			discs[i] = prevdiscs[i];		
		}
		discs[totprod - 1] = "";
		//update dates
		prevdiscs = dates;
		dates = new String[totprod];
		for (int i = 0; i<totprod-1; i++)
		{
			dates[i] = prevdiscs[i];		
		}
		dates[totprod - 1] = (new SimpleDateFormat("dd/MMM/yyyy")).format(new Date(System.currentTimeMillis()));
		//check for date updating
		checkdate(dates[totprod - 1]);
		for (int i = 0; i<totprod; i++)
		{
			Products[i] = ""+(i+1);		
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();
		singleton = this;
		// for total contributions matrix
		numberofbenf[0] = Names.length;
		for (int i = 0; i < Names.length; i++) {
			for (int j = 0; j < Names.length; j++) {
				trantable[i][j] = 0;
			}
		}
		for (int i = 0; i < Names.length; i++) {
			conts[0][i] = 0;
			weights[0][i] = 1;
			totalconts[i] = 0;
		}

	}

	void refresh() {
		// TODO Auto-generated method stub
		for (int i = 0; i < Names.length; i++) {
			totalconts[i] = 0;
			for (int j = 0; j < Names.length; j++) {
				trantable[i][j] = 0;
			}
		}
	}

	void deleteproductfun() {
		// TODO Auto-generated method stub
		if(totprod>1)
		{
			boolean needed = false;
			if(dates[totprod - 1] == lastdate || dates[totprod - 1] == firstdate)
				needed = true;
			--totprod;
			String[] prevdiscs = discs;
			discs = new String[totprod];
			Products = new String[totprod];
			int count = 0;
			for (int i = 0; i<totprod+1; i++)
			{
				if(i!=currentproduct-1)
					discs[count++] = prevdiscs[i];		
			}
			count = 0;
			prevdiscs = dates;
			dates = new String[totprod];
			for (int i = 0; i<totprod+1; i++)
			{
				if(i!=currentproduct-1)
					dates[count++] = prevdiscs[i];		
			}
			Products = new String[totprod];
			for (int i = 0; i<totprod; i++)
			{
				Products[i] = ""+(i+1);	
			}
			count = 0;
			for (int i = 0; i < totprod + 1; i++) 
			{
				if (i != currentproduct-1) 
				{
					for (int j = 0; j < Names.length; j++) 
					{
						newwgts[count][j] = weights[i][j];
						newconts[count][j] = conts[i][j];
					}
					count++;
				}
			}
			weights = newwgts;
			conts = newconts;
			if(currentproduct > 1)
			{
				currentproduct = currentproduct - 1;
			}
			if(needed)
			updatedatevals();
		}
	}

	void addmember() {
		// TODO Auto-generated method stub
		tempNames = new String[Names.length + 1];
		for(int i = 0; i < Names.length; i++)
		{
			tempNames[i] = Names[i];
		}
		tempNames[Names.length] = ""+(Names.length+1);
		Names = new String[Names.length + 1];
		Names = tempNames;
		for(int i = 0; i < totprod; i++)
		{
			conts[i][Names.length-1] = 0;
			weights[i][Names.length-1] = 0;
		}

	}

	void dodeletemember() {
		// TODO Auto-generated method stub
		tempNames = new String[Names.length - 1];
		int count = 0;
		for(int i = 0; i < Names.length; i++)
		{
			if(i!=deletingposition)
			{
				tempNames[count] = Names[i];
				for(int j = 0; j < totprod; j++)
				{
					newwgts[j][count] = weights[j][i];
					newconts[j][count] = conts[j][i];
				}
				count++;
			}
		}
		Names = new String[Names.length - 1];
		Names = tempNames;
		weights = newwgts;
		conts = newconts;		
	}

	void makeblank() {
		// TODO Auto-generated method stub
		loaded = false;
		totprod = 1;
		Names = new String[]{ "Joe", "Ben", "Tim", "Bob","Ian" };
		Products = new String[]{"1"};
		discs = new String[]{""};
		dates = new String[]{(new SimpleDateFormat("dd/MMM/yyyy")).format(new Date(System.currentTimeMillis()))};
		firstdate = dates[0];
		lastdate = dates[0];
		currentproduct = 1;
		numberofbenf[0] = Names.length;
		for (int i = 0; i < Names.length; i++) {
			for (int j = 0; j < Names.length; j++) {
				trantable[i][j] = 0;
			}
		}
		for (int i = 0; i < Names.length; i++) {
			conts[0][i] = 0;
			weights[0][i] = 1;
			totalconts[i] = 0;
		}
		refresh();
	}

	void calculatetotalconts() {
		// TODO Auto-generated method stub
		refresh();
		float weightsum = 0;
		for(int k = 0; k < totprod; k++)
		{
			for(int i = 0;i<Names.length;i++)
			{
				for(int j = 0;j<i;j++)
				{
					weightsum = 0;
					for (int p = 0; p < Names.length;p++)
					{
						weightsum = weightsum + weights[k][p];
					}
					trantable[i][j] = trantable[i][j] + ((conts[k][j]*weights[k][i]) /(weightsum));
					trantable[i][j] = trantable[i][j] - ((conts[k][i]*weights[k][j]) /(weightsum));
					trantable[j][i] = -trantable[i][j];
				}
			}
		}
		for (int i = 0; i< Names.length; i++)
		{
			for (int j = 0; j< Names.length; j++)
			{    
				totalconts[i] = totalconts[i] + trantable[i][j];

			}
		}
	}

	public void updatedatevals()
	{
		firstdate = dates[0];
		lastdate = dates[0];
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy");
		for(int i=0;i<dates.length;i++)
		{
			try{
				if(sdf.parse(firstdate).after(sdf.parse(dates[i])))
				{
					firstdate = dates[i];
				}
				if(sdf.parse(lastdate).before(sdf.parse(dates[i])))
				{
					lastdate = dates[i];
				}
			}
			catch(ParseException ex)
			{

			}
		}
	}

	public void checkdate(String dte)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy");
		try{
			if(sdf.parse(firstdate).after(sdf.parse(dte)))
			{
				firstdate = dte;
			}
			if(sdf.parse(lastdate).before(sdf.parse(dte)))
			{
				lastdate = dte;
			}
		}
		catch(ParseException ex)
		{

		}
	}

}
