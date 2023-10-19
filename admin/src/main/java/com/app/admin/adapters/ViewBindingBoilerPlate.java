package com.app.admin.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

public class ViewBindingBoilerPlate extends RecyclerView.ViewHolder {

    public ViewBinding binding;

    public ViewBindingBoilerPlate(@NonNull ViewBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
}
