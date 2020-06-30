package com.waichee.amebloimage.ui.main

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.waichee.amebloimage.R
import com.waichee.amebloimage.isAmeblo
import com.waichee.amebloimage.isAmebloEntry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.URI
import java.net.URL


class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    val inputUrl = MutableLiveData<String>()

    private val _photos = MutableLiveData<List<String>>()
    val photos: LiveData<List<String>>
        get() = _photos

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _toast = MutableLiveData<Int>()
    val toast: LiveData<Int>
        get() = _toast

    init {
        inputUrl.value = ""
        _isLoading.value = false
    }

    fun onGet() {

        _photos.value = emptyList()


        // Checking for input text
        if (inputUrl.value == "") {
            _toast.value = R.string.empty_url_toast
        } else if (!inputUrl.value!!.isAmeblo()) {
            _toast.value = R.string.non_ameblo_url_toast
        } else if (!inputUrl.value!!.isAmebloEntry()) {
            _toast.value = R.string.not_blog_entry_toast
        } else {
            viewModelScope.launch {
                _isLoading.value = true
                getData(inputUrl.value!!)
                _isLoading.value = false
            }
        }


    }

    private suspend fun getData(url: String) {
        withContext(Dispatchers.IO) {
            try {
                val document = Jsoup.connect(url).get()
                val images = document.getElementsByClass("PhotoSwipeImage")

                val imagesString: List<String> = images.map { it.attr("src") }

                if (imagesString.isEmpty()) {
                    _toast.postValue(R.string.no_photo_toast)

                }
                _photos.postValue(imagesString)
            } catch (e: IOException) {
                _toast.postValue(R.string.no_response_toast)
            }
        }
    }

    private suspend fun saveImage(b: Bitmap, imageName: String) {
        var foStream: OutputStream
        val saveImage = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            imageName
        )

        withContext(Dispatchers.IO) {
            try {
                foStream = FileOutputStream(saveImage)
                b.compress(Bitmap.CompressFormat.PNG, 100, foStream);
                foStream.close();
            } catch (e: Exception) {
                e.printStackTrace();
            }
        }
    }

    private suspend fun downloadImageBitmap(imageUrl: String): Bitmap? {
        var bitmap: Bitmap? = null

        withContext(Dispatchers.IO) {
            try {
                val inputStream: InputStream = URL(imageUrl).openStream()
                bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return bitmap
    }

    fun onDownload() {
        viewModelScope.launch {
            _isLoading.value = true
            photos.value?.forEach {
                val image: Bitmap? = downloadImageBitmap(it)
                val uri = URI(it)
                val imageName = File(uri.path).name
                if (image != null) {
                    saveImage(image, imageName)
                }
            }
            _toast.postValue(R.string.download_complete_toast)
            _isLoading.value = false
        }
    }

    fun completeDisplayToast() {
        _toast.value = null
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}