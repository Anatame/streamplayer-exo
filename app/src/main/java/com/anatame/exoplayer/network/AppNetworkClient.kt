package com.anatame.exoplayer.network

import android.util.Log
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.dnsoverhttps.DnsOverHttps
import timber.log.Timber


object AppNetworkClient {
    private var client: OkHttpClient? = null

    fun getClient(): OkHttpClient {
        return client ?: makeClient().also {
            client = it
        }
    }

    private fun makeClient(): OkHttpClient {

        val bootstrapClient = OkHttpClient.Builder()
            .cache(null)
            .build()

        val dns = DnsOverHttps.Builder().client(bootstrapClient)
            .url("https://cloudflare-dns.com/dns-query".toHttpUrl())
            .build()

        return bootstrapClient.newBuilder()
            .dns(dns)
            .addNetworkInterceptor { chain ->
                val newRequest = chain.request().newBuilder()
                    .build()

                Timber.tag("addInterceptorReq").d(newRequest.url.toString())

                chain.proceed(newRequest)
            }
            .build()
    }
}