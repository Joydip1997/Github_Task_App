package com.androdude.githubtaskapp.UI.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.androdude.githubtaskapp.DATA.Model.File
import com.androdude.githubtaskapp.DATA.Model.GithubResponseItem
import com.androdude.githubtaskapp.R
import com.androdude.githubtaskapp.Resources
import com.androdude.githubtaskapp.UI.BaseFragment
import com.androdude.githubtaskapp.UI.MainActivity
import com.androdude.githubtaskapp.ViewModel.MainViewModel
import com.androdude.githubtaskapp.databinding.FragmentCommitDetailsBinding

class CommitDetailsFragment : BaseFragment<MainViewModel,FragmentCommitDetailsBinding>()
{

    val TAG = "com.androdude.githubtaskapp.UI.Fragments"

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = (activity as MainActivity).mainViewModel



        val data = arguments?.getSerializable("ITEM") as GithubResponseItem
        val sha = data.sha
        viewModel?.let {viewmodel->
            viewmodel.getUserCommitDetails(sha)
        }

        viewModel?.userCommitDetails!!.observe(viewLifecycleOwner, Observer {resources->
            when (resources) {
                is Resources.Loading -> {
                    Log.i(TAG, "Loading")
                    showProgressbar()
                }

                is Resources.Success -> {
                    hideProgressBar()
                    resources.data?.let { commitResponse ->
                        binding.tvPersonName.text = "Name : "+commitResponse.commit.author.name
                        binding.tvCommitMessage.text = "Message : "+commitResponse.commit.message
                        binding.tvCommitDate.text = "Date : "+commitResponse.commit.author.date
                        binding.tvNumberOfFilesAdded.text = "Files Deleted : "+commitResponse.stats.additions
                        binding.tvNumberOfFilesDeleted.text = "Files Added : "+commitResponse.stats.deletions
                        binding.tvSha.text = "Sha-1 : "+commitResponse.sha
                        binding.tvTotalNumberOfFilesChanged.text =  "Total Number Of changes : "+commitResponse.stats.total.toString() // Fix it
                    }
                }

                is Resources.Error -> {
                    Log.i(TAG, "Error")
                    hideProgressBar()
                }
            }
        })

    }

    private fun hideProgressBar() {
        binding.pgWaiting.visibility = View.GONE
        binding.liLayout.visibility = View.VISIBLE
    }

    private fun showProgressbar() {
        binding.pgWaiting.visibility = View.VISIBLE
        binding.liLayout.visibility = View.GONE
    }

    override fun getFragmentView() = R.layout.fragment_commit_details
    override fun getViewModel() = MainViewModel::class.java

}