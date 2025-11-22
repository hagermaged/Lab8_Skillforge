package db;
import model.*;
import java.util.*;
import java.util.regex.*;

public class IdGenerator {

    private static int extract(String id, String regex) {
        Matcher m = Pattern.compile(regex).matcher(id);
        if (m.matches()) return Integer.parseInt(m.group(1));
        return 0;
    }

    public static String generateUserId(List<User> users) {
        int max = 0;
        for (User u : users)
            max = Math.max(max, extract(u.getUserId(), "^U(\\d+)$"));
        return "U" + (max + 1);
    }

    public static String generateCourseId(List<Course> courses) {
        int max = 0;
        for (Course c : courses)
            max = Math.max(max, extract(c.getCourseId(), "^C(\\d+)$"));
        return "C" + (max + 1);
    }

    public static String generateLessonId(List<Course> courses) {
        int max = 0;
        for (Course c : courses)
            for (Lesson l : c.getLessons())
                max = Math.max(max, extract(l.getLessonId(), "^L(\\d+)$"));
        return "L" + (max + 1);
    }
}
