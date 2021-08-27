package com.russvkm.askacharya.fragment

import android.content.res.AssetManager
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.russvkm.askacharya.R
import com.russvkm.askacharya.adapter.PanditJeeAdapter
import com.russvkm.askacharya.adapter.ViewPagerFragmentAdapter
import com.russvkm.askacharya.adapter.WisdomOverHomeAdapter
import com.russvkm.askacharya.firebase.FirebaseHandler
import com.russvkm.askacharya.models.Events
import com.russvkm.askacharya.models.Pandits
import com.russvkm.askacharya.utils.BaseClass
import com.russvkm.askacharya.utils.Constants
import com.russvkm.askacharya.utils.NotificationClass


class HomeFragment : BaseClass() {
    private lateinit var viewPagerFragmentAdapter: ViewPagerFragmentAdapter
    private lateinit var viewPager:ViewPager2
    private lateinit var titleTextView:TextView
    private lateinit var panditJeeRecyclerView: RecyclerView
    private lateinit var articleOverHome:RecyclerView
    private lateinit var tabLayout:TabLayout
    private lateinit var viewAll:TextView
    private lateinit var eventTitle:TextView
    private lateinit var eventPlace:TextView
    private lateinit var eventDate:TextView

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root=inflater.inflate(R.layout.fragment_home, container, false)
        viewPager=root.findViewById(R.id.pager)
        titleTextView=root.findViewById(R.id.titleTextView)
        eventTitle=root.findViewById(R.id.eventTitle)
        eventDate=root.findViewById(R.id.eventDate)
        eventPlace=root.findViewById(R.id.eventPlace)
        panditJeeRecyclerView=root.findViewById(R.id.panditJeeRecyclerView)
        articleOverHome=root.findViewById(R.id.articleOverHome)
        viewAll=root.findViewById(R.id.viewAll)
        viewPagerFragmentAdapter=ViewPagerFragmentAdapter(this@HomeFragment)
        tabLayout=root.findViewById(R.id.threeDot)
        FirebaseHandler().downloadPanditJee(this@HomeFragment)
       FirebaseHandler().downloadArticle(this@HomeFragment)
        viewPager.adapter=viewPagerFragmentAdapter
        FirebaseHandler().downloadEvent(this@HomeFragment)
        TabLayoutMediator(tabLayout,viewPager){
                _, _ ->
        }.attach()
        return root
    }


    fun illustrateEvent(eventArrayList:ArrayList<Events>){
        dismissDialog()
        if (eventArrayList.isNotEmpty()){
            eventDate.text=eventArrayList[0].date
            eventTitle.text=eventArrayList[0].title
            eventPlace.text=eventArrayList[0].place
        }
    }

    fun illustratePanditJee(panditJeeArrayList:ArrayList<Pandits>){
        dismissDialog()
        panditJeeRecyclerView.layoutManager=LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        panditJeeRecyclerView.setHasFixedSize(true)
        val adapter=PanditJeeAdapter(requireContext(),panditJeeArrayList)
        panditJeeRecyclerView.adapter=adapter
    }

    fun illustrateArticle(){
        dismissDialog()
        articleOverHome.layoutManager=LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        articleOverHome.setHasFixedSize(true)
        val adapter=WisdomOverHomeAdapter(requireContext(),Constants.ARTICLE_ARRAY_LIST)
        articleOverHome.adapter=adapter
        viewAll.setOnClickListener (
            Navigation.createNavigateOnClickListener(HomeFragmentDirections.actionNavHomeToNavWisdom())
        )
        if(NotificationClass(requireContext(),requireActivity()).
            fetchAllNotificationPreference(Constants.NUMBER_OF_CURRENT_ARTICLE,Constants.PREF_KEY_ARTICLE) != adapter.itemCount&&
            NotificationClass(requireContext(),requireActivity()).fetchAllNotificationPreference("all_notification","notification")!=1&&
            NotificationClass(requireContext(),requireActivity()).fetchAllNotificationPreference("wisdom_pref","wisdom_key")!=1
        ){
            NotificationClass(requireContext(),requireActivity()).configuringNotificationChannel()
            NotificationClass(requireContext(),requireActivity()).displayingNotification(requireContext()
                .getString(R.string.new_article_title),requireContext()
                .getString(R.string.new_article_notification_body),R.drawable.app_icon)
            NotificationClass(requireContext(),requireActivity()).saveAllNotificationPreference(adapter.itemCount,
                Constants.NUMBER_OF_CURRENT_ARTICLE,Constants.PREF_KEY_ARTICLE)
        }
    }
   /** private fun runningImageInLoop(){
        for (i in 0..5000){
            Handler().postDelayed({
                when {
                    i%3==0-> {
                        tabLayout.getTabAt(2)?.select()
                    }
                    i%3==1 -> {
                        tabLayout.getTabAt(0)?.select()
                    }
                    else -> {
                        tabLayout.getTabAt(1)?.select()
                    }
                }
            },2000*i.toLong() )
        }
    }*/
}