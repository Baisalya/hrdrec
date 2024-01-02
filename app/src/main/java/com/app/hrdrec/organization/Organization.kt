package com.app.hrdrec.organization

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.app.hrdrec.R
import com.app.hrdrec.databinding.ActivityOrganizationBinding
import com.app.hrdrec.home.HomeViewModel
import com.app.hrdrec.home.getallmodules.ModuleData
import com.app.hrdrec.home.getallmodules.Paths
import com.app.hrdrec.organization.clients.Clients
import com.app.hrdrec.organization.employees.Employees
import com.app.hrdrec.organization.holidaycalander.HolidayCalendar
import com.app.hrdrec.organization.locations.Location
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class Organization : AppCompatActivity() {

    private val homeViewModel: HomeViewModel by viewModels()

    lateinit var mObj: ModuleData
    private val binding by lazy { ActivityOrganizationBinding.inflate(layoutInflater) }

    @Inject
    lateinit var albumDataAdapter: OrganizationDataAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        mObj = intent.getSerializableExtra("mObj") as ModuleData

        val emptyDataTextView: TextView = findViewById(R.id.textViewEmptyData)

        if (mObj.paths.isEmpty()) {
            emptyDataTextView.visibility = View.VISIBLE
        } else {
            // Hide the empty data message
            emptyDataTextView.visibility = View.GONE
            // Update the adapter with non-empty data
            binding.recyclerView.adapter = albumDataAdapter
            albumDataAdapter.updateAlbumData(mObj.paths)

            albumDataAdapter.setItemClick(object : ClickInterfaceOrgan<Paths> {
                override fun onClick(data: Paths) {
                    Log.e("Data","org"+data.name)
                    //AlbumDetailActivity.launchActivity(this@ShowAlbumActivity,data)
                    when (data.name) {

                        "Locations" -> {
                            val intent = Intent(this@Organization, Location::class.java)
                            intent.putExtra("id", mObj.id)
                            startActivity(intent)

                        }

                        "Holiday Calendars" -> {
                            val admin = Intent(this@Organization, HolidayCalendar::class.java)
                            intent.putExtra("mObj", data)
                            startActivity(admin)

                        }

                        "Clients" -> {
                            val intent = Intent(this@Organization, Clients::class.java)
                            intent.putExtra("id", mObj.id)
                            startActivity(intent)

                        }

                        else -> {

                        }
                    }
                }
            })
        }
    }
}





