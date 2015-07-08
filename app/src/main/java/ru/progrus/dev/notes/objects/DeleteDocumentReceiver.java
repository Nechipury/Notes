package ru.progrus.dev.notes.objects;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Evgeniy on 08.07.2015.
 */
public class DeleteDocumentReceiver extends BroadcastReceiver {

    private ArrayList<TodoDoc> listDocuments;
    private Context context;


    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;
        listDocuments = ((AppContext) context).getListDocuments();

        //по умолчанию возвращает -1
        int docIndex = intent.getIntExtra(AppContext.DOC_INDEX, -1);

        if (docIndex >= 0) {// если 1 документ на удаление
            TodoDoc removedDocument = listDocuments.remove(docIndex);
            getCurrentTodoFile(removedDocument).delete();

        } else {// если несколько документов для удаления

            // важно, чтобы индексы были упорядочены по возрастанию
            ArrayList<Integer> indexes = intent.getIntegerArrayListExtra(AppContext.DOC_INDEXES);

            int i = 0;

            for (Integer index : indexes) {

                if (i > 0) {
                    index -= i;
                }

                // обязательно использовать intValue, иначе будет вызываться
                // перегруженный метод remove(Object) и удаления не будет
                TodoDoc removedDocument = listDocuments.remove(index
                        .intValue());

                i++;

                getCurrentTodoFile(removedDocument).delete();

            }


            //вызываем приемник у активити где он зарегин
            Intent intentBroadcast = new Intent(AppContext.RECEIVER_REFRESH_LISTVIEW);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intentBroadcast);
        }

    }

    private File getCurrentTodoFile(TodoDoc todoDocument) {
        String filePath = ((AppContext) context).getPrefsDir() + "/"
                + todoDocument.getCreateDate().getTime() + ".xml";
        return new File(filePath);
    }

}
