package com.russvkm.askacharya.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.russvkm.askacharya.R
import com.russvkm.askacharya.utils.LanguageClass
import kotlinx.android.synthetic.main.activity_language.*

class LanguageActivity : AppCompatActivity(), View.OnClickListener {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_language)
    hindi.setOnClickListener(this)
    english.setOnClickListener(this)
  }

  override fun onClick(v: View?) {
    when(v!!.id){
      R.id.hindi->{
        LanguageClass(this).hindi()
        hindiTextView.background=ContextCompat.getDrawable(this,R.drawable.language_selected_background)
        hindiTextView.setTextColor(Color.WHITE)
        englishTextView.background=null
        intendingToMain()
      }
      R.id.english->{
        LanguageClass(this).english()
        englishTextView.background=ContextCompat.getDrawable(this,R.drawable.language_selected_background)
        englishTextView.setTextColor(Color.WHITE)
        hindiTextView.background=null
        intendingToMain()
      }
    }
  }

  private fun intendingToMain(){
    startActivity(Intent(this,MainActivity::class.java))
    finish()
  }
}