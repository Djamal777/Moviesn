package com.example.movieapplication.ui.popularmovie

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieapplication.data.api.POSTER_BASE_URL
import com.example.movieapplication.data.repository.NetworkState
import com.example.movieapplication.data.vo.Movie
import com.example.movieapplication.databinding.MovieListItemBinding
import com.example.movieapplication.databinding.NetworkStateItemBinding

class PopularMoviePagingDataAdapter(private val onItemClicked: (Movie) -> Unit):
    PagingDataAdapter<Movie,RecyclerView.ViewHolder>(DiffObj) {

    val MOVIE_VIEW_TYPE=1
    val NETWORK_VIEW_TYPE=2

    private var networkState:NetworkState?=null

    inner class MovieItemViewHolder(private var binding:MovieListItemBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(movie:Movie?){
            binding.cvMovieTitle.text=movie?.title
            binding.cvMovieReleaseDate.text=movie?.releaseDate
            val moviePosterURl= POSTER_BASE_URL+movie?.posterPath
            Glide.with(binding.root.context)
                .load(moviePosterURl)
                .into(binding.cvIvMoviePoster)
            binding.root.setOnClickListener{
                val position=bindingAdapterPosition
                onItemClicked(getItem(position)!!)
            }
        }
    }

    class NetworkStateItemViewHolder(private var binding:NetworkStateItemBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(networkState:NetworkState?){
            if(networkState!=null && networkState== NetworkState.LOADING){
                binding.progressBarItem.visibility=View.VISIBLE
            }
            else{
                binding.progressBarItem.visibility=View.GONE
            }
            if(networkState!=null && networkState== NetworkState.ERROR){
                binding.errorMsgItem.visibility=View.VISIBLE
                binding.errorMsgItem.text=networkState.msg
            }
            else if(networkState!=null && networkState== NetworkState.ENDOFLIST){
                binding.errorMsgItem.visibility=View.VISIBLE
                binding.errorMsgItem.text=networkState.msg
            }
            else{
                binding.errorMsgItem.visibility=View.GONE
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(getItemViewType(position)==MOVIE_VIEW_TYPE){
            (holder as MovieItemViewHolder).bind(getItem(position))
        }
        else{
            (holder as NetworkStateItemViewHolder).bind(networkState)
        }
    }

    private fun hasExtraRow():Boolean{
        return networkState!=null && networkState!= NetworkState.LOADED
    }

    override fun getItemCount():Int{
        return super.getItemCount()+if(hasExtraRow()) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        return if(hasExtraRow() && position==itemCount-1){
            NETWORK_VIEW_TYPE
        }
        else{
            MOVIE_VIEW_TYPE
        }
    }

    fun setNetworkState(newNetworkState: NetworkState){
        val previousState=this.networkState
        val hadExtraRow=hasExtraRow()
        this.networkState=newNetworkState
        val hasExtraRow=hasExtraRow()

        if(hadExtraRow!=hasExtraRow){
            if(hadExtraRow){
                notifyItemRemoved(super.getItemCount())
            }
            else{
                notifyItemInserted(super.getItemCount())
            }
        }else if(hasExtraRow && previousState!=newNetworkState){
            notifyItemChanged(itemCount-1)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == MOVIE_VIEW_TYPE) {
            return MovieItemViewHolder(MovieListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
        } else {
            return NetworkStateItemViewHolder(NetworkStateItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
        }
    }

    companion object DiffObj:DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id==newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem==newItem
        }
    }
}