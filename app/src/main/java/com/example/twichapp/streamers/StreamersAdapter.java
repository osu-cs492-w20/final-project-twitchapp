package com.example.twichapp.streamers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.twichapp.R;
import com.example.twichapp.data.TwitchStream;
import com.example.twichapp.utils.TwitchUtils;

import java.util.List;

public class StreamersAdapter extends RecyclerView.Adapter<StreamersAdapter.StreamersViewHolder> {

    private List<TwitchStream> mTwitchStreams;
    private OnStreamerClickListener mStreamerClickListener;

    public interface OnStreamerClickListener {
        void onStreamerClick(TwitchStream twitchStream);
    }

    public StreamersAdapter(OnStreamerClickListener clickListener) {
        mStreamerClickListener = clickListener;
    }

    public void updateTwitchStreams(List<TwitchStream> twitchStreams){
        mTwitchStreams = twitchStreams;
        notifyDataSetChanged();
    }

    @Override
    public StreamersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.twitch_streamer_item, parent, false);
        return new StreamersViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StreamersViewHolder holder, int position) {
        holder.bind(mTwitchStreams.get(position));
    }

    @Override
    public int getItemCount() {
        if (mTwitchStreams != null) {
            return mTwitchStreams.size();
        } else {
            return 0;
        }
    }

    class StreamersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mStreamerThumbIV;
        private TextView mStreamerNameTV;
        private TextView mStreamerTitleTV;
        private TextView mStreamerViewersTV;

        StreamersViewHolder(View itemView) {
            super(itemView);

            mStreamerThumbIV = itemView.findViewById(R.id.iv_streamer_thumbnail);
            mStreamerNameTV = itemView.findViewById(R.id.tv_streamer_name);
            mStreamerTitleTV = itemView.findViewById(R.id.tv_streamer_title);
            mStreamerViewersTV = itemView.findViewById(R.id.tv_streamer_viewers);
            itemView.setOnClickListener(this);
        }


        public void bind(TwitchStream twitchStream) {
            mStreamerNameTV.setText(twitchStream.user_name);
            mStreamerTitleTV.setText(twitchStream.title);

            mStreamerTitleTV.setSelected(false);
            mStreamerTitleTV.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mStreamerTitleTV.setSelected(true);
                }
            }, 3000);
            mStreamerViewersTV.setText(Integer.toString(twitchStream.viewer_count));

            if (twitchStream.thumbnail_url != null) {
                String thumbnail_url = TwitchUtils.buildIconURL(twitchStream.thumbnail_url);
                Glide.with(mStreamerThumbIV.getContext()).load(thumbnail_url).into(mStreamerThumbIV);
            } else {
                mStreamerThumbIV.setImageResource(R.drawable.ic_videocam_off_black_24dp);
            }
        }

        @Override
        public void onClick(View v) {
            TwitchStream twitchStream = mTwitchStreams.get(getAdapterPosition());
            mStreamerClickListener.onStreamerClick(twitchStream);
        }
    }

}
