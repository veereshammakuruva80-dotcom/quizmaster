# QuizMaster – AI-Powered Smart Quiz & Personalized Learning Platform

A production-grade, responsive, full-stack web application designed for academic evaluation, B.Tech final-year viva, and real-world deployment.

---

## 🌟 Key Features

1. **Secure Authentication & Role-Based Access Control**
   - User Registration with academic profile (education, skills, interests, bio).
   - Dynamic Role Selection: Learner (`ROLE_USER`) & Administrator (`ROLE_ADMIN`).
   - BCrypt password encryption & stateless JWT session tokens.
   - Forgot Password & Reset Token verification workflow.
   - Profile management with dynamic avatar image configuration.

2. **Real-Time Interactive Quiz Engine**
   - Question delivery: One question at a time with instant previous/next navigation.
   - Live Countdown Timer with visual urgency warning when under 2 minutes.
   - Question Palette with color-coded badges: Answered, Marked for Review, Unvisited, Current.
   - Question types: Multiple Choice (Single), True/False, Multiple-Answer Checkboxes.
   - Auto-submit when time expires and anti-tamper lock against duplicate submissions.

3. **In-Depth Scorecards & Assessment Reports**
   - Immediate evaluation with score, total marks, percentage, accuracy, and time spent.
   - Performance levels: `OUTSTANDING`, `PROFICIENT`, `AVERAGE`, `NEEDS_IMPROVEMENT`.
   - Comprehensive question-by-question review with user choices, correct answers, and conceptual explanations.
   - Printable & downloadable verified HTML/PDF certificate reports.

4. **Python AI-Based Personalized Recommendation Engine**
   - Multi-factor proficiency analysis based on topic accuracy, completion time, and mistake frequency.
   - Classifies subjects into **Strong (Mastered)** and **Weak (Critical / Needs Improvement)**.
   - Generates:
     - Curated quizzes targeting identified weak subjects.
     - Actionable revision notes with conceptual guidance.
     - Foundation & Easier practice questions.
     - Advanced challenge quizzes for subject mastery.
     - Personalized step-by-step learning roadmap.

5. **Gamification & Leaderboard**
   - Automated badge unlocks: *First Quiz*, *Quiz Master*, *10 Quizzes Completed*, *90% Accuracy*, *7-Day Streak*, *Perfect Score*, *Programming Expert*, *Database Expert*.
   - Filterable Leaderboard: All-Time, Monthly, Weekly, Daily, and Category-wise.
   - Daily learning streak tracking.

6. **Full-Featured Administrator Control Center**
   - Platform Telemetry: Registered users, quizzes, questions, completion volume, platform average score.
   - User Management: Search users, toggle account status (Active/Suspended), toggle Administrator privileges.
   - Category CRUD: Add, edit, delete categories with theme colors and icons.
   - Quiz CRUD: Configure title, category, difficulty, duration, marks, and pass criteria.
   - Question Bank Management: Create questions, options, keys, and conceptual explanations.
   - Assessment Logs & CSV Export: View and export platform-wide attempt audits.

---

## 🏗️ Technology Stack

| Layer | Technology | Details |
|---|---|---|
| **Frontend** | HTML5, CSS3, JavaScript (ES6+), Bootstrap 5.3, Chart.js 4.x, SweetAlert2 | Responsive, mobile-ready glassmorphic UI |
| **Backend** | Java 21 / 25, Spring Boot 3.3.5, Spring Security, Spring Data JPA | RESTful APIs, JWT Auth Filter, BCrypt |
| **Database** | MySQL / MariaDB (Port 3306) | 14 relational tables, foreign key constraints |
| **AI / ML** | Python 3.14, Flask, NumPy, Scikit-learn (Port 5001) | Adaptive Topic Weighting & Deficiency Modeling |
| **Build Tools** | Apache Maven 3.9.9 (Portable included) | Java compilation and dependency packaging |

---

## 🚀 Quick Start (Running the Application)

### Option 1: One-Click Startup Script (Windows)
Double-click `start.bat` located in the root directory:
```cmd
start.bat
```
This automatically starts:
1. MySQL database daemon
2. Python AI Recommendation microservice (`http://127.0.0.1:5001`)
3. Spring Boot backend (`http://localhost:8085`)
4. Opens your default web browser to `http://localhost:8085`

---

### Option 2: Manual Step-by-Step Launch

#### 1. Start MySQL Server
```powershell
& "C:\xampp2\mysql\bin\mysqld.exe" --defaults-file="C:\xampp2\mysql\bin\my.ini" --console
```

#### 2. Start the Python AI Microservice
```powershell
cd quizmaster-ai
python app.py
```
*Health check:* `http://127.0.0.1:5001/health`

#### 3. Start the Spring Boot Web Application
```powershell
cd quizmaster-backend
..\tools\apache-maven-3.9.9\bin\mvn.cmd spring-boot:run
```
*Web Application:* `http://localhost:8085`

---

## 🌐 Application Navigation & Page Sitemap

| Page | URL Path | Description |
|---|---|---|
| **Home** | `/index.html` | Hero banner, live stats, popular categories, and features |
| **Register** | `/register.html` | Student/Admin registration with education, skills, interests |
| **Login** | `/login.html` | Secure JWT login with validation |
| **Forgot Password** | `/forgot-password.html` | Password recovery code generator |
| **Reset Password** | `/reset-password.html` | Token-verified new password setting |
| **User Dashboard** | `/dashboard.html` | Progress cards, streaks, Chart.js graphs, weak/strong subjects |
| **Categories** | `/categories.html` | 16 curated subject domains with live search |
| **Quiz Catalog** | `/quizzes.html` | Filter quizzes by category, difficulty, or keyword |
| **Quiz Instructions** | `/quiz-instructions.html?id=:id` | Exam briefing, timing rules, and syllabus preview |
| **Quiz Attempt** | `/quiz-attempt.html?quizId=:id` | Active timed exam portal with question palette and review |
| **Quiz Result** | `/quiz-result.html?attemptId=:id` | Scorecard, accuracy, pass/fail status, and report link |
| **Answer Review** | `/answer-review.html?attemptId=:id` | Question explanations with user vs correct choices |
| **Analytics** | `/analytics.html` | Chart.js score trajectory, subject proficiency, topic breakdown |
| **AI Recommendations** | `/recommendations.html` | AI roadmap, topics to revise, easier practice, advanced tests |
| **Leaderboard** | `/leaderboard.html` | Global ranks, streaks, all-time/monthly/weekly filters |
| **Achievements** | `/achievements.html` | Badges grid showing unlocked and locked milestones |
| **Profile** | `/profile.html` | Personal info, avatar URL, educational interests |
| **Admin Dashboard** | `/admin-dashboard.html` | Platform counters, telemetry, quick shortcuts |
| **Manage Users** | `/admin-users.html` | Search users, toggle active status, assign Admin roles |
| **Manage Categories** | `/admin-categories.html` | Add, edit, or delete syllabus categories |
| **Manage Quizzes** | `/admin-quizzes.html` | Configure quizzes, duration, difficulty, and passing scores |
| **Manage Questions** | `/admin-questions.html` | Add/edit questions, 4 options, keys, and explanations |
| **Audit Reports** | `/admin-reports.html` | Platform-wide attempt logs with CSV export |

---

## 🗄️ Database Architecture (`quizmaster_db`)

The platform uses 14 relational tables in MySQL with primary and foreign key constraints:

1. `users`: Stores candidate identity, BCrypt passwords, education, skills, interests, and avatars.
2. `roles`: Role definitions (`ROLE_USER`, `ROLE_ADMIN`).
3. `user_roles`: Many-to-many relationship between users and roles.
4. `categories`: 16 core domains (Computer Science, Java, Python, C++, DBMS, Networks, OS, AI, ML, etc.).
5. `quizzes`: Quiz configurations with difficulty, duration, marks, and passing criteria.
6. `questions`: Questions with type (`MCQ`, `TRUE_FALSE`, `MULTIPLE_ANSWER`), topic, marks, and explanation.
7. `question_options`: Four choices per question with correct answer flag.
8. `quiz_attempts`: Candidate exam sessions, start/end timestamps, scores, and status.
9. `user_answers`: Granular question response records with selected options and marks.
10. `results`: Official aggregated scorecards and performance level rankings.
11. `achievements`: Gamification badge criteria (Streak, Accuracy, Count, Category Master).
12. `user_achievements`: Badges unlocked by each candidate.
13. `recommendations`: Historical recommendation records cached per user.
14. `user_progress`: Aggregated learner metrics, streaks, and total points.

---

## 🧠 AI Recommendation Algorithm Details

The Python AI module (`quizmaster-ai/app.py`) operates via REST API:

$$\text{Accuracy}_{\text{topic}} = \frac{\text{Correct Answers}_{\text{topic}}}{\text{Total Questions Attempted}_{\text{topic}}} \times 100$$

1. **Deficiency Detection**:
   - If $\text{Accuracy} < 65\%$, the topic is flagged for mandatory revision with targeted study tips.
   - If $\text{Accuracy} \ge 75\%$, the topic is marked as mastered.
2. **Adaptive Scoring**:
   - Quizzes covering weak subjects receive a $+50.0$ point prioritization boost.
   - Quizzes matching candidate profile interests receive a $+35.0$ point boost.
   - Beginner/Intermediate difficulties are prioritized for weak topics, while Advanced difficulties are selected for mastered topics.
3. **High-Availability Fallback**:
   - If the Python microservice is ever stopped, the Spring Boot backend automatically triggers an internal algorithmic fallback to guarantee 100% platform uptime.
"# quizewebsite" 
