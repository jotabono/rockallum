package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Label.
 */
@Entity
@Table(name = "label")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "label")
public class Label implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "founding_date")
    private String foundingDate;
    
    @Column(name = "location")
    private String location;
    
    @Column(name = "status")
    private String status;
    
    @Column(name = "genres")
    private String genres;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "add_notes")
    private String addNotes;
    
    @Column(name = "links")
    private String links;
    
    @Column(name = "phone")
    private String phone;
    
    @Column(name = "address")
    private String address;
    
    @Column(name = "onlineshop")
    private String onlineshop;
    
    @Column(name = "picture")
    private String picture;
    
    @OneToMany(mappedBy = "label")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Band> bands = new HashSet<>();

    @OneToMany(mappedBy = "label")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Album> albums = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public String getFoundingDate() {
        return foundingDate;
    }
    
    public void setFoundingDate(String foundingDate) {
        this.foundingDate = foundingDate;
    }

    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }

    public String getGenres() {
        return genres;
    }
    
    public void setGenres(String genres) {
        this.genres = genres;
    }

    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddNotes() {
        return addNotes;
    }
    
    public void setAddNotes(String addNotes) {
        this.addNotes = addNotes;
    }

    public String getLinks() {
        return links;
    }
    
    public void setLinks(String links) {
        this.links = links;
    }

    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }

    public String getOnlineshop() {
        return onlineshop;
    }
    
    public void setOnlineshop(String onlineshop) {
        this.onlineshop = onlineshop;
    }

    public String getPicture() {
        return picture;
    }
    
    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Set<Band> getBands() {
        return bands;
    }

    public void setBands(Set<Band> bands) {
        this.bands = bands;
    }

    public Set<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(Set<Album> albums) {
        this.albums = albums;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Label label = (Label) o;
        if(label.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, label.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Label{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", foundingDate='" + foundingDate + "'" +
            ", location='" + location + "'" +
            ", status='" + status + "'" +
            ", genres='" + genres + "'" +
            ", description='" + description + "'" +
            ", addNotes='" + addNotes + "'" +
            ", links='" + links + "'" +
            ", phone='" + phone + "'" +
            ", address='" + address + "'" +
            ", onlineshop='" + onlineshop + "'" +
            ", picture='" + picture + "'" +
            '}';
    }
}
