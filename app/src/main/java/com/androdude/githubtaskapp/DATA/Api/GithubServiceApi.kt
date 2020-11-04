package com.androdude.githubtaskapp.DATA.Api

import com.androdude.githubtaskapp.DATA.Model.CommitDetailResponse
import com.androdude.githubtaskapp.DATA.Model.GithubResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface GithubServiceApi {

    @GET("commits")
    suspend fun getCommitDetails() : Response<GithubResponse>

    @GET("commits/{sha}")
    suspend fun getUserCommitDetails(@Path("sha")
                                         sha : String) : Response<CommitDetailResponse>

}