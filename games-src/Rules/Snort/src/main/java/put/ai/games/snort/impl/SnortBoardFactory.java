/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package put.ai.games.snort.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import put.ai.games.engine.BoardFactory;
import put.ai.games.engine.parameters.IntegerParameter;
import put.ai.games.engine.parameters.Parameter;
import put.ai.games.game.Board;

public class SnortBoardFactory implements BoardFactory {

    private static final List<? extends Parameter<?>> PARAMS = Collections.unmodifiableList(Arrays
            .asList(new IntegerParameter(BOARD_SIZE, 10, 4, 50)));
    private int boardSize;


    @Override
    public Board create() {
        return new SnortBoard(boardSize);
    }


    @Override
    public List<? extends Parameter<?>> getConfigurationOptions() {
        return PARAMS;
    }


    @Override
    public void configure(Map<String, Object> configuration) {
        boardSize = Parameter.<Integer> get(BOARD_SIZE, PARAMS, configuration);
    }


    @Override
    public String getName() {
        return "Snort";
    }


    @Override
    public String getRules() {
        return "http://homepages.di.fc.ul.pt/~jpn/gv/catdogs.htm";
    }
}
