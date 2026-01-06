package characters;

/**
 * 流浪修道士NPC - 可以治療或預言結局
 */
public class WanderingMonk extends NPC {
    public WanderingMonk() {
        super("流浪修道士", "一名神聖的修道士，掌握治療和預言的力量");
    }
    
    @Override
    public void interact(Player player) {
        System.out.println("\n═══════════════════════════════════");
        System.out.println("流浪修道士說: \"戰士啊，你需要什麼幫助？\"");
        System.out.println("1. 治療傷口 (恢復50 HP)");
        System.out.println("2. 預言命運 (查看可能的結局)");
        System.out.println("3. 離開");
        System.out.println("═══════════════════════════════════");
    }
    
    public void heal(Player player) {
        int originalHp = player.getHp();
        player.heal(50);
        System.out.println("✨ 修道士為你治療了傷口！");
        System.out.println("💚 HP: " + originalHp + " -> " + player.getHp());
    }
    
    public void predictFate(Player player) {
        System.out.println("\n✨ 修道士閉上眼睛，進入冥想狀態...");
        System.out.println("修道士說: \"我看到了你的命運...\"");
        System.out.println("\n═══════════════════════════════════");
        System.out.println("可能的結局:");
        
        if (player.getWarPoint() >= 150) {
            if (player.getHonor() >= 50 && player.getKillCount() < 30) {
                System.out.println("✨ 榮譽騎士 - 你將被銘記為偉大的戰士");
            } else if (player.getHonor() >= -20 && player.getHonor() <= 49) {
                System.out.println("⚔️  戰爭倖存者 - 你將活著離開戰場");
            } else if (player.getHonor() <= -50 && player.getKillCount() >= 50) {
                System.out.println("🩸 嗜血屠夫 - 你將淪為戰爭的怪物");
            }
        }
        
        if (player.getHp() <= 0) {
            System.out.println("☠️  戰死沙場 - 你將在戰場上畫下句點");
        }
        
        System.out.println("═══════════════════════════════════\n");
    }
}
