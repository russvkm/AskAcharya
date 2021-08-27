package com.russvkm.askacharya.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.russvkm.askacharya.R
import com.russvkm.askacharya.activity.MainActivity
import com.russvkm.askacharya.adapter.WisdomOverWisdomAdapter
import com.russvkm.askacharya.utils.Constants
import com.russvkm.askacharya.utils.NotificationClass


class Wisdom : Fragment() {
    private lateinit var articleRecyclerView:RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root=inflater.inflate(R.layout.fragment_wisdom, container, false)
        articleRecyclerView=root.findViewById(R.id.wisdomRecyclerView)
        configuringRecyclerView()
        return root
    }

    private fun configuringRecyclerView(){
        articleRecyclerView.layoutManager=LinearLayoutManager(context)
        articleRecyclerView.setHasFixedSize(true)
        val adapter=WisdomOverWisdomAdapter(requireContext(),Constants.ARTICLE_ARRAY_LIST)
        articleRecyclerView.adapter=adapter
        if(NotificationClass(requireContext(),requireActivity()).
            fetchAllNotificationPreference(Constants.NUMBER_OF_CURRENT_ARTICLE,Constants.PREF_KEY_ARTICLE)!=adapter.itemCount){
                NotificationClass(requireContext(),requireActivity()).saveAllNotificationPreference(adapter.itemCount,
                Constants.NUMBER_OF_CURRENT_ARTICLE,Constants.PREF_KEY_ARTICLE)
        }
    }
}