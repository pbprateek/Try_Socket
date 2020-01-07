package com.prateekgarage.trysocket

import com.tinder.scarlet.WebSocket
import com.tinder.scarlet.ws.Receive
import com.tinder.scarlet.ws.Send
import io.reactivex.Flowable

interface GdaxService {
    @Receive
    fun observeWebSocketEvent(): Flowable<WebSocket.Event>

    @Send
    fun sendSubscribe(subscribe: String)

    @Receive
    fun observeTicker(): Flowable<String>
}