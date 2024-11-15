package org.techtown.volleyball.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

abstract class BaseDialogFragment<V : ViewDataBinding> : DialogFragment() {
    protected lateinit var binding: V

    private val compositeDisposable = CompositeDisposable()
    private val progress: DefaultProgress by lazy { DefaultProgress(requireContext()) }

    protected abstract val layoutResID: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setStyle(STYLE_NO_TITLE, R.style.DialogFullScreen)

        isCancelable = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = createBinding(inflater, container, layoutResID)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

    open fun createBinding(inflater: LayoutInflater, container: ViewGroup?, layoutResID: Int): V {
        return DataBindingUtil.inflate(inflater, layoutResID, container, false)
    }

    open fun addDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    fun showProgress(isShow: Boolean) {
        progress.show(isShow)
    }
}