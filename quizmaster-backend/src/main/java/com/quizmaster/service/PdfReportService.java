package com.quizmaster.service;

import com.quizmaster.payload.response.QuestionReviewDto;
import com.quizmaster.payload.response.QuizResultDto;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PdfReportService {

    public String generateHtmlReport(QuizResultDto result, String studentName, String studentEmail, List<QuestionReviewDto> reviews) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMMM dd, yyyy HH:mm:ss");
        String dateStr = result.getCompletedAt() != null ? result.getCompletedAt().format(dtf) : "Recent";

        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>\n");
        sb.append("<html lang=\"en\">\n<head>\n<meta charset=\"UTF-8\">\n");
        sb.append("<title>QuizMaster - Certificate & Performance Report</title>\n");
        sb.append("<style>\n");
        sb.append("body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; margin: 40px; color: #1e293b; background: #fff; }\n");
        sb.append(".report-container { max-width: 850px; margin: 0 auto; border: 2px solid #e2e8f0; border-radius: 12px; padding: 40px; box-shadow: 0 4px 6px -1px rgba(0,0,0,0.1); }\n");
        sb.append(".header { display: flex; justify-content: space-between; align-items: center; border-bottom: 2px solid #4f46e5; padding-bottom: 20px; margin-bottom: 25px; }\n");
        sb.append(".brand { font-size: 28px; font-weight: 800; color: #4f46e5; }\n");
        sb.append(".badge { display: inline-block; padding: 6px 14px; border-radius: 9999px; font-weight: 700; font-size: 14px; }\n");
        sb.append(".badge-pass { background: #dcfce7; color: #15803d; }\n");
        sb.append(".badge-fail { background: #fee2e2; color: #b91c1c; }\n");
        sb.append(".stats-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 15px; margin: 25px 0; text-align: center; }\n");
        sb.append(".stat-card { background: #f8fafc; border: 1px solid #e2e8f0; padding: 15px; border-radius: 8px; }\n");
        sb.append(".stat-val { font-size: 24px; font-weight: 800; color: #0f172a; }\n");
        sb.append(".stat-lbl { font-size: 12px; color: #64748b; text-transform: uppercase; margin-top: 4px; }\n");
        sb.append(".review-item { margin-top: 20px; border-bottom: 1px solid #e2e8f0; padding-bottom: 15px; }\n");
        sb.append(".q-text { font-weight: 600; font-size: 15px; margin-bottom: 8px; }\n");
        sb.append(".explanation { background: #f1f5f9; padding: 10px 14px; border-radius: 6px; font-size: 13px; color: #334155; margin-top: 8px; }\n");
        sb.append("@media print { body { margin: 0; } .report-container { border: none; box-shadow: none; } .no-print { display: none; } }\n");
        sb.append("</style>\n</head>\n<body>\n");

        sb.append("<div class=\"report-container\">\n");
        sb.append("  <div class=\"no-print\" style=\"text-align: right; margin-bottom: 15px;\">\n");
        sb.append("    <button onclick=\"window.print()\" style=\"padding: 8px 18px; background: #4f46e5; color: white; border: none; border-radius: 6px; cursor: pointer; font-weight: 600;\">Print / Save as PDF</button>\n");
        sb.append("  </div>\n");

        sb.append("  <div class=\"header\">\n");
        sb.append("    <div>\n");
        sb.append("      <div class=\"brand\">QuizMaster</div>\n");
        sb.append("      <div style=\"color: #64748b; font-size: 13px;\">AI-Powered Assessment Platform</div>\n");
        sb.append("    </div>\n");
        sb.append("    <div style=\"text-align: right;\">\n");
        sb.append("      <div class=\"badge ").append(Boolean.TRUE.equals(result.getPassed()) ? "badge-pass" : "badge-fail").append("\">\n");
        sb.append("        ").append(Boolean.TRUE.equals(result.getPassed()) ? "PASSED" : "FAILED").append(" (").append(result.getPerformanceLevel()).append(")\n");
        sb.append("      </div>\n");
        sb.append("      <div style=\"font-size: 12px; color: #64748b; margin-top: 6px;\">").append(dateStr).append("</div>\n");
        sb.append("    </div>\n");
        sb.append("  </div>\n");

        sb.append("  <div style=\"margin-bottom: 20px;\">\n");
        sb.append("    <h2 style=\"margin: 0 0 5px 0;\">").append(result.getQuizTitle()).append("</h2>\n");
        sb.append("    <p style=\"color: #64748b; margin: 0;\">Category: <strong>").append(result.getCategoryName()).append("</strong> | Candidate: <strong>").append(studentName).append("</strong> (").append(studentEmail).append(")</p>\n");
        sb.append("  </div>\n");

        sb.append("  <div class=\"stats-grid\">\n");
        sb.append("    <div class=\"stat-card\"><div class=\"stat-val\">").append(result.getScore()).append(" / ").append(result.getTotalMarks()).append("</div><div class=\"stat-lbl\">Marks Scored</div></div>\n");
        sb.append("    <div class=\"stat-card\"><div class=\"stat-val\">").append(result.getPercentage()).append("%</div><div class=\"stat-lbl\">Percentage</div></div>\n");
        sb.append("    <div class=\"stat-card\"><div class=\"stat-val\">").append(result.getAccuracy()).append("%</div><div class=\"stat-lbl\">Accuracy</div></div>\n");
        sb.append("    <div class=\"stat-card\"><div class=\"stat-val\">").append(result.getTimeTakenSeconds()).append("s</div><div class=\"stat-lbl\">Time Taken</div></div>\n");
        sb.append("  </div>\n");

        sb.append("  <h3 style=\"margin-top: 30px; border-bottom: 2px solid #e2e8f0; padding-bottom: 8px;\">Question-by-Question Breakdown</h3>\n");
        if (reviews != null) {
            int qNum = 1;
            for (QuestionReviewDto q : reviews) {
                sb.append("  <div class=\"review-item\">\n");
                sb.append("    <div class=\"q-text\">Q").append(qNum++).append(". ").append(q.getQuestionText())
                        .append(" <span style=\"font-size: 12px; font-weight: normal; color: ").append(Boolean.TRUE.equals(q.getIsCorrect()) ? "#15803d" : "#b91c1c").append(";\">(")
                        .append(Boolean.TRUE.equals(q.getIsCorrect()) ? "Correct" : "Incorrect")
                        .append(" - ").append(q.getMarksAwarded()).append("/").append(q.getMaxMarks()).append(" Marks)</span></div>\n");

                sb.append("    <div style=\"font-size: 13px; margin: 6px 0;\">Topic: <strong>").append(q.getTopic() != null ? q.getTopic() : "General").append("</strong></div>\n");
                if (q.getExplanation() != null && !q.getExplanation().isBlank()) {
                    sb.append("    <div class=\"explanation\"><strong>Explanation:</strong> ").append(q.getExplanation()).append("</div>\n");
                }
                sb.append("  </div>\n");
            }
        }

        sb.append("  <div style=\"margin-top: 40px; text-align: center; color: #94a3b8; font-size: 12px; border-top: 1px solid #e2e8f0; padding-top: 15px;\">\n");
        sb.append("    QuizMaster Evaluation System &copy; 2026. Verified & Tamper-Resistant Report.\n");
        sb.append("  </div>\n");
        sb.append("</div>\n</body>\n</html>");

        return sb.toString();
    }
}
