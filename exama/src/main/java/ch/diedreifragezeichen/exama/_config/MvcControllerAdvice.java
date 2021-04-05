package ch.diedreifragezeichen.exama._config;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import ch.diedreifragezeichen.exama._services.AppService;
import ch.diedreifragezeichen.exama.courses.CoreCourse;
import ch.diedreifragezeichen.exama.courses.Course;
import ch.diedreifragezeichen.exama.semesters.Holiday;
import ch.diedreifragezeichen.exama.semesters.HolidayRepository;
import ch.diedreifragezeichen.exama.semesters.Semester;
import ch.diedreifragezeichen.exama.semesters.SemesterRepository;
import ch.diedreifragezeichen.exama.users.User;
import ch.diedreifragezeichen.exama.users.UserRepository;
import javassist.NotFoundException;

@ControllerAdvice
public class MvcControllerAdvice {
    @Autowired
    UserRepository userRepo2;
    @Autowired
    HolidayRepository holidayRepo2;
    @Autowired
    SemesterRepository semesterRepo2;
    @Autowired
    AppService helper;

    // @ModelAttribute("currentUser")
    // public User getCurrentUser(){
    // Authentication currentUserAuth =
    // SecurityContextHolder.getContext().getAuthentication();
    // User currentUser = userRepo.findUserByEmail(currentUserAuth.getName());
    // return currentUser;
    // }

    @ModelAttribute("coursesCurrentUser")
    public List<Course> getCoursesCurrentUser() {
        Authentication currentUserAuth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userRepo2.findUserByEmail(currentUserAuth.getName());
        if (currentUser == null || currentUser.getCourses() == null) {
            return null;
        }
        return currentUser.getCourses().stream().sorted((c1, c2) -> c1.getName().compareTo(c2.getName()))
                .collect(Collectors.toList());
    }

    @ModelAttribute("coreCoursesCurrentUser")
    public List<CoreCourse> getCoreCoursesCurrentUser() {
        Authentication currentUserAuth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userRepo2.findUserByEmail(currentUserAuth.getName());
        if (currentUser == null || currentUser.getCourses() == null) {
            return null;
        }
        return helper.getAllTeacherStudentCoreCourses();
    }

    @ModelAttribute("allHolidayDays")
    public String getAllHolidayDays() throws NotFoundException {
        
        /**
         * create a String including every single day of the holidays for datepicker
         * disableDates
         */
        Semester semester = getCurrentSemester();
        LocalDate semesterStart = semester.getStartDate().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate semesterEnd = semester.getEndDate().with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
        List<Holiday> listHolidays = holidayRepo2.findAllByStartDateBetween(semesterStart, semesterEnd);
        if (listHolidays == null) {
            throw new NotFoundException("No Holiday has been assigned");
        }
        List<String> allHolidayDaysList = new ArrayList<>();
        for (Holiday h : listHolidays) {
            long holidayLength = ChronoUnit.DAYS.between(h.getStartDate(), h.getEndDate());
            Stream.iterate(h.getStartDate(), date -> date.plusDays(1)).limit(holidayLength + 1)
                    .map(date -> (date.toString())).collect(Collectors.toCollection(() -> allHolidayDaysList));
        }
        String allHolidayDays = allHolidayDaysList.stream().map(s -> "\"" + s + "\"").collect(Collectors.joining(", "));

        // String join = "\"" + StringUtils.join(allHolidayDays,"','") + "'";
       return allHolidayDays;
    }

    @ModelAttribute("currentSemester")
    public Semester getCurrentSemester() throws NotFoundException {
        LocalDate today = LocalDate.now();
        LocalDate currentSemesterStart = semesterRepo2.findAll().stream().filter(u -> Objects.nonNull(u.getStartDate()))
                .map(Semester::getStartDate).filter(u -> Objects.nonNull(u.isBefore(today)))
                .filter(date -> date.isBefore(today)).sorted((c1, c2) -> c1.compareTo(c2))
                .reduce((first, second) -> second).get();
        if (currentSemesterStart == null) {
            throw new NotFoundException("No Semester has been assigned at the moment");
        }
        List<Semester> currentSem = semesterRepo2.findAll().stream()
                .filter(s -> s.getStartDate() == currentSemesterStart).collect(Collectors.toList());
        Semester currentSemester = currentSem.get(0);

        return currentSemester;
    }

}
