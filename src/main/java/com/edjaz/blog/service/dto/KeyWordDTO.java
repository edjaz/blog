package com.edjaz.blog.service.dto;


import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the KeyWord entity.
 */
public class KeyWordDTO implements Serializable {

    private Long id;

    @NotNull
    private String word;

    private Long blogItemId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Long getBlogItemId() {
        return blogItemId;
    }

    public void setBlogItemId(Long blogItemId) {
        this.blogItemId = blogItemId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        KeyWordDTO keyWordDTO = (KeyWordDTO) o;
        if(keyWordDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), keyWordDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "KeyWordDTO{" +
            "id=" + getId() +
            ", word='" + getWord() + "'" +
            "}";
    }
}
