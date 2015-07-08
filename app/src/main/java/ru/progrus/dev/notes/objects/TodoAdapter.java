package ru.progrus.dev.notes.objects;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.progrus.dev.notes.R;


/**
 * Created by Evgeniy on 09.06.2015.
 */
public class TodoAdapter  extends ArrayAdapter<TodoDoc>{


//    private List<TodoDoc> list;
private View.OnClickListener checkListener;

    public TodoAdapter(Context context,  ArrayList<TodoDoc> list,View.OnClickListener checkListener) {
        super(context,R.layout.listview_row ,R.id.todo_name,list);
//        this.list = list;
        this.checkListener = checkListener;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.listview_row,parent,false);

            ViewHolder viewHolder = new ViewHolder();

            viewHolder.todoName = (TextView) convertView.findViewById(R.id.todo_name);
            viewHolder.todoDate = (TextView) convertView.findViewById(R.id.todo_date);
            viewHolder.imagePriority = (ImageView) convertView.findViewById(R.id.image_priority);
            viewHolder.checkBox = (CheckBox) convertView
                    .findViewById(R.id.checkBox);

            convertView.setTag(viewHolder);

            viewHolder.checkBox.setOnClickListener(checkListener);
        }

        ViewHolder holder = (ViewHolder) convertView.getTag();

        TodoDoc todoDoc = getItem(position);

        holder.todoName.setText(todoDoc.getName());
        holder.todoDate.setText(DateFormat.format("dd MMMM, yyyy, hh:mm", todoDoc.getCreateDate()));

        switch (todoDoc.getPriorityType()){
            case HIGH:
                holder.imagePriority.setImageResource(R.drawable.ic_priority_high);
                break;
            case MIDDLE:
                holder.imagePriority.setImageResource(R.drawable.ic_priority_middle);
                break;
            case LOW:
                holder.imagePriority.setImageResource(R.drawable.ic_priority_low);
                break;

            default:
                break;
        }

        todoDoc.setChecked(false);

        //передаем чеку ссылку на его документ которому он принадлежит
        holder.checkBox.setTag(todoDoc);

        return convertView;
    }

    static class ViewHolder{
        public TextView todoName;
        public TextView todoDate;
        public ImageView imagePriority;
        public CheckBox checkBox;
    }

}
