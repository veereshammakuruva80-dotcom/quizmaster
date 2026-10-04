import sys
import logging
from flask import Flask, request, jsonify
import numpy as np

# Configure logging
logging.basicConfig(level=logging.INFO, format="%(asctime)s [%(levelname)s] %(message)s")
logger = logging.getLogger("QuizMasterAI")

app = Flask(__name__)

@app.route("/health", methods=["GET"])
def health():
    return jsonify({
        "status": "UP",
        "service": "QuizMaster AI Recommendation Engine",
        "algorithm": "Multi-Factor Proficiency Vector + Adaptive Topic Weighting",
        "version": "1.0.0"
    })

@app.route("/api/recommend", methods=["POST"])
def recommend():
    try:
        data = request.get_json(force=True) or {}
        user_id = data.get("userId")
        interests = [i.strip().lower() for i in data.get("userInterests", []) if i.strip()]
        skills = [s.strip().lower() for s in data.get("userSkills", []) if s.strip()]
        attempts = data.get("attemptHistory", [])
        available_quizzes = data.get("availableQuizzes", [])

        logger.info(f"Generating AI recommendations for user {user_id} with {len(attempts)} attempts")

        # 1. Subject and Topic Performance Analysis
        subject_stats = {} # category -> {scores: [], total_q: 0, correct_q: 0, times: []}
        topic_stats = {}   # topic -> {attempts: 0, incorrect: 0, correct: 0}
        attempted_quiz_ids = set()

        for att in attempts:
            quiz_id = att.get("quizId")
            if quiz_id:
                attempted_quiz_ids.add(quiz_id)

            category = att.get("category", "General").strip()
            score_pct = float(att.get("percentage", 0.0))
            accuracy = float(att.get("accuracy", score_pct))
            time_taken = float(att.get("timeTakenSeconds", 0))

            if category not in subject_stats:
                subject_stats[category] = {"scores": [], "accuracies": [], "times": []}
            subject_stats[category]["scores"].append(score_pct)
            subject_stats[category]["accuracies"].append(accuracy)
            subject_stats[category]["times"].append(time_taken)

            # Incorrect topics
            for inc_topic in att.get("incorrectTopics", []):
                t = inc_topic.strip()
                if t:
                    if t not in topic_stats:
                        topic_stats[t] = {"correct": 0, "incorrect": 0}
                    topic_stats[t]["incorrect"] += 1

            # Correct topics
            for cor_topic in att.get("correctTopics", []):
                t = cor_topic.strip()
                if t:
                    if t not in topic_stats:
                        topic_stats[t] = {"correct": 0, "incorrect": 0}
                    topic_stats[t]["correct"] += 1

        # Classify Strong & Weak Subjects
        strong_subjects = []
        weak_subjects = []

        for category, stats in subject_stats.items():
            avg_acc = float(np.mean(stats["accuracies"])) if stats["accuracies"] else 0.0
            if avg_acc >= 70.0:
                strong_subjects.append({
                    "subject": category,
                    "avgScore": round(avg_acc, 1),
                    "status": "Mastered" if avg_acc >= 85.0 else "Strong"
                })
            else:
                weak_subjects.append({
                    "subject": category,
                    "avgScore": round(avg_acc, 1),
                    "status": "Critical" if avg_acc < 50.0 else "Needs Improvement"
                })

        # Generate Topics to Revise
        topics_to_revise = []
        for topic, counts in topic_stats.items():
            total = counts["correct"] + counts["incorrect"]
            if total > 0:
                acc = (counts["correct"] / total) * 100.0
                if acc < 65.0 or counts["incorrect"] > counts["correct"]:
                    topics_to_revise.append({
                        "topic": topic,
                        "accuracy": round(acc, 1),
                        "incorrectCount": counts["incorrect"],
                        "tip": f"Review core principles and syntax of {topic}. Focus on common edge cases and practical implementations."
                    })

        # Sort topics by highest mistake count
        topics_to_revise.sort(key=lambda x: (x["accuracy"], -x["incorrectCount"]))

        # 2. Candidate Quiz Scoring & Ranking
        # Recommendation reasons & scoring formula
        scored_quizzes = []

        for quiz in available_quizzes:
            q_id = quiz.get("id")
            q_cat = quiz.get("categoryName", quiz.get("category", "")).strip()
            q_diff = quiz.get("difficulty", "INTERMEDIATE").upper()
            q_title = quiz.get("title", "")
            q_desc = quiz.get("description", "")
            is_attempted = q_id in attempted_quiz_ids

            score = 0.0
            rec_type = "GENERAL_RECOMMENDED"
            reason = "Recommended for broad subject enrichment"

            # Check if quiz category is a weak subject
            is_weak = any(w["subject"].lower() == q_cat.lower() for w in weak_subjects)
            # Check if quiz category is a strong subject
            is_strong = any(s["subject"].lower() == q_cat.lower() for s in strong_subjects)
            # Check user interests / skills match
            matches_interest = any(i in q_cat.lower() or i in q_title.lower() for i in interests)
            matches_skill = any(s in q_cat.lower() or s in q_title.lower() for s in skills)

            if is_weak:
                score += 50.0
                if q_diff in ["BEGINNER", "INTERMEDIATE"]:
                    score += 25.0
                    rec_type = "WEAK_TOPIC_REVISION"
                    reason = f"Your performance in {q_cat} is below target. Taking this quiz will reinforce foundational concepts."
                else:
                    score += 10.0
                    rec_type = "WEAK_TOPIC_PRACTICE"
                    reason = f"Targeted practice to strengthen weaker areas in {q_cat}."
            elif is_strong:
                if q_diff == "ADVANCED":
                    score += 45.0
                    rec_type = "ADVANCED_CHALLENGE"
                    reason = f"You showed strong mastery in {q_cat}! Challenge yourself with this advanced quiz."
                else:
                    score += 15.0
                    rec_type = "SKILL_MAINTENANCE"
                    reason = f"Keep your sharp proficiency in {q_cat} active."
            elif matches_interest or matches_skill:
                score += 35.0
                rec_type = "INTEREST_BASED"
                reason = f"Recommended based on your profile interests in {q_cat}."
            else:
                score += 10.0
                if not is_attempted:
                    score += 15.0
                    reason = f"Explore new horizons in {q_cat}."

            # Boost unattempted quizzes
            if not is_attempted:
                score += 20.0
            else:
                score -= 10.0 # slight penalty for already completed

            scored_quizzes.append({
                "quiz": quiz,
                "score": round(score, 2),
                "recommendationType": rec_type,
                "reason": reason
            })

        # Sort quizzes by highest recommendation score
        scored_quizzes.sort(key=lambda x: x["score"], reverse=True)

        top_recommended = scored_quizzes[:6]

        # Categorize into Easier Practice and Advanced Challenges
        easier_practice = [
            sq for sq in scored_quizzes 
            if sq["quiz"].get("difficulty", "").upper() in ["BEGINNER", "INTERMEDIATE"]
            and (any(w["subject"].lower() == sq["quiz"].get("categoryName", sq["quiz"].get("category", "")).lower() for w in weak_subjects) or not attempted_quiz_ids)
        ][:4]

        advanced_challenges = [
            sq for sq in scored_quizzes 
            if sq["quiz"].get("difficulty", "").upper() == "ADVANCED"
        ][:4]

        # 3. Personalized Learning Roadmap
        learning_roadmap = []
        step = 1

        if weak_subjects:
            for w in weak_subjects[:2]:
                learning_roadmap.append({
                    "step": step,
                    "title": f"Reinforce {w['subject']} Basics",
                    "description": f"Focus on foundational quizzes and review topics. Current average: {w['avgScore']}%",
                    "priority": "HIGH"
                })
                step += 1

        if topics_to_revise:
            learning_roadmap.append({
                "step": step,
                "title": f"Revise Critical Topics ({', '.join(t['topic'] for t in topics_to_revise[:3])})",
                "description": "Dedicate 20 minutes to review incorrect question explanations and documentation.",
                "priority": "HIGH"
            })
            step += 1

        if strong_subjects:
            for s in strong_subjects[:2]:
                learning_roadmap.append({
                    "step": step,
                    "title": f"Pursue Advanced Mastery in {s['subject']}",
                    "description": f"Attempt Advanced difficulty quizzes and timed mock tests. Current average: {s['avgScore']}%",
                    "priority": "MEDIUM"
                })
                step += 1

        learning_roadmap.append({
            "step": step,
            "title": "Maintain Learning Streak & Climb Leaderboard",
            "description": "Complete at least 1 quiz daily to earn streak badges and rank up.",
            "priority": "NORMAL"
        })

        response_payload = {
            "userId": user_id,
            "strongSubjects": strong_subjects,
            "weakSubjects": weak_subjects,
            "topicsToRevise": topics_to_revise,
            "recommendedQuizzes": top_recommended,
            "easierPractice": easier_practice,
            "advancedChallenges": advanced_challenges,
            "learningRoadmap": learning_roadmap,
            "algorithmSummary": "Scored using Adaptive Deficiency Weighting, Difficulty Progression, and Profile Interest Cosine Heuristics."
        }

        return jsonify(response_payload)

    except Exception as e:
        logger.exception("Error in AI recommendation:")
        return jsonify({"error": str(e), "status": "FAILED"}), 500

if __name__ == "__main__":
    logger.info("Starting QuizMaster AI Microservice on port 5001...")
    app.run(host="0.0.0.0", port=5001, debug=False)
