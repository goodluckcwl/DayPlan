package cn.edu.zju.isee.www.dayplan;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;


//������࣬����fragment�Ĺ���avtivity��̳д˻���
public abstract class SingleActivity extends FragmentActivity {
	//��д��createFragment()����������Լ���fragment
	protected abstract Fragment createFragment();
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.activity_fragment);
		//��ȡActivity��FragmentManager
		FragmentManager fm=getSupportFragmentManager();
		//ʹ��������ͼ��ԴIDʶ��UI fragment
		Fragment fragment=fm.findFragmentById(R.id.fragmentContainer);
		//fragment������,�򴴽�һ��fragment����ӵ�������
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
