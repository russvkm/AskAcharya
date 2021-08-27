package com.russvkm.askacharya.utils

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.provider.MediaStore
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.russvkm.askacharya.R
import com.russvkm.askacharya.firebase.FirebaseHandler
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.dialog_res_layout.*
import kotlinx.android.synthetic.main.select_photo_bottom_dialog.*
import java.io.ByteArrayOutputStream

open class BaseClass:Fragment() {
    private var dialog:Dialog?=null
    fun showDialog(string: String){
        dialog=Dialog(requireContext())
        dialog!!.setContentView(R.layout.dialog_res_layout)
        dialog!!.dialogTitleTextView.text=string
        dialog!!.setCancelable(false)
        dialog!!.show()
    }

    fun dismissDialog(){
        dialog!!.dismiss()
    }

    private fun meshingWithPermission(requestCode: Int){
        when {
            (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
                    == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
                    == PackageManager.PERMISSION_GRANTED)->{

                fetchingPhoto(requestCode)
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )->{
                alertDialog()
            }
            else->{
                requestPermission()
            }
        }
    }

    private fun requestPermission(){
        requestPermissions(
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ), Constants.PERMISSION_REQUEST_CODE
        )
    }

    fun fetchingPhoto(requestCode: Int){
        val intent= Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, requestCode)
    }

    private fun intendingToFetchPhoto(requestCode: Int){
        val intent=Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, requestCode)
    }

    fun creatingBottomSheetDialog(requestCode: Int, requestCode1: Int){
        Constants.DIALOG= BottomSheetDialog(requireContext())
        Constants.DIALOG.setContentView(R.layout.select_photo_bottom_dialog)
        Constants.DIALOG.cameraAccess.setOnClickListener {
            intendingToFetchPhoto(requestCode)
        }
        Constants.DIALOG.galleryAccess.setOnClickListener {
            meshingWithPermission(requestCode1)
            Constants.DIALOG.dismiss()
        }
        Constants.DIALOG.show()
    }

    private fun alertDialog(){
        AlertDialog.Builder(context)
            .setTitle(context?.getString(R.string.dialog_title))
            .setMessage(context?.getString(R.string.dialog_message))
            .setPositiveButton(context?.getString(R.string.enable_permission)){ _, _ ->
                requestPermission()
            }
            .setNegativeButton(context?.getString(R.string.cancel)){ dialog, _ ->
                dialog.dismiss()
            }
            .setCancelable(false)
            .create()
            .show()
    }

    /**fun getFileExtension(uri: Uri?):String?{
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(activity?.contentResolver?.getType(uri!!))
    }*/

    fun pushImageToFirebase(path: String, bitmap: Bitmap, imageView: ImageView?){
        val storageRef: StorageReference = FirebaseStorage.getInstance().reference.child(path)
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, baos)
        val data = baos.toByteArray()
        showDialog(requireContext().getString(R.string.pls_wait))
        val uploadTask = storageRef.putBytes(data)
        uploadTask.addOnFailureListener { exception->
            Toast.makeText(context, exception.message, Toast.LENGTH_SHORT).show()
            dismissDialog()
        }.addOnSuccessListener { taskSnapshot->
            dismissDialog()
            Toast.makeText(context, "successfully uploaded", Toast.LENGTH_SHORT).show()
            showDialog(requireContext().getString(R.string.pls_wait))
            taskSnapshot.metadata!!.reference!!.downloadUrl
                .addOnSuccessListener { uri->
                    dismissDialog()
                    Constants.imageUrl=uri.toString()
                    Picasso.get().load(Constants.imageUrl)
                        .placeholder(R.drawable.add_photo)
                        .into(imageView)
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(context, exception.message, Toast.LENGTH_SHORT).show()
                    dismissDialog()
                }
        }
    }
    fun alertDialogForUpdateUser(name: String){
        AlertDialog.Builder(context)
            .setTitle(context?.getString(R.string.alert))
            .setMessage(context?.getString(R.string.you_have_not_added))
            .setPositiveButton(context?.getText(R.string.yes)) { _, _ ->
                FirebaseHandler().updateOnlyDisplayName(this, name)
            }
            .setNegativeButton(context?.getString(R.string.cancel)){ dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    fun hideKeyBoard(view: View){
        view.setOnClickListener {
            try {
                val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            } catch (ignored: Exception) {
            }
        }
    }

    fun isNetworkAvailable():Boolean{

        val connectivityManager=context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            val network=connectivityManager.activeNetwork?:return false
            val activeNetwork=connectivityManager.getNetworkCapabilities(network)?: return false

            return when{
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ->true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)->true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)->true
                else-> false
            }


        }else{
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnectedOrConnecting
        }
    }
}