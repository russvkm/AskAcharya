package com.russvkm.askacharya.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.russvkm.askacharya.R
import com.russvkm.askacharya.fragment.WisdomDirections
import com.russvkm.askacharya.models.Wisdom
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.article_over_wisdom_layout.view.*
import kotlinx.android.synthetic.main.fragment_home.view.*

class WisdomOverWisdomAdapter(private val context: Context,private val arrayList:ArrayList<Wisdom>) :
    RecyclerView.Adapter<WisdomOverWisdomAdapter.MyViewHolder>(){
    class MyViewHolder(view: View):RecyclerView.ViewHolder(view){
        val circularImage:CircleImageView=view.articleCircleImage
        val articleDescription:TextView=view.articleDescription
        val titleTextView:TextView=view.articleTitle
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.article_over_wisdom_layout,parent,false)
        )
    }

    override fun getItemCount(): Int =arrayList.size


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model=arrayList[position]
        holder.articleDescription.text=model.article
        holder.titleTextView.text=model.articleTitle
        if (model.articleImage.isNotEmpty()){
            Picasso.get()
                .load(
                    model.articleImage
                )
                .into(holder.circularImage)
        }else{
            holder.circularImage.setImageResource(R.drawable.person_black)
        }
        val currentArticle=Wisdom(model.articleTitle,model.articleImage,model.article)
        holder.itemView.setOnClickListener (
            Navigation.createNavigateOnClickListener(WisdomDirections.actionNavWisdomToArticleDetail(currentArticle))
        )
    }
}