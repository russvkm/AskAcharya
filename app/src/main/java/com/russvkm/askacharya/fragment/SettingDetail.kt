package com.russvkm.askacharya.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.russvkm.askacharya.R
import com.russvkm.askacharya.adapter.NotificationRecyclerAdapter
import com.russvkm.askacharya.utils.LanguageClass
import com.russvkm.askacharya.utils.NotificationClass
import com.russvkm.askacharya.utils.NotificationObject

class SettingDetail :Fragment(),View.OnClickListener{
    private lateinit var notificationRecyclerView:RecyclerView
    private lateinit var languageLayout: ConstraintLayout
    private lateinit var hindi:CardView
    private lateinit var english:CardView
    private lateinit var hindiTextView: TextView
    private lateinit var englishTextView: TextView
    private lateinit var recyclerLayout:LinearLayout
    private lateinit var radioOnMain:RadioButton
    private lateinit var radioOffMain:RadioButton
    private lateinit var radioGroupMain:RadioGroup
    private var notificationCode=0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val args= arguments?.let { SettingDetailArgs.fromBundle(it) }
        (activity as AppCompatActivity).supportActionBar?.title=args!!.safeArgs
        val root=inflater.inflate(R.layout.fragment_setting_detail, container, false)
        findingViews(root)
        hidingShowingView(args.safeArgs)
        return root
    }

    private fun findingViews(root:View){
        notificationRecyclerView=root.findViewById(R.id.notificationRecyclerView)
        languageLayout=root.findViewById(R.id.languageLayout)
        hindi=root.findViewById(R.id.hindi)
        english=root.findViewById(R.id.english)
        recyclerLayout=root.findViewById(R.id.recyclerLayout)
        radioGroupMain=root.findViewById(R.id.radioGroupMain)
        radioOffMain=root.findViewById(R.id.radioOffMain)
        radioOnMain=root.findViewById(R.id.radioOnMain)
        hindiTextView=root.findViewById(R.id.hindiTextView)
        englishTextView=root.findViewById(R.id.englishTextView)
        languageIndication()
        radioButtonBackground()
        english.setOnClickListener(this)
        hindi.setOnClickListener(this)
        radioGroupMain.setOnClickListener(this)
        notificationRecyclerView.isEnabled=false
        configuringRecyclerView()
    }

    private fun configuringRecyclerView() {
        notificationRecyclerView.layoutManager = LinearLayoutManager(context)
        notificationRecyclerView.setHasFixedSize(true)
        val adapter = NotificationRecyclerAdapter(requireActivity(),
            requireContext(),
            NotificationObject(requireContext()).totalList()
        )
        notificationRecyclerView.adapter = adapter
    }

    private fun hidingShowingView(value:String){
        when(value){
            context?.getString(R.string.notification_setting)->{
                languageLayout.visibility=View.GONE
                recyclerLayout.visibility=View.VISIBLE
            }
            context?.getString(R.string.language_setting)->{
                languageLayout.visibility=View.VISIBLE
                recyclerLayout.visibility=View.GONE
            }
            context?.getString(R.string.contact_us)->{
                languageLayout.visibility=View.GONE
                recyclerLayout.visibility=View.GONE
            }
        }

    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.english->{
                LanguageClass(requireContext()).english()
                LanguageClass(requireContext()).creatingDialog(requireActivity())
            }
            R.id.hindi->{
                LanguageClass(requireContext()).hindi()
                LanguageClass(requireContext()).creatingDialog(requireActivity())
            }
            R.id.radioGroupMain->{
                if(radioOnMain.isChecked){
                    radioOffMain.isChecked=true
                    radioOnMain.isChecked=false
                    radioGroupMain.background=ContextCompat.getDrawable(requireContext(),R.drawable.radio_group_background)
                    notificationCode=1
                    NotificationClass(requireContext(),requireActivity()).saveAllNotificationPreference(notificationCode,"all_notification","notification")
                    notificationRecyclerView.visibility=View.GONE
                }
                else if(radioOffMain.isChecked){
                    radioOffMain.isChecked=false
                    radioOnMain.isChecked=true
                    radioGroupMain.background=ContextCompat.getDrawable(requireContext(),R.drawable.radio_button_checked)
                    notificationCode=2
                    NotificationClass(requireContext(),requireActivity()).saveAllNotificationPreference(notificationCode,"all_notification","notification")
                    notificationRecyclerView.visibility=View.VISIBLE
                }
            }
        }
    }
    private fun languageIndication(){
        if (LanguageClass(requireContext()).fetchingPreferences()==2){
            hindi.background=ContextCompat.getDrawable(requireContext(),R.drawable.language_selected_background)
            hindiTextView.setTextColor(Color.WHITE)
        }else{
            english.background=ContextCompat.getDrawable(requireContext(),R.drawable.language_selected_background)
            englishTextView.setTextColor(Color.WHITE)
        }
    }

    private fun radioButtonBackground(){
        when(NotificationClass(requireContext(),requireActivity()).fetchAllNotificationPreference("all_notification","notification")){
            1->{
                radioOffMain.isChecked=true
                radioOnMain.isChecked=false
                radioGroupMain.background=ContextCompat.getDrawable(requireContext(),R.drawable.radio_group_background)
                notificationRecyclerView.visibility=View.GONE
            }
            2->{
                radioOnMain.isChecked=true
                radioOffMain.isChecked=false
                notificationRecyclerView.visibility=View.VISIBLE
                radioGroupMain.background=ContextCompat.getDrawable(requireContext(),R.drawable.radio_button_checked)
            }
        }
    }
}