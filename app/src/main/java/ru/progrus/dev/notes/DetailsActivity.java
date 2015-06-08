package ru.progrus.dev.notes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import ru.progrus.dev.notes.objects.TodoDoc;


public class DetailsActivity extends Activity {

    public static final int RESULT_SAVE = 100;
    public static final int RESULT_DELETE = 101;

    private static final int NAME_LENGTH = 20;

    private TodoDoc todoDoc;
    private EditText txtDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        txtDetails = (EditText) findViewById(R.id.txtDetails);
        todoDoc = (TodoDoc) getIntent().getSerializableExtra(ListActivity.TODO_DOCUMENT);
        setTitle(todoDoc.getName());
        txtDetails.setText(todoDoc.getContent());

        getActionBar().setDisplayHomeAsUpEnabled(true);
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
        switch (item.getItemId()) {
            case android.R.id.home: {
                if (txtDetails.getText().toString().trim().length() == 0) {
                    setResult(RESULT_CANCELED);
                } else {
                    saveDoc();
                }
                finish();
                return true;
            }

            case R.id.save: {

//
                saveDoc();
// setResult(RESULT_SAVE);
                finish();
                return true;

            }

            case R.id.delete: {

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.confirm_delete);

                builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setResult(RESULT_DELETE, getIntent());
                        finish();
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

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveDoc() {

        StringBuilder sb = new StringBuilder(txtDetails.getText());
        todoDoc.setContent(sb.toString());

        if (sb.length() > NAME_LENGTH) {
            sb.delete(NAME_LENGTH, sb.length()).append(".....");
        }

        //trim - очищает строку от пробелов
        //split - возвращает массив строк разделенных параметром
        String tmpName = sb.toString().trim().split("\n")[0];
        String name = tmpName.length() > 0 ? tmpName : todoDoc.getName();

        todoDoc.setName(name);

        setResult(RESULT_SAVE, getIntent());

    }
}
