package characters;

/**
 * 敵人基類
 */
public abstract class Enemy extends Character {
    protected int expReward;      // 擊敗時獲得的經驗值
    protected int honorReward;    // 擊敗時獲得的榮譽值
    protected int warPointReward; // 擊敗時獲得的戰功
    
    public Enemy(String name, int maxHp, int atk, int def, 
                 int expReward, int honorReward, int warPointReward) {
        super(name, maxHp, atk, def);
        this.expReward = expReward;
        this.honorReward = honorReward;
        this.warPointReward = warPointReward;
    }
    
    public int getExpReward() {
        return expReward;
    }
    
    public int getHonorReward() {
        return honorReward;
    }
    
    public int getWarPointReward() {
        return warPointReward;
    }
    
    /**
     * 顯示敵人狀態
     */
    public void showStatus() {
        System.out.println("敵人: " + name + " | HP: " + hp + "/" + maxHp + " | ATK: " + atk + " | DEF: " + def);
    }
}
