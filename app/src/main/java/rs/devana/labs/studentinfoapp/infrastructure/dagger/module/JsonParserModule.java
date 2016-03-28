package rs.devana.labs.studentinfoapp.infrastructure.dagger.module;

import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import rs.devana.labs.studentinfoapp.infrastructure.json.parser.ClassroomParser;
import rs.devana.labs.studentinfoapp.infrastructure.json.parser.CourseEventParser;
import rs.devana.labs.studentinfoapp.infrastructure.json.parser.CourseParser;
import rs.devana.labs.studentinfoapp.infrastructure.json.parser.GlobalEventParser;
import rs.devana.labs.studentinfoapp.infrastructure.json.parser.GroupEventParser;
import rs.devana.labs.studentinfoapp.infrastructure.json.parser.GroupParser;
import rs.devana.labs.studentinfoapp.infrastructure.json.parser.LectureNotificationParser;
import rs.devana.labs.studentinfoapp.infrastructure.json.parser.LectureParser;
import rs.devana.labs.studentinfoapp.infrastructure.json.parser.NotificationParser;

@Module
public class JsonParserModule {

    @Provides
    @Singleton
    public LectureParser provideLectureParser(CourseParser courseParser, ClassroomParser classroomParser, LectureNotificationParser lectureNotificationParser, SharedPreferences sharedPreferences) {
        return new LectureParser(courseParser, classroomParser, lectureNotificationParser, sharedPreferences);
    }

    @Provides
    @Singleton
    public NotificationParser provideNotificationParser() {
        return new NotificationParser();
    }

    @Provides
    @Singleton
    public ClassroomParser provideClassroomParser() {
        return new ClassroomParser();
    }

    @Provides
    @Singleton
    public CourseParser provideCourseParser() {
        return new CourseParser();
    }

    @Provides
    @Singleton
    public GroupParser provideGroupParser() {
        return new GroupParser();
    }

    @Provides
    @Singleton
    public GroupEventParser provideGroupEventParser(ClassroomParser classroomParser, NotificationParser notificationParser, GroupParser groupParser) {
        return new GroupEventParser(classroomParser, notificationParser, groupParser);
    }

    @Provides
    @Singleton
    public GlobalEventParser provideGlobalEventParser(ClassroomParser classroomParser, NotificationParser notificationParser) {
        return new GlobalEventParser(classroomParser, notificationParser);
    }

    @Provides
    @Singleton
    public CourseEventParser provideCourseEventParser(ClassroomParser classroomParser, NotificationParser notificationParser, CourseParser courseParser) {
        return new CourseEventParser(classroomParser, notificationParser, courseParser);
    }

}