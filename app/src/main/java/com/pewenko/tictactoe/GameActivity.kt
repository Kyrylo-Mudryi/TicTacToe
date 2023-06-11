package com.pewenko.tictactoe

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat


class GameActivity : AppCompatActivity() {

    private lateinit var gameManager: GameManager
    private lateinit var one: TextView
    private lateinit var two: TextView
    private lateinit var three: TextView
    private lateinit var four: TextView
    private lateinit var five: TextView
    private lateinit var six: TextView
    private lateinit var seven: TextView
    private lateinit var eight: TextView
    private lateinit var nine: TextView
    private lateinit var returnButton: Button
    private lateinit var startNewGameButton: Button
    private lateinit var player1Points: TextView
    private lateinit var player2Points: TextView
    private val handler = Handler()
    private var isFriendPlaying = false
    private var isAIPlaying = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        gameManager = GameManager()

        one = findViewById(R.id.one)
        two = findViewById(R.id.two)
        three = findViewById(R.id.three)
        four = findViewById(R.id.four)
        five = findViewById(R.id.five)
        six = findViewById(R.id.six)
        seven = findViewById(R.id.seven)
        eight = findViewById(R.id.eight)
        nine = findViewById(R.id.nine)
        startNewGameButton = findViewById(R.id.start_new_game_button)
        returnButton = findViewById(R.id.return_button)
        player1Points = findViewById(R.id.player_one_score)
        player2Points = findViewById(R.id.player_two_score)
        isFriendPlaying = intent.getBooleanExtra("IsFriendPlaying", false)

        val boxClickListeners = mapOf(
            one to Position(0, 0),
            two to Position(0, 1),
            three to Position(0, 2),
            four to Position(1, 0),
            five to Position(1, 1),
            six to Position(1, 2),
            seven to Position(2, 0),
            eight to Position(2, 1),
            nine to Position(2, 2)
        )

        for ((box, position) in boxClickListeners) {
            box.setOnClickListener { onBoxClicked(box, position) }
        }

        startNewGameButton.setOnClickListener {
            startNewGameButton.visibility = View.GONE
            gameManager.reset()
            resetBoxes()
        }

        returnButton.setOnClickListener {
            finish()
        }

        updatePoints()
    }

    private fun updatePoints() {
        player1Points.text = "Player X Points: ${gameManager.player1Points}"
        player2Points.text = "Player O Points: ${gameManager.player2Points}"
    }

    private fun resetBoxes() {
        val boxes = listOf(one, two, three, four, five, six, seven, eight, nine)
        for (box in boxes) {
            box.text = ""
            box.background = null
            box.isEnabled = true
        }

        startNewGameButton.visibility = View.GONE
    }

    private fun onBoxClicked(box: TextView, position: Position) {
        if (isFriendPlaying) {
            if (box.text.isEmpty()) {
                box.text = if (gameManager.currentPlayer == 1) "X" else "O"
                val winningLine = gameManager.makeMove(position)
                if (winningLine != null) {
                    updatePoints()
                    disableBoxes()
                    startNewGameButton.visibility = View.VISIBLE
                    showWinner(winningLine)
                } else if (gameManager.isBoardFull()) {
                    startNewGameButton.visibility = View.VISIBLE
                }
            }
        }
        else {
            if (!isAIPlaying && box.text.isEmpty()) {
                box.text = "X"
                box.isEnabled = false

                val winningLine = gameManager.makeMove(position)

                if (winningLine != null) {
                    updatePoints()
                    disableBoxes()
                    showWinner(winningLine)
                    startNewGameButton.visibility = View.VISIBLE
                } else if (gameManager.isBoardFull()) {
                    startNewGameButton.visibility = View.VISIBLE
                } else {
                    isAIPlaying = true

                    handler.postDelayed({
                        gameManager.makeAIMove()
                        isAIPlaying = false

                        val aiPosition = gameManager.getLastMovePosition()
                        val aiBox = getBoxFromPosition(aiPosition)
                        aiBox?.text = "O"
                        aiBox?.isEnabled = false

                        val aiWinningLine = gameManager.hasGameEnded()
                        if (aiWinningLine != null) {
                            updatePoints()
                            disableBoxes()
                            showWinner(aiWinningLine)
                            startNewGameButton.visibility = View.VISIBLE
                        } else if (gameManager.isBoardFull()) {
                            startNewGameButton.visibility = View.VISIBLE
                        }
                    }, 500)
                }
            }
        }
    }


    private fun getBoxFromPosition(position: Position?): TextView? {
        return when (position) {
            Position(0, 0) -> one
            Position(0, 1) -> two
            Position(0, 2) -> three
            Position(1, 0) -> four
            Position(1, 1) -> five
            Position(1, 2) -> six
            Position(2, 0) -> seven
            Position(2, 1) -> eight
            Position(2, 2) -> nine
            else -> null
        }
    }

    private fun disableBoxes() {
        val boxes = listOf(one, two, three, four, five, six, seven, eight, nine)
        for (box in boxes) {
            box.isEnabled = false
        }
    }

    private fun showWinner(winningLine: WinningLine) {
        val (winningBoxes, background) = when (winningLine) {
            WinningLine.ROW_0 -> Pair(listOf(one, two, three), R.drawable.horizontal_line)
            WinningLine.ROW_1 -> Pair(listOf(four, five, six), R.drawable.horizontal_line)
            WinningLine.ROW_2 -> Pair(listOf(seven, eight, nine), R.drawable.horizontal_line)
            WinningLine.COLUMN_0 -> Pair(listOf(one, four, seven), R.drawable.vertical_line)
            WinningLine.COLUMN_1 -> Pair(listOf(two, five, eight), R.drawable.vertical_line)
            WinningLine.COLUMN_2 -> Pair(listOf(three, six, nine), R.drawable.vertical_line)
            WinningLine.DIAGONAL_LEFT -> Pair(listOf(one, five, nine), R.drawable.left_diagonal_line)
            WinningLine.DIAGONAL_RIGHT -> Pair(listOf(three, five, seven), R.drawable.right_diagonal_line)
        }

        for (box in winningBoxes) {
            box.background = ContextCompat.getDrawable(this, background)
        }
    }

}