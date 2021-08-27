package com.russvkm.askacharya.fragment

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.russvkm.askacharya.R
import com.russvkm.askacharya.firebase.FirebaseHandler
import com.russvkm.askacharya.utils.BaseClass
import com.russvkm.askacharya.utils.Constants
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_push_over_wisdom.*

class UpdateProfile : BaseClass() {
    private lateinit var updateProfileImage:ConstraintLayout
    private lateinit var updateProfileName:ConstraintLayout
    private lateinit var changePassword:ConstraintLayout
    private lateinit var mAuth:FirebaseAuth
    private lateinit var updateProfileNameEditText: AppCompatEditText
    private lateinit var updateProfileNameButton: AppCompatButton
    private lateinit var imageToUpdate:CircleImageView
    private lateinit var updatePictureButton:AppCompatButton
    private var imageBitmap: Bitmap?=null
    private lateinit var path:String
    private lateinit var oldPasswordEditText: AppCompatEditText
    private lateinit var newPasswordEditText: AppCompatEditText
    private lateinit var changePasswordButton: AppCompatButton
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root=inflater.inflate(R.layout.fragment_update_profile, container, false)
        val args=arguments?.let { UpdateProfileArgs.fromBundle(it) }
        mAuth=FirebaseAuth.getInstance()
        Constants.imageUrl=""
        (activity as AppCompatActivity).supportActionBar?.title=args!!.listValue
        findingViewsByIds(root)
        enablingDisablingLayout(args.listValue)
        updateProfileNameEditTextOperation()
        validatingProfileNameEditText()
        imageToUpdateHandler()
        updateProfileImage()
        validatingReAuthenticatingForm()
        return root
    }


    private fun findingViewsByIds(view: View){
        updateProfileImage=view.findViewById(R.id.updateProfileImage)
        updateProfileName=view.findViewById(R.id.updateProfileName)
        changePassword=view.findViewById(R.id.changePassword)
        updateProfileNameEditText=view.findViewById(R.id.UpdateProfileNameEditText)
        updateProfileNameButton=view.findViewById(R.id.updateNameButton)
        imageToUpdate=view.findViewById(R.id.imageToUpdate)
        updatePictureButton=view.findViewById(R.id.updatePictureButton)
        oldPasswordEditText=view.findViewById(R.id.oldPasswordEditText)
        newPasswordEditText=view.findViewById(R.id.passwordEditText)
        changePasswordButton=view.findViewById(R.id.changePasswordButton)
    }

    private fun enablingDisablingLayout(value:String){
        when(value){
            context?.getString(R.string.update_profile_image)->{
                updateProfileImage.visibility=View.VISIBLE
                updateProfileName.visibility=View.GONE
                changePassword.visibility=View.GONE
            }
            context?.getString(R.string.update_profile_name)->{
                updateProfileImage.visibility=View.GONE
                updateProfileName.visibility=View.VISIBLE
                changePassword.visibility=View.GONE
            }
            context?.getString(R.string.change_password)->{
                updateProfileImage.visibility=View.GONE
                updateProfileName.visibility=View.GONE
                changePassword.visibility=View.VISIBLE
            }
        }
    }

    private fun updateProfileNameEditTextOperation(){
        if(mAuth.currentUser!=null){
            updateProfileNameEditText.setText(mAuth.currentUser!!.displayName)
        }
    }

    private fun validatingProfileNameEditText(){
        updateProfileNameButton.setOnClickListener {
            val name=updateProfileNameEditText.text.toString()
            when {
                name.isEmpty() -> {
                    Toast.makeText(context,context?.getString(R.string.name_blank),Toast.LENGTH_SHORT).show()
                }
                name == mAuth.currentUser!!.displayName -> {
                    Toast.makeText(context,context?.getString(R.string.diff_name),Toast.LENGTH_SHORT).show()
                }
                else -> {
                    FirebaseHandler().updateOnlyDisplayName(this,name)
                }
            }
        }
    }

    fun updateAction(){
        view?.findNavController()?.navigate(R.id.action_updateProfile_to_userProfile)
    }

    private fun imageToUpdateHandler(){
        imageToUpdate.setOnClickListener {
            path="UserImage/UserImage${System.currentTimeMillis()}"
            creatingBottomSheetDialog(Constants.UPDATE_USER_IMAGE_REQUEST_CODE_CAMERA,Constants.UPDATE_USER_IMAGE_REQUEST_CODE_GALLERY)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==Constants.UPDATE_USER_IMAGE_REQUEST_CODE_CAMERA){
            if(data!=null){
                imageBitmap = data.extras!!.get("data") as Bitmap
                pushImageToFirebase(path, imageBitmap!!,imageToUpdate)
            }
        }
        if(requestCode==Constants.UPDATE_USER_IMAGE_REQUEST_CODE_GALLERY){
            if (data!=null){
                val photoUri= data.data
                imageBitmap= MediaStore.Images.Media.getBitmap(requireActivity().contentResolver,photoUri)
                pushImageToFirebase(path, imageBitmap!!,imageToUpdate)
            }
        }
    }

    private fun updateProfileImage(){
        updatePictureButton.setOnClickListener {
            if(Constants.imageUrl.isEmpty()){
                Toast.makeText(context,context?.getString(R.string.select_image),Toast.LENGTH_SHORT).show()
            }else{
                FirebaseHandler().updateProfileImage(this,Constants.imageUrl)
            }
        }
    }

    fun handleAction(){
        view?.findNavController()?.navigate(R.id.action_updateProfile_to_userProfile)
    }

    private fun validatingReAuthenticatingForm(){
        changePasswordButton.setOnClickListener {
            val oldPassword=oldPasswordEditText.text.toString()
            val newPassword=newPasswordEditText.text.toString()
            when{
                oldPassword.isEmpty()->{
                    oldPasswordEditText.error=context?.getString(R.string.enter_old_password)
                    oldPasswordEditText.requestFocus()
                }
                newPassword.isEmpty()->{
                    newPasswordEditText.error=context?.getString(R.string.enter_new_password)
                    newPasswordEditText.requestFocus()
                }
                else->{
                    val user = mAuth.currentUser
                    FirebaseHandler().reAuthenticate(this@UpdateProfile,user?.email!!,oldPassword,newPassword)
                }
            }
        }
    }

    fun changePassword(newPassword:String){
        val user=mAuth.currentUser
        user?.updatePassword(newPassword)
            ?.addOnCompleteListener {
                view?.findNavController()?.navigate(R.id.action_updateProfile_to_userProfile)
            }
            ?.addOnFailureListener {
                exception->
                Toast.makeText(context,exception.message,Toast.LENGTH_SHORT).show()
            }
    }
}