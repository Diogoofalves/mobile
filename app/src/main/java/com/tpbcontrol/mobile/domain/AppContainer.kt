package com.tpbcontrol.mobile.domain

import android.content.Context
import com.tpbcontrol.mobile.data.TPBRepository
import com.tpbcontrol.mobile.data.local.TokenStore
import com.tpbcontrol.mobile.data.remote.NetworkModule

class AppContainer(context: Context) {
    private val tokenStore: TokenStore = TokenStore(context)
    private val api = NetworkModule.provideApi(tokenStore)

    val repository: TPBRepository = TPBRepository(api, tokenStore)
}
