package com.russvkm.askacharya.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.russvkm.askacharya.fragment.SignInSignUpFragment
import com.russvkm.askacharya.utils.Constants

class SignInSignUpAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int =2

    override fun createFragment(position: Int): Fragment {
        val fragment= SignInSignUpFragment()
        fragment.arguments= Bundle().apply{
            putInt(Constants.ARG_OBJECT,position+1)
        }
        return fragment
    }

}