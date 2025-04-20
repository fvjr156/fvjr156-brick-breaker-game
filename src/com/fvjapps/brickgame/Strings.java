package src.com.fvjapps.brickgame;

public class Strings {
    private static final String EN[] = {
            "BRICK BREAKER GAME",
            "Press ENTER to start game.",
            "LEFT and RIGHT arrow keys to move. SHIFT for movement speed boost.",
            "1, 2, 3 to change game mode. 0 to open settings.",
            "EASY",
            "NORMAL",
            "EXPERT",
            "SCORE: ",
            "Settings",
            "Language",
            "Brick Rows",
            "Brick Columns",
            "Ball Count",
            "Save",
            "CRAZY Mode",
            "On game: ESC to exit mode."
    };
    private static final String JP[] = {
            "ブロック崩しゲーム",
            "Enterキーでゲーム開始。",
            "←→で移動。Shiftで加速。",
            "1、2、3でゲームモードを変更。0で設定をディスプレイする。",
            "イージー",
            "ノーマル",
            "エキスパート",
            "スコア: ",
            "設定",
            "言語",
            "ブロックの行数",
            "ブロックの列数",
            "ボールの数",
            "保存",
            "クレイジーモード",
            "ゲーム中、ESCキー押したら、ゲームやめる。"
    };
    private static final String CN[] = {
        "打砖块游戏", 
        "按ENTER键开始游戏。", 
        "使用左右方向键移动。Shift键加速。",
        "1、2、3切换游戏模式。0打开设置。", 
        "简单", 
        "普通",
        "专家",
        "分数: ",
        "设置",
        "语言",
        "砖块行数",
        "砖块列数",  
        "球数", 
        "保存",
        "疯狂模式",
        "游戏中，按ESC键退出模式。"
    };
    

    public static String[] getStrings(String code) {
        switch (code) {
            case "JP":
                return JP;
            case "CN":
                return CN;
            default:
                return EN;
        }
    }
}
