package me.azab.oa.galleryexample.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import me.azab.oa.galleryexample.R;
import me.azab.oa.galleryexample.models.Image;

/**
 * Created by omar on 23/03/2017.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>{

    private List<Image> mList;
    private Context mContext;

    public RecyclerViewAdapter(List<Image> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View imageView = inflater.inflate(R.layout.item_main, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(imageView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Image mImage = mList.get(position);
        ImageView mImageView = holder.mImageView;

        Glide.with(mContext)
                .load(mImage.getUrl())
                .placeholder(R.drawable.ic_cloud_off)
                .into(mImageView);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    /**
     * ViewHolder class of the recycer view
     */
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView mImageView;

        public MyViewHolder(View itemView) {

            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.item_image_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                //TODO fire intent to start detail activity
//                SpacePhoto spacePhoto = mSpacePhotos[position];
//                Intent intent = new Intent(mContext, SpacePhotoActivity.class);
//                intent.putExtra(SpacePhotoActivity.EXTRA_SPACE_PHOTO, spacePhoto);
//                startActivity(intent);
            }

        }
    }
}

