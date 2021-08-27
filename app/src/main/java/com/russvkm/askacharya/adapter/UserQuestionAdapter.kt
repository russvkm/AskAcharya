package com.russvkm.askacharya.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.russvkm.askacharya.R
import com.russvkm.askacharya.firebase.FirebaseHandler
import com.russvkm.askacharya.fragment.UserProfile
import com.russvkm.askacharya.models.Question
import kotlinx.android.synthetic.main.edit_delete_dialog.*
import kotlinx.android.synthetic.main.edit_question_dialog.*
import kotlinx.android.synthetic.main.single_user_question_adapter_layout.view.*

class UserQuestionAdapter(private val fragment:UserProfile,private val context: Context,private val questionList:ArrayList<Question>):
    RecyclerView.Adapter<UserQuestionAdapter.MyViewHolder>(){
    class MyViewHolder(view: View):RecyclerView.ViewHolder(view) {
        val questionTextView:TextView=view.questionTextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.single_user_question_adapter_layout,parent,false)
        )
    }

    override fun getItemCount(): Int = questionList.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model=questionList[position]
        holder.questionTextView
            .text=model.question
        holder.questionTextView.setOnClickListener {

        }
        holder.questionTextView.setOnLongClickListener {
            creatingListDialog(model)
            return@setOnLongClickListener true
        }
        holder.questionTextView.setOnClickListener {
            Toast.makeText(context,context.getString(R.string.press_and_hold),Toast.LENGTH_SHORT).show()
        }
    }

    private fun creatingDialog(question:Question){
        AlertDialog.Builder(context)
            .setMessage(context.getString(R.string.clicking_yes))
            .setTitle(context.getString(R.string.want_to_delete))
            .setIcon(R.drawable.warning)
            .setPositiveButton(context.getString(R.string.yes)){
                    _, _ ->
                FirebaseHandler().deleteQuestion(fragment,question)
            }
            .setNegativeButton(context.getString(R.string.cancel)){
                    dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }
    private fun creatingListDialog(question: Question){
        val dialog = Dialog(
            context
        )
        dialog.setContentView(R.layout.edit_delete_dialog)
        dialog.edit.setOnClickListener {
            creatingEditDialog(question)
            dialog.dismiss()
        }
        dialog.delete.setOnClickListener {
            creatingDialog(question)
            dialog.dismiss()
        }
        dialog.show()
        val window=dialog.window
        window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT
        )
    }

    private fun creatingEditDialog(question: Question){
        val editDialog=Dialog(context)
        editDialog.setContentView(R.layout.edit_question_dialog)
        editDialog.questionTitle.setText(question.title)
        editDialog.fullQuestion.setText(question.question)
        editDialog.update.setOnClickListener {
            when{
                editDialog.questionTitle.text.toString().isEmpty()->{
                    editDialog.questionTitle.error= context.getString(R.string.title_cannot_be_empty)
                    editDialog.questionTitle.requestFocus()
                }
                editDialog.fullQuestion.text.toString().isEmpty()->{
                    editDialog.fullQuestion.error= context.getString(R.string.question_cannot_be_empty)
                    editDialog.fullQuestion.requestFocus()
                }
                else->{
                   FirebaseHandler().editedQuestionUpdate(fragment,question,editDialog.fullQuestion.text.toString(),
                       editDialog.questionTitle.text.toString())
                    editDialog.dismiss()
                }
            }
        }
        editDialog.show()
        val window=editDialog.window
        window?.setLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
    }
}