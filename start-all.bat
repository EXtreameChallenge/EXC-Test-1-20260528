@echo off
chcp 65001 >nul
setlocal enabledelayedexpansion

echo.
echo ╔══════════════════════════════════════════════════════════════╗
echo ║          德莫无人车队管理系统 - 统一启动脚本                ║
echo ║          XClaw Multi-Project Launcher v1.0                  ║
echo ╚══════════════════════════════════════════════════════════════╝
echo.

set PROJECT_ROOT=%~dp0
set JAVA_BACKEND=%PROJECT_ROOT%JavaEEWeb03
set PC_FRONTEND=%PROJECT_ROOT%PC20260426
set PHONE_FRONTEND=%PROJECT_ROOT%Phone20260423
set DATA_CENTER=%PROJECT_ROOT%shared

echo [信息] 项目根目录: %PROJECT_ROOT%
echo.

echo ════════════════════════════════════════════════════════════════
echo 第一步: 检查运行环境
echo ════════════════════════════════════════════════════════════════
echo.

where java >nul 2>&1
if %errorlevel% neq 0 (
    echo [错误] 未找到 Java 环境，请先安装 JDK 8+
    pause
    exit /b 1
)
echo [√] Java 环境: 已安装

where mvn >nul 2>&1
if %errorlevel% neq 0 (
    echo [错误] 未找到 Maven，请先安装 Maven
    pause
    exit /b 1
)
echo [√] Maven 环境: 已安装

where node >nul 2>&1
if %errorlevel% neq 0 (
    echo [错误] 未找到 Node.js，请先安装 Node.js 18+
    pause
    exit /b 1
)
echo [√] Node.js 环境: 已安装

echo.
echo ════════════════════════════════════════════════════════════════
echo 第二步: 检查端口占用
echo ════════════════════════════════════════════════════════════════
echo.

netstat -ano | findstr ":8080" >nul 2>&1
if %errorlevel% equ 0 (
    echo [警告] 端口 8080 已被占用，Java后端可能无法启动
    echo        请检查是否有其他Tomcat服务在运行
)

netstat -ano | findstr ":5173" >nul 2>&1
if %errorlevel% equ 0 (
    echo [警告] 端口 5173 已被占用，PC端前端可能无法启动
)

netstat -ano | findstr ":3000" >nul 2>&1
if %errorlevel% equ 0 (
    echo [警告] 端口 3000 已被占用，手机端前端可能无法启动
)

echo [√] 端口检查完成
echo.

echo ════════════════════════════════════════════════════════════════
echo 第三步: 安装前端依赖
echo ════════════════════════════════════════════════════════════════
echo.

if not exist "%PC_FRONTEND%\node_modules" (
    echo [信息] 正在安装 PC端前端依赖...
    cd /d "%PC_FRONTEND%"
    call npm install
    if %errorlevel% neq 0 (
        echo [错误] PC端前端依赖安装失败
        pause
        exit /b 1
    )
    echo [√] PC端前端依赖安装完成
) else (
    echo [√] PC端前端依赖已存在
)

if not exist "%PHONE_FRONTEND%\node_modules" (
    echo [信息] 正在安装 手机端前端依赖...
    cd /d "%PHONE_FRONTEND%"
    call npm install
    if %errorlevel% neq 0 (
        echo [错误] 手机端前端依赖安装失败
        pause
        exit /b 1
    )
    echo [√] 手机端前端依赖安装完成
) else (
    echo [√] 手机端前端依赖已存在
)

echo.
echo ════════════════════════════════════════════════════════════════
echo 第四步: 编译 Java 后端项目
echo ════════════════════════════════════════════════════════════════
echo.

cd /d "%JAVA_BACKEND%"
echo [信息] 正在编译 Java 后端项目...
call mvn clean package -DskipTests
if %errorlevel% neq 0 (
    echo [错误] Java 后端编译失败
    pause
    exit /b 1
)
echo [√] Java 后端编译完成

echo.
echo ════════════════════════════════════════════════════════════════
echo 第五步: 启动所有服务
echo ════════════════════════════════════════════════════════════════
echo.

echo [信息] 正在启动 Java 后端 (需要配置Tomcat)...
echo        请手动将 %JAVA_BACKEND%\target\JavaEEWeb03.war 部署到Tomcat
echo        或者使用 IDEA 运行项目

echo.
echo [信息] 正在启动 数据中心服务...
start "Data Center Server" cmd /k "cd /d "%DATA_CENTER%" && npm install && node data-center.js"
timeout /t 3 >nul

echo.
echo [信息] 正在启动 手机端前端服务器...
start "Phone Frontend Server" cmd /k "cd /d "%PHONE_FRONTEND%" && node server-unified.js"
timeout /t 2 >nul

echo [信息] 正在启动 PC端前端开发服务器...
start "PC Frontend Server" cmd /k "cd /d "%PC_FRONTEND%" && npm run dev"
timeout /t 2 >nul

echo.
echo ╔══════════════════════════════════════════════════════════════╗
echo ║                    启动完成！                                ║
echo ╠══════════════════════════════════════════════════════════════╣
echo ║  数据中心:   http://localhost:3100                          ║
echo ║  手机端前端: http://localhost:3000                          ║
echo ║  PC端前端:   http://localhost:5173                          ║
echo ║  Java后端:   http://localhost:8080/JavaEEWeb03              ║
echo ║                                                              ║
echo ║  注意: Java后端需要手动启动Tomcat或使用IDEA运行             ║
echo ╚══════════════════════════════════════════════════════════════╝
echo.

pause
