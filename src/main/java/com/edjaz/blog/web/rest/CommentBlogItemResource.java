package com.edjaz.blog.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.edjaz.blog.service.CommentBlogItemService;
import com.edjaz.blog.service.dto.CommentBlogItemDTO;
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
 * REST controller for managing CommentBlogItem.
 */
@RestController
@RequestMapping("/api")
public class CommentBlogItemResource {

    private final Logger log = LoggerFactory.getLogger(CommentBlogItemResource.class);

    private static final String ENTITY_NAME = "commentBlogItem";

    private final CommentBlogItemService commentBlogItemService;

    public CommentBlogItemResource(CommentBlogItemService commentBlogItemService) {
        this.commentBlogItemService = commentBlogItemService;
    }

    /**
     * POST  /comment-blog-items : Create a new commentBlogItem.
     *
     * @param commentBlogItemDTO the commentBlogItemDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new commentBlogItemDTO, or with status 400 (Bad Request) if the commentBlogItem has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/comment-blog-items")
    @Timed
    public ResponseEntity<CommentBlogItemDTO> createCommentBlogItem(@Valid @RequestBody CommentBlogItemDTO commentBlogItemDTO) throws URISyntaxException {
        log.debug("REST request to save CommentBlogItem : {}", commentBlogItemDTO);
        if (commentBlogItemDTO.getId() != null) {
            throw new BadRequestAlertException("A new commentBlogItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CommentBlogItemDTO result = commentBlogItemService.save(commentBlogItemDTO);
        return ResponseEntity.created(new URI("/api/comment-blog-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /comment-blog-items : Updates an existing commentBlogItem.
     *
     * @param commentBlogItemDTO the commentBlogItemDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated commentBlogItemDTO,
     * or with status 400 (Bad Request) if the commentBlogItemDTO is not valid,
     * or with status 500 (Internal Server Error) if the commentBlogItemDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/comment-blog-items")
    @Timed
    public ResponseEntity<CommentBlogItemDTO> updateCommentBlogItem(@Valid @RequestBody CommentBlogItemDTO commentBlogItemDTO) throws URISyntaxException {
        log.debug("REST request to update CommentBlogItem : {}", commentBlogItemDTO);
        if (commentBlogItemDTO.getId() == null) {
            return createCommentBlogItem(commentBlogItemDTO);
        }
        CommentBlogItemDTO result = commentBlogItemService.save(commentBlogItemDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, commentBlogItemDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /comment-blog-items : get all the commentBlogItems.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of commentBlogItems in body
     */
    @GetMapping("/comment-blog-items")
    @Timed
    public ResponseEntity<List<CommentBlogItemDTO>> getAllCommentBlogItems(Pageable pageable) {
        log.debug("REST request to get a page of CommentBlogItems");
        Page<CommentBlogItemDTO> page = commentBlogItemService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/comment-blog-items");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /comment-blog-items/:id : get the "id" commentBlogItem.
     *
     * @param id the id of the commentBlogItemDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the commentBlogItemDTO, or with status 404 (Not Found)
     */
    @GetMapping("/comment-blog-items/{id}")
    @Timed
    public ResponseEntity<CommentBlogItemDTO> getCommentBlogItem(@PathVariable Long id) {
        log.debug("REST request to get CommentBlogItem : {}", id);
        CommentBlogItemDTO commentBlogItemDTO = commentBlogItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(commentBlogItemDTO));
    }

    /**
     * DELETE  /comment-blog-items/:id : delete the "id" commentBlogItem.
     *
     * @param id the id of the commentBlogItemDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/comment-blog-items/{id}")
    @Timed
    public ResponseEntity<Void> deleteCommentBlogItem(@PathVariable Long id) {
        log.debug("REST request to delete CommentBlogItem : {}", id);
        commentBlogItemService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/comment-blog-items?query=:query : search for the commentBlogItem corresponding
     * to the query.
     *
     * @param query the query of the commentBlogItem search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/comment-blog-items")
    @Timed
    public ResponseEntity<List<CommentBlogItemDTO>> searchCommentBlogItems(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of CommentBlogItems for query {}", query);
        Page<CommentBlogItemDTO> page = commentBlogItemService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/comment-blog-items");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
