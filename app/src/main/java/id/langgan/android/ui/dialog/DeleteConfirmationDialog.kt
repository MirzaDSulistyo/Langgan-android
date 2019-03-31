package id.langgan.android.ui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import id.langgan.android.R

class DeleteConfirmationDialog : DialogFragment() {

    private lateinit var mListener: NoticeDialogListener

    interface NoticeDialogListener {
        fun onDeleteItem()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(context!!)

        val item = if (arguments != null) arguments!!.getString("item") else ""
        val message = getString(R.string.delete_messages, item)

        builder.setMessage(message)
            .setPositiveButton(R.string.yes) { _, _ -> onDeleteConfirmed() }
            .setNegativeButton(R.string.cancel) { dialog, _ -> dialog.dismiss() }
        return builder.create()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = context as DeleteConfirmationDialog.NoticeDialogListener
        } catch (e: ClassCastException) {
            // The activity doesn't implement the interface, throw exception
            throw ClassCastException((context.toString() +
                    " must implement SettingDialogListener"))
        }
    }

    companion object {

        fun newInstance(msg: String): DeleteConfirmationDialog {
            val fragment = DeleteConfirmationDialog()

            val bundle = Bundle()
            bundle.putString("item", msg)
            fragment.arguments = bundle

            return fragment
        }
    }

    private fun onDeleteConfirmed() {
        mListener.onDeleteItem()
    }
}