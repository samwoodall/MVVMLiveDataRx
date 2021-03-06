package com.samwoodall.mvvmlivedatarx.LiveDataRx

import androidx.lifecycle.*
import com.samwoodall.mvvmlivedatarx.Repository
import com.samwoodall.mvvmlivedatarx.SignInRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class MainViewModel(
    private val repo: Repository = Repository(),
    private val signInRepository: SignInRepository = SignInRepository()
) : ViewModel(),
    LifecycleObserver {

    private val mainViewModelData: MutableLiveData<MainViewModelData> =
        MutableLiveData<MainViewModelData>().apply {
            value = MainViewModelData.Loading
        }

    private val disposable: CompositeDisposable = CompositeDisposable()

    fun getMainViewModel(): LiveData<MainViewModelData> = mainViewModelData

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun start() {
        disposable.add(signInRepository.getOauthToken()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                mainViewModelData.value = MainViewModelData.Complete("", 3, it)
            })

        disposable.add(signInRepository.getOauthToken().flatMap {
            repo.getData(it)
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                mainViewModelData.value = MainViewModelData.Complete("", 3, it)
            })
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stop() {
        disposable.dispose()
    }

}

sealed class MainViewModelData {
    object Loading : MainViewModelData()
    data class Complete(val userName: String, val userAge: Int, val userDesc: String) :
        MainViewModelData()
}

