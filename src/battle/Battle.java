package battle;

import characters.Character;
import characters.Enemy;
import characters.Player;
import java.util.Scanner;

/**
 * 戰鬥系統
 */
public class Battle {
    private Player player;
    private Enemy enemy;
    private Scanner scanner;
    private int round;
    
    public Battle(Player player, Enemy enemy) {
        this.player = player;
        this.enemy = enemy;
        this.scanner = new Scanner(System.in);
        this.round = 0;
    }
    
    /**
     * 開始戰鬥
     */
    public boolean start() {
        System.out.println("\n╔════════════════════════════════════╗");
        System.out.println("║          戰鬥開始！                ║");
        System.out.println("║  vs " + enemy.getName());
        System.out.println("╚════════════════════════════════════╝\n");
        
        while (player.isAlive() && enemy.isAlive()) {
            round++;
            System.out.println("\n【第 " + round + " 回合】");
            showBattleState();
            
            // 玩家行動
            playerTurn();
            if (!enemy.isAlive()) {
                playerVictory();
                return true;
            }
            
            // 敵人行動
            enemyTurn();
            if (!player.isAlive()) {
                playerDefeat();
                return false;
            }
        }
        
        return player.isAlive();
    }
    
    /**
     * 顯示戰鬥狀態
     */
    private void showBattleState() {
        System.out.println("┌─ 玩家 ─────────────────────────────┐");
        System.out.println("│ " + player.getName() + " HP: " + player.getHp() + "/" + player.getMaxHp());
        System.out.println("│ ATK: " + player.getAtk() + " | DEF: " + player.getDef());
        System.out.println("└────────────────────────────────────┘");
        System.out.println("┌─ 敵人 ─────────────────────────────┐");
        System.out.println("│ " + enemy.getName() + " HP: " + enemy.getHp() + "/" + enemy.getMaxHp());
        System.out.println("│ ATK: " + enemy.getAtk() + " | DEF: " + enemy.getDef());
        System.out.println("└────────────────────────────────────┘");
    }
    
    /**
     * 玩家回合
     */
    private void playerTurn() {
        System.out.println("\n你的回合:");
        System.out.println("1. 攻擊");
        System.out.println("2. 防禦 (下回合傷害-30%)");
        System.out.println("3. 逃跑 (50%成功率)");
        System.out.print("選擇行動 (1-3): ");
        
        int choice = getInput(1, 3);
        
        switch (choice) {
            case 1:
                attack(player, enemy);
                break;
            case 2:
                defend();
                break;
            case 3:
                if (attemptEscape()) {
                    System.out.println("成功逃離戰鬥！");
                    player.setHp(0);  // 標記為逃跑
                    return;
                }
                System.out.println("逃跑失敗！");
                attack(player, enemy);
                break;
        }
    }
    
    /**
     * 敵人回合 - AI簡單邏輯
     */
    private void enemyTurn() {
        System.out.println("\n敵人的回合:");
        
        // 簡單AI: 如果敵人HP低於50%，30%機率防禦
        if (enemy.getHp() < enemy.getMaxHp() / 2 && Math.random() < 0.3) {
            System.out.println(enemy.getName() + " 進行防禦姿勢！");
            lastRoundDefended = true;
        } else {
            attack(enemy, player);
        }
    }
    
    /**
     * 攻擊動作
     */
    private void attack(Character attacker, Character defender) {
        int damage = attacker.calculateDamage(defender);
        
        // 如果防禦者上回合進行了防禦，傷害減少30%
        if (defender == player && lastRoundDefended) {
            damage = (int) (damage * 0.7);
            lastRoundDefended = false;
        }
        
        defender.takeDamage(damage);
        
        String attackerName = attacker == player ? "你" : enemy.getName();
        String defenderName = defender == player ? "你" : enemy.getName();
        
        System.out.println("⚔️  " + attackerName + " 攻擊 " + defenderName + "，造成 " + damage + " 點傷害！");
        System.out.println("💔 " + defenderName + " 剩餘HP: " + defender.getHp());
    }
    
    private boolean lastRoundDefended = false;
    
    /**
     * 防禦動作
     */
    private void defend() {
        System.out.println("🛡️  你擺出防禦姿勢，準備承受下一次攻擊！");
        lastRoundDefended = true;
    }
    
    /**
     * 嘗試逃跑
     */
    private boolean attemptEscape() {
        return Math.random() < 0.5;  // 50%成功率
    }
    
    /**
     * 玩家勝利
     */
    private void playerVictory() {
        System.out.println("\n╔════════════════════════════════════╗");
        System.out.println("║        🎉 戰鬥勝利！  🎉          ║");
        System.out.println("╚════════════════════════════════════╝\n");
        
        player.gainExp(enemy.getExpReward());
        player.gainHonor(enemy.getHonorReward());
        player.gainWarPoint(enemy.getWarPointReward());
        player.addKillCount(1);
        
        System.out.println("獲得獎勵:");
        System.out.println("  + " + enemy.getExpReward() + " 經驗值");
        System.out.println("  + " + enemy.getHonorReward() + " 榮譽值");
        System.out.println("  + " + enemy.getWarPointReward() + " 戰功");
    }
    
    /**
     * 玩家戰敗
     */
    private void playerDefeat() {
        System.out.println("\n╔════════════════════════════════════╗");
        System.out.println("║        ☠️  你已陣亡！  ☠️           ║");
        System.out.println("╚════════════════════════════════════╝\n");
        System.out.println("遊戲結束...");
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
