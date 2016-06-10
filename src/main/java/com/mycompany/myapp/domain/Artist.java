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
 * A Artist.
 */
@Entity
@Table(name = "artist")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "artist")
public class Artist implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "real_name")
    private String realName;

    @Column(name = "born_in")
    private LocalDate bornIn;

    @Column(name = "age")
    private Integer age;

    @Column(name = "bio")
    private String bio;

    @Column(name = "years_active")
    private String yearsActive;

    @Column(name = "picture")
    private String picture;

    @Column(name = "still_in_band")
    private Boolean stillInBand;

    @Column(name = "live_musician")
    private Boolean liveMusician;

    @Column(name = "is_rip")
    private Boolean isRip;

    @ManyToMany(mappedBy = "artists")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Band> bands = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

    @ManyToOne
    @JoinColumn(name = "sex_id")
    private Sex sex;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private Status status;

    @ManyToMany (fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "artist_social",
               joinColumns = @JoinColumn(name="artists_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="socials_id", referencedColumnName="ID"))
    private Set<Social> socials = new HashSet<>();

    @ManyToMany (fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "artist_instrument",
               joinColumns = @JoinColumn(name="artists_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="instruments_id", referencedColumnName="ID"))
    private Set<Instrument> instruments = new HashSet<>();

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

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public LocalDate getBornIn() {
        return bornIn;
    }

    public void setBornIn(LocalDate bornIn) {
        this.bornIn = bornIn;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getYearsActive() {
        return yearsActive;
    }

    public void setYearsActive(String yearsActive) {
        this.yearsActive = yearsActive;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Boolean getStillInBand() {
        return stillInBand;
    }

    public void setStillInBand(Boolean stillInBand) {
        this.stillInBand = stillInBand;
    }

    public Boolean getLiveMusician() {
        return liveMusician;
    }

    public void setLiveMusician(Boolean liveMusician) {
        this.liveMusician = liveMusician;
    }

    public Boolean getIsRip() {
        return isRip;
    }

    public void setIsRip(Boolean isRip) {
        this.isRip = isRip;
    }

    public Set<Band> getBands() {
        return bands;
    }

    public void setBands(Set<Band> bands) {
        this.bands = bands;
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

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Set<Social> getSocials() {
        return socials;
    }

    public void setSocials(Set<Social> socials) {
        this.socials = socials;
    }

    public Set<Instrument> getInstruments() {
        return instruments;
    }

    public void setInstruments(Set<Instrument> instruments) {
        this.instruments = instruments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Artist artist = (Artist) o;
        if(artist.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, artist.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Artist{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", realName='" + realName + "'" +
            ", bornIn='" + bornIn + "'" +
            ", age='" + age + "'" +
            ", bio='" + bio + "'" +
            ", yearsActive='" + yearsActive + "'" +
            ", picture='" + picture + "'" +
            ", stillInBand='" + stillInBand + "'" +
            ", liveMusician='" + liveMusician + "'" +
            ", isRip='" + isRip + "'" +
            '}';
    }
}
