package characters;

/**
 * 玩家角色類別
 */
public class Player extends Character {
    private int exp;           // 經驗值
    private int level;         // 等級
    private int honor;         // 戰爭榮譽
    private int warPoint;      // 戰功
    private int killCount;     // 擊殺數
    
    public Player(String name) {
        super(name, 100, 15, 10);  // HP, ATK, DEF的初始值
        this.exp = 0;
        this.level = 1;
        this.honor = 0;
        this.warPoint = 0;
        this.killCount = 0;
    }
    
    /**
     * 獲得經驗值並檢查是否升級
     */
    public void gainExp(int amount) {
        this.exp += amount;
        int expNeeded = level * 100;  // 每級需要 level * 100 的經驗值
        
        while (this.exp >= expNeeded) {
            levelUp();
            this.exp -= expNeeded;
            expNeeded = level * 100;
        }
    }
    
    /**
     * 升級
     */
    private void levelUp() {
        this.level++;
        this.maxHp += 20;
        this.hp = this.maxHp;
        this.atk += 5;
        this.def += 3;
        System.out.println("🎉 升級了！現在等級: " + level + ", HP: " + maxHp + ", ATK: " + atk + ", DEF: " + def);
    }
    
    /**
     * 獲得榮譽值
     */
    public void gainHonor(int amount) {
        this.honor += amount;
        System.out.println((amount > 0 ? "➕" : "➖") + " 榮譽值變化: " + amount + " (目前: " + honor + ")");
    }
    
    /**
     * 獲得戰功
     */
    public void gainWarPoint(int amount) {
        this.warPoint += amount;
        System.out.println((amount > 0 ? "➕" : "➖") + " 戰功變化: " + amount + " (目前: " + warPoint + ")");
    }
    
    /**
     * 增加擊殺數
     */
    public void addKillCount(int amount) {
        this.killCount += amount;
    }
    
    /**
     * 恢復HP
     */
    public void restoreHealth(int amount) {
        int oldHp = hp;
        heal(amount);
        System.out.println("💚 HP恢復 +" + (hp - oldHp) + " (目前: " + hp + "/" + maxHp + ")");
    }
    
    // Getters
    public int getExp() {
        return exp;
    }
    
    public int getLevel() {
        return level;
    }
    
    public int getHonor() {
        return honor;
    }
    
    public int getWarPoint() {
        return warPoint;
    }
    
    public int getKillCount() {
        return killCount;
    }
    
    /**
     * 顯示玩家當前狀態
     */
    public void showStatus() {
        System.out.println("\n╔════════════════════════════════════╗");
        System.out.println("║        玩家狀態                   ║");
        System.out.println("╠════════════════════════════════════╣");
        System.out.println("║ 名稱: " + name);
        System.out.println("║ 等級: " + level + " | 經驗: " + exp);
        System.out.println("║ HP: " + hp + "/" + maxHp);
        System.out.println("║ 攻擊: " + atk + " | 防禦: " + def);
        System.out.println("║ 榮譽: " + honor + " | 戰功: " + warPoint);
        System.out.println("║ 擊殺數: " + killCount);
        System.out.println("╚════════════════════════════════════╝");
    }
}
