package cn.edu.zju.isee.www.dayplan;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.TextView;

public class TypeSelectFragment extends DialogFragment{
	public static final String				EXTRA_TYPE		="DayPlan.type";
	private String							mType=new String();
	private TextView						mImportantUrgent;
	private TextView						mImportanteasy;
	private TextView						mUmimportantUrgent;
	private TextView						mUnimportantEasy;
	private String[]						mTypes=			new String[]{"重要且紧迫","重要不紧迫","紧迫不重要","不重要不紧迫"};
	
	public static TypeSelectFragment newInstance(String type){
		Bundle args=new Bundle();
		args.putSerializable(EXTRA_TYPE, type);
		
		TypeSelectFragment fragment=new TypeSelectFragment();
		fragment.setArguments(args);
		
		return fragment;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		mType=(String)getArguments().getSerializable(EXTRA_TYPE);
		
		View v=getActivity().getLayoutInflater()
				.inflate(R.layout.dialog_type, null);
		

		return new AlertDialog.Builder(getActivity())
//		.setView(v)
		.setTitle(R.string.type_dialog_title)
		.setItems(mTypes,new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mType=mTypes[which];
				sendResult(Activity.RESULT_OK);
			}
		})
		.create();
		
	}
	
	private void sendResult(int resultCode){
		if(getTargetFragment()==null){
			return ;
		}
		Intent i=new Intent();
		i.putExtra(EXTRA_TYPE, mType);
		
		getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, i);
	
	}
	
}
