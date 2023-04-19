package com.atulya.photogallery

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.atulya.photogallery.core.utils.PERMISSION_REQUEST_CODE
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

class MainActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            ensurePermissions()
        }

    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun hasPermissions(): Boolean = EasyPermissions.hasPermissions(
        applicationContext,
        Manifest.permission.POST_NOTIFICATIONS
    )
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun ensurePermissions() {
        if (!hasPermissions()) {
            EasyPermissions.requestPermissions(
                this,
                "Access to camera and microphone is needed to record video. Please allow when prompted.",
                PERMISSION_REQUEST_CODE,
                Manifest.permission.POST_NOTIFICATIONS
            )
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        Log.d("#> ${this::class.simpleName}", "Permission Granted")
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                ensurePermissions()
            }
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }


    companion object{
        fun newIntent(context: Context) = Intent(context, MainActivity::class.java)
    }
}