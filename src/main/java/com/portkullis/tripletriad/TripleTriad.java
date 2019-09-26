package com.portkullis.tripletriad;

import com.portkullis.tripletriad.engine.MinMaxEngine;
import com.portkullis.tripletriad.manager.TripleTriadManager;
import com.portkullis.tripletriad.manager.model.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by darius on 1/18/16.
 */
public class TripleTriad {

    private final List<Card> blueCards = new ArrayList<>();
    private final List<Card> redCards = new ArrayList<>();
    private final MinMaxEngine.GameState.Player firstTurn;

    private final TripleTriadManager manager = TripleTriadManager.getInstance();

    public TripleTriad() {
        blueCards.add(Card.C6762_5_WEDGE_BIGGS);
        blueCards.add(Card.C9862_8_IFRIT);
        blueCards.add(Card.C8296_8_SIREN);
        blueCards.add(Card.C53A8_9_DIABLOS);
        blueCards.add(Card.C649A_10_SEIFER);

        redCards.add(Card.C9239_8_MINIMOG);
        redCards.add(Card.C2736_4_TURTAPOD);
        redCards.add(Card.C4762_4_T_REXAUR);
        redCards.add(Card.C5513_2_ANACONDAUR);
        redCards.add(Card.C7354_4_HEXADRAGON);

        firstTurn = MinMaxEngine.GameState.Player.MINIMIZING;
    }

    public static void main(String[] args) {
        TripleTriad game = new TripleTriad();
        game.run();
    }

    private void run() {
        TripleTriadState state = manager.startNewGame(blueCards, redCards, firstTurn);

        while (!state.isGameOver()) {
            state = doPlayerMove(state);
        }

        printBoard(state);
        double score = state.getScore();
        if (score < 0) {
            System.out.println("Winner: Red");
        } else if (score == 0) {
            System.out.println("Draw");
        } else {
            System.out.println("Winner: Blue");
        }
    }

    private TripleTriadState doPlayerMove(TripleTriadState state) {
        printBoard(state);

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        TripleTriadMove move = null;
        while (move == null) {
            Integer card = null;
            while (card == null) {
                try {
                    System.out.println();
                    System.out.print("Which card: ");
                    System.out.flush();
                    card = Integer.parseInt(in.readLine()) - 1;
                } catch (Exception e) {
                    System.out.println("Invalid input!");
                }
            }

            Integer cell = null;
            while (cell == null) {
                try {
                    System.out.println();
                    System.out.print("Which cell: ");
                    cell = Integer.parseInt(in.readLine()) - 1;
                } catch (Exception e) {
                    System.out.println("Invalid input!");
                }
            }

            move = new TripleTriadMove(state.getTurn(), card, Location.fromBoardIndex(cell));
            if (!state.getLegalMoves().contains(move)) {
                System.out.println("Illegal move!");
                move = null;
            }
        }

        return manager.applyMove(state, move);
    }

    private void printBoard(TripleTriadState state) {
        List<String> red = renderCards(MinMaxEngine.GameState.Player.MINIMIZING, state.getRedCards());
        List<String> blue = renderCards(MinMaxEngine.GameState.Player.MAXIMIZING, state.getBlueCards());
        List<String> board = renderBoard(state.getBoard());

        int maxRows = Math.max(Math.max(red.size(), blue.size()), board.size());

        int redTop = (maxRows - red.size()) / 2;
        int blueTop = (maxRows - blue.size()) / 2;
        int boardTop = (maxRows - board.size()) / 2;

        for (int i = 0; i < maxRows; i++) {
            printRow(i, redTop, red, "     ");
            System.out.print("  ");
            printRow(i, boardTop, board, "             ");
            System.out.print("  ");
            printRow(i, blueTop, blue, "     ");
            System.out.println();
        }

        System.out.println("Score: " + state.getScore());
    }

    private void printRow(int row, int top, List<String> data, String empty) {
        if (row >= top && row - top < data.size()) {
            System.out.print(data.get(row - top));
        } else {
            System.out.print(empty);
        }
    }

    private List<String> renderBoard(OwnedCard[] board) {
        List<String> rVal = new ArrayList<>();

        rVal.add("+---+---+---+");

        List<List<String>> renderedCards = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            if (board[i] == null) {
                renderedCards.add(renderCell(i + 1));
            } else {
                renderedCards.add(renderCard(board[i].getPlayer(), board[i].getCard()));
            }
            if (renderedCards.size() == 3) {
                for (int r = 0; r < 3; r++) {
                    StringBuilder builder = new StringBuilder();
                    builder.append("|");
                    for (int c = 0; c < 3; c++) {
                        builder.append(renderedCards.get(c).get(r));
                        builder.append("|");
                    }
                    rVal.add(builder.toString());
                }
                rVal.add("+---+---+---+");
                renderedCards.clear();
            }
        }

        return rVal;
    }

    private List<String> renderCards(MinMaxEngine.GameState.Player player, List<Card> redCards) {
        List<String> rVal = new ArrayList<>();
        rVal.add("+---+");
        for (Card c : redCards) {
            List<String> r = renderCard(player, c);
            for (String s : r) {
                rVal.add("|" + s + "|");
            }
            rVal.add("+---+");
        }
        return rVal;
    }

    private List<String> renderCard(MinMaxEngine.GameState.Player player, Card card) {
        List<String> rVal = new ArrayList<>();
        rVal.add(" " + toCardChar(card.getUp()) + " ");
        rVal.add(toCardChar(card.getLeft()) + (player == MinMaxEngine.GameState.Player.MINIMIZING ? "-" : "+") + toCardChar(card.getRight()));
        rVal.add(" " + toCardChar(card.getDown()) + " ");
        return rVal;
    }

    private List<String> renderCell(int cell) {
        List<String> rVal = new ArrayList<>();
        rVal.add("   ");
        rVal.add(" " + toCardChar(cell) + " ");
        rVal.add("   ");
        return rVal;
    }

    private String toCardChar(int value) {
        return value > 9 ? "A" : "" + value;
    }

}
