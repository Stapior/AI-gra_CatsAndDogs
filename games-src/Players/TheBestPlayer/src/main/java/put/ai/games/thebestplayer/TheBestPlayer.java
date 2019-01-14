/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package put.ai.games.thebestplayer;

import put.ai.games.game.Board;
import put.ai.games.game.Move;
import put.ai.games.game.Player;
import put.ai.games.game.moves.PlaceMove;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TheBestPlayer extends Player {

    private Move best;
    private Object object;
    private Thread thread;
    private Board board;
    private int lastDeepth=2;

    private class MoveValue {

        public double returnValue;
        public Move returnMove;

        public MoveValue() {
            returnValue = 0;
        }

        public MoveValue(double returnValue) {
            this.returnValue = returnValue;
        }

        public MoveValue(double returnValue, Move returnMove) {
            this.returnValue = returnValue;
            this.returnMove = returnMove;
        }

    }


        public MoveValue minMax(double alpha, double beta, int maxDepth, Color player, Move move) {

        ArrayList<Move> moves = (ArrayList<Move>) board.getMovesFor(player);

        Iterator<Move> movesIterator = moves.iterator();
        boolean isMaximizer = (player.equals(this.getColor()));
        double value;

        if (maxDepth == 0 ) {
            Color opponent = Player.getOpponent(this.getColor());
            value =1000 - board.getMovesFor(opponent).size();


            return new MoveValue(value);
        }
        MoveValue returnMove;
        MoveValue bestMove = null;
        if ( isMaximizer) {
            if(moves.isEmpty()){
                return new MoveValue(0);
            }
            while (movesIterator.hasNext()) {
                Move currentMove = movesIterator.next();
                board.doMove(currentMove);
                returnMove = minMax(alpha, beta, maxDepth - 1, Player.getOpponent(player), currentMove);
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
            if(moves.isEmpty()){
                return  new MoveValue(1000);

            }
            while (movesIterator.hasNext()) {
                Move currentMove = movesIterator.next();
                board.doMove(currentMove);
                returnMove = minMax(alpha, beta, maxDepth - 1, Player.getOpponent(player), currentMove);
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
        return "Piotr Stachowiak 132319, Pawel Bubak 123197";
    }

    class BestMove implements Runnable {
        private Board board1;

        public BestMove(Board board1) {
            this.board1 = board1;
        }

        @Override
        public void run() {
            int deepth = lastDeepth;
            System.out.println(deepth);
            while(true) {

                MoveValue moveValue = minMax(0, 100000000, deepth, getColor(), null);
                //System.out.println("zwrocono");
                best = moveValue.returnMove;
                lastDeepth=deepth;
                if (deepth >100){
                    break;
                }
                deepth+=1;
            }
            thread.interrupt();

        }
    }


    @Override
    public Move nextMove(Board b) {

        long start = System.currentTimeMillis();
        board=b.clone();
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
