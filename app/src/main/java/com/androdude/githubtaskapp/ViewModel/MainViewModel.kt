package com.androdude.githubtaskapp.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androdude.githubtaskapp.DATA.Model.CommitDetailResponse
import com.androdude.githubtaskapp.DATA.Model.GithubResponse
import com.androdude.githubtaskapp.DATA.Repository
import com.androdude.githubtaskapp.Resources
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel(val repository: Repository) : ViewModel()
{
    val commitDetails : MutableLiveData<Resources<GithubResponse>> = MutableLiveData()
    val userCommitDetails : MutableLiveData<Resources<CommitDetailResponse>> = MutableLiveData()



    fun getAllCommitDetails() {
        viewModelScope.launch {
            commitDetails.postValue(Resources.Loading())
            val response = repository.getCommitDetails()
            commitDetails.postValue(handleRepoResponse(response))
        }
    }

    fun getUserCommitDetails(sha : String)
    {
        viewModelScope.launch {
            userCommitDetails.postValue(Resources.Loading())
            val response = repository.getUserCommitDetails(sha)
            userCommitDetails.postValue(handleCommitResponse(response))
        }
    }

    private fun handleRepoResponse(response: Response<GithubResponse>) : Resources<GithubResponse> {
        if(response.isSuccessful)
        {
            response.body()?.let {
                return Resources.Success(it)
            }
        }

        return Resources.Error(response.message())
    }

    private fun handleCommitResponse(response: Response<CommitDetailResponse>) : Resources<CommitDetailResponse> {
        if(response.isSuccessful)
        {
            response.body()?.let {
                return Resources.Success(it)
            }
        }

        return Resources.Error(response.message())
    }


}