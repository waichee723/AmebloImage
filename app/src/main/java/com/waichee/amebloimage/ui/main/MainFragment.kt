package com.waichee.amebloimage.ui.main

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.waichee.amebloimage.R
import com.waichee.amebloimage.databinding.MainFragmentBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

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

}