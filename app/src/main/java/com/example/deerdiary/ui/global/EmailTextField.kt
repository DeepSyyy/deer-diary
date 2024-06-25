package com.example.deerdiary.ui.global

import android.content.Context
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class EmailTextField
    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
    ) : AppCompatEditText(context, attrs) {
        private val regexEmail = "^[A-Za-z0-9+_.-]+@(.+)\$".toRegex()

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
                        error =
                            if (!regexEmail.matches(s.toString())) {
                                "Invalid email"
                            } else {
                                null
                            }
                    }

                    override fun afterTextChanged(s: android.text.Editable?) {
                    }
                },
            )
        }
    }
