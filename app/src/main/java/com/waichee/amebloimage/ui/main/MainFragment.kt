package com.waichee.amebloimage.ui.main

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.waichee.amebloimage.RC_EXTERNAL_STORAGE
import com.waichee.amebloimage.databinding.MainFragmentBinding
import kotlinx.android.synthetic.main.main_fragment.status_image
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        methodRequiresPermission()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Receive url from intent
        val intentUrl = arguments?.getString("intent_url")

        val binding = MainFragmentBinding.inflate(inflater)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        binding.viewModel = viewModel

        // Set url from intent to editText
        viewModel.inputUrl.value = intentUrl

        binding.recyclerView.adapter = PhotoAdapter()
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.toast.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                viewModel.completeDisplayToast()
            }
        })

        return binding.root
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    @AfterPermissionGranted(RC_EXTERNAL_STORAGE)
    private fun methodRequiresPermission() {
        val perms: String =Manifest.permission.WRITE_EXTERNAL_STORAGE
        if (EasyPermissions.hasPermissions(context!!, perms)) {

        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(
                this, "Please grant the location permission", RC_EXTERNAL_STORAGE, perms
            )
        }
    }
}