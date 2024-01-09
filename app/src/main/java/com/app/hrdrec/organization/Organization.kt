package com.app.hrdrec.organization

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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
class OrganizationFragment : Fragment() {

    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var mObj: ModuleData
    private lateinit var binding: ActivityOrganizationBinding

    @Inject
    lateinit var albumDataAdapter: OrganizationDataAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ActivityOrganizationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mObj = arguments?.getSerializable("mObj") as ModuleData

        val emptyDataTextView: TextView = binding.root.findViewById(R.id.textViewEmptyData)

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
                    Log.e("Data", "org" + data.name)
                    //AlbumDetailActivity.launchActivity(this@ShowAlbumActivity,data)
                    when (data.name) {

                        "Locations" -> {
                            val intent = Intent(requireContext(), Location::class.java)
                            intent.putExtra("id", mObj.id)
                            startActivity(intent)
                        }

                        "Holiday Calendars" -> {
                            val admin = Intent(requireContext(), HolidayCalendar::class.java)
                            admin.putExtra("mObj", data)
                            startActivity(admin)
                        }

                        "Clients" -> {
                            val intent = Intent(requireContext(), Clients::class.java)
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
