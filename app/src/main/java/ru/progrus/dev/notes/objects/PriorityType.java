package ru.progrus.dev.notes.objects;

/**
 * Created by Evgeniy on 18.06.2015.
 */
public enum PriorityType {

    LOW (0),
    MIDDLE (1),
    HIGH (2);


    private int index;

    PriorityType(int index) {

        this.index= index;
    }

    public int getIndex() {
        return index;
    }
}
