package ch.diedreifragezeichen.exama;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import ch.diedreifragezeichen.exama.courses.CoreCourse;
import ch.diedreifragezeichen.exama.users.User;


public class CoreCourseTest {
    
@Test
public void testAllMethods(){
    CoreCourse testCourse= new CoreCourse();
    testCourse.setId((long)1);
    // testCourse.name="nametest";
    // testCourse.enabled=true;
    // testCourse.classTeacher=new User();
    assertEquals((long)1, testCourse.getId());
}

}