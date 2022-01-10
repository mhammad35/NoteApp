package com.example.imhacnotes;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = Note.class,version = 1)
public abstract class NoteDatabase extends RoomDatabase {

    private static NoteDatabase instance;

    public  abstract NoteDao noteDao();

    public static  synchronized NoteDatabase getInstance(Context context)
    {
        if(instance == null)
        {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    NoteDatabase.class,"note_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();

        }
        return instance;
         }

         private  static  RoomDatabase.Callback roomCallback = new Callback() {
             @Override
             public void onCreate(@NonNull SupportSQLiteDatabase db) {

                 super.onCreate(db);
                 new PopulateDbAsyncTask(instance).execute();
             }
         };

        private  static class PopulateDbAsyncTask extends AsyncTask<Void,Void,Void>
        {
            private  NoteDao noteDao;
            private PopulateDbAsyncTask(NoteDatabase database){
                noteDao = database.noteDao();
            }
            @Override
            protected Void doInBackground(Void... voids) {
            noteDao.Insert(new Note("Title 1" , "Description 1"));
                noteDao.Insert(new Note("Title 2" , "Description 2"));

                return null;
            }
        }



}
