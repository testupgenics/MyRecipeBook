package com.uphero.sadda.myrecipe;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.MyViewHolder> {

    private Context referenceOfActivity;
    private List<RecipeModel> recipeModels;

    RecipeListAdapter(Context c, List<RecipeModel> recipeModels) {
        this.recipeModels = recipeModels;
        referenceOfActivity = c;
        setHasStableIds(true);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(referenceOfActivity).inflate(R.layout.row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.Title.setText(recipeModels.get(position).getTitle());
        holder.Ingredients.setText("Ingredients: "+recipeModels.get(position).getIngredients());
        holder.Url.setText(recipeModels.get(position).getUrl());

        if (recipeModels.get(position).getPoster() == null) {
            Picasso.with(referenceOfActivity).load(R.drawable.defualt).into(holder.Poster);
        } else {
            Picasso.with(referenceOfActivity).load(recipeModels.get(position).getPoster()).into(holder.Poster);
        }

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(referenceOfActivity,WebActivity.class);
                i.putExtra("URL",recipeModels.get(position).getUrl());
                referenceOfActivity.startActivity(i);

            }
        });
    }

    @Override
    public int getItemCount() {
        return recipeModels.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView Title, Ingredients, Url;
        ImageView Poster;
        LinearLayout linearLayout;

        MyViewHolder(View itemView) {
            super(itemView);
            Title = itemView.findViewById(R.id.title);
            Ingredients = itemView.findViewById(R.id.ingredients);
            Url = itemView.findViewById(R.id.url);
            Poster = itemView.findViewById(R.id.poster);
            linearLayout = itemView.findViewById(R.id.linearLayout);
        }
    }
}
