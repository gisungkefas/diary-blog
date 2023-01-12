package com.kefas.diaryblog.controller;

import com.kefas.diaryblog.exception.ResponseEntityErrorException;
import com.kefas.diaryblog.model.Album;
import com.kefas.diaryblog.payload.AlbumPayload;
import com.kefas.diaryblog.response.AlbumResponse;
import com.kefas.diaryblog.response.ApiResponse;
import com.kefas.diaryblog.response.PagedResponse;
import com.kefas.diaryblog.response.PhotoResponse;
import com.kefas.diaryblog.security.CurrentUser;
import com.kefas.diaryblog.security.UserPrincipal;
import com.kefas.diaryblog.service.AlbumService;
import com.kefas.diaryblog.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/albums")
public class AlbumController {

    private AlbumService albumService;

    private PhotoService photoService;


    @ExceptionHandler(ResponseEntityErrorException.class)
    public ResponseEntity<ApiResponse> handleExceptions(ResponseEntityErrorException exception) {
        return exception.getApiResponse();
    }

    @GetMapping
    public PagedResponse<AlbumResponse> getAllAlbums(@RequestParam(name = "page") Integer page,
                                                     @RequestParam(name = "size") Integer size) {
        return albumService.getAllAlbums(page, size);
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Album> addAlbum(@Valid @RequestBody AlbumPayload albumPayload, @CurrentUser UserPrincipal currentUser) {
        return albumService.addAlbum(albumPayload, currentUser);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Album> getAlbum(@PathVariable(name = "id") Long id) {
        return albumService.getAlbum(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<AlbumResponse> updateAlbum(@PathVariable(name = "id") Long id, @Valid @RequestBody AlbumPayload albumPayload,
                                                     @CurrentUser UserPrincipal currentUser) {
        return albumService.updateAlbum(id, albumPayload, currentUser);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteAlbum(@PathVariable(name = "id") Long id, @CurrentUser UserPrincipal currentUser) {
        return albumService.deleteAlbum(id, currentUser);
    }

    @GetMapping("/{id}/photos")
    public ResponseEntity<PagedResponse<PhotoResponse>> getAllPhotosByAlbum(@PathVariable(name = "id") Long id,
                                                                            @RequestParam(name = "page") Integer page,
                                                                            @RequestParam(name = "size") Integer size) {

        PagedResponse<PhotoResponse> response = photoService.getAllPhotosByAlbum(id, page, size);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
