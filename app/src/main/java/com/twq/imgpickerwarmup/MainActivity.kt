package com.twq.imgpickerwarmup

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.github.dhaval2404.imagepicker.ImagePicker
import com.squareup.picasso.Picasso
import com.twq.imgpickerwarmup.databinding.ActivityMainBinding
import java.lang.Exception
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.widget.ImageView
import java.io.ByteArrayOutputStream


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var currentUser: User
    private val vm: UserViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)



        vm.getAllUsers().observe(this, {
            currentUser = it[0] // change index to call another user.

            // if the user has an image, else use avatar.
            if (currentUser.image.length > 20) {
                binding.imageViewAvatarOrImage.setImageBitmap(decodePicFromApi(currentUser.image))
            } else {
                Picasso.get().load(currentUser.avatar).into(binding.imageViewAvatarOrImage)
            }
            //set text name and text email
            binding.textViewName.text = currentUser.name
            binding.textViewEmail.text = currentUser.email
        })

        binding.cardViewofImage.setOnClickListener {
            onActivityResult(0, 0, intent)
            ImagePicker.with(this)
                .crop()    //Crop image(Optional), Check Customization for more option
                .compress(50)//Final image size will be less than 1 MB(Optional)
                .maxResultSize(
                    1080,
                    1080
                )    //Final image resolution will be less than 1080 x 1080(Optional)
                .start()
        }


        setContentView(binding.root)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            val uri: Uri = data?.data!!
            // Use Uri object instead of File to avoid storage permissions
            binding.imageViewAvatarOrImage.setImageURI(uri)
            encodePicAndUploadToApi(uri)


        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    fun encodePicAndUploadToApi (uri: Uri){
        val bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri )

        // initialize byte stream
        val stream = ByteArrayOutputStream()

        // compress Bitmap
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)

        // Initialize byte array
        val bytes: ByteArray = stream.toByteArray()
        // get base64 encoded string
        val encodedImage = Base64.encodeToString(bytes, Base64.DEFAULT)
        // set encoded text on textview
        println(encodedImage)
        vm.updateImg(currentUser.id,User(currentUser.avatar,currentUser.email,currentUser.id,
            encodedImage,currentUser.name))
    }
    fun decodePicFromApi (encodedString: String): Bitmap {

        val imageBytes = Base64.decode(encodedString, Base64.DEFAULT)
        val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

        return decodedImage
    }
}
