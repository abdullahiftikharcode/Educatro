package com.example.educatro.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.educatro.R;

import java.util.List;

public class SkillAdapter extends RecyclerView.Adapter<SkillAdapter.SkillViewHolder> {

    private List<String> skills;

    public SkillAdapter(List<String> skills) {
        this.skills = skills;
    }

    @NonNull
    @Override
    public SkillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_skill, parent, false);
        return new SkillViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SkillViewHolder holder, int position) {
        String skill = skills.get(position);
        holder.skillTextView.setText(skill);
    }

    @Override
    public int getItemCount() {
        return skills.size();
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
        notifyDataSetChanged();
    }

    static class SkillViewHolder extends RecyclerView.ViewHolder {
        TextView skillTextView;

        SkillViewHolder(@NonNull View itemView) {
            super(itemView);
            skillTextView = itemView.findViewById(R.id.skillTextView);
        }
    }
} 