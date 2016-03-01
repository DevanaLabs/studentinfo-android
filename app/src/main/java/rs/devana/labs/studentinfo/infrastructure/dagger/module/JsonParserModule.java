package rs.devana.labs.studentinfo.infrastructure.dagger.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import rs.devana.labs.studentinfo.infrastructure.json.parser.ClassroomParser;
import rs.devana.labs.studentinfo.infrastructure.json.parser.CourseParser;
import rs.devana.labs.studentinfo.infrastructure.json.parser.GroupParser;
import rs.devana.labs.studentinfo.infrastructure.json.parser.LectureNotificationParser;
import rs.devana.labs.studentinfo.infrastructure.json.parser.LectureParser;
import rs.devana.labs.studentinfo.infrastructure.json.parser.NotificationParser;

@Module
public class JsonParserModule {

    @Provides
    @Singleton
    public LectureParser provideLectureParser(CourseParser courseParser, ClassroomParser classroomParser, LectureNotificationParser lectureNotificationParser) {
        return new LectureParser(courseParser, classroomParser, lectureNotificationParser);
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

}
