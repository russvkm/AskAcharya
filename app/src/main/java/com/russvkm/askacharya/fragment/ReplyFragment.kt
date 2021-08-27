package com.russvkm.askacharya.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.russvkm.askacharya.R
import com.russvkm.askacharya.adapter.QuestionAdapter
import com.russvkm.askacharya.firebase.FirebaseHandler
import com.russvkm.askacharya.models.Question
import com.russvkm.askacharya.utils.BaseClass


class ReplyFragment : BaseClass() {
    private lateinit var answerRecyclerView:RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root=inflater.inflate(R.layout.fragment_reply, container, false)
        answerRecyclerView=root.findViewById(R.id.answerRecyclerView)
        FirebaseHandler().fetchingQuestion(this)
        return root
    }

    fun configuringAnswerRecyclerView(questionArrayList:ArrayList<Question>){
        answerRecyclerView.layoutManager=LinearLayoutManager(requireContext())
        answerRecyclerView.setHasFixedSize(true)
        val adapter= QuestionAdapter(this,requireContext(),questionArrayList)
        answerRecyclerView.adapter=adapter
    }
}