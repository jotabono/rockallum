package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A FavouriteAlbum.
 */
@Entity
@Table(name = "favourite_album")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "favouritealbum")
public class FavouriteAlbum implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "liked")
    private Boolean liked;
    
    @ManyToOne
    @JoinColumn(name = "album_id")
    private Band album;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getLiked() {
        return liked;
    }
    
    public void setLiked(Boolean liked) {
        this.liked = liked;
    }

    public Band getAlbum() {
        return album;
    }

    public void setAlbum(Band band) {
        this.album = band;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FavouriteAlbum favouriteAlbum = (FavouriteAlbum) o;
        if(favouriteAlbum.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, favouriteAlbum.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "FavouriteAlbum{" +
            "id=" + id +
            ", liked='" + liked + "'" +
            '}';
    }
}
