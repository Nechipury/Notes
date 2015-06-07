package ru.progrus.dev.notes;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import ru.progrus.dev.notes.objects.TodoDoc;


public class DetailsActivity extends Activity {

    public static final int RESULT_SAVE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        TodoDoc todoDoc = (TodoDoc) getIntent().getSerializableExtra(ListActivity.TODO_DOCUMENT);
        setTitle(todoDoc.getName());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()){
            case R.id.back:{
                setResult(RESULT_CANCELED);
                finish();
                return true;
            }

            case R.id.save:{

                setResult(RESULT_SAVE);
                finish();
                return true;

            }

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
