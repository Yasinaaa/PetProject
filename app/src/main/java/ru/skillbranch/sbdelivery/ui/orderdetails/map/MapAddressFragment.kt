package ru.skillbranch.sbdelivery.ui.orderdetails.map

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import ru.skillbranch.sbdelivery.R
import ru.skillbranch.sbdelivery.databinding.FragmentMapBinding
import ru.skillbranch.sbdelivery.ui.base.BaseFragment
import ru.skillbranch.sbdelivery.ui.base.Binding
import ru.skillbranch.sbdelivery.ui.base.IViewModelState
import android.graphics.Bitmap
import androidx.core.content.ContextCompat
import android.graphics.Canvas
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.VisibleForTesting
import androidx.core.app.ActivityCompat

import ru.skillbranch.sbdelivery.utils.RenderProp

import android.location.Geocoder
import android.location.Location
import android.util.Log
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.*
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import ru.skillbranch.sbdelivery.data.remote.models.response.Address
import ru.skillbranch.sbdelivery.ui.orderdetails.OrderDetailsFragment.Companion.DELIVERY_ADDRESS
import java.io.IOException
import java.util.*


class MapAddressFragment : BaseFragment<MapAddressViewModel>(), OnMapReadyCallback{

    override val viewModel: MapAddressViewModel by stateViewModel()
    private var binding: FragmentMapBinding? = null
    override val baseBinding: Binding? by lazy { MapAddressBinding() }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    lateinit var permissionsLauncher: ActivityResultLauncher<Array<out String>>

    private var mapFragment: SupportMapFragment? = null
    private var currentMarker: Marker? = null
    private var currentLocation: Location? = null
    private var map: GoogleMap? = null
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        permissionsLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions(),
            requireActivity().activityResultRegistry,
            ::callbackPermissions
        )
    }

    private fun callbackPermissions(result: Map<String, Boolean>) {

        val permissionsResult = result.mapValues { (permission, isGranted) ->
            if (isGranted) true to true
            else false to ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                permission
            )
        }
        //remove tempt file by uri if permission denied
        val isAllGranted = !permissionsResult.values.map { it.first }.contains(false)
        if (!isAllGranted) {
            //todo
        }
        viewModel.handlePermission(permissionsResult)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapBinding.inflate(inflater, container, false)
        binding?.viewModel = viewModel
        return binding!!.root
    }

    override fun setupViews() {
        mapFragment = childFragmentManager.findFragmentById(R.id.fr_map) as SupportMapFragment?
        viewModel.checkLocationPermission()
        viewModel.observedPermissions(viewLifecycleOwner) {
            //launch callback for request permissions
            permissionsLauncher.launch(it.toTypedArray())
        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        map = googleMap

        val latLng =
            if (currentLocation != null)
                LatLng(currentLocation!!.latitude, currentLocation!!.longitude)
            else
                LatLng(55.77, 49.11)

        moveMarket(latLng)

        map?.setOnMapClickListener { marker ->
            Log.d("====", "latitude : " + marker.latitude)

            if (currentMarker != null) {
                currentMarker?.remove()
            }
            moveMarket(LatLng(marker.latitude, marker.longitude))
        }
    }

    private fun bitmapDescriptorFromVector(vectorResId: Int): BitmapDescriptor? {
        return ContextCompat.getDrawable(requireContext(), vectorResId)?.run {
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            val bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
            draw(Canvas(bitmap))
            BitmapDescriptorFactory.fromBitmap(bitmap)
        }
    }

    private fun moveMarket(latLng: LatLng) {
        val markerOptions = MarkerOptions()
            .position(latLng)
            .icon(bitmapDescriptorFromVector(R.drawable.ic_marker))
            .draggable(true)

        map?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17f))
        currentMarker = map?.addMarker(markerOptions)
        currentMarker?.showInfoWindow()
        viewModel.searchAddress(latLng)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    inner class MapAddressBinding : Binding() {

        private var returnBack: Boolean by RenderProp(false) {
            if (it){
                findNavController().previousBackStackEntry?.savedStateHandle
                    ?.set(DELIVERY_ADDRESS,
                        String.format(requireContext().getString(R.string.address_input),
                            currentAddress.city,
                            currentAddress.street,
                            currentAddress.house)
                    )
                findNavController().popBackStack()
            }
        }

        private var showCurrentLocation: Boolean by RenderProp(false) {
            if (it){
                getLoc()
            }
        }

        private fun getLoc(){
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                viewModel.checkLocationPermission()
                return
            }
            val task = fusedLocationProviderClient!!.lastLocation

            task.addOnSuccessListener { location ->
                if (location != null) {
                    currentLocation = location
                }
                mapFragment!!.getMapAsync(this@MapAddressFragment)
            }
        }

        private var currentAddress: Address by RenderProp(Address()) {
            if (it.city!!.isNotEmpty()){
                binding?.tietAddress?.setText(
                    String.format(requireContext().getString(R.string.address_input),
                        it.city, it.street, it.house)
                )
                binding?.mbChooseAddress?.isEnabled = true
            }else{
                binding?.mbChooseAddress?.isEnabled = false
            }
        }

        override fun bind(data: IViewModelState) {
            data as MapAddressState
            showCurrentLocation = data.currentLocation
            currentAddress = data.currentAddress
            returnBack = data.returnBack
        }
    }

}