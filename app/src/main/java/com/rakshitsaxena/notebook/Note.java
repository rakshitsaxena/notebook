package com.rakshitsaxena.notebook;

/**
 * Created by Rakshit on 10/24/2016.
 */

public class Note {

    private String title, message;

    private long noteId, dateCreatedMilli;
    private Category category;

    public enum Category {
        PERSONAL,
        TECHNICAL,
        QUOTE,
        FINANCE
    }

    public Note(String title, String message, Category category) {
        this.title = title;
        this.message = message;
        this.noteId = 0;
        this.dateCreatedMilli = 0;
        this.category = category;
    }

    public Note(String title, String message, long noteId, long dateCreatedMilli, Category category) {
        this.title = title;
        this.message = message;
        this.noteId = noteId;
        this.dateCreatedMilli = dateCreatedMilli;
        this.category = category;
    }

    public String getMessage() {
        return message;
    }

    public String getTitle() {
        return title;
    }

    public Category getCategory() {
        return category;
    }

    public long getId() {return noteId;}

    @Override
    public String toString() {
        return "Note{" +
                "title='" + title + '\'' +
                ", message='" + message + '\'' +
                ", noteId=" + noteId +
                ", Date=" + dateCreatedMilli +
                ", category=" + category +
                '}';
    }

    public int getrAsssociatedDrawable(){
        return categoryToDrawable(category);
    }

    public static int categoryToDrawable(Category noteCategory){
        switch (noteCategory){
            case PERSONAL:
                return R.drawable.p;
            case TECHNICAL:
                return R.drawable.t;
            case FINANCE:
                return R.drawable.f;
            case QUOTE:
                return R.drawable.q;
        }

        return R.drawable.p;
    }
}
