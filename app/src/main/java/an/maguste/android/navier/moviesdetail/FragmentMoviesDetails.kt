package an.maguste.android.navier.moviesdetail

import an.maguste.android.navier.R
import an.maguste.android.navier.data.Actor
import an.maguste.android.navier.data.Movie
import an.maguste.android.navier.databinding.FragmentMoviesDetailsBinding
import android.Manifest
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.CalendarContract
import android.provider.CalendarContract.Events
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import java.util.*


class FragmentMoviesDetails : Fragment() {

    // view model
    private lateinit var viewModel: MoviesDetailsViewModel

    // ViewBinding
    private var _binding: FragmentMoviesDetailsBinding? = null
    private val binding get() = _binding!!

    private var movie: Movie? = null

    // permission
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private var isRationaleShown = false

    // calendar
    private var dateAndTime = Calendar.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoviesDetailsBinding.inflate(inflater, container, false)

        movie = FragmentMoviesDetailsArgs.fromBundle(requireArguments()).selectedMovie

        val viewModelFactory = MoviesDetailViewModelFactory()

        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(MoviesDetailsViewModel::class.java)


        binding.scheduleMovie.setOnClickListener {
            //viewModel.scheduleMoveIntoCalendar(binding.title.text.toString())
            scheduleIntoCalendar()
        }

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.adapter = ActorAdapter()
        binding.recyclerView.hasFixedSize()

        binding.toolbar.setOnClickListener {
            requireActivity().onBackPressed()
        }

        setObservers()

        movie?.let {
            setMovieData(it)
            viewModel.getActors(it.id)
        }
    }

    private fun setObservers() {
        // observe actors data
        viewModel.actors.observe(viewLifecycleOwner, {
            setActorsData(it)
        })

        // observe write into calendar intent
        viewModel.calendarIntent.observe(viewLifecycleOwner, { calendarIntet ->
            if (calendarIntet != null){
                startActivity(calendarIntet)
                viewModel.scheduleMoveDone()
            }
        })

    }

    // set data on fragment
    private fun setMovieData(movie: Movie) {
        movie.backdrop?.let {
            Glide.with(requireContext())
                .load(movie.backdrop)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .apply(imageOption)
                .into(binding.poster)
        }

        with(movie) {
            // remove age rating or put correct
            if (adult) {
                binding.ageRating.text = resources.getString(R.string.age_rating_default)
            } else {
                binding.ageRating.visibility = View.INVISIBLE
            }

            binding.title.text = title
            binding.genres.text = genres.joinToString(", ")
            with(binding.ratingBar) {
                visibility = View.VISIBLE
                rating = ratings
            }
            binding.reviews.text = resources.getQuantityString(R.plurals.review, reviews, reviews)

            // storyline
            overview?.let {
                binding.storylineLabel.visibility = View.VISIBLE
                binding.storylineText.text = overview
            }
        }
    }

    private fun setActorsData(actors: List<Actor>) {
        if (actors.isNotEmpty()) {
            binding.cast.visibility = View.VISIBLE
            (binding.recyclerView.adapter as? ActorAdapter)?.bindActor(actors)
        } else {
            binding.cast.visibility = View.INVISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private val imageOption = RequestOptions()
            .placeholder(R.drawable.empty_photo)
            .fallback(R.drawable.empty_photo)
    }

    @SuppressLint("MissingPermission")
    override fun onAttach(context: Context) {
        super.onAttach(context)

        // permission
        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                onLocationPermissionGranted()
            } else {
                onLocationPermissionNotGranted()
            }
        }
    }

    override fun onDetach() {
        requestPermissionLauncher.unregister()

        super.onDetach()
    }

    /** start schedule dialog */
    private fun scheduleIntoCalendar() {
        activity?.let {
            // if we have permission
            if (ContextCompat.checkSelfPermission(it, Manifest.permission.WRITE_CALENDAR)
                == PackageManager.PERMISSION_GRANTED
            ) {
                onLocationPermissionGranted()
                return
            }
            showLocationPermissionExplanationDialog()
            //if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_CALENDAR)) {
            //    showLocationPermissionExplanationDialog()
            //}
            /*when {



                // ask permission politely

                ->
                //
                isRationaleShown -> showLocationPermissionDeniedDialog()
                else -> requestLocationPermission()
            }*/
        }
    }

    private fun launchDatePicker() {
        val dpd = DatePickerDialog(
            requireContext(),
            { _, year, monthOfYear, dayOfMonth ->
                dateAndTime.set(year, monthOfYear, dayOfMonth)

                launchTimePicker()
            },
            dateAndTime.get(Calendar.YEAR),
            dateAndTime.get(Calendar.MONTH),
            dateAndTime.get(Calendar.DAY_OF_MONTH)
        )
        dpd.show()
    }

    private fun launchTimePicker() {
        val tpd = TimePickerDialog(requireContext(), { _, hour, minute ->
            dateAndTime.set(
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH),
                hour,
                minute
            )
            writeIntoCalendar()
        }, dateAndTime.get(Calendar.HOUR), dateAndTime.get(Calendar.MINUTE), true)
        tpd.show()
    }

    private fun writeIntoCalendar() {
        context?.let {
            Toast.makeText(
                context, "writeIntoCalendar ${dateAndTime.get(Calendar.DAY_OF_MONTH)} ${
                    dateAndTime.get(Calendar.HOUR)
                }", Toast.LENGTH_SHORT
            ).show()
            viewModel.scheduleMoveIntoCalendar(binding.title.text.toString(), dateAndTime)
        }
    }


    @RequiresPermission(Manifest.permission.WRITE_CALENDAR)
    private fun onLocationPermissionGranted() {
        // grab date and write movie
        launchDatePicker()
    }

    private fun onLocationPermissionNotGranted() {
        context?.let {
            Toast.makeText(context, "not_granted_text", Toast.LENGTH_SHORT).show()
        }
    }

    private fun requestLocationPermission() {
        context?.let {
            requestPermissionLauncher.launch(Manifest.permission.WRITE_CALENDAR)
        }
    }

    private fun showLocationPermissionDeniedDialog() {
        context?.let {
            AlertDialog.Builder(it)
                .setMessage("R.string.ws04_permission_dialog_denied_text")
                .setPositiveButton("Yes"/*R.string.ws04_dialog_positive_button*/) { dialog, _ ->
                    startActivity(
                        Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.parse("package:" + it.packageName)
                        )
                    )
                    dialog.dismiss()
                }
                .setNegativeButton("No"/*R.string.ws04_dialog_negative_button*/) { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    private fun showLocationPermissionExplanationDialog() {
        context?.let {
            AlertDialog.Builder(it)
                .setMessage("R.string.ws04_permission_dialog_explanation_text")
                .setPositiveButton("Yes"/*R.string.ws04_dialog_positive_button*/) { dialog, _ ->

                    isRationaleShown = true
                    requestLocationPermission()
                    dialog.dismiss()
                }
                .setNegativeButton("No"/*"R.string.ws04_dialog_negative_button"*/) { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }
}
