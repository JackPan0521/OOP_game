#!/bin/bash
cd "$(dirname "$0")"

echo "════════════════════════════════════"
echo "正在編譯 Java 遊戲..."
echo "════════════════════════════════════"
echo ""

javac -d bin src/characters/*.java src/battle/*.java src/events/*.java src/game/*.java

if [ $? -eq 0 ]; then
    echo ""
    echo "✅ 編譯完成！"
    echo ""
    echo "編譯的Java文件:"
    ls -la src/*/*.java | wc -l
    echo "個文件"
    echo ""
    echo "生成的.class文件:"
    ls -la bin/*/*.class 2>/dev/null | wc -l
    echo "個文件"
    echo ""
    echo "════════════════════════════════════"
    echo "準備開始遊戲..."
    echo "════════════════════════════════════"
    echo ""
    
    sleep 1
    java -cp bin game.Game
else
    echo ""
    echo "❌ 編譯失敗！"
    echo "════════════════════════════════════"
    exit 1
fi
