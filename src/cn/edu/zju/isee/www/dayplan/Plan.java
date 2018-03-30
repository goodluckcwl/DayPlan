package cn.edu.zju.isee.www.dayplan;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;

public class Plan{
	private static final String			JSON_ID					="id";
	private static final String			JSON_TITLE				="title";
	private static final String			JSON_DETAIL				="detail";
	private static final String			JSON_TYPE				="type";
	private static final String			JSON_LABEL				="label";
	private static final String			JSON_DEAD_HOUR			="deadHour";
	private static final String			JSON_DEAD_MINUTE		="deadMinute";
	private static final String			JSON_DAY				="day";
	private static final String 		JSON_MONTH				="month";
	private static final String			JSON_DATE				="date";
	private static final String			JSON_SOLVED				="solved";
	private static final String			JSON_PHOTO				="photo";
	private static final String			JSON_ALARM				="alarm";
	private static final String			JSON_RESOLVED			="resolved";
	
	private UUID 						mId;
	private String						mTitle;
	private String 						mDetail;
	private String						mType;
	private String						mLabel;
	private	Date						mDate					=new Date();
//	private int							mDay;
//	private int 						mMonth;
//	private int							mDeadHour;
//	private int							mDeadMinute;
	private Bitmap						mImage;
	private Photo						mPhoto;
	private	int							mIsSetAlarm;
	private int							mIsSolved;
	
	public Plan(){
		mId=UUID.randomUUID();	
		mType=new String();
		mDetail=new String();
		mTitle=new String();
		mLabel=new String();
	}
	public Plan(String title,String detail){
		mId=UUID.randomUUID();
		mTitle=title;
		mDetail=detail;
	}
	public Plan(String title,String detail,String type){
		this(title,detail);
		mType=type;
	}
	public Plan(String title,String detail,String type,String label,Date date){
		this(title,detail,type);
		mLabel=label;
		mDate=date;
	}
	
	public Plan(JSONObject json)throws JSONException{
		if(json.has(JSON_ID)){
			mId=UUID.fromString(json.getString(JSON_ID));
		}
		if(json.has(JSON_TITLE)){
			mTitle=json.getString(JSON_TITLE);
		}
		if(json.has(JSON_DETAIL)){
			mDetail=json.getString(JSON_DETAIL);
		}
		if(json.has(JSON_TYPE)){
			mType=json.getString(JSON_TYPE);
		}
		if(json.has(JSON_LABEL)){
			mLabel=json.getString(JSON_LABEL);
		}
		if(json.has(JSON_MONTH)&&json.has(JSON_DAY)&&json.has(JSON_DEAD_HOUR)
				&&json.has(JSON_DEAD_MINUTE)){
			mDate=new GregorianCalendar(2015, json.getInt(JSON_MONTH),
					json.getInt(JSON_DAY), json.getInt(JSON_DEAD_HOUR),
					json.getInt(JSON_DEAD_MINUTE)).getTime();
		}
		if(json.has(JSON_PHOTO)){
			mPhoto=new Photo(json.getJSONObject(JSON_PHOTO));
		}
		if(json.has(JSON_ALARM)){
			mIsSetAlarm=json.getInt(JSON_ALARM);
		}
		if(json.has(JSON_RESOLVED)){
			mIsSolved=json.getInt(JSON_RESOLVED);
		}
	}	
	
	public JSONObject toJSON()throws JSONException{
		JSONObject json=new JSONObject();
		json.put(JSON_ID, mId.toString());
		json.put(JSON_TITLE, mTitle);
		json.put(JSON_DETAIL, mDetail);
		json.put(JSON_TYPE, mType);
		json.put(JSON_LABEL, mLabel);
		
		Calendar c=Calendar.getInstance();
		c.setTime(mDate);
		json.put(JSON_MONTH, c.get(Calendar.MONTH));
		json.put(JSON_DAY, c.get(Calendar.DAY_OF_MONTH));
		json.put(JSON_DEAD_HOUR, c.get(Calendar.HOUR_OF_DAY));
		json.put(JSON_DEAD_MINUTE, c.get(Calendar.MINUTE));
	
		if(mPhoto!=null){
			json.put(JSON_PHOTO, mPhoto.toJSON());
		}
		json.put(JSON_ALARM, mIsSetAlarm);
		json.put(JSON_RESOLVED, mIsSolved);
		
		return json;
	}
	
	
	public UUID getId() {
		return mId;
	}
	public void setId(UUID id) {
		mId = id;
	}
	public String getTitle() {
		return mTitle;
	}
	public void setTitle(String title) {
		mTitle = title;
	}
	
	public String getDetail() {
		return mDetail;
	}

	public void setDetail(String detail) {
		mDetail = detail;
	}

	public String getType() {
		return mType;
	}

	public void setType(String type) {
		mType = type;
	}

	public String getLabel() {
		return mLabel;
	}

	public void setLabel(String label) {
		mLabel = label;
	}

	public Date getDate() {
		return mDate;
	}
	public int getYear(){
		
		return getCalendar().get(Calendar.YEAR);
	}
	public int getMonth(){
		
		return getCalendar().get(Calendar.MONTH)+1;
	}
	public int getDay(){
		
		return getCalendar().get(Calendar.DAY_OF_MONTH);
	}
	public int getHour(){
		
		return getCalendar().get(Calendar.HOUR_OF_DAY);
	}
	public int getMinute(){
		return getCalendar().get(Calendar.MINUTE);
	}
	
	private Calendar getCalendar(){
		Calendar c=Calendar.getInstance();
		c.setTime(mDate);
		return c;
	}
	public void setDate(Date date) {
		mDate = date;
	}
	public Bitmap getImage() {
		return mImage;
	}

	public void setImage(Bitmap image) {
		mImage = image;
	}
	public int getSolved() {
		return mIsSolved;
	}
	public void setSolved(int isSolved) {
		mIsSolved =isSolved;
	}
	public Photo getPhoto() {
		return mPhoto;
	}
	public void setPhoto(Photo photo) {
		mPhoto = photo;
	}
	public int getIsSetAlarm() {
		return mIsSetAlarm;
	}
	public void setIsSetAlarm(int isSetAlarm) {
		mIsSetAlarm = isSetAlarm;
	}
	
	

}
