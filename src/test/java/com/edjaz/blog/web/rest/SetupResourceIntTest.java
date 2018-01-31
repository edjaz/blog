package com.edjaz.blog.web.rest;


import com.edjaz.blog.BlogApp;
import com.edjaz.blog.domain.Authority;
import com.edjaz.blog.domain.Blog;
import com.edjaz.blog.domain.User;
import com.edjaz.blog.repository.BlogRepository;
import com.edjaz.blog.repository.UserRepository;
import com.edjaz.blog.repository.search.BlogSearchRepository;
import com.edjaz.blog.security.AuthoritiesConstants;
import com.edjaz.blog.web.rest.vm.ManagedUserVM;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.persistence.EntityManager;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.edjaz.blog.web.rest.BlogResourceIntTest.createEntity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BlogApp.class)
@WithMockUser
public class SetupResourceIntTest {

    @Autowired
    private EntityManager em;

    private MockMvc restSetupMockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private BlogSearchRepository blogSearchRepository;

    @Autowired
    private WebApplicationContext context;

    private Blog blog;


    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.restSetupMockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Before
    public void initTest() {
        blogSearchRepository.deleteAll();
        blog = createEntity(em);
    }

    @Test
    @WithAnonymousUser
    public void should_return_true_when_no_blog_and_has_anonymous() throws Exception {
        restSetupMockMvc.perform(get("/api/setup"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
    }

    private static final String DEFAULT_ADMIN_USER_LOGIN = "admin";
    private static final String DEFAULT_LANGKEY = "en";
    private static final String DEFAULT_IMAGEURL = "http://placehold.it/50x50";
    private static final String DEFAULT_EMAIL = "johndoe@localhost";


    public ManagedUserVM createDefaultAdminUser() {

        // Create the User
        ManagedUserVM managedUserVM = new ManagedUserVM();
        managedUserVM.setLogin("login");
        managedUserVM.setPassword("a123456");
        managedUserVM.setFirstName("firstname");
        managedUserVM.setLastName("lastname");
        managedUserVM.setEmail(RandomStringUtils.randomAlphabetic(5) + DEFAULT_EMAIL);
        managedUserVM.setActivated(true);
        managedUserVM.setImageUrl(DEFAULT_IMAGEURL);
        managedUserVM.setLangKey(DEFAULT_LANGKEY);
        managedUserVM.setAuthorities(Stream.of(AuthoritiesConstants.ADMIN, AuthoritiesConstants.USER).collect(Collectors.toSet()));
        managedUserVM.setPassword(RandomStringUtils.random(60));

        return managedUserVM;
    }

    @Test
    @WithAnonymousUser
    public void should_return_id_admin_when_no_blog_and_has_anonymous() throws Exception {
        blogRepository.deleteAll();
        Long id = userRepository.findOneByLogin("admin")
            .orElseThrow(() -> new UsernameNotFoundException("User admin not found"))
            .getId();
        restSetupMockMvc.perform(get("/api/setup/user"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string(id.toString()))
        ;
    }

    @Test
    @WithAnonymousUser
    public void should_return_forbiden_when_has_blog_has_anonymous() throws Exception {
        blog = createEntity(em);
        blogRepository.deleteAll();
        blogRepository.saveAndFlush(blog);
        restSetupMockMvc.perform(get("/api/setup"))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    public void should_id_admin_return_forbiden_when_has_blog_has_anonymous() throws Exception {
        blog = createEntity(em);
        blogRepository.deleteAll();
        blogRepository.saveAndFlush(blog);
        restSetupMockMvc.perform(get("/api/setup/user"))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    public void should_return_forbiden_when_has_blog_has_any_role() throws Exception {
        blog = createEntity(em);
        blogRepository.deleteAll();
        blogRepository.saveAndFlush(blog);
        restSetupMockMvc.perform(get("/api/setup"))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void should_return_forbiden_when_has_blog_has_admin() throws Exception {
        blog = createEntity(em);
        blogRepository.deleteAll();
        blogRepository.saveAndFlush(blog);
        restSetupMockMvc.perform(get("/api/setup"))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void should_id_admin_return_forbiden_when_has_blog_has_admin() throws Exception {
        blog = createEntity(em);
        blogRepository.deleteAll();
        blogRepository.saveAndFlush(blog);
        restSetupMockMvc.perform(get("/api/setup/user"))
            .andExpect(status().isForbidden());
    }



    @Test
    @WithMockUser(username = "user", roles = {"USER","ADMIN"})
    public void should_replace_admin_return_forbiden_when_has_blog_has_admin() throws Exception {
        blog = createEntity(em);
        blogRepository.deleteAll();
        blogRepository.saveAndFlush(blog);

        ManagedUserVM user = createDefaultAdminUser();
        user.setId(1L);

        restSetupMockMvc.perform(put("/api/setup/user")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(user)))
            .andExpect(status().isForbidden());
    }



    @Test
    @WithAnonymousUser
    public void should_replace_admin_when_has_no_blog_and_is_anonymous() throws Exception {
        blogRepository.deleteAll();
        ManagedUserVM user = createDefaultAdminUser();
        User dbAdmin = userRepository.findOneByLogin("admin").orElseThrow(() -> new UsernameNotFoundException("No admin"));
        user.setId(dbAdmin.getId());

        restSetupMockMvc.perform(put("/api/setup/user")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(user)))
            .andExpect(status().isOk());


        userRepository.findOneByLogin("admin").ifPresent((u) -> {
            throw new RuntimeException("admin is present");
        });

        User testUser = userRepository.findOneWithAuthoritiesByLogin(user.getLogin()).orElseThrow(() -> new UsernameNotFoundException("New user not found"));

        assertThat(testUser.getLogin()).isEqualTo(user.getLogin());
        assertThat(testUser.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(testUser.getLastName()).isEqualTo(user.getLastName());
        assertThat(testUser.getEmail()).isEqualTo(user.getEmail());
        assertThat(testUser.getImageUrl()).isEqualTo(DEFAULT_IMAGEURL);
        assertThat(testUser.getLangKey()).isEqualTo(DEFAULT_LANGKEY);
        assertThat(testUser.getAuthorities().stream().map(Authority::getName)).contains(AuthoritiesConstants.ADMIN, AuthoritiesConstants.ADMIN);
    }


}
