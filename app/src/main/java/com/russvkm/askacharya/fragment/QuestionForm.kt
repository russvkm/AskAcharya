package com.russvkm.askacharya.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.russvkm.askacharya.R
import com.russvkm.askacharya.firebase.FirebaseHandler
import com.russvkm.askacharya.models.Question
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_question_form.view.*


class QuestionForm : Fragment() {
    private var mAuth:FirebaseAuth?=null
    private lateinit var postQuestionButton: AppCompatButton
    private lateinit var titleEditText: EditText
    private lateinit var questionEditText: EditText
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root=inflater.inflate(R.layout.fragment_question_form, container, false)
        postQuestionButton=root.findViewById(R.id.submitQuestion)
        titleEditText=root.findViewById(R.id.questionTitle)
        questionEditText=root.findViewById(R.id.fullQuestion)
        mAuth= FirebaseAuth.getInstance()
        if (mAuth!=null){
            root.nameTextView.text= mAuth!!.currentUser?.displayName
            Picasso.get()
                .load(mAuth!!.currentUser!!.photoUrl)
                .into(root.personImage)
        }
        postQuestion()
        return root
    }

    private fun postQuestion(){
        postQuestionButton.setOnClickListener {
            when{
                titleEditText.text.toString().isEmpty()->{
                    titleEditText.error=context?.getString(R.string.title_cannot_be_empty)
                    titleEditText.requestFocus()
                }
                questionEditText.text.toString().isEmpty()->{
                    questionEditText.error=context?.getString(R.string.question_cannot_be_empty)
                    questionEditText.requestFocus()
                }
                else->{
                    val allQuestion:ArrayList<String> = ArrayList()
                    allQuestion.add(mAuth?.currentUser!!.uid)
                    val question=Question(mAuth?.currentUser?.displayName!!,
                        allQuestion,titleEditText.text.toString(),questionEditText.text.toString(),
                        mAuth?.currentUser?.photoUrl.toString()
                    )
                    FirebaseHandler().submitQuestion(this,question)
                }
            }
        }
    }
    fun questionAdded(){
        view?.findNavController()?.navigate(R.id.action_questionForm_to_nav_question)
    }
}