package com.russvkm.askacharya.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.*
import android.widget.*
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.get
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.russvkm.askacharya.R
import com.russvkm.askacharya.adapter.QuestionAdapter
import com.russvkm.askacharya.adapter.SignInSignUpAdapter
import com.russvkm.askacharya.firebase.FirebaseHandler
import com.russvkm.askacharya.models.Question
import com.russvkm.askacharya.utils.BaseClass
import com.russvkm.askacharya.utils.Constants
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_ask_question.view.*

class AskQuestion : BaseClass(),View.OnClickListener {
    private lateinit var mAuth:FirebaseAuth
    private lateinit var googleSignUpButton:SignInButton
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var logInLayout:ConstraintLayout
    private lateinit var askQuestion: AppCompatButton
    private lateinit var questionAnswerRecyclerView: RecyclerView
    private lateinit var menuItem: MenuItem
    private lateinit var adapter: QuestionAdapter
    private lateinit var pager: ViewPager2
    private lateinit var tabLayout:TabLayout
    private lateinit var signInSignUpAdapter:SignInSignUpAdapter
    private var target : com.squareup.picasso.Target? = null
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root=inflater.inflate(R.layout.fragment_ask_question, container, false)
        setHasOptionsMenu(true)
        googleSignUpButton=root.findViewById(R.id.googleSignInButton)
        logInLayout=root.findViewById(R.id.loginLayout)
        askQuestion=root.findViewById(R.id.askQuestion)
        questionAnswerRecyclerView=root.findViewById(R.id.questionAnswerRecyclerView)
        mAuth=FirebaseAuth.getInstance()
        googleSignUpButton.setSize(SignInButton.SIZE_STANDARD)
        val textViewGoogle= googleSignUpButton[0] as TextView
        textViewGoogle.text=requireContext().getString(R.string.sign_in_with_google)
        textViewGoogle.setTextSize(TypedValue.COMPLEX_UNIT_PX,
            resources.getDimensionPixelSize(R.dimen.txt_size).toFloat()
        )
        val gso=GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient= GoogleSignIn.getClient(requireActivity(),gso)
        FirebaseHandler().fetchingQuestion(this@AskQuestion)
        root.askQuestion.setOnClickListener(this)
        googleSignUpButton.setOnClickListener(this)
        illustratingViewPager2(root)
        return root
    }

    private fun signIn(){
        val signInIntent=googleSignInClient.signInIntent
        startActivityForResult(signInIntent,RC_SIGN_IN)
    }

    companion object{
        private const val RC_SIGN_IN=30
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode== RC_SIGN_IN){
            val task=GoogleSignIn.getSignedInAccountFromIntent(data)
            try{
                val account=task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account?.idToken!!)
            }catch (e: ApiException){
                e.printStackTrace()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken:String){
        val credential=GoogleAuthProvider.getCredential(idToken,null)
        showDialog(requireContext().getString(R.string.pls_wait))
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener {
                task->
                if(task.isSuccessful){
                    mAuth.currentUser
                    logInLayout.visibility=View.GONE
                    questionAnswerRecyclerView.visibility=View.VISIBLE
                    askQuestion.visibility=View.VISIBLE
                    if(Constants.askQuestionSwitch){
                        view?.findNavController()?.navigate(R.id.action_nav_question_to_questionForm)
                    }else{
                        view?.findNavController()!!.navigate(R.id.action_nav_question_to_userProfile)
                    }
                    displayingAuthenticatedMenu()
                }else{
                    Log.i("Current User",task.exception!!.message!!)
                }
                dismissDialog()
            }
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.googleSignInButton->{
                signIn()
            }
            R.id.askQuestion->{
                if(mAuth.currentUser==null){
                    logInLayout.visibility=View.VISIBLE
                    questionAnswerRecyclerView.visibility=View.GONE
                }else{
                    v.findNavController().navigate(R.id.action_nav_question_to_questionForm)
                }
                Constants.askQuestionSwitch=true
                askQuestion.visibility=View.GONE
            }
        }
    }
    fun fetchingQuestionList(questionArrayList:ArrayList<Question>){
        questionAnswerRecyclerView.layoutManager=LinearLayoutManager(context)
        questionAnswerRecyclerView.setHasFixedSize(true)
        adapter=QuestionAdapter(this,requireContext(),questionArrayList)
        questionAnswerRecyclerView.adapter=adapter
        adapter.setOnClickListener(object :QuestionAdapter.OnClickListener{
            override fun onClick(position: Int, question: Question) {
                view!!.findNavController().navigate(AskQuestionDirections.actionNavQuestionToQuestionDetail(question))
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.user_menu,menu)
        menuItem=menu.getItem(1)
        if (mAuth.currentUser!=null){
            displayingAuthenticatedMenu()
        }
        val item=menu.findItem(R.id.searchButton)
        val searchView=item.actionView as SearchView
        searchView.setOnQueryTextListener(object:SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return false
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.userImage->{
                if(mAuth.currentUser!=null){
                    view?.findNavController()!!.navigate(R.id.action_nav_question_to_userProfile)
                }else{
                    logInLayout.visibility=View.VISIBLE
                    questionAnswerRecyclerView.visibility=View.GONE
                    askQuestion.visibility=View.GONE
                }
            }

        }
        return super.onOptionsItemSelected(item)
    }

    private fun com.squareup.picasso.Target.picassoLoad(url: String): com.squareup.picasso.Target{
        Picasso.get()
            .load(
                url
            )
            .into(this)
        return this
    }

    private fun displayingAuthenticatedMenu(){
        target=object : com.squareup.picasso.Target{
            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                menuItem.setIcon(R.drawable.bordered_person_image)
            }

            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                menuItem.setIcon(R.drawable.bordered_person_image)
            }

            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                menuItem.icon=BitmapDrawable(resources,bitmap)
            }
        }.picassoLoad(mAuth.currentUser!!.photoUrl.toString())
    }

    private fun illustratingViewPager2(root:View){
        tabLayout=root.findViewById(R.id.signUpLogIn)
        pager=root.findViewById(R.id.pager)
        signInSignUpAdapter= SignInSignUpAdapter(this@AskQuestion)
        pager.adapter=signInSignUpAdapter
        TabLayoutMediator(tabLayout,pager){
                tab,position ->
            when(position){
                0->{
                    tab.text=context?.getString(R.string.sign_up)
                }
                1->{
                    tab.text=context?.getString(R.string.sign_in)
                }
            }
        }.attach()
    }
}