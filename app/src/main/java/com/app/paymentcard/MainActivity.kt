package com.app.paymentcard

import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.TextWatcher
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import java.util.regex.Pattern

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val info = """
            
        Visa : 13 or 16 digits, starting with 4. 
        
        VISA:
        4485100111868737
        4485559771471720

        MasterCard : 16 digits, starting with 51 through 55 or four digits number may range from 2221 to 2720.
        
        MasterCard:
        2221000426933684
        5460521639923587
        2221003867310222

        American Express : 15 digits, starting with 34 or 37.
        
        American Express (AMEX):
        374079872519741
        344063095658676
        376419934278688

        Discover : 16 digits, starting with 6011 or 65.
        
        Discover:
        6011638559899894
        6011130695498658
        6011010464735976448

        JCB : 15 digits, starting with 2131 or 1800, or 16 digits starting with 35.

        JCB:
        3538296346516205
        3544523186625628

        Diners Club : 14 digits, starting with 300 through 305, 36, or 38.

        Diners Club:
        5573112894654879
        5453456784708526
        5515499822249216 
        """.trimIndent()

        tvInfo.text = info

        //first do formatting
        etCardNumber.apply {

            val input = text.toString().trim()
            val regex = "(\\d{4})(?=\\d)".toRegex()


            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                    val formattedCardNumber = text.toString().replace(regex, "$1 ")

                    Log.d("TAG", "formattedCardNumber length = ${formattedCardNumber.length}")

                    removeTextChangedListener(this)

                    setText(formattedCardNumber)

                    setSelection(length())

                    addTextChangedListener(this)
                }

                override fun afterTextChanged(s: Editable?) {
                    //do card validation
                    val cardNumber = s.toString().replace(" ", "")

                    when (Cards.detect(cardNumber)) {
                        Cards.UNKNOWN -> {
                            setCompoundDrawablesRelativeWithIntrinsicBounds(
                                R.drawable.ic_dummy_card,
                                0,
                                0,
                                0
                            )
                            tilCardNumber.addError()
                        }
                        Cards.MASTERCARD -> {
                            setCompoundDrawablesRelativeWithIntrinsicBounds(
                                R.drawable.ic_mastercard,
                                0,
                                0,
                                0
                            )
                            tilCardNumber.removeError()
                        }
                        Cards.AMEX -> {
                            setCompoundDrawablesRelativeWithIntrinsicBounds(
                                R.drawable.ic_amex,
                                0,
                                0,
                                0
                            )
                            tilCardNumber.removeError()
                        }
                        Cards.DINNERCLUB -> {
                            setCompoundDrawablesRelativeWithIntrinsicBounds(
                                R.drawable.ic_dinners_club,
                                0,
                                0,
                                0
                            )
                            tilCardNumber.removeError()
                        }
                        Cards.DISCOVER -> {
                            setCompoundDrawablesRelativeWithIntrinsicBounds(
                                R.drawable.ic_discover,
                                0,
                                0,
                                0
                            )
                            tilCardNumber.removeError()
                        }
                        Cards.JCB -> {
                            setCompoundDrawablesRelativeWithIntrinsicBounds(
                                R.drawable.ic_jcb,
                                0,
                                0,
                                0
                            )
                            tilCardNumber.removeError()
                        }
                        Cards.VISA -> {
                            setCompoundDrawablesRelativeWithIntrinsicBounds(
                                R.drawable.ic_visa,
                                0,
                                0,
                                0
                            )
                            tilCardNumber.removeError()
                        }
                    }
                }

            })


        }

    }


    enum class Cards(val pattern: Pattern? = null) {
        /*2221 2720*/
        UNKNOWN,

        //        MASTERCARD(Pattern.compile("^(?:5[1-5][0-9]{2}|2[2-7][01])[0-9]{12}\$")),
        MASTERCARD(Pattern.compile("^5[1-5][0-9]{14}\$|^(?:222[1-9]|22[3-9][1-9]|2[3-6][0-9]{2}|27[0-1][0-9]|2720)[0-9]{12}")),
        VISA(Pattern.compile("^4[0-9]{12}(?:[0-9]{3})?\$")),
        AMEX(Pattern.compile("^3[47][0-9]{5,}\$")),
        DISCOVER(Pattern.compile("^6(?:011|5[0-9]{2})[0-9]{12}\$")),
        JCB(Pattern.compile("^(?:2131|1800|35[0-9]{3})[0-9]{11}\$")),
        DINNERCLUB(Pattern.compile("^3(?:0[0-5]|[68][0-9])[0-9]{11}\$"));

        companion object {
            fun detect(cardNumber: String): Cards {

                for (card in Cards.values()) {
                    if (card.pattern == null) continue
                    if (card.pattern.matcher(cardNumber).matches()) return card
                }

                return UNKNOWN
            }
        }
    }

    fun TextInputLayout.removeError() {
        error = null
        isErrorEnabled = false
    }

    fun TextInputLayout.addError() {
        isErrorEnabled = true
        error = "Invalid Card Number"
    }

}