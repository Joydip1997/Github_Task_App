package com.androdude.githubtaskapp.DATA.Model

import java.io.Serializable

data class GithubResponseItem(
    val author: Author,
    val comments_url: String,
    val commit: Commit,
    val committer: CommitterX,
    val html_url: String,
    val node_id: String,
    val parents: List<Parent>,
    val sha: String,
    val url: String
) : Serializable