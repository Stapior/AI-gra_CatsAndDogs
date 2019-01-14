/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package put.ai.games.thebestplayer;

import put.ai.games.game.Board;
import put.ai.games.game.Move;
import put.ai.games.game.Player;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class TheBestPlayer extends Player {

    private Move best;
    private Object object;
    private Thread thread;
    private Board board;
    private int lastDeepth = 1;

    private float blocked(Board boardNow, int x, int y, Color player) {
        if (boardNow.getState(x, y) == player) {
            float value = 0;
            if (boardNow.getState(x + 1, y + 1) == Color.EMPTY)
                if (boardNow.getState(x + 2, y + 1) == player) {
                    value += 0.5;

                }
            if (boardNow.getState(x - 1, y + 1) == Color.EMPTY)
                if (boardNow.getState(x - 2, y + 1) == player) {
                    value += 0.5;
                }
            if (boardNow.getState(x + 1, y - 1) == Color.EMPTY)
                if (boardNow.getState(x + 2, y - 1) == player) {
                    value += 0.5;
                }
            if (boardNow.getState(x - 1, y - 1) == Color.EMPTY)
                if (boardNow.getState(x - 2, y - 1) == player) {
                    value += 0.5;
                }
            if (boardNow.getState(x + 1, y + 1) == Color.EMPTY)
                if (boardNow.getState(x + 1, y + 2) == player) {
                    value += 0.5;
                }
            if (boardNow.getState(x + 1, y - 1) == Color.EMPTY)
                if (boardNow.getState(x + 1, y - 2) == player) {
                    value += 0.5;
                }
            if (boardNow.getState(x - 1, y + 1) == Color.EMPTY)
                if (boardNow.getState(x - 1, y + 2) == player) {
                    value += 0.5;
                }
            if (boardNow.getState(x - 1, y - 1) == Color.EMPTY)
                if (boardNow.getState(x - 1, y - 2) == player) {
                    value += 0.5;
                }
            return value;
        } else {
            return 0;
        }
    }

    private class alfaBetaMove {
        double returnValue;
        Move returnMove;

        alfaBetaMove(double returnValue) {
            this.returnValue = returnValue;
        }
    }


    public alfaBetaMove alfaBeta(double alpha, double beta, int maxDepth, Color player, Move move) {

        ArrayList<Move> moves = (ArrayList<Move>) board.getMovesFor(player);
        Iterator<Move> movesIterator = moves.iterator();

        boolean isMax = (player.equals(this.getColor()));
        double value;

        if (maxDepth == 0) {

            Color opponent = Player.getOpponent(this.getColor());
            value =  1000 - board.getMovesFor(opponent).size();

            for (int i = 0; i < board.getSize(); i++) {
                for (int j = 0; j < board.getSize(); j++) {
                     value += blocked(board, i, j, this.getColor())*0.1;
                }
            }
            return new alfaBetaMove(value);
        }
        alfaBetaMove returnMove;
        alfaBetaMove bestMove = null;
        if (isMax) {
            if (moves.isEmpty()) {
                return new alfaBetaMove(0);
            }
            while (movesIterator.hasNext()) {
                Move currentMove = movesIterator.next();
                board.doMove(currentMove);
                returnMove = alfaBeta(alpha, beta, maxDepth - 1, Player.getOpponent(player), currentMove);
                board.undoMove(currentMove);

                if ((bestMove == null) || (bestMove.returnValue < returnMove.returnValue)) {
                    bestMove = returnMove;
                    bestMove.returnMove = currentMove;
                }
                if (returnMove.returnValue > alpha) {
                    alpha = returnMove.returnValue;
                    bestMove = returnMove;
                }
                if (beta <= alpha) {
                    bestMove.returnValue = beta;
                    bestMove.returnMove = null;
                    return bestMove;
                }
            }
            return bestMove;

        } else {
            if (moves.isEmpty()) {
                return new alfaBetaMove(1000);
            }
            while (movesIterator.hasNext()) {
                Move currentMove = movesIterator.next();
                board.doMove(currentMove);
                returnMove = alfaBeta(alpha, beta, maxDepth - 1, Player.getOpponent(player), currentMove);
                board.undoMove(currentMove);
                if ((bestMove == null) || (bestMove.returnValue > returnMove.returnValue)) {
                    bestMove = returnMove;
                    bestMove.returnMove = currentMove;
                }
                if (returnMove.returnValue < beta) {
                    beta = returnMove.returnValue;
                    bestMove = returnMove;
                }
                if (beta <= alpha) {
                    bestMove.returnValue = alpha;
                    bestMove.returnMove = null;
                    return bestMove;
                }
            }
            return bestMove;
        }
    }


    @Override
    public String getName() {
        return "Piotr Stachowiak 132319, Pawel Bubak 132197";
    }

    class BestMove implements Runnable {
        private Board board1;

        public BestMove(Board board1) {
            this.board1 = board1;
        }

        @Override
        public void run() {
            int deepth = lastDeepth-1;
            System.out.println(deepth);
            while (true) {

                alfaBetaMove moveValue = alfaBeta(0, 100000000, deepth, getColor(), null);
                //System.out.println("zwrocono");
                best = moveValue.returnMove;
                lastDeepth = deepth;
                if (deepth > 100) {
                    break;
                }
                deepth += 1;
            }
            thread.interrupt();

        }
    }


    @Override
    public Move nextMove(Board b) {

        long start = System.currentTimeMillis();
        board = b.clone();
        thread = Thread.currentThread();
        List<Move> moves = b.getMovesFor(getColor());
        best = moves.get(0);

        Thread newThread = new Thread(new BestMove(b.clone()));
        newThread.start();
        System.out.println("nastepny");

        long stop = System.currentTimeMillis();
        long time = this.getTime() - (stop - start);

        if (time < 30) {
            return best;
        } else {
            try {
                Thread.sleep(time - 20);
            } catch (InterruptedException e) {
                return best;
            }
            newThread.stop();
            return best;
        }
    }
}
