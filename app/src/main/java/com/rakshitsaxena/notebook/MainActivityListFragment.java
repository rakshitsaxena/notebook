package com.rakshitsaxena.notebook;


import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainActivityListFragment extends ListFragment {

    public static final String NOTE_ID_EXTRA = "com.rakshitsaxena.notebook.Note Identifier";
    public static final String NOTE_TITLE_EXTRA = "com.rakshitsaxena.notebook.Note Title";
    public static final String NOTE_MESSAGE_EXTRA = "com.rakshitsaxena.notebook.Note Message";
    public static final String NOTE_CATEGORY_EXTRA = "com.rakshitsaxena.notebook.Note Category";

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        List<Note> notes = new ArrayList<>();
        notes.add(new Note("First Note", "Hello, How are you?", Note.Category.PERSONAL));
        notes.add(new Note("Second Note", "Hello, I am good", Note.Category.TECHNICAL));
        notes.add(new Note("Third Note", "Stock quote of the day", Note.Category.QUOTE));
        notes.add(new Note("Fourtth Note", "Buy Tata Motors", Note.Category.FINANCE));

        NoteAdapter noteAdapter = new NoteAdapter(getActivity(), notes);

        setListAdapter(noteAdapter);

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id){
        super.onListItemClick(l,v,position,id);

        launchNoteDetailActivity(position);

    }

    private void launchNoteDetailActivity(int position){
        Note note = (Note)getListAdapter().getItem(position);

        Intent intent = new Intent(getActivity(), NoteDetailActivity.class);

        intent.putExtra(NOTE_TITLE_EXTRA, note.getTitle());
        intent.putExtra(NOTE_ID_EXTRA, note.getId());
        intent.putExtra(NOTE_MESSAGE_EXTRA, note.getMessage());
        intent.putExtra(NOTE_CATEGORY_EXTRA, note.getCategory());

        startActivity(intent);
    }

}
