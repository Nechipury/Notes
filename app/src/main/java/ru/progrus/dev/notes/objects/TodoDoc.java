package ru.progrus.dev.notes.objects;

import java.io.Serializable;
import java.util.Date;


public class TodoDoc implements Serializable {

    private static final long serialVersionUID = -7367289796391092618L;

    private String name;
    private String content;
    private Date createDate;
    private PriorityType priorityType = PriorityType.LOW;

    public Boolean getIsCheked() {
        return isCheked;
    }

    public void setChecked(Boolean isCheked) {
        this.isCheked = isCheked;
    }

    private Boolean isCheked;

    private int number = -1;


    public TodoDoc() {


    }

    public PriorityType getPriorityType() {
        return priorityType;
    }

    public void setPriorityType(PriorityType priorityType) {
        this.priorityType = priorityType;
    }

    public TodoDoc(String name, String content, Date createDate) {
        super();
        this.name = name;
        this.content = content;
        this.createDate = createDate;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return name;
    }



//    @Override
//    public int compareTo(TodoDoc another) {
//        return another.getCreateDate().compareTo(createDate);
//    }
}
