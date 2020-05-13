package unpsjb.ing.tnt.primeramapastw

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.maps.android.heatmaps.HeatmapTileProvider
import org.json.JSONArray
import org.json.JSONException
import java.io.InputStream
import java.util.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private val SOLICITUD_PERMISO_UBICACION = 1
    private lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
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

        agregarMarcador(map)
        setMapLongClick(map)
        setPoiClick(map)
        agregarCapaSuperpuesta(map)
        activarUbicacion(map)
        addHeatMap(map)

    }

    private fun agregarMarcador(map: GoogleMap) {
        val playa_union = LatLng(-43.321762, -65.047201)
        val nivelZoom = 20f

        this.map.addMarker(
            MarkerOptions()
                .position(playa_union)
                .title("Marcador en Playa Unión")
        )
        this.map.moveCamera(
            CameraUpdateFactory.newLatLngZoom(playa_union, nivelZoom)
        )
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.opciones_del_mapa, menu)
        return true
    }

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
                    .title(getString(R.string.texto_nuevo_marcador))
                    .snippet(snippet)
                    //TODO: Cambiar de color al marcador:
                    //.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
            )
        }
    }

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

    private fun agregarCapaSuperpuesta(map: GoogleMap){
        val overlaySize = 100f
        val playaUnionLatLong = LatLng(-43.321762, -65.047201)

        val androidOverlay = GroundOverlayOptions()
            .image(BitmapDescriptorFactory.fromResource(R.drawable.tantos_5))
            .position(playaUnionLatLong, overlaySize)

        map.addGroundOverlay(androidOverlay)

    }

    private fun seOtorgoPermiso() : Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun activarUbicacion(map: GoogleMap) {
        if (seOtorgoPermiso()) {
            this.map.isMyLocationEnabled = true
        }
        else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION),
                SOLICITUD_PERMISO_UBICACION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == SOLICITUD_PERMISO_UBICACION) {
            if (grantResults.contains(PackageManager.PERMISSION_GRANTED)) {
                activarUbicacion(map)
            }
        }

    }

    private fun addHeatMap(map: GoogleMap) {
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
        var mOverlay = this.map.addTileOverlay(TileOverlayOptions().tileProvider(mProvider))
    }

    @Throws(JSONException::class)
    private fun readItems(resource: Int): ArrayList<LatLng?>? {
        val list = ArrayList<LatLng?>()
        val inputStream: InputStream = resources.openRawResource(resource)
        val json: String = Scanner(inputStream).useDelimiter("\\A").next()
        val array = JSONArray(json)
        for (i in 0 until array.length()) {
            val `object` = array.getJSONObject(i)
            val lat = `object`.getDouble("lat")
            val lng = `object`.getDouble("lng")
            list.add(LatLng(lat, lng))
        }
        return list
    }



}
