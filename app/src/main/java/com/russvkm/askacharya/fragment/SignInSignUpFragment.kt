package com.russvkm.askacharya.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.russvkm.askacharya.R
import com.russvkm.askacharya.firebase.FirebaseHandler
import com.russvkm.askacharya.utils.BaseClass
import com.russvkm.askacharya.utils.Constants

class SignInSignUpFragment : BaseClass() {

    private lateinit var signInLayout:ConstraintLayout
    private lateinit var signUpLayout: ConstraintLayout
    private lateinit var emailName:AppCompatEditText
    private lateinit var name:AppCompatEditText
    private lateinit var passwordInputEditText: AppCompatEditText
    private lateinit var confirmPassword:AppCompatEditText
    private lateinit var signUpButton: AppCompatButton
    private lateinit var mAuth: FirebaseAuth
    private lateinit var email:String
    private lateinit var logInEmail:AppCompatEditText
    private lateinit var logInPassword:AppCompatEditText
    private lateinit var logInButton:AppCompatButton


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root=inflater.inflate(R.layout.fragment_sign_in_sign_up, container, false)
        findingViewsByIds(root)
        mAuth=FirebaseAuth.getInstance()
        arguments?.takeIf { it.containsKey(Constants.ARG_OBJECT) }?.apply {
            when(getInt(Constants.ARG_OBJECT)){
                1->{
                    signUpLayout.visibility=View.VISIBLE
                    signInLayout.visibility=View.GONE
                }
                2->{
                    signUpLayout.visibility=View.GONE
                    signInLayout.visibility=View.VISIBLE
                }
            }
        }
        validatingLayout()
        validatingLogInLayout(root)
        return root
    }

    private fun findingViewsByIds(view:View){
        signInLayout=view.findViewById(R.id.signInLayout)
        signUpLayout=view.findViewById(R.id.signUpLayout)
        emailName=view.findViewById(R.id.emailName)
        name=view.findViewById(R.id.name)
        passwordInputEditText=view.findViewById(R.id.passwordInputEditText)
        confirmPassword=view.findViewById(R.id.confirmPassword)
        signUpButton=view.findViewById(R.id.signUpButton)
        logInEmail=view.findViewById(R.id.loginEmail)
        logInPassword=view.findViewById(R.id.loginPasswordInputEditText)
        logInButton=view.findViewById(R.id.loginButtonUser)
    }

    private fun validatingLayout(){
        signUpButton.setOnClickListener {
            email=emailName.text.toString()
            val password=passwordInputEditText.text.toString()
            val cPassword=confirmPassword.text.toString()
            val userName=name.text.toString()
            when{
                email.isEmpty()->{
                    emailName.error=requireContext().getString(R.string.email_blank)
                    emailName.requestFocus()
                }
                userName.isEmpty()->{
                    name.error=requireContext().getString(R.string.name_blank)
                    name.requestFocus()
                }
                password.isEmpty()->{
                    passwordInputEditText.error=requireContext().getString(R.string.enter_pass)
                    passwordInputEditText.requestFocus()
                }
                cPassword.isEmpty()->{
                    confirmPassword.error=requireContext().getString(R.string.pls_confirm_password)
                    confirmPassword.requestFocus()
                }
                password != cPassword->{
                    confirmPassword.error=requireContext().getString(R.string.wrong_pass_confirmation)
                    confirmPassword.requestFocus()
                }else->{
                    FirebaseHandler().signUpAccount(this,email,password,userName)
                }
            }
        }
    }
    fun signUpAndIntendUser(name:String){
        FirebaseHandler().updateOnlyDisplayName(this,name)
    }

    fun updateAndIntend(){
        if(Constants.askQuestionSwitch){
            view?.findNavController()?.navigate(R.id.action_nav_question_to_questionForm)
        }else{
            view?.findNavController()!!.navigate(R.id.action_nav_question_to_userProfile)
        }
    }

    private fun validatingLogInLayout(view:View){
        logInButton.setOnClickListener {
            val logInEmailString=logInEmail.text.toString()
            val logInPasswordString=logInPassword.text.toString()
            when{
                logInEmailString.isEmpty()->{
                    logInEmail.error=context?.getString(R.string.email_blank)
                    logInEmail.requestFocus()
                }
                logInPasswordString.isEmpty()->{
                    logInPassword.error=context?.getString(R.string.enter_pass)
                    logInPassword.requestFocus()
                }
                else->{
                    FirebaseHandler().signInAccount(this,logInEmailString,logInPasswordString,view)
                }
            }
        }
    }

    fun handelLogIn(){
        if(Constants.askQuestionSwitch){
            view?.findNavController()?.navigate(R.id.action_nav_question_to_questionForm)
        }else{
            view?.findNavController()!!.navigate(R.id.action_nav_question_to_userProfile)
        }
    }
}