package com.example.myapplication

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.example.myapplication.network.NetworkClient
import com.example.myapplication.network.RepoImage
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var imageGallery: ImageView
    private lateinit var imageBtn: Button

    companion object {
        private val IMAGE_PICK_CODE = 1000
        private val PERMISSION_CODE = 1001

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sample_image)

        initComponent()

        imageBtn.setOnClickListener {
//            checkPermission()
            val permission = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            requestPermissions(permission, PERMISSION_CODE)
        }
    }

    private fun initComponent() {
        imageGallery = findViewById(R.id.img_view)
        imageBtn = findViewById(R.id.img_btn)
    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            getPicture()
        } else {
            val permission = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            requestPermissions(permission, PERMISSION_CODE)
        }
    }

    private fun getPicture() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    private fun getPath(uri: Uri): String {
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        var cursor = contentResolver.query(uri, filePathColumn, null, null, null)
        assert(cursor != null)
        cursor!!.moveToFirst()

        val columnIndex = cursor.getColumnIndex(filePathColumn[0])
        val mediaPath = cursor.getString(columnIndex)

        return mediaPath
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getPicture()
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            val imageFile = File(getPath(data?.data!!))
            imageGallery.setImageURI(imageFile.toUri())

            Log.i("DATA/IMAGE", imageFile.toString())


            RepoImage().uploadImage(imageFile)
        }
    }
}