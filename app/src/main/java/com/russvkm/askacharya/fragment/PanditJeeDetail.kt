package com.russvkm.askacharya.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.russvkm.askacharya.R
import com.russvkm.askacharya.activity.MainActivity
import com.russvkm.askacharya.models.Pandits
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_pandit_jee_detail.*
import kotlinx.android.synthetic.main.fragment_pandit_jee_detail.view.*


class PanditJeeDetail : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val args=PanditJeeDetailArgs.fromBundle(requireArguments())
        val actionBar=(activity as MainActivity).supportActionBar
        actionBar!!.setDisplayShowTitleEnabled(true)
        val dataKey=args.panditJeeDetailArgs
        actionBar.title = dataKey.name
        val root=inflater.inflate(R.layout.fragment_pandit_jee_detail, container, false)
        illustratingViews(dataKey,root)
        return root
    }

    @SuppressLint("SetTextI18n")
    private fun illustratingViews(pandit:Pandits, view:View){
        Picasso.get()
            .load(pandit.image)
            .placeholder(R.drawable.person_black)
            .into(view.panditJeeImage)
        view.panditJeeName.text="${pandit.name}\n${pandit.specialization} "
    }
}
