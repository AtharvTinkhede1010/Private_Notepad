package com.atharv.notepad;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.atharv.notepad.data.NoteContract.NoteEntry;

import java.util.List;


public class NoteCursorAdapter extends CursorAdapter implements Filterable {
    private LayoutInflater cursorInflater;
    List<String> Topics;
    //https://www.youtube.com/watch?v=CTvzoVtKoJ8
    
    public NoteCursorAdapter(Context context, Cursor c,int flags) {
        super(context, c, flags);
        cursorInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return cursorInflater.inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView nameTextView =(TextView) view.findViewById(R.id.miwok_text_view);
        TextView summaryTextView =(TextView) view.findViewById(R.id.default_text_view);


        int nameColumnIndex = cursor.getColumnIndex(NoteEntry.COLUMN_SUB);
        int breedColumnIndex = cursor.getColumnIndex(NoteEntry.COLUMN_DIS);


        String Sub = cursor.getString(nameColumnIndex);
        String Dis = cursor.getString(breedColumnIndex);


        nameTextView.setText(Sub);
        summaryTextView.setText(Dis);
    }

    @Override
    public Filter getFilter() {
        return super.getFilter();
    }
}
