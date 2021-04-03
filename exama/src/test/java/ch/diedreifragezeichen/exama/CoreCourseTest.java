package ch.diedreifragezeichen.exama;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import ch.diedreifragezeichen.exama.courses.CoreCourse;
import ch.diedreifragezeichen.exama.users.User;


public class CoreCourseTest {
  
@Test
public void testGetSetId(){
    CoreCourse testCourse= new CoreCourse();
    testCourse.setId((long)1);
    assertEquals((long)1, testCourse.getId());
}

@Test
public void testGetSetName(){
    CoreCourse testCourse= new CoreCourse();
    testCourse.setName("nametest");
    assertEquals("nametest", testCourse.getName());
}

@Test
public void testIsSetEnabled(){
    CoreCourse testCourse= new CoreCourse();
    testCourse.setEnabled(true);
    assertEquals(true, testCourse.isEnabled());
}

@Test
public void testGetSetClassTeacher(){
    CoreCourse testCourse= new CoreCourse();
    User user=new User();
    testCourse.setClassTeacher(user);
    assertEquals(user, testCourse.getClassTeacher());
}

@Test
public void testGetSetStudents(){
    CoreCourse testCourse= new CoreCourse();
    ArrayList<User> liste=new ArrayList<User>();
    testCourse.setStudents(liste);
    assertEquals(liste, testCourse.getStudents());
}
}