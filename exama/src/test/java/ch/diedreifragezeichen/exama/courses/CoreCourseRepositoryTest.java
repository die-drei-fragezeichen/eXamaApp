package ch.diedreifragezeichen.exama.courses;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class CoreCourseRepositoryTest {
    
    @Autowired
    CoreCourseRepository underTest;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void canFindCoreCourseById(){
        //given
        Long id = 1L;
        CoreCourse coreCourse = new CoreCourse();
        coreCourse.setName("Kosta");
        coreCourse.setId(id);
        coreCourse.setEnabled(true);
        underTest.save(coreCourse);
    
        //when
        CoreCourse expected = underTest.findCoreCourseById(id);

        //then
        assertThat(expected).isEqualTo(coreCourse);
    }

    @Test
    void cannotFindCoreCourseById(){
          //given
          Long id = 1L;
      
          //when
          CoreCourse expected = underTest.findCoreCourseById(id);
  
          //then
          assertThat(expected).isNull();
    }


}
