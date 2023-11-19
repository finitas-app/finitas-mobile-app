package pl.finitas.app

import android.app.Application
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import pl.finitas.app.di.koinApplication

class FinitasApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin(koinApplication(this))
    }

    override fun onTerminate() {
        super.onTerminate()
        stopKoin()
    }
}