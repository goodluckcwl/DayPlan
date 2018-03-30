package cn.edu.zju.isee.www.dayplan;


import java.util.ArrayList;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class PlanListFragment extends ListFragment{
	private static final String				TAG			="PlanListFragment";
	private ArrayList<Plan>					mPlans;

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		
		mPlans=SinglePlanLab.getInstance(getActivity()).getPlans();
		PlanAdapter adapter=new PlanAdapter(mPlans);
		setListAdapter(adapter);
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v=super.onCreateView(inflater, container, savedInstanceState);
		ListView listView=(ListView)v.findViewById(android.R.id.list);
		if(Build.VERSION.SDK_INT<Build.VERSION_CODES.HONEYCOMB){
			registerForContextMenu(listView);
		}
		else{
			listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
			listView.setMultiChoiceModeListener(new MultiChoiceModeListener() {
				
				@Override
				public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
					return false;
				}
				
				@Override
				public void onDestroyActionMode(ActionMode mode) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public boolean onCreateActionMode(ActionMode mode, Menu menu) {
					MenuInflater inflater=mode.getMenuInflater();
					inflater.inflate(R.menu.plan_list_item_context, menu);
					return true;
				}
				
				@Override
				public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
					switch(item.getItemId()){
						case R.id.menu_item_delete_plan:
							PlanAdapter adapter=(PlanAdapter)getListAdapter();
							for(int i=adapter.getCount()-1;i>=0;i--){
								if(getListView().isItemChecked(i)){
									SinglePlanLab.getInstance(getActivity()).
									deletePlan(adapter.getItem(i));
								}
							}
							mode.finish();
							adapter.notifyDataSetChanged();
							return true;
						default:
							return false;
					}
				}
				
				@Override
				public void onItemCheckedStateChanged(ActionMode mode, int position,
						long id, boolean checked) {
					// TODO Auto-generated method stub
					
				}
			});
		}
		return v;
	}



	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Plan p=((PlanAdapter)getListAdapter()).getItem(position);
		//Start PlanActivity
		Intent i=new Intent(getActivity(), PlanPagerActivity.class);
		i.putExtra(PlanEditFragment.EXTRA_PLAN_ID, p.getId());
		startActivity(i);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		((PlanAdapter)getListAdapter()).notifyDataSetChanged();
	}



	@Override
	public void onPause() {
		super.onPause();
		((PlanAdapter)getListAdapter()).notifyDataSetChanged();
	}



	private class PlanAdapter extends ArrayAdapter<Plan>{
		public PlanAdapter(ArrayList<Plan>plans){
			super(getActivity(),0,plans);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView==null){
				convertView=getActivity().getLayoutInflater()
						.inflate(R.layout.item_list_plan, null);
			}
			
			
			Plan plan=getItem(position);
			ImageView typeImage=(ImageView)convertView.findViewById(R.id.plan_list_typeImageView);
			if(plan.getType().equals("重要且紧迫")){
				typeImage.setBackgroundResource(android.R.color.holo_green_light);
			}else if(plan.getType().equals("重要不紧迫")){
				typeImage.setBackgroundResource(android.R.color.holo_blue_bright);
			}else if(plan.getType().equals("紧迫不重要")){
				typeImage.setBackgroundResource(android.R.color.holo_purple);
			}else if(plan.getType().equals("不重要不紧迫")){
				typeImage.setBackgroundResource(android.R.color.holo_orange_light);
			}else{
				typeImage.setBackgroundResource(android.R.color.darker_gray);
			}
			
			CheckBox resolved=(CheckBox)convertView.findViewById(R.id.plan_list_isSolved);
			if(plan.getSolved()==1){
				resolved.setChecked(true);
			}else{
				resolved.setChecked(false);
			}
			
			TextView title=(TextView)convertView.findViewById(R.id.plan_list_item_titleTextView);
			title.setText(plan.getMonth()+"月"+plan.getDay()+"日 "+plan.getTitle());
			TextView detail=(TextView)convertView.findViewById(R.id.plan_list_item_detailTextView);
			detail.setText("截止："+plan.getHour()+"时"+plan.getMinute()+"分 "+plan.getDetail());
			return convertView;
		} 
	}



	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_plan_list, menu);
	}



	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
			case R.id.menu_item_new_plan:
				Plan plan=new Plan();
				SinglePlanLab.getInstance(getActivity()).addPlan(plan);
				Intent i=new Intent(getActivity(),PlanPagerActivity.class);
				i.putExtra(PlanEditFragment.EXTRA_PLAN_ID, plan.getId());
				startActivityForResult(i, 0);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
		
	}



	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		getActivity().getMenuInflater().inflate(R.menu.plan_list_item_context, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info=(AdapterContextMenuInfo)item.getMenuInfo();
		int position=info.position;
		PlanAdapter adapter=(PlanAdapter)getListAdapter();
		Plan p=adapter.getItem(position);
		
		switch(item.getItemId()){
			case R.id.menu_item_delete_plan:
				Log.d(TAG,"delete item");
				SinglePlanLab.getInstance(getActivity()).deletePlan(p);
//				SinglePlanLab.getInstance(getActivity()).clearPlans();
//				SinglePlanLab.getInstance(getActivity()).savePlans();
				adapter.notifyDataSetChanged();
				return true;
			default:
				return super.onContextItemSelected(item);
		}
	}
	
	

	
	
}
