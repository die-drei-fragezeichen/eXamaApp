package ch.diedreifragezeichen.exama._viewControllers;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.*;

import javax.persistence.*;
import org.springframework.beans.factory.annotation.*;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import ch.diedreifragezeichen.exama.assignments.exams.*;
import ch.diedreifragezeichen.exama.operator.*;
import ch.diedreifragezeichen.exama.semesters.*;
import ch.diedreifragezeichen.exama.subjects.*;
import javassist.NotFoundException;

@Controller
public class semesterViewController {
    @Autowired
    private ExamRepository examRepo;

    @Autowired
    private SubjectRepository subjectRepo;

    @Autowired
    private SemesterRepository semesterRepo;

    @Autowired
    private HolidayRepository holidayRepo;

    @PersistenceContext
    private EntityManager em;

    /**
     * The following methods handle the Semester view creation
     */

    @GetMapping("/semesterView/choose")
    public ModelAndView selectSemester() {
        ModelAndView mav = new ModelAndView("studentTemplates/semesterViewChoose");
        List<Semester> allSemesters = semesterRepo.findAll();
        mav.addObject("allSemesters", allSemesters);

        Operator semester = new Operator();
        mav.addObject("chosenSemster", semester);

        return mav;
    }

    @GetMapping("/semesterView/show")
    public ModelAndView showSemesterView(@RequestParam(name = "selectedSemester") Long id) throws NotFoundException {
        ModelAndView mav = new ModelAndView("studentTemplates/semesterViewShow");

        Semester semester = semesterRepo.findSemesterById(id);
        mav.addObject("semester", semester);

        // retrieve semester Information and first / last day of Semester
        LocalDate semesterStart = semester.getStartDate().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate monday = semesterStart;
        LocalDate semesterEnd = semester.getEndDate().with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        // count all the exams
        Long examTotal = examRepo.findAllByDueDateBetween(semesterStart, semesterEnd).stream().count();
        mav.addObject("examTotal", examTotal);

        // create a list of all Mondays
        List<LocalDate> mondays = new ArrayList<>();
        while (monday.isBefore(semesterEnd)) {
            mondays.add(monday);
            // Set up the loop.
            monday = monday.plusWeeks(1);
        }
        mav.addObject("allMondays", mondays);

        // retrieve allExams, week by week, for the whole semester and put into a list
        // of hashmap
        // note: dead approach with list of list leads to thymeleaf useability
        // restrictions
        // dead code: List<List<Exam>> allExams = new ArrayList<>();
        monday = semesterStart;
        List<HashMap<String, Exam>> allExams = new ArrayList<>();
        while (monday.isBefore(semesterEnd)) {
            List<Exam> examsByWeek = examRepo.findAllByDueDateBetween(monday, monday.with(DayOfWeek.SUNDAY));
            HashMap<String, Exam> map = new HashMap<>();
            for (Exam exam : examsByWeek) {
                map.put(exam.getCourse().getSubject().getTag(), exam);
            }
            allExams.add(map);
            monday = monday.plusWeeks(1);
        }
        mav.addObject("allExams", allExams);
        long xExam = allExams.stream().count();
        mav.addObject("xExam", xExam);

        // create a HolidayHashmap
        // used to find all "index" of holiday in holidayMap
        monday = semesterStart;
        List<Holiday> allHolidays = holidayRepo.findAllByStartDateBetween(semesterStart, semesterEnd);
        mav.addObject("allHolidays", allHolidays);
        HashMap<LocalDate, Holiday> allHolidaysMap = new HashMap<>();
        for (Holiday holiday : allHolidays) {

            allHolidaysMap.put(holiday.getStartDate(), holiday);
        }
        mav.addObject("allHolidaysMap", allHolidaysMap);


        // create a HolidayHashmap that maps every single day of the holiday with holiday
        monday = semesterStart;
        List<Holiday> listHolidays = holidayRepo.findAllByStartDateBetween(semesterStart, semesterEnd);
        HashMap<LocalDate, Holiday> alleTageMap = new HashMap<>();
        for (Holiday holiday : listHolidays) {
                long holidayLength = ChronoUnit.DAYS.between(
                        holiday.getStartDate(), holiday.getEndDate());
                List<LocalDate> allHolidayDays = Stream.iterate(holiday.getStartDate(), date -> date.plusDays(1)).limit(holidayLength)
                        .collect(Collectors.toList());
                        for (LocalDate date : allHolidayDays) {

                            alleTageMap.put(date, holiday);
                        }
        }

        mav.addObject("alleTageMap", alleTageMap);

        // create a List containing all the days of all the holidays
        // this allows catching holidays that are wrongly set, or begin mid-week ->
        // TemporalAdjusters then sets start to following Monday.
        List<Holiday> allHolidaysList = holidayRepo.findAllByStartDateBetween(semesterStart, semesterEnd);
        List<LocalDate> allHolidaysDays = new ArrayList<>();
        for (Holiday holiday : allHolidaysList) {
            long numOfDays = ChronoUnit.DAYS.between(
                    holiday.getStartDate().with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY)), holiday.getEndDate());
            List<LocalDate> allDays = Stream.iterate(holiday.getStartDate(), date -> date.plusDays(1)).limit(numOfDays)
                    .collect(Collectors.toList());
            allHolidaysDays.addAll(allDays);
        }
        mav.addObject("allHolidaysList", allHolidaysDays);

        List<Subject> allSubjects = subjectRepo.findAll();
        mav.addObject("allSubjects", allSubjects);
        // count all the subjects, method returns "numberOfSubjects"
        calculateNumberOfSubjects(mav, allSubjects);
        return mav;

    }

    

    /**
    * Hilfsmethode 1
    */
    public Model calculateNumberOfExams(Model model, List<Exam> listExams) {
        // NOTE: Thymeleaf isn't Velocity or Freemarker and doesn't replace expressions
        // blindly. You need the expression in an appropriate tag attribute, such as
        // <h1 data-th-text="${data}" />

        model.addAttribute("msg", "Anzahl Leistungsmessungen diese Woche: ");
        long anzahlExamen = listExams.stream().count();
        model.addAttribute("anzpr", anzahlExamen);
        // Schreibe im html: <h1 data-th-text="${anzpr}" />
        return model;
    }

    /**
     * Hilfsmethode 2
     */
    public Model calculateExamFactor(Model model, List<Exam> listExams) {

        model.addAttribute("msg2", "Insgesamt zählt das mit einem Belastungsfaktor von: ");
        double ExamFactor = listExams.stream().mapToDouble(exam -> exam.getCountingFactor()).sum();
        model.addAttribute("xFactor", ExamFactor);
        // Schreibe im html: <h1 data-th-text="${anzpr}" />
        return model;
    }

    /**
     * Hilfsmethode 3
     */
    public ModelAndView calculateNumberOfSubjects(ModelAndView mav, List<Subject> allSubjects) {
        long numberOfSubjects = allSubjects.stream().count();
        mav.addObject("numberOfSubjects", numberOfSubjects);
        return mav;
    }

    /*
     * Hilfsmethode 4
     */
    @SuppressWarnings("unused")
    private int calculateNumberOfExams(List<List<Exam>> allExams) {
    int sum = 0;
    Iterator<List<Exam>> iter = allExams.iterator();
    while (iter.hasNext()) {
    Iterator<Exam> innerIter = iter.next().iterator();
    while (innerIter.hasNext()) {
    sum++;
    }
    }
    return sum;
    }
}
