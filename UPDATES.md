# 🎮 遊戲更新說明

## 更新內容：清屏功能

### 新增特性 ✨

已為遊戲添加自動清屏功能，使遊戲畫面更加乾淨整潔：

#### 📍 清屏觸發位置

1. **每日冒險開始時** - 進入新的一天時清屏
2. **做出選擇後** - 選擇行動後立即清屏
3. **戰鬥開始時** - 進入戰鬥前清屏
4. **NPC互動時** - 與NPC交互後清屏
5. **休息完成時** - 休息後清屏並等待確認
6. **遊戲結束時** - 顯示結局前清屏

#### 🎮 使用體驗改進

- ✅ 不再堆積舊記錄，每一步都有新的乾淨界面
- ✅ 所有操作後可按 **Enter** 鍵繼續遊戲
- ✅ 更清晰的視覺層級，易於閱讀
- ✅ 支持 macOS 和 Linux 系統

### 技術實現

```java
// 清屏方法
private void clearScreen() {
    try {
        // 對於 macOS 和 Linux
        new ProcessBuilder("clear").inheritIO().start().waitFor();
    } catch (IOException | InterruptedException e) {
        // 備用方案：ANSI 轉義序列
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
```

### 運行方式

#### 方式1：直接運行編譯好的遊戲
```bash
cd /Users/jackpan/development/OOP_game
java -cp bin game.Game
```

#### 方式2：使用運行腳本
```bash
cd /Users/jackpan/development/OOP_game
./play.sh
```

### 遊戲流程示例

```
【初始化】
   ↓ [輸入玩家名字]
   ↓ [清屏]
【第1天 - 選擇菜單】
   ↓ [選擇行動]
   ↓ [清屏]
【戰鬥/互動/休息等】
   ↓ [操作完成]
   ↓ [按Enter繼續]
   ↓ [清屏]
【回到菜單循環】
```

### 文件變更

- `src/game/Game.java` - 添加了 `clearScreen()` 方法和相應的清屏調用

---

祝你享受更清爽的遊戲體驗！🎮✨
