package com.kefas.diaryblog.service.serviceImpl;

import com.kefas.diaryblog.exception.BlogApiException;
import com.kefas.diaryblog.exception.ResourceNotFoundException;
import com.kefas.diaryblog.model.Album;
import com.kefas.diaryblog.model.role.Role;
import com.kefas.diaryblog.model.user.Users;
import com.kefas.diaryblog.payload.AlbumPayload;
import com.kefas.diaryblog.repository.AlbumRepository;
import com.kefas.diaryblog.repository.UsersRepository;
import com.kefas.diaryblog.response.AlbumResponse;
import com.kefas.diaryblog.response.ApiResponse;
import com.kefas.diaryblog.response.PagedResponse;
import com.kefas.diaryblog.security.UserPrincipal;
import com.kefas.diaryblog.service.AlbumService;
import com.kefas.diaryblog.utils.AppUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AlbumServiceImpl implements AlbumService {

    private AlbumRepository albumRepository;

    private UsersRepository usersRepository;

    private ModelMapper modelMapper;

    @Autowired
    public void AlbumServiceImpl(AlbumRepository albumRepository, UsersRepository usersRepository){
        this.albumRepository = albumRepository;
        this.usersRepository = usersRepository;
    }

    @Override
    public PagedResponse<AlbumResponse> getAllAlbums(int page, int size) {
        AppUtils.validatePageNumberAndSize(page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC,"createdAt");

        Page<Album> albums = albumRepository.findAll(pageable);
        if(albums.getNumberOfElements() == 0){
            return new PagedResponse<>(Collections.emptyList(), albums.getNumber(), albums.getSize(), albums.getTotalElements(),
                    albums.getTotalPages(), albums.isLast());
        }

        List<AlbumResponse> albumResponses = Arrays.asList(modelMapper.map(albums.getContent(), AlbumResponse[].class));
        return new PagedResponse<>(albumResponses, albums.getNumber(), albums.getSize(), albums.getTotalElements(), albums.getTotalPages(), albums.isLast());
    }

    @Override
    public ResponseEntity<Album> addAlbum(AlbumPayload albumRequest, UserPrincipal currentUser) {

        Users users = usersRepository.getUsers(currentUser);

        Album album = new Album();
        modelMapper.map(albumRequest, album);
        album.setUsers(users);
        Album newAlbum = albumRepository.save(album);

        return new ResponseEntity<>(newAlbum, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Album> getAlbum(Long id) {

        Album album = albumRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Album Id was not found")
        );
        return new ResponseEntity<>(album, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<AlbumResponse> updateAlbum(Long id, AlbumPayload newAlbum, UserPrincipal currentUser) {

        Album album = albumRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Album Id was not found")
        );

        Users users = usersRepository.getUsers(currentUser);
        if(album.getUsers().getId().equals(users.getId()) || currentUser.getAuthorities().contains(new SimpleGrantedAuthority(Role.ADMIN.toString()))){
            album.setTitle(newAlbum.getTitle());
            Album updateAlbum = albumRepository.save(album);

            AlbumResponse albumResponse = new AlbumResponse();
            modelMapper.map(updateAlbum, albumResponse);

            return new ResponseEntity<>(albumResponse, HttpStatus.OK);
        }

        throw new BlogApiException(HttpStatus.UNAUTHORIZED, "You don't have permission to perform this operation");
    }

    @Override
    public ResponseEntity<ApiResponse> deleteAlbum(Long id, UserPrincipal currentUser) {

        Album album = albumRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Album Id was not found")
        );

        Users users = usersRepository.getUsers(currentUser);
        if(album.getUsers().getId().equals(users.getId()) || currentUser.getAuthorities().contains(new SimpleGrantedAuthority(Role.ADMIN.toString()))){
            albumRepository.deleteById(id);

            return new ResponseEntity<>(new ApiResponse("You have succesfully deleted this album", LocalDateTime.now()), HttpStatus.OK);
        }

        throw new BlogApiException(HttpStatus.UNAUTHORIZED, "You don't have permission to perform this operation");
    }

    @Override
    public PagedResponse<Album> getUserAlbums(String email, int page, int size) {

        Users users = usersRepository.getUsersByName(email);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "created at");

        Page<Album> albums = albumRepository.findByCreatedBy(users.getId(), (java.awt.print.Pageable) pageable);

        List<Album> content = albums.getNumberOfElements() > 0 ? albums.getContent() : Collections.emptyList();

        return new PagedResponse<>(content, albums.getNumber(), albums.getSize(), albums.getTotalElements(), albums.getTotalPages(), albums.isLast());
    }
}
