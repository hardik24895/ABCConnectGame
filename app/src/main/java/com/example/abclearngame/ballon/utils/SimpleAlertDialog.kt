package com.example.abclearngame.ballon.utils

import android.R
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

/**
 * <h1>SimpleAlertDialog</h1>
 *
 *
 * **SimpleAlertDialog** class is responsible to show user dialog when he reach new high score.
 */
class SimpleAlertDialog : DialogFragment() {
    /**
     * This method create, define, build and return new instance of AlertDialog class.
     *
     * @param savedInstanceState Define potentially saved parameters due to configurations changes.
     * @return Dialog New instance of AlertDialog.Builder class
     * @see Dialog
     *
     * @see Bundle
     *
     * @see AlertDialog.Builder
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val args = arguments ?: throw AssertionError()
        val builder = AlertDialog.Builder(activity)
            .setTitle(args.getString(TITLE_KEY))
            .setMessage(args.getString(MESSAGE_KEY))
            .setCancelable(false)
        builder.setPositiveButton(R.string.ok, null)
        return builder.create()
    }

    companion object {
        private const val TITLE_KEY = "title_key"
        private const val MESSAGE_KEY = "message_key"

        /**
         * This method create, define and return new instance of SimpleAlertDialog class.
         *
         * @param title   Title of the dialog.
         * @param message Message of the dialog contain new high score.
         * @return SimpleAlertDialog New instance of class.
         * @see Bundle
         */
        fun newInstance(title: String?, message: String?): SimpleAlertDialog {
            val args = Bundle()
            args.putString(TITLE_KEY, title)
            args.putString(MESSAGE_KEY, message)
            val fragment = SimpleAlertDialog()
            fragment.arguments = args
            return fragment
        }
    }
}