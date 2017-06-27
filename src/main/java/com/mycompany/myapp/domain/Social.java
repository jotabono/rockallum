package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A Social.
 */
@Entity
@Table(name = "social")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "social")
public class Social implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "url")
    private String url;

    @Column(name = "official")
    private Boolean official;

    @Column(name = "merchandising")
    private Boolean merchandising;

    @Column(name = "tabs")
    private Boolean tabs;

    @ManyToMany(mappedBy = "socials")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Artist> artists = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getOfficial() {
        return official;
    }

    public void setOfficial(Boolean official) {
        this.official = official;
    }

    public Boolean getMerchandising() {
        return merchandising;
    }

    public void setMerchandising(Boolean merchandising) {
        this.merchandising = merchandising;
    }

    public Boolean getTabs() {
        return tabs;
    }

    public void setTabs(Boolean tabs) {
        this.tabs = tabs;
    }

    public Set<Artist> getArtists() {
        return artists;
    }

    public void setArtists(Set<Artist> artists) {
        this.artists = artists;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Social social = (Social) o;
        if(social.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, social.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Social{" +
            "id=" + id +
            ", url='" + url + "'" +
            ", official='" + official + "'" +
            ", merchandising='" + merchandising + "'" +
            ", tabs='" + tabs + "'" +
            '}';
    }
}
