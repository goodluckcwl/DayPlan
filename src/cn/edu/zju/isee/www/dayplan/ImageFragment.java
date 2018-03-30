package cn.edu.zju.isee.www.dayplan;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ImageFragment extends Fragment{
	public static final String	EXTRA_IMAGE_PATH=
			"image_path";
	private ZoomImageView			mImageView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v=inflater.inflate(R.layout.fragment_image, container, false);
		mImageView=(ZoomImageView)v.findViewById(R.id.image);
		String imagePath=getActivity().getIntent().getStringExtra(EXTRA_IMAGE_PATH);

		Bitmap bitmap=BitmapFactory.decodeFile(imagePath);
		mImageView.setImageBitmap(bitmap);
		return v;
		
	}
	

	@Override
	public void onDestroyView() {
		
		super.onDestroyView();
	}
	
	
}
