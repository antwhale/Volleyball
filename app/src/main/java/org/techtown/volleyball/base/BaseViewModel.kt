package org.techtown.volleyball.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

abstract class BaseViewModel(application: Application): AndroidViewModel(application) {
    protected val TAG = this::class.java.simpleName
    val compositeDisposable = CompositeDisposable()

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _alert = MutableLiveData<String>()
    val alert: LiveData<String>
        get() = _alert

    /*private val _customException = MutableLiveData<Exception>()
    val customException: LiveData<Exception>
        get() = _customException*/

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    open fun addDisposable(disposable: Disposable){
        compositeDisposable.add(disposable)
    }

    fun showLoading(isShow: Boolean){
        _isLoading.postValue(isShow)
    }

    fun showAlert(message: String){
        _alert.postValue(message)
    }

    /*fun handleException(exception: Exception) {
        showLoading(false)
        Log.e(TAG, "handleException: ${exception.message}", exception)
        _customException.postValue(exception)
    }*/
}