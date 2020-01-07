package com.prateekgarage.trysocket

import com.tinder.scarlet.websocket.okhttp.newWebSocketFactory
import okhttp3.OkHttpClient

fun getOkHttpClient(client: OkHttpClient) =
client.newWebSocketFactory("ws://test.asdfd.com:2346/")