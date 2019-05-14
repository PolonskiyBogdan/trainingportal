package trainingportal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import trainingportal.dao.LessonDaoImpl;
import trainingportal.model.Lesson;
import trainingportal.service.generic.GenericServiceImpl;

import java.util.List;

@Service("lessonService")
@Transactional
public class LessonServiceImpl extends GenericServiceImpl<Lesson> implements LessonService {

    @Autowired
    private LessonDaoImpl lessonDao;

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
    public List<Lesson> getLessonsPageByCourseId(int page, int total, Long courseId) {

        if(page == 1){
            //do nothing
        } else {
            page = (page - 1) * total + 1;
        }
        return lessonDao.getAllAsPageByCourseId(courseId, page, total);
    }

    @Override
    public int getPages(Long courseId, double total) {
        return (int) Math.ceil(lessonDao.countAllByCourseId(courseId) / total);
    }

    @Override
    public boolean isConnectedWithTrainer(Long userId, Long courseId) {
        Long trainerId =  lessonDao.getTrainerIdByCourseId(courseId);

        if(userId == trainerId){
            return true;
        } else  {
            return false;
        }
    }

    @Override
    public Lesson getLessonByScheduleId(Long id) {
        return lessonDao.getLessonByScheduleId(id);
    }
}
