package com.kakaomapalarm.utils

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MapUtils
{
    companion object {
        private const val GPS_ENABLE_REQUEST_CODE = 2001
        private const val PERMISSIONS_REQUEST_CODE = 2001

        fun CheckLocationServiceState(activity: Activity): Boolean {
            val locationManager: LocationManager = activity.getSystemService(LOCATION_SERVICE) as LocationManager

            return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                    || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        }

        fun ShowDialogForLocationServiceSetting(activity: Activity)
        {
            val alertBuilder: AlertDialog.Builder = AlertDialog.Builder(activity)
            alertBuilder.setTitle("위치 서비스 비활성화")
            alertBuilder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                                  + "위치 설정을 수정하실래요?")
            alertBuilder.setCancelable(true)

            alertBuilder.setPositiveButton("설정", DialogInterface.OnClickListener() { dialog, id ->
                val callGPSIntent = Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                activity.startActivityForResult(callGPSIntent, GPS_ENABLE_REQUEST_CODE)
            })

            alertBuilder.setNegativeButton("취소", DialogInterface.OnClickListener() { dialog, id ->
                dialog.cancel()
            })

            alertBuilder.create().show()
        }

        fun CheckRunTimePermission(activity: Activity): Boolean
        {
            // 위치 퍼미션을 가지고 있는지 체크
            val hasFineLocationPermission: Int = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
            if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED)
            {
                // 이미 퍼미션을 가지고 있다면
                // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)
                return true
            } else {
                // 사용자가 퍼미션 거부를 한 적이 있는 경우
                return !ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }
}