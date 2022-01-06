package com.example.movieapplication.ui.singlemoviedetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.movieapplication.R
import com.example.movieapplication.data.api.POSTER_BASE_URL
import com.example.movieapplication.data.api.TheMovieDBClient
import com.example.movieapplication.data.api.TheMovieDBInterface
import com.example.movieapplication.data.repository.NetworkState
import com.example.movieapplication.data.vo.MovieDetails
import com.example.movieapplication.databinding.SingleMovieFragmentBinding
import kotlinx.android.synthetic.main.toolbar.*
import java.text.NumberFormat
import java.util.*

class SingleMovieFragment : Fragment() {

    private val viewModel: SingleMovieViewModel by viewModels{
        SingleMovieViewModelFactory(movieRepository, movieId)
    }
    private lateinit var movieRepository:MovieDetailsRepository
    private lateinit var binding:SingleMovieFragmentBinding
    private var movieId=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let{
            movieId=it.getInt("movieId")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=SingleMovieFragmentBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val apiService:TheMovieDBInterface=TheMovieDBClient.getClient()
        movieRepository= MovieDetailsRepository(apiService)

        viewModel.movieDetails.observe(viewLifecycleOwner, {
            bindUI(it)
        })

        viewModel.networkState.observe(viewLifecycleOwner, {
            binding.progressBar.visibility=if(it== NetworkState.LOADING) View.VISIBLE else View.GONE
            binding.scrollView.visibility=if(it== NetworkState.LOADED) View.VISIBLE else View.GONE
            binding.txtError.visibility=if(it==NetworkState.ERROR) View.VISIBLE else View.GONE
        })
        back.setOnClickListener{
            activity?.onBackPressed()
        }
    }

    private fun bindUI(it: MovieDetails) {
        val formatCurrency= NumberFormat.getCurrencyInstance(Locale.US)
        val moviePosterURL= POSTER_BASE_URL+it.posterPath
        binding.apply{
            movieTitle.text=it.title
            movieTagline.text=it.tagline
            dateReleased.text=resources.getString(R.string.date_released,it.releaseDate)
            rating.text=resources.getString(R.string.rating,it.rating.toString())
            lasting.text=resources.getString(R.string.length,it.runtime.toString())
            description.text=resources.getString(R.string.description, it.overview)
            budget.text=resources.getString(R.string.budget,formatCurrency.format(it.budget))
            plus.text=resources.getString(R.string.plus,formatCurrency.format(it.revenue))
        }
        Glide.with(this)
            .load(moviePosterURL)
            .into(binding.ivMoviePoster)
    }
}