package ch.diedreifragezeichen.exama.users;

import java.util.Collection;
import java.util.Set;
import java.util.List;
import java.time.LocalDate;
import java.util.ArrayList;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import ch.diedreifragezeichen.exama.assignments.exams.Exam;
import ch.diedreifragezeichen.exama.courses.CoreCourse;
import ch.diedreifragezeichen.exama.courses.Course;

@SuppressWarnings("serial")
public class UserDetailsExama implements UserDetails {

    private User user;

    public UserDetailsExama(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<Role> userRoles = user.getRoles();
        List<SimpleGrantedAuthority> userAuthorities = new ArrayList<>();
        for (Role role : userRoles) {
            userAuthorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return userAuthorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }

    public Long getId() {
        return user.getId();
    }

    public String getEmail() {
        return user.getEmail();
    }

    public Set<Role> getRoles() {
        return user.getRoles();
    }

    public String getFullName() {
        return user.getFirstName() + " " + user.getLastName();
    }

    public String getFirstName() {
        return user.getFirstName();
    }

    public String getLastName() {
        return user.getLastName();
    }

    public Set<Course> getCourses() {
        return user.getCourses();
    }

    public String getAbbreviation() {
        return user.getAbbreviation();
    }

    public boolean isLoggedIn() {
        return user.isLoggedIn();
    }

    public LocalDate getLastLogin() {
        return user.getLastLogin();
    }

    public CoreCourse getCoreCourse() {
        return user.getCoreCourse();
    }

    public List<Exam> getExams() {
        return user.getExams();
    }

    public List<CoreCourse> getClassTeacherCourses() {
        return user.getClassTeacherCourses();
    }

}
