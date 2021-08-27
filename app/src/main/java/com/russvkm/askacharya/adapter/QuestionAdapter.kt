package com.russvkm.askacharya.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.*
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.iid.FirebaseInstanceId
import com.russvkm.askacharya.R
import com.russvkm.askacharya.firebase.FirebaseHandler
import com.russvkm.askacharya.fragment.ReplyFragment
import com.russvkm.askacharya.models.Question
import com.russvkm.askacharya.utils.Constants
import com.russvkm.askacharya.utils.NotificationClass
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.question_adapter_layout.view.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.lang.Exception
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.URL
import java.util.*
import kotlin.collections.ArrayList


class QuestionAdapter(
    private val fragment: Fragment,
    private val context: Context,
    private val question: ArrayList<Question>
):
    RecyclerView.Adapter<QuestionAdapter.MyViewHolder>() ,Filterable{
    private var onClickListener:OnClickListener?=null
    private var viewController:Int=0
    private var filterArrayList=ArrayList<Question>()
    private lateinit var fcmToken: String

    init {
        filterArrayList=question
    }

    class MyViewHolder(view: View):RecyclerView.ViewHolder(view) {
        val titleTextView:TextView=view.titleTextView
        val questionTextView:TextView=view.questionTextView
        val askingPersonImage:ImageView=view.askingPersonImage
        val askingPersonName:TextView=view.askingPersonName
        val answerTextView:TextView=view.answer
        val replyView:LinearLayout=view.replyView
        val replyEditText:EditText=view.replyEditText
        val reply:TextView=view.replyTextView
        val deleteTextView:TextView=view.delete
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.question_adapter_layout, parent, false)
        )
    }

    override fun getItemCount(): Int =filterArrayList.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model=filterArrayList[position]
        holder.titleTextView.text=model.title
        holder.questionTextView.text=model.question
        holder.reply.text=model.answer
        holder.askingPersonName.text= HtmlCompat.fromHtml(
            "<u>${model.name}</u>",
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
        Picasso.get()
            .load(model.image)
            .into(holder.askingPersonImage)
        if(fragment is ReplyFragment){
            holder.answerTextView.visibility=View.VISIBLE
            holder.deleteTextView.visibility=View.VISIBLE
            if (NotificationClass(fragment.requireContext(),fragment.requireActivity()).isFcmPassed()){
                notificationLoadService(model.answer,model.fcm)
            }
        }else{
            holder.answerTextView.visibility=View.GONE
            holder.deleteTextView.visibility=View.GONE
        }
        holder.answerTextView.setOnClickListener {
            if(viewController==0){
                holder.replyView.visibility=View.VISIBLE
                viewController=1
            }else{
                holder.replyView.visibility=View.GONE
                viewController=0
            }
        }
       drawableOnClick(holder.replyEditText,model)
        holder.deleteTextView.setOnClickListener {
            createDeleteDialog(model)
        }
        holder.itemView.setOnClickListener {
            if(onClickListener!=null){
                onClickListener!!.onClick(position,model)
            }
        }
    }

    fun setOnClickListener(onClickListener: OnClickListener){
        this.onClickListener=onClickListener
    }
    
    interface OnClickListener{
        fun onClick(position: Int,question: Question)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun drawableOnClick(editComment:EditText,model:Question){
        editComment.setOnTouchListener(OnTouchListener { _, event ->
            val DRAWABLE_RIGHT = 2
            FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
                    instanceResult->
                fcmToken=instanceResult.token
            }
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= editComment.right - editComment.compoundDrawables[DRAWABLE_RIGHT].bounds.width()
                ) {
                    if(editComment.text.isEmpty()){
                        editComment.error=context.getString(R.string.enter_reply)
                        editComment.requestFocus()
                    }else{
                        val answer:HashMap<String,Any> =HashMap()
                        answer[Constants.ANSWER]=editComment.text.toString()
                        answer[Constants.FCM_QUESTION]=fcmToken
                        FirebaseHandler().updatingAnswer(
                            fragment as ReplyFragment,
                            model,
                            answer
                        )
                    }
                    return@OnTouchListener true
                }
            }
            false
        })
    }

    override fun getFilter(): Filter {
        return object :Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch=constraint.toString()
                filterArrayList = if(charSearch.isEmpty()){
                    question
                }else{
                    val resultList=ArrayList<Question>()
                    for(row in question){
                        if (row.title.toLowerCase(Locale.ROOT).contains(charSearch.toLowerCase(Locale.ROOT))||
                            row.question.toLowerCase(Locale.ROOT).contains(charSearch.toLowerCase(Locale.ROOT))||
                            row.answer.toLowerCase(Locale.ROOT).contains(charSearch.toLowerCase(Locale.ROOT))||
                            row.name.toLowerCase(Locale.ROOT).contains(charSearch.toLowerCase(Locale.ROOT))) {
                            resultList.add(row)
                        }
                    }
                    resultList
                }

                val filterResults = FilterResults()
                filterResults.values = filterArrayList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filterArrayList = results?.values as ArrayList<Question>
                notifyDataSetChanged()
            }

        }
    }

    private fun createDeleteDialog(question:Question){
        AlertDialog.Builder(context)
            .setIcon(R.drawable.warning)
            .setTitle(context.getString(R.string.want_to_delete))
            .setMessage(context.getString(R.string.clicking_yes_reply))
            .setPositiveButton(R.string.yes) {
                    _, _ ->
                val answer:HashMap<String,Any> =HashMap()
                answer[Constants.ANSWER]=""
                FirebaseHandler().updatingAnswer(fragment as ReplyFragment,question, answer)
            }
            .setNegativeButton(R.string.cancel){
                dialog,_->
                dialog.dismiss()
            }
            .create()
            .show()
    }


    @SuppressLint("StaticFieldLeak")
    private inner class ShowNotificationByDownloading(private val reply:String,private val token:String):
        AsyncTask<Any, Void, String>(){
        override fun onPreExecute() {
            super.onPreExecute()
            (fragment as ReplyFragment).showDialog(fragment.requireContext().getString(R.string.pls_wait))
        }
        override fun doInBackground(vararg params: Any?): String {
            var result:String
            var connection: HttpURLConnection?=null
            try {
                val url= URL(Constants.FCM_BASE_URL)
                connection=url.openConnection() as HttpURLConnection
                connection.doInput=true
                connection.doOutput=true
                connection.instanceFollowRedirects=false
                connection.requestMethod="POST"
                connection.setRequestProperty("Content-Type","application/json")
                connection.setRequestProperty("charset","utf-8")
                connection.setRequestProperty("Accept","application/json")
                connection.setRequestProperty(Constants.FCM_AUTHORIZATION,"${Constants.FCM_KEY}=${Constants.FCM_TOKEN_KEY}")
                connection.useCaches=false
                val wr= DataOutputStream(connection.outputStream)
                val jsonRequest= JSONObject()
                val dataObject= JSONObject()
                dataObject.put(Constants.FCM_KEY_TITLE,"Your question is replied")
                dataObject.put(Constants.FCM_KEY_MESSAGE,"Yor question is replied tap to read answer of your question $reply")
                //TODO write all messages
                jsonRequest.put(Constants.FCM_KEY_DATA,dataObject)
                jsonRequest.put(Constants.FCM_KEY_TO,token)
                wr.writeBytes(jsonRequest.toString())
                wr.flush()
                wr.close()
                val httpResult:Int=connection.responseCode
                if(httpResult== HttpURLConnection.HTTP_OK){
                    val inputStream=connection.inputStream
                    val reader= BufferedReader(InputStreamReader(inputStream))
                    val sb= StringBuilder()
                    var line:String?
                    try {
                        while (reader.readLine().also { line=it }!=null){
                            sb.append(line+"\n")
                        }
                    }catch (e: IOException){
                        e.printStackTrace()
                    }finally {
                        try {
                            inputStream.close()
                        }catch (e: IOException){
                            e.printStackTrace()
                        }
                    }
                    result=sb.toString()
                }else{
                    result=connection.responseMessage
                }
            }catch (e: SocketTimeoutException){
                result=e.message!!
            }catch (e: Exception){
                result=e.message!!
            }finally {
                connection?.disconnect()
            }
            return result
        }
        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            (fragment as ReplyFragment).dismissDialog()
        }
    }

   private  fun notificationLoadService(reply:String,token: String){
       if(NotificationClass(fragment.requireContext(),fragment.requireActivity()).
           fetchAllNotificationPreference("notification_pref","notification")!=1&&
           NotificationClass(fragment.requireContext(),fragment.requireActivity())
               .fetchAllNotificationPreference("question_pref","question_key")!=1){
           ShowNotificationByDownloading(reply,token)
       }
    }
}