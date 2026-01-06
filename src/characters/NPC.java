package characters;

/**
 * NPC基類
 */
public abstract class NPC {
    protected String name;
    protected String description;
    
    public NPC(String name, String description) {
        this.name = name;
        this.description = description;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public abstract void interact(Player player);
}
