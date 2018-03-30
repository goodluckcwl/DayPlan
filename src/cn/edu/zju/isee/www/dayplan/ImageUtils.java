package cn.edu.zju.isee.www.dayplan;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

public class ImageUtils {
	
	//Bitmap
	public static Bitmap decodeSampledBitmapFromFile(String path,int reqWidth,int reqHeight){
		//First decode with inJustDecodeBounds true to check dimension
		final BitmapFactory.Options options=new BitmapFactory.Options();
		options.inJustDecodeBounds=true;
		BitmapFactory.decodeFile(path, options);
		//Calculate inSampleSize
		int mInSampleSize=calculateInSampleSize(options, reqWidth, reqHeight);
		options.inSampleSize=mInSampleSize;
		
		//Decode bitmap with inSampleSize set
		options.inJustDecodeBounds=false;

		return BitmapFactory.decodeFile(path,options);
	}
	
	public static int calculateInSampleSize(
			BitmapFactory.Options options,int reqWidth,int reqHeight)
	{
		//raw height and width if image
		final int height=options.outHeight;
		final int width=options.outWidth;
		int inSampleSize=1;
		
		if(height>reqHeight||width>reqWidth){
			final int halfHeight=height/2;
			final int halfWidth=width/2;
			
			while((halfHeight/inSampleSize)>reqHeight
					&&(halfWidth/inSampleSize)>reqWidth){
				inSampleSize*=2;
			}
		}
		
		return inSampleSize;
	}
	
	public static void cleanImageView(ImageView imageView){
		if(!(imageView.getDrawable()instanceof BitmapDrawable))
			return;
		BitmapDrawable b=(BitmapDrawable)imageView.getDrawable();
		if(b.getBitmap()!=null){
			b.getBitmap().recycle();
		}
		imageView.setImageBitmap(null);
	}

}
