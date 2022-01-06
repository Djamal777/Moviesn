package com.example.movieapplication.ui.popularmovie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.movieapplication.data.api.TheMovieDBClient
import com.example.movieapplication.data.api.TheMovieDBInterface
import com.example.movieapplication.data.repository.MovieDataSource
import com.example.movieapplication.data.repository.NetworkState
import com.example.movieapplication.databinding.PopularMovieFragmentBinding
import io.reactivex.rxjava3.disposables.CompositeDisposable

class PopularMovieFragment: Fragment() {


    lateinit var movieRepository: MoviePagingDataRepository
    private lateinit var binding: PopularMovieFragmentBinding
    private val viewModel: PopularMovieViewModel by viewModels{
        PopularMovieViewModelFactory(movieRepository)
    }
    private val compositeDisposable=CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= PopularMovieFragmentBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val apiService: TheMovieDBInterface = TheMovieDBClient.getClient()
        val movieDataSource=MovieDataSource(apiService)
        movieRepository= MoviePagingDataRepository(movieDataSource)

        val popularMoviePagingDataAdapter= PopularMoviePagingDataAdapter{
            val action =
                PopularMovieFragmentDirections.actionPopularMovieFragmentToSingleMovieFragment(it.id)
            findNavController().navigate(action)
        }

        val gridLayoutManager = GridLayoutManager(requireContext(), 3)
        gridLayoutManager.spanSizeLookup=object : GridLayoutManager.SpanSizeLookup(){
            override fun getSpanSize(position: Int): Int {
                val viewType=popularMoviePagingDataAdapter.getItemViewType(position)
                return if(viewType==popularMoviePagingDataAdapter.MOVIE_VIEW_TYPE) 1
                else 3
            }
        }

        binding.recyclerView.layoutManager=gridLayoutManager
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.adapter=popularMoviePagingDataAdapter

        viewModel.moviePagingData.observe(viewLifecycleOwner,{
            popularMoviePagingDataAdapter.submitData(lifecycle,it)
        })
        viewModel.networkState.observe(viewLifecycleOwner,{
            binding.progressBar.visibility=if(viewModel.dataIsEmpty&&it== NetworkState.LOADING) View.VISIBLE else View.GONE
            binding.txtError.visibility=if(viewModel.dataIsEmpty&&it== NetworkState.ERROR) View.VISIBLE else View.GONE
            if(!viewModel.dataIsEmpty){
                popularMoviePagingDataAdapter.setNetworkState(it)
            }
        })
        popularMoviePagingDataAdapter.addLoadStateListener { loadState ->
            if(loadState.append.endOfPaginationReached){
                viewModel.dataIsEmpty = popularMoviePagingDataAdapter.itemCount<1
            }
        }
    }

    override fun onDestroyView() {
        compositeDisposable.dispose()
        super.onDestroyView()
    }
}