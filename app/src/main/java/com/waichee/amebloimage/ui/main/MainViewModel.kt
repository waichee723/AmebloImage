package com.waichee.amebloimage.ui.main

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.waichee.amebloimage.ui.isAmeblo
import com.waichee.amebloimage.ui.isAmebloEntry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import timber.log.Timber
import java.io.IOException
import kotlin.reflect.typeOf

class MainViewModel : ViewModel() {

    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    val inputUrl = MutableLiveData<String>()

    private val _photos = MutableLiveData<List<String>>()
    val photos: LiveData<List<String>>
        get() = _photos

    private val _toast = MutableLiveData<String>()
    val toast: LiveData<String>
        get() = _toast

    init {
        inputUrl.value = ""
    }

    fun onGet() {

        _photos.value = emptyList()

        if (inputUrl.value == "") {
            _toast.value = "Url cannot be empty"
        } else if (!inputUrl.value!!.isAmeblo()) {
            _toast.value = "Url must be ameblo"
        } else if (!inputUrl.value!!.isAmebloEntry()) {
            _toast.value = "Url must be blog entry"
        } else {

            viewModelScope.launch {
                getData(inputUrl.value!!)
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
                    _toast.postValue("This blog has 0 images")
                }

                _photos.postValue(imagesString)

            } catch (e: IOException) {
                _toast.postValue("No Response. Please try again or check if Url is correct.")
            }
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