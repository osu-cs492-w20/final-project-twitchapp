package com.example.twichapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.twichapp.data.TwitchGames;
import com.example.twichapp.utils.TwitchUtils;

import java.util.List;

public class GamesAdapter extends RecyclerView.Adapter<GamesAdapter.GamesViewHolder> {

    private List<TwitchGames> mTwitchGames;
    private OnGameClickListener mGameClickListener;

    public interface OnGameClickListener {
        void onGameClick(TwitchGames twitchStream);
    }

    public GamesAdapter(OnGameClickListener clickListener) {
        mGameClickListener = clickListener;
    }

    public void updateTwitchGames(List<TwitchGames> twitchStreams){
        mTwitchGames = twitchStreams;
        notifyDataSetChanged();
    }

    @Override
    public GamesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.twitch_game_item, parent, false);
        return new GamesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull GamesViewHolder holder, int position) {
        holder.bind(mTwitchGames.get(position));
    }

    @Override
    public int getItemCount() {
        if (mTwitchGames != null) {
            return mTwitchGames.size();
        } else {
            return 0;
        }
    }

    class GamesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mGameThumbIV;
        private TextView mGameNameTV;

        GamesViewHolder(View itemView) {
            super(itemView);

            mGameThumbIV = itemView.findViewById(R.id.iv_game_thumbnail);
            mGameNameTV = itemView.findViewById(R.id.tv_game_name);
            itemView.setOnClickListener(this);
        }


        public void bind(TwitchGames twitchGame) {
            mGameNameTV.setText(twitchGame.name);

            String thumbnail_url = TwitchUtils.buildIconURL(twitchGame.box_art_url);
            Glide.with(mGameThumbIV.getContext()).load(thumbnail_url).into(mGameThumbIV);
        }

        @Override
        public void onClick(View v) {
            TwitchGames twitchGame = mTwitchGames.get(getAdapterPosition());
            mGameClickListener.onGameClick(twitchGame);
        }
    }
}
