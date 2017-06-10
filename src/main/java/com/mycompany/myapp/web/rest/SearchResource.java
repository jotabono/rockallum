package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.repository.*;
import com.sun.org.glassfish.gmbal.ManagedData;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class SearchResource {

    @Inject
    private BandRepository bandRepository;

    @Inject
    private ArtistRepository artistRepository;

    @Inject
    private LabelRepository labelRepository;

    @Inject
    private AlbumRepository albumRepository;

    @Inject
    private SongRepository songRepository;

    // Search Bands

    @RequestMapping(value = "/_search/{query2:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public HashMap<String, Object> searchFunc(@PathVariable String query2) {

        String query = query2.toLowerCase();

        List<Band> bands = bandRepository.findAll()
            .stream().filter(band -> band.getName().toLowerCase()
            .contains(query)).collect(Collectors.toList());

        List<Artist> artists = artistRepository.findAll()
            .stream().filter(artist -> artist.getName().toLowerCase()
                .contains(query)).collect(Collectors.toList());

        List<Label> labels = labelRepository.findAll()
            .stream().filter(label -> label.getName().toLowerCase()
                .contains(query)).collect(Collectors.toList());

        List<Album> albums = albumRepository.findAll()
            .stream().filter(album -> album.getTitle().toLowerCase()
                .contains(query)).collect(Collectors.toList());

        List<Song> songs = songRepository.findAll()
            .stream().filter(song -> song.getTitle().toLowerCase()
                .contains(query)).collect(Collectors.toList());

        HashMap result = new HashMap();

        result.put("Bands", bands);
        result.put("Artists", artists);
        result.put("Labels", labels);
        result.put("Albums", albums);
        result.put("Songs", songs);

        return result;
    }
}
