package com.example.appfond;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class BlogRecyclerAdapter extends RecyclerView.Adapter<BlogRecyclerAdapter.ViewHolder> {

    private Context context;
    public List<BlogPost> blog_list;

    public BlogRecyclerAdapter(Context context, List<BlogPost> blog_list){
        this.context = context;
        this.blog_list = blog_list;
    }

    //@NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.blog_list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(blog_list.get(position).getImage()).into(holder.imageViewPost);
        String title_data = blog_list.get(position).getTitle();
        String date_data = blog_list.get(position).date_post_txt;
        holder.setTitleText(title_data);
        holder.setDateText(date_data);
    }

    @Override
    public int getItemCount() {
        return blog_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView fieldTitle, fieldDate, valueID;
        ImageView imageViewPost;
        private View mView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mView = itemView;

            imageViewPost = itemView.findViewById(R.id.blog_image);
            fieldDate = itemView.findViewById(R.id.blog_date);
        }

        public void setTitleText(String Titletext){
            fieldTitle = mView.findViewById(R.id.blog_title);
            fieldTitle.setText(Titletext);
        }

        public void setDateText(String Datetext){
            fieldDate = mView.findViewById(R.id.blog_date);
            fieldDate.setText(Datetext);
        }

    }
}
