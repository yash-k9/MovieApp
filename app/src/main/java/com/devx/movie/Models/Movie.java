package com.devx.movie.Models;

public class Movie {
    public String id;
    public String name;
    public String date;
    public int duration;
    public String description;
    public String genre;
    public String cast[];
    public String[] tag;
    public String link;
    public float rating;
    public String image;

    public Movie(String id, String name, String date, int duration, String description, String genre,String cast[], String[] tag, String link, float rating, String image) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.duration = duration;
        this.description = description;
        this.genre = genre;
        this.cast = cast;
        this.tag = tag;
        this.link = link;
        this.rating = rating;
        this.image = image;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String[] getCast() {
        return cast;
    }

    public void setCast(String[] cast) {
        this.cast = cast;
    }

    public String[] getTag() {
        return tag;
    }

    public void setTag(String[] tag) {
        this.tag = tag;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}