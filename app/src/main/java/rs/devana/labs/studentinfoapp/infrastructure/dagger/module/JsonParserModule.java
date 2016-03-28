package rs.devana.labs.studentinfoapp.infrastructure.dagger.module;

import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import rs.devana.labs.studentinfoapp.infrastructure.json.parser.ClassroomParser;
import rs.devana.labs.studentinfoapp.infrastructure.json.parser.CourseEventParser;
import rs.devana.labs.studentinfoapp.infrastructure.json.parser.CourseParser;
import rs.devana.labs.studentinfoapp.infrastructure.json.parser.EventNotificationParser;
import rs.devana.labs.studentinfoapp.infrastructure.json.parser.EventParser;
import rs.devana.labs.studentinfoapp.infrastructure.json.parser.GlobalEventParser;
import rs.devana.labs.studentinfoapp.infrastructure.json.parser.GroupEventParser;
import rs.devana.labs.studentinfoapp.infrastructure.json.parser.GroupParser;
import rs.devana.labs.studentinfoapp.infrastructure.json.parser.LectureNotificationParser;
import rs.devana.labs.studentinfoapp.infrastructure.json.parser.LectureParser;

@Module
public class JsonParserModule {

    @Provides
    @Singleton
    public LectureParser provideLectureParser(CourseParser courseParser, ClassroomParser classroomParser, LectureNotificationParser lectureNotificationParser, SharedPreferences sharedPreferences) {
        return new LectureParser(courseParser, classroomParser, lectureNotificationParser, sharedPreferences);
    }

    @Provides
    @Singleton
    public EventParser provideEventParser(GroupEventParser groupEventParser, GlobalEventParser globalEventParser, CourseEventParser courseEventParser, SharedPreferences sharedPreferences) {
        return new EventParser(groupEventParser, globalEventParser, courseEventParser, sharedPreferences);
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
    public GroupEventParser provideGroupEventParser(ClassroomParser classroomParser, EventNotificationParser eventNotificationParser, GroupParser groupParser) {
        return new GroupEventParser(classroomParser, eventNotificationParser, groupParser);
    }

    @Provides
    @Singleton
    public GlobalEventParser provideGlobalEventParser(ClassroomParser classroomParser, EventNotificationParser eventNotificationParser) {
        return new GlobalEventParser(classroomParser, eventNotificationParser);
    }

    @Provides
    @Singleton
    public CourseEventParser provideCourseEventParser(ClassroomParser classroomParser, EventNotificationParser eventNotificationParser, CourseParser courseParser) {
        return new CourseEventParser(classroomParser, eventNotificationParser, courseParser);
    }
}
