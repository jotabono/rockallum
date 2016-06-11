package com.mycompany.myapp.web.rest.dto;

import com.mycompany.myapp.domain.Band;

/**
 * Created by jotabono on 10/6/16.
 */
public class BandDTO {
    private Band band;
    private Boolean liked;

    public Boolean getLiked() {
        return liked;
    }

    public void setLiked(Boolean liked) {
        this.liked = liked;
    }

    public Band getBand() {
        return band;
    }

    public void setBand(Band band) {
        this.band = band;
    }

    @Override
    public String toString() {
        return "BandDTO{" +
            "band=" + band +
            ", liked=" + liked +
            '}';
    }
}
