package events;

import characters.Player;
import java.util.Scanner;

/**
 * 事件系統 - 處理遊戲中的隨機事件
 */
public class EventManager {
    private Player player;
    private Scanner scanner;
    
    public EventManager(Player player) {
        this.player = player;
        this.scanner = new Scanner(System.in);
    }
    
    /**
     * 戰場廢墟事件 - 戰鬥後30%機率觸發
     */
    public void battlefieldRuinsEvent() {
        System.out.println("\n╔════════════════════════════════════╗");
        System.out.println("║        戰場廢墟事件                ║");
        System.out.println("╚════════════════════════════════════╝");
        System.out.println("你走進滿目瘡痍的戰場廢墟...");
        System.out.println("1. 搜刮物資 (HP +10, 榮譽 -10)");
        System.out.println("2. 幫助倖存者 (榮譽 +15)");
        System.out.println("3. 無視離開 (無變化)");
        System.out.print("選擇: ");
        
        int choice = getInput(1, 3);
        
        switch (choice) {
            case 1:
                System.out.println("你開始搜刮廢墟中的物資...");
                player.restoreHealth(10);
                player.gainHonor(-10);
                break;
            case 2:
                System.out.println("你救起了幾名奄奄一息的倖存者...");
                player.gainHonor(15);
                System.out.println("倖存者感激地向你致謝。");
                break;
            case 3:
                System.out.println("你無視了周圍的一切，冷漠地離開了。");
                break;
        }
    }
    
    /**
     * 投降敵軍事件 - WarPoint >= 50時觸發
     */
    public void surrenderedEnemyEvent() {
        System.out.println("\n╔════════════════════════════════════╗");
        System.out.println("║        投降敵軍事件                ║");
        System.out.println("╚════════════════════════════════════╝");
        System.out.println("一隊敵軍向你投降，他們跪地乞求憐憫...");
        System.out.println("1. 處決 (戰功 +10, 榮譽 -20, 擊殺數 +5)");
        System.out.println("2. 俘虜 (榮譽 +10)");
        System.out.print("選擇: ");
        
        int choice = getInput(1, 2);
        
        switch (choice) {
            case 1:
                System.out.println("你無情地處決了投降的敵軍...");
                player.gainWarPoint(10);
                player.gainHonor(-20);
                player.addKillCount(5);
                System.out.println("⚠️  你開始看起來像個真正的屠夫...");
                break;
            case 2:
                System.out.println("你將敵軍收為俘虜，對待他們相對仁慈...");
                player.gainHonor(10);
                System.out.println("他們感激你保留了他們的生命。");
                break;
        }
    }
    
    /**
     * 教會審判事件 - Honor <= -30時觸發
     */
    public void churchTrialEvent() {
        System.out.println("\n╔════════════════════════════════════╗");
        System.out.println("║        教會審判事件                ║");
        System.out.println("╚════════════════════════════════════╝");
        System.out.println("教會為你的暴行進行審判...");
        System.out.println("主教說: \"你的罪孽太多了！\"");
        System.out.println("1. 通過審判 (運氣好)");
        System.out.println("2. 失敗 (進入強制戰鬥)");
        System.out.print("選擇: ");
        
        int choice = getInput(1, 2);
        
        switch (choice) {
            case 1:
                if (Math.random() < 0.4) {  // 40%成功率
                    System.out.println("主教檢視了你的內心，發現了一絲悔意...");
                    System.out.println("\"也許還有救...但要小心！\"");
                    player.gainHonor(10);
                } else {
                    System.out.println("你的謊言被識破了！");
                    forceChurchBattle();
                }
                break;
            case 2:
                System.out.println("你拒絕接受審判，教會對你宣戰！");
                forceChurchBattle();
                break;
        }
    }
    
    /**
     * 強制教會戰鬥
     */
    private void forceChurchBattle() {
        System.out.println("⚔️  教會戰士向你發起攻擊！");
        System.out.println("(這是一場強制戰鬥)");
        // 在實際遊戲中，這會觸發一場強制戰鬥
    }
    
    /**
     * 最後戰役事件 - WarPoint >= 150時觸發 (固定事件)
     */
    public void finalBattleEvent() {
        System.out.println("\n╔════════════════════════════════════╗");
        System.out.println("║        最後戰役事件                ║");
        System.out.println("╚════════════════════════════════════╝");
        System.out.println("\n經過無數次戰鬥，你終於來到了戰爭的終點...");
        System.out.println("眼前是敵方的最後堡壘，以及那位傳說中的敵軍統帥！");
        System.out.println("\n這是最終決戰！");
    }
    
    /**
     * 隨機觸發戰鬥後事件
     */
    public void tryTriggerRandomEvent() {
        double random = Math.random();
        
        // 戰場廢墟 - 30%機率
        if (random < 0.3) {
            battlefieldRuinsEvent();
        }
    }
    
    /**
     * 檢查固定事件
     */
    public boolean checkFixedEvents() {
        // 投降敵軍事件
        if (player.getWarPoint() >= 50 && player.getWarPoint() < 150 && Math.random() < 0.2) {
            surrenderedEnemyEvent();
            return true;
        }
        
        // 教會審判事件
        if (player.getHonor() <= -30 && Math.random() < 0.15) {
            churchTrialEvent();
            return true;
        }
        
        // 最後戰役事件
        if (player.getWarPoint() >= 150) {
            finalBattleEvent();
            return true;
        }
        
        return false;
    }
    
    /**
     * 獲取玩家輸入
     */
    private int getInput(int min, int max) {
        while (true) {
            try {
                int input = Integer.parseInt(scanner.nextLine());
                if (input >= min && input <= max) {
                    return input;
                }
                System.out.print("請輸入 " + min + "-" + max + " 之間的數字: ");
            } catch (NumberFormatException e) {
                System.out.print("無效的輸入，請重試: ");
            }
        }
    }
}
