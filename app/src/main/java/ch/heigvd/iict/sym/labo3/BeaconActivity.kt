package ch.heigvd.iict.sym.labo3

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.lifecycle.Observer
import android.widget.Button
import org.altbeacon.beacon.BeaconManager

import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import org.altbeacon.beacon.Beacon
import org.altbeacon.beacon.BeaconParser
import org.altbeacon.beacon.Region

import android.widget.BaseAdapter
import android.widget.TextView
import ch.heigvd.iict.sym.labo3.R
import java.sql.Timestamp
import java.util.*
import kotlin.collections.ArrayList


class BeaconActivity : AppCompatActivity()  {

    private var beaconManager: BeaconManager? = null

    companion object{
        private var TIME_LIMIT = 5000
        private var BEACON_LAYOUT = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"
    }


    private lateinit var beaconListView: ListView

    private lateinit var beaconArray : ArrayList<Beacon>
    private lateinit var beaconAdapter:  MyAdapter
    // This property is only valid between onCreateView and
    // onDestroyView.

    private val PERMISSION_REQUEST_COARSE_LOCATION = 1


    private class MyAdapter(val data: MutableList<Beacon>,var activity: Activity) :
        BaseAdapter() {



        override fun getCount(): Int {
            return data.size
        }

        override fun getItem(position: Int): Beacon? {
            return data.getOrNull(position)
        }

            override fun getItemId(position: Int): Long {
                return position.toLong()
            }

            @SuppressLint("ViewHolder", "SetTextI18n")
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val result = View.inflate(activity,R.layout.beacon_list_view,null)
                val item = getItem(position)

                (result.findViewById<View>(R.id.UUID) as TextView).text = "UUID = " + item?.id1.toString()
                (result.findViewById<View>(R.id.major) as TextView).text = "Minor = " +  item?.id2.toString()
                (result.findViewById<View>(R.id.minor) as TextView).text = "Major = " + item?.id3.toString()
                (result.findViewById<View>(R.id.rssi) as TextView).text = "Rssi = " + item?.rssi.toString()
                (result.findViewById<View>(R.id.last_detected) as TextView).text = "Last detected = " + item?.lastCycleDetectionTimestamp?.let {
                    Timestamp(
                        it
                    )
                }

                return result
            }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ibeacon)

        beaconArray = ArrayList()
        beaconAdapter = MyAdapter(beaconArray,this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Demander l'access à la localisation
            if ((this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
                var  builder =  AlertDialog.Builder(this);
                builder.setTitle("This app needs location access");
                builder.setMessage("Please grant location access so this app can detect beacons.");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener {
                    requestPermissions( arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ), PERMISSION_REQUEST_COARSE_LOCATION);
                    startRanging()
                }
                builder.show();
            }
            else{
                startRanging()
            }
        }


        beaconListView = findViewById(R.id.beacon_list)
        beaconListView.adapter = beaconAdapter

    }

    private fun startRanging(){
        //permet de preparer et démarrer la détection de beacon
        beaconManager =  BeaconManager.getInstanceForApplication(this)
        beaconManager?.beaconParsers?.add(
            BeaconParser()
                .setBeaconLayout(BEACON_LAYOUT)
        )
        val region = Region("allRegions",null,null,null)
        beaconManager?.getRegionViewModel(region)?.rangedBeacons?.observe(this,monitoringObserver)
        beaconManager?.startRangingBeacons(region)
    }
    private val monitoringObserver = Observer<Collection<Beacon>>{
            beacons ->

        var currentTime = Calendar.getInstance().time

        Log.d("Range","Ranged ${beacons.count()}")
        for(beacon : Beacon in beacons){
            Log.d("Range","In about ${beacon.distance} meters away")
            if(!beaconArray.contains(beacon)){
                //ajouter le beacon si il appartient pas déjà à la liste
                beaconArray.add(beacon)
                beaconAdapter.notifyDataSetChanged()
            }else{

                //Principalement pour mettre a jour la dernière détection
                Log.d("update","${beacon.id1}")
                beaconArray[beaconArray.indexOf(beacon)] = beacon
                beaconAdapter.notifyDataSetChanged()
            }
        }

        //supprimer de la liste si le beacon n'as pas été detecte pendant X secondes
        val iterator = beaconArray.iterator()
        while(iterator.hasNext()){
            val item = iterator.next()
            if(currentTime.time - item.lastCycleDetectionTimestamp > TIME_LIMIT){
                iterator.remove()
            }
        }
    }
}