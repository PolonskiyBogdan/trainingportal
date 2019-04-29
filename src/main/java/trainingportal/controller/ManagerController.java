package trainingportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import trainingportal.model.Role;
import trainingportal.model.User;
import trainingportal.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Controller
public class ManagerController {
    @Autowired
    private UserService managerService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    /* Show all managers*/
    @RequestMapping("/managers")
    public ModelAndView allManagers(ModelAndView model) {

        List<User> managers = managerService.findAllByRole(Role.MANAGER);
        model.addObject("managers", managers);
        model.setViewName("manager/managers");

        return model;
    }

    @RequestMapping("/manager-{id}")
    public ModelAndView showManager(@PathVariable("id") Long id, ModelAndView model) {

        User manager= managerService.findById(id);
        model.addObject("manager", manager);
        model.setViewName("manager/show");
        return model;
    }

    @GetMapping("/addsubordinates")
    public ModelAndView showAddSubordinates(Long id, ModelAndView model) {

        List<User> users = managerService.findFreeUsers();

        model.addObject("users", users);
        model.addObject("manager", managerService.findById(id));

        model.setViewName("manager/addsubordinates");

        return model;
    }

    @PostMapping("/addSelectedSubordinates")
    public ModelAndView addSelectedSubordinates(Long managerId,
                                                @RequestParam(value = "userId", required = false) Long[] userIds,
                                                ModelAndView model, RedirectAttributes redir) {

        String message = managerService.assignSubordinates(managerId, userIds);

        redir.addFlashAttribute("infoMessage", message);

        model.addObject("id", managerId);
        model.setViewName("redirect:subordinates");

        return model;
    }

    @GetMapping("/subordinates")
    public ModelAndView viewSubordinates(Long id, ModelAndView model) {

        User manager = managerService.findById(id);

        // Find all subordinates of the manager by manager's id
        List<User> subordinates = managerService.findSubordinatesById(id);

        model.addObject("manager", manager);
        model.addObject("subordinates", subordinates);

        model.setViewName("manager/subordinates");

        return model;
    }

    @GetMapping("/releaseSubordinate")
    public ModelAndView setSubordinateFree(Long id, ModelAndView model, RedirectAttributes redir) {

        User employee = managerService.findById(id);
        User manager = managerService.findManagerBySubordinateId(id);

        managerService.setManagerId(null, id);

        redir.addFlashAttribute("successMessage",
                "User " + employee.getUserName() + " " + employee.getEmail() + " has no manager now.");
        model.addObject("id", manager.getUserId());
        model.setViewName("redirect:subordinates");

        return model;
    }

    @GetMapping("backtosubordinates")
    public ModelAndView backToManager(Long id, ModelAndView model) {

        model.addObject("id", id);
        model.setViewName("redirect:subordinates");

        return model;
    }

    @RequestMapping("/manager-profile-{id}")
    public ModelAndView showManagerProfile(@PathVariable("id") Long id, ModelAndView model) {

        User manager= managerService.findById(id);
        model.addObject("manager", manager);
        model.setViewName("manager/profile");

        return model;
    }

    @GetMapping("/manager-add")
    public ModelAndView addManager(ModelAndView model){

        model.addObject("user",new User());
        model.setViewName("manager/add");

        return model;
    }

    @RequestMapping(value = { "/manager-update-{id}" }, method = RequestMethod.GET)
    public ModelAndView editManager(@PathVariable("id") Long id, ModelAndView model) {

        User manager = managerService.findById(id);

        manager.setPassword(bCryptPasswordEncoder.encode(manager.getPassword()));
        model.addObject("manager", manager);
        model.addObject("edit", true);
        model.setViewName("manager/update");

        return model;
    }

    @RequestMapping(value = { "/manager-update-{id}" }, method = RequestMethod.POST)
    public ModelAndView updateManager(User manager,BindingResult bindingResult, ModelAndView model, RedirectAttributes redir){
        if(bindingResult.hasErrors())
        {
            model.setViewName("manager/update");
            return model;
        }
        else {
            managerService.update(manager);
            redir.addFlashAttribute("successMessage", "User " + manager.getUserName() + " "+ manager.getEmail() + " updated successfully");
            model.setViewName("redirect:/managers");
            return model;
        }
    }

    @RequestMapping(value = "saveManager", method = RequestMethod.POST)
    public ModelAndView saveManager(@Valid User user, BindingResult bindingResult, ModelAndView model, RedirectAttributes redir){

        User userExists = managerService.findByEmail(user.getEmail());

        if (userExists != null) {
            model.addObject("alreadyRegisteredMessage",
                    "Oops! There is already a user registered with the email provided.");
            model.setViewName("manager/add");
            bindingResult.reject("email");
            return model;
        }
        if (bindingResult.hasErrors()) {
            model.setViewName("manager/add");
            return model;
        }
        user.setEnabled(1);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRoleId(Role.MANAGER);
        managerService.save(user);
        redir.addFlashAttribute("successMessage", "User " + user.getUserName() + " "+ user.getEmail() + " created successfully");
        model.setViewName("redirect:/managers");

        return model;
    }

    @RequestMapping(value = "/manager-delete-{id}", method = RequestMethod.GET)
    public ModelAndView deleteManager(@PathVariable("id") Long managerId, ModelAndView model, RedirectAttributes redir){

        User manager = managerService.findById(managerId);
        managerService.deleteById(managerId);
        redir.addFlashAttribute("successMessage", "User " + manager.getUserName() + " "+ manager.getEmail() + " deleted successfully");
        model.setViewName("redirect:/managers");
        return model;
    }

    @RequestMapping(value = "/managers-delete-all", method = RequestMethod.GET)
    public ModelAndView deleteAllManagers(ModelAndView model, RedirectAttributes redir){

        managerService.deleteAllByRole(Role.MANAGER);
        redir.addFlashAttribute("successMessage", "All users deleted successfully");
        model.setViewName("redirect:/managers");

        return model;
    }
}
