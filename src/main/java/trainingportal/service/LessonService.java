package trainingportal.service;

import org.springframework.security.core.Authentication;
import trainingportal.model.Course;
import trainingportal.model.Lesson;
import trainingportal.service.generic.GenericService;

import java.util.List;

public interface LessonService extends GenericService<Lesson> {
    List<Lesson> getLessonCourseId(Long lessonId);

    List<Lesson> getLessonsPageByCourseId(int page, int total, Long courseId);

    int getPages(Long courseId, double total);

    boolean isConnectedWithTrainer(Long userId, Long courseId);

    boolean isConnectedWithLessonByCourseId(Long userId, Long courseId);

    boolean isConnectedWithTrainerByLessonId(Long userId, Long lessonId);

    boolean isConnectedWithLessonByLessonId(Long userId, Long lessonId);
}
