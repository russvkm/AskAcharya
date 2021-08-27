package com.russvkm.askacharya.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.russvkm.askacharya.R
import com.russvkm.askacharya.models.Question
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_question_detail.view.*

class QuestionDetail : Fragment() {

    private lateinit var askedBy:TextView
    private lateinit var question:TextView
    private lateinit var answer:TextView
    private lateinit var personName:TextView
    private lateinit var askedByImage:ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root=inflater.inflate(R.layout.fragment_question_detail, container, false)
        findingViewsByIds(root)
        val args=QuestionDetailArgs.fromBundle(requireArguments())
        val bundle=args.wholeAnswerQuestion
        illustratingView(bundle)
        return root
    }


    private fun findingViewsByIds(view:View){
        question=view.findViewById(R.id.question)
        answer=view.findViewById(R.id.answer)
        personName=view.findViewById(R.id.personName)
        askedBy=view.findViewById(R.id.askedBy)
        askedByImage=view.findViewById(R.id.askedByImage)
    }

    @SuppressLint("SetTextI18n")
    private fun illustratingView(bundle: Question){
       question.text="Question:- ${bundle.question}"
        if (bundle.answer.isNotEmpty()){
            answer.text="Answer:- ${bundle.answer}"
        }else{
            answer.text=context?.getString(R.string.still_need_to_reply)
        }
        askedBy.text=context?.getString(R.string.asked_by)
        Picasso.get()
            .load(bundle.image)
            .placeholder(R.drawable.person_black)
            .into(askedByImage)
        personName.text=bundle.name
    }
}