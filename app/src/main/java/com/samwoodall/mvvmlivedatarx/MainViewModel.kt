package com.samwoodall.mvvmlivedatarx

import androidx.lifecycle.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class MainViewModel(
    private val repo: Repository = Repository(),
    private val signInRepository: SignInRepository = SignInRepository()
) : ViewModel(),
    LifecycleObserver {

    private val mainViewModelData: MutableLiveData<MainViewModelData> =
        MediatorLiveData<MainViewModelData>().apply {
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

data class UserData(val userName: String, val userAge: Int)

class SignInRepository {
    fun getOauthToken(): Observable<String> = Observable.just("one", "two", "three", "four", "five")

}

class Repository {
    fun getData(auth: String): Observable<String> =
        Observable.just("one", "two", "three", "four", "five")
}