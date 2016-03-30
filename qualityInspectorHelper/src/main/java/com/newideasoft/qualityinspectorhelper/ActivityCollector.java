package com.newideasoft.qualityinspectorhelper;

import java.util.ArrayList;

import android.app.Activity;

public class ActivityCollector {
	public static ArrayList<Activity> activitiesList = new ArrayList<Activity>();
	public static void addActivity(Activity activity){
		activitiesList.add(activity);
	}
	public static void removeActivity(Activity activity){
		activitiesList.remove(activity);
	}
	public static void finishiAll(){
		for(Activity activity:activitiesList){
			if(!activity.isFinishing()){
				activity.finish();
			}
		}
	}
}
