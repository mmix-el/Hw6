package xyz.mmixel.hw6.ui.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import xyz.mmixel.hw6.data.Contact
import xyz.mmixel.hw6.data.ContactsSource

private const val TAG = "ContactDetailsViewModel"

class ContactDetailsViewModel(
    private val contactsService: ContactsSource
) : ViewModel() {
    private val _contact = MutableLiveData<Contact>()
    val contact: LiveData<Contact>
        get() = _contact

    private var _newAvatar = MutableLiveData<String>()
    val newAvatar: LiveData<String>
        get() = _newAvatar

    fun loadContact(contactId: Int) {
        try {
            _contact.value = contactsService.getContactById(contactId)
        } catch (e: IllegalArgumentException) {
            Log.e(TAG, "${e.message}")
        }
    }

    fun editContact(contact: Contact) {
        contactsService.editContact(contact)
    }

    fun setNewAvatar(avatar: String) {
        _newAvatar.value = avatar
    }
}