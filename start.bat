@echo off
title QuizMaster Platform Launcher
echo ======================================================================
echo    QuizMaster - AI-Powered Smart Quiz & Personalized Learning
echo ======================================================================
echo.

echo [1/3] Starting MySQL Database Server...
start "QuizMaster - MySQL Server" "C:\xampp2\mysql\bin\mysqld.exe" --defaults-file="C:\xampp2\mysql\bin\my.ini" --console

timeout /t 3 /nobreak >nul

echo [2/3] Starting Python AI Recommendation Microservice (Port 5001)...
start "QuizMaster - AI Engine" cmd /k "cd /d %~dp0quizmaster-ai && python app.py"

timeout /t 3 /nobreak >nul

echo [3/3] Starting Spring Boot Backend (Port 8085)...
start "QuizMaster - Spring Boot Web Server" cmd /k "cd /d %~dp0quizmaster-backend && ..\tools\apache-maven-3.9.9\bin\mvn.cmd spring-boot:run"

echo.
echo Waiting for services to initialize...
timeout /t 10 /nobreak >nul

echo Launching QuizMaster in your browser...
start http://localhost:8085

echo.
echo ======================================================================
echo    QuizMaster is now RUNNING!
echo    Web Portal:       http://localhost:8085
echo    AI Microservice:  http://127.0.0.1:5001/health
echo    MySQL DB:         localhost:3306 (quizmaster_db)
echo ======================================================================
pause
