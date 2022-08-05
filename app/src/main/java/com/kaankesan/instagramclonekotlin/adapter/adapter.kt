package com.kaankesan.instagramclonekotlin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kaankesan.instagramclonekotlin.databinding.RecyclerRowBinding
import com.kaankesan.instagramclonekotlin.model.Posts
import com.squareup.picasso.Picasso

class adapter(private val arrayList : ArrayList<Posts>) : RecyclerView.Adapter<adapter.ViewHolder>() {

     class  ViewHolder( val binding : RecyclerRowBinding) : RecyclerView.ViewHolder(binding.root)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.name.text = arrayList[position].mail
        holder.binding.comment.text = arrayList[position].comment
        val url = arrayList[position].downloadUrl
        Picasso.get().load(url).into(holder.binding.imageView2)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

}