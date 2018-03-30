package cn.edu.zju.isee.www.dayplan;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class PlanEditFragment extends Fragment{
	public static final String			EXTRA_PLAN			="cn.edu.zju.isee.www.dayplan";
	public static final String			EXTRA_PLAN_ID		="extra_id";
	private static final String			TAG					="PlanEditFragment";	
	private static final String 		DIALOG_DATE			="date";
	private static final String 		DIALOG_TYPE			="type";
	private static final int 			REQUEST_DATE		=0;
	private static final int 			REQUEST_PHOTO		=1;
	private static final int			REQUEST_TIME		=2;
	private static final int 			REQUEST_TYPE		=3;
	private Plan 						mPlan;
	private EditText					mTitleField;
	private Button						mDateButton;
	private Button						mShareButton;
	private Button						mAlarmButton;
	private Button						mAnalyseButton;
	private ImageButton					mPhotoButton;
	private CheckBox					mSolvedCheckBox;
	private ImageView					mPhotoView;
	private TextView					mTypeTextView;
	private ImageView					mTypeImageView;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		UUID planId=(UUID)getArguments().getSerializable(EXTRA_PLAN_ID);
		mPlan=SinglePlanLab.getInstance(getActivity()).getPlan(planId);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v=inflater.inflate(R.layout.fragment_plan_edit, container,false);
		
		if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB){
			if(NavUtils.getParentActivityIntent(getActivity())!=null){
				getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
			}
		}
		
		mTitleField=(EditText)v.findViewById(R.id.plan_title);
		mTitleField.setText(mPlan.getTitle());
		mTitleField.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				mPlan.setTitle(s.toString());
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		
		mDateButton=(Button)v.findViewById(R.id.plan_date);
		mDateButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				FragmentManager fm=getActivity().getSupportFragmentManager();
				DatePickerFragment dialog=DatePickerFragment.newInstance(mPlan.getDate());
				dialog.setTargetFragment(PlanEditFragment.this,REQUEST_DATE);
				dialog.show(fm,DIALOG_DATE);
				
			}
		});
		updateDate();
		
		mSolvedCheckBox=(CheckBox)v.findViewById(R.id.plan_solved);
		if(mPlan.getSolved()==1){
			mSolvedCheckBox.setChecked(true);
		}else{
			mSolvedCheckBox.setChecked(false);
		}
		mSolvedCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					mPlan.setSolved(1);
				}else{
					mPlan.setSolved(0);
				}
			}
		});
		
		mPhotoButton=(ImageButton)v.findViewById(R.id.plan_imageButton);
		mPhotoButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i=new Intent(getActivity(),PlanCameraActivity.class);
				startActivityForResult(i, REQUEST_PHOTO);
			}
		});
		
		mPhotoView=(ImageView)v.findViewById(R.id.plan_imageView);
		mPhotoView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i=new Intent(getActivity(),ImageActivity.class);
				Photo p=mPlan.getPhoto();
				if(p!=null){
					String path=getActivity().getFileStreamPath(p.getFileName()).getAbsolutePath();
					i.putExtra(ImageFragment.EXTRA_IMAGE_PATH, path);
					startActivity(i);
				}
			}
		});
		
		mShareButton=(Button)v.findViewById(R.id.plan_shareButton);
		mShareButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i=new Intent(Intent.ACTION_SEND);
				i.setType("text/plain");
				i.putExtra(EXTRA_PLAN, getText());
//
				startActivity(i);
			}
		});
		
		mAlarmButton=(Button)v.findViewById(R.id.plan_alarmButton);
		mAlarmButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Calendar currentTime=Calendar.getInstance();
				final AlarmManager alarmManager=(AlarmManager)getActivity().
						getSystemService(getActivity().ALARM_SERVICE);
				Intent i=new Intent(getActivity(),AlarmActivity.class);
				i.putExtra(EXTRA_PLAN_ID, mPlan.getId().toString());
				final PendingIntent pi=PendingIntent.getActivity(getActivity(), REQUEST_TIME, i, 0);
				if(mPlan.getIsSetAlarm()==0){
					new TimePickerDialog(getActivity(), 
							new OnTimeSetListener() {
								
								@Override
								public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
									Calendar c=Calendar.getInstance();
									c.setTime(mPlan.getDate());
									c.set(Calendar.HOUR_OF_DAY,hourOfDay);
									c.set(Calendar.MINUTE,minute);
									Date d=c.getTime();
									mPlan.setDate(d);
									mPlan.setIsSetAlarm(1);
									alarmManager.set(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),pi);
									
									//
									Toast.makeText(getActivity(), "提醒设置完毕", Toast.LENGTH_SHORT).show();
								}
							}, currentTime.get(Calendar.HOUR_OF_DAY), currentTime.get(Calendar.MINUTE),
							true).show();
					mAlarmButton.setText("取消提醒");
					mPlan.setIsSetAlarm(1);
				}else{
					alarmManager.cancel(pi);
					Toast.makeText(getActivity(), "提醒已取消", Toast.LENGTH_SHORT).show();
					mAlarmButton.setText("提醒我");
					mPlan.setIsSetAlarm(0);
				}
			}
		});
		
		mTypeTextView=(TextView)v.findViewById(R.id.plan_typeTextView);
		mTypeImageView=(ImageView)v.findViewById(R.id.plan_typeImageView);
		updateType();
		mTypeTextView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				FragmentManager fm=getActivity().getSupportFragmentManager();
				TypeSelectFragment dialog=TypeSelectFragment.newInstance(mPlan.getType());
				dialog.setTargetFragment(PlanEditFragment.this,REQUEST_TYPE);
				dialog.show(fm,DIALOG_TYPE);
			}
		});
		
		return v;
	}
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode!=Activity.RESULT_OK){
			return;
		}
		if(requestCode==REQUEST_DATE){
			Date date=(Date)data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
			mPlan.setDate(date);
			updateDate();
		}else if(requestCode==REQUEST_PHOTO){
			String fileName=data.getStringExtra(PlanCameraFragment.EXTRA_PHOTO_NAME);
			if(fileName!=null){
				Photo photo=new Photo(fileName);
				mPlan.setPhoto(photo);
				showPhoto();
				Log.d(TAG,"plan:"+mPlan.getTitle()+" has a photo");
			}
		}else if(requestCode==REQUEST_TYPE){
			String type=(String)data.getSerializableExtra(TypeSelectFragment.EXTRA_TYPE);
			mPlan.setType(type);
			//.........
			updateType();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public static PlanEditFragment newInstance(UUID planId){
		Bundle args=new Bundle();
		args.putSerializable(EXTRA_PLAN_ID, planId);
		PlanEditFragment fragment=new PlanEditFragment();
		fragment.setArguments(args);
		
		return fragment;
	}
	public void updateDate(){
		mDateButton.setText(""+mPlan.getYear()+"-"+mPlan.getMonth()+"-"+mPlan.getDay());
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
			case android.R.id.home:
				if(NavUtils.getParentActivityIntent(getActivity())!=null){
					NavUtils.navigateUpFromSameTask(getActivity());
				}
				return true;
			default:
				return super.onOptionsItemSelected(item);
				
		}
	}
	
	
	
	@Override
	public void onStart() {
		super.onStart();
		showPhoto();
	}

	
	@Override
	public void onStop() {
		super.onStop();
		ImageUtils.cleanImageView(mPhotoView);
	}
	

	@Override
	public void onPause() {
		super.onPause();
		SinglePlanLab.getInstance(getActivity()).savePlans();
	}

	private void showPhoto(){
		Photo p=mPlan.getPhoto();
		Bitmap b=null;
		if(p!=null){
			String path=getActivity().getFileStreamPath(p.getFileName()).getAbsolutePath();
			b=ImageUtils.decodeSampledBitmapFromFile(path, 240, 240);
		}
		mPhotoView.setImageBitmap(b);
	}
	
	private void updateType(){
		if(mPlan.getType().equals("重要且紧迫")){
			mTypeTextView.setText("      重要且紧迫");
			mTypeImageView.setBackgroundResource(android.R.color.holo_green_light);
					
		}else if(mPlan.getType().equals("重要不紧迫")){
			mTypeTextView.setText("      重要不紧迫");
			mTypeImageView.setBackgroundResource(android.R.color.holo_blue_bright);
		}else if(mPlan.getType().equals("紧迫不重要")){
			mTypeTextView.setText("      紧迫不重要");
			mTypeImageView.setBackgroundResource(android.R.color.holo_purple);
		}else if(mPlan.getType().equals("不重要不紧迫")){
			mTypeTextView.setText("      不重要不紧迫");
			mTypeImageView.setBackgroundResource(android.R.color.holo_orange_light);
		}else{
			mTypeTextView.setText("      未分类");
			mTypeImageView.setBackgroundResource(android.R.color.darker_gray);
			
		}
	}
	
	private String	getText(){
		return new String(mPlan.getMonth()+"-"+mPlan.getDay()+" \n"+mPlan.getTitle()+
				"\n 任务类型"+mPlan.getType());
	}
}
