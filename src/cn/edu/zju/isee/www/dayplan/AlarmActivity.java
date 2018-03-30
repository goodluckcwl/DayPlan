package cn.edu.zju.isee.www.dayplan;

import java.util.UUID;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;

public class AlarmActivity extends Activity{
	private Plan			mPlan;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm);
		UUID id=UUID.fromString(getIntent().getStringExtra(PlanEditFragment.EXTRA_PLAN_ID));
				
		mPlan=SinglePlanLab.getInstance(this).getPlan(id);
		new AlertDialog.Builder(AlarmActivity.this).setTitle("��������").setMessage(mPlan.getMonth()
				+"��"+mPlan.getDay()+"�� "+mPlan.getTitle()+"\n"+
				" ��ֹ��"+mPlan.getHour()+"ʱ"+mPlan.getMinute()+"��")
		.setPositiveButton("��֪����", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}
		}).show();
	}
	
}
