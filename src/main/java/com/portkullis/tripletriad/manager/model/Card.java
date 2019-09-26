package com.portkullis.tripletriad.manager.model;

import java.util.Objects;

/**
 * Created by darius on 1/18/16.
 */
public class Card implements Comparable<Card> {

    // Level 1
    public static final Card C1541_1_GEEZARD = new Card(1, 1, "Geezard", 1, 5, 4, 1);
    public static final Card C5311_1_FUNGUAR = new Card(1, 2, "Funguar", 5, 3, 1, 1);
    public static final Card C1533_1_BITE_BUG = new Card(1, 3, "Bite Bug", 1, 5, 3, 3);
    public static final Card C6211_1_RED_BAT = new Card(1, 4, "Red Bat", 6, 2, 1, 1);
    public static final Card C2531_1_BLOBRA = new Card(1, 5, "Blobra", 2, 5, 3, 1);
    public static final Card C2414_1_GAYLA = new Card(1, 6, "Gayla", 2, 5, 3, 1);
    public static final Card C1154_1_GESPER = new Card(1, 7, "Gesper", 1, 1, 5, 4);
    public static final Card C3152_1_FASTITOCALON_F = new Card(1, 8, "Fastitocalon-F", 3, 1, 5, 2);
    public static final Card C2116_1_BLOOD_SOUL = new Card(1, 9, "Blood Soul", 2, 1, 1, 6);
    public static final Card C4324_1_CATERCHIPILLAR = new Card(1, 10, "Caterchipillar", 4, 3, 2, 4);
    public static final Card C2612_1_COCKATRICE = new Card(1, 11, "Cockatrice", 2, 6, 1, 2);

    // Level 2
    public static final Card C7113_2_GRAT = new Card(2, 1, "Grat", 7, 1, 1, 3);
    public static final Card C6322_2_BUEL = new Card(2, 2, "Buel", 6, 3, 2, 2);
    public static final Card C5433_2_MESMERIZE = new Card(2, 3, "Mesmerize", 5, 4, 3, 3);
    public static final Card C6314_2_GLACIAL_EYE = new Card(2, 4, "Glacial Eye", 6, 3, 1, 4);
    public static final Card C3345_2_BELHELMEL = new Card(2, 5, "Belhelmel", 3, 3, 4, 5);
    public static final Card C5532_2_THRUSTAEVIS = new Card(2, 6, "Thrustaevis", 5, 5, 3, 2);
    public static final Card C5513_2_ANACONDAUR = new Card(2, 7, "Anacondaur", 5, 5, 1, 3);
    public static final Card C5225_2_CREEPS = new Card(2, 8, "Creeps", 5, 2, 2, 5);
    public static final Card C4245_2_GRENDEL = new Card(2, 9, "Grendel", 4, 2, 4, 5);
    public static final Card C3721_2_JELLEYE = new Card(2, 10, "Jelleye", 3, 7, 2, 1);
    public static final Card C5325_2_GRAND_MANTIS = new Card(2, 11, "Grand Mantis", 5, 3, 2, 5);

    // Level 3
    public static final Card C6263_3_FORBIDDEN = new Card(3, 1, "Forbidden", 6, 2, 6, 3);
    public static final Card C6631_3_ARMADODO = new Card(3, 2, "Armadodo", 6, 6, 3, 1);
    public static final Card C3555_3_TRI_FACE = new Card(3, 3, "Tri-Face", 3, 5, 5, 5);
    public static final Card C7351_3_FASTITOCALON = new Card(3, 4, "Fastitocalon", 7, 3, 5, 1);
    public static final Card C7315_3_SNOW_LION = new Card(3, 5, "Snow Lion", 7, 3, 1, 5);
    public static final Card C5363_3_OCHU = new Card(3, 6, "Ochu", 5, 3, 6, 3);
    public static final Card C5462_3_SAM08G = new Card(3, 7, "SAM08G", 5, 4, 6, 2);
    public static final Card C4247_3_DEATH_CLAW = new Card(3, 8, "Death Claw", 4, 2, 4, 7);
    public static final Card C6326_3_CACTUAR = new Card(3, 9, "Cactaur", 6, 3, 2, 6);
    public static final Card C3464_3_TONBERRY = new Card(3, 10, "Tonberry", 3, 4, 6, 4);
    public static final Card C7523_3_ABYSS_WORM = new Card(3, 11, "Abyss Worm", 7, 5, 2, 3);

    // Level 4
    public static final Card C2736_4_TURTAPOD = new Card(4, 1, "Turtapod", 2, 7, 3, 6);
    public static final Card C6554_4_VYSAGE = new Card(4, 2, "Vysage", 6, 5, 5, 4);
    public static final Card C4762_4_T_REXAUR = new Card(4, 3, "T-Rexaur", 4, 7, 6, 2);
    public static final Card C2376_4_BOMB = new Card(4, 4, "Bomb", 2, 3, 7, 6);
    public static final Card C1764_4_BLITZ = new Card(4, 4, "Blitz", 1, 7, 6, 4);
    public static final Card C7444_4_TORAMA = new Card(4, 7, "Torama", 7, 4, 4, 4);
    public static final Card C3673_4_IMP = new Card(4, 8, "Imp", 3, 6, 7, 3);
    public static final Card C6327_4_BLUE_DRAGON = new Card(4, 9, "Blue Dragon", 6, 3, 2, 7);
    public static final Card C4655_4_ADAMANTOISE = new Card(4, 10, "Adamantoise", 4, 6, 5, 5);
    public static final Card C7354_4_HEXADRAGON = new Card(4, 11, "Hexadragon", 7, 3, 5, 4);

    // Level 5
    public static final Card C6556_5_IRON_GIANT = new Card(5, 1, "Iron Giant", 6, 5, 5, 6);
    public static final Card C3765_5_BEHEMOTH = new Card(5, 2, "Behemoth", 3, 7, 6, 5);
    public static final Card C7365_5_CHIMERA = new Card(5, 3, "Chimera", 7, 3, 6, 5);
    public static final Card C6726_5_ELASTOID = new Card(5, 5, "Elastoid", 6, 7, 2, 6);
    public static final Card C5457_5_GIM47N = new Card(5, 6, "GIM47N", 5, 4, 5, 7);
    public static final Card C7274_5_MALBORO = new Card(5, 7, "Malboro", 7, 2, 7, 4);
    public static final Card C5637_5_ELNOYLE = new Card(5, 9, "Elnoyle", 5, 6, 3, 7);
    public static final Card C4467_5_TONBERRY_KING = new Card(5, 10, "Tonberry King", 4, 4, 6, 7);
    public static final Card C6762_5_WEDGE_BIGGS = new Card(5, 11, "Wedge, Biggs", 6, 7, 6, 2);

    // Level 6 - Boss

    // Level 7 - Boss

    // Level 8 - GF
    public static final Card C9367_8_ANGELO = new Card(8, 2, "Angelo", 9, 3, 6, 7);
    public static final Card C9239_8_MINIMOG = new Card(8, 4, "MiniMog", 9, 2, 3, 9);
    public static final Card C9862_8_IFRIT = new Card(8, 8, "Ifrit", 9, 8, 6, 2);
    public static final Card C8296_8_SIREN = new Card(8, 9, "Siren", 8, 2, 9, 6);

    // Level 9 - GF
    public static final Card C53A8_9_DIABLOS = new Card(9, 2, "Diablos", 5, 3, 10, 8);

    // Level 10 - Player
    public static final Card C926A_10_QUISTIS = new Card(10, 5, "Quistis", 9, 2, 6, 10);
    public static final Card C865A_10_ZELL = new Card(10, 7, "Zell", 8, 6, 5, 10);
    public static final Card C649A_10_SEIFER = new Card(10, 10, "Seifer", 6, 4, 9, 10);

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return level == card.level &&
                sortIndex == card.sortIndex &&
                getUp() == card.getUp() &&
                getDown() == card.getDown() &&
                getLeft() == card.getLeft() &&
                getRight() == card.getRight() &&
                Objects.equals(getName(), card.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(level, sortIndex, getName(), getUp(), getDown(), getLeft(), getRight());
    }

    @Override
    public int compareTo(Card o) {
        int compare = level - o.level;
        if (compare == 0) {
            compare = sortIndex - o.sortIndex;
        }
        return compare;
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
