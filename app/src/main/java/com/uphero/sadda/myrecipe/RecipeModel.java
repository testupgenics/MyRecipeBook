package com.uphero.sadda.myrecipe;

/**
 * Created by Sadda on 1/12/2017.
 */

public class RecipeModel {

    private String title;
    private String ingredients;
    private String url;
    private String poster;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        if (poster.equals("")){
            this.poster = null;
        }
        else {
            this.poster = poster;
        }

    }

}
