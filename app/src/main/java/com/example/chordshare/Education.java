package com.example.chordshare;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class Education {
    String educationName,educationDescription,educationLink;

    public Education() {
    }

    public Education(String educationName, String educationDescription, String educationLink) {
        this.educationName = educationName;
        this.educationDescription = educationDescription;
        this.educationLink = educationLink;
    }

    public String getEducationName() {
        return educationName;
    }

    public void setEducationName(String educationName) {
        this.educationName = educationName;
    }

    public String getEducationDescription() {
        return educationDescription;
    }

    public void setEducationDescription(String educationDescription) {
        this.educationDescription = educationDescription;
    }

    public String getEducationLink() {
        return educationLink;
    }

    public void setEducationLink(String educationLink) {
        this.educationLink = educationLink;
    }

    static public ArrayList<Education> getData(Context context) {
        ArrayList<Education> educationList = new ArrayList<>();

        ArrayList<String> educationNameList = new ArrayList<>();
        ArrayList<String> educationDescriptionList = new ArrayList<>();
        ArrayList<String> educationLinkList = new ArrayList<>();

        try {
            SQLiteDatabase database = context.openOrCreateDatabase("Educations", Context.MODE_PRIVATE, null);

            Cursor cursor = database.rawQuery("SELECT * FROM educations", null);

            int educationNameIndex = cursor.getColumnIndex("educationName");
            int educationDescriptionIndex = cursor.getColumnIndex("educationDescription");
            int educationLinkIndex = cursor.getColumnIndex("educationLink");

            while (cursor.moveToNext()) {
                educationNameList.add(cursor.getString(educationNameIndex));
                educationDescriptionList.add(cursor.getString(educationDescriptionIndex));
                educationLinkList.add(cursor.getString(educationLinkIndex));
            }

            cursor.close();

            for (int i = 0; i < educationNameList.size(); i++) {
                Education education = new Education();
                education.setEducationName(educationNameList.get(i));
                education.setEducationDescription(educationDescriptionList.get(i));
                education.setEducationLink(educationLinkList.get(i));

                educationList.add(education);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return educationList;
    }


}
