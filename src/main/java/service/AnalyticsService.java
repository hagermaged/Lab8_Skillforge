package service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import db.JsonDatabaseManager;
import db.QuizDatabaseManager;
import model.Course;
import model.Lesson;
import model.Quiz;
import model.QuizAttempt;
import model.Student;

public class AnalyticsService {
    private final QuizDatabaseManager quizDb;
    private final JsonDatabaseManager jsonDb;

    public AnalyticsService() {
        this.quizDb = new QuizDatabaseManager();
        this.jsonDb = new JsonDatabaseManager();
    }

    // Average score for a quizId (delegates to getAttemptsByQuiz)
    public double getAverageScoreForQuiz(String quizId) {
        if (quizId == null) return 0.0;
        List<QuizAttempt> attempts = quizDb.getAttemptsByQuiz(quizId);
        if (attempts == null || attempts.isEmpty()) return 0.0;
        int sum = 0, count = 0;
        for (QuizAttempt a : attempts) {
            sum += a.getScore();
            count++;
        }
        return count == 0 ? 0.0 : (double) sum / count;
    }

    // Average score for lesson (resolve quiz and reuse)
    public double getAverageScoreForLesson(String courseId, String lessonId) {
        Quiz q = jsonDb.getQuizByLesson(courseId, lessonId);
        if (q == null) return 0.0;
        return getAverageScoreForQuiz(q.getQuizId());
    }

    // Average for course (all attempts in course)
    public double getAverageScoreForCourse(String courseId) {
        List<QuizAttempt> attempts = quizDb.getAttemptsByCourse(courseId);
        if (attempts == null || attempts.isEmpty()) return 0.0;
        int sum = 0, count = 0;
        for (QuizAttempt a : attempts) {
            sum += a.getScore();
            count++;
        }
        return count == 0 ? 0.0 : (double) sum / count;
    }

    // Lesson pass rate: % of unique students who passed at least one attempt for that quiz
    public double getLessonPassRate(String quizId) {
        if (quizId == null) return 0.0;
        List<QuizAttempt> attempts = quizDb.getAttemptsByQuiz(quizId);
        if (attempts == null || attempts.isEmpty()) return 0.0;

        List<String> students = new ArrayList<>();
        for (QuizAttempt a : attempts) {
            if (a != null && a.getStudentId() != null && !contains(students, a.getStudentId())) {
                students.add(a.getStudentId());
            }
        }
        if (students.isEmpty()) return 0.0;

        int passedCount = 0;
        for (String sid : students) {
            List<QuizAttempt> stuQuizAttempts = quizDb.getAttempts(sid, quizId);
            boolean passed = false;
            for (QuizAttempt aa : stuQuizAttempts) {
                if (aa != null && aa.isPassed()) { passed = true; break; }
            }
            if (passed) passedCount++;
        }
        return ((double) passedCount / (double) students.size()) * 100.0;
    }

    // Course completion: percentage of enrolled students who passed all lesson quizzes
    public double getCourseCompletionRate(String courseId) {
        Course course = jsonDb.findCourseById(courseId);
        if (course == null) return 0.0;

        List<Lesson> lessons = course.getLessons();
        List<String> quizIds = new ArrayList<>();
        if (lessons != null) {
            for (Lesson l : lessons) {
                if (l != null && l.hasQuiz() && l.getQuiz() != null && l.getQuiz().getQuizId() != null) {
                    quizIds.add(l.getQuiz().getQuizId());
                }
            }
        }
        if (quizIds.isEmpty()) return 0.0;

        List<Student> enrolled = course.getStudents();
        if (enrolled == null || enrolled.isEmpty()) return 0.0;

        int completedCount = 0;
        for (Student s : enrolled) {
            if (s == null || s.getUserId() == null) continue;
            boolean allPassed = true;
            for (String qid : quizIds) {
                // check student attempts for this qid
                List<QuizAttempt> stuAttempts = quizDb.getAttempts(s.getUserId(), qid);
                boolean passedThis = false;
                for (QuizAttempt a : stuAttempts) {
                    if (a != null && a.isPassed()) { passedThis = true; break; }
                }
                if (!passedThis) { allPassed = false; break; }
            }
            if (allPassed) completedCount++;
        }
        return ((double) completedCount / (double) enrolled.size()) * 100.0;
    }

    // Attempt distribution per quiz: counts of first-pass-attempt-index (1st, 2nd, ...)
    public List<Integer> getAttemptDistributionForQuiz(String quizId, int maxAttemptsToTrack) {
        List<Integer> distribution = new ArrayList<>();
        for (int i = 0; i < maxAttemptsToTrack; i++) distribution.add(0);

        if (quizId == null) return distribution;
        List<QuizAttempt> attempts = quizDb.getAttemptsByQuiz(quizId);
        if (attempts == null || attempts.isEmpty()) return distribution;

        // collect unique student ids
        List<String> students = new ArrayList<>();
        for (QuizAttempt a : attempts) {
            if (a != null && a.getStudentId() != null && !contains(students, a.getStudentId())) students.add(a.getStudentId());
        }

        for (String sid : students) {
            // gather student's attempts for this quiz (using getAttempts which filters by studentId & quizId)
            List<QuizAttempt> stuAttempts = quizDb.getAttempts(sid, quizId);
            // sort by attemptDate if available; else preserve order (JsonDB returns stored order)
            Collections.sort(stuAttempts, (a1, a2) -> {
                if (a1.getAttemptDate() == null && a2.getAttemptDate() == null) return 0;
                if (a1.getAttemptDate() == null) return -1;
                if (a2.getAttemptDate() == null) return 1;
                return a1.getAttemptDate().compareTo(a2.getAttemptDate());
            });

            int firstPassIndex = -1;
            for (int i = 0; i < stuAttempts.size(); i++) {
                if (stuAttempts.get(i).isPassed()) { firstPassIndex = i; break; }
            }
            if (firstPassIndex >= 0) {
                int idx = Math.min(firstPassIndex, maxAttemptsToTrack - 1);
                distribution.set(idx, distribution.get(idx) + 1);
            }
        }
        return distribution;
    }

    // Top N students by aggregated best scores in a course
    public List<String> getTopStudentsForCourse(String courseId, int topN) {
        List<String> result = new ArrayList<>();
        Course course = jsonDb.findCourseById(courseId);
        if (course == null) return result;
        List<Lesson> lessons = course.getLessons();
        List<String> quizIds = new ArrayList<>();
        if (lessons != null) {
            for (Lesson l : lessons) {
                if (l != null && l.hasQuiz()) quizIds.add(l.getQuiz().getQuizId());
            }
        }
        if (quizIds.isEmpty()) return result;

        List<Student> enrolled = course.getStudents();
        if (enrolled == null || enrolled.isEmpty()) return result;

        class Pair { String studentId; int total; Pair(String s, int t) {studentId = s; total = t;} }

        List<Pair> pairs = new ArrayList<>();
        for (Student s : enrolled) {
            if (s == null || s.getUserId() == null) continue;
            int total = 0;
            for (String qid : quizIds) {
                List<QuizAttempt> stuAttempts = quizDb.getAttempts(s.getUserId(), qid);
                int best = 0;
                for (QuizAttempt a : stuAttempts) {
                    if (a != null && a.getScore() > best) best = a.getScore();
                }
                total += best;
            }
            pairs.add(new Pair(s.getUserId(), total));
        }

        Collections.sort(pairs, (p1, p2) -> Integer.compare(p2.total, p1.total));
        for (int i = 0; i < pairs.size() && result.size() < topN; i++) result.add(pairs.get(i).studentId);

        return result;
    }

    // Weakest lessons: return lessonIds sorted by ascending average (lowest first)
    public List<String> getWeakestLessonsForCourse(String courseId, int topK) {
        List<String> result = new ArrayList<>();
        Course course = jsonDb.findCourseById(courseId);
        if (course == null) return result;
        List<Lesson> lessons = course.getLessons();
        if (lessons == null || lessons.isEmpty()) return result;

        class LA { String lessonId; double avg; LA(String l,double a){lessonId=l;avg=a;} }
        List<LA> list = new ArrayList<>();
        for (Lesson l : lessons) {
            if (l != null && l.hasQuiz()) {
                String qid = l.getQuiz().getQuizId();
                double avg = getAverageScoreForQuiz(qid);
                list.add(new LA(l.getLessonId(), avg));
            }
        }
        Collections.sort(list, (a,b) -> Double.compare(a.avg, b.avg));
        for (int i = 0; i < list.size() && i < topK; i++) result.add(list.get(i).lessonId);
        return result;
    }

    // Student trend: best score per lesson in course order
    public List<Integer> getStudentScoreTrend(String courseId, String studentId) {
        List<Integer> trend = new ArrayList<>();
        Course course = jsonDb.findCourseById(courseId);
        if (course == null) return trend;
        List<Lesson> lessons = course.getLessons();
        if (lessons == null) return trend;
        for (Lesson l : lessons) {
            if (l != null && l.hasQuiz()) {
                String qid = l.getQuiz().getQuizId();
                List<QuizAttempt> stuAttempts = quizDb.getAttempts(studentId, qid);
                int best = 0;
                for (QuizAttempt a : stuAttempts) if (a != null && a.getScore() > best) best = a.getScore();
                trend.add(best);
            } else {
                trend.add(0);
            }
        }
        return trend;
    }

    // helper
    private boolean contains(List<String> lst, String v) {
        if (v == null) return false;
        for (String s : lst) if (v.equals(s)) return true;
        return false;
    }
}
