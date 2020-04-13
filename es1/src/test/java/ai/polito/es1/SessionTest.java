package ai.polito.es1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.mockito.InjectMocks;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.SharedHttpSessionConfigurer.sharedHttpSession;

public class SessionTest {
    @InjectMocks
    RegistrationManager registrationManager;
    private MockMvc mockMvc;
    /*@Autowired
    @InjectMocks
    private HomeController hm;
    @Autowired
    HomeController homeController;
    @BeforeEach
    void setup(WebApplicationContext wac) {
        //MockitoAnnotations.initMocks(this);
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new HomeController())
                .apply(sharedHttpSession())
                .build();
    }*/

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new HomeController(registrationManager))
                .apply(sharedHttpSession())
                .build();
    }

    @RepeatedTest(1)
    public void homePage() throws Exception {
        mockMvc.perform(get("/")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attribute("command", any(LoginCommand.class)))
                .andExpect(model().attribute("currentPageNav", is("home")))
                .andExpect(view().name("home"));
    }
}
