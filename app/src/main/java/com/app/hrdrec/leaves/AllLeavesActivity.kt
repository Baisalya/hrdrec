package com.app.hrdrec.leaves

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.app.hrdrec.R
import com.app.hrdrec.databinding.ActivityAllLeavesBinding
import com.app.hrdrec.leaves.response.LeaveModel
import com.app.hrdrec.utils.CommonInterface
import com.app.hrdrec.utils.CommonMethods
import com.app.hrdrec.utils.getDate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllLeavesActivity : Fragment(), CommonInterface,
    AllLeaveListAdapter.FeatureCallBack, View.OnClickListener {

    private val viewModel: LeavesViewModel by viewModels()
    private var productFeatureAdapter: AllLeaveListAdapter? = null

    private var _binding: ActivityAllLeavesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ActivityAllLeavesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setCallBacks(this)
        setObserver()
        binding.headerMain.txtTitle.text = "Leaves"
        viewModel.getAllLeaves()

        setUpListener()
    }

    private fun setUpListener() {
        binding.apply {
            addLeaves.setOnClickListener(this@AllLeavesActivity)
            headerMain.imgBackBtn.setOnClickListener(this@AllLeavesActivity)
            conFromDate.setOnClickListener(this@AllLeavesActivity)
            conToDate.setOnClickListener(this@AllLeavesActivity)
            btnSubmit.setOnClickListener(this@AllLeavesActivity)
        }
    }

    private fun setObserver() {
        viewModel.myResponseList.observe(viewLifecycleOwner) {
            Log.e("Data", "herrrr" + it.data.size)

            binding.recyclerLeaves.visibility = View.VISIBLE
            binding.txtNoData.visibility = View.GONE
            productFeatureAdapter = AllLeaveListAdapter(it.data, this)
            binding.recyclerLeaves.adapter = productFeatureAdapter
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.addLeaves -> {
                val intent = Intent(requireContext(), AddLeaveActivity::class.java)
                intent.putExtra("from", "add")
                resultLauncher.launch(intent)
            }

            R.id.img_back_btn -> {
                // Handle back button click
            }

            R.id.conFromDate -> {
                // Handle conFromDate click
            }

            R.id.conToDate -> {
                // Handle conToDate click
            }

            R.id.btnSubmit -> {
                // Handle btnSubmit click
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onErrorMessage(message: String) {
        // Handle error message
    }

    override fun onResponseSuccess() {
        // Handle response success
    }

    override fun noListData() {
        binding.recyclerLeaves.visibility = View.GONE
        binding.txtNoData.visibility = View.VISIBLE
    }

    override fun showLoader() {
        // Show loader
    }

    override fun hideLoader() {
        // Hide loader
    }

    override fun selectedItem(week: LeaveModel) {
        val intent = Intent(requireContext(), AddLeaveActivity::class.java)
        intent.putExtra("from", "edit")
        intent.putExtra("mObj", week)
        resultLauncher.launch(intent)
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == 121) {
                viewModel.getAllLeaves()
            } else {
                Log.e("Location Result", "Location Is Not Fetched")
            }
        }
}