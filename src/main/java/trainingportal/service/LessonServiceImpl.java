package trainingportal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import trainingportal.dao.LessonDao;
import trainingportal.dao.UserGroupDao;
import trainingportal.model.Lesson;
import trainingportal.model.UserGroup;
import trainingportal.service.generic.GenericServiceImpl;

import java.util.List;

@Service("lessonService")
@Transactional
public class LessonServiceImpl extends GenericServiceImpl<Lesson> implements LessonService {
    @Autowired
    private LessonDao lessonDao;
    @Autowired
    private UserGroupDao userGroupDao;

    @Override
    public void update(Lesson lesson) {
        Lesson lessonEdit = lessonDao.findById(lesson.getLessonId());
        if (lessonEdit != null) {
            lessonEdit.setLessonName(lesson.getLessonName());
            lessonEdit.setLessonDescription(lesson.getLessonDescription());
            lessonEdit.setLessonDuration(lesson.getLessonDuration());
            lessonEdit.setCourseId(lesson.getCourseId());
            lessonEdit.setLessonNumber(lesson.getLessonNumber());
        }
        lessonDao.update(lessonEdit);
    }

    @Override
    public List<Lesson> getLessonCourseId(Long courseId) {
        return lessonDao.getLessonCourseId(courseId);
    }

    @Override
    public List<Lesson> getLessonsPageByCourseId(int page, int rowsPerPage, Long courseId) {

        //get page number in GENERIC SERVICE implementation class
        page = getPageNumber(page, rowsPerPage);

        return lessonDao.getAllAsPageByCourseId(courseId, page, rowsPerPage);
    }

    @Override
    public int getPages(Long courseId, double rowsPerPage) {
        return (int) Math.ceil(lessonDao.countAllByCourseId(courseId) / rowsPerPage);
    }

    @Override
    public boolean isConnectedWithTrainerByCourseId(Long userId, Long courseId) {
        Long trainerId = lessonDao.getTrainerIdByCourseId(courseId);

        return userId == trainerId;
    }

    @Override
    public boolean isConnectedWithLessonByCourseId(Long userId, Long courseId) {

        List<UserGroup> users = userGroupDao.getUserIdByCourseId(courseId);

        for (UserGroup user : users) {
            if (user.getUserId() == userId) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Lesson getLessonByScheduleId(Long id) {
        return lessonDao.getLessonByScheduleId(id);
    }

    public boolean isConnectedWithTrainerByLessonId(Long userId, Long lessonId) {

        Long trainerId = lessonDao.getTrainerIdByLessonId(lessonId);

        return trainerId == userId;
    }

    @Override
    public boolean isConnectedWithLessonByLessonId(Long userId, Long lessonId) {

        List<UserGroup> users = userGroupDao.getUserIdByLessonId(lessonId);

        for (UserGroup user : users) {
            if (user.getUserId() == userId) {
                return true;
            }
        }
        return false;
    }
}
