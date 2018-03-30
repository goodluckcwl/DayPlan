package cn.edu.zju.isee.www.dayplan;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;


//抽象基类，所有fragment的管理avtivity类继承此基类
public abstract class SingleActivity extends FragmentActivity {
	//重写此createFragment()方法以添加自己的fragment
	protected abstract Fragment createFragment();
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.activity_fragment);
		//获取Activity的FragmentManager
		FragmentManager fm=getSupportFragmentManager();
		//使用容器视图资源ID识别UI fragment
		Fragment fragment=fm.findFragmentById(R.id.fragmentContainer);
		//fragment不存在,则创建一个fragment并添加到容器中
		if(fragment==null){
			fragment=createFragment();
			fm.beginTransaction()
			.add(R.id.fragmentContainer, fragment)  
			.commit();                             
		}
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
