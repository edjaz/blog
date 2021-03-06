package com.edjaz.blog.web.rest;

import com.edjaz.blog.BlogApp;

import com.edjaz.blog.domain.BlogItem;
import com.edjaz.blog.repository.BlogItemRepository;
import com.edjaz.blog.service.BlogItemService;
import com.edjaz.blog.repository.search.BlogItemSearchRepository;
import com.edjaz.blog.service.dto.BlogItemDTO;
import com.edjaz.blog.service.mapper.BlogItemMapper;
import com.edjaz.blog.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.edjaz.blog.web.rest.TestUtil.sameInstant;
import static com.edjaz.blog.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.edjaz.blog.domain.enumeration.ItemStatus;
import com.edjaz.blog.domain.enumeration.ItemVisibility;
/**
 * Test class for the BlogItemResource REST controller.
 *
 * @see BlogItemResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BlogApp.class)
public class BlogItemResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final String DEFAULT_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_TEXT = "BBBBBBBBBB";

    private static final ItemStatus DEFAULT_STAUS = ItemStatus.PUBLISHED;
    private static final ItemStatus UPDATED_STAUS = ItemStatus.TRASH;

    private static final ZonedDateTime DEFAULT_CREATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_UPDATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final ItemVisibility DEFAULT_VISIBLITY = ItemVisibility.PUBLIC;
    private static final ItemVisibility UPDATED_VISIBLITY = ItemVisibility.PRIVATE;

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    @Autowired
    private BlogItemRepository blogItemRepository;

    @Autowired
    private BlogItemMapper blogItemMapper;

    @Autowired
    private BlogItemService blogItemService;

    @Autowired
    private BlogItemSearchRepository blogItemSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBlogItemMockMvc;

    private BlogItem blogItem;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BlogItemResource blogItemResource = new BlogItemResource(blogItemService);
        this.restBlogItemMockMvc = MockMvcBuilders.standaloneSetup(blogItemResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BlogItem createEntity(EntityManager em) {
        BlogItem blogItem = new BlogItem()
            .title(DEFAULT_TITLE)
            .url(DEFAULT_URL)
            .text(DEFAULT_TEXT)
            .staus(DEFAULT_STAUS)
            .created(DEFAULT_CREATED)
            .updated(DEFAULT_UPDATED)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE)
            .visiblity(DEFAULT_VISIBLITY)
            .password(DEFAULT_PASSWORD);
        return blogItem;
    }

    @Before
    public void initTest() {
        blogItemSearchRepository.deleteAll();
        blogItem = createEntity(em);
    }

    @Test
    @Transactional
    public void createBlogItem() throws Exception {
        int databaseSizeBeforeCreate = blogItemRepository.findAll().size();

        // Create the BlogItem
        BlogItemDTO blogItemDTO = blogItemMapper.toDto(blogItem);
        restBlogItemMockMvc.perform(post("/api/blog-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(blogItemDTO)))
            .andExpect(status().isCreated());

        // Validate the BlogItem in the database
        List<BlogItem> blogItemList = blogItemRepository.findAll();
        assertThat(blogItemList).hasSize(databaseSizeBeforeCreate + 1);
        BlogItem testBlogItem = blogItemList.get(blogItemList.size() - 1);
        assertThat(testBlogItem.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testBlogItem.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testBlogItem.getText()).isEqualTo(DEFAULT_TEXT);
        assertThat(testBlogItem.getStaus()).isEqualTo(DEFAULT_STAUS);
        assertThat(testBlogItem.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testBlogItem.getUpdated()).isEqualTo(DEFAULT_UPDATED);
        assertThat(testBlogItem.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testBlogItem.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testBlogItem.getVisiblity()).isEqualTo(DEFAULT_VISIBLITY);
        assertThat(testBlogItem.getPassword()).isEqualTo(DEFAULT_PASSWORD);

        // Validate the BlogItem in Elasticsearch
        BlogItem blogItemEs = blogItemSearchRepository.findOne(testBlogItem.getId());
        assertThat(testBlogItem.getCreated()).isEqualTo(testBlogItem.getCreated());
        assertThat(testBlogItem.getUpdated()).isEqualTo(testBlogItem.getUpdated());
        assertThat(blogItemEs).isEqualToIgnoringGivenFields(testBlogItem, "created", "updated");
    }

    @Test
    @Transactional
    public void createBlogItemWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = blogItemRepository.findAll().size();

        // Create the BlogItem with an existing ID
        blogItem.setId(1L);
        BlogItemDTO blogItemDTO = blogItemMapper.toDto(blogItem);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBlogItemMockMvc.perform(post("/api/blog-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(blogItemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BlogItem in the database
        List<BlogItem> blogItemList = blogItemRepository.findAll();
        assertThat(blogItemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = blogItemRepository.findAll().size();
        // set the field null
        blogItem.setTitle(null);

        // Create the BlogItem, which fails.
        BlogItemDTO blogItemDTO = blogItemMapper.toDto(blogItem);

        restBlogItemMockMvc.perform(post("/api/blog-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(blogItemDTO)))
            .andExpect(status().isBadRequest());

        List<BlogItem> blogItemList = blogItemRepository.findAll();
        assertThat(blogItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUrlIsRequired() throws Exception {
        int databaseSizeBeforeTest = blogItemRepository.findAll().size();
        // set the field null
        blogItem.setUrl(null);

        // Create the BlogItem, which fails.
        BlogItemDTO blogItemDTO = blogItemMapper.toDto(blogItem);

        restBlogItemMockMvc.perform(post("/api/blog-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(blogItemDTO)))
            .andExpect(status().isBadRequest());

        List<BlogItem> blogItemList = blogItemRepository.findAll();
        assertThat(blogItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTextIsRequired() throws Exception {
        int databaseSizeBeforeTest = blogItemRepository.findAll().size();
        // set the field null
        blogItem.setText(null);

        // Create the BlogItem, which fails.
        BlogItemDTO blogItemDTO = blogItemMapper.toDto(blogItem);

        restBlogItemMockMvc.perform(post("/api/blog-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(blogItemDTO)))
            .andExpect(status().isBadRequest());

        List<BlogItem> blogItemList = blogItemRepository.findAll();
        assertThat(blogItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStausIsRequired() throws Exception {
        int databaseSizeBeforeTest = blogItemRepository.findAll().size();
        // set the field null
        blogItem.setStaus(null);

        // Create the BlogItem, which fails.
        BlogItemDTO blogItemDTO = blogItemMapper.toDto(blogItem);

        restBlogItemMockMvc.perform(post("/api/blog-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(blogItemDTO)))
            .andExpect(status().isBadRequest());

        List<BlogItem> blogItemList = blogItemRepository.findAll();
        assertThat(blogItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreatedIsRequired() throws Exception {
        int databaseSizeBeforeTest = blogItemRepository.findAll().size();
        // set the field null
        blogItem.setCreated(null);

        // Create the BlogItem, which fails.
        BlogItemDTO blogItemDTO = blogItemMapper.toDto(blogItem);

        restBlogItemMockMvc.perform(post("/api/blog-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(blogItemDTO)))
            .andExpect(status().isBadRequest());

        List<BlogItem> blogItemList = blogItemRepository.findAll();
        assertThat(blogItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBlogItems() throws Exception {
        // Initialize the database
        blogItemRepository.saveAndFlush(blogItem);

        // Get all the blogItemList
        restBlogItemMockMvc.perform(get("/api/blog-items?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(blogItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT.toString())))
            .andExpect(jsonPath("$.[*].staus").value(hasItem(DEFAULT_STAUS.toString())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(sameInstant(DEFAULT_CREATED))))
            .andExpect(jsonPath("$.[*].updated").value(hasItem(sameInstant(DEFAULT_UPDATED))))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].visiblity").value(hasItem(DEFAULT_VISIBLITY.toString())))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD.toString())));
    }

    @Test
    @Transactional
    public void getBlogItem() throws Exception {
        // Initialize the database
        blogItemRepository.saveAndFlush(blogItem);

        // Get the blogItem
        restBlogItemMockMvc.perform(get("/api/blog-items/{id}", blogItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(blogItem.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()))
            .andExpect(jsonPath("$.text").value(DEFAULT_TEXT.toString()))
            .andExpect(jsonPath("$.staus").value(DEFAULT_STAUS.toString()))
            .andExpect(jsonPath("$.created").value(sameInstant(DEFAULT_CREATED)))
            .andExpect(jsonPath("$.updated").value(sameInstant(DEFAULT_UPDATED)))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.visiblity").value(DEFAULT_VISIBLITY.toString()))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBlogItem() throws Exception {
        // Get the blogItem
        restBlogItemMockMvc.perform(get("/api/blog-items/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBlogItem() throws Exception {
        // Initialize the database
        blogItemRepository.saveAndFlush(blogItem);
        blogItemSearchRepository.save(blogItem);
        int databaseSizeBeforeUpdate = blogItemRepository.findAll().size();

        // Update the blogItem
        BlogItem updatedBlogItem = blogItemRepository.findOne(blogItem.getId());
        // Disconnect from session so that the updates on updatedBlogItem are not directly saved in db
        em.detach(updatedBlogItem);
        updatedBlogItem
            .title(UPDATED_TITLE)
            .url(UPDATED_URL)
            .text(UPDATED_TEXT)
            .staus(UPDATED_STAUS)
            .created(UPDATED_CREATED)
            .updated(UPDATED_UPDATED)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .visiblity(UPDATED_VISIBLITY)
            .password(UPDATED_PASSWORD);
        BlogItemDTO blogItemDTO = blogItemMapper.toDto(updatedBlogItem);

        restBlogItemMockMvc.perform(put("/api/blog-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(blogItemDTO)))
            .andExpect(status().isOk());

        // Validate the BlogItem in the database
        List<BlogItem> blogItemList = blogItemRepository.findAll();
        assertThat(blogItemList).hasSize(databaseSizeBeforeUpdate);
        BlogItem testBlogItem = blogItemList.get(blogItemList.size() - 1);
        assertThat(testBlogItem.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testBlogItem.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testBlogItem.getText()).isEqualTo(UPDATED_TEXT);
        assertThat(testBlogItem.getStaus()).isEqualTo(UPDATED_STAUS);
        assertThat(testBlogItem.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testBlogItem.getUpdated()).isEqualTo(UPDATED_UPDATED);
        assertThat(testBlogItem.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testBlogItem.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testBlogItem.getVisiblity()).isEqualTo(UPDATED_VISIBLITY);
        assertThat(testBlogItem.getPassword()).isEqualTo(UPDATED_PASSWORD);

        // Validate the BlogItem in Elasticsearch
        BlogItem blogItemEs = blogItemSearchRepository.findOne(testBlogItem.getId());
        assertThat(testBlogItem.getCreated()).isEqualTo(testBlogItem.getCreated());
        assertThat(testBlogItem.getUpdated()).isEqualTo(testBlogItem.getUpdated());
        assertThat(blogItemEs).isEqualToIgnoringGivenFields(testBlogItem, "created", "updated");
    }

    @Test
    @Transactional
    public void updateNonExistingBlogItem() throws Exception {
        int databaseSizeBeforeUpdate = blogItemRepository.findAll().size();

        // Create the BlogItem
        BlogItemDTO blogItemDTO = blogItemMapper.toDto(blogItem);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restBlogItemMockMvc.perform(put("/api/blog-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(blogItemDTO)))
            .andExpect(status().isCreated());

        // Validate the BlogItem in the database
        List<BlogItem> blogItemList = blogItemRepository.findAll();
        assertThat(blogItemList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteBlogItem() throws Exception {
        // Initialize the database
        blogItemRepository.saveAndFlush(blogItem);
        blogItemSearchRepository.save(blogItem);
        int databaseSizeBeforeDelete = blogItemRepository.findAll().size();

        // Get the blogItem
        restBlogItemMockMvc.perform(delete("/api/blog-items/{id}", blogItem.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean blogItemExistsInEs = blogItemSearchRepository.exists(blogItem.getId());
        assertThat(blogItemExistsInEs).isFalse();

        // Validate the database is empty
        List<BlogItem> blogItemList = blogItemRepository.findAll();
        assertThat(blogItemList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchBlogItem() throws Exception {
        // Initialize the database
        blogItemRepository.saveAndFlush(blogItem);
        blogItemSearchRepository.save(blogItem);

        // Search the blogItem
        restBlogItemMockMvc.perform(get("/api/_search/blog-items?query=id:" + blogItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(blogItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT.toString())))
            .andExpect(jsonPath("$.[*].staus").value(hasItem(DEFAULT_STAUS.toString())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(sameInstant(DEFAULT_CREATED))))
            .andExpect(jsonPath("$.[*].updated").value(hasItem(sameInstant(DEFAULT_UPDATED))))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].visiblity").value(hasItem(DEFAULT_VISIBLITY.toString())))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BlogItem.class);
        BlogItem blogItem1 = new BlogItem();
        blogItem1.setId(1L);
        BlogItem blogItem2 = new BlogItem();
        blogItem2.setId(blogItem1.getId());
        assertThat(blogItem1).isEqualTo(blogItem2);
        blogItem2.setId(2L);
        assertThat(blogItem1).isNotEqualTo(blogItem2);
        blogItem1.setId(null);
        assertThat(blogItem1).isNotEqualTo(blogItem2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BlogItemDTO.class);
        BlogItemDTO blogItemDTO1 = new BlogItemDTO();
        blogItemDTO1.setId(1L);
        BlogItemDTO blogItemDTO2 = new BlogItemDTO();
        assertThat(blogItemDTO1).isNotEqualTo(blogItemDTO2);
        blogItemDTO2.setId(blogItemDTO1.getId());
        assertThat(blogItemDTO1).isEqualTo(blogItemDTO2);
        blogItemDTO2.setId(2L);
        assertThat(blogItemDTO1).isNotEqualTo(blogItemDTO2);
        blogItemDTO1.setId(null);
        assertThat(blogItemDTO1).isNotEqualTo(blogItemDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(blogItemMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(blogItemMapper.fromId(null)).isNull();
    }
}
