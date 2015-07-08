package ru.progrus.dev.notes;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import ru.progrus.dev.notes.objects.AppContext;
import ru.progrus.dev.notes.objects.ComparatorList;
import ru.progrus.dev.notes.objects.PriorityType;
import ru.progrus.dev.notes.objects.TodoAdapter;
import ru.progrus.dev.notes.objects.TodoDoc;


public class ListActivity extends Activity {

//    public static String TODO_DOCUMENT = "ru.progrus.dev.notes.TodoDocument";
//    public static int TODO_DETAILS_REQUEST =1;

    private ListView listTasks;
    private EditText txtSearch;
    private Intent intent;
    private TodoAdapter arrayAdapter;
    private ArrayList<TodoDoc> docList ;

    //по умолчанию сортировку
    private static Comparator<TodoDoc> comparator = ComparatorList.getDateComparator();

    private MenuItem menuSort;
    private MenuItem menuDelete;
    private MenuItem menuCreate;



    private CheckboxListener checkboxListener = new CheckboxListener();

    private BroadcastReceiver refreshListViewReceiver = new RefreshListViewReceiver();

    private class RefreshListViewReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            sort();

        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        listTasks = (ListView) findViewById(R.id.listTasks);
        listTasks.setOnItemClickListener(new ListViewClickListener());
        //убирает надпись если есть запись
        listTasks.setEmptyView(findViewById(R.id.emptyView));

        docList = ((AppContext)getApplicationContext()).getListDocuments();

        txtSearch = (EditText) findViewById(R.id.txtSearch);
        //фильтрует по символам
        txtSearch.addTextChangedListener(new TextChangeListener());

        //рисуем системную кнопку "назад" возле иконки
        getActionBar().setDisplayHomeAsUpEnabled(false);

//        fillTodoList();

        intent = new Intent(this,DetailsActivity.class);

//        fillList();

        //регистрируем приемник

        LocalBroadcastManager.getInstance(this).registerReceiver(
                refreshListViewReceiver,
                new IntentFilter(AppContext.RECEIVER_REFRESH_LISTVIEW));

    }

    private void fillList() {

        File prefsdir = new File(getApplicationInfo().dataDir+"/shared_prefs");

        if (prefsdir.exists() && prefsdir.isDirectory()){
            String[] list = prefsdir.list();
            for (int i = 0; i < list.length; i++){
                SharedPreferences sharedPreferences = getSharedPreferences(list[i].replace(".xml",""),Context.MODE_PRIVATE);
                TodoDoc todoDoc = new TodoDoc();
                todoDoc.setContent(sharedPreferences.getString(AppContext.FIELD_CONTENT,null));
                todoDoc.setName(sharedPreferences.getString(AppContext.FIELD_NAME, null));
                todoDoc.setCreateDate(new Date(sharedPreferences.getLong(AppContext.FIELD_CREATE_DATE, 0)));
                todoDoc.setPriorityType(PriorityType.values()[sharedPreferences.getInt(AppContext.FIELD_PRIORITY_TYPE,0)]);

                docList.add(todoDoc);
            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

//        arrayAdapter = new TodoAdapter(this,docList);
//        listTasks.setAdapter(arrayAdapter);

        sort();

        //фильтрует по символам
//        checkSearchActive();
//        checkMenuActive();
    }

    private void checkMenuActive() {
        if(menuSort==null || menuDelete == null)return;
        if (docList.isEmpty()){
            menuSort.setEnabled(false);
            menuDelete.setEnabled(false);
            menuCreate.setEnabled(true);
            txtSearch.setEnabled(false);
        }else {
            menuDelete.setEnabled(!indexesForDelete.isEmpty());
            menuSort.setEnabled(indexesForDelete.isEmpty());
            menuCreate.setEnabled(indexesForDelete.isEmpty());
            txtSearch.setEnabled(indexesForDelete.isEmpty());
        }


    }

    private void checkSearchActive() {
        //если пустой
        if (docList.isEmpty()) {
            txtSearch.setEnabled(false);
        } else {
            txtSearch.setEnabled(true);
//            arrayAdapter.getFilter().filter(txtSearch.getText());
        }
    }

//    private void fillTodoList() {
//
////        TodoDoc d1 = new TodoDoc("s1","c1",null);
////        TodoDoc d2 = new TodoDoc("s2","c2",null);
////        TodoDoc d3 = new TodoDoc("s3","c3",null);
////
////
////        docList.add(d1);
////        docList.add(d2);
////        docList.add(d3);
//
//        arrayAdapter = new TodoAdapter(this,docList);
//        listTasks.setAdapter(arrayAdapter);
//
//
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list, menu);

        menuSort = menu.findItem(R.id.menu_sort);
        menuDelete = menu.findItem(R.id.menu_delete_check);
        menuCreate = menu.findItem(R.id.new_task);

        checkMenuActive();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //проверяем на повторение
        //если пункт уже выбран два раза не нужно делать
        if (item.isChecked()){
            return true;
        }

        switch (item.getItemId()){
            case R.id.new_task:{
//                TodoDoc todoDoc = new TodoDoc();
//                todoDoc.setName(getResources().getString(R.string.new_doc));
//                showDoc(todoDoc);

                //многопоточие
                Bundle bundle = new Bundle();
                bundle.putInt(AppContext.ACTION_TYPE,AppContext.ACTION_NEW_TASK);

                intent.putExtras(bundle);
                startActivity(intent);

                return true;
            }

            case R.id.menu_sort_date:{

                item.setChecked(true);
                comparator = ComparatorList.getDateComparator();
                sort();

                return true;

            }

            case R.id.menu_sort_name:{

                item.setChecked(true);
                comparator = ComparatorList.getNameComparator();
                sort();

                return true;
            }

            case R.id.menu_sort_priority:{

                item.setChecked(true);
                comparator = ComparatorList.getPriorityComparator();
                sort();

                return true;

            }

            case R.id.menu_delete_check: {

                if (!indexesForDelete.isEmpty()) {

                    Intent intent = new Intent(AppContext.RECEIVER_DELETE_DOCUMENT);

                    intent.putIntegerArrayListExtra(AppContext.DOC_INDEXES,
                            new ArrayList<Integer>(indexesForDelete));
                    LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

                }


                return true;

            }

            default:
                break;
        }


            return super.onOptionsItemSelected(item);
    }

    private void sort() {

        indexesForDelete.clear();

        Collections.sort(docList, comparator);

        updateIndexes();

        arrayAdapter = new TodoAdapter(this,docList, checkboxListener);
        listTasks.setAdapter(arrayAdapter);

        arrayAdapter.getFilter().filter(txtSearch.getText());

        checkMenuActive();

        setTitle(getResources().getString(R.string.app_name) + " (" + docList.size() + ")");
    }

    public void clearSeach(View view){

        txtSearch.setText("");

    }
//
//    private void showDoc(TodoDoc todoDoc) {
//
//        Intent intentDetails = new Intent(this,DetailsActivity.class);
//        intentDetails.putExtra(TODO_DOCUMENT, todoDoc);
//        startActivityForResult(intentDetails, TODO_DETAILS_REQUEST);
//
//
//
//
//
//
//
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == TODO_DETAILS_REQUEST) {
//
//            TodoDoc todoDoc = null;
//
//            switch (resultCode){
//                case RESULT_CANCELED:
////                    Toast.makeText(this,"Canceled",Toast.LENGTH_SHORT).show();
//                    break;
//                case DetailsActivity.RESULT_SAVE:
////                    Toast.makeText(this,"Saved",Toast.LENGTH_SHORT).show();
//                    todoDoc = (TodoDoc) data.getSerializableExtra(TODO_DOCUMENT);
//                    todoDoc.setCreateDate(new Date());
//                    addDoc(todoDoc);
//                    break;
//
//                case DetailsActivity.RESULT_DELETE:{
//                    todoDoc = (TodoDoc) data.getSerializableExtra(TODO_DOCUMENT);
//                    deleteDoc(todoDoc);
//                    break;
//                }
//
//                default:
//                    break;
//            }
//
//        }
//    }

//    private void deleteDoc(TodoDoc todoDoc) {
//
//        docList.remove(todoDoc.getNumber());
//        arrayAdapter.notifyDataSetChanged();
//
//    }

//    private void addDoc(TodoDoc todoDoc) {
//
//        if (todoDoc.getNumber() == -1){
//        docList.add(todoDoc);
////        todoDoc.setNumber(docList.indexOf(todoDoc));
//        }else{
//            docList.set(todoDoc.getNumber(),todoDoc);
//        }
//
//
//        Collections.sort(docList);
//        arrayAdapter.notifyDataSetChanged();
//
//    }


    class ListViewClickListener implements AdapterView.OnItemClickListener{

         @Override
         public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//             TodoDoc doc = (TodoDoc) parent.getAdapter().getItem(position);
//             doc.setNumber(position);//позиция записи при нажатие на нее
//             showDoc(doc);

             Bundle bundle = new Bundle();
             bundle.putInt(AppContext.ACTION_TYPE, AppContext.ACTION_UPDATE);
             bundle.putInt(AppContext.DOC_INDEX, ((TodoDoc) parent.getAdapter().getItem(position)).getNumber());

             intent.putExtras(bundle);
             startActivity(intent);

         }
     }

    private class TextChangeListener implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            arrayAdapter.getFilter().filter(s);

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    private void updateIndexes() {
        int i = 0;
        for (TodoDoc doc : docList) {
            doc.setNumber(i++);
        }
    }

    //заполняем набор чеками
    private Set<Integer> indexesForDelete = new TreeSet<Integer>();

    private class CheckboxListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            CheckBox checkBox = (CheckBox) v;
            TodoDoc todoDocument = (TodoDoc) checkBox.getTag();
            todoDocument.setChecked(checkBox.isChecked());

            RelativeLayout ve = (RelativeLayout)v.getParent();

            TextView txtTodoName = (TextView)ve.findViewById(R.id.todo_name);
            TextView txtTodoDate = (TextView)ve.findViewById(R.id.todo_date);

            if (checkBox.isChecked()) {
                indexesForDelete.add(todoDocument.getNumber());
                txtTodoName.setTextColor(Color.LTGRAY);
                txtTodoDate.setTextColor(Color.LTGRAY);
            } else {
                indexesForDelete.remove(todoDocument.getNumber());
                txtTodoName.setTextColor(Color.BLACK);
                txtTodoDate.setTextColor(Color.BLACK);
            }


            checkMenuActive();
//            checkControlsActive();

        }

    }

}
