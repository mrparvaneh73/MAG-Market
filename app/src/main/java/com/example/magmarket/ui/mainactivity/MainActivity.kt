package com.example.magmarket.ui.mainactivity

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.magmarket.R
import com.example.magmarket.databinding.ActivityMainBinding
import com.example.magmarket.utils.ConnectivityStatus
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ActivityContext
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var networkConnection : ConnectivityStatus
    private val mainViewModel by viewModels<MainActivityViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initPreferences()
        createSplashScreen()
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        checkConnection()

    }

    private fun initPreferences() {
        lifecycleScope.launch {
            mainViewModel.preferences.collect {
                val mode = it.theme.mode
                val currentMode = AppCompatDelegate.getDefaultNightMode()
                if (currentMode != mode) {
                    AppCompatDelegate.setDefaultNightMode(mode)
                }
            }
        }
    }

    private fun init() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment
        navController = navHostFragment.navController
        bottomNavigationView=findViewById(R.id.bottom_nav)
        bottomNavigationView.setOnItemSelectedListener { item ->

            NavigationUI.onNavDestinationSelected(item, navController)

            return@setOnItemSelectedListener true
        }
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.cartFragment -> visibleBottomNavigation()
                R.id.homeFragment -> visibleBottomNavigation()
                R.id.categoryFragment -> visibleBottomNavigation()
                R.id.userFragment -> visibleBottomNavigation()
                else -> hideBottomNavigation()
            }

        }

    }

    private fun createSplashScreen() {
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

   private fun visibleBottomNavigation() {
        bottomNavigationView.visibility = View.VISIBLE
    }

  private  fun hideBottomNavigation() {
        bottomNavigationView.visibility = View.GONE
    }

    private fun checkConnection() {
        networkConnection= ConnectivityStatus(context = this)
        networkConnection.observe(this) { isConnected ->
            if (isConnected == true) {
                binding.container.isVisible = true
                binding.bottomNav.isVisible = true
                binding.connection.connection.isVisible = false
            } else {
                binding.bottomNav.isVisible = false
                binding.container.isVisible = false
                binding.connection.connection.isVisible = true
            }
        }
        binding.connection.btnTryagain.setOnClickListener {

        }
    }

}