package lulukprojects.musicaltuner.requester

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

class PermissionRequester
{
    private val permissions: List<String>
    private val PERMISSIONS_REQUEST_CODE: Int
    private val activity: Activity

    constructor(activity: Activity, permissions: List<String>, PERMISSIONS_REQUEST_CODE: Int) {
        this.activity = activity
        this.permissions = permissions
        this.PERMISSIONS_REQUEST_CODE = PERMISSIONS_REQUEST_CODE
    }

    private fun getPermissionsToRequest(): List<String> {
        var ret = permissions.toMutableList()
        for (permission in permissions.toList())
        {
            if( ContextCompat.checkSelfPermission(
                    activity,
                    permission
                ) == PackageManager.PERMISSION_GRANTED)
            {
                ret.remove(permission)
            }
        }
        return ret.toList()
    }

    fun requestPermissions() {
        val requiredPermissions = getPermissionsToRequest()
        if(requiredPermissions.isNotEmpty())
            activity.requestPermissions(requiredPermissions.toTypedArray(), PERMISSIONS_REQUEST_CODE)
    }

    fun isPermissionGranted(): Boolean {
        return  getPermissionsToRequest().isEmpty()
    }
}