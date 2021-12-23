package xyz.mmixel.hw6.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import xyz.mmixel.hw6.R
import xyz.mmixel.hw6.data.Contact
import xyz.mmixel.hw6.databinding.FragmentContactsBinding
import xyz.mmixel.hw6.ui.adapters.ContactActionListener
import xyz.mmixel.hw6.ui.adapters.ContactsAdapter
import xyz.mmixel.hw6.ui.decoration.ContactDecoration
import xyz.mmixel.hw6.ui.viewModels.ContactsViewModel
import xyz.mmixel.hw6.ui.viewModels.factory

class ContactsFragment : Fragment(R.layout.fragment_contacts),
    DeleteContactDialogFragment.PositiveButtonClickListener {
    private lateinit var binding: FragmentContactsBinding
    private lateinit var contactsAdapter: ContactsAdapter
    private val viewModel: ContactsViewModel by viewModels { factory() }
    private var contactClickListener: ContactClickListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        contactClickListener = context as ContactClickListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_contacts, container,
            false
        )
        contactsAdapter = ContactsAdapter(object : ContactActionListener {
            override fun onContactClick(contact: Contact) {
                contactClickListener?.onContactClicked(contact)
            }
            override fun onContactLongClick(contact: Contact) {
                DeleteContactDialogFragment.newInstance(contact)
                    .show(childFragmentManager, DeleteContactDialogFragment.DELETE_DIALOG_TAG)
            }
        })
        viewModel.contacts.observe(viewLifecycleOwner, {
            contactsAdapter.contacts = it
        })
        binding.apply {
            recyclerView.adapter = contactsAdapter
            recyclerView.addItemDecoration(ContactDecoration())
        }
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_main, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.apply {
            imeOptions = EditorInfo.IME_ACTION_DONE
            findViewById<ImageView>(R.id.search_close_btn).setOnClickListener {
                findViewById<EditText>(R.id.search_src_text).setText("")
                setQuery("", false)
                onActionViewCollapsed()
                searchItem.collapseActionView()
                binding.recyclerView.layoutManager?.scrollToPosition(0)
            }

            searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener{
                override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                    return true
                }

                override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                    binding.recyclerView.layoutManager?.scrollToPosition(0)
                    return true
                }
            })

            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    hideKeyboard()
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    val filteredContacts = viewModel.filterContacts(newText)
                    contactsAdapter.contacts = filteredContacts
                    return false
                }
            })
        }
    }

    fun Fragment.hideKeyboard() = ViewCompat.getWindowInsetsController(requireView())
        ?.hide(WindowInsetsCompat.Type.ime())

    override fun onPositiveButtonClicked(contact: Contact) {
        val position = viewModel.getContactIndex(contact)
        viewModel.deleteContact(contact)
        contactClickListener?.onContactLongClicked()
        binding.recyclerView.layoutManager?.scrollToPosition(position)
    }

    interface ContactClickListener {
        fun onContactClicked(contact: Contact)
        fun onContactLongClicked()
    }

    companion object {
        const val CONTACTS_TAG = "CONTACTS_TAG"
        fun newInstance() = ContactsFragment()
    }
}
