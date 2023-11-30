package com.chaitanya.hms_dcrust.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.chaitanya.hms_dcrust.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FeesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fees, container, false)
    }

}