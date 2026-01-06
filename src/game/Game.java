package game;

import characters.*;
import battle.Battle;
import events.EventManager;
import java.util.Scanner;
import java.util.Random;
import java.io.IOException;

/**
 * 主遊戲類 - 控制整個遊戲流程
 */
public class Game {
    private Player player;
    private EventManager eventManager;
    private Scanner scanner;
    private boolean gameRunning;
    private Mercenary mercenary;
    private WanderingMonk wanderingMonk;
    
    public Game() {
        this.scanner = new Scanner(System.in);
        new Random();
        this.gameRunning = false;
        this.mercenary = new Mercenary();
        this.wanderingMonk = new WanderingMonk();
    }
    
    /**
     * 清空終端螢幕
     */
    private void clearScreen() {
        try {
            // 對於 macOS 和 Linux
            new ProcessBuilder("clear").inheritIO().start().waitFor();
        } catch (IOException | InterruptedException e) {
            // 如果無法執行 clear 命令，使用替代方法
            System.out.print("\033[H\033[2J");
            System.out.flush();
        }
    }
    
    /**
     * 初始化遊戲
     */
    public void initialize() {
        System.out.println("╔════════════════════════════════════════════════╗");
        System.out.println("║   🎮 回合制戰鬥之我身處中世紀戰爭 🎮         ║");
        System.out.println("╚════════════════════════════════════════════════╝\n");
        
        System.out.print("請輸入你的名字 (戰士): ");
        String playerName = scanner.nextLine().trim();
        if (playerName.isEmpty()) {
            playerName = "戰士";
        }
        
        this.player = new Player(playerName);
        this.eventManager = new EventManager(player);
        
        System.out.println("\n歡迎, " + playerName + "!");
        System.out.println("你現在身處在中世紀的戰場上...");
        System.out.println("要生存下來，你必須在戰場上證明自己。\n");
        
        player.showStatus();
    }
    
    /**
     * 開始遊戲主循環
     */
    public void start() {
        initialize();
        gameRunning = true;
        int dayCount = 0;
        
        while (gameRunning && player.isAlive()) {
            clearScreen();
            dayCount++;
            System.out.println("\n═════════════════════════════════════════════════");
            System.out.println("第 " + dayCount + " 天 - 新的冒險");
            System.out.println("═════════════════════════════════════════════════");
            
            // 顯示當前狀態
            System.out.println("\n你有以下選擇:");
            System.out.println("1. 上陣線尋找敵人戰鬥");
            System.out.println("2. 查看當前狀態");
            System.out.println("3. 與 NPC 互動");
            System.out.println("4. 休息 (恢復 HP)");
            System.out.println("5. 結束冒險 (進行結局判定)");
            System.out.print("選擇 (1-5): ");
            
            int choice = getInput(1, 5);
            clearScreen();
            
            switch (choice) {
                case 1:
                    goToBattle();
                    // 戰鬥後檢查事件
                    if (player.isAlive()) {
                        eventManager.tryTriggerRandomEvent();
                    }
                    break;
                case 2:
                    player.showStatus();
                    break;
                case 3:
                    interactWithNPC();
                    break;
                case 4:
                    rest();
                    break;
                case 5:
                    endGame();
                    break;
            }
            
            // 檢查固定事件 (需要達成特定條件)
            if (player.isAlive() && eventManager.checkFixedEvents()) {
                // 如果觸發了最後戰役，結束遊戲
                if (player.getWarPoint() >= 150) {
                    endGame();
                }
            }
        }
    }
    
    /**
     * 上陣線戰鬥
     */
    private void goToBattle() {
        clearScreen();
        System.out.println("\n你踏上了戰場...\n");
        
        // 根據玩家當前等級隨機選擇敵人
        Enemy enemy = generateRandomEnemy();
        System.out.println("你遇到了: " + enemy.getName() + "!");
        
        Battle battle = new Battle(player, enemy);
        boolean playerWon = battle.start();
        
        if (!playerWon) {
            gameRunning = false;
        }
        
        System.out.println("\n按 Enter 繼續...");
        scanner.nextLine();
    }
    
    /**
     * 隨機生成敵人
     */
    private Enemy generateRandomEnemy() {
        double random = Math.random();
        player.getLevel();
        
        // 根據玩家等級調整敵人
        if (random < 0.5) {
            // 50% 小兵
            return new Soldier();
        } else if (random < 0.85) {
            // 35% 將領
            return new General();
        } else {
            // 15% 逃兵
            return new Deserter();
        }
    }
    
    /**
     * 與 NPC 互動
     */
    private void interactWithNPC() {
        clearScreen();
        System.out.println("\n═════════════════════════════════════════════════");
        System.out.println("你遇到了以下 NPC:");
        System.out.println("1. " + mercenary.getName() + " - " + mercenary.getDescription());
        System.out.println("2. " + wanderingMonk.getName() + " - " + wanderingMonk.getDescription());
        System.out.println("3. 離開");
        System.out.print("選擇要互動的 NPC (1-3): ");
        
        int choice = getInput(1, 3);
        clearScreen();
        
        switch (choice) {
            case 1:
                mercenary.interact(player);
                break;
            case 2:
                handleMonkInteraction();
                break;
            case 3:
                System.out.println("你離開了 NPC...");
                break;
        }
        
        System.out.println("\n按 Enter 繼續...");
        scanner.nextLine();
    }
    
    /**
     * 處理與修道士的互動
     */
    private void handleMonkInteraction() {
        wanderingMonk.interact(player);
        
        System.out.print("選擇 (1-3): ");
        int choice = getInput(1, 3);
        clearScreen();
        
        switch (choice) {
            case 1:
                wanderingMonk.heal(player);
                break;
            case 2:
                wanderingMonk.predictFate(player);
                break;
            case 3:
                System.out.println("你離開了修道士...");
                break;
        }
    }
    
    /**
     * 休息
     */
    private void rest() {
        clearScreen();
        System.out.println("\n你決定在營地休息...");
        int healAmount = (int) (player.getMaxHp() * 0.5);  // 恢復50% HP
        int oldHp = player.getHp();
        player.heal(healAmount);
        System.out.println("💚 你得到了充分的休息。");
        System.out.println("HP: " + oldHp + " -> " + player.getHp() + "/" + player.getMaxHp());
        
        System.out.println("\n按 Enter 繼續...");
        scanner.nextLine();
    }
    
    /**
     * 結束遊戲
     */
    private void endGame() {
        clearScreen();
        gameRunning = false;
        System.out.println("\n═════════════════════════════════════════════════");
        
        EndingManager.Ending ending = EndingManager.determineEnding(player);
        EndingManager.showEnding(ending, player);
        
        System.out.println("\n═════════════════════════════════════════════════");
        System.out.println("感謝遊玩《回合制戰鬥之我身處中世紀戰爭》");
        System.out.println("═════════════════════════════════════════════════");
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
    
    /**
     * 主方法 - 遊戲入口
     */
    public static void main(String[] args) {
        Game game = new Game();
        game.start();
    }
}
