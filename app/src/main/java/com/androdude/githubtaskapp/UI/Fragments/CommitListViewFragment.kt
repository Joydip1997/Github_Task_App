package com.androdude.githubtaskapp.UI.Fragments


import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.androdude.githubtaskapp.DATA.Model.GithubResponse
import com.androdude.githubtaskapp.DATA.Model.GithubResponseItem
import com.androdude.githubtaskapp.R
import com.androdude.githubtaskapp.Resources
import com.androdude.githubtaskapp.UI.Adapters.UserListAdapter
import com.androdude.githubtaskapp.UI.Adapters.UserListAdapter.*
import com.androdude.githubtaskapp.UI.BaseFragment
import com.androdude.githubtaskapp.UI.MainActivity
import com.androdude.githubtaskapp.ViewModel.MainViewModel
import com.androdude.githubtaskapp.databinding.FragmentCommitListViewBinding


class CommitListViewFragment : BaseFragment<MainViewModel, FragmentCommitListViewBinding>() {

    val TAG = "com.androdude.githubtaskapp.UI.Fragments"
    private lateinit var mUserListAdapter: UserListAdapter
    private lateinit var mGithubResponseItem: ArrayList<GithubResponseItem>

    override fun getFragmentView() = R.layout.fragment_commit_list_view
    override fun getViewModel(): Class<MainViewModel> = MainViewModel::class.java

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = (activity as MainActivity).mainViewModel
        mUserListAdapter = UserListAdapter().apply {
            setOnItemClickListener {githubResponseItem->
                val bundle = Bundle()
                bundle.putSerializable("ITEM",githubResponseItem)
                findNavController().navigate(R.id.action_commitListViewFragment_to_commitDetailsFragment,bundle)
            }
        }
        mGithubResponseItem = ArrayList()
        viewModel!!.commitDetails.observe(viewLifecycleOwner, Observer { resources ->
            when (resources) {
                is Resources.Loading -> {
                    Log.i(TAG, "Loading")
                    showProgressbar()
                }

                is Resources.Success -> {
                    resources.data?.let { githubResponse ->
                        githubResponse.forEach()
                        { githubResponseItem ->
                            mGithubResponseItem.add(githubResponseItem)
                        }
                     mUserListAdapter.differ.submitList(githubResponse)
                        hideProgressBar()
                    }
                }

                is Resources.Error -> {
                    Log.i(TAG, "Error")
                    hideProgressBar()
                }
            }
        })

        binding.commiterListView.apply {
            adapter=mUserListAdapter
        }




    }

    private fun hideProgressBar() {
        binding.pgWaiting.visibility = View.GONE
        binding.commiterListView.visibility = View.VISIBLE
    }

    private fun showProgressbar() {
        binding.pgWaiting.visibility = View.VISIBLE
        binding.commiterListView.visibility = View.GONE
    }


}