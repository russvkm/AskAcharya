package com.russvkm.askacharya.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.iid.FirebaseInstanceId
import com.russvkm.askacharya.R
import com.russvkm.askacharya.firebase.FirebaseHandler
import com.russvkm.askacharya.models.Wisdom
import com.russvkm.askacharya.utils.BaseClass
import com.russvkm.askacharya.utils.Constants
import kotlinx.android.synthetic.main.fragment_push_over_wisdom.*
import kotlinx.android.synthetic.main.fragment_push_over_wisdom.view.*


class PushOverWisdomFragment : BaseClass(),View.OnClickListener {
    private lateinit var path:String
    private var imageBitmap:Bitmap?=null
    private var fcm:String=""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root=inflater.inflate(R.layout.fragment_push_over_wisdom, container, false)
        path="Article/Article${System.currentTimeMillis()}.jpg"
        root.wisdomImage.setOnClickListener (this)
        root.addArticleButton.setOnClickListener(this)
        return root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode== Activity.RESULT_OK){
            if (requestCode==Constants.ARTICLE_CAMERA_REQUEST_CODE){
                if (data != null) {
                    imageBitmap = data.extras!!.get("data") as Bitmap
                    pushImageToFirebase(path, imageBitmap!!,wisdomImage)
                }
            }
            else if(requestCode==Constants.ARTICLE_GALLERY_REQUEST_CODE){
                if (data!=null){
                    val photoUri= data.data
                    imageBitmap= MediaStore.Images.Media.getBitmap(requireActivity().contentResolver,photoUri)
                    pushImageToFirebase(path,imageBitmap!!,wisdomImage)
                }
            }
        }
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.wisdomImage->{
                creatingBottomSheetDialog(Constants.ARTICLE_CAMERA_REQUEST_CODE,Constants.ARTICLE_GALLERY_REQUEST_CODE)
            }
            R.id.addArticleButton->{
                val title=wisdomTitleEditText.text.toString()
                val article=wisdomArticleEditText.text.toString()
                if(title.isNotEmpty()&&article.isNotEmpty()){
                    val wisdom=Wisdom(title, Constants.imageUrl,article)
                    FirebaseHandler().addArticle(this,wisdom)
                }else{
                    Toast.makeText(context,context?.getString(R.string.wisdom_blank_alert),Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}