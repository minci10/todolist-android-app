package com.example.todolistapp;

public class NoteModel {

    private int noteId;
    private String noteText;
    private int isFavorite;
    private int isDone;

    public NoteModel(int noteId, String noteText, int isFavorite, int isDone) {
        this.noteId = noteId;
        this.noteText = noteText;
        this.isFavorite = isFavorite;
        this.isDone = isDone;
    }

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public String getNoteText() {
        return noteText;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }

    public int getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(int isFavorite) {
        this.isFavorite = isFavorite;
    }

    public int getIsDone() {
        return isDone;
    }

    public void setIsDone(int isDone) {
        this.isDone = isDone;
    }
}
