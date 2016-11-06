package com.rakshitsaxena.notebook;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;


/**
 * A simple {@link Fragment} subclass.
 */
public class NoteEditFragment extends Fragment {

    private ImageButton noteCatButton;
    private Note.Category savedButtonCategory;
    private AlertDialog categoryDialog;

    public NoteEditFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View framentLayout = inflater.inflate(R.layout.fragment_note_edit, container, false);

        EditText title = (EditText) framentLayout.findViewById(R.id.editNoteTitle);
        EditText message = (EditText) framentLayout.findViewById(R.id.editNoteMessage);
        noteCatButton = (ImageButton) framentLayout.findViewById(R.id.editNoteButton);

        Intent intent = getActivity().getIntent();
        title.setText(intent.getExtras().getString(MainActivity.NOTE_TITLE_EXTRA));
        message.setText(intent.getExtras().getString(MainActivity.NOTE_MESSAGE_EXTRA));

        Note.Category noteCat = (Note.Category) intent.getSerializableExtra(MainActivity.NOTE_CATEGORY_EXTRA);
        noteCatButton.setImageResource(Note.categoryToDrawable(noteCat));

        buildCategoryDialog();

        noteCatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryDialog.show();
            }
        });

        return framentLayout;
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

}
