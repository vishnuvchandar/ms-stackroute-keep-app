package com.stackroute.keepnote.model;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;

@Document
public class Note {

	/*
	 * This class should have eight fields
	 * (noteId,noteTitle,noteContent,noteStatus,createdAt,
	 * category,reminder,createdBy). This class should also contain the getters and
	 * setters for the fields along with the no-arg , parameterized constructor and
	 * toString method. The value of createdAt should not be accepted from the user
	 * but should be always initialized with the system date.
	 * 
	 */
	@Id
	@JsonProperty("id")
	private int noteId;
	@JsonProperty("title")
	private String noteTitle;
	@JsonProperty("text")
	private String noteContent;
	@JsonProperty("state")
	private String noteStatus;
	private Date noteCreationDate;
	private Category category;
	private List<Reminder> reminders;
	private String noteCreatedBy;

	public Note() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Note(int noteId, String noteTitle, String noteContent, String noteStatus, Date noteCreationDate,
			Category category, List<Reminder> reminders, String noteCreatedBy) {
		super();
		this.noteId = noteId;
		this.noteTitle = noteTitle;
		this.noteContent = noteContent;
		this.noteStatus = noteStatus;
		this.noteCreationDate = noteCreationDate;
		this.category = category;
		this.reminders = reminders;
		this.noteCreatedBy = noteCreatedBy;
	}

	public int getNoteId() {
		return noteId;
	}

	public void setNoteId(int noteId) {
		this.noteId = noteId;
	}

	public String getNoteTitle() {
		return noteTitle;
	}

	public void setNoteTitle(String noteTitle) {
		this.noteTitle = noteTitle;
	}

	public String getNoteContent() {
		return noteContent;
	}

	public void setNoteContent(String noteContent) {
		this.noteContent = noteContent;
	}

	public String getNoteStatus() {
		return noteStatus;
	}

	public void setNoteStatus(String noteStatus) {
		this.noteStatus = noteStatus;
	}

	public Date getNoteCreationDate() {
		return noteCreationDate;
	}

	public void setNoteCreationDate(Date noteCreationDate) {
		this.noteCreationDate = noteCreationDate;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public List<Reminder> getReminders() {
		return reminders;
	}

	public void setReminders(List<Reminder> reminders) {
		this.reminders = reminders;
	}

	public String getNoteCreatedBy() {
		return noteCreatedBy;
	}

	public void setNoteCreatedBy(String noteCreatedBy) {
		this.noteCreatedBy = noteCreatedBy;
	}

	@Override
	public String toString() {
		return "Note [noteId=" + noteId + ", noteTitle=" + noteTitle + ", noteContent=" + noteContent + ", noteStatus="
				+ noteStatus + ", noteCreationDate=" + noteCreationDate + ", category=" + category + ", reminders="
				+ reminders + ", noteCreatedBy=" + noteCreatedBy + "]";
	}

}
