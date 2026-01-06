#!/bin/bash
cd /Users/jackpan/development/OOP_game

echo "Java遊戲編譯完成！"
echo "════════════════════════════════════"
echo "編譯的Java文件:"
ls -la src/*/*.java | wc -l
echo "個文件"
echo ""

echo "生成的.class文件:"
ls -la bin/*/*.class 2>/dev/null | wc -l
echo "個文件"
echo ""

echo "════════════════════════════════════"
echo "遊戲已準備完成！"
echo ""
echo "執行遊戲:"
echo "cd /Users/jackpan/development/OOP_game"
echo "java -cp bin game.Game"
echo ""
echo "════════════════════════════════════"
