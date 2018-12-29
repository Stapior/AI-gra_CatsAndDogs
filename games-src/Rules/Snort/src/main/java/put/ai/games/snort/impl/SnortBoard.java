/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package put.ai.games.snort.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import put.ai.games.game.Move;
import put.ai.games.game.Player;
import put.ai.games.game.Player.Color;
import put.ai.games.game.TypicalBoard;

public class SnortBoard extends TypicalBoard {

    private int nStones;

    public SnortBoard(int size) {
        super(size);
        if (size % 2 != 1)
            throw new IllegalArgumentException("Board size must be odd");
        nStones = 0;
    }


    private SnortBoard(SnortBoard base) {
        super(base);
        nStones = base.nStones;
    }

    private boolean isEmpty() {
        return nStones == 0;
    }

    private int getCenter() {
        assert getSize() % 2 == 1;
        return (getSize() - 1) / 2;
    }

    private void doSwap() {
        for (int x = 0; x < getSize(); ++x)
            for (int y = 0; y < getSize(); ++y)
                if (state[x][y] != Color.EMPTY)
                    state[x][y] = Player.getOpponent(state[x][y]);
    }

    @Override
    public void doMove(Move _m) {
        if (_m instanceof SnortDropMove) {
            SnortDropMove m = (SnortDropMove) _m;
            if (state[m.getX()][m.getY()] != Color.EMPTY) {
                throw new IllegalArgumentException("Move on non-empty cell");
            }
            state[m.getX()][m.getY()] = m.getColor();
            nStones += 1;
        } else if (_m instanceof SnortSwapMove) {
            doSwap();
        } else
            throw new IllegalArgumentException();
    }


    @Override
    public void undoMove(Move _m) {
        if (_m instanceof SnortDropMove) {
            SnortDropMove m = (SnortDropMove) _m;
            if (state[m.getX()][m.getY()] != m.getColor()) {
                throw new IllegalArgumentException("Undo on invalid cell");
            }
            state[m.getX()][m.getY()] = Color.EMPTY;
            nStones -= 1;
        } else if (_m instanceof SnortSwapMove) {
            doSwap();
        } else
            throw new IllegalArgumentException();
    }


    @Override
    public SnortBoard clone() {
        return new SnortBoard(this);
    }


    @Override
    public List<Move> getMovesFor(Color c) {
        if (c == Color.EMPTY) {
            throw new IllegalArgumentException("Color for getMovesFor must be well defined");
        }
        Color opp = Player.getOpponent(c);
        ArrayList<Move> result = new ArrayList<>();
        for (int i = 0; i < getSize(); ++i) {
            for (int j = 0; j < getSize(); ++j) {
                if (canMove(opp, i, j)) {
                    result.add(new SnortDropMove(i, j, c));
                }
            }
        }
        if (c == Color.PLAYER2 && nStones == 1)
            result.add(new SnortSwapMove(c));
        return result;
    }


    private boolean canMove(Color opp, int i, int j) {
        if (isEmpty() && i == getCenter() && j == getCenter())
            return false;
        return getState(i, j) == Color.EMPTY && getState(i - 1, j) != opp && getState(i + 1, j) != opp
                && getState(i, j - 1) != opp && getState(i, j + 1) != opp;
    }


    @Override
    public boolean canMove(Color c) {
        c = Player.getOpponent(c);
        for (int i = 0; i < getSize(); ++i) {
            for (int j = 0; j < getSize(); ++j) {
                if (canMove(c, i, j)) {
                    return true;
                }
            }
        }
        return false;
    }
}
