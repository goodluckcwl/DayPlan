package cn.edu.zju.isee.www.dayplan;

import java.util.UUID;

import android.support.v4.app.Fragment;

public class PlanEditActivity extends SingleActivity{

	@Override
	protected Fragment createFragment() {
		UUID planId=(UUID)getIntent().getSerializableExtra(PlanEditFragment.EXTRA_PLAN_ID);
		
		return PlanEditFragment.newInstance(planId);
	}
	
}
