package ai.polito.es1;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest // instead of @SpringBootTest, subset
@AutoConfigureMockMvc
@Execution(ExecutionMode.CONCURRENT)
class Es1ApplicationTests {

//    @Autowired
//    RegistrationManager registrationManager;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private RegistrationManager service;

    // ConcurrentMap should be empty at start
    @Test
    public void testManager() {
        when(service.containsKey("John")).thenReturn(false);
    }

    @Test
    public void homePage() throws Exception {
        mockMvc.perform(get("/")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attribute("command", any(LoginCommand.class)))
                .andExpect(model().attribute("currentPageNav", is("home")))
                .andExpect(view().name("home"))
                .andExpect(content().string(containsString("Home Page")));
    }

    @Test
    public void registerGet() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(content().string(containsString("Registration")));
    }

    @Test
    public void registerPost() throws Exception {
        mockMvc.perform(post("/register")).andExpect(status().is(200))
                .andExpect(view().name("forward:/myerror"));
    }

    @Test
    void testLoginUnvalid() throws Exception {
        this.mockMvc.perform(post("/login")
//                .param("password", null)
                .param("email", ""))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));
    }

    //    @Test
    @RepeatedTest(3)
    void testRegisterPostValid() throws Exception {
        mockMvc.perform(post("/register")
                .param("name", "Jimmy")
                .param("surname", "Buffett")
                .param("privacy", "true")
                .param("email", "Jimmy@hotmail.com")
                .param("password1", "3151231234")
                .param("password2", "3151231234"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/private"));
    }

    @Test
    void testRegisterPostNotValid() throws Exception {
        mockMvc.perform(post("/register")
                .param("name", "")
                .param("surname", "Buffett")
                .param("privacy", "false")
                .param("email", "Jihtmail.com")
                .param("password1", "3")
                .param("password2", "3151231234"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeHasErrors("command"))
                .andExpect(model().attributeHasFieldErrors("command", "name"))
                .andExpect(model().attributeHasFieldErrors("command", "privacy"))
                .andExpect(model().attributeHasFieldErrors("command", "email"))
                .andExpect(model().attributeHasFieldErrors("command", "password1"))
                .andExpect(model().attributeHasFieldErrors("command", "form"))
                .andExpect(view().name("register"));
    }

    @Test
    public void privateTest() throws Exception {
        mockMvc.perform(get("/private"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));
    }

    @Test
    void test_indevelopment() throws Exception {
        mockMvc.perform(post("/register")
                .param("name", "Jimmy")
                .param("surname", "Buffett")
                .param("privacy", "true")
                .param("email", "Jimmy@hotmail.com")
                .param("password1", "3151231234")
                .param("password2", "3151231234"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/private"));
//        ConcurrentHashMap<String, RegistrationDetails> c = new ConcurrentHashMap();
//        RegistrationDetails r = RegistrationDetails.builder().email("John@h.com").name("nam").password("pass").surname("surn").build();
//        c.put("John@h.com",r);
/*        boolean test = registrationManager.containsKey("Jimmy@hotmail.com");
        assertThat(test).isTrue();*/
    }
}
