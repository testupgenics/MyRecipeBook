package com.uphero.sadda.myrecipe

/**
 * Created by Sadda on 1/12/2017.
 */

class RecipeModel {

    var title: String? = null
    var ingredients: String? = null
    var url: String? = null
    var poster: String? = null
        set(poster) = if (poster == "") {
            field = null
        } else {
            field = poster
        }

}
