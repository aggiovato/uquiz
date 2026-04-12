package com.uquiz.android

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.annotation.RequiresApi
import androidx.compose.runtime.LaunchedEffect
import com.uquiz.android.core.database.DatabaseModule
import com.uquiz.android.core.reminder.builder.createReminderNotificationChannel
import com.uquiz.android.ui.navigation.AppNavGraph
import com.uquiz.android.ui.rememberAppRepositories

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        createReminderNotificationChannel(this)

        val database = DatabaseModule.getDatabase(this, allowDestructiveMigration = true)

        setContent {
            val repositories = rememberAppRepositories(this, database)
            LaunchedEffect(repositories) {
                try {
                    repositories.appBootstrapper.ensureReady()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            AppNavGraph(repositories = repositories)
        }
    }
}
