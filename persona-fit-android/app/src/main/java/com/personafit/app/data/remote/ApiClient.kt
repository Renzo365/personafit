package com.personafit.app.data.remote

import android.content.Context
import android.os.Build
import com.personafit.app.data.local.SessionManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {

    /**
     * Automatically resolves the correct backend host:
     * - Inside Android Studio Emulator: 10.0.2.2 connects to host PC's localhost:8000
     * - On Physical Phone: Connects to host PC's LAN IP (192.168.1.92:8000) or configured BuildConfig.BASE_URL
     */
    fun getBaseUrl(): String {
        if (isEmulator()) {
            return "http://10.0.2.2:8000/api/"
        }
        return try {
            val configured = com.personafit.app.BuildConfig.BASE_URL
            if (configured.isNotBlank() && !configured.contains("10.0.2.2")) {
                configured
            } else {
                "http://192.168.1.92:8000/api/"
            }
        } catch (e: Throwable) {
            "http://192.168.1.92:8000/api/"
        }
    }

    private fun isEmulator(): Boolean {
        val fp = Build.FINGERPRINT.lowercase()
        val model = Build.MODEL.lowercase()
        val manufacturer = Build.MANUFACTURER.lowercase()
        val brand = Build.BRAND.lowercase()
        val device = Build.DEVICE.lowercase()
        val product = Build.PRODUCT.lowercase()
        val hardware = Build.HARDWARE.lowercase()

        return fp.startsWith("generic")
                || fp.startsWith("unknown")
                || fp.contains("emulator")
                || fp.contains("sdk")
                || model.contains("google_sdk")
                || model.contains("emulator")
                || model.contains("android sdk built for")
                || hardware.contains("goldfish")
                || hardware.contains("ranchu")
                || product.contains("sdk")
                || product.contains("emulator")
                || product.contains("google_sdk")
                || manufacturer.contains("genymotion")
                || (brand.startsWith("generic") && device.startsWith("generic"))
                || "google_sdk" == Build.PRODUCT
    }

    @Volatile
    private var apiService: PersonaFitApiService? = null

    fun getService(context: Context): PersonaFitApiService {
        return apiService ?: synchronized(this) {
            val sessionManager = SessionManager(context.applicationContext)

            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(AuthInterceptor(sessionManager))
                .addInterceptor(loggingInterceptor)
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(getBaseUrl())
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val service = retrofit.create(PersonaFitApiService::class.java)
            apiService = service
            service
        }
    }
}
