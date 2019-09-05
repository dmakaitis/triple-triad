package com.portkullis.tripletriad;

/**
 * Created by darius on 1/18/16.
 */
public class Card {

    // Level 1
    public static final Card GEEZARD = new Card(1, 1, "Geezard", 1, 5, 4, 1);
    public static final Card FUNGUAR = new Card(1, 2, "Funguar", 5, 3, 1, 1);
    public static final Card BLOBRA = new Card(1, 5, "Blobra", 2, 5, 3, 1);
    public static final Card CATERCHIPILLAR = new Card(1, 10, "Caterchipillar", 4, 3, 2, 4);
    public static final Card COCKATRICE = new Card(1, 11, "Cockatrice", 2, 6, 1, 2);

    // Level 2

    // Level 3
    public static final Card TRI_FACE = new Card(3, 3, "Tri-Face", 3, 5, 5, 5);
    public static final Card FASTITOCALON = new Card(3, 4, "Fastitocalon", 7, 3, 5, 1);
    public static final Card SNOW_LION = new Card(3, 5, "Snow Lion", 7, 3, 1, 5);

    // Level 4

    // Lavel 5
    public static final Card MALBORO = new Card(5, 7, "Malboro", 7, 2, 7, 4);
    public static final Card ELNOYLE = new Card(5, 9, "Elnoyle", 5, 6, 3, 7);
    public static final Card TONBERRY_KING = new Card(5, 10, "Tonberry King", 4, 4, 6, 7);

    // Level 6 - Boss

    // Level 7 - Boss

    // Level 8 - GF
    public static final Card IFRIT = new Card(8, 8, "Ifrit", 9, 8, 6, 2);

    // Level 9 - GF
    public static final Card DIABLOS = new Card(9, 2, "Diablos", 5, 3, 10, 8);

    // Level 10 - Player
    public static final Card QUISTIS = new Card(10, 5, "Quistis", 9, 2, 6, 10);
    public static final Card ZELL = new Card(10, 7, "Zell", 8, 6, 5, 10);
    public static final Card SEIFER = new Card(10, 10, "Seifer", 6, 4, 9, 10);

    private final int level;
    private final int sortIndex;
    private final String name;
    private final int up;
    private final int down;
    private final int left;
    private final int right;

    public Card(int level, int sortIndex, String name, int up, int left, int right, int down) {
        this.level = level;
        this.sortIndex = sortIndex;
        this.name = name;
        this.up = up;
        this.left = left;
        this.right = right;
        this.down = down;
    }

    @Override
    public String toString() {
        return name + " [" + up + ", " + left + ", " + right + ", " + down + "]";
    }

    public String getName() {
        return name;
    }

    public int getUp() {
        return up;
    }

    public int getDown() {
        return down;
    }

    public int getLeft() {
        return left;
    }

    public int getRight() {
        return right;
    }

}
