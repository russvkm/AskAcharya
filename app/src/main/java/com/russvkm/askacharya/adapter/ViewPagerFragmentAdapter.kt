package com.russvkm.askacharya.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.russvkm.askacharya.fragment.ViewPagerCollectionFragment
import com.russvkm.askacharya.utils.Constants

class ViewPagerFragmentAdapter(fragment: Fragment):FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int =3

    override fun createFragment(position: Int): Fragment {
        val fragment=ViewPagerCollectionFragment()
        fragment.arguments=Bundle().apply{
            putInt(Constants.ARG_OBJECT,position+1)
        }
        return fragment
    }

}