package com.example.ujicoba;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide; // Pakai Glide untuk gambar
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private Context context;
    private List<Chat> chatList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Chat chat);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public ChatAdapter(Context context, List<Chat> chatList) {
        this.context = context;
        this.chatList = chatList;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_chat, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        Chat chat = chatList.get(position);

        holder.tvName.setText(chat.getName());
        holder.tvLastMessage.setText(chat.getLastMessage());
        holder.tvTimestamp.setText(chat.getTimestamp());

        // Tampilkan gambar profil dari drawable
        Glide.with(context)
                .load(chat.getProfileImageResId())
                .into(holder.ivProfile);

        // Atur notifikasi pesan belum dibaca
        if (chat.getUnreadCount() > 0) {
            holder.tvUnreadCount.setVisibility(View.VISIBLE);
            holder.tvUnreadCount.setText(String.valueOf(chat.getUnreadCount()));
        } else {
            holder.tvUnreadCount.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    class ChatViewHolder extends RecyclerView.ViewHolder {
        CircleImageView ivProfile;
        TextView tvName, tvLastMessage, tvTimestamp, tvUnreadCount;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfile = itemView.findViewById(R.id.profile_image);
            tvName = itemView.findViewById(R.id.chat_name);
            tvLastMessage = itemView.findViewById(R.id.last_message);
            tvTimestamp = itemView.findViewById(R.id.timestamp);
            tvUnreadCount = itemView.findViewById(R.id.unread_count);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(chatList.get(position));
                }
            });
        }
    }
}