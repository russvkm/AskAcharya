package com.russvkm.askacharya.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.russvkm.askacharya.R
import com.russvkm.askacharya.firebase.FirebaseHandler
import com.russvkm.askacharya.utils.BaseClass
import com.russvkm.askacharya.utils.Constants

class ViewPagerCollectionFragment : BaseClass() {
    private lateinit var homeScreenImageView:ImageView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root=inflater.inflate(R.layout.fragment_view_pager_collection, container, false)
        arguments?.takeIf { it.containsKey(Constants.ARG_OBJECT) }?.apply {
            homeScreenImageView= root.findViewById(R.id.textApp)
            when(getInt(Constants.ARG_OBJECT)){
                1->{
                    FirebaseHandler().downloadImages(this@ViewPagerCollectionFragment,homeScreenImageView,1)

                }
                2->{
                    FirebaseHandler().downloadImages(this@ViewPagerCollectionFragment,homeScreenImageView,2)
                }
                3->{
                    FirebaseHandler().downloadImages(this@ViewPagerCollectionFragment,homeScreenImageView,3)
                }
            }
        }
        return root
    }

}