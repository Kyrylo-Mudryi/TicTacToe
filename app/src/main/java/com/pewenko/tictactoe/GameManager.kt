package com.pewenko.tictactoe

class GameManager {

    var currentPlayer = 1
    var player1Points = 0
    var player2Points = 0

    private var state = arrayOf(
        intArrayOf(0, 0, 0),
        intArrayOf(0, 0, 0),
        intArrayOf(0, 0, 0)
    )

    private var lastMovePosition: Position? = null

    fun makeMove(position: Position): WinningLine? {
        state[position.row][position.column] = currentPlayer
        val winningLine = hasGameEnded()

        if (winningLine == null) {
            currentPlayer = 3 - currentPlayer
        } else {
            updatePoints()
        }

        lastMovePosition = position

        return winningLine
    }

    fun getLastMovePosition(): Position? {
        return lastMovePosition
    }

    fun makeAIMove() {
        val winningMove = getMoveByPlayer(2)
        if (winningMove != null) {
            makeMove(winningMove)
            return
        }

        val blockingMove = getMoveByPlayer(1)
        if (blockingMove != null) {
            makeMove(blockingMove)
            return
        }

        makeRandomMove()
    }

    private fun getMoveByPlayer(player: Int): Position? {
        for (row in state.indices) {
            if (state[row].count { it == player } == 2 && state[row].count { it == 3 - player } == 0) {
                val col = state[row].indexOf(0)
                return Position(row, col)
            }
        }

        for (col in state[0].indices) {
            if ((0 until state.size).count { state[it][col] == player } == 2 &&
                (0 until state.size).count { state[it][col] == 3 - player } == 0) {
                val row = state.indices.firstOrNull { state[it][col] == 0 }
                if (row != null) {
                    return Position(row, col)
                }
            }
        }

        if (state[0][0] == player && state[1][1] == player && state[2][2] == 0) {
            return Position(2, 2)
        }
        if (state[0][0] == player && state[1][1] == 0 && state[2][2] == player) {
            return Position(1, 1)
        }
        if (state[0][0] == 0 && state[1][1] == player && state[2][2] == player) {
            return Position(0, 0)
        }

        if (state[0][2] == player && state[1][1] == player && state[2][0] == 0) {
            return Position(2, 0)
        }
        if (state[0][2] == player && state[1][1] == 0 && state[2][0] == player) {
            return Position(1, 1)
        }
        if (state[0][2] == 0 && state[1][1] == player && state[2][0] == player) {
            return Position(0, 2)
        }

        return null
    }

    private fun makeRandomMove() {
        val emptyPositions = mutableListOf<Position>()
        for (row in state.indices) {
            for (col in state[row].indices) {
                if (state[row][col] == 0) {
                    emptyPositions.add(Position(row, col))
                }
            }
        }

        if (emptyPositions.isNotEmpty()) {
            val randomPosition = emptyPositions.random()
            makeMove(randomPosition)
        }
    }

    fun reset() {
        state = arrayOf(
            intArrayOf(0, 0, 0),
            intArrayOf(0, 0, 0),
            intArrayOf(0, 0, 0)
        )
        currentPlayer = 1
    }

    private fun updatePoints() {
        when (currentPlayer) {
            1 -> player1Points++
            2 -> player2Points++
        }
    }

    fun hasGameEnded(): WinningLine? {
        if (state[0][0] == currentPlayer && state[0][1] == currentPlayer && state[0][2] == currentPlayer) {
            return WinningLine.ROW_0
        } else if (state[1][0] == currentPlayer && state[1][1] == currentPlayer && state[1][2] == currentPlayer) {
            return WinningLine.ROW_1
        } else if (state[2][0] == currentPlayer && state[2][1] == currentPlayer && state[2][2] == currentPlayer) {
            return WinningLine.ROW_2
        } else if (state[0][0] == currentPlayer && state[1][0] == currentPlayer && state[2][0] == currentPlayer) {
            return WinningLine.COLUMN_0
        } else if (state[0][1] == currentPlayer && state[1][1] == currentPlayer && state[2][1] == currentPlayer) {
            return WinningLine.COLUMN_1
        } else if (state[0][2] == currentPlayer && state[1][2] == currentPlayer && state[2][2] == currentPlayer) {
            return WinningLine.COLUMN_2
        } else if (state[0][0] == currentPlayer && state[1][1] == currentPlayer && state[2][2] == currentPlayer) {
            return WinningLine.DIAGONAL_LEFT
        } else if (state[0][2] == currentPlayer && state[1][1] == currentPlayer && state[2][0] == currentPlayer) {
            return WinningLine.DIAGONAL_RIGHT
        }
        return null
    }

    fun isBoardFull(): Boolean {
        for (row in state.indices) {
            for (col in state[row].indices) {
                if (state[row][col] == 0) {
                    return false
                }
            }
        }
        return true
    }
}