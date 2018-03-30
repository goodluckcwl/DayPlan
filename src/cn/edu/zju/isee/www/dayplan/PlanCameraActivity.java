package cn.edu.zju.isee.www.dayplan;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Window;
import android.view.WindowManager;

public class PlanCameraActivity extends SingleActivity{
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		super.onCreate(savedInstanceState);
	}

	@Override
	protected Fragment createFragment() {
		return new PlanCameraFragment();
	}
	
}
