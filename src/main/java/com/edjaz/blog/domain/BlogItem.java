package com.edjaz.blog.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.edjaz.blog.domain.enumeration.ItemStatus;

import com.edjaz.blog.domain.enumeration.ItemVisibility;

/**
 * A BlogItem.
 */
@Entity
@Table(name = "blog_item")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "blogitem")
public class BlogItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(min = 3, max = 60)
    @Column(name = "title", length = 60, nullable = false)
    private String title;

    @NotNull
    @Size(min = 3, max = 30)
    @Pattern(regexp = "[A-Z]+")
    @Column(name = "url", length = 30, nullable = false)
    private String url;

    @NotNull
    @Lob
    @Column(name = "text", nullable = false)
    private String text;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "staus", nullable = false)
    private ItemStatus staus;

    @NotNull
    @Column(name = "created", nullable = false)
    private ZonedDateTime created;

    @Column(name = "updated")
    private ZonedDateTime updated;

    @Lob
    @Column(name = "image")
    private byte[] image;

    @Column(name = "image_content_type")
    private String imageContentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "visiblity")
    private ItemVisibility visiblity;

    @Column(name = "jhi_password")
    private String password;

    @ManyToOne
    private Blog blog;

    @OneToMany(mappedBy = "blogItem")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<CommentBlogItem> comments = new HashSet<>();

    @OneToMany(mappedBy = "blogItem")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<KeyWord> keywords = new HashSet<>();

    @ManyToOne
    private BlogItem blogItem;

    @OneToMany(mappedBy = "blogItem")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<BlogItem> histories = new HashSet<>();

    @ManyToOne
    private User author;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "blog_item_tags",
               joinColumns = @JoinColumn(name="blog_items_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="tags_id", referencedColumnName="id"))
    private Set<Tag> tags = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public BlogItem title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public BlogItem url(String url) {
        this.url = url;
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getText() {
        return text;
    }

    public BlogItem text(String text) {
        this.text = text;
        return this;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ItemStatus getStaus() {
        return staus;
    }

    public BlogItem staus(ItemStatus staus) {
        this.staus = staus;
        return this;
    }

    public void setStaus(ItemStatus staus) {
        this.staus = staus;
    }

    public ZonedDateTime getCreated() {
        return created;
    }

    public BlogItem created(ZonedDateTime created) {
        this.created = created;
        return this;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    public ZonedDateTime getUpdated() {
        return updated;
    }

    public BlogItem updated(ZonedDateTime updated) {
        this.updated = updated;
        return this;
    }

    public void setUpdated(ZonedDateTime updated) {
        this.updated = updated;
    }

    public byte[] getImage() {
        return image;
    }

    public BlogItem image(byte[] image) {
        this.image = image;
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public BlogItem imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public ItemVisibility getVisiblity() {
        return visiblity;
    }

    public BlogItem visiblity(ItemVisibility visiblity) {
        this.visiblity = visiblity;
        return this;
    }

    public void setVisiblity(ItemVisibility visiblity) {
        this.visiblity = visiblity;
    }

    public String getPassword() {
        return password;
    }

    public BlogItem password(String password) {
        this.password = password;
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Blog getBlog() {
        return blog;
    }

    public BlogItem blog(Blog blog) {
        this.blog = blog;
        return this;
    }

    public void setBlog(Blog blog) {
        this.blog = blog;
    }

    public Set<CommentBlogItem> getComments() {
        return comments;
    }

    public BlogItem comments(Set<CommentBlogItem> commentBlogItems) {
        this.comments = commentBlogItems;
        return this;
    }

    public BlogItem addComments(CommentBlogItem commentBlogItem) {
        this.comments.add(commentBlogItem);
        commentBlogItem.setBlogItem(this);
        return this;
    }

    public BlogItem removeComments(CommentBlogItem commentBlogItem) {
        this.comments.remove(commentBlogItem);
        commentBlogItem.setBlogItem(null);
        return this;
    }

    public void setComments(Set<CommentBlogItem> commentBlogItems) {
        this.comments = commentBlogItems;
    }

    public Set<KeyWord> getKeywords() {
        return keywords;
    }

    public BlogItem keywords(Set<KeyWord> keyWords) {
        this.keywords = keyWords;
        return this;
    }

    public BlogItem addKeywords(KeyWord keyWord) {
        this.keywords.add(keyWord);
        keyWord.setBlogItem(this);
        return this;
    }

    public BlogItem removeKeywords(KeyWord keyWord) {
        this.keywords.remove(keyWord);
        keyWord.setBlogItem(null);
        return this;
    }

    public void setKeywords(Set<KeyWord> keyWords) {
        this.keywords = keyWords;
    }

    public BlogItem getBlogItem() {
        return blogItem;
    }

    public BlogItem blogItem(BlogItem blogItem) {
        this.blogItem = blogItem;
        return this;
    }

    public void setBlogItem(BlogItem blogItem) {
        this.blogItem = blogItem;
    }

    public Set<BlogItem> getHistories() {
        return histories;
    }

    public BlogItem histories(Set<BlogItem> blogItems) {
        this.histories = blogItems;
        return this;
    }

    public BlogItem addHistories(BlogItem blogItem) {
        this.histories.add(blogItem);
        blogItem.setBlogItem(this);
        return this;
    }

    public BlogItem removeHistories(BlogItem blogItem) {
        this.histories.remove(blogItem);
        blogItem.setBlogItem(null);
        return this;
    }

    public void setHistories(Set<BlogItem> blogItems) {
        this.histories = blogItems;
    }

    public User getAuthor() {
        return author;
    }

    public BlogItem author(User user) {
        this.author = user;
        return this;
    }

    public void setAuthor(User user) {
        this.author = user;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public BlogItem tags(Set<Tag> tags) {
        this.tags = tags;
        return this;
    }

    public BlogItem addTags(Tag tag) {
        this.tags.add(tag);
        return this;
    }

    public BlogItem removeTags(Tag tag) {
        this.tags.remove(tag);
        return this;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BlogItem blogItem = (BlogItem) o;
        if (blogItem.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), blogItem.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BlogItem{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", url='" + getUrl() + "'" +
            ", text='" + getText() + "'" +
            ", staus='" + getStaus() + "'" +
            ", created='" + getCreated() + "'" +
            ", updated='" + getUpdated() + "'" +
            ", image='" + getImage() + "'" +
            ", imageContentType='" + getImageContentType() + "'" +
            ", visiblity='" + getVisiblity() + "'" +
            "}";
    }
}
