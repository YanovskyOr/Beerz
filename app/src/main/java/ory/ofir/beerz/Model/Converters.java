package ory.ofir.beerz.Model;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Converters {
    @TypeConverter
    public static String commentsToJsonString(ArrayList<String> commentsList){
        Gson gson = new Gson();

        String commentsJsonString = gson.toJson(commentsList);

        return commentsJsonString;
    }

    @TypeConverter
    public static ArrayList<String> CommentsJsonStringToArrayList(String commentsJsonString){

        Gson gson = new Gson();

        Type type = new TypeToken<ArrayList<String>>(){}.getType();
        ArrayList<String> out = gson.fromJson(commentsJsonString, type);
        return out;
    }
}
