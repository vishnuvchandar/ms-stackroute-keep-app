package com.stackroute.keepnote.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.stackroute.keepnote.exception.NoteNotFoundExeption;
import com.stackroute.keepnote.model.Note;
import com.stackroute.keepnote.service.NoteService;

/*
 * As in this assignment, we are working with creating RESTful web service, hence annotate
 * the class with @RestController annotation.A class annotated with @Controller annotation
 * has handler methods which returns a view. However, if we use @ResponseBody annotation along
 * with @Controller annotation, it will return the data directly in a serialized 
 * format. Starting from Spring 4 and above, we can use @RestController annotation which 
 * is equivalent to using @Controller and @ResposeBody annotation
 */
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS}, allowCredentials = "true")
public class NoteController {

	/*
	 * Autowiring should be implemented for the NoteService. (Use Constructor-based
	 * autowiring) Please note that we should not create any object using the new
	 * keyword
	 */
	@Autowired
	private NoteService noteService;

	public NoteController(NoteService noteService) {
	}

	/*
	 * Define a handler method which will create a specific note by reading the
	 * Serialized object from request body and save the note details in the
	 * database.This handler method should return any one of the status messages
	 * basis on different situations: 1. 201(CREATED) - If the note created
	 * successfully. 2. 409(CONFLICT) - If the noteId conflicts with any existing
	 * user.
	 * 
	 * This handler method should map to the URL "/api/v1/note" using HTTP POST
	 * method
	 */
	@PostMapping(value = "/api/v1/note", produces = "application/JSON")
	public ResponseEntity<Note> save(@RequestBody Note note) {
		Note createdNote = noteService.createNote(note);
		if (createdNote != null) {
			return new ResponseEntity<>(createdNote, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
	}

	/*
	 * Define a handler method which will delete a note from a database. This
	 * handler method should return any one of the status messages basis on
	 * different situations: 1. 200(OK) - If the note deleted successfully from
	 * database. 2. 404(NOT FOUND) - If the note with specified noteId is not found.
	 *
	 * This handler method should map to the URL "/api/v1/note/{id}" using HTTP
	 * Delete method" where "id" should be replaced by a valid noteId without {}
	 */
	@DeleteMapping(value = "/api/v1/note/{userId}/{id}", produces = "application/JSON")
	public ResponseEntity<Map<String, Boolean>> delete(@PathVariable String userId, @PathVariable int id) {
		boolean deleted = false;
		Map<String, Boolean> map = new HashMap<>();
		try {
			deleted = noteService.deleteNote(userId, id);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		if (deleted) {
			map.put("isDeleted", true);
			return new ResponseEntity<>(map, HttpStatus.OK);
		} else {
			map.put("isDeleted", false);
			return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
		}
	}

	/*
	 * DELETE ALL NOtes MAPPING
	 */
	@DeleteMapping(value = "/api/v1/note/{userId}", produces = "application/JSON")
	public ResponseEntity<Map<String, Boolean>> deleteAll(@PathVariable String userId) {
		boolean deleted = false;
		Map<String, Boolean> map = new HashMap<>();
		try {
			deleted = noteService.deleteAllNotes(userId);
		} catch (Exception e) {
			map.put("isDeleted", false);
			return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
		}
		if (deleted) {
			map.put("isDeleted", true);
			return new ResponseEntity<>(map, HttpStatus.OK);
		} else {
			map.put("isDeleted", false);
			return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
		}
	}

	/*
	 * Define a handler method which will update a specific note by reading the
	 * Serialized object from request body and save the updated note details in a
	 * database. This handler method should return any one of the status messages
	 * basis on different situations: 1. 200(OK) - If the note updated successfully.
	 * 2. 404(NOT FOUND) - If the note with specified noteId is not found.
	 * 
	 * This handler method should map to the URL "/api/v1/note/{id}" using HTTP PUT
	 * method.
	 */
	@PutMapping(value = "/api/v1/note/{userId}/{id}", produces = "application/JSON")
	public ResponseEntity<Note> update(@PathVariable String userId, @PathVariable int id, @RequestBody Note note) {
		Note dbNote = null;
		try {
			dbNote = noteService.updateNote(note, id, userId);
		} catch (NoteNotFoundExeption e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		if (dbNote != null) {
			return new ResponseEntity<Note>(dbNote, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	/*
	 * Define a handler method which will get us the all notes by a userId. This
	 * handler method should return any one of the status messages basis on
	 * different situations: 1. 200(OK) - If the note found successfully.
	 * 
	 * This handler method should map to the URL "/api/v1/note" using HTTP GET
	 * method
	 */
	@GetMapping(value = "/api/v1/note/{userId}", produces = "application/JSON")
	public ResponseEntity<List<Note>> getNotesByUserId(@PathVariable String userId) {
		List<Note> notes = noteService.getAllNoteByUserId(userId);
		return new ResponseEntity<List<Note>>(notes, HttpStatus.OK);
	}
	/*
	 * Define a handler method which will show details of a specific note created by
	 * specific user. This handler method should return any one of the status
	 * messages basis on different situations: 1. 200(OK) - If the note found
	 * successfully. 2. 404(NOT FOUND) - If the note with specified noteId is not
	 * found. This handler method should map to the URL
	 * "/api/v1/note/{userId}/{noteId}" using HTTP GET method where "id" should be
	 * replaced by a valid reminderId without {}
	 * 
	 */
	@GetMapping(value="/api/v1/note/{userId}/{id}", produces="application/JSON")
	public ResponseEntity<Note> getNoteByIds(@PathVariable String userId, @PathVariable int id) {
		Note note = null;
		try {
			note = noteService.getNoteByNoteId(userId, id);
		} catch (NoteNotFoundExeption e) {
			return new ResponseEntity<Note>(HttpStatus.NOT_FOUND);
		}
		if(note != null) {
			return new ResponseEntity<Note>(note, HttpStatus.OK);
		} else {
			return new ResponseEntity<Note>(HttpStatus.NOT_FOUND);
		}
	}
}
