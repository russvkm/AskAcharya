package com.russvkm.askacharya.fragment

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.russvkm.askacharya.R
import com.russvkm.askacharya.firebase.FirebaseHandler
import com.russvkm.askacharya.utils.BaseClass
import kotlinx.android.synthetic.main.fragment_admin_only.*
import kotlinx.android.synthetic.main.fragment_admin_only.view.*
import kotlinx.android.synthetic.main.fragment_ask_question.*

class AdminOnly : BaseClass(),View.OnClickListener {
    private lateinit var mAuth:FirebaseAuth
    private var emailInDB:Int?=null
    private lateinit var logInCard: CardView
    private lateinit var pushDataCardView: CardView
    private var logInClicked:Boolean=false
    private var emailVerified:Boolean=false
    private lateinit var email:String
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root=inflater.inflate(R.layout.fragment_admin_only, container, false)
        logInCard=root.findViewById(R.id.logInCard)
        pushDataCardView=root.findViewById(R.id.pushDataCardView)
        mAuth=FirebaseAuth.getInstance()
        if(mAuth.currentUser!=null){
            FirebaseHandler().fetchAdminEmail(this)
        }else{
            logInCard.visibility=View.VISIBLE
            pushDataCardView.visibility=View.GONE
        }
        setHasOptionsMenu(true)
        loginExistingAccount(root)
        root.pushDataOverMain.setOnClickListener (this)
        root.pushDataOverWisdom.setOnClickListener(this)
        root.replyQuestion.setOnClickListener(this)
        hideKeyBoard(root.adminOnlyMain)
        return root
    }


    private fun disablingEnablingView(){
        if (emailInDB!=null){
            logInCard.visibility=View.GONE
            pushDataCardView.visibility=View.VISIBLE
        }else{
            logInCard.visibility=View.VISIBLE
            pushDataCardView.visibility=View.GONE
        }
    }

    private fun loginExistingAccount(view:View){
        view.logInButton.setOnClickListener {
            logInClicked=true
            email=emailEditText.text.toString()
            val password=passwordEditText.text.toString()
            when {
                email.isEmpty() -> {
                    emailEditText.error = context?.getString(R.string.email_blank)
                    emailEditText.requestFocus()
                }
                password.isEmpty() -> {
                    passwordEditText.error = context?.getString(R.string.blank_password)
                }
                password.length < 6 -> {
                    Toast.makeText(
                        context,
                        context?.getString(R.string.pasword_length),
                        Toast.LENGTH_LONG
                    ).show()
                }
                else -> {
                    FirebaseHandler().fetchAdminEmail(this)
                    if(emailVerified)
                        FirebaseHandler().signInAccount(this,email,password,view)
                    else{
                        Toast.makeText(context,context?.getString(R.string.you_are_not_admin),Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }


    override fun onClick(view: View?) {
        when(view?.id){
            R.id.pushDataOverMain->{
                view.findNavController().navigate(R.id.action_adminOnly_to_pushDataOverMain2)
            }
            R.id.pushDataOverWisdom->{
                view.findNavController().navigate(R.id.action_adminOnly_to_pushOverWisdomFragment)
            }
            R.id.replyQuestion->{
                view.findNavController().navigate(R.id.action_adminOnly_to_replyFragment)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.add_admin_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.addAdmin->{
               if(mAuth.currentUser!=null&&emailInDB!=null){
                   view?.findNavController()?.navigate(R.id.action_adminOnly_to_signUpFragment)
                }else{
                   Toast.makeText(context,context?.getString(R.string.only_admin_can),Toast.LENGTH_SHORT).show()
                }
            }
            R.id.logout->{
                if(mAuth.currentUser!=null){
                    showLogoutDialog()
                }else{
                    Toast.makeText(context,context?.getString(R.string.you_are_already),Toast.LENGTH_SHORT).show()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun adminTest(arrayList:ArrayList<String>){
        if(mAuth.currentUser!=null){
            for(items in arrayList){
                if(items == mAuth.currentUser!!.email){
                    emailInDB=1
                }
                disablingEnablingView()
            }
        }
    }
    fun adminTestForLogIn(arrayList: ArrayList<String>){
        if (mAuth.currentUser==null&&logInClicked){
            for (items in arrayList){
                if(items==email){
                   emailVerified=true
                }
            }
        }
    }

    private fun showLogoutDialog(){
        AlertDialog.Builder(context)
            .setTitle(context?.getString(R.string.continue_logout))
            .setMessage(context?.getString(R.string.logout_message))
            .setIcon(R.drawable.logout)
            .setPositiveButton(context?.getString(R.string.yes)){
                    _, _->
                mAuth.signOut()
                logInCard.visibility=View.VISIBLE
                pushDataCardView.visibility=View.GONE
            }
            .setNegativeButton(context?.getString(R.string.cancel)){
                dialog,_->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    fun enablingView(view:View){
        view.logInCard?.visibility= View.GONE
        view.pushDataCardView?.visibility= View.VISIBLE
    }
}