package com.edjaz.blog.service.dto;


import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import javax.persistence.Lob;
import com.edjaz.blog.domain.enumeration.ItemStatus;
import com.edjaz.blog.domain.enumeration.ItemVisibility;

/**
 * A DTO for the BlogItem entity.
 */
public class BlogItemDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 3, max = 60)
    private String title;

    @NotNull
    @Size(min = 3, max = 30)
    @Pattern(regexp = "[A-Z]+")
    private String url;

    @NotNull
    @Lob
    private String text;

    @NotNull
    private ItemStatus staus;

    @NotNull
    private ZonedDateTime created;

    private ZonedDateTime updated;

    @Lob
    private byte[] image;
    private String imageContentType;

    private ItemVisibility visiblity;

    private String password;

    private Long blogId;

    private Long blogItemId;

    private Long authorId;

    private Set<TagDTO> tags = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ItemStatus getStaus() {
        return staus;
    }

    public void setStaus(ItemStatus staus) {
        this.staus = staus;
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

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public ItemVisibility getVisiblity() {
        return visiblity;
    }

    public void setVisiblity(ItemVisibility visiblity) {
        this.visiblity = visiblity;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getBlogId() {
        return blogId;
    }

    public void setBlogId(Long blogId) {
        this.blogId = blogId;
    }

    public Long getBlogItemId() {
        return blogItemId;
    }

    public void setBlogItemId(Long blogItemId) {
        this.blogItemId = blogItemId;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long userId) {
        this.authorId = userId;
    }

    public Set<TagDTO> getTags() {
        return tags;
    }

    public void setTags(Set<TagDTO> tags) {
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BlogItemDTO blogItemDTO = (BlogItemDTO) o;
        if(blogItemDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), blogItemDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BlogItemDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", url='" + getUrl() + "'" +
            ", text='" + getText() + "'" +
            ", staus='" + getStaus() + "'" +
            ", created='" + getCreated() + "'" +
            ", updated='" + getUpdated() + "'" +
            ", image='" + getImage() + "'" +
            ", visiblity='" + getVisiblity() + "'" +
            "}";
    }
}
