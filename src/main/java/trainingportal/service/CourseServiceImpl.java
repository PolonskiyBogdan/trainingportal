package trainingportal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import trainingportal.dao.CourseDAOImpl;
import trainingportal.dao.UserDao;
import trainingportal.model.Course;
import trainingportal.model.CourseStatus;
import trainingportal.service.generic.GenericServiceImpl;
import trainingportal.model.LoggedInUser;
import trainingportal.model.Role;


import java.util.Collection;
import java.util.List;

@Service("courseService")
@Transactional
public class CourseServiceImpl extends GenericServiceImpl<Course> implements CourseService {

    @Autowired
    private CourseDAOImpl courseDAO;

    @Autowired
    private UserDao userDAO;

    @Override
    public int getPages(double total) {
        return (int) Math.ceil(courseDAO.countAll() / total);
    }

    @Override
    public List<Course> findByTrainerId(Long id) {
        return courseDAO.findByTrainerId(id);

    public int getPagesByUserId(Long userId, double total) {

        if(userId == Role.ADMIN){
            return (int) Math.ceil(courseDAO.countAll() / total);
        }

        return (int) Math.ceil(courseDAO.countAllByUserId(userId) / total);
    }

    @Override
    public List<CourseStatus> getStatusList() {
        return courseDAO.getStatusList();
    }

    @Override
    public CourseStatus findStatusById(Long id) {
        return courseDAO.findStatusById(id);
    }

    @Override
    public List<Course> getCoursesPage(int page, int total, Authentication auth) {

        if(page == 1){
            //do nothing
        } else {
            page = (page - 1) * total + 1;
        }

        Long userId = ((LoggedInUser)auth.getPrincipal()).getId();
        List<Course> courseList;

        Collection<GrantedAuthority> authority = ((LoggedInUser) auth.getPrincipal()).getAuthorities();

        if(authority.iterator().next().toString().equals("ROLE_ADMIN")){
            courseList = courseDAO.getAllAsPage(page, total);
        } else {
            courseList = courseDAO.getAllAsPageById(userId, page, total);
        }
        for(Course course : courseList){
            course.setTrainer(userDAO.findById(course.getTrainerId()));
            course.setStatus(courseDAO.findStatusById(course.getCourseStatus()));
        }
        return courseList;
    }
}
