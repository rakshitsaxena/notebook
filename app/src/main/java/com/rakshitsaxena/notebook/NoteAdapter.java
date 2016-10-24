package com.rakshitsaxena.notebook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

/*
 * Created by Rakshit on 10/24/2016
*/

public class NoteAdapter extends ArrayAdapter<Note>{

    public NoteAdapter(Context context, List<Note> notes){
        super(context, 0, notes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        //Get the data item for this position
        Note note  = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_row, parent, false);
        }

        return new View(getContext());
    }


}
