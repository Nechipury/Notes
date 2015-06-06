package ru.progrus.dev.notes;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

        ArrayAdapter<TodoDoc> arrayAdapter = new ArrayAdapter<TodoDoc>(this,R.layout.listview_row,docList);
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
