package com.example.deerdiary.ui.global

import android.content.Context
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class PasswordTextField
    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
    ) : AppCompatEditText(context, attrs) {
        init {
            addTextChangedListener(
                object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int,
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int,
                    ) {
                        if (s.toString().length < 8) {
                            setError("Password must be at least 8 characters", null)
                        } else {
                            setError(null, null)
                        }
                    }

                    override fun afterTextChanged(s: android.text.Editable?) {
                    }
                },
            )
        }
    }
