/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package put.ai.games.thebestplayer;

        import java.util.List;
        import java.util.Random;
        import put.ai.games.game.Board;
        import put.ai.games.game.Move;
        import put.ai.games.game.Player;

public class TheBestPlayer extends Player {

    private Random random = new Random();


    @Override
    public String getName() {
        return "Piotr Stachowiak 132319";
    }


    @Override
    public Move nextMove(Board b) {

        List<Move> moves = b.getMovesFor(getColor());
        Board board= b.clone();
        int minSize=99999999;
        Move best=moves.get(0);
        for (Move move: moves) {
            board.doMove(move);
            int size = board.getMovesFor(Player.getOpponent(getColor())).size();
            if(size < minSize){
                System.out.println(size);
                minSize=size;
                best=move;
            }
            board.undoMove(move);
        }
        return best;
    }
}
