package com.pmanager.ui;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.pmanager.R;
import com.pmanager.data.PasswordEntry;
import com.pmanager.security.CryptoManager;
import com.pmanager.util.ClipboardHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView adapter for the password list.
 */
public class PasswordAdapter extends RecyclerView.Adapter<PasswordAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(PasswordEntry entry);
    }

    private List<PasswordEntry> passwords = new ArrayList<>();
    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setPasswords(List<PasswordEntry> passwords) {
        this.passwords = passwords != null ? passwords : new ArrayList<>();
        notifyDataSetChanged();
    }

    public PasswordEntry getItem(int position) {
        return passwords.get(position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_password, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PasswordEntry entry = passwords.get(position);
        Context context = holder.itemView.getContext();

        holder.tvTitle.setText(entry.getTitle());
        holder.tvUsername.setText(entry.getUsername());

        if (entry.getWebsite() != null && !entry.getWebsite().isEmpty()) {
            holder.tvWebsite.setVisibility(View.VISIBLE);
            holder.tvWebsite.setText(entry.getWebsite());
        } else {
            holder.tvWebsite.setVisibility(View.GONE);
        }

        // Category dot color
        int color = getCategoryColor(context, entry.getCategory());
        GradientDrawable dot = new GradientDrawable();
        dot.setShape(GradientDrawable.OVAL);
        dot.setColor(color);
        holder.categoryDot.setBackground(dot);

        // Set initial letter in category dot
        // (we use a colored circle — the dot already serves as the indicator)

        // Copy button
        holder.btnCopy.setOnClickListener(v -> {
            try {
                String decrypted = CryptoManager.getInstance().decrypt(entry.getEncryptedPassword());
                ClipboardHelper.copyToClipboard(context, "Password", decrypted);
            } catch (Exception e) {
                // Handle gracefully
            }
        });

        // Item click
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(entry);
            }
        });
    }

    @Override
    public int getItemCount() {
        return passwords.size();
    }

    private int getCategoryColor(Context context, String category) {
        if (category == null) return ContextCompat.getColor(context, R.color.cat_other);
        switch (category.toLowerCase()) {
            case "social": return ContextCompat.getColor(context, R.color.cat_social);
            case "email": return ContextCompat.getColor(context, R.color.cat_email);
            case "finance": return ContextCompat.getColor(context, R.color.cat_finance);
            case "shopping": return ContextCompat.getColor(context, R.color.cat_shopping);
            case "work": return ContextCompat.getColor(context, R.color.cat_work);
            default: return ContextCompat.getColor(context, R.color.cat_other);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View categoryDot;
        TextView tvTitle, tvUsername, tvWebsite;
        ImageButton btnCopy;

        ViewHolder(View itemView) {
            super(itemView);
            categoryDot = itemView.findViewById(R.id.category_dot);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvUsername = itemView.findViewById(R.id.tv_username);
            tvWebsite = itemView.findViewById(R.id.tv_website);
            btnCopy = itemView.findViewById(R.id.btn_copy);
        }
    }
}
