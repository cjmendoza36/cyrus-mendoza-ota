package com.mndz.cyrus.test.controller;

import com.mndz.cyrus.test.domain.Note;
import com.mndz.cyrus.test.exception.NoteNotFoundException;
import com.mndz.cyrus.test.service.NoteService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(NoteController.class)
class NoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NoteService noteService;

    @Test
    void getAllNotes_ReturnsListOfNotes() throws Exception {
        Note note1 = new Note(1, "Title 1", "Body 1");
        Note note2 = new Note(2, "Title 2", "Body 2");
        when(noteService.getAllNotes()).thenReturn(Arrays.asList(note1, note2));

        mockMvc.perform(get("/notes")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Title 1"))
                .andExpect(jsonPath("$[0].body").value("Body 1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].title").value("Title 2"))
                .andExpect(jsonPath("$[1].body").value("Body 2"));
    }

    @Test
    void getNoteById_ExistingNote_ReturnsNote() throws Exception {
        long id = 1;
        Note note = new Note(id, "Title", "Body");
        when(noteService.getNoteById(id)).thenReturn(note);

        mockMvc.perform(get("/notes/{id}", id)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.title").value("Title"))
                .andExpect(jsonPath("$.body").value("Body"));
    }

    @Test
    void getNoteById_NonExistingNote_Returns404() throws Exception {
        long id = 1;
        when(noteService.getNoteById(id)).thenThrow(new NoteNotFoundException("Note not found"));

        mockMvc.perform(get("/notes/{id}", id)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void addNote_ValidNote_ReturnsCreatedNoteWithURI() throws Exception {
        Note note = new Note();
        note.setTitle("Title");
        note.setBody("Body");

        Note createdNote = new Note(1, "Title", "Body");
        when(noteService.addNote(any(Note.class))).thenReturn(createdNote);

        mockMvc.perform(post("/notes")
                        .contentType(APPLICATION_JSON)
                        .content("{\"title\": \"Title\", \"body\": \"Body\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Title"))
                .andExpect(jsonPath("$.body").value("Body"))
                .andExpect(header().string("Location", "/notes/1"));
    }

    @Test
    public void addNote_InvalidInput_ReturnsBadRequest() throws Exception {
        String invalidNoteJson = "{\"title\": \"\", \"body\": \"\"}";

        when(noteService.addNote(any(Note.class))).thenReturn(new Note());

        mockMvc.perform(post("/notes")
                        .contentType(APPLICATION_JSON)
                        .content(invalidNoteJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateNote_ExistingNote_ReturnsUpdatedNote() throws Exception {
        long id = 1;
        Note note = new Note(id, "Updated Title", "Updated Body");
        when(noteService.updateNote(eq(id), any(Note.class))).thenReturn(note);

        mockMvc.perform(put("/notes/{id}", id)
                        .contentType(APPLICATION_JSON)
                        .content("{\"title\": \"Updated Title\", \"body\": \"Updated Body\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.title").value("Updated Title"))
                .andExpect(jsonPath("$.body").value("Updated Body"));
    }

    @Test
    public void updateNote_InvalidInput_ReturnsBadRequest() throws Exception {
        String invalidNoteJson = "{\"title\": \"\", \"body\": \"\"}";

        mockMvc.perform(put("/notes/1")
                        .contentType(APPLICATION_JSON)
                        .content(invalidNoteJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteNoteById_ExistingNote_ReturnsNoContent() throws Exception {
        long id = 1;
        when(noteService.deleteNoteById(id)).thenReturn(true);

        mockMvc.perform(delete("/notes/{id}", id)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteNoteById_NonExistingNote_ReturnsNotFound() throws Exception {
        long id = 1;
        when(noteService.deleteNoteById(id)).thenReturn(false);

        mockMvc.perform(delete("/notes/{id}", id)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}

