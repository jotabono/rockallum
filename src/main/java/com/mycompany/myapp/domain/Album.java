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
 * A Album.
 */
@Entity
@Table(name = "album")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "album")
public class Album implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;
    
    @NotNull
    @Column(name = "release_date", nullable = false)
    private LocalDate releaseDate;
    
    @Column(name = "catalog_id")
    private String catalogID;
    
    @Column(name = "num_copies")
    private String numCopies;
    
    @Column(name = "format")
    private String format;
    
    @Column(name = "add_notes")
    private String addNotes;
    
    @Column(name = "rec_info")
    private String recInfo;
    
    @Column(name = "independent")
    private Boolean independent;
    
    @Column(name = "picture")
    private String picture;
    
    @OneToMany(mappedBy = "album")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Song> songs = new HashSet<>();

    @OneToMany(mappedBy = "album")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Review> reviews = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "label_id")
    private Label label;

    @ManyToOne
    @JoinColumn(name = "album_types_id")
    private AlbumTypes albumTypes;

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

    public LocalDate getReleaseDate() {
        return releaseDate;
    }
    
    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getCatalogID() {
        return catalogID;
    }
    
    public void setCatalogID(String catalogID) {
        this.catalogID = catalogID;
    }

    public String getNumCopies() {
        return numCopies;
    }
    
    public void setNumCopies(String numCopies) {
        this.numCopies = numCopies;
    }

    public String getFormat() {
        return format;
    }
    
    public void setFormat(String format) {
        this.format = format;
    }

    public String getAddNotes() {
        return addNotes;
    }
    
    public void setAddNotes(String addNotes) {
        this.addNotes = addNotes;
    }

    public String getRecInfo() {
        return recInfo;
    }
    
    public void setRecInfo(String recInfo) {
        this.recInfo = recInfo;
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

    public Set<Song> getSongs() {
        return songs;
    }

    public void setSongs(Set<Song> songs) {
        this.songs = songs;
    }

    public Set<Review> getReviews() {
        return reviews;
    }

    public void setReviews(Set<Review> reviews) {
        this.reviews = reviews;
    }

    public Label getLabel() {
        return label;
    }

    public void setLabel(Label label) {
        this.label = label;
    }

    public AlbumTypes getAlbumTypes() {
        return albumTypes;
    }

    public void setAlbumTypes(AlbumTypes albumTypes) {
        this.albumTypes = albumTypes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Album album = (Album) o;
        if(album.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, album.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Album{" +
            "id=" + id +
            ", title='" + title + "'" +
            ", releaseDate='" + releaseDate + "'" +
            ", catalogID='" + catalogID + "'" +
            ", numCopies='" + numCopies + "'" +
            ", format='" + format + "'" +
            ", addNotes='" + addNotes + "'" +
            ", recInfo='" + recInfo + "'" +
            ", independent='" + independent + "'" +
            ", picture='" + picture + "'" +
            '}';
    }
}
