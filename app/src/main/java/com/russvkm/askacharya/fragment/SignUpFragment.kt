package com.russvkm.askacharya.fragment

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.firebase.auth.FirebaseAuth
import com.russvkm.askacharya.R
import com.russvkm.askacharya.firebase.FirebaseHandler
import com.russvkm.askacharya.utils.BaseClass
import com.russvkm.askacharya.utils.Constants
import de.hdodenhof.circleimageview.CircleImageView

class SignUpFragment : BaseClass(){
    private lateinit var adminName: AppCompatEditText
    private lateinit var adminEmail:AppCompatEditText
    private lateinit var adminPassword:AppCompatEditText
    private lateinit var adminConfirmPassword:AppCompatEditText
    private lateinit var adminImageToUpdate:CircleImageView
    private lateinit var addAdmin:AppCompatButton
    private lateinit var mAuth:FirebaseAuth
    private var imageBitmap: Bitmap?=null
    private lateinit var validateEmailButton:AppCompatButton
    private lateinit var validateEmailCardView:CardView
    private var email:String?=null
    private lateinit var addAdminLayout:ConstraintLayout
    private lateinit var emailTextView: AppCompatTextView
    private lateinit var mainLayout:ConstraintLayout
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root=inflater.inflate(R.layout.fragment_sign_up, container, false)
        findingViewByIds(root)
        validatingViewAndAction()
        mAuth=FirebaseAuth.getInstance()
        adminImageToUpdate.setOnClickListener {
            creatingBottomSheetDialog(Constants.SIGN_UP_REQUEST_CODE_CAMERA,Constants.SIGN_UP_REQUEST_CODE_GALLERY)
        }
        validateEmail()
        hideKeyBoard(mainLayout)
        return root
    }
    private fun findingViewByIds(view:View){
       adminName=view.findViewById(R.id.adminName)
        adminEmail=view.findViewById(R.id.adminEmail)
        adminPassword=view.findViewById(R.id.passwordInputEditText)
        adminConfirmPassword=view.findViewById(R.id.adminConfirmPassword)
        adminImageToUpdate=view.findViewById(R.id.imageToUpdate)
        addAdmin=view.findViewById(R.id.addAdmin)
        validateEmailButton=view.findViewById(R.id.validateEmailButton)
        validateEmailCardView=view.findViewById(R.id.validateEmailCardView)
        addAdminLayout=view.findViewById(R.id.addAdminLayout)
        emailTextView=view.findViewById(R.id.emailTextView)
        mainLayout=view.findViewById(R.id.mainLayout)
    }

    private fun validatingViewAndAction(){
        addAdmin.setOnClickListener {
            val name=adminName.text.toString()
            val password=adminPassword.text.toString()
            email=emailTextView.text.toString()
            val cPassword=adminConfirmPassword.text.toString()
            when{
                name.isEmpty()->{
                    adminName.error=requireContext().getString(R.string.name_blank)
                    adminName.requestFocus()
                }
                password.isEmpty()->{
                    adminPassword.error=requireContext().getString(R.string.enter_pass)
                    adminPassword.requestFocus()
                }
                cPassword.isEmpty()->{
                    adminConfirmPassword.error=requireContext().getString(R.string.pls_confirm_password)
                    adminConfirmPassword.requestFocus()
                }
                password != cPassword->{
                    adminConfirmPassword.error=requireContext().getString(R.string.wrong_pass_confirmation)
                    adminConfirmPassword.requestFocus()
                }
                else->{
                   FirebaseHandler().signUpAccount(this,email!!,password,name)
                }
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==Constants.SIGN_UP_REQUEST_CODE_CAMERA){
            if(data!=null){
                imageBitmap=data.extras?.get("data") as Bitmap
                pushImageToFirebase("admin/${System.currentTimeMillis()}",imageBitmap!!,adminImageToUpdate)
            }
        }
        if(requestCode==Constants.SIGN_UP_REQUEST_CODE_GALLERY){
            if(data!=null){
                val photoUri=data.data
                imageBitmap =
                    MediaStore.Images.Media.getBitmap(requireActivity().contentResolver,photoUri)
                pushImageToFirebase("admin/${System.currentTimeMillis()}",imageBitmap!!,adminImageToUpdate)
            }
        }
    }

    private fun validateEmail(){
        validateEmailButton.setOnClickListener {
            email=adminEmail.text.toString()
            emailTextView.text=email
            when{
                email!!.isEmpty()->{
                    adminEmail.error=requireContext().getString(R.string.email_blank)
                    adminEmail.requestFocus()
                }else->{
                    FirebaseHandler().validateEmail(this,email!!)
                }
            }
        }
    }

    fun validatingLayout(emailExists:Int){
        when(emailExists){
            0->{
              validateEmailCardView.visibility=View.GONE
              addAdminLayout.visibility=View.VISIBLE
            }
            1->{
                FirebaseHandler().fetchAdminEmail(this)
            }
        }
    }
    fun checkForAdmin(arrayList:ArrayList<String>){
        var emailInDB:Int?=1
        for(items in arrayList){
            if(items == email){
                Toast.makeText(context,context?.getText(R.string.this_email_is_already_admin),Toast.LENGTH_SHORT).show()
                emailInDB=null
            }
        }
        val userHashMap=HashMap<String,Any>()
        userHashMap["email"]=email!!
        if(emailInDB!=null){
            FirebaseHandler().addAdmin(this,userHashMap)
        }
    }

    fun creatingAndRegisteringAdmin(name:String){
        val user=mAuth.currentUser
        val hashMap=HashMap<String,Any>()
        hashMap["email"]= user!!.email.toString()
        if(imageBitmap!=null){
            FirebaseHandler().updateUser(this@SignUpFragment,name,Constants.imageUrl)
        }else{
            alertDialogForUpdateUser(name)
        }
        FirebaseHandler().addAdmin(this,hashMap)
    }
}