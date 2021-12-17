package com.eunwoo.contactlensmanagement

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.core.text.set
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener

object BindingAdapter {
    @BindingAdapter("text")
    @JvmStatic
    fun setText(editText: EditText, text: String) {
        if (editText.text.toString() != text) {
            editText.setText(text)
        }
    }

    @InverseBindingAdapter(attribute = "text")
    @JvmStatic
    fun getText(editText: EditText) : String {
        return editText.text.toString()
    }

//    @BindingAdapter("app:textAttrChanged")
    @BindingAdapter("textAttrChanged")
    @JvmStatic
    fun setTextListeners(editText: EditText,
                         attrChange: InverseBindingListener
    ) {
       // set a listener for click, focus, touch, etc.
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                attrChange.onChange()
            }

        })
    }
}