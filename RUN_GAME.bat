@echo off
chcp 65001 >nul
cd /d "%~dp0"

echo ════════════════════════════════════
echo 正在編譯 Java 遊戲...
echo ════════════════════════════════════
echo.

javac -d bin src/characters/*.java src/battle/*.java src/events/*.java src/game/*.java

if errorlevel 1 (
    echo.
    echo ❌ 編譯失敗！
    echo ════════════════════════════════════
    pause
    exit /b 1
)

echo.
echo ✅ 編譯完成！
echo.
echo ════════════════════════════════════
echo 準備開始遊戲...
echo ════════════════════════════════════
echo.

timeout /t 1 /nobreak

java -cp bin game.Game
pause
