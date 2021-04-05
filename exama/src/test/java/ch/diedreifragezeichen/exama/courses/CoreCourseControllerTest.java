package ch.diedreifragezeichen.exama.courses;

import java.util.Arrays;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.plugins.MockMaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import ch.diedreifragezeichen.exama.users.RoleRepository;
import ch.diedreifragezeichen.exama.users.UserRepository;

@ExtendWith(SpringExtension.class)
@WebMvcTest
public class CoreCourseControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CoreCourseRepository repo;
    @MockBean
    private UserRepository userRepo;
    @MockBean
    private RoleRepository roleRepo;
    @MockBean
    private EntityManager em;

    @Test
    void testGetCoreCourses() throws Exception{

        CoreCourse cc1 = new CoreCourse();
        cc1.setName("Kosta");
        cc1.setId(1L);
        cc1.setEnabled(true);

        CoreCourse cc2 = new CoreCourse();
        cc2.setName("Jonas");
        cc2.setId(2L);
        cc2.setEnabled(false);

        Mockito.when(repo.findAll()).thenReturn(Arrays.asList(cc1,cc2));

        mockMvc.perform(MockMvcRequestBuilders.get("/coreCourses/show")
            .contentType("adminTemplates/coreCoursesShow"))
            .andExpect(MockMvcResultMatchers.status().isOk());

    }
}
