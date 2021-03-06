package trainingportal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import trainingportal.dao.CourseDao;
import trainingportal.dao.UserDao;
import trainingportal.model.Course;
import trainingportal.model.CourseStatus;
import trainingportal.model.Role;
import trainingportal.model.User;
import trainingportal.service.generic.GenericServiceImpl;
import trainingportal.model.Role;


import java.util.List;

@Service("courseService")
@Transactional
public class CourseServiceImpl extends GenericServiceImpl<Course> implements CourseService {
    @Autowired
    private CourseDao courseDao;
    @Autowired
    private UserDao userDao;

    @Override
    public int getPages(double rowsPerPage) {
        return (int) Math.ceil(courseDao.countAll() / rowsPerPage);
    }

    @Override
    public List<Course> findByTrainerId(Long id) {
        return courseDao.findByTrainerId(id);
    }

    @Override
    public int getPagesByUserId(Long userId, double rowsPerPage, String role) {

        if (role.equals("ROLE_ADMIN")) {
            return (int) Math.ceil(courseDao.countAll() / rowsPerPage);
        }

        if (role.equals("ROLE_TRAINER")) {
            return (int) Math.ceil(courseDao.countAllByTrainerId(userId) / rowsPerPage);
        }
        return (int) Math.ceil(courseDao.countAllByUserId(userId) / rowsPerPage);
    }

    @Override
    public List<CourseStatus> getStatusList() {
        return courseDao.getStatusList();
    }

    @Override
    public CourseStatus findStatusById(Long id) {
        return courseDao.findStatusById(id);
    }

    @Override
    public List<Course> getCoursesPage(int page, int rowsPerPage, Long userId, String role) {

        //get page number  GENERIC SERVICE implementation class
        page = getPageNumber(page, rowsPerPage);

        List<Course> courseList;

        if (role.equals("ROLE_ADMIN")) {
            courseList = courseDao.getAllAsPage(page, rowsPerPage);
        } else if (role.equals("ROLE_TRAINER")) {
            courseList = courseDao.getAllAsPageById(userId, page, rowsPerPage);
        } else {
            courseList = courseDao.getAllAsPageByEmployeeId(userId, page, rowsPerPage);
        }
        for (Course course : courseList) {
            course.setTrainer(userDao.findById(course.getTrainerId()));
            course.setStatus(courseDao.findStatusById(course.getCourseStatus()));
        }
        return courseList;
    }

    @Override
    public List<Course> findCoursesByUser(User user) {

        List<Course> courseList;

        if(user.getRoleId().equals(Role.TRAINER)) {
            courseList =  courseDao.findByTrainerId(user.getUserId());
        } else {
            courseList = courseDao.findCoursesByUserId(user.getUserId());

            for(Course course : courseList){
                course.setTrainer(userDao.findById(course.getTrainerId()));
            }
        }
        return courseList;
    }

    @Override
    public Course findCourseIdByGroupId(Long groupId) {

        return courseDao.findCourseIdByGroupId(groupId);
    }
}
