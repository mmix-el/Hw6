package xyz.mmixel.hw6.data

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class ContactsPOJO(
    @SerializedName("contacts")
    val contacts: List<Contact>
)

typealias ContactsListener = (contacts: List<Contact>) -> Unit

class ContactsSource(jsonData: String) {
    private var contacts = mutableListOf<Contact>()
    private val listeners = mutableListOf<ContactsListener>()

    init {
        val contactsPOJO = Gson().fromJson(jsonData, ContactsPOJO::class.java)
        contacts = (contactsPOJO?.contacts ?: emptyList()) as MutableList<Contact>
    }

    fun getContactById(contactId: Int): Contact {
        return contacts.find { it.id == contactId } ?: throw IllegalArgumentException()
    }

    fun editContact(contact: Contact) {
        val indexToEdit = contacts.indexOfFirst { it.id == contact.id }
        if (indexToEdit != -1) {
            val newContact = Contact(
                id = contact.id,
                name = contact.name,
                surname = contact.surname,
                phone = contact.phone,
                email = contact.email,
                avatar = contact.avatar
            )
            contacts = ArrayList(contacts)
            contacts[indexToEdit] = newContact
            notifyChanges()
        }
    }

    fun deleteContact(contact: Contact) {
        val indexToDelete = contacts.indexOfFirst { it.id == contact.id }
        if (indexToDelete != -1) {
            contacts = ArrayList(contacts)
            contacts.removeAt(indexToDelete)
            notifyChanges()
        }
    }

    fun addListener(listener: ContactsListener) {
        listeners.add(listener)
        listener.invoke(contacts)
    }

    fun removeListener(listener: ContactsListener) {
        listeners.remove(listener)
    }

    private fun notifyChanges() {
        listeners.onEach { it.invoke(contacts) }
    }

    fun getContactIndex(contact: Contact): Int {
        return contacts.indexOfFirst { it.id == contact.id }
    }
}