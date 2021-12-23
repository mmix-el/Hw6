package xyz.mmixel.hw6.ui.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import xyz.mmixel.hw6.data.Contact

class DeleteContactDialogFragment : DialogFragment() {
    private lateinit var positiveButtonClickListener: PositiveButtonClickListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        positiveButtonClickListener = parentFragment as PositiveButtonClickListener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val contact: Contact = requireArguments().getSerializable(DELETE_CONTACT_EXTRA) as Contact

        return AlertDialog.Builder(requireContext())
            .setTitle("Delete a contact")
            .setMessage(
                "Are you sure you want to delete\n" +
                        "${contact.name} ${contact.surname} from contacts?"
            )
            .setNegativeButton(android.R.string.cancel) { dialog, _ -> dialog.dismiss() }
            .setPositiveButton(android.R.string.ok) { dialog, _ ->
                positiveButtonClickListener.onPositiveButtonClicked(contact)
                dialog.dismiss()
            }
            .create()
    }

    interface PositiveButtonClickListener {
        fun onPositiveButtonClicked(contact: Contact)
    }

    companion object {
        const val DELETE_DIALOG_TAG = "DELETE_DIALOG_TAG"
        const val DELETE_CONTACT_EXTRA = "DELETE_CONTACT_EXTRA"

        fun newInstance(contact: Contact) = DeleteContactDialogFragment().also {
            it.arguments = Bundle().apply {
                putSerializable(DELETE_CONTACT_EXTRA, contact)
            }
        }
    }
}