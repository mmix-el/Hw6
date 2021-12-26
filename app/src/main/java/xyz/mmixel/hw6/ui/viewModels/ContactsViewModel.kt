package xyz.mmixel.hw6.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import xyz.mmixel.hw6.data.Contact
import xyz.mmixel.hw6.data.ContactsListener
import xyz.mmixel.hw6.data.ContactsSource

class ContactsViewModel(
    private val contactsService: ContactsSource
) : ViewModel() {
    private val _contacts = MutableLiveData<List<Contact>>()
    val contacts: LiveData<List<Contact>>
        get() = _contacts

    private val listener: ContactsListener = {
        _contacts.value = it
    }

    init {
        loadContacts()
    }

    override fun onCleared() {
        super.onCleared()
        contactsService.removeListener(listener)
    }

    private fun loadContacts() {
        contactsService.addListener(listener)
    }

    fun deleteContact(contact: Contact) {
        contactsService.deleteContact(contact)
    }

    fun filterContacts(query: String?): List<Contact> {
        return contacts.value?.let { contactsList ->
            if (query.isNullOrEmpty()) {
                contactsList
            } else {
                val pattern = query.lowercase().trim()
                contactsList.filter {
                    if (pattern.contains(' ')) {
                        "${it.name} ${it.surname}".lowercase().contains(pattern)
                    } else {
                        it.name.lowercase().startsWith(pattern)
                                || it.surname.lowercase().startsWith(pattern)
                    }
                }
            }
        } ?: emptyList()
    }

    fun getContactIndex(contact: Contact): Int {
        return contactsService.getContactIndex(contact)
    }
}
