package com.mycompany.myapp.web.rest.dto;

import com.mycompany.myapp.domain.Album;

/**
 * Created by jotabono on 12/6/16.
 */
public class AlbumDTO {
    private Album album;
    private Boolean liked;

    public Boolean getLiked() {
        return liked;
    }

    public void setLiked(Boolean liked) {
        this.liked = liked;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    @Override
    public String toString() {
        return "AlbumDTO{" +
            "album=" + album +
            ", liked=" + liked +
            '}';
    }
}
