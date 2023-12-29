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

//        if(drawerLayout.isDrawerOpen(Gravity.LEFT)) {
//            drawerLayout.closeDrawers();
//        } else {
//            drawerLayout.openDrawer(Gravity.LEFT)
//        }

        // Display the hamburger icon to launch the drawer
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        binding.txtSignOut.setOnClickListener {
//            CommonMethods.showAlertYesNoMessage(this,"Are you sure you want to sign out"){
//                homeViewModel.sharedPreferences.clearPreference()
//                val intent = Intent(this, Login::class.java)
//                //  val intent = Intent(this, AddRoles::class.java)
//                startActivity(intent)
//                finish()
//            }
//        }

        binding.recyclerView1.adapter = albumDataAdapter
        setObserver()
        homeViewModel.setCallBacks(this)
        homeViewModel.getModuleList()
        val current = CommonMethods.getCurrentDateTime()
        Log.e("Current", "ss $current")

/*
        albumDataAdapter.setItemClick(object : ClickInterface<ModuleData> {
            override fun onClick(data: ModuleData) {
                Log.e("Data", "onClick" + data.name)
                //AlbumDetailActivity.launchActivity(this@ShowAlbumActivity,data)
                when (data.name) {
                    "Organization" -> {
                        val intent = Intent(this@HomeActivity, Organization::class.java)
                        intent.putExtra("mObj", data)
                        startActivity(intent)

                    }

                    "Users", "User Administration" -> {
                        val admin = Intent(this@HomeActivity, Admin::class.java)
                        admin.putExtra("mObj", data)
                        startActivity(admin)

                    }

                    "Leave Management" -> {
                        val intent = Intent(this@HomeActivity, Organization::class.java)
                        intent.putExtra("mObj", data)
                        startActivity(intent)

                    }

                    "Leaves" -> {
                        if (moduleSize == 2 || moduleSize == 3) {
                            val intent =
                                Intent(this@HomeActivity, AllLeavesActivity::class.java)
                            intent.putExtra("mObj", data)
                            startActivity(intent)
                        } else {
                            val intent =
                                Intent(this@HomeActivity, ManagerAuthorisedActivity::class.java)
                            intent.putExtra("mObj", data)
                            intent.putExtra("from", "authLeave")
                            startActivity(intent)
                        }

                    }

                    "Timesheets" -> {

                        if (moduleSize == 2 || moduleSize == 3) {
                            val intent =
                                Intent(this@HomeActivity, TimeSchedulerActivity::class.java)
                            intent.putExtra("mObj", data)
                            startActivity(intent)
                        } else {
                            val intent =
                                Intent(this@HomeActivity, ManagerAuthorisedActivity::class.java)
                            intent.putExtra("mObj", data)
                            intent.putExtra("from", "authTime")
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
        })
*/


       // Create a method to handle item clicks
       fun handleItemClick(data: ModuleData) {
           Log.e("Data", "onClick" + data.name)

           when (data.name) {
               "Organization" -> {
                   val intent = Intent(this@HomeActivity, Organization::class.java)
                   intent.putExtra("mObj", data)
                   startActivity(intent)
               }

               "Users", "User Administration" -> {
                   val admin = Intent(this@HomeActivity, Admin::class.java)
                   admin.putExtra("mObj", data)
                   startActivity(admin)
               }

               "Leave Management" -> {
                   val intent = Intent(this@HomeActivity, Organization::class.java)
                   intent.putExtra("mObj", data)
                   startActivity(intent)
               }

               "Leaves" -> {
                   if (moduleSize == 2 || moduleSize == 3) {
                       val intent = Intent(this@HomeActivity, AllLeavesActivity::class.java)
                       intent.putExtra("mObj", data)
                       startActivity(intent)
                   } else {
                       val intent = Intent(this@HomeActivity, ManagerAuthorisedActivity::class.java)
                       intent.putExtra("mObj", data)
                       intent.putExtra("from", "authLeave")
                       startActivity(intent)
                   }
               }

               "Timesheets" -> {
                   if (moduleSize == 2 || moduleSize == 3) {
                       val intent = Intent(this@HomeActivity, TimeSchedulerActivity::class.java)
                       intent.putExtra("mObj", data)
                       startActivity(intent)
                   } else {
                       val intent = Intent(this@HomeActivity, ManagerAuthorisedActivity::class.java)
                       intent.putExtra("mObj", data)
                       intent.putExtra("from", "authTime")
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

// Set item click listener for albumDataAdapter
        albumDataAdapter.setItemClick(object : ClickInterface<ModuleData> {
            override fun onClick(data: ModuleData) {
                handleItemClick(data)
            }
        })

        binding.bottomNavigation.setOnNavigationItemSelectedListener { menuItem ->
            val itemName = menuItem.title.toString()
            when (itemName) {
                "Organization" -> {
                    handleItemClick(ModuleData(name = "Organization"))
                }
                "Users", "User Administration" -> {
                    handleItemClick(ModuleData(name = "User Administration"))
                }
                "Leave Management" -> {
                    handleItemClick(ModuleData(name = "Leave Management"))
                }
                "Leaves" -> {
                    val data = ModuleData(name = "Leaves")
                    if (moduleSize == 2 || moduleSize == 3) {
                        handleItemClick(data)
                    } else {
                        val intent = Intent(this@HomeActivity, ManagerAuthorisedActivity::class.java)
                        intent.putExtra("mObj", data)
                        intent.putExtra("from", "authLeave")
                        startActivity(intent)
                    }
                }
                "Timesheets" -> {
                    val data = ModuleData(name = "Timesheets")
                    if (moduleSize == 2 || moduleSize == 3) {
                        handleItemClick(data)
                    } else {
                        val intent = Intent(this@HomeActivity, ManagerAuthorisedActivity::class.java)
                        intent.putExtra("mObj", data)
                        intent.putExtra("from", "authTime")
                        startActivity(intent)
                    }
                }
                // Add other cases as needed
                else -> {
                    // Handle default case
                    // You can modify this based on your requirements
                    handleItemClick(ModuleData(name = itemName))
                }
            }

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
            Log.d("TAG", "Module Data Received : ${mList.size}")
            albumDataAdapter.updateAlbumData(mList)

            // Clear existing menu items
            binding.bottomNavigation.menu.clear()

            // Add new menu items based on recyclerView1 data
            for (item in mList) {
                binding.bottomNavigation.menu.add(Menu.NONE, Menu.NONE, Menu.NONE, item.name)
                    /*.setIcon(*//* Set your icon here if needed *//*)*/
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