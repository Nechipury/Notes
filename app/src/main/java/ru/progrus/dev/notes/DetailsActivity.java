package ru.progrus.dev.notes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import ru.progrus.dev.notes.objects.AppContext;
import ru.progrus.dev.notes.objects.PriorityType;
import ru.progrus.dev.notes.objects.TodoDoc;


public class DetailsActivity extends Activity {

//    public static final int RESULT_SAVE = 100;
//    public static final int RESULT_DELETE = 101;

    private static final int NAME_LENGTH = 20;

    private TodoDoc todoDoc;
    private EditText txtDetails;

    private ArrayList<TodoDoc> docList;

    private int actionType;
    private int docIndex;

    private PriorityType priorityType;
    private MenuItem menuPriority;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        txtDetails = (EditText) findViewById(R.id.txtDetails);

        docList = ((AppContext)getApplicationContext()).getListDocuments();

//        todoDoc = (TodoDoc) getIntent().getSerializableExtra(ListActivity.TODO_DOCUMENT);
//        setTitle(todoDoc.getName());
//        txtDetails.setText(todoDoc.getContent());

        getActionBar().setDisplayHomeAsUpEnabled(true);

        actionType = getIntent().getExtras().getInt(AppContext.ACTION_TYPE);

        prepareDocument(actionType);
    }

    private void prepareDocument(int actionType) {
        switch (actionType) {
            case AppContext.ACTION_NEW_TASK:
                todoDoc = new TodoDoc();
                break;

            case AppContext.ACTION_UPDATE:
                docIndex = getIntent().getExtras().getInt(AppContext.DOC_INDEX);
                todoDoc = docList.get(docIndex);
                txtDetails.setText(todoDoc.getContent());
                break;

            default:
                break;
        }

        priorityType = todoDoc.getPriorityType();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_details, menu);

        menuPriority = menu.findItem(R.id.menu_priority);

        MenuItem menuItem = menuPriority.getSubMenu().getItem(todoDoc.getPriorityType().getIndex());
        menuItem.setChecked(true);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home: {
                if (txtDetails.getText().toString().trim().length() == 0) {
//
// setResult(RESULT_CANCELED);
                    finish();
                } else {
                    saveDoc();
                }

                return true;
            }

            case R.id.save: {

//
                saveDoc();
// setResult(RESULT_SAVE);
//                finish();
                return true;

            }

            case R.id.delete: {

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.confirm_delete);

                builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteDocument(todoDoc);
//                        setResult(RESULT_DELETE, getIntent());
//                        finish();
                    }

                });

                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

                return true;


            }

            case R.id.menu_priority_high :
            case R.id.menu_priority_low :
            case  R.id.menu_priority_middle : {

                item.setChecked(true);
                priorityType = PriorityType.values()[Integer.valueOf(item.getTitleCondensed().toString())];

                return true;


            }

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteDocument(TodoDoc todoDocument) {
        if (actionType == AppContext.ACTION_UPDATE) {
//            File file = getTodoFile();
//            if(file.delete()){
//            docList.remove(docIndex);
//            }else {
//                Log.e("DetailsActivity","Can't delete file: "+file.getName());
//            }
////            updateIndexes();

            Intent intent = new Intent(AppContext.RECEIVER_DELETE_DOCUMENT);
            intent.putExtra(AppContext.DOC_INDEX, todoDocument.getNumber());
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

        }

        finish();
    }

    private File getTodoFile(){
        String filePath = ((AppContext)getApplicationContext()).getPrefsDir()
                +"/"+todoDoc.getCreateDate().getTime()+".xml";
        return new File(filePath);
    }

    private void saveDoc() {

//        todoDoc.setName(getDocumentName());

        if (actionType == AppContext.ACTION_UPDATE) {

            boolean edited = false;

            SharedPreferences sharedPreferences = getSharedPreferences(String.valueOf(todoDoc.getCreateDate().getTime()), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            // если изменился текст, тогда обновить дату сохранения
            // если документ старый и текст не изменился
            if (!txtDetails.getText().toString().trim().equals(todoDoc.getContent())) {
                todoDoc.setName(getDocumentName());
//                todoDoc.setCreateDate(new Date());
                todoDoc.setContent(txtDetails.getText().toString().trim());

                editor.putString(AppContext.FIELD_CONTENT, todoDoc.getContent());
                edited = true;
            }

            //если приоретет изменился
            if (priorityType != todoDoc.getPriorityType()){
                todoDoc.setPriorityType(priorityType);
//                todoDoc.setCreateDate(new Date());
                editor.putInt(AppContext.FIELD_PRIORITY_TYPE,todoDoc.getPriorityType().getIndex());
                edited = true;
            }

            if (edited){
               String pa = ((AppContext)getApplicationContext()).getPrefsDir();
                File file = new File(pa, todoDoc.getCreateDate().getTime()+".xml");
                todoDoc.setCreateDate(new Date());

                editor.putString(AppContext.FIELD_NAME, todoDoc.getName());
                editor.putLong(AppContext.FIELD_CREATE_DATE, todoDoc.getCreateDate().getTime());

                editor.commit();

                //переменовываем
                file.renameTo(new File(pa, todoDoc.getCreateDate().getTime()+".xml"));

            }



        } else if (actionType == AppContext.ACTION_NEW_TASK) {
            todoDoc.setName(getDocumentName());
            todoDoc.setCreateDate(new Date());
            todoDoc.setPriorityType(priorityType);
            todoDoc.setContent(txtDetails.getText().toString().trim());
            try {
                //создаем файл и редактируем его
                SharedPreferences sharedPreferences = getSharedPreferences(String.valueOf(todoDoc.getCreateDate().getTime()), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(AppContext.FIELD_CONTENT,todoDoc.getContent());
                editor.putString(AppContext.FIELD_NAME,todoDoc.getName());
                editor.putLong(AppContext.FIELD_CREATE_DATE, todoDoc.getCreateDate().getTime());
                editor.putInt(AppContext.FIELD_PRIORITY_TYPE,todoDoc.getPriorityType().getIndex());

                editor.commit();


                docList.add(todoDoc);
            }catch (Exception e){
                Log.e("error",e.getMessage());
            }

        }



//        Collections.sort(docList);

//        updateIndexes();

        finish();

//        StringBuilder sb = new StringBuilder(txtDetails.getText());
//        todoDoc.setContent(sb.toString());
//
//        if (sb.length() > NAME_LENGTH) {
//            sb.delete(NAME_LENGTH, sb.length()).append(".....");
//        }
//
//        //trim - очищает строку от пробелов
//        //split - возвращает массив строк разделенных параметром
//        String tmpName = sb.toString().trim().split("\n")[0];
//        String name = tmpName.length() > 0 ? tmpName : todoDoc.getName();
//
//        todoDoc.setName(name);
//
//        setResult(RESULT_SAVE, getIntent());

    }

    private String getDocumentName() {
        StringBuilder sb = new StringBuilder(txtDetails.getText());

        if (sb.length() > NAME_LENGTH) {
            sb.delete(NAME_LENGTH, sb.length()).append("...");
        }

        String tmpName = sb.toString().trim().split("\n")[0];

        String name = (tmpName.length() > 0) ? tmpName : getResources()
                .getString(R.string.new_doc);

        return name;
    }


}
