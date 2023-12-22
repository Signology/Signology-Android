package com.adem.signology.view

import android.content.Intent
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.adem.signology.R
import com.adem.signology.databinding.ActivityRootBinding
import com.adem.signology.view.ui.camera.CameraActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class RootActivity : AppCompatActivity() {

private lateinit var binding: ActivityRootBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

     binding = ActivityRootBinding.inflate(layoutInflater)
     setContentView(binding.root)

        val navView: BottomNavigationView = binding.bottomNavMenu
        navView.menu.getItem(1).isEnabled = false

        val navController = findNavController(R.id.nav_host_fragment_activity_root)

        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.navigation_home, R.id.navigation_camera, R.id.navigation_profile))
        supportActionBar?.hide()

        val fabTranslate: FloatingActionButton = binding.fabTranslate
        fabTranslate.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            startActivity(intent)
        }


        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_activity_root)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}