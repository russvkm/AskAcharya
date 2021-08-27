package com.russvkm.askacharya.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.russvkm.askacharya.R
import kotlinx.android.synthetic.main.fragment_more.view.*
import java.util.*

class MoreFragment : Fragment(),View.OnClickListener {

    private lateinit var adView: AdView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root=inflater.inflate(R.layout.fragment_more, container, false)
        root.facebook.setOnClickListener(this)
        root.instagram.setOnClickListener(this)
        root.twitter.setOnClickListener(this)
        MobileAds.initialize(context){}
        root.aboutAcharya.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_nav_more_to_aboutAcharya)
        }
        root.adminOnly.setOnClickListener {view->
            view.findNavController().navigate(R.id.action_nav_more_to_adminOnly)
        }
        root.appSetting.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_nav_more_to_appSetting)
        )
        root.appInfo.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_nav_more_to_appInfo)
        }
        adView=root.findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        val testDeviceIds = Arrays.asList("298DE570EF2C49FEF5E1179A6CAA8637")
        val configuration = RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build()
        MobileAds.setRequestConfiguration(configuration)
        adView.loadAd(adRequest)
        return root
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.facebook->{
                val uri= Uri.parse("https://www.facebook.com/profile.php?id=100042125059288")
                startActivity(Intent(Intent.ACTION_VIEW,uri))
            }
            R.id.twitter->{
                Toast.makeText(context,"Yet_to_implement",Toast.LENGTH_SHORT).show()
            }
            R.id.instagram->{
                Toast.makeText(context,"Yet_to_implement",Toast.LENGTH_SHORT).show()
            }
        }
    }

}