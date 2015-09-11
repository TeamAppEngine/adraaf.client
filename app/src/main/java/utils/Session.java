package utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Session {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private static final String SHARED_NAME = "Adroff_Preferences";
    private static final String USER_LEVEL = "user_level";
    private static final String USER_POINT = "user_point";
    private static final String USER_ID = "user_id";
    private static final String USER_EMAIL = "user_email";

    public Session(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    //User level
    public void insertOrUpdateUserLevel(int userLevel){
        editor.putInt(USER_LEVEL, userLevel);
        editor.commit();
    }

    public Integer readUserLevel(){
        return sharedPreferences.getInt(USER_LEVEL,0);
    }

    //User point
    public void insertOrUpdateUserPoint(int userPoint){
        editor.putInt(USER_POINT, userPoint);
        editor.commit();
    }

    public Integer readUserPoint(){
        return sharedPreferences.getInt(USER_POINT, 0);
    }

    //User ID
    public void insertOrUpdateUserID(int userID){
        editor.putInt(USER_ID, userID);
        editor.commit();
    }

    public Integer readUserID(){
        return sharedPreferences.getInt(USER_ID,0);
    }

    //User email
    public void insertOrUpdateUserEmail(String userEmail){
        editor.putString(USER_EMAIL, userEmail);
        editor.commit();
    }

    public String readUserEmail(){
        return sharedPreferences.getString(USER_EMAIL,"");
    }

}