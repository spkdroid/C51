package com.check.coupon.page

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.check.coupon.R
import com.check.coupon.viewmodel.SplashViewModel
import kotlinx.android.synthetic.main.main_fragment.*

class SplashFragment : Fragment() {

    companion object {
        fun newInstance() = SplashFragment()
    }

    private lateinit var viewModel: SplashViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SplashViewModel::class.java)
        // TODO: Use the ViewModel
        LoginTextButton.setOnClickListener{
            viewModel.initializeRepository()
            loginProgressBar.visibility = View.VISIBLE
        }

        viewModel.appReady.observe(viewLifecycleOwner, Observer {
            if(it) {
                Navigation.findNavController(this.requireView())
                    .navigate(R.id.action_splashFragment_to_hubFragment)
            }
        })
    }
}