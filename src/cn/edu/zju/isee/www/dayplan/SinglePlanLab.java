package cn.edu.zju.isee.www.dayplan;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import android.content.Context;
import android.util.Log;

public class SinglePlanLab {
	private static final String			TAG			="SinglePlanLab";
	private static final String			FILENAME	="plans.json";
	
	private static SinglePlanLab		sPlanLab;
	private ArrayList<Plan>				mPlans;
	private DayPlanJSONSerializer		mSerializer;
	private Context						mContext;
	
	
	
	private SinglePlanLab(Context context){
		mContext=context;
//		mPlans=new ArrayList<Plan>();
		mSerializer=new DayPlanJSONSerializer(mContext, FILENAME);
//		mPlans.add(new Plan("","今天要去跑步"));
//		mPlans.add(new Plan("","信息论与编码还有复习"));
//		mPlans.add(new Plan("","嵌入式系统设计课程作业"));
//		mPlans.add(new Plan("","网络安全课程大作业期末..........."));
		try{
			mPlans=mSerializer.loadPlans();
//创建1000条信息用于测试
			Log.d(TAG,"load plans");
		}catch(Exception e){
			Log.d(TAG,"plans.json doesn't exist,create empty planLab");
			mPlans=new ArrayList<Plan>();
		}
	}
	
	
	public static SinglePlanLab getInstance(Context context){
		if(sPlanLab==null){
			sPlanLab=new SinglePlanLab(context);
		}
		
		return sPlanLab;
	}
	
	public void addPlan(Plan plan){
		mPlans.add(plan);
	}
	
	public void deletePlan(Plan plan){
		mPlans.remove(plan);
	}
	
	public Plan getPlan(int index){
		Plan p;
		try{
			p=mPlans.get(index);
		}catch(Exception e){
			Log.d(TAG,"getPlan out of range,return null");
			return null;
		}
		return p;
	}
	
	public Plan getPlan(UUID planId){
		for(Plan p:mPlans){
			if(p.getId().equals(planId)){
				return p;
			}
		}
		return null;
	}
	
	public ArrayList<Plan>getPlans(){
		return mPlans;
	}
	public int getLength(){
		int result=0;
		for(Plan plan:mPlans){
			result++;
		}
		return result;
	}
	
	public ArrayList<Plan> getPlanByLabel(String label){
		ArrayList<Plan>result=new ArrayList<Plan>();
		for(Plan plan:mPlans){
			if(plan.getLabel().equals(label)){
				result.add(plan);
			}
		}
		return result;
	}
	
	public ArrayList <Plan> getPlanByType(String type){
		ArrayList <Plan>result=new ArrayList<Plan>();
		for(Plan plan:mPlans){
			if(plan.getType().equals(type)){
				result.add(plan);
			}
		}
		return result;
	}
	
	public ArrayList <Plan>getPlanByDate(Date date){
		ArrayList<Plan>result=new ArrayList<Plan>();
		for(Plan plan:mPlans){
			if(plan.getDate().equals(date)){
				result.add(plan);
			}
		}
		return result;
	}
	
	public Boolean savePlans(){
		try{
			mSerializer.savePlans(mPlans);
			Log.d(TAG,"plans saved to file");
			return true;
		}catch(Exception e){
			Log.d(TAG,"error save plans");
			return false;
		}
	}
	
	public void clearPlans(){
		File f=new File(mContext.getFilesDir()+"/"+FILENAME);
		if(f.exists()){
			mContext.deleteFile(FILENAME);			
		}
		mPlans.clear();
	}
}
