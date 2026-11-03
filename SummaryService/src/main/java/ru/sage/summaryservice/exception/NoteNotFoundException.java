package ru.sage.summaryservice.exception;

public class NoteNotFoundException extends RuntimeException{
    public  NoteNotFoundException(String message) {
        super(message);
    }
}
