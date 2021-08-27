package com.russvkm.askacharya.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.russvkm.askacharya.R
import com.russvkm.askacharya.utils.LanguageClass
import kotlinx.android.synthetic.main.activity_flash.*

class FlashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flash)
        Handler().postDelayed({
            languageHandling()
            startProgressBar.progress=100
        },1000)
    }

    private fun languageHandling(){
        when(LanguageClass(this).fetchingPreferences()) {
            0->{
              startActivity(Intent(this@FlashActivity,LanguageActivity::class.java))
            }
            1->{
                LanguageClass(this).english()
                startActivity(Intent(this@FlashActivity,MainActivity::class.java))
                finish()
            }
            2->{
               LanguageClass(this).hindi()
                startActivity(Intent(this@FlashActivity,MainActivity::class.java))
                finish()
            }
        }
    }
}