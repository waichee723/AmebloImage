package com.waichee.amebloimage.ui

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.waichee.amebloimage.ui.main.PhotoAdapter

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, url: String?) {
    url?.let {
        Picasso.get().load(url).into(imgView)
    }
}

@BindingAdapter("listData")
fun bindListData(recyclerView: RecyclerView, data: List<String>?) {
    val adapter = recyclerView.adapter as PhotoAdapter
    adapter.submitList(data)
}