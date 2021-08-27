package com.russvkm.askacharya.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.russvkm.askacharya.R
import com.russvkm.askacharya.adapter.UserQuestionAdapter
import com.russvkm.askacharya.firebase.FirebaseHandler
import com.russvkm.askacharya.models.Question
import com.russvkm.askacharya.utils.BaseClass
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_user_profile.view.*
import kotlinx.android.synthetic.main.top_sheet_dialog.*
import kotlinx.android.synthetic.main.update_dialog.*

class UserProfile : BaseClass() {
    private var mAuth:FirebaseAuth?=null
    private lateinit var questionListView:RecyclerView
    private lateinit var noDataAvailable: TextView
    private lateinit var arrayList:ArrayList<String>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root=inflater.inflate(R.layout.fragment_user_profile, container, false)
        mAuth=FirebaseAuth.getInstance()
        setHasOptionsMenu(true)
        questionListView=root.findViewById(R.id.questionRecyclerView)
        noDataAvailable=root.findViewById(R.id.noDataAvailable)
        FirebaseHandler().fetchingOnlyQuestion(this)
        if(mAuth?.currentUser!=null){
            Picasso.get()
                .load(mAuth!!.currentUser!!.photoUrl)
                .placeholder(R.drawable.person_black)
                .into(root.userCircleImage)
            root.userNameTextView.text=mAuth!!.currentUser!!.displayName
            root.emailTextView.text=mAuth!!.currentUser!!.email
        }
        arrayList= ArrayList()
        arrayList.add(requireContext().getString(R.string.update_profile_image))
        arrayList.add(requireContext().getString(R.string.update_profile_name))
        arrayList.add(requireContext().getString(R.string.change_password))
        arrayList.add(requireContext().getString(R.string.logout))
        root.userCircleImage.setOnClickListener {
            topSheet()
        }
        return root
    }
    fun configuringRecyclerView(arrayList:ArrayList<Question>){
        questionListView.layoutManager=LinearLayoutManager(requireContext())
        questionListView.setHasFixedSize(true)
        val adapter=UserQuestionAdapter(this,requireContext(),arrayList)
        questionListView.adapter=adapter
        if(adapter.itemCount==0){
            questionListView.visibility=View.GONE
            noDataAvailable.visibility=View.VISIBLE
        }else{
            questionListView.visibility=View.VISIBLE
            noDataAvailable.visibility=View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.user_profile_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.logout->{
                showLogoutDialog()
            }
            R.id.updateProfile->{
                //view?.findNavController()?.navigate(R.id.action_userProfile_to_updateProfile)
                updateBottomSheetDialog()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showLogoutDialog(){
        AlertDialog.Builder(context)
            .setTitle(context?.getString(R.string.continue_logout))
            .setMessage(context?.getString(R.string.logout_message))
            .setIcon(R.drawable.logout)
            .setPositiveButton(context?.getString(R.string.yes)){
                    _, _->
                mAuth!!.signOut()
                view?.findNavController()!!.navigate(R.id.action_userProfile_to_nav_question)
            }
            .setNegativeButton(context?.getString(R.string.cancel)){
                    dialog,_->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun updateBottomSheetDialog(){
        val dialog=BottomSheetDialog(requireContext())
        dialog.setContentView(R.layout.update_dialog)
        dialog.setTitle(context?.getString(R.string.choose_an_option))
        val adapter=ArrayAdapter(requireContext(),R.layout.simple_list_item_1,arrayList)
        dialog.updateListView.adapter=adapter
        dialog.updateListView.setOnItemClickListener { _, _, position, _ ->
            when(position){
                3->{
                    mAuth?.signOut()
                    view?.findNavController()?.navigate(R.id.action_userProfile_to_nav_question)
                    dialog.dismiss()
                }else->{
                    val string:String=adapter.getItem(position)!!
                    view?.findNavController()?.navigate(UserProfileDirections.actionUserProfileToUpdateProfile(string))
                    dialog.dismiss()
                }
            }
        }
        dialog.create()
        dialog.show()
    }

    private fun topSheet(){
        if (mAuth!!.currentUser!!.photoUrl!=null){
            val alertDialog=Dialog(requireContext())
            val window:Window=alertDialog.window!!
            window.setGravity(Gravity.TOP)
            alertDialog.setContentView(R.layout.top_sheet_dialog)
            Picasso.get()
                .load(mAuth?.currentUser!!.photoUrl)
                .placeholder(R.drawable.person_black)
                .into(alertDialog.profileImage)
            alertDialog.create()
            alertDialog.show()
        }
        else{
            Toast.makeText(context,context?.getString(R.string.no_data_available_to_show),Toast.LENGTH_SHORT).show()
        }
    }
}