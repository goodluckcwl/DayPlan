package cn.edu.zju.isee.www.dayplan;

import java.util.ArrayList;
import java.util.UUID;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

public class PlanPagerActivity extends FragmentActivity{
	private ViewPager mViewPaper;
	private ArrayList<Plan> mPlans;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		mViewPaper=new ViewPager(this);
		mViewPaper.setId(R.id.viewPaper);
		setContentView(mViewPaper);
		
		mPlans=SinglePlanLab.getInstance(this).getPlans();
		FragmentManager fm=getSupportFragmentManager();
		mViewPaper.setAdapter(new FragmentStatePagerAdapter(fm) {
			
			@Override
			public int getCount() {
				return mPlans.size();
			}
			
			@Override
			public Fragment getItem(int arg0) {
				Plan p=mPlans.get(arg0);
				return PlanEditFragment.newInstance(p.getId());
			}
		});
		
		UUID planId=(UUID)getIntent()
				.getSerializableExtra(PlanEditFragment.EXTRA_PLAN_ID);
		for(int i=0;i<mPlans.size();i++){
			if(mPlans.get(i).getId().equals(planId)){
				mViewPaper.setCurrentItem(i);
				break;
			}
		}
	}
	
	
}
