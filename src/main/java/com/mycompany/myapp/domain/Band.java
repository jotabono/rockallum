package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import java.time.LocalDate;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Band.
 */
@Entity
@Table(name = "band")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "band")
public class Band implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "founding_date")
    private LocalDate foundingDate;

    @Column(name = "years_active")
    private String yearsActive;

    @Column(name = "lyrical_themes")
    private String lyricalThemes;

    @Column(name = "independent")
    private Boolean independent;

    @Column(name = "picture")
    private String picture;

    @Column(name = "links")
    private String links;

    @Column(name = "logo")
    private String logo;

    @Column(name = "bio")
    private String bio;

    @ManyToMany (fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "band_genre",
               joinColumns = @JoinColumn(name="bands_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="genres_id", referencedColumnName="ID"))
    private Set<Genre> genres = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "band_artist",
               joinColumns = @JoinColumn(name="bands_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="artists_id", referencedColumnName="ID"))
    private Set<Artist> artists = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "band_album",
        joinColumns = @JoinColumn(name="bands_id", referencedColumnName="ID"),
        inverseJoinColumns = @JoinColumn(name="albums_id", referencedColumnName="ID"))
    private Set<Album> albums = new HashSet<>();

    @OneToMany(mappedBy = "band")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<FavouriteBand> favouritebands = new HashSet<>();

    @OneToMany(mappedBy = "album")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<FavouriteAlbum> favouritealbums = new HashSet<>();

    @OneToMany(mappedBy = "song")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<FavouriteSong> favouritesongs = new HashSet<>();

    @OneToMany(mappedBy = "label")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<FavouriteLabel> favouritelabels = new HashSet<>();

    @OneToMany(mappedBy = "artist")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<FavouriteArtist> favouriteartists = new HashSet<>();

    @OneToMany(mappedBy = "review")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<FavouriteReview> favouritereviews = new HashSet<>();

    @OneToMany(mappedBy = "album")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Collection> collections = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

    @ManyToOne
    @JoinColumn(name = "label_id")
    private Label label;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private Status status;

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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public LocalDate getFoundingDate() {
        return foundingDate;
    }

    public void setFoundingDate(LocalDate foundingDate) {
        this.foundingDate = foundingDate;
    }

    public String getYearsActive() {
        return yearsActive;
    }

    public void setYearsActive(String yearsActive) {
        this.yearsActive = yearsActive;
    }

    public String getLyricalThemes() {
        return lyricalThemes;
    }

    public void setLyricalThemes(String lyricalThemes) {
        this.lyricalThemes = lyricalThemes;
    }

    public Boolean getIndependent() {
        return independent;
    }

    public void setIndependent(Boolean independent) {
        this.independent = independent;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getLinks() {
        return links;
    }

    public void setLinks(String links) {
        this.links = links;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Set<Genre> getGenres() {
        return genres;
    }

    public void setGenres(Set<Genre> genres) {
        this.genres = genres;
    }

    public Set<Artist> getArtists() {
        return artists;
    }

    public void setArtists(Set<Artist> artists) {
        this.artists = artists;
    }

    public Set<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(Set<Album> albums) {
        this.albums = albums;
    }

    public Set<FavouriteBand> getFavouritebands() {
        return favouritebands;
    }

    public void setFavouritebands(Set<FavouriteBand> favouriteBands) {
        this.favouritebands = favouriteBands;
    }

    public Set<FavouriteAlbum> getFavouritealbums() {
        return favouritealbums;
    }

    public void setFavouritealbums(Set<FavouriteAlbum> favouriteAlbums) {
        this.favouritealbums = favouriteAlbums;
    }

    public Set<FavouriteSong> getFavouritesongs() {
        return favouritesongs;
    }

    public void setFavouritesongs(Set<FavouriteSong> favouriteSongs) {
        this.favouritesongs = favouriteSongs;
    }

    public Set<FavouriteLabel> getFavouritelabels() {
        return favouritelabels;
    }

    public void setFavouritelabels(Set<FavouriteLabel> favouriteLabels) {
        this.favouritelabels = favouriteLabels;
    }

    public Set<FavouriteArtist> getFavouriteartists() {
        return favouriteartists;
    }

    public void setFavouriteartists(Set<FavouriteArtist> favouriteArtists) {
        this.favouriteartists = favouriteArtists;
    }

    public Set<FavouriteReview> getFavouritereviews() {
        return favouritereviews;
    }

    public void setFavouritereviews(Set<FavouriteReview> favouriteReviews) {
        this.favouritereviews = favouriteReviews;
    }

    public Set<Collection> getCollections() {
        return collections;
    }

    public void setCollections(Set<Collection> collections) {
        this.collections = collections;
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

    public Label getLabel() {
        return label;
    }

    public void setLabel(Label label) {
        this.label = label;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Band band = (Band) o;
        if(band.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, band.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Band{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", location='" + location + "'" +
            ", latitude='" + latitude + "'" +
            ", longitude='" + longitude + "'" +
            ", foundingDate='" + foundingDate + "'" +
            ", yearsActive='" + yearsActive + "'" +
            ", lyricalThemes='" + lyricalThemes + "'" +
            ", independent='" + independent + "'" +
            ", picture='" + picture + "'" +
            ", links='" + links + "'" +
            ", logo='" + logo + "'" +
            ", bio='" + bio + "'" +
            '}';
    }
}
