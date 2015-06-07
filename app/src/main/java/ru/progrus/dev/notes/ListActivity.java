package ru.progrus.dev.notes;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ru.progrus.dev.notes.objects.TodoDoc;


public class ListActivity extends Activity {

    public static String TODO_DOCUMENT = "ru.progrus.dev.notes.TodoDocument";
    public static int TODO_DETAILS_REQUEST =1;

    private ListView listTasks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        listTasks = (ListView) findViewById(R.id.listTasks);

        fillTodoList();



    }

    private void fillTodoList() {

        TodoDoc d1 = new TodoDoc("s1","c1",null);
        TodoDoc d2 = new TodoDoc("s2","c2",null);
        TodoDoc d3 = new TodoDoc("s3","c3",null);

        List<TodoDoc> docList = new ArrayList<>();
        docList.add(d1);
        docList.add(d2);
        docList.add(d3);

        ArrayAdapter<TodoDoc> arrayAdapter = new ArrayAdapter<>(this,R.layout.listview_row,docList);
        listTasks.setAdapter(arrayAdapter);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()){
            case R.id.add_task:{
                TodoDoc todoDoc = new TodoDoc();
                todoDoc.setName(getResources().getString(R.string.create));
                showDoc(todoDoc);
                return true;
            }

            default:
                break;
        }



        return super.onOptionsItemSelected(item);
    }

    private void showDoc(TodoDoc todoDoc) {

        Intent intentDetails = new Intent(this,DetailsActivity.class);
        intentDetails.putExtra(TODO_DOCUMENT, todoDoc);
        startActivityForResult(intentDetails, TODO_DETAILS_REQUEST);







    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TODO_DETAILS_REQUEST) {

            switch (resultCode){
                case RESULT_CANCELED:
                    Toast.makeText(this,"Canceled",Toast.LENGTH_SHORT).show();
                    break;
                case DetailsActivity.RESULT_SAVE:
                    Toast.makeText(this,"Saved",Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }

        }
    }
}
