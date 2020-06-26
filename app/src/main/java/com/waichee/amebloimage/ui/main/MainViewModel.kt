package com.waichee.amebloimage.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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

    private val url: String = "https://ameblo.jp/tsubaki-factory/entry-12606831572.html"

    private val _photos = MutableLiveData<List<String>>()
    val photos: LiveData<List<String>>
        get() = _photos


    fun onGet() {
        viewModelScope.launch {
            getData()
        }
    }

    private suspend fun getData() {
        Timber.i("getData executed")
        withContext(Dispatchers.IO) {
            try {
                val document = Jsoup.connect(url).get()
                val images = document.getElementsByClass("PhotoSwipeImage")
                images.forEach {
                    Timber.i(it.attr("src"))
                }
                val imagesString: List<String> = images.map { it.attr("src") }
                _photos.postValue(imagesString)

                Timber.i("title is ${document.title()}")

            } catch (e: IOException) {
                Timber.e("Error")
            }
        }
    }


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}