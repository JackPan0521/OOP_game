package game;

import characters.Player;

/**
 * 結局判定系統
 */
public class EndingManager {
    
    /**
     * 判定結局並顯示
     */
    public static Ending determineEnding(Player player) {
        System.out.println("\n╔════════════════════════════════════╗");
        System.out.println("║        遊戲結束，結局判定中...    ║");
        System.out.println("╚════════════════════════════════════╝\n");
        
        // 優先級1: 戰死沙場
        if (player.getHp() <= 0) {
            return Ending.FALLEN_ON_BATTLEFIELD;
        }
        
        // 優先級2: 榮譽騎士
        if (player.getWarPoint() >= 150 && 
            player.getHonor() >= 50 && 
            player.getKillCount() < 30) {
            return Ending.HONORABLE_KNIGHT;
        }
        
        // 優先級3: 戰爭倖存者
        if (player.getWarPoint() >= 150 && 
            player.getHonor() >= -20 && 
            player.getHonor() <= 49) {
            return Ending.WAR_SURVIVOR;
        }
        
        // 優先級4: 嗜血屠夫
        if (player.getWarPoint() >= 150 && 
            player.getHonor() <= -50 && 
            player.getKillCount() >= 50) {
            return Ending.BLOODTHIRSTY_BUTCHER;
        }
        
        // 預設: 未完成遊戲 (不應該觸發，因為最後戰役必須完成)
        return Ending.INCOMPLETE;
    }
    
    /**
     * 顯示結局內容
     */
    public static void showEnding(Ending ending, Player player) {
        System.out.println("\n╔════════════════════════════════════════════════════╗");
        System.out.println("║             以下是你的故事的結局...               ║");
        System.out.println("╚════════════════════════════════════════════════════╝\n");
        
        switch (ending) {
            case HONORABLE_KNIGHT:
                showHonorableKnightEnding(player);
                break;
            case WAR_SURVIVOR:
                showWarSurvivorEnding(player);
                break;
            case BLOODTHIRSTY_BUTCHER:
                showBloodthirstyButcherEnding(player);
                break;
            case FALLEN_ON_BATTLEFIELD:
                showFallenOnBattlefieldEnding(player);
                break;
            case INCOMPLETE:
                showIncompleteEnding(player);
                break;
        }
    }
    
    private static void showHonorableKnightEnding(Player player) {
        System.out.println("✨ 【榮譽騎士】✨\n");
        System.out.println("戰爭結束了。");
        System.out.println("\n你的英勇事蹟被傳唱在四海八荒...");
        System.out.println("戰士們聚集在篝火周圍，講述著你的故事。");
        System.out.println("\n『那位騎士用他的劍捍衛了無辜者，");
        System.out.println("用他的勇氣點燃了他人心中的希望。』");
        System.out.println("\n國王親自為你加冕，授予你最高的榮譽。");
        System.out.println("你的名字被刻在紀念碑上，將被永遠銘記。");
        System.out.println("\n最終統計:");
        System.out.println("  等級: " + player.getLevel());
        System.out.println("  戰功: " + player.getWarPoint());
        System.out.println("  榮譽: " + player.getHonor());
        System.out.println("  擊殺數: " + player.getKillCount());
        System.out.println("\n🏆 你的故事將永遠被歌頌... 🏆");
    }
    
    private static void showWarSurvivorEnding(Player player) {
        System.out.println("⚔️  【戰爭倖存者】⚔️\n");
        System.out.println("戰爭終於結束了。");
        System.out.println("\n你活著走出了戰場。");
        System.out.println("身體滿是傷痕，靈魂背負著戰爭的重擔。");
        System.out.println("\n你既不被稱為英雄，也不被詛咒為怪物。");
        System.out.println("你只是...活著回來了。");
        System.out.println("\n沒有人會特別記得你，");
        System.out.println("但在酒館的角落，有人會重述你生存的故事。");
        System.out.println("\n最終統計:");
        System.out.println("  等級: " + player.getLevel());
        System.out.println("  戰功: " + player.getWarPoint());
        System.out.println("  榮譽: " + player.getHonor());
        System.out.println("  擊殺數: " + player.getKillCount());
        System.out.println("\n你活著看到了戰爭的終結...");
    }
    
    private static void showBloodthirstyButcherEnding(Player player) {
        System.out.println("🩸 【嗜血屠夫】🩸\n");
        System.out.println("當槍聲最終停息時...");
        System.out.println("\n你站在數百具屍體之上，雙手沾滿鮮血。");
        System.out.println("你早已忘記了自己為何而戰。");
        System.out.println("\n曾經的目標早已模糊，取而代之的是對殺戮的渴望。");
        System.out.println("人們看著你時，眼中滿是恐懼和厭惡。");
        System.out.println("\n你成為了戰爭的怪物。");
        System.out.println("也許有人會紀念你的力量，");
        System.out.println("但沒有人會愛你。");
        System.out.println("\n最終統計:");
        System.out.println("  等級: " + player.getLevel());
        System.out.println("  戰功: " + player.getWarPoint());
        System.out.println("  榮譽: " + player.getHonor());
        System.out.println("  擊殺數: " + player.getKillCount());
        System.out.println("\n你活著，但靈魂已經死去...");
    }
    
    private static void showFallenOnBattlefieldEnding(Player player) {
        System.out.println("☠️  【戰死沙場】☠️\n");
        System.out.println("黑暗吞沒了你的視線...");
        System.out.println("\n你的故事在這裡終結了。");
        System.out.println("沒有人知道你的最後一刻是什麼樣的，");
        System.out.println("也沒有人能夠見證你最後的抉擇。");
        System.out.println("\n有人會記得你嗎？");
        System.out.println("也許會，也許不會。");
        System.out.println("\n你的遺體被埋在了無名的戰場上，");
        System.out.println("被無盡的鮮紅覆蓋...");
        System.out.println("\n最終統計:");
        System.out.println("  等級: " + player.getLevel());
        System.out.println("  戰功: " + player.getWarPoint());
        System.out.println("  榮譽: " + player.getHonor());
        System.out.println("  擊殺數: " + player.getKillCount());
        System.out.println("\nFIN. 遊戲結束。");
    }
    
    private static void showIncompleteEnding(Player player) {
        System.out.println("? 【未完成】?\n");
        System.out.println("你尚未達到遊戲的結束條件...");
        System.out.println("也許你應該繼續冒險？");
    }
    
    /**
     * 結局枚舉
     */
    public enum Ending {
        HONORABLE_KNIGHT("榮譽騎士"),
        WAR_SURVIVOR("戰爭倖存者"),
        BLOODTHIRSTY_BUTCHER("嗜血屠夫"),
        FALLEN_ON_BATTLEFIELD("戰死沙場"),
        INCOMPLETE("未完成");
        
        private final String name;
        
        Ending(String name) {
            this.name = name;
        }
        
        public String getName() {
            return name;
        }
    }
}
