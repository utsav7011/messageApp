package com.example.android.messageapp

import android.accounts.Account
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.BaseAdapter
import android.widget.Toast
import android.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var viewPager2: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar

    private lateinit var appPagerAdapter: AppPagerAdapter
    private val titles = arrayListOf<String>("Chats", "Feeds", "Account")
    private lateinit var fAuth :FirebaseAuth
    private lateinit var showContacts:FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        floating action button initialization:
        showContacts = findViewById(R.id.btContacts)

        toolbar = findViewById(R.id.toolBarMain)
        tabLayout = findViewById(R.id.tabLayoutMain)
        viewPager2 = findViewById(R.id.viewPager2Main)
        toolbar.title =  "JIIT QnA"
        setSupportActionBar(toolbar)
        fAuth = FirebaseAuth.getInstance()

        appPagerAdapter = AppPagerAdapter(this)
        viewPager2.adapter = appPagerAdapter

        TabLayoutMediator(tabLayout, viewPager2){
            tab,position->
            tab.text = titles[position]
        }.attach()

        showContacts.setOnClickListener{
            val intent = Intent(this, MenuActivity::class.java)
            intent.putExtra("optionName", "contacts")
            startActivity(intent)
        }
    }

    class AppPagerAdapter(fragmentActivity: FragmentActivity):FragmentStateAdapter(fragmentActivity){
        override fun getItemCount(): Int {
            return 3;
        }

        override fun createFragment(position: Int):Fragment {
            return when(position){
                0->Chats()
                2->Feeds()
                3->Account()
                else->Chats()

            }
        }

    }
//inflate the menu :
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.profile ->{
                val intent = Intent(this@MainActivity,MenuActivity::class.java)
                intent.putExtra("optionName", "profile")
                startActivity(intent)
            }
            R.id.about->{
                val intent = Intent(this@MainActivity,MenuActivity::class.java)
                intent.putExtra("optionName","about")
                startActivity(intent)
            }
            R.id.logout->{
                fAuth.signOut()
                val intent = Intent(this@MainActivity, AuthenticationActivity::class.java)
                startActivity(intent)
                finish()
            }

        }

        return super.onOptionsItemSelected(item)
    }


}