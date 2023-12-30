package uz.frodo.trafficpro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class BaseActivity : AppCompatActivity() {
    lateinit var bottomNav:BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)

        setSupportActionBar(findViewById(R.id.toolbar))
        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.menu_home, R.id.menu_liked, R.id.menu_info)
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        bottomNav.setupWithNavController(navController)


    }
}