package com.prateekgarage.trysocket

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.WebSocket
import com.tinder.scarlet.messageadapter.gson.GsonMessageAdapter
import com.tinder.scarlet.retry.LinearBackoffStrategy
import com.tinder.scarlet.streamadapter.rxjava2.RxJava2StreamAdapterFactory
import com.tinder.scarlet.websocket.okhttp.newWebSocketFactory
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import okhttp3.OkHttpClient


class MainActivity : AppCompatActivity() {

    private lateinit var textView: TextView
    private lateinit var button: Button
    private lateinit var editText: EditText
    private lateinit var color: ImageView
    var disposable: Disposable? = null
    var disposable1: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textView = findViewById(R.id.text)
        button = findViewById(R.id.button)
        editText = findViewById(R.id.edit)
        color = findViewById(R.id.color)


        val client = OkHttpClient()

        val scarletInstance = Scarlet.Builder()
            .webSocketFactory(getOkHttpClient(client))
            .addMessageAdapterFactory(GsonMessageAdapter.Factory())
            .addStreamAdapterFactory(RxJava2StreamAdapterFactory())
            .backoffStrategy(LinearBackoffStrategy(1000))
            .build()


        val gdaxService = scarletInstance.create<GdaxService>()


        val BITCOIN_TICKER_SUBSCRIBE_MESSAGE = "adarsh: how are u?"

        disposable = gdaxService.observeWebSocketEvent()
            .subscribe({ event ->
                if (event is WebSocket.Event.OnConnectionOpened<*>) {
                    color.setColorFilter(
                        ContextCompat.getColor(
                            MainActivity@ this,
                            R.color.green
                        )
                    )
                } else if (event is WebSocket.Event.OnConnectionFailed) {

                    color.setColorFilter(
                        ContextCompat.getColor(
                            MainActivity@ this,
                            R.color.red
                        )
                    )
                }

            },
                { e ->

                    Log.i("CHECK8899", e.toString())
                    color.setColorFilter(
                        ContextCompat.getColor(
                            MainActivity@ this,
                            R.color.red
                        )
                    )

                }, {

                    color.setColorFilter(
                        ContextCompat.getColor(
                            MainActivity@ this,
                            R.color.colorPrimary
                        )
                    )

                })


        disposable1 = gdaxService.observeTicker()
            .subscribe { ticker ->
                runOnUiThread {
                    textView.text = ticker
                }

            }

        button.setOnClickListener {
            gdaxService.sendSubscribe(editText.text.toString())
        }


    }

    fun getOkHttpClient(client: OkHttpClient) =
        client.newWebSocketFactory("wss://sandconsole.hubblerapp.com/ws/echo/")


    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
        disposable1?.dispose()
    }
}
