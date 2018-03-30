package cn.edu.zju.isee.www.dayplan;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import android.content.Context;
import android.util.Log;

public class DayPlanJSONSerializer {
	private static final String				TAG			="DayPlanJSONSerializer";
	private Context mContext;
	private String mFileName;
	
	public DayPlanJSONSerializer(Context context,String fileName){
		mContext=context;
		mFileName=fileName;
	}
	
	public void savePlans(ArrayList<Plan> plans)throws
		JSONException,IOException{
		//Build an array in JSON
		JSONArray array=new JSONArray();
		for(Plan t:plans){
			array.put(t.toJSON());
		}
		
		//Write to disk
		Writer writer=null;
		try{
			OutputStream out=mContext
					.openFileOutput(mFileName, Context.MODE_PRIVATE);
			writer=new OutputStreamWriter(out);
			writer.write(array.toString());
		}finally{
			if(writer!=null)
				writer.close();
		}
	}
	
	public ArrayList<Plan> loadPlans()throws IOException,JSONException{
		ArrayList<Plan>plans=new ArrayList<Plan>();
		BufferedReader reader=null;
		try{
			//open and read the file into a StringBuilder
			InputStream in=mContext.openFileInput(mFileName);
			reader=new BufferedReader(new InputStreamReader(in));
			StringBuilder jsonString=new StringBuilder();
			String line=null;
			while((line=reader.readLine())!=null){
				//Line breaks are omitted and irrelevent
				jsonString.append(line);
			}
			//Parse the JSON using JSONTokener
			JSONArray array=(JSONArray)new JSONTokener(jsonString.toString()).nextValue();
			//Build the array of faces from jsonObjects
			for(int i=0;i<array.length();i++){
			
				plans.add(new Plan(array.getJSONObject(i)));
			}
		}catch(FileNotFoundException e){
			Log.d(TAG,"Error loading faces");
			//ignore this one,it happens when starting fresh
		}finally{
			if(reader!=null)
				reader.close();
		}
		return plans;
	}

}
