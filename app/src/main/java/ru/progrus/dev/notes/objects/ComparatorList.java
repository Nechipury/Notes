package ru.progrus.dev.notes.objects;

import java.util.Comparator;

/**
 * Created by Evgeniy on 18.06.2015.
 */
public class ComparatorList {

    private static DateComparator dateComparator;
    private static NameComparator nameComparator;
    private static PriorityComparator priorityComparator;

    public static DateComparator getDateComparator() {
        if (dateComparator == null) {
            dateComparator =new DateComparator();
        }
        return dateComparator;
    }

    public static NameComparator getNameComparator() {
        if (nameComparator == null) {
            nameComparator=new NameComparator();
        }
        return nameComparator;
    }

    private static class NameComparator implements Comparator<TodoDoc>{

        @Override
        public int compare(TodoDoc lhs, TodoDoc rhs) {
            return lhs.getName().compareTo(rhs.getName());
        }
    }

    private static class DateComparator implements Comparator<TodoDoc>{


        @Override
        public int compare(TodoDoc lhs, TodoDoc rhs) {
            return lhs.getCreateDate().compareTo(rhs.getCreateDate());
        }
    }

    public static PriorityComparator getPriorityComparator() {
        if (priorityComparator == null){
            priorityComparator = new PriorityComparator();
        }
        return priorityComparator;
    }

    private static class PriorityComparator implements Comparator<TodoDoc>{

        @Override
        public int compare(TodoDoc lhs, TodoDoc rhs) {
            //приоритет сверху вниз
            int result = rhs.getPriorityType().compareTo(lhs.getPriorityType());
            if (result==0){
                result = rhs.getCreateDate().compareTo(lhs.getCreateDate());
            }

            return result;
        }
    }

}
