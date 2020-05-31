package com.petapp.capybara.auth

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.petapp.capybara.R
import kotlinx.android.synthetic.main.fragment_auth.*

class AuthFragment : Fragment(R.layout.fragment_auth) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        start_main.setOnClickListener {
            findNavController().navigate(R.id.main)
        }
    }
}