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
 * A FavouriteLabel.
 */
@Entity
@Table(name = "favourite_label")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "favouritelabel")
public class FavouriteLabel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "liked")
    private Boolean liked;
    
    @ManyToOne
    @JoinColumn(name = "label_id")
    private Band label;

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

    public Band getLabel() {
        return label;
    }

    public void setLabel(Band band) {
        this.label = band;
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
        FavouriteLabel favouriteLabel = (FavouriteLabel) o;
        if(favouriteLabel.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, favouriteLabel.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "FavouriteLabel{" +
            "id=" + id +
            ", liked='" + liked + "'" +
            '}';
    }
}
