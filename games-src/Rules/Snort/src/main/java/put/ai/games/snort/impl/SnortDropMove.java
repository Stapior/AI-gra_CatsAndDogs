/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package put.ai.games.snort.impl;

import put.ai.games.game.moves.PlaceMove;
import put.ai.games.game.Move;
import put.ai.games.game.Player.Color;

import java.util.Objects;

public class SnortDropMove implements PlaceMove {

    private int x;
    private int y;
    private Color c;


    public SnortDropMove(int x, int y, Color c) {
        this.x = x;
        this.y = y;
        this.c = c;
    }


    @Override
    public int getX() {
        return x;
    }


    @Override
    public int getY() {
        return y;
    }


    @Override
    public Color getColor() {
        return c;
    }


    @Override
    public String toString() {
        return String.format("%s@(%d,%d)", c, x, y);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SnortDropMove that = (SnortDropMove) o;
        return x == that.x &&
                y == that.y &&
                c == that.c;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, c);
    }
}
