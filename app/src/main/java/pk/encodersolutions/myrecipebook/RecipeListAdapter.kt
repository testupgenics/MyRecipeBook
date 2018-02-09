package pk.encodersolutions.myrecipebook

import android.content.Context
import android.content.Intent
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.squareup.picasso.Picasso

class RecipeListAdapter internal constructor(private val referenceOfActivity: Context, private val recipeModels: List<RecipeModel>) : RecyclerView.Adapter<RecipeListAdapter.MyViewHolder>() {

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(referenceOfActivity).inflate(R.layout.row, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.Title.text = recipeModels[position].title
        holder.Ingredients.text = "Ingredients: " + recipeModels[position].ingredients
        holder.Url.text = recipeModels[position].url

        if (recipeModels[position].poster == null) {
            Picasso.with(referenceOfActivity).load(R.drawable.defualt).into(holder.Poster)
        } else {
            Picasso.with(referenceOfActivity).load(recipeModels[position].poster).into(holder.Poster)
        }

        holder.rootView.setOnClickListener {
            val i = Intent(referenceOfActivity, WebActivity::class.java)
            i.putExtra("URL", recipeModels[position].url)
            referenceOfActivity.startActivity(i)
        }
    }

    override fun getItemCount(): Int {
        return recipeModels.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var Title: TextView
        var Ingredients: TextView
        var Url: TextView
        var Poster: ImageView
        var rootView: LinearLayout

        init {
            Title = itemView.findViewById(R.id.title)
            Ingredients = itemView.findViewById(R.id.ingredients)
            Url = itemView.findViewById(R.id.url)
            Poster = itemView.findViewById(R.id.poster)
            rootView = itemView.findViewById(R.id.linearLayout)
        }
    }
}
