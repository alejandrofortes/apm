package com.apm.a2pjb.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pablo on 13/5/17.
 */

public class TeacherDB extends SQLiteOpenHelper{


    private static final String dbName = "a2pjb";
    private static final String tableName = "teachers";
    private Context context;
    private  static final int version = 1;


    public TeacherDB(Context context, String dbName, SQLiteDatabase.CursorFactory factory, int version){
        super(context, dbName, factory, version);
    }

    public TeacherDB(Context context){
        super(context, dbName, null, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS Teachers (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, department TEXT, job TEXT, office TEXT, extension INT(4), email TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Teachers");
        this.onCreate(sqLiteDatabase);
    }

    public List<Teacher> getAllTeachers(){
        List<Teacher> teacherList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        if (db != null){
            Cursor c = db.rawQuery("SELECT * FROM Teachers",null);

            if (c.moveToFirst()){
                do{
                    Teacher teacher = new Teacher();
                    teacher.setId(Long.valueOf(c.getInt(0)));
                    teacher.setName(c.getString(1));
                    teacher.setDepartment(c.getString(2));
                    teacher.setJob(c.getString(3));
                    teacher.setOffice(c.getString(4));
                    teacher.setExtension(c.getInt(5));
                    teacher.setEmail(c.getString(6));
                    teacherList.add(teacher);
                }while (c.moveToNext());
            }
        }
        db.close();
        return teacherList;
    }

    public boolean createAll(List<Teacher> teachers){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            if (db != null) {
                for (Teacher teacher : teachers) {
                    ContentValues values = new ContentValues();
                    values.put("name", teacher.getName());
                    values.put("department", teacher.getDepartment());
                    values.put("job", teacher.getJob());
                    values.put("office", teacher.getOffice());
                    values.put("extension", teacher.getExtension());
                    values.put("email", teacher.getEmail());
                    db.insert(tableName, null, values);
                }
            }
            db.close();
        } catch (SQLiteException e){
            Log.e("DB Error", e.getMessage());
            return false;
        }
        return true;
    }

    public List<String> getTeachers(){
        List<String> teacherList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        if (db != null){
            Cursor c = db.rawQuery("SELECT name FROM Teachers ORDER BY name",null);
            if (c.moveToFirst()){
                do{
                    teacherList.add(c.getString(0));
                }while (c.moveToNext());
            }
        }
        db.close();
        return teacherList;
    }

    public List<String> getRooms(){
        List<String> teacherList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        if (db != null){
            Cursor c = db.rawQuery("SELECT DISTINCT office FROM Teachers ORDER BY office",null);
            if (c.moveToFirst()){
                do{
                    teacherList.add(c.getString(0));
                }while (c.moveToNext());
            }
        }
        db.close();
        return teacherList;
    }

    public void deleteAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        if (db != null){
            db.delete(tableName,null,null);
        }
        db.close();
    }
}
