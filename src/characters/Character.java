package characters;

/**
 * 角色基礎類別 - 所有遊戲角色的父類
 */
public abstract class Character {
    protected String name;
    protected int hp;
    protected int maxHp;
    protected int atk;  // 攻擊力
    protected int def;  // 防禦力
    
    public Character(String name, int maxHp, int atk, int def) {
        this.name = name;
        this.maxHp = maxHp;
        this.hp = maxHp;
        this.atk = atk;
        this.def = def;
    }
    
    /**
     * 計算傷害 - 攻擊者對防禦者造成的傷害
     */
    public int calculateDamage(Character defender) {
        int baseDamage = this.atk - (defender.def / 2);
        // 增加20%的隨機波動
        int variance = (int) (baseDamage * 0.2);
        int damage = baseDamage + (int) (Math.random() * variance - variance / 2);
        return Math.max(1, damage);  // 最少造成1點傷害
    }
    
    /**
     * 受傷
     */
    public void takeDamage(int damage) {
        this.hp -= damage;
        if (this.hp < 0) {
            this.hp = 0;
        }
    }
    
    /**
     * 治療
     */
    public void heal(int amount) {
        this.hp = Math.min(this.hp + amount, this.maxHp);
    }
    
    /**
     * 角色是否還活著
     */
    public boolean isAlive() {
        return this.hp > 0;
    }
    
    // Getters and Setters
    public String getName() {
        return name;
    }
    
    public int getHp() {
        return hp;
    }
    
    public int getMaxHp() {
        return maxHp;
    }
    
    public int getAtk() {
        return atk;
    }
    
    public int getDef() {
        return def;
    }
    
    public void setHp(int hp) {
        this.hp = Math.min(hp, maxHp);
    }
}
