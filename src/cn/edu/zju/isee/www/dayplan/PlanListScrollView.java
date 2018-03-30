package	cn.edu.zju.isee.www.dayplan;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


public class PlanListScrollView extends ScrollView implements OnTouchListener{
	//每页要加载的数量
	public static final int 		PAGE_SIZE					=15;
	private int 					mPageIndex;
	private int 					mColumnWidth;
	private int 					mFirstColumnHeight;
	private int						mSecondColumnHeight;
	private int						mThirdColumnHeight;
	private Boolean					mIsLoaded					=false;
	private LinearLayout			mFirstColumn;
	private LinearLayout			mSecondColumn;
	private LinearLayout			mThirdColumn;
	
	private static View				sScrollLayout;
	private static int				sScrollViewHeight;
	private static int				sLastScrollY				=-1;
	private List<TextView>			mPlanViewList				=new ArrayList<TextView>();
	
	private static Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg){
			PlanListScrollView scrollView=(PlanListScrollView)msg.obj;
			int  scrollY=scrollView.getScrollY();
			//如果当前位置与上次相同，表示已经停止滚动
			if(scrollY==sLastScrollY){
				//当滚动的最底部，开始加载下一页的图片
				if(sScrollViewHeight+scrollY>=sScrollLayout.getHeight()){
					scrollView.loadNextPage();
				}
				//...........
			}else{
				sLastScrollY=scrollY;
				Message message=new Message();
				message.obj=scrollView;
				//5毫秒后再次对滚动位置判断
				handler.sendMessageDelayed(message, 5);
			}
		}
	};
	
	
	
	public PlanListScrollView(Context context,AttributeSet attrs) {
		super(context, attrs);
		setOnTouchListener(this);
	
	}



	/** 
	 * 进行一些关键性的初始化操作，获取ScrollView的高度，以及得到第一列的宽度值。并在这里开始加载第一页。 
	 */  
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if(changed&&!mIsLoaded){
			sScrollViewHeight=getHeight();
			sScrollLayout=getChildAt(0);
	        mFirstColumn = (LinearLayout) findViewById(R.id.first_column);  
	        mSecondColumn =(LinearLayout) findViewById(R.id.second_column); 
	        mThirdColumn = (LinearLayout) findViewById(R.id.third_column);
	        mColumnWidth=mFirstColumn.getWidth();
	        mIsLoaded=true;
	        loadNextPage();
		}
	}
	
    /** 
     * 监听用户的触屏事件，如果用户手指离开屏幕则开始进行滚动检测。 
     */  
    @Override  
    public boolean onTouch(View v, MotionEvent event) {  
        if (event.getAction() == MotionEvent.ACTION_UP) {  
            Message message = new Message();  
            message.obj = this;  
            handler.sendMessageDelayed(message, 5);  
        }  
        return false;  
    }  
    
    /** 
     * 开始加载下一页 
     */  
	public void loadNextPage(){
		int startIndex=mPageIndex*PAGE_SIZE;
		int endIndex=mPageIndex*PAGE_SIZE+PAGE_SIZE;
		int length=SinglePlanLab.getInstance(getContext()).getLength();
		if(startIndex<length){
			Toast.makeText(getContext(), "正在加载", Toast.LENGTH_SHORT).show();
			if(endIndex>length){
				endIndex=length;
			}
			for(int i=startIndex;i<endIndex;i++){
				//添加View
				//.................
				final TextView planText=new TextView(getContext());
				Plan p=SinglePlanLab.getInstance(getContext()).getPlan(i);
				if(p!=null){
					planText.setText(p.getDetail());
					Spannable sp=new SpannableString(planText.getText());
					sp.setSpan(new ForegroundColorSpan(Color.GRAY), 0, planText.getText().length(),
							Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					
					//设置字体
					Typeface tf=Typeface.createFromAsset(getContext().getAssets(),
							"fonts/EvernotePuck-Regular.otf");
					sp.setSpan(tf, 0, planText.getText().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					sp.setSpan(new RelativeSizeSpan(2.0f), 0,planText.getText().length() ,
							Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					sp.setSpan(new AbsoluteSizeSpan(25), 0, planText.getText().length(),
							Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); 
					planText.setText(sp);

			
					planText.setWidth(mColumnWidth);
					LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(
							mColumnWidth,Math.round(getTextViewLength(planText, p.getDetail())/mColumnWidth)*getFontHeight(30));
			
					planText.setLayoutParams(params);
					planText.setPadding(15, 15, 15, 15);
					
					findColumnToAdd(planText, mColumnWidth).addView(planText);
				}
			}
			mPageIndex++;
		}else{
			Toast.makeText(getContext(), "已经没有更多的计划", Toast.LENGTH_SHORT).show();
		}
	}
	
	public LinearLayout findColumnToAdd(TextView textView,int textHeight){
		if(mFirstColumnHeight<=mSecondColumnHeight){
			if(mFirstColumnHeight<=mThirdColumnHeight){
				mFirstColumnHeight+=textHeight;
				return mFirstColumn;
			}else{
				mThirdColumnHeight+=textHeight;
				return mThirdColumn;
			}
			
		}else{
			if(mSecondColumnHeight<=mThirdColumnHeight){
				mSecondColumnHeight+=textHeight;
				return mSecondColumn;
			}else{
				mThirdColumnHeight+=textHeight;
				return mThirdColumn;
			}
		}
	}
	
	// 计算出该TextView中文字的长度(像素)
	public static float getTextViewLength(TextView textView,String text){
	  TextPaint paint = textView.getPaint();
	  // 得到使用该paint写上text的时候,像素为多少
	  float textLength = paint.measureText(text);
	  return textLength;
	}
	
    public int getFontHeight(float fontSize)  
    {  
        Paint paint = new Paint();  
        paint.setTextSize(fontSize);  
        FontMetrics fm = paint.getFontMetrics();  
        return (int) Math.ceil(fm.descent - fm.ascent);  
    }  
}
