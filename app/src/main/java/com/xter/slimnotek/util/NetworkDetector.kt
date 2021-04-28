package com.xter.slimnotek.util

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.*
import android.os.Build
import android.telephony.TelephonyManager
import android.util.Log

/**
 * @author xter
 * 项目名称: SlimNote
 * 创建时间: 2020-09-12
 * 描述:网络监听
 */
class NetworkDetector constructor(context: Context, observer: NetworkObserver) :
    BroadcastReceiver() {

    private val intentFilter: IntentFilter
    private val mContext: Context
    private val mConnectivityManager: ConnectivityManager
    private val mObserver: NetworkObserver

    init {
        mContext = context
        mObserver = observer
        intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        mConnectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        registerReceiver()

        if (supportNetworkCallback()) {
            try {
                requestMobileNetwork(ConnectivityManager.NetworkCallback())
            } catch (e: SecurityException) {
                Log.w("NetworkDetector","no permission")
            }
            registerNetworkCallback(SimpleNetworkCallback())
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent?.action)) {
            val networkInfo = mConnectivityManager.activeNetworkInfo
            if (networkInfo == null || networkInfo.isConnected) {
                mObserver.onNetworkDisconnect()
            } else {
                mObserver.onNetworkConnected(
                    getConnectionType(
                        networkInfo.type,
                        networkInfo.subtype
                    )
                )
            }
        }
    }

    private fun registerReceiver() {
        mContext.registerReceiver(this, intentFilter)
    }

    /**
     * Only callable on Lollipop and newer releases.
     */
    @SuppressLint("NewApi")
    fun registerNetworkCallback(networkCallback: ConnectivityManager.NetworkCallback) {
        mConnectivityManager.registerNetworkCallback(
            NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build(),
            networkCallback
        )
    }

    /**
     * Only callable on Lollipop and newer releases.
     */
    @SuppressLint("NewApi")
    fun requestMobileNetwork(networkCallback: ConnectivityManager.NetworkCallback) {
        val builder = NetworkRequest.Builder()
        builder.addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        mConnectivityManager.requestNetwork(builder.build(), networkCallback)
    }

    fun supportNetworkCallback(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
    }

    @SuppressLint("NewApi")
    inner class SimpleNetworkCallback : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            onNetworkChanged(network)
        }

        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            onNetworkChanged(network)
        }

        override fun onLinkPropertiesChanged(network: Network, linkProperties: LinkProperties) {
            onNetworkChanged(network)
        }

        override fun onLosing(network: Network, maxMsToLive: Int) {
            L.d("Network " + network.toString() + " is about to lose in " + maxMsToLive + "ms")
        }

        override fun onLost(network: Network) {
            L.d("Network $network is disconnected")
            mObserver.onNetworkDisconnect()
        }

        private fun onNetworkChanged(network: Network) {
            mConnectivityManager.getLinkProperties(network)?.let { linkProperties ->
                if (linkProperties.interfaceName != null) {
                    val networkInfo = mConnectivityManager.getNetworkInfo(network)
                    notifyObserver(networkInfo)
                }
            }
        }
    }

    private fun notifyObserver(networkInfo: NetworkInfo?) {
        if (networkInfo == null || networkInfo.isConnected) {
            mObserver.onNetworkDisconnect()
        } else {
            mObserver.onNetworkConnected(
                getConnectionType(
                    networkInfo.type,
                    networkInfo.subtype
                )
            )
        }
    }

    private fun getConnectionType( networkType: Int, networkSubtype: Int
    ): ConnectionType {
        when (networkType) {
            ConnectivityManager.TYPE_ETHERNET -> return ConnectionType.CONNECTION_ETHERNET
            ConnectivityManager.TYPE_WIFI -> return ConnectionType.CONNECTION_WIFI
            ConnectivityManager.TYPE_WIMAX -> return ConnectionType.CONNECTION_4G
            ConnectivityManager.TYPE_BLUETOOTH -> return ConnectionType.CONNECTION_BLUETOOTH
            ConnectivityManager.TYPE_MOBILE ->
                // Use information from TelephonyManager to classify the connection.
                when (networkSubtype) {
                    TelephonyManager.NETWORK_TYPE_GPRS, TelephonyManager.NETWORK_TYPE_EDGE, TelephonyManager.NETWORK_TYPE_CDMA, TelephonyManager.NETWORK_TYPE_1xRTT, TelephonyManager.NETWORK_TYPE_IDEN -> return ConnectionType.CONNECTION_2G
                    TelephonyManager.NETWORK_TYPE_UMTS, TelephonyManager.NETWORK_TYPE_EVDO_0, TelephonyManager.NETWORK_TYPE_EVDO_A, TelephonyManager.NETWORK_TYPE_HSDPA, TelephonyManager.NETWORK_TYPE_HSUPA, TelephonyManager.NETWORK_TYPE_HSPA, TelephonyManager.NETWORK_TYPE_EVDO_B, TelephonyManager.NETWORK_TYPE_EHRPD, TelephonyManager.NETWORK_TYPE_HSPAP -> return ConnectionType.CONNECTION_3G
                    TelephonyManager.NETWORK_TYPE_LTE -> return ConnectionType.CONNECTION_4G
                    else -> return ConnectionType.CONNECTION_UNKNOWN_CELLULAR
                }
            ConnectivityManager.TYPE_VPN -> return ConnectionType.CONNECTION_VPN
            else -> return ConnectionType.CONNECTION_UNKNOWN
        }
    }
}

interface NetworkObserver {
    fun onNetworkConnected(connectionType: ConnectionType)
    fun onNetworkDisconnect()
}

enum class ConnectionType {
    CONNECTION_UNKNOWN,
    CONNECTION_ETHERNET,
    CONNECTION_WIFI,
    CONNECTION_4G,
    CONNECTION_3G,
    CONNECTION_2G,
    CONNECTION_UNKNOWN_CELLULAR,
    CONNECTION_BLUETOOTH,
    CONNECTION_VPN,
}