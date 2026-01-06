package characters;

/**
 * 傭兵NPC - 會根據玩家的榮譽值和戰功決定是否背叛
 */
public class Mercenary extends NPC {
    public Mercenary() {
        super("傭兵", "一名貪財的傭兵，會根據你的聲望決定是否幫助你");
    }
    
    @Override
    public void interact(Player player) {
        System.out.println("\n═══════════════════════════════════");
        System.out.println("傭兵說: \"看看你的聲望啊...\"");
        
        int loyalty = player.getHonor() - player.getWarPoint();
        if (loyalty >= 30) {
            System.out.println("\"你的聲望很好，我會幫你戰鬥！\"");
            System.out.println("傭兵加入你的隊伍！");
            System.out.println("📊 下次戰鬥時獲得 +30% 攻擊加成");
        } else if (loyalty >= 0) {
            System.out.println("\"看起來還不錯...我會考慮。\"");
            System.out.println("傭兵中立態度");
        } else {
            System.out.println("\"你的聲望太糟糕了！我才不跟你一起！\"");
            System.out.println("⚠️  傭兵背叛了你!");
            player.gainHonor(-20);
        }
        System.out.println("═══════════════════════════════════\n");
    }
}
