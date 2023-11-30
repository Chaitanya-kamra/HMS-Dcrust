package com.chaitanya.hms_dcrust.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.chaitanya.hms_dcrust.R
import com.chaitanya.hms_dcrust.databinding.ActivityMainBinding
import com.chaitanya.hms_dcrust.viewModel.HostelViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    val viewModel: HostelViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigation.itemIconTintList = null

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNavigation.setupWithNavController(navController)

        binding.bottomNavigation.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> navController.navigate(R.id.homeFragment)
                R.id.fees -> navController.navigate(R.id.feesFragment)
                R.id.complain -> navController.navigate(R.id.complainFragment)
                R.id.chat -> navController.navigate(R.id.chatFragment)
                R.id.profile -> navController.navigate(R.id.profileFragment)
            }
            true
        }
        navController.addOnDestinationChangedListener { _, destination, _ ->

            if (destination.id == R.id.loginFragment) {
                binding.bottomNavigation.visibility = View.GONE
            } else {
                binding.bottomNavigation.visibility = View.VISIBLE
            }

            when (destination.id) {
                R.id.homeFragment -> binding.bottomNavigation.menu.findItem(R.id.home)?.isChecked =
                    true
                R.id.feesFragment -> binding.bottomNavigation.menu.findItem(R.id.fees)?.isChecked =
                    true
                R.id.complainFragment -> binding.bottomNavigation.menu.findItem(R.id.complain)?.isChecked =
                    true
                R.id.chatFragment -> binding.bottomNavigation.menu.findItem(R.id.chat)?.isChecked =
                    true
                R.id.profileFragment -> binding.bottomNavigation.menu.findItem(R.id.profile)?.isChecked =
                    true
            }
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}