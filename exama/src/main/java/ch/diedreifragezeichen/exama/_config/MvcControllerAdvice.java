package ch.diedreifragezeichen.exama._config;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import ch.diedreifragezeichen.exama.courses.CoreCourse;
import ch.diedreifragezeichen.exama.courses.Course;
import ch.diedreifragezeichen.exama.users.User;
import ch.diedreifragezeichen.exama.users.UserRepository;

@ControllerAdvice
public class MvcControllerAdvice {
    @Autowired
    UserRepository userRepo2;

    // @ModelAttribute("currentUser")
    // public User getCurrentUser(){
    //     Authentication currentUserAuth = SecurityContextHolder.getContext().getAuthentication();
    //     User currentUser = userRepo.findUserByEmail(currentUserAuth.getName());
    //     return currentUser;
    // }

    @ModelAttribute("coursesCurrentUser")
    public List<Course> getCoursesCurrentUser(){
        Authentication currentUserAuth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userRepo2.findUserByEmail(currentUserAuth.getName());
        if(currentUser == null || currentUser.getCourses()==null){
            return null;
        }
        return currentUser.getCourses().stream().sorted((c1, c2) -> c1.getName().compareTo(c2.getName())).collect(Collectors.toList());
    }

    @ModelAttribute("coreCoursesCurrentUser")
    public List<CoreCourse> getCoreCoursesCurrentUser(){
        Authentication currentUserAuth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userRepo2.findUserByEmail(currentUserAuth.getName());
        if(currentUser == null || currentUser.getCourses()==null){
            return null;
        }
        return currentUser.getCourses().stream().filter(c -> Objects.nonNull(c.getCoreCourse())).map(Course::getCoreCourse).sorted((c1, c2) -> c1.getName().compareTo(c2.getName())).distinct().collect(Collectors.toList());
    }
}
