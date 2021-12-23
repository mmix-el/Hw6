package xyz.mmixel.hw6.app

import android.app.Application
import android.content.Context
import xyz.mmixel.hw6.data.ContactsSource
import java.io.IOException

class App : Application() {
    lateinit var contactsSource: ContactsSource

    override fun onCreate() {
        super.onCreate()
        val json = readJSONFromAsset(this, "mock_data.json")
        contactsSource = ContactsSource(json)
    }

    companion object {
        fun readJSONFromAsset(context: Context, fileName: String): String {
            return try {
                context.assets.open(fileName).run {
                    val buffer = ByteArray(available())
                    use { it.read(buffer) }
                    String(buffer)
                }
            } catch (e: IOException) {
                ""
            }
        }
    }
}