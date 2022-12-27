package com.example.appfond;



import static android.content.Intent.getIntent;

import static java.security.AccessController.getContext;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.util.List;

public class BlogRecyclerAdapter extends RecyclerView.Adapter<BlogRecyclerAdapter.ViewHolder> {

    public String mPostText;
    public String mPostImage;
    public String mPostId;
    public String mPostDate;
    public String mPostDesc;

//    final ViewPost mAdapter = new ViewPost( ViewPost.this, null, mPostText);
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
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Glide.with(context).load(blog_list.get(position).getImage()).into(holder.imageViewPost);
        String title_data = blog_list.get(position).getTitle();
        String date_data = blog_list.get(position).date_post_txt;
        String idPost = blog_list.get(position).getId();
        String textPost = blog_list.get(position).getText();

        holder.setTitleText(title_data);
        holder.setDateText(date_data);

        holder.imageViewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),ViewPost.class);
                // String url = response.get(position).getUrl();
                intent.putExtra("textPost", textPost.toString());
                intent.putExtra("imagePost", blog_list.get(position).getImage().toString());
                intent.putExtra("postId", idPost.toString());
                intent.putExtra("postDate", date_data.toString());
                intent.putExtra("postDesc", title_data.toString());

                // System.out.println("textPost= " + mPostText.toString());
                //System.out.println("imagePost= " + mPostImage.toString());
                //System.out.println("postId= " + mPostId.toString());
                v.getContext().startActivity(intent);
            }
        });

        mPostText = blog_list.get(position).getText();
        mPostImage = blog_list.get(position).getImage();
        mPostId = blog_list.get(position).getId();
        System.out.println("mPostID=" + mPostId.toString());
        mPostDate = blog_list.get(position).getDate_post_txt();
        mPostDesc = blog_list.get(position).getTitle();
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

            /*imageViewPost.setOnClickListener(v -> {
                //Intent showPost = new Intent(itemView.getContext(), ViewPost.class);
                //startActivity(showPost);
                Intent intent = new Intent(v.getContext(),ViewPost.class);
               // String url = response.get(position).getUrl();
                intent.putExtra("textPost", mPostText.toString());
                intent.putExtra("imagePost", mPostImage.toString());
                intent.putExtra("postId", mPostId.toString());
                intent.putExtra("postDate", mPostDate.toString());
                intent.putExtra("postDesc", mPostDesc.toString());

               // System.out.println("textPost= " + mPostText.toString());
                System.out.println("imagePost= " + mPostImage.toString());
                System.out.println("postId= " + mPostId.toString());
                v.getContext().startActivity(intent);
            });*/
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
