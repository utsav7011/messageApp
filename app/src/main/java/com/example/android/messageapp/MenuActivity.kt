package com.example.android.messageapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.FrameLayout

class MenuActivity : AppCompatActivity() {

    private lateinit var toolbarMenu: androidx.appcompat.widget.Toolbar
    private lateinit var frameLayout: FrameLayout
    private lateinit var fragmentValue: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
//        the below line keeps the screen of the user lit and does not allow it to get dim:
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        toolbarMenu = findViewById(R.id.toolBarMenu)
        frameLayout = findViewById(R.id.frameLayout)

        if (intent!=null){

            fragmentValue = intent.getStringExtra("optionName").toString()
            when(fragmentValue)
            {
                "profile" -> {
                    supportFragmentManager.beginTransaction().replace(R.id.frameLayout,Profile()).commit()
                    toolbarMenu?.title = "Profile"
                }
                "about" -> {
                    supportFragmentManager.beginTransaction().replace(R.id.frameLayout,About()).commit()
                    toolbarMenu?.title = "About"
                }

                "contacts"->{
                    supportFragmentManager.beginTransaction().replace(R.id.frameLayout,Contacts()).commit()
                    toolbarMenu?.title = "Contacts"
                }
            }
        }


    }
}