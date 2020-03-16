package com.example.twichapp.favorites;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.twichapp.R;
import com.example.twichapp.data.TwitchStream;
import com.example.twichapp.utils.TwitchUtils;

import java.util.List;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder> {

    private List<TwitchStream> mFavorites;
    private OnFavoriteClickListener mFavoriteClickListener;

    public interface OnFavoriteClickListener {
        void onFavoriteClick(TwitchStream twitchStream);
    }

    public FavoritesAdapter(OnFavoriteClickListener clickListener) {
        mFavoriteClickListener = clickListener;
    }

    public void updateFavorites(List<TwitchStream> twitchStreams){
        mFavorites = twitchStreams;
        notifyDataSetChanged();
    }

    @Override
    public FavoritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.favorite_streamer_item, parent, false);
        return new FavoritesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesViewHolder holder, int position) {
        holder.bind(mFavorites.get(position));
    }

    @Override
    public int getItemCount() {
        if (mFavorites != null) {
            return mFavorites.size();
        } else {
            return 0;
        }
    }

    class FavoritesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mFavoriteThumbIV;
        private TextView mFavoriteNameTV;
        private TextView mFavoriteTitleTV;
        private TextView mFavoriteViewersTV;

        FavoritesViewHolder(View itemView) {
            super(itemView);

            mFavoriteThumbIV = itemView.findViewById(R.id.iv_favorite_thumbnail);
            mFavoriteNameTV = itemView.findViewById(R.id.tv_favorite_name);
            mFavoriteTitleTV = itemView.findViewById(R.id.tv_favorite_title);
            mFavoriteViewersTV = itemView.findViewById(R.id.tv_favorite_viewers);
            itemView.setOnClickListener(this);
        }


        public void bind(TwitchStream twitchStream) {
            mFavoriteNameTV.setText(twitchStream.user_name);
            mFavoriteTitleTV.setText(twitchStream.title);

            mFavoriteTitleTV.setSelected(false);
            mFavoriteTitleTV.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mFavoriteTitleTV.setSelected(true);
                }
            }, 3000);
            mFavoriteViewersTV.setText(Integer.toString(twitchStream.viewer_count));

            if (twitchStream.thumbnail_url != null) {
                String thumbnail_url = TwitchUtils.buildIconURL(twitchStream.thumbnail_url);
                Glide.with(mFavoriteThumbIV.getContext()).load(thumbnail_url).into(mFavoriteThumbIV);
            } else {
                mFavoriteThumbIV.setImageResource(R.drawable.ic_videocam_off_black_24dp);
            }
        }

        @Override
        public void onClick(View v) {
            TwitchStream twitchStream = mFavorites.get(getAdapterPosition());
            mFavoriteClickListener.onFavoriteClick(twitchStream);
        }
    }
}
