package xyz.mmixel.hw6.ui.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import xyz.mmixel.hw6.R
import xyz.mmixel.hw6.data.Contact
import xyz.mmixel.hw6.databinding.ListItemBinding

class ContactsDiffCallback(
    private val oldList: List<Contact>,
    private val newList: List<Contact>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldContact = oldList[oldItemPosition]
        val newContact = newList[newItemPosition]
        return oldContact.id == newContact.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldContact = oldList[oldItemPosition]
        val newContact = newList[newItemPosition]
        return oldContact == newContact
    }
}

interface ContactActionListener {
    fun onContactClick(contact: Contact)
    fun onContactLongClick(contact: Contact)
}

class ContactsAdapter(
    private val contactListener: ContactActionListener
) :
    RecyclerView.Adapter<ContactsAdapter.ContactViewHolder>() {
    var contacts = emptyList<Contact>()
        set(newList) {
            val diffCallback = ContactsDiffCallback(field, newList)
            val diffResults = DiffUtil.calculateDiff(diffCallback)
            field = newList
            diffResults.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        return ContactViewHolder(
            ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contacts[position]

        holder.apply {
            bind(contact)
            itemView.setOnClickListener {
                contactListener.onContactClick(contact)
            }
            itemView.setOnLongClickListener {
                contactListener.onContactLongClick(contact)
                true
            }
        }
    }

    override fun getItemCount() = contacts.size

    class ContactViewHolder(
        private val binding: ListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(contact: Contact) {
            with(binding) {
                nameLabel.text = contact.name
                surnameLabel.text = contact.surname
                phoneLabel.text = contact.phone

                if (contact.avatar.isNotBlank()) {
                    Picasso.get()
                        .load(Uri.parse(contact.avatar))
                        .placeholder(R.drawable.photo)
                        .error(R.drawable.photo)
                        .into(contactImage)
                } else {
                    contactImage.setImageResource(R.drawable.photo)
                }
            }
        }
    }
}
