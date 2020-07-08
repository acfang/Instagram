package com.example.instagram.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.instagram.R;
import com.example.instagram.activities.MainActivity;
import com.example.instagram.databinding.ItemPostBinding;

import com.example.instagram.fragments.CommentFragment;
import com.example.instagram.fragments.DetailFragment;
import com.example.instagram.fragments.ProfileFragment;
import com.example.instagram.models.Post;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    public interface OnClickListener {
        void onClickListener(int position);
    }

    private final String TAG = "PostsAdapter";
    private Context context;
    private List<Post> posts;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemPostBinding itemPostBinding = ItemPostBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(itemPostBinding);
    }

    public PostsAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        try {
            holder.bind(post);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        posts.clear();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Post> list) {
        posts.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivProfileImage;
        TextView tvDescription;
        TextView tvUsername;
        ImageView ivImage;
        ImageView ivLike;
        ImageView ivComment;
        ImageView ivShare;
        TextView tvTimestamp;
        TextView tvUsernameDescription;

        public ViewHolder(@NonNull ItemPostBinding itemPostBinding) {

            super(itemPostBinding.getRoot());
            ivProfileImage = itemPostBinding.ivProfileImg;
            tvUsername = itemPostBinding.tvUsername;
            tvDescription = itemPostBinding.tvDescription;
            ivImage = itemPostBinding.ivImg;
            ivLike = itemPostBinding.ivLike;
            ivComment = itemPostBinding.ivComment;
            ivShare = itemPostBinding.ivShare;
            tvTimestamp = itemPostBinding.tvTimeStamp;
            tvUsernameDescription = itemPostBinding.tvUsernameDescription;
        }

        public void bind(final Post post) throws ParseException {
            tvUsername.setText(post.getUser().getUsername());
            tvDescription.setText(post.getDescription());
            tvTimestamp.setText(post.getRelativeTime());
            tvUsernameDescription.setText(post.getUser().getUsername());

            ParseFile image = post.getImg();
            if (image != null) {
                Glide.with(context).load(image.getUrl().replaceAll("http", "https")).into(ivImage);
            }
            ParseFile profileImg = post.getUser().getParseFile("profileImg");
            if (profileImg != null) {
                Glide.with(context).load(profileImg.getUrl().replaceAll("http", "https")).into(ivProfileImage);
            }

            ivImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DetailFragment detailFragment = DetailFragment.newInstance(Parcels.wrap(posts.get(getAdapterPosition())));
                    MainActivity.fragmentManager.beginTransaction().replace(R.id.flContainer, detailFragment).commit();
                }
            });

            ivProfileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        goToProfile(getAdapterPosition());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            });

            tvUsername.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        goToProfile(getAdapterPosition());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            });

            if (post.userLiked(ParseUser.getCurrentUser())) {
                ivLike.setImageResource(R.drawable.ufi_heart_active);
            } else {
                ivLike.setImageResource(R.drawable.ufi_heart);
            }

            ivLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        post.attemptToLike(ParseUser.getCurrentUser());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    notifyItemChanged(getAdapterPosition());
                }
            });

            ivComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    goToComment(getAdapterPosition());
                }
            });
        }

        private void goToProfile(int adapterPosition) throws ParseException {
            ProfileFragment profileFragment = ProfileFragment.newInstance(Parcels.wrap(posts.get(adapterPosition).getUser().fetch()));
            MainActivity.fragmentManager.beginTransaction().replace(R.id.flContainer, profileFragment).commit();
        }

        public void goToComment(int position) {
            CommentFragment commentFragment = CommentFragment.newInstance(Parcels.wrap(posts.get(position)));
            MainActivity.fragmentManager.beginTransaction().replace(R.id.flContainer, commentFragment).commit();
        }
    }

}