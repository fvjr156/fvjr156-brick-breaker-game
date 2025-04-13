package src.com.fvjapps.brickgame;

public class Strings {
    private static final String EN[] = {
            "BRICK BREAKER GAME",
            "Press ENTER to start game.",
            "LEFT and RIGHT arrow keys to move. SHIFT for movement speed boost.",
            "1, 2, 3 to change game mode.",
            "EASY",
            "NORMAL",
            "EXPERT",
            "SCORE: "
    };
    private static final String JP[] = {
            "ブロック崩しゲーム",
            "Enterキーでゲーム開始。",
            "←→で移動。Shiftで加速。",
            "1、2、3でゲームモードを変更。",
            "イージー",
            "ノーマル",
            "エキスパート",
            "スコア: "
    };

    public static String[] getStrings(String code) {
        switch (code) {
            case "JP":
                return JP;
            default:
                return EN;
        }
    }
}
