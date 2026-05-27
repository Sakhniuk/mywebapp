package com.example.mywebapp.controller;

import com.example.mywebapp.model.Note;
import com.example.mywebapp.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
public class NotesController {

    @Autowired private NoteService noteService;

    @GetMapping("/")
    public ResponseEntity<String> index() {
        String html = "<html><body>" +
                "<h1>MyWebApp Endpoints</h1>" +
                "<ul>" +
                "<li>GET /notes</li>" +
                "<li>POST /notes</li>" +
                "<li>GET /notes/{id}</li>" +
                "</ul>" +
                "</body></html>";
        return ResponseEntity.ok().header("Content-Type", "text/html").body(html);
    }

    @GetMapping(value = "/notes", produces = {"text/html", "application/json"})
    public ResponseEntity<?> getAllNotes(HttpServletRequest request) {
        List<Note> notes = noteService.getAllNotes();
        String accept = request.getHeader("Accept");

        if (accept != null && accept.contains("application/json")) {
            List<Map<String, Object>> response = new ArrayList<>();
            for (Note note : notes) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", note.getId());
                map.put("title", note.getTitle());
                response.add(map);
            }
            return ResponseEntity.ok(response);
        } else {
            StringBuilder html = new StringBuilder("<html><body><h1>Notes</h1><table border='1'>");
            html.append("<tr><th>ID</th><th>Title</th></table>");
            for (Note note : notes) {
                html.append("<tr>")
                    .append("<td>").append(note.getId()).append("</td>")
                    .append("<td>").append(note.getTitle()).append("</td>")
                    .append("</tr>");
            }
            html.append("</table></body></html>");
            return ResponseEntity.ok().header("Content-Type", "text/html").body(html.toString());
        }
    }

    @PostMapping(value = "/notes", consumes = "application/json")
    public ResponseEntity<?> createNote(@RequestBody Map<String, String> payload) {
        String title = payload.get("title");
        String content = payload.get("content");
        
        if (title == null || content == null) {
            return ResponseEntity.badRequest().body("title and content are required");
        }
        
        Note note = noteService.createNote(title, content);
        Map<String, Object> response = new HashMap<>();
        response.put("id", note.getId());
        response.put("title", note.getTitle());
        response.put("content", note.getContent());
        response.put("created_at", note.getCreatedAt());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(value = "/notes/{id}", produces = {"text/html", "application/json"})
    public ResponseEntity<?> getNoteById(@PathVariable Long id, HttpServletRequest request) {
        Note note = noteService.getNoteById(id);
        if (note == null) {
            return ResponseEntity.notFound().build();
        }

        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            Map<String, Object> response = new HashMap<>();
            response.put("id", note.getId());
            response.put("title", note.getTitle());
            response.put("content", note.getContent());
            response.put("created_at", note.getCreatedAt());
            return ResponseEntity.ok(response);
        } else {
            String html = "<html><body>" +
                    "<h1>Note</h1>" +
                    "<p><strong>ID:</strong> " + note.getId() + "</p>" +
                    "<p><strong>Title:</strong> " + note.getTitle() + "</p>" +
                    "<p><strong>Content:</strong> " + note.getContent() + "</p>" +
                    "<p><strong>Created at:</strong> " + note.getCreatedAt() + "</p>" +
                    "</body></html>";
            return ResponseEntity.ok().header("Content-Type", "text/html").body(html);
        }
    }
}
