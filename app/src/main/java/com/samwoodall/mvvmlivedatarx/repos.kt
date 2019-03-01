package com.samwoodall.mvvmlivedatarx

import io.reactivex.Observable
import io.reactivex.Single

class SignInRepository {
    fun getOauthToken(): Observable<String> = Observable.just("one", "two", "three", "four", "five")

}

class Repository {
    fun getData(auth: String): Observable<String> =
        Observable.just("one", "two", "three", "four", "five")

    fun getOneTimeEvent() = Single.just("one time")
}

data class UserData(val userName: String, val userAge: Int)