package ory.ofir.beerz.Model;


import android.support.annotation.NonNull;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Entity;

@Entity
public class Beer {
    @PrimaryKey
    @NonNull
    public String id;
    public String name;
    public String picture;
    public float rating;
    public String description;

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
