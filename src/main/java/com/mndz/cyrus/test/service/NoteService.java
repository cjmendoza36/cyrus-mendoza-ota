package com.mndz.cyrus.test.service;

import com.mndz.cyrus.test.domain.Note;
import com.mndz.cyrus.test.exception.NoteNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class NoteService {
    private final List<Note> noteList = new ArrayList<>();
    private final AtomicLong counter = new AtomicLong();

    public NoteService() {
        noteList.add(new Note(counter.incrementAndGet(), "Note 1", "Body 1"));
        noteList.add(new Note(counter.incrementAndGet(), "Note 2", "Body 2"));
        noteList.add(new Note(counter.incrementAndGet(), "Note 3", "Body 3"));
    }

    public List<Note> getAllNotes() {
        return noteList;
    }

    public Note getNoteById(long id) {
        return noteList.stream()
                .filter(note -> note.getId() == id)
                .findFirst()
                .orElseThrow(() -> new NoteNotFoundException("Note with id " + id + " not found"));
    }

    public Note addNote(Note note) {
        note.setId(counter.incrementAndGet());
        noteList.add(note);
        return note;
    }

    public boolean deleteNoteById(long id) {
        return noteList.removeIf(note -> note.getId() == id);
    }

    public Note updateNote(long id, Note updatedNote) {
        for (int i = 0; i < noteList.size(); i++) {
            Note note = noteList.get(i);
            if (note.getId() == id) {
                updatedNote.setId(id);
                noteList.set(i, updatedNote);
                return updatedNote;
            }
        }
        throw new NoteNotFoundException("Note with id " + id + " not found");
    }
}
