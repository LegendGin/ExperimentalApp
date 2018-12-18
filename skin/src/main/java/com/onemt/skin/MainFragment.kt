package com.onemt.skin

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.PermissionChecker
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.main_frag.*

/**
 * @author: chenjinghang
 * @version: 1.0.0
 * @date: 2018/12/14 17:20
 * @see
 */
class MainFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.main_frag, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hello.setOnClickListener {
            if (PermissionChecker.checkSelfPermission(activity!!, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity!!, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Toast.makeText(activity!!, "need storage permission", Toast.LENGTH_SHORT).show()
                }
                this@MainFragment.requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 0)
            } else {
                SkinManager.change(activity!!)
            }
        }

        btn_recycler.setOnClickListener {
            startActivity(Intent(activity, RecyclerActivity::class.java))
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            0 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(activity!!, "granted", Toast.LENGTH_SHORT).show()
                    SkinManager.change(activity!!)
                } else {
                    Toast.makeText(activity!!, "not granted", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}