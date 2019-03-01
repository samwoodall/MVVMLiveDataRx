package com.samwoodall.mvvmlivedatarx.LiveDataRxReactiveStreams

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.LiveDataReactiveStreams.fromPublisher
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.samwoodall.mvvmlivedatarx.LiveDataRx.MainViewModelData
import com.samwoodall.mvvmlivedatarx.Repository
import com.samwoodall.mvvmlivedatarx.SignInRepository
import com.samwoodall.mvvmlivedatarx.addOneTimeSource
import io.reactivex.BackpressureStrategy

class LiveDataReactiveStreamsRxViewModel(
    private val repo: Repository = Repository(),
    private val signInRepository: SignInRepository = SignInRepository()) : ViewModel() {

    private val mainViewModelData: MediatorLiveData<MainViewModelData> =
        MediatorLiveData<MainViewModelData>().apply {
            value = MainViewModelData.Loading
        }

    fun getMainViewModel(): LiveData<MainViewModelData> = mainViewModelData

    init {
        val signInRepoData = fromPublisher(signInRepository.getOauthToken().toFlowable(BackpressureStrategy.LATEST))
        val repoData = fromPublisher(signInRepository.getOauthToken().flatMap { repo.getData(it) }.toFlowable(BackpressureStrategy.LATEST))

        mainViewModelData.addSource(repoData) { userData ->
            mainViewModelData.value = MainViewModelData.Complete(userData, 3, userData)
        }

        mainViewModelData.addSource(signInRepoData) { user1 ->
            mainViewModelData.value = MainViewModelData.Complete(user1, 2, user1)
        }
    }

    fun oneTimeCall() {
        mainViewModelData.addOneTimeSource(LiveDataReactiveStreams.fromPublisher<String>(repo.getOneTimeEvent().toFlowable())) {
            value = MainViewModelData.Complete(it, 2, it)
        }
    }
}