package xyz.mmixel.hw6.ui.viewModels

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import xyz.mmixel.hw6.app.App

class ViewModelFactory(
    private val app: App
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val viewModel = when (modelClass) {
            ContactsViewModel::class.java -> {
                ContactsViewModel(app.contactsSource)
            }
            ContactDetailsViewModel::class.java -> {
                ContactDetailsViewModel(app.contactsSource)
            }
            else -> {
                throw IllegalStateException("Unknown ViewModel class")
            }
        }
        return viewModel as T
    }
}

fun Fragment.factory() = ViewModelFactory(requireContext().applicationContext as App)