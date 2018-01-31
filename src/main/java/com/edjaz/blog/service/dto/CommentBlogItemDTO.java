package com.edjaz.blog.service.dto;


import com.edjaz.blog.domain.enumeration.CommentStatus;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the CommentBlogItem entity.
 */
public class CommentBlogItemDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 3, max = 512)
    private String text;

    @NotNull
    private ZonedDateTime created;

    private ZonedDateTime updated;

    @NotNull
    private CommentStatus status;

    private Long blogItemId;

    private Long commentBlogItemId;

    private Long authorId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ZonedDateTime getCreated() {
        return created;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    public ZonedDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(ZonedDateTime updated) {
        this.updated = updated;
    }

    public CommentStatus getStatus() {
        return status;
    }

    public void setStatus(CommentStatus status) {
        this.status = status;
    }

    public Long getBlogItemId() {
        return blogItemId;
    }

    public void setBlogItemId(Long blogItemId) {
        this.blogItemId = blogItemId;
    }

    public Long getCommentBlogItemId() {
        return commentBlogItemId;
    }

    public void setCommentBlogItemId(Long commentBlogItemId) {
        this.commentBlogItemId = commentBlogItemId;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long userId) {
        this.authorId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CommentBlogItemDTO commentBlogItemDTO = (CommentBlogItemDTO) o;
        if(commentBlogItemDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), commentBlogItemDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CommentBlogItemDTO{" +
            "id=" + getId() +
            ", text='" + getText() + "'" +
            ", created='" + getCreated() + "'" +
            ", updated='" + getUpdated() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
