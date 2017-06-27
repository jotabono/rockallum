package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Song;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Song entity.
 */
public interface SongRepository extends JpaRepository<Song,Long> {
    @Query("select song from Song song where song.album.id = :album_id")
    List<Song> findSongsByAlbum(@Param("album_id") Long id);
}
