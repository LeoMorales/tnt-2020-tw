package unpsjb.ing.tnt.mapsapp

import android.Manifest
import android.annotation.TargetApi
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.library.BuildConfig
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.snackbar.Snackbar
import com.google.maps.android.heatmaps.HeatmapTileProvider
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import unpsjb.ing.tnt.mapsapp.databinding.ActivityMapsBinding
import java.io.InputStream
import java.util.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private val SOLICITUD_PERMISO_UBICACION = 1
    private lateinit var binding: ActivityMapsBinding

    // TODO [Geofences] #1.2: Comprobar si el dispositivo está corriendo Android Q (API 29) o posterior.
    private val ejecutando_Q_o_superior = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q

    // TODO [Geofences] #1.7: Crear un Intent pendiente
    // Un PendingIntent para el Broadcast Receiver que maneja las transiciones de geofence
    // con respecto a lazy properties -> el valor se obtiene solo en el primer acceso
    private val geofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(this, GeofenceBroadcastReceiver::class.java)
        intent.action = ACCION_EVENTO_GEOFENCE
        // Usamos FLAG_UPDATE_CURRENT para obtener el mismo pending intent de nuevo
        // cuando se invoque addGeofences() y removeGeofences().
        PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private lateinit var clienteGeofencing: GeofencingClient
    private lateinit var viewModel: GeofenceViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_maps)
        viewModel = ViewModelProviders.of(
            this,
            SavedStateViewModelFactory(
                this.application,
                this)
            ).get(GeofenceViewModel::class.java)
        binding.lifecycleOwner = this


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // agregamos cliente geofences: instanciacion
        clienteGeofencing = LocationServices.getGeofencingClient(this)

        // Crear el canal para las notificaciones
        crearCanal(this )
    }


    override fun onStart() {
        super.onStart()
        comprobarPermisosEIniciarGeofencing()
    }

    /* Cuando se obtiene el resultado de pedirle al usuario que active la ubicación
     * del dispositivo, llamamos a checkDeviceLocationSettingsAndStartGeofence
     * nuevamente para asegurarnos de que esté realmente activado, pero no resolvemos
     * la verificación para evitar que el usuario vea un bucle sin fin.
     **/
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SOLICITUD_ENCENDIDO_UBICACION_DEL_DISPOSITIVO) {
            verificarConfigDeUbicacionDelDispositivoEIniciarGeofences(false)
        }
    }

    /*
     *  Cuando el usuario hace clic en la notificación, se llamará a este método,
     *  informándonos que se ha activado el geofence y que es hora de pasar
     *  al siguiente
     */
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val extras = intent?.extras
        if(extras != null){
            if(extras.containsKey(GeofencingConstants.EXTRA_GEOFENCE_INDICE)){
                viewModel.actualizarSugerencia(extras.getInt(GeofencingConstants.EXTRA_GEOFENCE_INDICE))
                comprobarPermisosEIniciarGeofencing()
            }
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        // TODO #1.1: Cambiar el marcador
        // Add a marker in Sydney and move the camera

        val playaUnionLatLong = LatLng(-43.327159, -65.050439)

        // Create a val for how zoomed in you want to be on the map. Use zoom level 15f.
        val zoomLevel = 15f
        //map.moveCamera(CameraUpdateFactory.newLatLng(playaUnion))
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(playaUnionLatLong, zoomLevel))

        // TODO: Agregar un overlay
        val overlaySize = 100f
        val androidOverlay = GroundOverlayOptions()
            .image(BitmapDescriptorFactory.fromResource(R.drawable.tantos_5))
            .position(playaUnionLatLong, overlaySize)

        map.addGroundOverlay(androidOverlay)

        map.addMarker(MarkerOptions().position(playaUnionLatLong).title("Torneo de truco - Playa Unión"))


        setMapLongClick(map)

        setPoiClick(map)

        activarUbicacion()

        addHeatMap()
    }


    // TODO #2.1: Menú -> Creamos el menu:
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.opciones_mapa, menu)
        return true
    }

    // TODO #2.2: Menú -> Click en las opciones del menú:
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        // Cambiar el tipo del mapa segun la elección del usuario
        R.id.mapa_normal -> {
            map.mapType = GoogleMap.MAP_TYPE_NORMAL
            true
        }
        R.id.mapa_hibrido -> {
            map.mapType = GoogleMap.MAP_TYPE_HYBRID
            true
        }
        R.id.mapa_satelite -> {
            map.mapType = GoogleMap.MAP_TYPE_SATELLITE
            true
        }
        R.id.mapa_terreno -> {
            map.mapType = GoogleMap.MAP_TYPE_TERRAIN
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    // TODO #3.1: Agregar un marcador al clic largo
    private fun setMapLongClick(map: GoogleMap) {
        map.setOnMapLongClickListener { latLng ->
            // Snippet --> texto adicional que se muestra debajo del titulo.
            val snippet = String.format(
                Locale.getDefault(),
                "Lat: %1$.5f, Long: %2$.5f",
                latLng.latitude,
                latLng.longitude
            )
            map.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title(getString(R.string.nuevo_marcador))
                    .snippet(snippet)
                    //TODO: Cambiar de color al marcador:
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
            )
        }
    }

    // TODO #4.1: Agregar escuchador POIs
    private fun setPoiClick(map: GoogleMap) {
        map.setOnPoiClickListener { poi ->
            val poiMarker = map.addMarker(
                MarkerOptions()
                    .position(poi.latLng)
                    .title(poi.name)
            )
            poiMarker.showInfoWindow()
        }
    }

    //TODO #5.1: Ubicacion actual (isPermissionGranted)
    private fun seOtorgoPermiso() : Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun activarUbicacion() {
        if (seOtorgoPermiso()) {
            map.isMyLocationEnabled = true
        }
        else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION),
                SOLICITUD_PERMISO_UBICACION
            )
        }
    }

    /*****************************************************************************************
     * ***************************************************************************************
     * Geofences
     * ***************************************************************************************
     *****************************************************************************************/
    // TODO #6.5: Manejar la solicitud de permisos
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == SOLICITUD_PERMISO_UBICACION) {
            if (grantResults.contains(PackageManager.PERMISSION_GRANTED)) {
                activarUbicacion()
            }
        }
        Log.d(TAG, "-> onRequestPermissionResult")

        if (
            grantResults.isEmpty() ||
            grantResults[PERMISO_UBICACION_INDICE] == PackageManager.PERMISSION_DENIED ||
            (requestCode == SOLICITUD_PERMISOS_FOREGROUND_Y_BACKGROUND_CODIGO_RESULTADO &&
                    grantResults[PERMISO_UBICACION_BACKGROUND_INDICE] ==
                    PackageManager.PERMISSION_DENIED))
        {
            // Permiso denegado
            // Explicar que la app necesita la ubicación;
            Snackbar.make(
                binding.mapsActivityMain,
                R.string.permiso_denegado_explicacion,
                Snackbar.LENGTH_INDEFINITE
            )
                .setAction(R.string.settings) {
                    startActivity(Intent().apply {
                        action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        data = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    })
                }.show()
        } else {
            verificarConfigDeUbicacionDelDispositivoEIniciarGeofences()
        }
    }

    /**
     * This will also destroy any saved state in the associated ViewModel, so we remove the
     * geofences here.
     */
    override fun onDestroy() {
        super.onDestroy()
        removeGeofences()
    }


    /**
     * Comenzar el chequeo de permisos y el proceso de Geofence
     * solo si la Geofence asociada con el punto actual
     * no está activo.
     */
    private fun comprobarPermisosEIniciarGeofencing() {

        if (viewModel.geofenceEstaActiva()) return
        if (permisosDeUbicacionEnPrimerYSegundoPlanoAprobados()) {
            verificarConfigDeUbicacionDelDispositivoEIniciarGeofences()
        } else {
            solicitarPermisosDeUbicacionForegroundYBackground()
        }
    }


    // TODO [Geofences] #1.6: chequear la ubicación del usuario
    /*
     *  Utiliza el Cliente de Ubicacion (Location Client) para verificar el estado actual
     *  del estado de configuración de ubicacion y le permite al usuario
     *  prender la ubicación con nuestra aplicación.
     */
    private fun verificarConfigDeUbicacionDelDispositivoEIniciarGeofences(resolver:Boolean = true) {
        // Primero crear un LocationRequest y utilizarlo con el LocationSettingsRequest Builder
        val solicitudDeUbicacion = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_LOW_POWER
        }
        val builder = LocationSettingsRequest.Builder().addLocationRequest(solicitudDeUbicacion)

        // Luego utilizar LocationServices para obtener el SettingsClient.
        // crear locationSettingsResponseTask y utilizarlo para comprobar la configuración de la ubicación
        val clienteDeConfiguracion = LocationServices.getSettingsClient(this)
        val tareaRespuestaDeConfiguracionDeUbicacion =
            clienteDeConfiguracion.checkLocationSettings(builder.build())

        // Agregamos un escuchador onFailureListener a locationSettingsResponseTask
        // para el caso en que la configuracione de la ubicacion no sea satisfecha:
        tareaRespuestaDeConfiguracionDeUbicacion.addOnFailureListener { exception ->
            // Comprobar si la excepción es del tipo ResolvableApiException y,
            // en caso afirmativo, intentamos llamar al método startResolutionForResult()
            // para solicitar al usuario que active la ubicación del dispositivo.
            if (exception is ResolvableApiException && resolver){
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    exception.startResolutionForResult(this@MapsActivity,
                        SOLICITUD_ENCENDIDO_UBICACION_DEL_DISPOSITIVO)
                } catch (sendEx: IntentSender.SendIntentException) {
                    // caso de error, logear un mensaje
                    Log.d(TAG, "Error al obtener la resolucion de la ubicacion: " + sendEx.message)
                }
            } else {
                // si la excepción no es del tipo ResolvableApiException, mostrar una snackbar que
                // alerte al usuario se necesita habilitada para aprovechar una mejor funcionalidad
                Snackbar.make(
                    binding.mapsActivityMain,
                    R.string.error_ubicacion_requerida, Snackbar.LENGTH_INDEFINITE
                ).setAction(android.R.string.ok) {
                    verificarConfigDeUbicacionDelDispositivoEIniciarGeofences()
                }.show()
            }
        }

        // si locationSettingsResponseTask se completa, se comprueba de que haya sido
        // exitosa y se agregan los geofences
        tareaRespuestaDeConfiguracionDeUbicacion.addOnCompleteListener {
            if ( it.isSuccessful ) {
                agregarGeofences()
            }
        }
    }

    // TODO [Geofences] #1.3: Crear el método para comprobar permisos
    @TargetApi(29)
    private fun permisosDeUbicacionEnPrimerYSegundoPlanoAprobados(): Boolean {
        // Primero se chequea si el permiso ACCESS_FINE_LOCATION fue aceptado
        val ubicacionEnPrimerPlanoAprobada = (
                PackageManager.PERMISSION_GRANTED ==
                        ActivityCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION))
        // Si el dispositivo ejecuta Android Q (API 29) o superior, verificar que se haya otorgado
        //  el permiso ACCESS_BACKGROUND_LOCATION. Devolver true si el dispositivo ejecuta una
        //  versión inferior a Q, donde no se necesita un permiso para acceder a la ubicación en
        //  segundo plano.
        val permisoEnBackgroundAprobado =
            if (ejecutando_Q_o_superior) {
                PackageManager.PERMISSION_GRANTED ==
                        ActivityCompat.checkSelfPermission(
                            this, Manifest.permission.ACCESS_BACKGROUND_LOCATION
                        )
            } else {
                true
            }

        // Devuelve verdadero si se han otorgado los permisos y falso si no.
        return ubicacionEnPrimerPlanoAprobada && permisoEnBackgroundAprobado
    }

    // TODO [Geofences] #1.4: Solicitar permisos de ubicación al usuario
    @TargetApi(29 )
    private fun solicitarPermisosDeUbicacionForegroundYBackground() {
        // Si los permisos ya se han otorgado, no necesita volver a solicitarlo,
        //  por lo que puede volver a salir del método.
        if (permisosDeUbicacionEnPrimerYSegundoPlanoAprobados()) {
            return
        }
        // permissionsArray contiene los permisos que se solicitarán.
        // Inicialmente, se agrega ACCESS_FINE_LOCATION ya que es necesario para todos
        // los niveles de API.
        var listaDePermisos = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)

        // Decidir qué permisos se deben solicitar
        val codigoResultado = when {
            ejecutando_Q_o_superior -> {
                listaDePermisos += Manifest.permission.ACCESS_BACKGROUND_LOCATION
                SOLICITUD_PERMISOS_FOREGROUND_Y_BACKGROUND_CODIGO_RESULTADO
            }
            else -> SOLICITUD_SOLO_PERMISO_FOREGROUND_CODIGO_RESULTADO
        }
        Log.d(TAG, "Solicitar solo permiso de ubicación foreground")

        // Finalmente, solicitamos permisos pasando la actividad actual, el arreglo de permisos
        // y el código de resultado.
        ActivityCompat.requestPermissions(
            this@MapsActivity,
            listaDePermisos,
            codigoResultado
        )
    }



    /*
     * Agrega una geofence para el punto actual y si es necesario remueve cualquier Geofence
     * existente. Este método debe ser invocado despues de que el usuario otorgue permisos de
     * ubicacion.
     * En caso de que no tengamos mas geofences para agregar, removemos la que nos queda y le
     * indicamos al viewModel que la sugerencia final esta "activa."
     */
    private fun agregarGeofences() {
        // Comprobamos si ya tenemos geofences activos.
        // Deberíamos agregar de a uno...
        if (viewModel.geofenceEstaActiva()) return

        // Obtener el indice del geofence desde el viewMOdel.
        val indiceGeofenceActual = viewModel.proximoIndiceGeofence()
        // si nos excedimos en la cantidad de geofences -> remover y desactivar
        if(indiceGeofenceActual >= GeofencingConstants.NUM_LANDMARKS) {
            removeGeofences()
            viewModel.geofenceActivada()
            return
        }

        // una vez que tenemos el indice del geofence y sabemos que es válido,
        // obtener los datos del geofence: id, lat y longitud
        val datosGeofenceActual = GeofencingConstants.MARCADORES_DATA[indiceGeofenceActual]

        // Crear el object Geofence
        val geofence = Geofence.Builder()
            // Setear un request ID, cadena para identificar la geofence.
            .setRequestId(datosGeofenceActual.id)
            // Setear la region circular region de la geofence.
            .setCircularRegion(
                datosGeofenceActual.latLong.latitude,
                datosGeofenceActual.latLong.longitude,
                GeofencingConstants.GEOFENCE_RADIO_EN_METROS
            )
            // Setear la duración de expiration de geofence. Esta geofence es removida
            // automaticamente luego de este período de tiempo.
            .setExpirationDuration(GeofencingConstants.GEOFENCE_EXPIRACION_EN_MILLISEGUNDOS)
            // Setear el tipo de transición de interes. Las alertas se generan solo para
            // estas transiciones. En este ejemplo se restrean transiciones de entrada
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
            .build()

        // Crear el geofence request
        val geofencingRequest = GeofencingRequest.Builder()
            // La bandera INITIAL_TRIGGER_ENTER indica que el servicio de geofencing debe
            // disparar una notificacion GEOFENCE_TRANSITION_ENTER cuando el geofence es
            // agregado y si el dispositivo ya se encuentra dentro de la geofence
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            // Agregar las geofences que van a ser monitoreadas por el servicio de geofencing.
            .addGeofence(geofence)
            .build()

        // Primero, remover cualquier geofence existente que utilice nuestro pending intent
        clienteGeofencing.removeGeofences(geofencePendingIntent)?.run {
            // Independientemente de si se pudo efectuar el remover, agregar la geofence
            addOnCompleteListener {
                // Agregar la nueva solicitud de geofence con la nueva geofence.
                clienteGeofencing.addGeofences(geofencingRequest, geofencePendingIntent)?.run {
                    addOnSuccessListener {
                        // Geofence agregada.
                        Toast.makeText(this@MapsActivity, R.string.geofences_agregada,
                            Toast.LENGTH_SHORT)
                            .show()
                        Log.e("Agregar Geofence", geofence.requestId)
                        // Indicarle al viewmodel que se activo la geofence.
                        viewModel.geofenceActivada()
                    }
                    addOnFailureListener {
                        // Fallo al agregar geofences.
                        Toast.makeText(this@MapsActivity, R.string.geofences_no_agregadas,
                            Toast.LENGTH_SHORT).show()
                        if ((it.message != null)) {
                            Log.w(TAG, it.message)
                        }
                    }
                }
            }
        }
    }


    /**
     * Remover geofences. Este método debe ser invocado despues de que el usuario otorgue permisos de
     * ubicacion.
     */
    private fun removeGeofences() {
        if (!permisosDeUbicacionEnPrimerYSegundoPlanoAprobados()) {
            return
        }
        clienteGeofencing.removeGeofences(geofencePendingIntent)?.run {
            addOnSuccessListener {
                // Geofences removida
                Log.d(TAG, getString(R.string.geofences_removida))
                Toast.makeText(applicationContext, R.string.geofences_removida, Toast.LENGTH_SHORT)
                    .show()
            }
            addOnFailureListener {
                // Fallo al remover geofences
                Log.d(TAG, getString(R.string.geofences_no_removidas))
            }
        }
    }

    private fun addHeatMap() {
        var list: List<LatLng?>? = null

        // Get the data: latitude/longitude positions of police stations.
        try {
            list = readItems(R.raw.police_stations)
        } catch (e: JSONException) {
            Toast.makeText(this, "Problem reading list of locations.", Toast.LENGTH_LONG).show()
        }

        // Create a heat map tile provider, passing it the latlngs of the police stations.
        var mProvider = HeatmapTileProvider.Builder()
            .data(list)
            .build()
        // Add a tile overlay to the map, using the heat map tile provider.
        var mOverlay = map.addTileOverlay(TileOverlayOptions().tileProvider(mProvider))
    }

    @Throws(JSONException::class)
    private fun readItems(resource: Int): ArrayList<LatLng?>? {
        val list = ArrayList<LatLng?>()
        val inputStream: InputStream = resources.openRawResource(resource)
        val json: String = Scanner(inputStream).useDelimiter("\\A").next()
        val array = JSONArray(json)
        for (i in 0 until array.length()) {
            val `object`: JSONObject = array.getJSONObject(i)
            val lat: Double = `object`.getDouble("lat")
            val lng: Double = `object`.getDouble("lng")
            list.add(LatLng(lat, lng))
        }
        return list
    }

    companion object {
        internal const val ACCION_EVENTO_GEOFENCE =
            "MapsActivity.torneotruco.action.ACTION_GEOFENCE_EVENT"
    }
}

private const val SOLICITUD_PERMISOS_FOREGROUND_Y_BACKGROUND_CODIGO_RESULTADO = 33
private const val SOLICITUD_SOLO_PERMISO_FOREGROUND_CODIGO_RESULTADO = 34
private const val SOLICITUD_ENCENDIDO_UBICACION_DEL_DISPOSITIVO = 29
private const val TAG = "MapsActivity"
private const val PERMISO_UBICACION_INDICE = 0
private const val PERMISO_UBICACION_BACKGROUND_INDICE = 1
