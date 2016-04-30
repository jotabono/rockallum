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
 * A FavouriteArtist.
 */
@Entity
@Table(name = "favourite_artist")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "favouriteartist")
public class FavouriteArtist implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "liked")
    private Boolean liked;
    
    @ManyToOne
    @JoinColumn(name = "artist_id")
    private Band artist;

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

    public Band getArtist() {
        return artist;
    }

    public void setArtist(Band band) {
        this.artist = band;
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
        FavouriteArtist favouriteArtist = (FavouriteArtist) o;
        if(favouriteArtist.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, favouriteArtist.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "FavouriteArtist{" +
            "id=" + id +
            ", liked='" + liked + "'" +
            '}';
    }
}
