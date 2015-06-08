package ru.progrus.dev.notes.objects;

import java.io.Serializable;
import java.util.Date;


public class TodoDoc implements Serializable {

    private static final long serialVersionUID = -7367289796391092618L;

    private String name;
    private String content;
    private Date createDate;

    private int number = -1;


    public TodoDoc() {


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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof TodoDoc)) return false;
        TodoDoc todoDoc = (TodoDoc) o;
        return number == todoDoc.getNumber();
    }
}
