@echo off
chcp 65001 >nul

echo.
echo ╔══════════════════════════════════════════════════════════════╗
echo ║          德莫无人车队管理系统 - 停止所有服务                ║
echo ╚══════════════════════════════════════════════════════════════╝
echo.

echo [信息] 正在停止所有服务...
echo.

echo [信息] 停止 Node.js 进程 (手机端前端)...
taskkill /F /IM node.exe /FI "WINDOWTITLE eq Phone Frontend Server*" 2>nul
if %errorlevel% equ 0 (
    echo [√] 手机端前端已停止
) else (
    echo [!] 未找到运行中的手机端前端
)

echo.
echo [信息] 停止 Vite 进程 (PC端前端)...
taskkill /F /IM node.exe /FI "WINDOWTITLE eq PC Frontend Server*" 2>nul
if %errorlevel% equ 0 (
    echo [√] PC端前端已停止
) else (
    echo [!] 未找到运行中的PC端前端
)

echo.
echo [信息] Java后端需要手动停止 (Tomcat或IDEA)
echo.

echo [√] 所有前端服务已停止
echo.
pause
