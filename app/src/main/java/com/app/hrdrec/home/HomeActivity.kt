package com.app.hrdrec.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

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


fun handleItemClick(data: ModuleData) {
    Log.e("Data", "onClick" + data.name)

    when (data.name) {
        "Organization" -> {
            val intent = Intent(this@HomeActivity, Organization::class.java)
            intent.putExtra("mObj", data)
            startActivity(intent)
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
                val intent = Intent(
                    this@HomeActivity,
                    if (data.name == "Leaves") AllLeavesActivity::class.java else TimeSchedulerActivity::class.java
                )
                intent.putExtra("mObj", data)
                startActivity(intent)
            } else {
                val intent = Intent(this@HomeActivity, ManagerAuthorisedActivity::class.java)
                intent.putExtra("mObj", data)
                intent.putExtra("from", if (data.name == "Leaves") "authLeave" else "authTime")
                startActivity(intent)
            }
        }
        else -> {
            val intent = Intent(this@HomeActivity, Organization::class.java)
            intent.putExtra("mObj", data)
            startActivity(intent)
        }
    }
}

// albumDataAdapter
        albumDataAdapter.setItemClick(object : ClickInterface<ModuleData> {
            override fun onClick(data: ModuleData) {
                handleItemClick(data)
            }
        })

//  bottomNavigation
        binding.bottomNavigation.setOnNavigationItemSelectedListener { menuItem ->
            val itemName = menuItem.title.toString()

            val data = when (itemName) {
                "Organization" -> {
                    val organizationPaths = listOf(
                        Paths(3, "Organization Profile", false, "/organizationProfiles"),
                        Paths(4, "Locations", false, "/locationList"),
                        Paths(5, "Holiday Calendars", false, "/holidayCalendarList"),
                        Paths(6, "Leave Types", false, "/leaveTypeList"),
                        Paths(7, "Email Templates", false, "/emailTemplateList"),
                        Paths(10, "Clients", false, "/clientList"),
                        Paths(11, "Projects", false, "/projectList"),
                        Paths(18, "Expense Categories", false, "/expenses")
                    )
                    ModuleData(id = 1, name = itemName, description = "This is organization module", paths = ArrayList(organizationPaths))
                }
                "Users" -> {
                    val usersPaths = listOf(
                        Paths(12, "Roles", false, "/roleList"),
                        Paths(13, "Users", false, "/userList")
                    )
                    ModuleData(id = 2, name = itemName, description = "This is User administration module", paths = ArrayList(usersPaths))
                }
                "Employees" -> {
                    val employeesPaths = listOf(
                        Paths(8, "Employees", false, "/employeeList"),
                        Paths(9, "Leave Balances", false, "/leaveBalances")
                    )
                    ModuleData(id = 3, name = itemName, description = "This is Employees module", paths = ArrayList(employeesPaths))
                }
                // Add other cases as needed
                else -> ModuleData(name = itemName)
            }

            handleItemClick(data)  // This line is calling handleItemClick for bottomNavigation item

            true
        }

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
            }
        }

        return true

    }

    private fun setObserver() {
        homeViewModel.moduleData.observe(this) { mList ->
            moduleSize = mList.size
            Log.d("TAG", "Module Data Received: ${mList.size}")
            albumDataAdapter.updateAlbumData(mList)

            binding.bottomNavigation.menu.clear()

            for (item in mList) {
                val menuItem = binding.bottomNavigation.menu.add(Menu.NONE, Menu.NONE, Menu.NONE, item.name)
                when (item.name) {
                    "Organization" -> menuItem.setIcon(R.drawable.ic_organization)
                    "Users" -> menuItem.setIcon(R.drawable.ic_users)
                    "Employees" -> menuItem.setIcon(R.drawable.ic_employees)
                    "Leaves" -> menuItem.setIcon(R.drawable.ic_leaves)
                }



            }
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