package com.example.appfond;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.Holder>{

    int[] images;
    String[] urls;/* = {"https://raw.githubusercontent.com/sayyam/carouselview/master/sample/src/main/res/drawable/image_3.jpg",
            "https://raw.githubusercontent.com/sayyam/carouselview/master/sample/src/main/res/drawable/image_1.jpg",
            "https://raw.githubusercontent.com/sayyam/carouselview/master/sample/src/main/res/drawable/image_2.jpg"};*/

    public SliderAdapter(String[] urls){

        this.urls = urls;

    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.slider_item,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder viewHolder, int position) {

       // viewHolder.imageView.setImageResource(images[position]);
        Picasso.get().load(urls[position]).into(viewHolder.imageView);

    }

    @Override
    public int getCount() {
        return urls.length;
    }

    public class Holder extends  SliderViewAdapter.ViewHolder{

        ImageView imageView;

        public Holder(View itemView){
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view_crop);

        }
    }

}