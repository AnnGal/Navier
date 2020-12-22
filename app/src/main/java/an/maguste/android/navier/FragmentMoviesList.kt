package an.maguste.android.navier

import an.maguste.android.navier.adapters.MovieAdapter
import an.maguste.android.navier.adapters.OnMovieClickListener
import an.maguste.android.navier.databinding.FragmentMoviesListBinding
import an.maguste.android.navier.mvvm.FragmentMoviesListVM
import an.maguste.android.navier.mvvm.MoviesListViewModelFactory
import an.maguste.android.navier.mvvm.State
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager


class FragmentMoviesList : Fragment() {

    // view model
    private lateinit var viewModel: FragmentMoviesListVM

    // ViewBinding
    private var _binding: FragmentMoviesListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoviesListBinding.inflate(inflater, container, false)

        // create view model
        val viewModelFactory = MoviesListViewModelFactory(requireContext())
        viewModel = ViewModelProvider(this, viewModelFactory).get(FragmentMoviesListVM::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // set recycler view
        binding.recyclerView.adapter = MovieAdapter(OnMovieClickListener {
            viewModel.selectMovie(it)
        })
        binding.recyclerView.layoutManager = GridLayoutManager(context, getSpanCount())

        // watch for LiveData
        setObservers()
    }


    /** observe ViewModel data */
    private fun setObservers() {
        // observe movies data
        viewModel.moviesData.observe(viewLifecycleOwner, { movieList ->
            (binding.recyclerView.adapter as MovieAdapter).apply {
                bindMovie(movieList)
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

        // observe movie selected
        viewModel.selectedMovie.observe(viewLifecycleOwner,  {
            if ( null != it ) {
                this.findNavController().navigate(FragmentMoviesListDirections.actionToMoviesDetails(it))
                viewModel.selectMovieShown()
            }
        })
    }

    /** calculate grid's columns number */
    private fun getSpanCount() =
        when (resources.configuration.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> 3
            else -> 2
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}