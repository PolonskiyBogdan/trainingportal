package trainingportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import trainingportal.model.Course;
import trainingportal.model.CourseStatus;
import trainingportal.model.Role;
import trainingportal.model.User;
import trainingportal.service.CourseServiceImpl;
import trainingportal.service.UserService;
import trainingportal.universalexportcreator.dao.DataDaoImpl;

import java.util.ArrayList;
import java.util.List;

@Controller
public class CourseController {

    @Autowired
    CourseServiceImpl courseService;

    @Autowired
    UserService userService;

    private static final int ROWS_LIMIT = 10;

    @RequestMapping(value = "/course_create/{page}")
    public ModelAndView showCoursesList(@PathVariable("page") int page, Long courseId, ModelAndView modelAndView) {

        List<Course> courseList = courseService.getAllAsPage(page, ROWS_LIMIT);

        for(Course course : courseList){
            course.setTrainer(userService.findById(course.getTrainerId()));
            course.setStatus(courseService.findStatusById(course.getCourseStatus()));
        }
        modelAndView.addObject("courseList", courseList);
        modelAndView.addObject("pages", courseService.getPages(ROWS_LIMIT));
        modelAndView.setViewName("courseCreator/course_create");
        modelAndView.addObject("currentUrl", "course_create");
        return modelAndView;
    }

    @RequestMapping(value = "/course-add", method = RequestMethod.GET)
    public ModelAndView addCourse(ModelAndView modelAndView) {

        List<User> trainers = userService.findAllEnabledByRole(Role.TRAINER);
        List<CourseStatus> statuses = courseService.getStatusList();

        modelAndView.addObject("statuses", statuses);
        modelAndView.addObject("trainers", trainers);
        modelAndView.addObject("course", new Course());
        modelAndView.setViewName("courseCreator/course_add");

        return modelAndView;
    }

    @RequestMapping(value = "course-save", method = RequestMethod.POST)
    public ModelAndView saveCourse(Course course, ModelAndView modelAndView) {
        courseService.save(course);
        modelAndView.setViewName("redirect:/course_create/1");
        return modelAndView;
    }

    @RequestMapping(value = {"/edit-course-{id}"}, method = RequestMethod.GET)
    public ModelAndView editCourseBase(@PathVariable("id") Long courseId, ModelAndView modelAndView) {
        Course course = courseService.findById(courseId);

        List<User> trainers = userService.findAllEnabledByRole(Role.TRAINER);
        List<CourseStatus> statuses = courseService.getStatusList();

        modelAndView.addObject("statuses", statuses);
        modelAndView.addObject("trainers", trainers);
        modelAndView.addObject("course", course);
        modelAndView.addObject("edit", true);
        modelAndView.setViewName("courseCreator/edit_course_by_id");

        return modelAndView;
    }

    @RequestMapping(value = {"/edit-course-{id}"}, method = RequestMethod.POST)
    public ModelAndView editCourseById(Course course, BindingResult bindingResult, ModelAndView modelAndView, RedirectAttributes redirect) {
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("courseCreator/edit_course_by_id");
            return modelAndView;
        } else {
            courseService.update(course);
            modelAndView.setViewName("redirect:/course_create/1");
            return modelAndView;
        }
    }

    @RequestMapping(value = "/course-delete-by-{id}", method = RequestMethod.GET)
    public ModelAndView deleteCourseById(@PathVariable("id") Long courseId, ModelAndView model, RedirectAttributes redirect) {
        courseService.deleteById(courseId);

        redirect.addFlashAttribute("successMessage", "course deleted successfully");

        model.setViewName("redirect:/course_create/1");
        return model;
    }

    @Autowired
    public DataDaoImpl dataDao;

    @RequestMapping(value = "/course-download-all-courses", method = RequestMethod.GET)
    public ModelAndView downloadAllTrainers(ModelAndView model, RedirectAttributes redir){

        List list = new ArrayList();
        list.add("name");
        list.add("courseid");
        list.add("course_level");
        list.add("course_status_id");
        list.add("min_number");
        list.add("max_number");
        list.add("description");
        list.add("trainer_id");
        list.add("lessons_number");


        List<List> courses = dataDao.findFieldsFromTable(list, "course","allCourses","table");

//        String fromFile = "/Users/mrlova/Downloads/log.txt";
//        String toFile = "/Users/mrlova/Downloads/log.txt";
//
//        try {
//            FileUtils.copyURLToFile(new URL(fromFile), new File(toFile), 10000, 10000);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        return model;
    }
}