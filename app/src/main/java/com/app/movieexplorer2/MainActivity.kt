package com.app.movieexplorer2

import android.content.Context
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.app.movieexplorer2.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            v.updatePadding(top = bars.top)
            insets
        }

        val navHost = supportFragmentManager
            .findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController = navHost.navController

        if (savedInstanceState == null) {
            val prefs = getSharedPreferences(
                "auth_session",
                Context.MODE_PRIVATE
            )

            val accessToken = prefs.getString("access_token", null)
            val hasApiSession = !accessToken.isNullOrBlank()
            val hasFirebaseSession =
                FirebaseAuth.getInstance().currentUser != null

            val startDestination = if (
                hasApiSession || hasFirebaseSession
            ) {
                R.id.homeFragment
            } else {
                R.id.onboardingFragment
            }

            val graph = navController.navInflater
                .inflate(R.navigation.nav_graph)

            graph.setStartDestination(startDestination)
            navController.graph = graph
        }

        // Bottom bar drives navigation (item IDs match the nav graph IDs)
        binding.bottomNav.setupWithNavController(navController)

        val topLevel = setOf(
            R.id.homeFragment,
            R.id.searchFragment,
            R.id.favoritesFragment,
            R.id.profileFragment,
        )

        navController.addOnDestinationChangedListener { _, destination, _ ->
            val isTopLevel = destination.id in topLevel
            binding.bottomNav.isVisible = isTopLevel      // hidden on detail
            binding.bottomDivider.isVisible = isTopLevel
            binding.topAppBar.isVisible = destination.id == R.id.homeFragment
        }
    }
}

