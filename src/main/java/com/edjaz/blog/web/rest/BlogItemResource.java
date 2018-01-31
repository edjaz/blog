package com.edjaz.blog.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.edjaz.blog.service.BlogItemService;
import com.edjaz.blog.service.dto.BlogItemDTO;
import com.edjaz.blog.web.rest.errors.BadRequestAlertException;
import com.edjaz.blog.web.rest.util.HeaderUtil;
import com.edjaz.blog.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing BlogItem.
 */
@RestController
@RequestMapping("/api")
public class BlogItemResource {

    private final Logger log = LoggerFactory.getLogger(BlogItemResource.class);

    private static final String ENTITY_NAME = "blogItem";

    private final BlogItemService blogItemService;

    public BlogItemResource(BlogItemService blogItemService) {
        this.blogItemService = blogItemService;
    }

    /**
     * POST  /blog-items : Create a new blogItem.
     *
     * @param blogItemDTO the blogItemDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new blogItemDTO, or with status 400 (Bad Request) if the blogItem has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/blog-items")
    @Timed
    public ResponseEntity<BlogItemDTO> createBlogItem(@Valid @RequestBody BlogItemDTO blogItemDTO) throws URISyntaxException {
        log.debug("REST request to save BlogItem : {}", blogItemDTO);
        if (blogItemDTO.getId() != null) {
            throw new BadRequestAlertException("A new blogItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BlogItemDTO result = blogItemService.save(blogItemDTO);
        return ResponseEntity.created(new URI("/api/blog-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /blog-items : Updates an existing blogItem.
     *
     * @param blogItemDTO the blogItemDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated blogItemDTO,
     * or with status 400 (Bad Request) if the blogItemDTO is not valid,
     * or with status 500 (Internal Server Error) if the blogItemDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/blog-items")
    @Timed
    public ResponseEntity<BlogItemDTO> updateBlogItem(@Valid @RequestBody BlogItemDTO blogItemDTO) throws URISyntaxException {
        log.debug("REST request to update BlogItem : {}", blogItemDTO);
        if (blogItemDTO.getId() == null) {
            return createBlogItem(blogItemDTO);
        }
        BlogItemDTO result = blogItemService.save(blogItemDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, blogItemDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /blog-items : get all the blogItems.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of blogItems in body
     */
    @GetMapping("/blog-items")
    @Timed
    public ResponseEntity<List<BlogItemDTO>> getAllBlogItems(Pageable pageable) {
        log.debug("REST request to get a page of BlogItems");
        Page<BlogItemDTO> page = blogItemService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/blog-items");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /blog-items/:id : get the "id" blogItem.
     *
     * @param id the id of the blogItemDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the blogItemDTO, or with status 404 (Not Found)
     */
    @GetMapping("/blog-items/{id}")
    @Timed
    public ResponseEntity<BlogItemDTO> getBlogItem(@PathVariable Long id) {
        log.debug("REST request to get BlogItem : {}", id);
        BlogItemDTO blogItemDTO = blogItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(blogItemDTO));
    }

    /**
     * DELETE  /blog-items/:id : delete the "id" blogItem.
     *
     * @param id the id of the blogItemDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/blog-items/{id}")
    @Timed
    public ResponseEntity<Void> deleteBlogItem(@PathVariable Long id) {
        log.debug("REST request to delete BlogItem : {}", id);
        blogItemService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/blog-items?query=:query : search for the blogItem corresponding
     * to the query.
     *
     * @param query the query of the blogItem search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/blog-items")
    @Timed
    public ResponseEntity<List<BlogItemDTO>> searchBlogItems(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of BlogItems for query {}", query);
        Page<BlogItemDTO> page = blogItemService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/blog-items");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
