package com.russvkm.askacharya.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.russvkm.askacharya.R
import com.russvkm.askacharya.models.Notification
import com.russvkm.askacharya.utils.NotificationClass
import kotlinx.android.synthetic.main.notification_adapterlayout.view.*

class NotificationRecyclerAdapter(private val activity:Activity, private val context: Context, private val arrayList:ArrayList<Notification>)
    :RecyclerView.Adapter<NotificationRecyclerAdapter.MyViewHolder>() {
    class MyViewHolder(view: View):RecyclerView.ViewHolder(view) {
        val pictureHolderImageView:AppCompatImageView=view.pictureHolderImageView
        val nameHolderTextView:AppCompatTextView=view.nameHolderTextView
        val radioGroup: RadioGroup =view.radioGroup
        val radioOn:RadioButton=view.radioOn
        val radioOff:RadioButton=view.radioOff
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.notification_adapterlayout,parent,false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model=arrayList[position]
        holder.nameHolderTextView.text=model.name
        holder.pictureHolderImageView.setImageResource(model.image)
        holder.itemView.setOnClickListener {
            if(holder.radioOn.isChecked){
                holder.radioOff.isChecked=true
                holder.radioOn.isChecked=false
                holder.radioGroup.background=ContextCompat.getDrawable(context,R.drawable.radio_group_background)
                when(position){
                    0->{
                        NotificationClass(context,activity).saveAllNotificationPreference(1,"video_pref","video_key")
                    }
                    1->{
                        NotificationClass(context,activity).saveAllNotificationPreference(1,"wisdom_pref","wisdom_key")
                    }
                    2->{
                        NotificationClass(context,activity).saveAllNotificationPreference(1,"question_pref","question_key")
                    }
                }
            }
            else if(holder.radioOff.isChecked){
                holder.radioOff.isChecked=false
                holder.radioOn.isChecked=true
                holder.radioGroup.background=ContextCompat.getDrawable(context,R.drawable.radio_button_checked)
                when(position){
                    0->{
                        NotificationClass(context,activity).saveAllNotificationPreference(2,"video_pref","video_key")
                    }
                    1->{
                        NotificationClass(context,activity).saveAllNotificationPreference(2,"wisdom_pref","wisdom_key")
                    }
                    2->{
                        NotificationClass(context,activity).saveAllNotificationPreference(2,"question_pref","question_key")
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int =arrayList.size
}