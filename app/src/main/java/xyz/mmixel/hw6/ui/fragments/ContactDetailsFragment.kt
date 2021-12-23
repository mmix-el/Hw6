package xyz.mmixel.hw6.ui.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.squareup.picasso.Picasso
import xyz.mmixel.hw6.R
import xyz.mmixel.hw6.data.Contact
import xyz.mmixel.hw6.databinding.FragmentContactDetailsBinding
import xyz.mmixel.hw6.ui.viewModels.ContactDetailsViewModel
import xyz.mmixel.hw6.ui.viewModels.factory

class ContactDetailsFragment : Fragment(R.layout.fragment_contact_details) {
    private lateinit var binding: FragmentContactDetailsBinding
    private val viewModel: ContactDetailsViewModel by viewModels { factory() }
    private var editButtonClickListener: EditButtonClickListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        editButtonClickListener = context as EditButtonClickListener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_contact_details,
                container,
                false
            )

        viewModel.contact.observe(viewLifecycleOwner, {
            binding.apply {
                nameEditText.setText(it.name)
                surnameEditText.setText(it.surname)
                phoneEditText.setText(it.phone)
                emailEditText.setText(it.email)
                if (it.avatar.isNotBlank()) {
                    Picasso.get()
                        .load(Uri.parse(viewModel.newAvatar.value ?: it.avatar))
                        .into(profileImage)
                } else {
                    Picasso.get()
                        .load(R.drawable.photo)
                        .into(profileImage)
                }
            }
        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val contactId: Contact = requireArguments().getSerializable(CONTACT_EXTRA) as Contact
        viewModel.loadContact(contactId.id)

        binding.apply {
            editButton.setOnClickListener {
                editContact()
                editButtonClickListener?.onEditButtonClicked()
            }
            profileImage.setOnClickListener {
                selectImageFromGallery()
            }
        }
    }

    private fun editContact() {
        viewModel.contact.value?.let {
            binding.apply {
                viewModel.editContact(
                    Contact(
                        it.id,
                        nameEditText.text.toString(),
                        surnameEditText.text.toString(),
                        phoneEditText.text.toString(),
                        emailEditText.text.toString(),
                        viewModel.newAvatar.value ?: it.avatar
                    )
                )
            }
        }
    }

    private val selectImageFromGalleryResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                binding.profileImage.setImageURI(uri)
                viewModel.setNewAvatar(uri.toString())
            }
        }

    private fun selectImageFromGallery() = selectImageFromGalleryResult.launch("image/*")

    interface EditButtonClickListener {
        fun onEditButtonClicked()
    }

    companion object {
        const val CONTACT_DETAILS_TAG = "CONTACT_DETAILS_FRAGMENT_TAG"
        const val CONTACT_EXTRA = "CONTACT_EXTRA"

        fun newInstance(contact: Contact) = ContactDetailsFragment().also {
            it.arguments = Bundle().apply {
                putSerializable(CONTACT_EXTRA, contact)
            }
        }
    }
}