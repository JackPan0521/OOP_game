# UML 類圖說明文檔

## 📊 OOP 遊戲架構設計

本遊戲採用物件導向設計（OOP），包含以下四個主要包（Package）：

---

## 🎯 包結構

### 1️⃣ **characters 包** - 角色系統

#### 類別層次結構
```
Character (abstract)
├── Player
└── Enemy (abstract)
    ├── Soldier
    ├── General
    └── Deserter

NPC (abstract)
├── Mercenary
└── WanderingMonk
```

#### **Character 類**（抽象基類）
- **責任**：定義所有遊戲角色的基本屬性和行為
- **主要屬性**：
  - `name`: 角色名稱
  - `hp`, `maxHp`: 生命值
  - `atk`: 攻擊力
  - `def`: 防禦力
- **主要方法**：
  - `calculateDamage()`: 計算傷害
  - `takeDamage()`: 承受傷害
  - `heal()`: 治療
  - `isAlive()`: 判斷是否存活

#### **Player 類**（玩家角色）
- **繼承**：extends Character
- **額外屬性**：
  - `exp`: 經驗值
  - `level`: 等級
  - `honor`: 榮譽值
  - `warPoint`: 戰功
  - `killCount`: 擊殺數
- **特殊方法**：
  - `gainExp()`: 獲得經驗並檢查升級
  - `gainHonor()`: 改變榮譽值
  - `gainWarPoint()`: 改變戰功
  - `showStatus()`: 顯示玩家狀態

#### **Enemy 類**（敵人基類）
- **繼承**：extends Character
- **額外屬性**：
  - `expReward`: 獲得的經驗值
  - `honorReward`: 獲得的榮譽值
  - `warPointReward`: 獲得的戰功
- **子類**：
  - **Soldier（小兵）**：HP 30，ATK 8，DEF 3（最弱敵人）
  - **General（將領）**：HP 80，ATK 18，DEF 8（強力敵人）
  - **Deserter（逃兵）**：HP 40，ATK 10，DEF 5（可選擇解放）

#### **NPC 類**（非玩家角色）
- **責任**：定義 NPC 的基本框架
- **子類**：
  - **Mercenary（傭兵）**：
    - 根據玩家榮譽值判斷是否背叛
    - `interact()`: 與玩家互動
  
  - **WanderingMonk（流浪修道士）**：
    - `heal()`: 治療玩家
    - `predictFate()`: 預言結局
    - `interact()`: 與玩家互動

---

### 2️⃣ **battle 包** - 戰鬥系統

#### **Battle 類**
- **責任**：管理回合制戰鬥的所有邏輯
- **主要屬性**：
  - `player`: 玩家角色
  - `enemy`: 敵人角色
  - `round`: 當前回合數
  - `lastRoundDefended`: 上回合是否進行了防禦
- **主要方法**：
  - `start()`: 開始戰鬥，返回是否勝利
  - `playerTurn()`: 玩家行動（攻擊/防禦/逃跑）
  - `enemyTurn()`: 敵人行動（AI 邏輯）
  - `attack()`: 執行攻擊
  - `defend()`: 執行防禦（傷害 -30%）
  - `attemptEscape()`: 嘗試逃跑（50% 成功率）

#### **戰鬥流程**
```
玩家發起戰鬥
    ↓
顯示戰鬥狀態（HP、ATK、DEF）
    ↓
玩家選擇行動
    ├─ 攻擊
    ├─ 防禦
    └─ 逃跑
    ↓
敵人進行反擊
    ↓
檢查是否有人死亡
    ├─ 玩家勝利 → 獲得獎勵
    ├─ 敵人勝利 → 遊戲結束
    └─ 繼續戰鬥
```

---

### 3️⃣ **events 包** - 事件系統

#### **EventManager 類**
- **責任**：管理遊戲中的所有事件
- **主要方法**：
  - `battlefieldRuinsEvent()`: 戰場廢墟事件
  - `surrenderedEnemyEvent()`: 投降敵軍事件
  - `churchTrialEvent()`: 教會審判事件
  - `finalBattleEvent()`: 最後戰役事件
  - `tryTriggerRandomEvent()`: 嘗試觸發隨機事件
  - `checkFixedEvents()`: 檢查固定事件

#### **事件觸發條件**

| 事件 | 觸發條件 | 機率 |
|------|---------|------|
| 戰場廢墟 | 任何戰鬥後 | 30% |
| 投降敵軍 | WarPoint >= 50 | 20% |
| 教會審判 | Honor <= -30 | 15% |
| 最後戰役 | WarPoint >= 150 | 100%（固定） |

---

### 4️⃣ **game 包** - 遊戲核心

#### **Game 類**
- **責任**：控制整個遊戲流程
- **主要屬性**：
  - `player`: 玩家角色
  - `eventManager`: 事件管理器
  - `mercenary`: 傭兵 NPC
  - `wanderingMonk`: 修道士 NPC
  - `gameRunning`: 遊戲是否繼續
- **主要方法**：
  - `initialize()`: 初始化遊戲
  - `start()`: 開始遊戲主循環
  - `clearScreen()`: 清屏（保持界面整潔）
  - `goToBattle()`: 進行戰鬥
  - `interactWithNPC()`: 與 NPC 互動
  - `rest()`: 休息恢復 HP
  - `endGame()`: 結束遊戲並顯示結局

#### **EndingManager 類**
- **責任**：判定和顯示遊戲結局
- **結局類型**：
  - **✨ 榮譽騎士**：WarPoint >= 150 & 榮譽 >= 50 & 擊殺數 < 30
  - **⚔️ 戰爭倖存者**：WarPoint >= 150 & 榮譽 -20~49
  - **🩸 嗜血屠夫**：WarPoint >= 150 & 榮譽 <= -50 & 擊殺數 >= 50
  - **☠️ 戰死沙場**：HP <= 0

#### **Ending 枚舉**
定義遊戲的四種可能結局

---

## 🔗 類別關係

### 繼承關係（Inheritance）
```
Character → Player
Character → Enemy → Soldier, General, Deserter
NPC → Mercenary, WanderingMonk
```

### 依賴關係（Dependency）
```
Game → Player（使用）
Game → Battle（使用）
Game → EventManager（使用）
Battle → Player, Enemy（使用）
EventManager → Player（使用）
EndingManager → Player（使用）
```

### 聚合關係（Aggregation）
```
Game 包含：
  - 一個 Player
  - 一個 EventManager
  - 一個 Mercenary
  - 一個 WanderingMonk
```

---

## 📈 設計模式

### 1. **模板方法模式**（Template Method）
- **Character** 和 **NPC** 的抽象方法定義了子類必須實現的行為

### 2. **工廠方法**（Factory Method）
- `Game.generateRandomEnemy()` 根據條件生成不同類型的敵人

### 3. **狀態模式**（State）
- `Player` 的等級、經驗等代表遊戲狀態
- `lastRoundDefended` 代表戰鬥狀態

### 4. **策略模式**（Strategy）
- 不同的敵人類型有不同的屬性和行為

---

## 💾 如何使用 UML 文件

### 線上查看
1. 訪問 **PlantUML Online Editor**：https://www.plantuml.com/plantuml/uml/
2. 複製 `OOP_Game_UML.puml` 的內容
3. 粘貼到編輯器中查看圖表

### 本地生成圖片
```bash
# 安裝 PlantUML（需要 Java）
brew install plantuml

# 生成 PNG 圖片
plantuml OOP_Game_UML.puml

# 生成 SVG（向量圖）
plantuml -tsvg OOP_Game_UML.puml
```

### VS Code 擴展
1. 安装 PlantUML 擴展：`plantuml.plantuml`
2. 右鍵點擊 `.puml` 文件
3. 選擇「Preview」查看圖表

---

## 📝 OOP 概念應用

| OOP 概念 | 實現方式 |
|---------|---------|
| **封裝** | Character 的私有屬性 `#` |
| **繼承** | Player/Enemy 繼承 Character |
| **多態** | NPC 的 `interact()` 抽象方法 |
| **抽象** | Character 和 NPC 的抽象類 |
| **組合** | Game 組合 Player、Battle 等 |

---

希望這份 UML 檔案能幫助您理解遊戲的整體架構！🎮

