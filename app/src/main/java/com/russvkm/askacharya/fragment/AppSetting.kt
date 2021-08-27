package com.russvkm.askacharya.fragment

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.russvkm.askacharya.R
import kotlinx.android.synthetic.main.fragment_app_setting.view.*


class AppSetting : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root=inflater.inflate(R.layout.fragment_app_setting, container, false)
        val arrayList=ArrayList<String>()
        arrayList.add(requireContext().getString(R.string.notification_setting))
        arrayList.add(requireContext().getString(R.string.language_setting))
        arrayList.add(requireContext().getString(R.string.feedback))
        arrayList.add(requireContext().getString(R.string.contact_us))
        val arrayAdapter=ArrayAdapter(requireContext(),R.layout.simple_list_item_1,arrayList)
        root.settingListView.adapter=arrayAdapter

        root.settingListView.setOnItemClickListener { parent, view, position, _ ->
            when(val selectedItem=parent.getItemAtPosition(position)){
                requireContext().getString(R.string.notification_setting)->{
                    view.findNavController().navigate(AppSettingDirections.actionAppSettingToSettingDetail(selectedItem.toString()))
                }
                requireContext().getString(R.string.language_setting)->{
                    view.findNavController().navigate(AppSettingDirections.actionAppSettingToSettingDetail(selectedItem.toString()))
                }
                requireContext().getString(R.string.feedback)->{
                    sendMailIntent()
                }
                requireContext().getString(R.string.contact_us)->{
                    view.findNavController().navigate(AppSettingDirections.actionAppSettingToSettingDetail(selectedItem.toString()))
                }
            }
        }
        return root
    }

    private fun sendMailIntent(){
        val emailIntent = Intent(
            Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "russvkm@gmail.com", null
            )
        ).apply {
            putExtra(Intent.EXTRA_SUBJECT,"Feedback")
        }
        startActivity(Intent.createChooser(emailIntent, "feedback"))
    }
}