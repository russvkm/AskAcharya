package com.russvkm.askacharya.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.russvkm.askacharya.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        configuringActionBar()
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.navHome,
            R.id.navVideo,
            R.id.nav_wisdom,
            R.id.nav_question,
            R.id.nav_more
        ))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController=findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration)||super.onSupportNavigateUp()
    }

    private fun configuringActionBar(){
        mainActivityToolbar.title=this.getString(R.string.app_name)
        setSupportActionBar(mainActivityToolbar)
        /*toolbar_title.text = mainActivityToolbar.title
        supportActionBar!!.setDisplayShowTitleEnabled(false)*/
    }
}
