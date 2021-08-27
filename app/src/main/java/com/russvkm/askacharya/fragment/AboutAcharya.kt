package com.russvkm.askacharya.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.text.HtmlCompat
import com.russvkm.askacharya.R

class AboutAcharya : Fragment() {
    private lateinit var aboutAcharya:TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root=inflater.inflate(R.layout.fragment_about_acharya, container, false)
        aboutAcharya=root.findViewById(R.id.aboutAcharya)
        aboutAcharya.text=context?.getString(R.string.about_acharya)
        return root
    }
}