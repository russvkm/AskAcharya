package com.russvkm.askacharya.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.russvkm.askacharya.R
import com.russvkm.askacharya.models.Video
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.video_adapter_layout.view.*


class VideoAdapter(private val context: Context, private val videoArrayList: ArrayList<Video>):RecyclerView.Adapter<VideoAdapter.MyViewHolder>() {
    class MyViewHolder(view: View):RecyclerView.ViewHolder(view) {
        val titleTextView: TextView =view.titleTextView
        val videoThumbnail:ImageView=view.videoThumbnail
        val descriptionTextView:TextView=view.descriptionTextView
        val uploadTimeTextView:TextView=view.uploadTimeTextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.video_adapter_layout, parent, false)
        )
    }

    override fun getItemCount(): Int =videoArrayList.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model=videoArrayList[position]
        holder.titleTextView.text=model.title
        Picasso.get()
            .load(model.videoThumbnails)
            .placeholder(R.drawable.youtube)
            .into(holder.videoThumbnail)
        holder.descriptionTextView.text=model.description
        val string=model.publishTime
        val part: List<String> =string.split("T")
        val date=part[0]
        holder.uploadTimeTextView.text="Uploaded At: $date"
        holder.itemView.setOnClickListener {
            val openURL = Intent(Intent.ACTION_VIEW)
            openURL.data = Uri.parse(model.videoUrl)
            startActivity(context,openURL,null)
        }
    }
}