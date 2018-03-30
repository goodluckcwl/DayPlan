package cn.edu.zju.isee.www.dayplan;

import android.support.v4.app.Fragment;

public class PlanListActivity extends SingleActivity{

	@Override
	protected Fragment createFragment() {
		return new PlanListFragment();
	}
	
}
