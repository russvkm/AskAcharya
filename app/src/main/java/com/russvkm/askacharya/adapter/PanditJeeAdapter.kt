package com.russvkm.askacharya.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.russvkm.askacharya.R
import com.russvkm.askacharya.fragment.HomeFragmentDirections
import com.russvkm.askacharya.models.Pandits
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.pandit_jee_adapter_layout.view.*

class PanditJeeAdapter(
    private val context: Context,
    private val panditArrayList: ArrayList<Pandits>
) :
    RecyclerView.Adapter<PanditJeeAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val image: CircleImageView = view.panditJeeImage
        val panditJeeName: TextView = view.panditJeeName
        val panditJeeDescription: TextView = view.panditJeeDescription
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.pandit_jee_adapter_layout, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return panditArrayList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = panditArrayList[position]
        Picasso.get()
            .load(model.image)
            .placeholder(R.drawable.person_black)
            .into(holder.image)
        holder.panditJeeDescription.text = model.specialization
        holder.panditJeeName.text = model.name

        val panditJeeDetailArgs = Pandits(model.name, model.image, model.specialization)

        holder.itemView.setOnClickListener(
            Navigation.createNavigateOnClickListener(
                HomeFragmentDirections.actionNavHomeToPanditJeeDetail(panditJeeDetailArgs)
            )
        )
    }
}