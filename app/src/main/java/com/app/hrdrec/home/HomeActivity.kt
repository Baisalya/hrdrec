package com.app.hrdrec.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.app.hrdrec.R
import com.app.hrdrec.admin.Admin
import com.app.hrdrec.databinding.ActivityHomeBinding
import com.app.hrdrec.home.getallmodules.ModuleData
import com.app.hrdrec.home.getallmodules.Paths
import com.app.hrdrec.leaves.AllLeavesActivity
import com.app.hrdrec.login.Login
import com.app.hrdrec.manager.ManagerAuthorisedActivity
import com.app.hrdrec.organization.Organization
import com.app.hrdrec.timesheet.TimeSchedulerActivity
import com.app.hrdrec.utils.CommonMethods
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class HomeActivity : AppCompatActivity(), HomeViewModel.CallBackLogin {



    @Inject
    lateinit var albumDataAdapter: ModuleDataAdapter

    var moduleSize: Int = 0
    private val homeViewModel: HomeViewModel by viewModels()

    private lateinit var actionBarToggle: ActionBarDrawerToggle

    private val binding by lazy { ActivityHomeBinding.inflate(layoutInflater) }

    private fun handleTabSelection(tab: TabLayout.Tab) {
        val selectedModule = homeViewModel.moduleData.value?.get(tab.position)
        selectedModule?.let { module ->
            handleItemClick(module)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val tabLayout: TabLayout = findViewById(R.id.tabLayout)
        val frameContainer: FrameLayout = findViewById(R.id.frameContainer)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
//        val navRecyclerView: RecyclerView = findViewById(R.id.recyclerView1)
        actionBarToggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, 0, 0)
        drawerLayout.addDrawerListener(actionBarToggle)
        actionBarToggle.syncState()
        toolbar.setNavigationOnClickListener {
            if (drawerLayout.isDrawerOpen(GravityCompat.END)) drawerLayout.closeDrawer(
                GravityCompat.END
            ) else drawerLayout.openDrawer(GravityCompat.END)
        }


        binding.recyclerView1.adapter = albumDataAdapter
        setObserver()
        homeViewModel.setCallBacks(this)
        homeViewModel.getModuleList()
        val current = CommonMethods.getCurrentDateTime()
        Log.e("Current", "ss $current")


        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    // Call the handleTabSelection method only when a tab is explicitly clicked
                    handleTabSelection(it)
                }

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // Handle unselected tabs if needed
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // Handle reselected tabs if needed
            }
        })
        if (binding.tabLayout.selectedTabPosition == TabLayout.Tab.INVALID_POSITION) {
            // Select your desired default tab here, for example, the first tab
            binding.tabLayout.getTabAt(0)?.select()
        }
// albumDataAdapter
        albumDataAdapter.setItemClick(object : ClickInterface<ModuleData> {
            override fun onClick(data: ModuleData) {
                handleItemClick(data)
                closeDrawer()

            }
        })


    }
    private fun handleItemClick(data: ModuleData) {
        Log.e("Data", "onClick" + data.name)

        // Find the index of the clicked module in the list
        val tabIndex = homeViewModel.moduleData.value?.indexOf(data) ?: -1

        // If the module is found, select the corresponding tab
        if (tabIndex != -1) {
            binding.tabLayout.getTabAt(tabIndex)?.select()
        }

        // Handle specific actions based on the clicked item
        when (data.name) {
            "Organization" -> {
                // Create the OrganizationFragment and pass the data
                val organizationFragment = Organization().apply {
                    arguments = Bundle().apply {
                        putSerializable("mObj", data)
                    }
                }

                // Replace the current fragment with OrganizationFragment
                supportFragmentManager.beginTransaction()
                    .replace(R.id.frameContainer, organizationFragment)
                    .commit()
            }
            "User Administration" -> {
                val admin = Intent(this@HomeActivity, Admin::class.java)
                admin.putExtra("mObj", data)
                startActivity(admin)
            }
            "Leave Management", "Leaves", "Timesheets" -> {
                if ((moduleSize == 2 || moduleSize == 3) &&
                    (data.name == "Leaves" || data.name == "Timesheets")
                ) {
                    // Create the appropriate fragment and replace the current fragment
                    val fragment = if (data.name == "Leaves") AllLeavesActivity() else TimeSchedulerActivity()

                    // Pass the data to the fragment
                    val bundle = Bundle().apply {
                        putSerializable("mObj", data)
                    }
                    fragment.arguments = bundle

                    // Replace the current fragment with the selected fragment
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameContainer, fragment)
                        .commit()
                } else {
                    // Create ManagerAuthorisedFragment and pass data
                    val fragment = ManagerAuthorisedActivity().apply {
                        arguments = Bundle().apply {
                            putSerializable("mObj", data)
                            putString("from", if (data.name == "Leaves") "authLeave" else "authTime")
                        }

                    }
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameContainer, fragment)
                        .commit()
                }
            }
            else -> {
                // Create the OrganizationFragment and pass the data
                val organizationFragment = Organization().apply {
                    arguments = Bundle().apply {
                        putSerializable("mObj", data)
                    }
                }
                // Replace the current fragment with OrganizationFragment
                supportFragmentManager.beginTransaction()
                    .replace(R.id.frameContainer, organizationFragment)
                    .commit()
            }
        }
    }

    private fun closeDrawer() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.END)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.logmenu, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId)
        {
            R.id.logout -> {
                CommonMethods.showAlertYesNoMessage(this,"Are you sure you want to sign out"){
                    homeViewModel.sharedPreferences.clearPreference()
                    val intent = Intent(this, Login::class.java)
                    startActivity(intent)
                    finish()
                }
                // Close the drawer after performing the action
                val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
                drawerLayout.closeDrawer(GravityCompat.END)
            }
        }

        return true

    }

    private fun setObserver() {
        homeViewModel.moduleData.observe(this) { mList ->
            moduleSize = mList.size
            Log.d("TAG", "Module Data Received: ${mList.size}")

            // Clear existing tabs
            binding.tabLayout.removeAllTabs()

            // Add tabs dynamically
            for (module in mList) {
                val tab = binding.tabLayout.newTab()
                tab.text = module.name // A
                // Set icons based on the module name
                when (module.name) {
                    "Organization" -> tab.setIcon(R.drawable.ic_organization)
                    "User Administration" -> tab.setIcon(R.drawable.ic_users)
                    "Employees" -> tab.setIcon(R.drawable.ic_employees)
                    "Users" -> tab.setIcon(R.drawable.ic_users)
                    "Leave Management", "Leaves", "Timesheets" -> tab.setIcon(R.drawable.ic_leaves)
                    // Add more cases for other module names if needed
                    else -> tab.setIcon(R.drawable.ic_more)
                }
                binding.tabLayout.addTab(tab)

            }

            // Update album and drawer adapters
            albumDataAdapter.updateAlbumData(mList)
        }
    }

    override fun onErrorMessage(message: String) {

    }
    override fun showLoader() {
        CommonMethods.showLoader(this)
    }
    override fun hideLoader() {
        CommonMethods.hideLoader()
    }
}
