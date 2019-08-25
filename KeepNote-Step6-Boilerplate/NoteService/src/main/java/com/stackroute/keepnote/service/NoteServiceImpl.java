package com.stackroute.keepnote.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.stackroute.keepnote.exception.NoteNotFoundExeption;
import com.stackroute.keepnote.model.Note;
import com.stackroute.keepnote.model.NoteUser;
import com.stackroute.keepnote.repository.NoteRepository;

/*
* Service classes are used here to implement additional business logic/validation 
* This class has to be annotated with @Service annotation.
* @Service - It is a specialization of the component annotation. It doesn't currently 
* provide any additional behavior over the @Component annotation, but it's a good idea 
* to use @Service over @Component in service-layer classes because it specifies intent 
* better. Additionally, tool support and additional behavior might rely on it in the 
* future.
* */
@Service
public class NoteServiceImpl implements NoteService {

	/*
	 * Autowiring should be implemented for the NoteRepository and MongoOperation.
	 * (Use Constructor-based autowiring) Please note that we should not create any
	 * object using the new keyword.
	 */
	@Autowired
	private NoteRepository noteRepository;

	/*
	 * This method should be used to save a new note.
	 */
	public Note createNote(Note note) {
		note.setNoteId(new Random().nextInt(10));
		note.setNoteCreationDate(new Date());
		String userId = note.getNoteCreatedBy();
		Optional<NoteUser> dbNoteUser = noteRepository.findById(userId);
		NoteUser savedNoteUser;
		NoteUser noteUser;
		if (dbNoteUser.isPresent()) {
			noteUser = dbNoteUser.get();
			noteUser.getNotes().add(note);
			savedNoteUser = noteRepository.save(noteUser);
		} else {
			noteUser = new NoteUser();
			noteUser.setUserId(userId);
			List<Note> notes = new ArrayList<>();
			note.setNoteCreationDate(new Date());
			notes.add(note);
			noteUser.setNotes(notes);
			savedNoteUser = noteRepository.insert(noteUser);
		}
		Note noteObj = null;
		if (savedNoteUser != null) {
			List<Note> notes = savedNoteUser.getNotes();
			noteObj = notes.stream().filter(eachNote -> eachNote.getNoteId() == note.getNoteId())
					.collect(Collectors.toList()).get(0);
		}
		return noteObj;

	}

	/* This method should be used to delete an existing note. */

	public boolean deleteNote(String userId, int noteId) {
		Optional<NoteUser> dbNoteUser = noteRepository.findById(userId);
		NoteUser noteUser = null;
		if (dbNoteUser.isPresent()) {
			noteUser = dbNoteUser.get();
			List<Note> notes = noteUser.getNotes();
			Iterator<Note> itr = notes.iterator();
			while (itr.hasNext()) {
				Note noteObj = itr.next();
				if (noteObj.getNoteId() == noteId) {
					itr.remove();
				}
			}
			noteRepository.save(noteUser);
			return true;
		} else {
			throw new NullPointerException("Error Deleting Note");
		}
	}

	/* This method should be used to delete all notes with specific userId. */

	public boolean deleteAllNotes(String userId) {
		Optional<NoteUser> dbNoteUser = noteRepository.findById(userId);
		if (dbNoteUser.isPresent()) {
			NoteUser noteUser = dbNoteUser.get();
			if (noteUser.getNotes() == null || noteUser.getNotes().isEmpty()) {
				throw new NoSuchElementException("No note found for id #" + userId);
			}
			noteRepository.deleteById(userId);
			return true;
		} else {
			return false;
		}
	}

	/*
	 * This method should be used to update a existing note.
	 */
	public Note updateNote(Note note, int id, String userId) throws NoteNotFoundExeption {
		Optional<NoteUser> dbNoteUser = null;
		try {
			dbNoteUser = noteRepository.findById(userId);
			NoteUser noteUser = null;
			List<Note> updateNotes = new ArrayList<>();
			if (dbNoteUser.isPresent()) {
				noteUser = dbNoteUser.get();
				List<Note> notes = noteUser.getNotes();
				boolean isExist = false;
				for (Note eachNote : notes) {
					if (eachNote.getNoteId() == id) {
						if(note.getCategory()!=null) {
							eachNote.setCategory(note.getCategory());
						}
						if(!StringUtils.isEmpty(note.getNoteContent())) {
							eachNote.setNoteContent(note.getNoteContent());
						}
						if(!StringUtils.isEmpty(note.getNoteTitle())) {
							eachNote.setNoteTitle(note.getNoteTitle());
						}
						if(!StringUtils.isEmpty(note.getNoteStatus())) {
							eachNote.setNoteStatus(note.getNoteStatus());
						}
						if(note.getReminders() !=null && note.getReminders().isEmpty()) {
							eachNote.setReminders(note.getReminders());
						}
						
						updateNotes.add(eachNote);
						isExist = true;
					} else {
						updateNotes.add(eachNote);
					}
				}
				if (!isExist) {
					throw new NoteNotFoundExeption("Note not found for id #" + id);
				}
				dbNoteUser.get().setNotes(updateNotes);
				noteRepository.save(noteUser);
			} else {
				throw new NoteNotFoundExeption("Note not found for id #" + id);
			}
		} catch (Exception e) {
			throw new NoteNotFoundExeption("Note not found for id #" + id);
		}
		return note;
	}

	/*
	 * This method should be used to get a note by noteId created by specific user
	 */
	public Note getNoteByNoteId(String userId, int noteId) throws NoteNotFoundExeption {

		Optional<NoteUser> dbNoteUser = null;
		Note dbNote = null;
		try {
			dbNoteUser = noteRepository.findById(userId);
			if (dbNoteUser.isPresent()) {
				NoteUser noteUser = dbNoteUser.get();
				List<Note> notes = noteUser.getNotes();
				boolean isExist = false;
				for (Note note : notes) {
					if (note.getNoteId() == noteId) {
						dbNote = note;
						isExist = true;
					}
				}
				if (!isExist || dbNote == null) {
					throw new NoteNotFoundExeption("Note not found for id #" + noteId);
				}
			} else {
				throw new NoteNotFoundExeption("Note not found for id #" + noteId);
			}
		} catch (Exception e) {
			throw new NoteNotFoundExeption("Note not found for id #" + noteId);
		}
		return dbNote;
	}

	/*
	 * This method should be used to get all notes with specific userId.
	 */
	public List<Note> getAllNoteByUserId(String userId) {

		Optional<NoteUser> dbNoteUser = null;
		List<Note> notes = new ArrayList<>();;
		dbNoteUser = noteRepository.findById(userId);
		if (dbNoteUser.isPresent()) {
			NoteUser noteUser = dbNoteUser.get();
			notes = noteUser.getNotes();
		}
		return notes;
	}

}
