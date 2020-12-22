package an.maguste.android.navier

import an.maguste.android.navier.adapters.MovieAdapter
import an.maguste.android.navier.adapters.OnMovieClickListener
import an.maguste.android.navier.data.ChangeFragment
import an.maguste.android.navier.data.Movie
import an.maguste.android.navier.data.loadMovies
import an.maguste.android.navier.databinding.FragmentMoviesListBinding
import an.maguste.android.navier.mvvm.FragmentMoviesListVM
import an.maguste.android.navier.mvvm.MoviesViewModelFactory
import an.maguste.android.navier.mvvm.State
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel


class FragmentMoviesList : Fragment() {

   /* private val viewModel: FragmentMoviesListVM by lazy {
        ViewModelProvider(this).get(FragmentMoviesListVM::class.java)
    }*/
   private lateinit var viewModel: FragmentMoviesListVM

    private var listenerFragment: ChangeFragment? = null

    private var _binding: FragmentMoviesListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoviesListBinding.inflate(inflater, container, false)

        val viewModelFactory = MoviesViewModelFactory(requireContext())
        viewModel = ViewModelProvider(this, viewModelFactory).get(FragmentMoviesListVM::class.java)

        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // catch listener from activity
        listenerFragment = context as? ChangeFragment
    }

    override fun onDetach() {
        super.onDetach()
        listenerFragment = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // set recycler grid
        binding.recyclerView.adapter = MovieAdapter(movieListener)
        binding.recyclerView.layoutManager = GridLayoutManager(context, getSpanCount())

        // watch for LiveData
        setObservers()

        // get movies data
        viewModel.getMovieData()
    }


    /** observe ViewModel data */
    private fun setObservers() {
        // observe movies data
        viewModel.moviesData.observe(viewLifecycleOwner, { movieList ->
            (binding.recyclerView.adapter as MovieAdapter).apply { (
                    bindMovie(movieList)
            )
            }
        })

        // observe status
        viewModel.state.observe(viewLifecycleOwner, { status ->
            when (status) {
                is State.Init, is State.Success -> {
                    binding.progressBar.visibility = View.INVISIBLE
                    binding.layoutLostData.visibility = View.INVISIBLE
                }
                is State.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.layoutLostData.visibility = View.INVISIBLE
                }
                is State.Error -> {
                    binding.progressBar.visibility = View.INVISIBLE
                    binding.layoutLostData.visibility = View.VISIBLE
                }
            }
        })
    }

    /** calculate grid's columns number */
    private fun getSpanCount() =
        when (resources.configuration.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> 3
            else -> 2
        }

    /** on MovieCard click reaction */
    private val movieListener = object: OnMovieClickListener {
        override fun onClick(movie: Movie) {
            listenerFragment?.toMovieDetail(movie)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //scope.cancel()
        _binding = null
    }

}