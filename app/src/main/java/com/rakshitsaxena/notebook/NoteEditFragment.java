package com.rakshitsaxena.notebook;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;


/**
 * A simple {@link Fragment} subclass.
 */
public class NoteEditFragment extends Fragment {

    private static final String MODIFIED_CATEGORY = "Modified Category";
    private boolean newNote = false;
    private ImageButton noteCatButton;
    private Note.Category savedButtonCategory;
    private EditText title, message;
    private long noteId = 0;
    private AlertDialog categoryDialog, confirmDialog;

    public NoteEditFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //to check if NoteEditFrag is used for new note
        Bundle bundle = this.getArguments();
        if(bundle != null){
            newNote = bundle.getBoolean(NoteDetailActivity.NEW_NOTE_EXTRA, false);
        }
        if(savedInstanceState != null){
            savedButtonCategory = (Note.Category)savedInstanceState.get(MODIFIED_CATEGORY);
        }
        // Inflate the layout for this fragment
        View framentLayout = inflater.inflate(R.layout.fragment_note_edit, container, false);

        title = (EditText) framentLayout.findViewById(R.id.editNoteTitle);
        message = (EditText) framentLayout.findViewById(R.id.editNoteMessage);
        noteCatButton = (ImageButton) framentLayout.findViewById(R.id.editNoteButton);
        Button savedButton = (Button) framentLayout.findViewById(R.id.saveNote);

        savedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog.show();
            }
        });

        Intent intent = getActivity().getIntent();
        title.setText(intent.getExtras().getString(MainActivity.NOTE_TITLE_EXTRA , ""));
        message.setText(intent.getExtras().getString(MainActivity.NOTE_MESSAGE_EXTRA, ""));
        noteId = intent.getExtras().getLong(MainActivity.NOTE_ID_EXTRA, 0);


        if(savedButtonCategory != null){
            noteCatButton.setImageResource(Note.categoryToDrawable((savedButtonCategory)));
        } else if(!newNote) {
            Note.Category noteCat = (Note.Category) intent.getSerializableExtra(MainActivity.NOTE_CATEGORY_EXTRA);
            savedButtonCategory = noteCat;
            noteCatButton.setImageResource(Note.categoryToDrawable(noteCat));
        }
        buildCategoryDialog();
        buildConfirmDialog();

        noteCatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryDialog.show();
            }
        });

        return framentLayout;
    }

    @Override
    public void onSaveInstanceState(Bundle saveInstanceState){
        super.onSaveInstanceState(saveInstanceState);
        saveInstanceState.putSerializable(MODIFIED_CATEGORY,savedButtonCategory );
    }

    private void buildCategoryDialog(){
        final String[] categories = new String[]{"Personal", "Technical", "Quote", "Finance"};
        AlertDialog.Builder categoryBuilder = new AlertDialog.Builder(getActivity());
        categoryBuilder.setTitle("Choose Note Type");

        categoryBuilder.setSingleChoiceItems(categories, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                //dismisses dialog window
                categoryDialog.cancel();
                int savedButtonImageRes = R.drawable.p;
                switch (item){
                    case 0:
                        savedButtonCategory = Note.Category.PERSONAL;
                        savedButtonImageRes = R.drawable.p;
                        break;
                    case 1:
                        savedButtonCategory = Note.Category.TECHNICAL;
                        savedButtonImageRes = R.drawable.t;
                        break;
                    case 2:
                        savedButtonCategory = Note.Category.QUOTE;
                        savedButtonImageRes = R.drawable.q;
                        break;
                    case 3:
                        savedButtonCategory = Note.Category.FINANCE;
                        savedButtonImageRes = R.drawable.f;
                        break;
                }

                noteCatButton.setImageResource(savedButtonImageRes);
            }
        });

        categoryDialog = categoryBuilder.create();
    }

    private void buildConfirmDialog(){
        AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(getActivity());

        confirmBuilder.setTitle("Are you sure?");
        confirmBuilder.setMessage("Are you sure, you want to save the note?");

        confirmBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                savedButtonCategory = savedButtonCategory == null? Note.Category.PERSONAL:savedButtonCategory;
                Log.d("Save Note", "Note Title:" + title.getText() +
                        "Note Message:" + message.getText() +
                        "Note Category" + savedButtonCategory
                );

                NotebookDbAdapter notebookDbAdapter = new NotebookDbAdapter(getActivity().getBaseContext());
                notebookDbAdapter.open();
                if(newNote){
                    //if it is a new note then create it our database
                    notebookDbAdapter.createNote(title.getText() + "",
                            message.getText() + "",
                            savedButtonCategory
                            );
                }

                else {
                    //otheriwse it is an old note so update our db
                    Log.d("UPDATE_NOTE", "Updating note");
                    notebookDbAdapter.updateNote(noteId, title.getText() + "",
                            message.getText() + "", savedButtonCategory);
                }
                notebookDbAdapter.close();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });

        confirmBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    //Do nothing here

            }
        });

        confirmDialog = confirmBuilder.create();
    }

}


