package cn.edu.zju.isee.www.dayplan;

import java.util.UUID;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;

public class TypeSelectActivity extends Activity{
	private Plan				mPlan;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		UUID id=UUID.fromString(getIntent().getStringExtra(PlanEditFragment.EXTRA_PLAN_ID));
		mPlan=SinglePlanLab.getInstance(this).getPlan(id);

	}
	
}
