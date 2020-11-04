package com.androdude.githubtaskapp.UI.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.androdude.githubtaskapp.DATA.Model.GithubResponseItem
import com.androdude.githubtaskapp.R
import com.androdude.githubtaskapp.UI.Adapters.UserListAdapter.*
import kotlinx.android.synthetic.main.item_commit_preview.view.*

class UserListAdapter : RecyclerView.Adapter<mUserViewModel>() {


    inner class mUserViewModel(itemview : View) : RecyclerView.ViewHolder(itemview)

    private val differCallBack = object : DiffUtil.ItemCallback<GithubResponseItem>()
    {
        override fun areItemsTheSame(oldItem: GithubResponseItem, newItem: GithubResponseItem): Boolean {
            return oldItem.sha == newItem.sha
        }

        override fun areContentsTheSame(oldItem: GithubResponseItem, newItem: GithubResponseItem): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this,differCallBack)





    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): mUserViewModel {
        return mUserViewModel(LayoutInflater.from(parent.context).inflate(R.layout.item_commit_preview,parent,false))
    }

    override fun onBindViewHolder(holder: mUserViewModel, position: Int) {
    val current = differ.currentList[position]

        holder.itemView.apply {
            tvPersonName.text = current.commit.author.name
            tvPersonCommit.text = current.sha
            tvPersonCommitMessage.text = current.commit.message
            setOnClickListener {
                onItemClickListener?.let {
                    it(current)
                }
            }
        }
    }

    override fun getItemCount(): Int = differ.currentList.size




    private var onItemClickListener : ((GithubResponseItem) -> Unit)?=null
    fun setOnItemClickListener(listener : (GithubResponseItem)->Unit){
        onItemClickListener=listener
    }




}