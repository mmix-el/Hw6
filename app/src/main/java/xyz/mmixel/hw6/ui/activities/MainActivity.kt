package xyz.mmixel.hw6.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import xyz.mmixel.hw6.R
import xyz.mmixel.hw6.data.Contact
import xyz.mmixel.hw6.ui.fragments.ContactDetailsFragment
import xyz.mmixel.hw6.ui.fragments.ContactsFragment

class MainActivity : AppCompatActivity(), ContactsFragment.ContactClickListener,
    ContactDetailsFragment.EditButtonClickListener {
    private val isTablet by lazy { resources.getBoolean(R.bool.isTablet) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (supportFragmentManager.findFragmentByTag(ContactsFragment.CONTACTS_TAG) == null) {
            supportFragmentManager.beginTransaction().run {
                replace(
                    R.id.contactsContainer,
                    ContactsFragment.newInstance(),
                    ContactsFragment.CONTACTS_TAG
                )
                if (!isTablet) addToBackStack(ContactsFragment.CONTACTS_TAG)
                commit()
            }
        }
    }

    override fun onContactClicked(contact: Contact) {
        supportFragmentManager.beginTransaction().run {
            Bundle().putSerializable(ContactDetailsFragment.CONTACT_EXTRA, contact)
            if (!isTablet) {
                replace(
                    R.id.contactsContainer,
                    ContactDetailsFragment.newInstance(contact),
                    ContactDetailsFragment.CONTACT_DETAILS_TAG
                )
                addToBackStack(ContactDetailsFragment.CONTACT_DETAILS_TAG)
            } else {
                replace(
                    R.id.contactDetailsContainer,
                    ContactDetailsFragment.newInstance(contact),
                    ContactDetailsFragment.CONTACT_DETAILS_TAG
                )
            }
            commit()
        }
    }

    override fun onEditButtonClicked() {
        if (!isTablet) {
            supportFragmentManager.popBackStack()
        } else {
            supportFragmentManager.beginTransaction().run {
                replace(
                    R.id.contactsContainer,
                    ContactsFragment.newInstance(),
                    ContactsFragment.CONTACTS_TAG
                )
                commit()
            }
        }
    }

    override fun onContactLongClicked() {
        if (isTablet) {
            supportFragmentManager.beginTransaction().run {
                supportFragmentManager.findFragmentByTag(ContactDetailsFragment.CONTACT_DETAILS_TAG)
                    ?.let {
                        remove(it)
                    }
                commit()
            }
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 1 || isTablet) finish()
        supportFragmentManager.popBackStack()
    }
}
