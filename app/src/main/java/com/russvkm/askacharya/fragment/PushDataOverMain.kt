package com.russvkm.askacharya.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.russvkm.askacharya.R
import com.russvkm.askacharya.firebase.FirebaseHandler
import com.russvkm.askacharya.models.Events
import com.russvkm.askacharya.models.Images
import com.russvkm.askacharya.models.Pandits
import com.russvkm.askacharya.utils.BaseClass
import com.russvkm.askacharya.utils.Constants
import kotlinx.android.synthetic.main.add_pandit_dialog.*
import kotlinx.android.synthetic.main.events_info_layout.*
import kotlinx.android.synthetic.main.fragment_push_data_over_main.view.*

class PushDataOverMain : BaseClass(), View.OnClickListener {
    private var photoUri: Uri?=null
    private var imageBitmapOne:Bitmap?=null
    private var imageBitmapTwo:Bitmap?=null
    private var imageBitmapThree:Bitmap?=null
    private var panditImageBitmap:Bitmap?=null
    private var clickCounter:Int=1
    private lateinit var panditImagePath:String
    private lateinit var slidingImage: ImageView
    private lateinit var finalSubmitButton: Button
    private lateinit var virtualImageView: ImageView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root=inflater.inflate(R.layout.fragment_push_data_over_main, container, false)
        slidingImage=root.findViewById(R.id.slidingImage)
        root.pushPhoto.setOnClickListener(this)
        root.addEvents.setOnClickListener(this)
        root.addPandits.setOnClickListener(this)
        finalSubmitButton=root.findViewById(R.id.finalSubmitButton)
        virtualImageView=root.findViewById(R.id.virtualImageView)
        panditImagePath="Pandits/Pandits${System.currentTimeMillis()}.jpg"

        return root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Constants.DIALOG.dismiss()
        if (resultCode== Activity.RESULT_OK) {
            if (requestCode == Constants.CAMERA_REQUEST_CODE_ONE) {
                if (data != null) {
                    imageBitmapOne = data.extras!!.get("data") as Bitmap
                    FirebaseHandler().addingViewPagerImageToFireStore(this@PushDataOverMain,
                        Constants.pathOne,imageBitmapOne!!,
                        "Image One Added ${context?.getString(R.string.add_two_more)}")
                    clickCounter=2
                }
            }else if(requestCode == Constants.CAMERA_REQUEST_CODE_TWO){
                imageBitmapTwo = data!!.extras!!.get("data") as Bitmap
                FirebaseHandler().addingViewPagerImageToFireStore(this@PushDataOverMain,
                    Constants.pathTwo,imageBitmapTwo!!,
                    "Image Two Added ${context?.getString(R.string.add_one_more)}")
                clickCounter=3
            }else if (requestCode == Constants.CAMERA_REQUEST_CODE_THREE){
                imageBitmapThree = data!!.extras!!.get("data") as Bitmap
                FirebaseHandler().addingViewPagerImageToFireStore(this@PushDataOverMain,
                    Constants.pathThree,imageBitmapThree!!,
                    "Image Two Added ${context?.getString(R.string.click_final_submit)}")
                finalSubmitButton.isEnabled=true
                finalSubmitButton()
                clickCounter=1
            }else if (requestCode==Constants.PANDIT_CAMERA_REQUEST_CODE){
                if (data!=null){
                    panditImageBitmap = data.extras!!.get("data") as Bitmap
                    pushImageToFirebase(panditImagePath, panditImageBitmap!!,virtualImageView)
                }
            }
        }
        if (resultCode==Activity.RESULT_OK){
            Log.i("ResultCode","Success")
            when (requestCode) {
                Constants.GALLERY_REQUEST_CODE_ONE -> {
                    photoUri=data!!.data
                    imageBitmapOne=MediaStore.Images.Media.getBitmap(requireActivity().contentResolver,photoUri)
                    FirebaseHandler().addingViewPagerImageToFireStore(this@PushDataOverMain,
                        Constants.pathOne,imageBitmapOne!!,
                        "Image One Added ${context?.getString(R.string.add_two_more)}")
                    clickCounter=2
                }
                Constants.GALLERY_REQUEST_CODE_TWO -> {
                    photoUri=data!!.data
                    imageBitmapTwo=MediaStore.Images.Media.getBitmap(requireActivity().contentResolver,photoUri)
                    FirebaseHandler().addingViewPagerImageToFireStore(this@PushDataOverMain,
                        Constants.pathTwo,imageBitmapTwo!!,
                        "Image Two Added ${context?.getString(R.string.add_one_more)}")
                    clickCounter=3
                }
                Constants.GALLERY_REQUEST_CODE_THREE -> {
                    photoUri=data!!.data
                    imageBitmapThree=MediaStore.Images.Media.getBitmap(requireActivity().contentResolver,photoUri)
                    FirebaseHandler().addingViewPagerImageToFireStore(this@PushDataOverMain,
                        Constants.pathThree,imageBitmapThree!!,
                        "Image Two Added ${context?.getString(R.string.click_final_submit)}")
                    finalSubmitButton.isEnabled=true
                    finalSubmitButton()
                    clickCounter=1
                }
                Constants.PANDIT_GALLERY_REQUEST_CODE -> {
                    photoUri=data!!.data
                    panditImageBitmap=MediaStore.Images.Media.getBitmap(requireActivity().contentResolver,photoUri)
                    pushImageToFirebase(panditImagePath,panditImageBitmap!!,virtualImageView)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode==Constants.PERMISSION_REQUEST_CODE){
            fetchingPhoto(Constants.GALLERY_REQUEST_CODE_ONE)
        }
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.pushPhoto->{
                when(clickCounter){
                    1->{
                        creatingBottomSheetDialog(Constants.CAMERA_REQUEST_CODE_ONE,Constants.GALLERY_REQUEST_CODE_ONE)
                    }
                    2->{
                        creatingBottomSheetDialog(Constants.CAMERA_REQUEST_CODE_TWO,Constants.GALLERY_REQUEST_CODE_TWO)
                    }
                    3->{
                        creatingBottomSheetDialog(Constants.CAMERA_REQUEST_CODE_THREE,Constants.GALLERY_REQUEST_CODE_THREE)
                    }
                }

            }
            R.id.addEvents->{
                creatingBottomSheetDialogPushingEvent()
            }
            R.id.addPandits->{
                creatingBottomDialogToAddPandits()
            }
        }
    }

    private fun creatingBottomSheetDialogPushingEvent(){
        val dialog=BottomSheetDialog(requireContext())
        dialog.setContentView(R.layout.events_info_layout)
        dialog.pushEventButton.setOnClickListener {
            val title=dialog.titleEditText.text.toString()
            val date=dialog.eventDateEditText.text.toString()
            val place=dialog.placeEditText.text.toString()
            if (date.isNotEmpty()){
                val events=
                    Events(title, date, place)
                FirebaseHandler().pushEvent(this,events)
                dialog.dismiss()
            }else{
                dialog.eventDateEditText.error=context?.getString(R.string.date_cannot_be_empty)
                dialog.eventDateEditText.requestFocus()
            }
        }
        dialog.create()
        dialog.show()
    }

    private fun creatingBottomDialogToAddPandits(){
        val dialog=BottomSheetDialog(requireContext())
        dialog.setContentView(R.layout.add_pandit_dialog)
        dialog.addPanditJeePhoto.setOnClickListener {
            creatingBottomSheetDialog(Constants.PANDIT_CAMERA_REQUEST_CODE,Constants.PANDIT_GALLERY_REQUEST_CODE)
        }
        dialog.addPanditJee.setOnClickListener {
            val name=dialog.panditJeeNameEditText.text.toString()
            val desc=dialog.panditJeeDescriptionEditText.text.toString()

            if (name.isNotEmpty()&&desc.isNotEmpty()){
                val pandits=Pandits(name, Constants.imageUrl,desc)
                FirebaseHandler().addPanditJeeDetail(this@PushDataOverMain,pandits)
                dialog.dismiss()
            }else{
                Toast.makeText(context, context?.getString(R.string.blank_alert),Toast.LENGTH_SHORT).show()
            }
        }
        dialog.create()
        dialog.show()
    }

    private fun finalSubmitButton(){
        finalSubmitButton.setOnClickListener {
            val images= Images(Constants.imageOne,Constants.imageTwo,Constants.imageThree)
            FirebaseHandler().finalSubmitImages(this@PushDataOverMain,images,finalSubmitButton)
        }
    }
}