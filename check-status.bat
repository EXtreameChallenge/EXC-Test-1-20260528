@echo off
chcp 65001 >nul

echo.
echo ╔══════════════════════════════════════════════════════════════╗
echo ║          德莫无人车队管理系统 - 运行状态监控                ║
echo ╚══════════════════════════════════════════════════════════════╝
echo.

echo ════════════════════════════════════════════════════════════════
echo 端口状态检查
echo ════════════════════════════════════════════════════════════════
echo.

echo [Java 后端 - 端口 8080]
netstat -ano | findstr ":8080" | findstr "LISTENING"
if %errorlevel% equ 0 (
    echo     状态: ✓ 运行中
) else (
    echo     状态: ✗ 未运行
)

echo.
echo [PC端前端 - 端口 5173]
netstat -ano | findstr ":5173" | findstr "LISTENING"
if %errorlevel% equ 0 (
    echo     状态: ✓ 运行中
) else (
    echo     状态: ✗ 未运行
)

echo.
echo [手机端前端 - 端口 3000]
netstat -ano | findstr ":3000" | findstr "LISTENING"
if %errorlevel% equ 0 (
    echo     状态: ✓ 运行中
) else (
    echo     状态: ✗ 未运行
)

echo.
echo ════════════════════════════════════════════════════════════════
echo API 健康检查
echo ════════════════════════════════════════════════════════════════
echo.

echo [Java 后端 API]
curl -s http://localhost:8080/JavaEEWeb03/api/login?username=test^&password=test >nul 2>&1
if %errorlevel% equ 0 (
    echo     状态: ✓ 可访问
) else (
    echo     状态: ✗ 无法访问
)

echo.
echo [手机端 Mock API]
curl -s http://localhost:3000/api/health >nul 2>&1
if %errorlevel% equ 0 (
    echo     状态: ✓ 可访问
) else (
    echo     状态: ✗ 无法访问
)

echo.
echo ════════════════════════════════════════════════════════════════
echo 数据库连接检查
echo ════════════════════════════════════════════════════════════════
echo.

echo [MySQL 数据库 - 端口 3306]
netstat -ano | findstr ":3306" | findstr "LISTENING"
if %errorlevel% equ 0 (
    echo     状态: ✓ 运行中
) else (
    echo     状态: ✗ 未运行 (请启动MySQL服务)
)

echo.
echo ════════════════════════════════════════════════════════════════
echo 访问地址
echo ════════════════════════════════════════════════════════════════
echo.
echo   手机端前端: http://localhost:3000
echo   PC端前端:   http://localhost:5173
echo   Java后端:   http://localhost:8080/JavaEEWeb03
echo.

pause
