package com.russvkm.askacharya.firebase
import android.graphics.Bitmap
import android.net.Uri
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.russvkm.askacharya.R
import com.russvkm.askacharya.fragment.*
import com.russvkm.askacharya.models.*
import com.russvkm.askacharya.models.Wisdom
import com.russvkm.askacharya.utils.Constants
import com.russvkm.askacharya.utils.NotificationClass
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream

class FirebaseHandler {
    private val fireStore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val mAuth:FirebaseAuth=FirebaseAuth.getInstance()


    fun pushEvent(fragment: PushDataOverMain, events: Events) {
        fragment.showDialog(fragment.getString(R.string.pls_wait))
        fireStore.collection(Constants.EVENT_DATA)
            .add(events)
            .addOnCompleteListener {
                fragment.dismissDialog()
                Toast.makeText(
                    fragment.context,
                    fragment.getString(R.string.event_added_successfully),
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener { exception ->
                fragment.dismissDialog()
                Toast.makeText(fragment.context, exception.message, Toast.LENGTH_SHORT).show()
            }

    }

    fun addPanditJeeDetail(fragment: Fragment, pandits: Pandits) {
        if (fragment is PushDataOverMain) {
            fragment.showDialog(fragment.getString(R.string.pls_wait))
        }
        fireStore.collection(Constants.PANDIT_DATA)
            .add(pandits)
            .addOnSuccessListener {
                if (fragment is PushDataOverMain) {
                    fragment.dismissDialog()
                }
                Toast.makeText(
                    fragment.context,
                    fragment.getString(R.string.pandit_jee_added_successfully),
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(fragment.context, exception.message, Toast.LENGTH_SHORT).show()
            }
    }

    fun addArticle(fragment:PushOverWisdomFragment, wisdom: Wisdom) {
        fragment.showDialog(fragment.getString(R.string.pls_wait))
        fireStore.collection(Constants.ARTICLE_DATA)
            .add(wisdom)
            .addOnSuccessListener {
                fragment.dismissDialog()
                Toast.makeText(fragment.context,
                    fragment.getString(R.string.article_added_successfully),
                    Toast.LENGTH_SHORT).show()
                }
            .addOnFailureListener { exception ->
                fragment.dismissDialog()
                Toast . makeText (fragment.context, exception.message, Toast.LENGTH_SHORT).show()
            }
    }

    fun addingViewPagerImageToFireStore(
        fragment: PushDataOverMain,
        path: String,
        bitmap: Bitmap,
        message: String
    ) {
        val storageRef: StorageReference = FirebaseStorage.getInstance().reference.child(path)
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos)
        val data = baos.toByteArray()
        fragment.showDialog(fragment.getString(R.string.pls_wait))
        val uploadTask = storageRef.putBytes(data)
        uploadTask.addOnFailureListener { exception ->
            Toast.makeText(fragment.context, exception.message, Toast.LENGTH_SHORT).show()
            fragment.dismissDialog()
        }.addOnSuccessListener {
            taskSnapshot->
            fragment.dismissDialog()
            fragment.showDialog(fragment.getString(R.string.pls_wait))
            taskSnapshot.metadata!!.reference!!.downloadUrl
                .addOnSuccessListener {
                        uri->
                    fragment.dismissDialog()
                    Toast.makeText(fragment.context, message, Toast.LENGTH_SHORT).show()
                    when(path){
                        Constants.pathOne->{
                           Constants.imageOne=uri.toString()
                        }
                        Constants.pathTwo->{
                           Constants.imageTwo=uri.toString()
                        }
                        Constants.pathThree->{
                           Constants.imageThree=uri.toString()
                        }
                    }
                }
                .addOnFailureListener {
                        exception ->
                    Toast.makeText(fragment.context,exception.message, Toast.LENGTH_SHORT).show()
                    fragment.dismissDialog()
                }
        }
    }

    fun finalSubmitImages(fragment: PushDataOverMain,images:Images,button: Button){
        fragment.showDialog(fragment.getString(R.string.pls_wait))
        fireStore.collection(Constants.IMAGES)
            .document(Constants.IMAGES)
            .set(images, SetOptions.merge())
            .addOnSuccessListener {
                button.isEnabled=false
                fragment.dismissDialog()
                Toast.makeText(fragment.context,fragment.getString(R.string.final_submit_done),Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                exception->
                fragment.dismissDialog()
                Toast.makeText(fragment.context,exception.message,Toast.LENGTH_LONG).show()
            }

    }

    fun downloadImages(fragment: ViewPagerCollectionFragment,imageView:ImageView,value:Int){
        fragment.showDialog(fragment.getString(R.string.pls_wait))
        val imageArrayList:ArrayList<Images> = ArrayList()
        fireStore.collection(Constants.IMAGES)
            .document(Constants.IMAGES)
            .get()
            .addOnSuccessListener { document ->
                fragment.dismissDialog()
                val images = document.toObject(Images::class.java)
                imageArrayList.add(images!!)

                when(value){
                    1->{
                      Picasso.get().load(imageArrayList[0].imageOne)
                          .placeholder(R.drawable.sync)
                          .into(imageView)
                    }
                    2->{
                        Picasso.get().load(imageArrayList[0].imageTwo)
                            .placeholder(R.drawable.sync)
                            .into(imageView)
                    }
                    3->{
                        Picasso.get().load(imageArrayList[0].imageThree)
                            .placeholder(R.drawable.sync)
                            .into(imageView)
                    }
                }
            }
            .addOnFailureListener {
                    exception->
                fragment.dismissDialog()
                Toast.makeText(fragment.context,exception.message,Toast.LENGTH_SHORT).show()
            }
    }

     fun downloadEvent(fragment:HomeFragment){
        fragment.showDialog(fragment.getString(R.string.pls_wait))
        val eventArrayList:ArrayList<Events> =ArrayList()
        fireStore.collection(Constants.EVENT_DATA)
            .get()
            .addOnSuccessListener {
                documentSnapshot->
                fragment.dismissDialog()
                for(document in documentSnapshot.documents){
                    val events=document.toObject(Events::class.java)
                    eventArrayList.add(events!!)
                }
                fragment.illustrateEvent(eventArrayList)
            }
            .addOnFailureListener {
                exception ->
                fragment.dismissDialog()
                Toast.makeText(fragment.context,exception.message,Toast.LENGTH_SHORT).show()
            }
    }

    fun downloadPanditJee(fragment:HomeFragment) {
        //fragment.showDialog(fragment.getString(R.string.pls_wait))
        val panditArrayList: ArrayList<Pandits> = ArrayList()
        fireStore.collection(Constants.PANDIT_DATA)
            .get()
            .addOnSuccessListener { snapshot ->
                fragment.dismissDialog()
                for (documents in snapshot) {
                    val panditJee = documents.toObject(Pandits::class.java)
                    panditArrayList.add(panditJee)
                }
                fragment.illustratePanditJee(panditArrayList)
            }
            .addOnFailureListener { exception ->
                fragment.dismissDialog()
                Toast.makeText(fragment.context, exception.message, Toast.LENGTH_SHORT).show()
            }

    }

        fun downloadArticle(fragment: Fragment){
            Constants.ARTICLE_ARRAY_LIST = ArrayList()
            fireStore.collection(Constants.ARTICLE_DATA)
                .orderBy("articleTitle",Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener {
                    snapshot->
                    if(fragment is HomeFragment){
                        fragment.dismissDialog()
                    }
                    for(document in snapshot){
                        val wisdomList=document.toObject(Wisdom::class.java)
                        Constants.ARTICLE_ARRAY_LIST.add(wisdomList)
                    }
                    if(fragment is HomeFragment){
                        fragment.dismissDialog()
                        fragment.illustrateArticle()
                    }
                }
                .addOnFailureListener {
                    exception-> if(fragment is HomeFragment){
                    fragment.dismissDialog()
                }
                    Toast.makeText(fragment.context,exception.message,Toast.LENGTH_SHORT).show()
                }
        }

    fun submitQuestion(fragment:QuestionForm,question:Question){
        fireStore.collection(Constants.QUESTION)
            .add(question)
            .addOnSuccessListener {
                fragment.questionAdded()
            }
            .addOnFailureListener{
                exception ->
                Toast.makeText(fragment.context,exception.message,Toast.LENGTH_SHORT).show()
            }
    }
    fun fetchingQuestion(fragment:Fragment){
        if(fragment is AskQuestion)
            fragment.showDialog(fragment.getString(R.string.pls_wait))
        if(fragment is ReplyFragment)
            fragment.showDialog(fragment.getString(R.string.pls_wait))
        val questionArrayList=ArrayList<Question>()
        fireStore.collection(Constants.QUESTION)
            .get()
            .addOnSuccessListener {
                snapshot->
                if(fragment is AskQuestion)
                    fragment.dismissDialog()
                if(fragment is ReplyFragment)
                    fragment.dismissDialog()
                for(document in snapshot){
                    val questionList=document.toObject(Question::class.java)
                    questionList.documentId=document.id
                    questionArrayList.add(questionList)
                }
                if(fragment is AskQuestion)
                    fragment.fetchingQuestionList(questionArrayList)
                if(fragment is ReplyFragment){
                    fragment.configuringAnswerRecyclerView(questionArrayList)
                }
            }
            .addOnFailureListener{
                exception->
                if(fragment is AskQuestion)
                    fragment.dismissDialog()
                if(fragment is ReplyFragment)
                    fragment.dismissDialog()
                Toast.makeText(fragment.context,exception.message,Toast.LENGTH_SHORT).show()
            }
    }

    fun fetchingOnlyQuestion(fragment:UserProfile){
        val arrayList:ArrayList<Question> = ArrayList()
        fragment.showDialog(fragment.getString(R.string.pls_wait))
        fireStore.collection(Constants.QUESTION)
            .whereArrayContains(Constants.ALL_QUESTION_FOR_USER,returnCurrentUid())
            .get()
            .addOnSuccessListener {
                snapshot->
                fragment.dismissDialog()
                for(document in snapshot){
                   val question=document.toObject(Question::class.java)
                    question.documentId=document.id
                    arrayList.add(question)
                }
                fragment.configuringRecyclerView(arrayList)
            }
            .addOnFailureListener{
                exception ->
                fragment.dismissDialog()
                Toast.makeText(fragment.context,exception.message,Toast.LENGTH_SHORT).show()
            }
    }

    fun addAdmin(fragment:SignUpFragment,email:HashMap<String,Any>){
        fragment.showDialog(fragment.getString(R.string.pls_wait))
        fireStore.collection(Constants.ADMIN_UID)
            .add(email)
            .addOnSuccessListener {
                Toast.makeText(fragment.context,fragment.getString(R.string.congrats),Toast.LENGTH_SHORT).show()
                fragment.dismissDialog()
            }
            .addOnFailureListener {
                exception->
                fragment.dismissDialog()
                Toast.makeText(fragment.context,exception.message,Toast.LENGTH_SHORT).show()
            }
    }

    fun fetchAdminEmail(fragment:Fragment){
        if(fragment is SignUpFragment){
            fragment.showDialog(fragment.getString(R.string.pls_wait))}
        if(fragment is AdminOnly){
            fragment.showDialog(fragment.getString(R.string.pls_wait))}
        val arrayList:ArrayList<String> = ArrayList()
        fireStore.collection(Constants.ADMIN_UID)
            .get()
            .addOnSuccessListener {
                snapshot->
                if(fragment is SignUpFragment){
                fragment.dismissDialog()}
                if (fragment is AdminOnly){
                    fragment.dismissDialog()
                }
                for(document in snapshot){
                    val emailAdmin=document.toObject(AdminEmail::class.java)
                    arrayList.add(emailAdmin.email)
                }
                if(fragment is SignUpFragment){
                    fragment.checkForAdmin(arrayList)}
                if (fragment is AdminOnly){
                    fragment.adminTest(arrayList)
                    fragment.adminTestForLogIn(arrayList)
                }
            }
            .addOnFailureListener {
                exception ->
                if(fragment is SignUpFragment){
                    fragment.dismissDialog()
                }
                if (fragment is AdminOnly){
                    fragment.dismissDialog()
                }
                Toast.makeText(fragment.context,exception.message,Toast.LENGTH_SHORT).show()
            }
    }


    fun updateUser(fragment:Fragment,name:String,imageUrl:String){
        /*if(fragment is SignUpFragment){
        //fragment.showDialog(fragment.getString(R.string.pls_wait))
        }*/
        val user= mAuth.currentUser
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setPhotoUri(
                Uri.parse(imageUrl)
            )
            .setDisplayName(name)
            .build()
        user?.updateProfile(profileUpdates)
            ?.addOnCompleteListener { task ->
                if(task.isSuccessful)
                    if(fragment is SignUpFragment){
                        fragment.dismissDialog()
                    }
            }
            ?.addOnFailureListener {
                    exception ->
                if(fragment is SignUpFragment){
                    fragment.dismissDialog()
                }
                Toast.makeText(fragment.context,exception.message,Toast.LENGTH_SHORT).show()
            }
    }

    fun updatingAnswer(fragment: ReplyFragment,question: Question,answer:HashMap<String,Any>){
        fragment.showDialog(fragment.getString(R.string.pls_wait))
        fireStore.collection(Constants.QUESTION)
            .document(question.documentId)
            .update(answer)
            .addOnSuccessListener {
                fragment.dismissDialog()
                fetchingQuestion(fragment)
                Toast.makeText(fragment.context,fragment.context?.getString(R.string.reply_sent),Toast.LENGTH_SHORT).show()
                NotificationClass(fragment.requireContext(),fragment.requireActivity()).fcmPassedPreference(true)
                Toast.makeText(fragment.context,question.fcm,Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                exception->
                fragment.dismissDialog()
                Toast.makeText(fragment.context,exception.message,Toast.LENGTH_LONG).show()
            }
    }

    fun deleteQuestion(fragment:UserProfile,question: Question){
        fireStore.collection(Constants.QUESTION)
            .document(question.documentId)
            .delete()
            .addOnCompleteListener {
                task->
                if(task.isSuccessful){
                    //fragment.dismissDialog()
                    Toast.makeText(fragment.context,fragment.getString(R.string.question_deleted),Toast.LENGTH_SHORT).show()
                    fetchingOnlyQuestion(fragment)
                }
            }
            .addOnFailureListener {
                exception->
                //fragment.dismissDialog()
                Toast.makeText(fragment.context,exception.message,Toast.LENGTH_SHORT).show()
            }
        fragment.dismissDialog()
    }

    fun editedQuestionUpdate(fragment:UserProfile,question: Question,fullQuestion: String,title:String){
        val hashMap:HashMap<String,Any> = HashMap()
        hashMap[Constants.TITLE]=title
        hashMap[Constants.QUESTION_TO_UPDATE]=fullQuestion
        fireStore.collection(Constants.QUESTION)
            .document(question.documentId)
            .update(hashMap)
            .addOnSuccessListener {
                Toast.makeText(fragment.context,fragment.getString(R.string.question_successfully_edited),Toast.LENGTH_SHORT).show()
                fetchingOnlyQuestion(fragment)
            }
            .addOnFailureListener {
                exception ->
                Toast.makeText(fragment.context,exception.message,Toast.LENGTH_SHORT).show()
            }
    }


    fun updateOnlyDisplayName(fragment: Fragment,name:String){
        if(fragment is SignUpFragment){
            fragment.showDialog(fragment.getString(R.string.pls_wait))
        }
        if(fragment is UpdateProfile){
            fragment.showDialog(fragment.getString(R.string.pls_wait))
        }
        if(fragment is SignInSignUpFragment)
            fragment.showDialog(fragment.getString(R.string.pls_wait))
        val user= mAuth.currentUser
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(name)
            .build()
        user?.updateProfile(profileUpdates)
            ?.addOnCompleteListener { task ->
                if(task.isSuccessful)
                    if(fragment is SignUpFragment){
                        fragment.dismissDialog()
                    }
                    if(fragment is SignInSignUpFragment){
                        fragment.dismissDialog()
                        fragment.updateAndIntend()
                    }
                if(fragment is UpdateProfile){
                    fragment.dismissDialog()
                    fragment.updateAction()
                }
            }
            ?.addOnFailureListener {
                    exception ->
                if(fragment is SignUpFragment){
                    fragment.dismissDialog()
                }
                if(fragment is UpdateProfile){
                fragment.dismissDialog()
            }
                if(fragment is SignInSignUpFragment)
                    fragment.dismissDialog()
                Toast.makeText(fragment.context,exception.message,Toast.LENGTH_SHORT).show()
            }
    }

    fun validateEmail(fragment: SignUpFragment,email:String){
        mAuth.fetchSignInMethodsForEmail(email)
            .addOnCompleteListener {
                task ->
                if(task.isSuccessful){
                    fragment.validatingLayout(task.result!!.signInMethods!!.size)
                }
            }
    }

    fun signInAccount(fragment:Fragment,email:String, password:String,view: View){
        if(fragment is AdminOnly)
        fragment.showDialog(fragment.getString(R.string.pls_wait))
        if(fragment is SignInSignUpFragment)
            fragment.showDialog(fragment.getString(R.string.pls_wait))
        mAuth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                    task ->
                if (task.isSuccessful){
                    if(fragment is AdminOnly){
                       fragment.dismissDialog()
                        fragment.enablingView(view)
                    }
                    if(fragment is SignInSignUpFragment){
                        fragment.dismissDialog()
                        fragment.handelLogIn()
                    }
                }
            }
            .addOnFailureListener{ exception ->
                if (fragment is AdminOnly)
                    fragment.dismissDialog()
                if(fragment is SignInSignUpFragment)
                    fragment.dismissDialog()
                Toast.makeText(fragment.context,exception.message,Toast.LENGTH_LONG).show()
            }
    }

    fun signUpAccount(fragment:Fragment,email:String,password: String,name: String){
        if(fragment is SignUpFragment){
            fragment.showDialog(fragment.getString(R.string.pls_wait))
        }
        if (fragment is SignInSignUpFragment){
            fragment.showDialog(fragment.getString(R.string.pls_wait))
        }
        mAuth.createUserWithEmailAndPassword(email,password)
            .addOnSuccessListener {
                if(fragment is SignUpFragment){
                    fragment.dismissDialog()
                    fragment.creatingAndRegisteringAdmin(name)
                }
                if (fragment is SignInSignUpFragment){
                    fragment.dismissDialog()
                    fragment.signUpAndIntendUser(name)
                }
            }
            .addOnFailureListener {
                exception->
                Toast.makeText(fragment.context,exception.message,Toast.LENGTH_SHORT).show()
                if(fragment is SignUpFragment){
                    fragment.dismissDialog()
                    Toast.makeText(fragment.context,exception.message,Toast.LENGTH_SHORT).show()
                }
                if (fragment is SignInSignUpFragment){
                    fragment.dismissDialog()
                    Toast.makeText(fragment.context,exception.message,Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun updateProfileImage(fragment:UpdateProfile,url:String){
        val user= mAuth.currentUser
        fragment.showDialog(fragment.getString(R.string.pls_wait))
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setPhotoUri(
                Uri.parse(url)
            )
            .build()
        user?.updateProfile(profileUpdates)
            ?.addOnCompleteListener {
                task->
                fragment.dismissDialog()
                if(task.isSuccessful){
                    fragment.handleAction()
                }
            }
            ?.addOnFailureListener{
                exception->
                Toast.makeText(fragment.context,exception.message,Toast.LENGTH_SHORT).show()
                fragment.dismissDialog()
            }
    }

    fun reAuthenticate(fragment: UpdateProfile,email:String,password: String,newPassword:String){
        fragment.showDialog(fragment.getString(R.string.pls_wait))
        val credentials = EmailAuthProvider.getCredential(email,password)
        mAuth.currentUser!!.reauthenticate(credentials)
            .addOnCompleteListener {
                    task->
                fragment.dismissDialog()
                if(task.isSuccessful){
                    fragment.changePassword(newPassword)
                }
            }
            .addOnFailureListener {
                    exception->
                fragment.dismissDialog()
                Toast.makeText(fragment.context,exception.message,Toast.LENGTH_SHORT).show()
            }
    }
    private fun returnCurrentUid():String{
        val user=mAuth.currentUser
        var currentUser=""
        if(user!=null){
            currentUser=user.uid
        }
        return currentUser
    }
}