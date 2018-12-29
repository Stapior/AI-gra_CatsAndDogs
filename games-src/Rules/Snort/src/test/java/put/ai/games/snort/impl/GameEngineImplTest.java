/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package put.ai.games.snort.impl;

import put.ai.games.snort.impl.SnortBoard;
import put.ai.games.engine.impl.GameEngineImpl;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import put.ai.games.engine.BoardFactory;
import put.ai.games.game.Board;
import put.ai.games.engine.GameEngine;
import put.ai.games.engine.impl.GameEngineImpl;
import put.ai.games.engine.parameters.Parameter;
import put.ai.games.game.Move;
import put.ai.games.game.Player;
import put.ai.games.game.exceptions.RuleViolationException;
import put.ai.games.naiveplayer.NaivePlayer;

class FilePlayer extends NaivePlayer {

    @Override
    public Move nextMove(Board b) {
        try {
            File.createTempFile("snort", ".txt");
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return super.nextMove(b);
    }
}

class ThreadPlayer extends NaivePlayer {

    @Override
    public Move nextMove(Board b) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                System.err.println("Run!");
            }
        }).start();
        return super.nextMove(b);
    }
}

public class GameEngineImplTest {

    private GameEngine game;


    //    @BeforeClass
    //    public static void beforeClass() {
    //        System.setSecurityManager(new SecurityManager());
    //    }
    @Before
    public void before() {
        game = new GameEngineImpl(new BoardFactory() {

            @Override
            public Board create() {
                return new SnortBoard(5);
            }


            @Override
            public List<? extends Parameter<?>> getConfigurationOptions() {
                return Collections.EMPTY_LIST;
            }


            @Override
            public void configure(Map<String, Object> configuration) {
            }


            @Override
            public String getName() {
                return "";
            }


            @Override
            public String getRules() {
                return "";
            }
        });
    }


    @Ignore
    @Test
    public void normalPlayer()
            throws RuleViolationException {
        Player p1 = new NaivePlayer();
        Player p2 = new NaivePlayer();
        game.addPlayer(p1);
        game.addPlayer(p2);
        game.play(null);
    }


    @Ignore
    @Test(expected = Exception.class)
    public void filePlayer()
            throws RuleViolationException {
        Player p1 = new NaivePlayer();
        Player p2 = new FilePlayer();
        game.addPlayer(p1);
        game.addPlayer(p2);
        game.play(null);
    }


    @Ignore
    @Test(expected = Exception.class)
    public void threadPlayer()
            throws RuleViolationException {
        Player p1 = new NaivePlayer();
        Player p2 = new ThreadPlayer();
        game.addPlayer(p1);
        game.addPlayer(p2);
        game.play(null);
    }
}
