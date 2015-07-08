package ru.progrus.dev.notes.objects;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;

//методы и переменные доступны из любого активити

public class AppContext extends Application{
	
	public static final String ACTION_TYPE = "ru.progrus.dev.notes.TodoDocument";
	public static final String DOC_INDEX = "ru.progrus.dev.notes.AppContext.ActionIndex";
	public static final String DOC_INDEXES = "ru.progrus.dev.notes.AppContext.ActionIndexes";

	public static final int ACTION_NEW_TASK = 0;
	public static final int ACTION_UPDATE = 1;

	public static final String FIELD_CONTENT = "FIELD_CONTENT";
	public static final String FIELD_NAME = "FIELD_NAME";
	public static final String FIELD_CREATE_DATE = "FIELD_CREATE_DATE";
	public static final String FIELD_PRIORITY_TYPE = "FIELD_PRIORITY_TYPE";

	public static final String RECEIVER_DELETE_DOCUMENT = "ru.javabegin.training.android.todoproject.AppContext.DeleteDocument";
	public static final String RECEIVER_REFRESH_LISTVIEW = "ru.javabegin.training.android.todoproject.AppContext.RefreshListView";

	private ArrayList<TodoDoc> docList = new ArrayList<>();

	private BroadcastReceiver deleteDocumentReceiver = new DeleteDocumentReceiver();

	@Override
	public void onCreate() {
		super.onCreate();

		//регистрируем приемник по удалению
		LocalBroadcastManager.getInstance(this).registerReceiver(deleteDocumentReceiver, new IntentFilter(RECEIVER_DELETE_DOCUMENT));

		fillList();
	}

	@Override
	public void onTerminate() {

		super.onTerminate();
		LocalBroadcastManager.getInstance(this).unregisterReceiver(deleteDocumentReceiver);
	}


	public ArrayList<TodoDoc> getListDocuments() {
		return docList;
	}
	
	public void setListDocuments(ArrayList<TodoDoc> listDocuments) {
		this.docList = listDocuments;
	}

	public String getPrefsDir(){
		return getApplicationInfo().dataDir+"/"+"shared_prefs";
	}

	private void fillList() {

		File prefsdir = new File(getApplicationInfo().dataDir+"/shared_prefs");

		if (prefsdir.exists() && prefsdir.isDirectory()){
			String[] list = prefsdir.list();
			for (int i = 0; i < list.length; i++){
				SharedPreferences sharedPreferences = getSharedPreferences(list[i].replace(".xml",""), Context.MODE_PRIVATE);
				TodoDoc todoDoc = new TodoDoc();
				todoDoc.setContent(sharedPreferences.getString(AppContext.FIELD_CONTENT,null));
				todoDoc.setName(sharedPreferences.getString(AppContext.FIELD_NAME, null));
				todoDoc.setCreateDate(new Date(sharedPreferences.getLong(AppContext.FIELD_CREATE_DATE, 0)));
				todoDoc.setPriorityType(PriorityType.values()[sharedPreferences.getInt(AppContext.FIELD_PRIORITY_TYPE,0)]);

				docList.add(todoDoc);
			}
		}

	}
}
