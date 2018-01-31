package com.edjaz.blog.service.dto;


import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Blog entity.
 */
public class BlogDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 3, max = 60)
    private String title;

    @NotNull
    @Size(max = 1024)
    private String descritpion;

    @Lob
    private byte[] image;
    private String imageContentType;

    private Long authorId;

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

    public String getDescritpion() {
        return descritpion;
    }

    public void setDescritpion(String descritpion) {
        this.descritpion = descritpion;
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

        BlogDTO blogDTO = (BlogDTO) o;
        if(blogDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), blogDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BlogDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", descritpion='" + getDescritpion() + "'" +
            ", image='" + getImage() + "'" +
            "}";
    }
}
