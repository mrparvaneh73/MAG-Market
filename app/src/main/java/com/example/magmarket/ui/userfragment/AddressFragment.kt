package com.example.magmarket.ui.userfragment


import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.IntentSender
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.carto.styles.MarkerStyleBuilder
import com.example.magmarket.BuildConfig
import com.example.magmarket.R
import com.example.magmarket.application.Constants.TAG
import com.example.magmarket.data.datastore.user.User
import com.example.magmarket.data.remote.Resource
import com.example.magmarket.data.remote.model.customer.Billing
import com.example.magmarket.data.remote.model.customer.Customer
import com.example.magmarket.data.remote.model.customer.Shipping
import com.example.magmarket.databinding.FragmentAdressBinding
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.neshan.common.model.LatLng
import org.neshan.mapsdk.MapView
import org.neshan.mapsdk.internal.utils.BitmapUtils
import org.neshan.mapsdk.model.Marker
import pub.devrel.easypermissions.EasyPermissions
import java.text.DateFormat
import java.util.*

class AddressFragment : Fragment(R.layout.fragment_adress), EasyPermissions.PermissionCallbacks {
    private var _binding: FragmentAdressBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<AddressFragmentArgs>()
    private val viewModel by activityViewModels<UserViewModel>()
    private val UPDATE_INTERVAL_IN_MILLISECONDS: Long = 1000

    private val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS: Long = 1000

    private val REQUEST_CODE = 123

    private lateinit var map: MapView

    private var userLocation: Location? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var settingsClient: SettingsClient
    private lateinit var locationRequest: LocationRequest
    private var locationSettingsRequest: LocationSettingsRequest? = null
    private var locationCallback: LocationCallback? = null
    private var lastUpdateTime: String? = null

    private var mRequestingLocationUpdates: Boolean? = null
    private var marker: Marker? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAdressBinding.bind(view)
        addAddress()
        getResponseOfEdit()
        focusOnUserLocation()
        initLayoutReferences()
        initLocation()
        startReceivingLocationUpdates()
        backToAccount()

    }

    override fun onResume() {
        super.onResume()
        startLocationUpdates()
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    private fun initLayoutReferences() {
        initViews()
        initMap()
    }

    private fun initMap() {

        map.moveCamera(LatLng(35.767234, 51.330743), 0f)
        map.setZoom(14f, 0f)
    }

    private fun initViews() {
        map = view!!.findViewById(R.id.magmap)
    }

    private fun initLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        settingsClient = LocationServices.getSettingsClient(requireActivity())
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)

                userLocation = locationResult.lastLocation
                lastUpdateTime = DateFormat.getTimeInstance().format(Date())
                onLocationChange()
            }
        }
        mRequestingLocationUpdates = false
        locationRequest = LocationRequest.create().apply {
            numUpdates = 10
            interval = UPDATE_INTERVAL_IN_MILLISECONDS
            fastestInterval = FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(locationRequest)
        locationSettingsRequest = builder.build()
    }

    private fun addAddress() = with(binding) {
        submitButton.setOnClickListener {
            if (countryTextField.text.isNotEmpty() || cityTextField.text.isNotEmpty() || firstNameTextField.text.isNotEmpty() || secondNameTextField.text.isNotEmpty() || phoneNumberTextField.text.isNotEmpty() || postCodeTextField.text.isNotEmpty() || addressTextField.text.isNotEmpty()) {
                viewModel.updateCustomer(
                    args.id, Customer(
                        billing = Billing(
                            address_1 = addressTextField.text.toString(),
                            address_2 = addressTextField.text.toString(),
                            city = cityTextField.text.toString(),
                            company = "",
                            country = countryTextField.text.toString(),
                            email = args.email,
                            first_name = firstNameTextField.text.toString(),
                            last_name = secondNameTextField.text.toString(),
                            phone = phoneNumberTextField.text.toString(),
                            postcode = postCodeTextField.text.toString(),
                            state = ""
                        ), email = args.email,
                        first_name = firstNameTextField.text.toString(),
                        last_name = secondNameTextField.text.toString(),
                        shipping = Shipping(
                            address_1 = addressTextField.text.toString(),
                            address_2 = addressTextField.text.toString(),
                            city = cityTextField.text.toString(),
                            company = "",
                            country = countryTextField.text.toString(),
                            first_name = firstNameTextField.text.toString(),
                            last_name = secondNameTextField.text.toString(),
                            postcode = phoneNumberTextField.text.toString(),
                            state = "",

                            ), username = ""
                    )
                )
            } else {
                Toast.makeText(
                    requireContext(),
                    "لطفا اطلاعات را بدرستی تکمیل کنید",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun getResponseOfEdit() = with(binding) {
        viewModel.userUpdate.collectIt(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    openDialogSuccess()
                    updateUserLocal(
                        User(
                            userId = args.id,
                            email = args.email,
                            firstName = firstNameTextField.text.toString(),
                            lastName = secondNameTextField.text.toString(),
                            isLogin = true
                        )
                    )
                    addressTextField.text = null
                    firstNameTextField.text = null
                    secondNameTextField.text = null
                    countryTextField.text = null
                    cityTextField.text = null
                    secondNameTextField.text = null
                    phoneNumberTextField.text = null
                    postCodeTextField.text = null

                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), "مشکلی پیش آمده است", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun openDialogSuccess() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.edit_success)
        val button = dialog.findViewById<ImageView>(R.id.img_close)
        button.setOnClickListener {
            dialog.dismiss()

        }
        dialog.show()
    }

    private fun <T> StateFlow<T>.collectIt(lifecycleOwner: LifecycleOwner, function: (T) -> Unit) {
        lifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                collect {
                    function.invoke(it)
                }
            }
        }
    }

    private fun backToAccount() {
        binding.imgBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    fun startReceivingLocationUpdates() {

        if (EasyPermissions.hasPermissions(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            mRequestingLocationUpdates = true
            startLocationUpdates()
        } else {
            EasyPermissions.requestPermissions(
                requireActivity(),
                "App needs access to your location",
                101,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        when (requestCode) {
            101 -> {
                mRequestingLocationUpdates = true
                startLocationUpdates()
            }
        }

    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(requireActivity(), perms)) {
            openSettings()
        }
    }

    private fun openSettings() {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri = Uri.fromParts(
            "package",
            BuildConfig.APPLICATION_ID, null
        )
        intent.data = uri
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        settingsClient.checkLocationSettings(locationSettingsRequest!!)
            .addOnSuccessListener(requireActivity()) {
                Log.i(TAG, "All location settings are satisfied.")
                locationCallback?.let {
                    fusedLocationClient.requestLocationUpdates(
                        locationRequest,
                        it,
                        Looper.myLooper()
                    )
                }
                onLocationChange()
            }
            .addOnFailureListener(requireActivity())
            { e ->
                val statusCode = (e as ApiException).statusCode
                when (statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                        Log.i(
                            TAG,
                            "Location settings are not satisfied. Attempting to upgrade " +
                                    "location settings "
                        )
                        try {
                            val rae = e as ResolvableApiException
                            rae.startResolutionForResult(requireActivity(), REQUEST_CODE)
                        } catch (sie: IntentSender.SendIntentException) {
                            Log.i(
                                TAG,
                                "PendingIntent unable to execute request."
                            )
                        }
                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                        val errorMessage = "Location settings are inadequate, and cannot be " +
                                "fixed here. Fix in Settings."
                        Log.e(
                            TAG,
                            errorMessage
                        )
                        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
                    }
                }
                onLocationChange()
            }
    }

    private fun onLocationChange() {
        if (userLocation != null) {
            addUserMarker(LatLng(userLocation!!.latitude, userLocation!!.longitude))
        }
    }

    fun stopLocationUpdates() {

        fusedLocationClient.removeLocationUpdates(locationCallback!!)
            .addOnCompleteListener(
                requireActivity()
            ) {
                Toast.makeText(requireContext(), "Location updates stopped!", Toast.LENGTH_SHORT)
                    .show()
            }
    }


    private fun addUserMarker(loc: LatLng) {

        if (marker != null) {
            map.removeMarker(marker)
        }

        val markStCr = MarkerStyleBuilder()
        markStCr.size = 30f
        markStCr.bitmap = BitmapUtils.createBitmapFromAndroidBitmap(
            BitmapFactory.decodeResource(
                resources, R.drawable.ic_marker
            )
        )
        val markSt = markStCr.buildStyle()


        marker = Marker(loc, markSt)

        map.addMarker(marker)
    }

    fun focusOnUserLocation() {
        binding.focouse.setOnClickListener {
            if (userLocation != null) {
                map.moveCamera(
                    LatLng(userLocation!!.latitude, userLocation!!.longitude), 0.25f
                )
                map.setZoom(15f, 0.25f)
            }
        }

    }

    private fun updateUserLocal(user: User) {
        viewModel.saveUser(user)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopLocationUpdates()

    }

    override fun onStop() {
        super.onStop()
        stopLocationUpdates()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
}