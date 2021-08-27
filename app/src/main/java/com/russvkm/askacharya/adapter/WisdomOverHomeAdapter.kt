package com.russvkm.askacharya.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.iid.FirebaseInstanceId
import com.russvkm.askacharya.R
import com.russvkm.askacharya.firebase.FirebaseHandler
import com.russvkm.askacharya.fragment.HomeFragment
import com.russvkm.askacharya.models.Wisdom
import com.russvkm.askacharya.utils.Constants
import com.russvkm.askacharya.utils.NotificationClass
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.article_over_home.view.*

class WisdomOverHomeAdapter(private val context: Context, private val arrayList: ArrayList<Wisdom>):RecyclerView.Adapter<WisdomOverHomeAdapter.MyViewHolder>() {
    class MyViewHolder(view: View):RecyclerView.ViewHolder(view) {
        val articleImage: ImageView =view.articleImageView
        val articleTitleTextView: TextView =view.articleTitleTextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.article_over_home,parent,false)
        )
    }

    override fun getItemCount(): Int =arrayList.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
       val model=arrayList[position]
        if (model.articleImage.isNotEmpty()){
            Picasso.get()
                .load(model.articleImage)
                .into(holder.articleImage)
        }else{
            holder.articleImage.setImageResource(R.drawable.wisdom_black)
        }

            holder.articleTitleTextView.text=model.articleTitle
    }

}