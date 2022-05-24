package com.example.magmarket.ui.mainactivity

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.magmarket.R
import com.example.magmarket.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private val viewmodel by viewModels<MainActivityViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initpreferences()
        createSplashScreen()
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()

    }
fun initpreferences(){
    lifecycleScope.launch {
        viewmodel.preferences.collect{
            val mode = it.theme.mode
            val currentMode = AppCompatDelegate.getDefaultNightMode()
            if (currentMode != mode) {
                AppCompatDelegate.setDefaultNightMode(mode)
            }
        }
    }
}
    fun init() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment
        navController = navHostFragment.navController
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)

        NavigationUI.setupWithNavController(bottomNavigationView, navController)
    }

    fun createSplashScreen() {
        installSplashScreen().setOnExitAnimationListener { splashScreenView ->
            val alpha = ObjectAnimator.ofFloat(
                splashScreenView.view,
                View.ALPHA,
                1f,
                0f
            )
            alpha.duration = 1000L
            alpha.doOnEnd { splashScreenView.remove() }
            alpha.start()
        }
    }


}