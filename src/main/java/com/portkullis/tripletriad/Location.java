package com.portkullis.tripletriad;

import java.util.Optional;

/**
 * Created by darius on 1/20/16.
 */
public enum Location {

    TOPLEFT(0), TOP(1), TOPRIGHT(2),
    LEFT(3), CENTER(4), RIGHT(5),
    BOTTOMLEFT(6), BOTTOM(7), BOTTOMRIGHT(8);

    private final int boardIndex;

    private Location(int boardIndex) {
        this.boardIndex = boardIndex;
    }

    public int getBoardIndex() {
        return boardIndex;
    }

    public static Location fromBoardIndex(int index) {
        for (Location l : values()) {
            if (index == l.getBoardIndex()) {
                return l;
            }
        }
        throw new RuntimeException("Illegal board index value: " + index);
    }

    public Optional<Location> getUp() {
        return boardIndex >= 3 ? Optional.of(fromBoardIndex(boardIndex - 3)) : Optional.empty();
    }

    public Optional<Location> getDown() {
        return boardIndex < 6 ? Optional.of(fromBoardIndex(boardIndex + 3)) : Optional.empty();
    }

    public Optional<Location> getLeft() {
        return boardIndex % 3 != 0 ? Optional.of(fromBoardIndex(boardIndex - 1)) : Optional.empty();
    }

    public Optional<Location> getRight() {
        return boardIndex % 3 != 2 ? Optional.of(fromBoardIndex(boardIndex + 1)) : Optional.empty();
    }

}
