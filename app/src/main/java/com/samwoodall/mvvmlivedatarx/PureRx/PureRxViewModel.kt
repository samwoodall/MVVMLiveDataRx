package com.samwoodall.mvvmlivedatarx.PureRx

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import com.samwoodall.mvvmlivedatarx.LiveDataRx.MainViewModelData
import com.samwoodall.mvvmlivedatarx.Repository
import com.samwoodall.mvvmlivedatarx.SignInRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class PureRxViewModel(private val repo: Repository = Repository(), private val signInRepository: SignInRepository = SignInRepository()) : ViewModel(),
    LifecycleObserver {

    private val disposable: CompositeDisposable = CompositeDisposable()

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun start() {
        disposable.add(signInRepository.getOauthToken()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                MainViewModelData.Complete("", 3, it)
            })

        disposable.add(signInRepository.getOauthToken().flatMap {
            repo.getData(it)
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                MainViewModelData.Complete("", 3, it)
            })
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stop() {
        disposable.dispose()
    }
}
