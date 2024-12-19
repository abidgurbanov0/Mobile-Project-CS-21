package com.example.muricerr

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.muricerr.view.AllQuiz
import com.example.muricerr.view.FouvaritsFragment
import com.example.muricerr.view.PlaylistFragment
import com.example.muricerr.view.SearchFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)

        val searchFragment = SearchFragment()

        makeCurrentFragment(searchFragment)

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.search -> makeCurrentFragment(SearchFragment())
                R.id.playlist -> makeCurrentFragment(PlaylistFragment())
                R.id.favorites -> makeCurrentFragment(FouvaritsFragment())
                R.id.quiz -> makeCurrentFragment(AllQuiz())

            }
            true
        }
    }

    private fun makeCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_layout,fragment)
            commit()
        }
    }

}