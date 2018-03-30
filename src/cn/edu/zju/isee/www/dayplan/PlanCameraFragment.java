package cn.edu.zju.isee.www.dayplan;


import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class PlanCameraFragment extends Fragment{
	public static final String					EXTRA_PHOTO_NAME="photo_filename";
	private static final String					TAG				="PlanCameraFragment";
	
	private Camera 								mCamera;
	private SurfaceView							mSurfaceView;
	private View 								mProgressContainer;

	private Camera.ShutterCallback mShutterCallback=new Camera.ShutterCallback() {
		
		@Override
		public void onShutter() {
			mProgressContainer.setVisibility(View.VISIBLE);
		}
	};
	
	private Camera.PictureCallback mJpegCallBack=new Camera.PictureCallback() {
		
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			String fileName=UUID.randomUUID().toString()+".jpg";
			
			FileOutputStream os=null;
			Boolean success=true;
			try{
				os=getActivity().openFileOutput(fileName, Context.MODE_PRIVATE);
				os.write(data);
			}catch(Exception e){
				Log.e(TAG,"error writing to file"+fileName,e);
				success=false;
			}finally{
				try{
					if(os!=null)
						os.close();
				}catch (Exception e) {
						Log.e(TAG,"Error closing file "+fileName,e);
						success=false;
				}
			}
			
			if(success){
				Intent i=new Intent();
				i.putExtra(EXTRA_PHOTO_NAME, fileName);
				getActivity().setResult(Activity.RESULT_OK, i);
				Log.d(TAG,"JPEG saved at "+fileName);
			}else{
				getActivity().setResult(Activity.RESULT_CANCELED);
			}
			getActivity().finish();
		}
	};
	
	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v=inflater.inflate(R.layout.fragment_plan_camera, container,false);
		
		mProgressContainer=v.findViewById(R.id.plan_camera_progressContainer);
		mProgressContainer.setVisibility(View.INVISIBLE);
		
		Button takePictureButton=(Button)v.findViewById(R.id.plan_camera_takePictureButton);
		takePictureButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mCamera!=null){
					mCamera.takePicture(mShutterCallback, null, mJpegCallBack);
				}
			}
		});
		
		mSurfaceView=(SurfaceView)v.findViewById(R.id.plan_camera_surfaceView);
		SurfaceHolder holder=mSurfaceView.getHolder();
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		
		holder.addCallback(new SurfaceHolder.Callback() {
			
			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				if(mCamera!=null){
					mCamera.stopPreview();
				}
			}
			
			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				try{
					if(mCamera!=null){
						mCamera.setPreviewDisplay(holder);
					}
				}catch(IOException e){
					Log.e(TAG,"error setting up preview display",e);
				}
			}
			
			@Override
			public void surfaceChanged(SurfaceHolder holder, int format, int width,
					int height) {
				if(mCamera==null)
					return;
				
				Camera.Parameters parameters=mCamera.getParameters();
				Size s=getBestSupportedSize(parameters.getSupportedPreviewSizes(), width, height);
				parameters.setPreviewSize(s.width, s.height);
				s=getBestSupportedSize(parameters.getSupportedPictureSizes(), width, height);
				parameters.setPictureSize(s.width, s.height);
				mCamera.setParameters(parameters);
				try{
					mCamera.startPreview();
				}catch(Exception e){
					Log.e(TAG,"Could not start preview",e);
					mCamera.release();
					mCamera=null;
				}
			}
		});
		
		return v;
	}

	@Override
	public void onResume() {
		super.onResume();
		if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.GINGERBREAD){
			mCamera=Camera.open(0);
		}
		else{
			mCamera=Camera.open();
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		if(mCamera!=null){
			mCamera.release();
			mCamera=null;
		}
	}
	
	//get the largest size available
	private Size getBestSupportedSize(List<Size>sizes,int width,int height){
		Size bestSize=sizes.get(0);
		int largestArea=bestSize.height*bestSize.width;
		for(Size s:sizes){
			int area=s.width*s.height;
			if(area>largestArea){
				bestSize=s;
				largestArea=area;
			}
		}
		
		return bestSize;
	}
	

	
	
}
