package com.cookandroid.dice

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.ImageView

/*
* This activity allows the user to roll a dice and view the result
* one the screen.
* */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rollButton: Button = findViewById(R.id.button)

        rollButton.setOnClickListener { rollDice() }

        // Do a dice roll whtn the app starts
        rollDice()
    }
/*
* Roll the dice and update the screen with the result.
* */
    private fun rollDice() {
        // Create new Dice object with 6 sides and roll it
        val dice = Dice(6)
        val diceRoll = dice.roll()

        // Update the screen with the dice roll
        val diceImage: ImageView = findViewById(R.id.imageView)

        val drawableResource = when(diceRoll) {
            1 -> R.drawable.dice_1
            2 -> R.drawable.dice_2
            3 -> R.drawable.dice_3
            4 -> R.drawable.dice_4
            5 -> R.drawable.dice_5
            else -> R.drawable.dice_6
        }

        // Update the ImageView with the correct drawable resource ID
        diceImage.setImageResource(drawableResource)

        // Update the content description
        diceImage.contentDescription = diceRoll.toString()
    }
}

class Dice(val numSide: Int) {
    fun roll(): Int {
        return (1..numSide).random()
    }
}