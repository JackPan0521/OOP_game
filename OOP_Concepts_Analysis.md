# UML 各包的 OOP 概念分析

本文檔詳細說明遊戲架構中四個包如何應用物件導向程設計（OOP）的核心概念。

---

## 📦 characters 包 - 使用的 OOP 概念

### 關鍵概念應用

| OOP 概念 | 具體應用 |
|---------|---------|
| **繼承** | `Player extends Character`、`Enemy extends Character`、`Soldier/General/Deserter extends Enemy`、`Mercenary/WanderingMonk extends NPC` |
| **抽象類** | `Character` 和 `NPC` 是抽象基類，定義子類必須實現的方法 |
| **多態** | 不同敵人類型（Soldier、General、Deserter）有相同方法但不同實現 |
| **封裝** | 私有屬性（hp、atk、def）通過 getter/setter 訪問 |
| **方法重寫** | 子類重寫父類的 `calculateDamage()`、`takeDamage()` 方法 |

### 代碼示例

```java
// 抽象基類定義模板
abstract class Character {
    protected int hp, maxHp, atk, def;
    
    // 抽象方法 - 多態
    abstract int calculateDamage(int enemyDef);
    
    // 封裝 - 私有屬性
    public void takeDamage(int damage) {
        this.hp -= damage;
    }
}

// 子類實現（繼承 + 多態）
class Player extends Character {
    private int exp, level, honor;
    
    @Override
    int calculateDamage(int enemyDef) {
        // 玩家獨特的傷害計算邏輯
        return (this.atk - enemyDef / 2) + (int)(Math.random() * 20 - 10);
    }
}

class Soldier extends Enemy {
    public Soldier() {
        super("小兵", 30, 8, 3);
    }
    
    @Override
    int calculateDamage(int enemyDef) {
        // 小兵的傷害計算
        return (this.atk - enemyDef / 2) + (int)(Math.random() * 20 - 10);
    }
}
```

### 繼承結構圖

```
Character（抽象類）
├── Player
└── Enemy（抽象類）
    ├── Soldier（攻擊力 8）
    ├── General（攻擊力 18）
    └── Deserter（攻擊力 10）

NPC（抽象類）
├── Mercenary
└── WanderingMonk
```

### 學習重點

- **為什麼用抽象類？** 定義通用接口，強制子類實現特定行為
- **為什麼要繼承？** 複用代碼，避免重複定義 hp、takeDamage() 等
- **多態的好處？** 同一方法名，不同子類有不同表現（不同敵人計算傷害的方式可能不同）

---

## ⚔️ battle 包 - 使用的 OOP 概念

### 關鍵概念應用

| OOP 概念 | 具體應用 |
|---------|---------|
| **依賴注入** | Battle 接受 `Player` 和 `Enemy` 對象作為參數 |
| **封裝** | 將戰鬥邏輯封裝在 `Battle` 類中 |
| **多態** | 使用 `Character` 類型引用，統一調用 `takeDamage()`、`isAlive()` |
| **單一責任** | Battle 只負責戰鬥邏輯，不處理事件或遊戲主循環 |
| **接口隔離** | 只使用 Character 需要的方法 |

### 代碼示例

```java
class Battle {
    private Player player;      // 依賴注入
    private Enemy enemy;
    private int round;
    
    // 構造方法注入依賴
    public Battle(Player player, Enemy enemy) {
        this.player = player;
        this.enemy = enemy;
        this.round = 0;
    }
    
    // 多態：使用抽象類 Character 的方法
    boolean start() {
        while (player.isAlive() && enemy.isAlive()) {
            playerTurn();
            enemyTurn();
            round++;
        }
        return player.isAlive();
    }
    
    // 多態應用：無論是 Soldier、General 還是 Deserter，
    // 都使用同一個 takeDamage() 方法
    private void attack(Character attacker, Character defender) {
        int damage = attacker.calculateDamage(defender.getDef());
        defender.takeDamage(damage);  // 多態調用
    }
}
```

### 戰鬥流程（展示多態）

```
開始戰鬥(玩家 vs 敵人)
    ↓
玩家行動
    ├─ 攻擊：player.calculateDamage(enemy.def) + enemy.takeDamage()
    ├─ 防禦：設置防禦標誌
    └─ 逃跑：嘗試離開戰鬥
    ↓
敵人行動（多態：不同敵人類型有不同 AI）
    ├─ Soldier：直接攻擊
    ├─ General：30% 防禦 + 70% 攻擊
    └─ Deserter：100% 逃跑
    ↓
檢查存活（多態：調用 character.isAlive()）
    ├─ 玩家勝利 → 返回 true
    ├─ 敵人勝利 → 返回 false
    └─ 繼續戰鬥 → 回到玩家行動
```

### 學習重點

- **依賴注入的好處？** 讓 Battle 不需要自己創建 Player 和 Enemy，便於測試和複用
- **多態在戰鬥中的應用？** 無論敵人是什麼類型，都能通過 `Character` 接口統一操作
- **單一責任原則？** Battle 只處理戰鬥，不管遊戲全局、事件系統

---

## 🎲 events 包 - 使用的 OOP 概念

### 關鍵概念應用

| OOP 概念 | 具體應用 |
|---------|---------|
| **單一責任** | EventManager 只負責管理事件觸發邏輯 |
| **依賴** | EventManager 接收 `Player` 對象來判斷事件觸發條件 |
| **方法組織** | 不同事件有各自的方法（戰場廢墟、投降敵軍、教會審判） |
| **條件判斷** | 根據玩家狀態（honor、warPoint、level）觸發不同事件 |
| **狀態驅動** | 事件觸發完全取決於玩家當前的數據狀態 |

### 代碼示例

```java
class EventManager {
    // 依賴：接收 Player 對象
    void tryTriggerRandomEvent(Player player) {
        double random = Math.random();
        
        // 根據玩家狀態判斷事件
        // 戰場廢墟事件：30% 機率，任何時間都能觸發
        if (random < 0.30) {
            battlefieldRuinsEvent(player);
            return;
        }
        
        // 投降敵軍事件：20% 機率，需要 warPoint >= 50
        if (random < 0.50 && player.getWarPoint() >= 50) {
            surrenderedEnemyEvent(player);
            return;
        }
        
        // 教會審判事件：15% 機率，需要 honor <= -30
        if (random < 0.65 && player.getHonor() <= -30) {
            churchTrialEvent(player);
            return;
        }
    }
    
    // 固定事件：檢查條件是否滿足
    void checkFixedEvents(Player player) {
        // 最後戰役：warPoint >= 150 時必定觸發
        if (player.getWarPoint() >= 150) {
            finalBattleEvent(player);
        }
    }
    
    // 單一責任：每個事件方法只處理該事件邏輯
    private void surrenderedEnemyEvent(Player player) {
        System.out.println("你遇到了一名投降的士兵...");
        // 玩家選擇：執行或放過
        // 結果：改變 honor 和 warPoint
    }
}
```

### 事件觸發決策樹

```
tryTriggerRandomEvent(player)
    ├─ 檢查 player.getWarPoint()
    ├─ 檢查 player.getHonor()
    ├─ 檢查 player.getLevel()
    └─ 根據條件觸發相應事件

條件示例：
    - warPoint >= 150 → 最後戰役（100%）
    - honor <= -30 → 教會審判（15%）
    - warPoint >= 50 → 投降敵軍（20%）
```

### 學習重點

- **為什麼單一責任？** EventManager 只管事件，不涉及戰鬥、角色屬性計算
- **為什麼依賴 Player？** 需要讀取玩家狀態（honor、warPoint）來判斷事件
- **狀態驅動設計？** 事件完全由玩家數據決定，邏輯清晰易懂

---

## 🎮 game 包 - 使用的 OOP 概念

### 關鍵概念應用

| OOP 概念 | 具體應用 |
|---------|---------|
| **組合** | Game 包含 Player、Battle、EventManager、Mercenary、WanderingMonk |
| **聚合** | Game 使用多個對象協作完成遊戲流程 |
| **依賴注入** | 向其他類傳遞 Player 對象 |
| **多態** | 通過 `Character` 引用調用不同角色的方法 |
| **狀態管理** | `gameRunning` 標誌控制遊戲循環狀態 |
| **模板方法** | `start()` 定義遊戲的標準流程 |

### 代碼示例

```java
class Game {
    private Player player;                  // 組合
    private EventManager eventManager;      // 組合
    private Mercenary mercenary;            // 組合
    private WanderingMonk wanderingMonk;   // 組合
    private boolean gameRunning = true;    // 狀態管理
    
    // 初始化：創建所有遊戲對象
    void initialize() {
        this.player = new Player("戰士");
        this.eventManager = new EventManager();
        this.mercenary = new Mercenary();
        this.wanderingMonk = new WanderingMonk();
    }
    
    // 模板方法：定義遊戲的主循環結構
    void start() {
        initialize();
        
        while (gameRunning) {
            // 顯示當前狀態
            showMenu();
            
            // 讀取玩家輸入
            int choice = getUserInput();
            
            switch (choice) {
                case 1:
                    goToBattle();           // 進行戰鬥
                    break;
                case 2:
                    interactWithNPC();      // 與 NPC 互動
                    break;
                case 3:
                    rest();                 // 休息恢復 HP
                    break;
                case 4:
                    gameRunning = false;    // 結束遊戲
                    break;
            }
            
            // 檢查遊戲狀態
            eventManager.checkFixedEvents(player);  // 依賴注入
            
            if (player.getHp() <= 0) {
                gameRunning = false;
            }
        }
        
        // 結束遊戲
        EndingManager.determineEnding(player);
    }
    
    // 戰鬥流程：展示組合和依賴注入
    private void goToBattle() {
        Enemy enemy = generateRandomEnemy();
        Battle battle = new Battle(player, enemy);  // 注入依賴
        
        if (battle.start()) {
            // 玩家勝利，更新數據
            player.gainExp(enemy.getExpReward());
            player.gainHonor(enemy.getHonorReward());
        } else {
            // 玩家失敗
            gameRunning = false;
        }
    }
    
    // NPC 互動：展示多態
    private void interactWithNPC() {
        System.out.println("1. 與傭兵交談");
        System.out.println("2. 與修道士交談");
        
        int choice = getUserInput();
        if (choice == 1) {
            mercenary.interact(player);      // 多態調用
        } else if (choice == 2) {
            wanderingMonk.interact(player);  // 多態調用
        }
    }
    
    // 清屏：保持 UI 整潔
    private void clearScreen() {
        try {
            new ProcessBuilder("clear").inheritIO().start().waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

### Game 類的組合結構

```
Game（最高層協調者）
├── Player（玩家角色）
├── EventManager（事件系統）
│   └── 調用 Player.getHonor(), getWarPoint()
├── Mercenary（傭兵 NPC）
│   └── interact(Player)
├── WanderingMonk（修道士 NPC）
│   └── interact(Player)
└── Battle（戰鬥系統）
    ├── 接收 Player 和 Enemy
    ├── 調用 Character.calculateDamage()
    └── 調用 Character.takeDamage()
```

### 遊戲主循環（展示流程協調）

```
Game.start()
    ├─ initialize()
    │   └─ 創建所有對象（Player、EventManager、NPC）
    └─ while(gameRunning)
        ├─ showMenu()
        ├─ 等待玩家輸入
        ├─ switch(choice)
        │   ├─ 1 → goToBattle()
        │   │       └─ Battle.start() → 依賴注入
        │   ├─ 2 → interactWithNPC()
        │   │       └─ NPC.interact(player) → 多態
        │   ├─ 3 → rest()
        │   └─ 4 → gameRunning = false
        ├─ eventManager.checkFixedEvents(player) → 依賴注入
        └─ if(player.hp <= 0) → gameRunning = false
    └─ EndingManager.determineEnding(player)
```

### 學習重點

- **組合與聚合的區別？** 
  - 組合：Game 創建 Player，Player 不存在於 Game 外
  - 聚合：Game 可能擁有 Battle，但 Battle 也能獨立存在
  
- **為什麼依賴注入？** 
  - Battle 不需要知道如何創建 Player 和 Enemy
  - 便於測試和複用
  
- **多態在遊戲中的應用？**
  - 不同 NPC 類型有不同的 `interact()` 實現
  - 遊戲可以統一調用，而不需要判斷 NPC 類型

- **狀態管理的重要性？**
  - `gameRunning` 控制主循環
  - 通過更改一個標誌，可以簡潔地結束遊戲

---

## 🎯 四個包的 OOP 階層關係

```
characters 包（基礎層）
    ↓
    定義所有可能的遊戲對象
    （Player、Enemy、NPC 及其子類）
    使用：抽象、繼承、封裝、多態

battle 包（邏輯層）
    ↓
    使用 characters 包定義的對象進行互動
    根據對象的方法計算戰鬥結果
    使用：依賴注入、多態、單一責任

events 包（條件層）
    ↓
    根據 characters 包對象的狀態判斷邏輯
    決定是否觸發事件
    使用：依賴、狀態驅動、單一責任

game 包（協調層）
    ↓
    組合所有上層組件
    控制整個遊戲流程
    使用：組合、聚合、依賴注入、多態、狀態管理
```

---

## 📊 OOP 概念總覽

| 概念 | 定義 | 遊戲中的應用 |
|------|------|-----------|
| **封裝** | 隱藏內部細節，通過公開接口訪問 | Character 的私有屬性通過 getter/setter 訪問 |
| **繼承** | 子類獲得父類的屬性和方法 | Enemy 繼承 Character，獲得 hp、takeDamage() 等 |
| **多態** | 同一方法名，不同對象有不同表現 | `calculateDamage()` 在不同角色有不同計算 |
| **抽象** | 定義通用接口，具體實現留給子類 | Character 和 NPC 是抽象基類 |
| **組合** | 對象包含其他對象 | Game 包含 Player、EventManager 等 |
| **依賴注入** | 對象通過參數接收依賴 | Battle 接收 Player 和 Enemy 對象 |
| **單一責任** | 一個類只負責一個功能 | EventManager 只管事件，不管戰鬥 |

---

## 💡 設計模式應用

### 1. 模板方法模式（Template Method）
```java
// Character 定義了 calculateDamage() 模板
// 子類實現具體邏輯
abstract class Character {
    abstract int calculateDamage(int enemyDef);
}
```

### 2. 工廠方法
```java
// Game 根據隨機條件生成不同敵人
Enemy enemy = generateRandomEnemy();  // 30% Soldier, 35% General, 15% Deserter
```

### 3. 依賴注入
```java
// Battle 接收依賴而不是自己創建
Battle battle = new Battle(player, enemy);
```

### 4. 狀態模式
```java
// Player 的 honor、warPoint、killCount 代表不同狀態
// 事件根據這些狀態觸發
if (player.getHonor() <= -30) {
    churchTrialEvent(player);
}
```

---

## 🚀 實踐建議

### 如何閱讀和理解代碼

1. **從 characters 包開始**
   - 理解 Character 抽象類如何定義通用接口
   - 觀察 Player 和 Enemy 如何通過繼承複用代碼
   - 注意不同敵人如何實現相同方法

2. **學習 battle 包**
   - 看 Battle 如何使用 Player 和 Enemy 對象
   - 觀察多態如何簡化代碼（不需要判斷敵人類型）
   - 理解依賴注入的優勢

3. **分析 events 包**
   - 注意事件如何根據玩家狀態觸發
   - 理解單一責任原則的實踐
   - 觀察條件判斷的邏輯組織

4. **整合理解 game 包**
   - 看 Game 如何組合所有組件
   - 理解依賴注入流經整個系統
   - 觀察多態如何應用於 NPC 互動

### 修改和擴展的考慮

- **新增敵人類型？** 創建新類 extends Enemy
- **新增 NPC 類型？** 創建新類 extends NPC
- **新增事件？** 在 EventManager 中添加新方法
- **改變遊戲流程？** 修改 Game.start() 的 switch 語句

---

## 總結

本遊戲是一個完整的 OOP 設計案例，展示了：
- ✅ 抽象和繼承如何實現代碼複用
- ✅ 多態如何簡化複雜邏輯
- ✅ 單一責任如何組織清晰的架構
- ✅ 組合和依賴注入如何協調多個對象
- ✅ 狀態驅動如何實現事件系統

通過這個遊戲，你可以深入理解 OOP 的核心價值！🎮
