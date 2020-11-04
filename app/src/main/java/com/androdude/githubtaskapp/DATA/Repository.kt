package com.androdude.githubtaskapp.DATA

import com.androdude.githubtaskapp.DATA.Api.RetrofitInstance

class Repository
{
    suspend fun getCommitDetails()  = RetrofitInstance.api.getCommitDetails()
    suspend fun getUserCommitDetails(sha : String)  = RetrofitInstance.api.getUserCommitDetails(sha)

}