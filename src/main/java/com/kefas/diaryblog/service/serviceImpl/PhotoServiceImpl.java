package com.kefas.diaryblog.service.serviceImpl;

import com.kefas.diaryblog.exception.ResourceNotFoundException;
import com.kefas.diaryblog.exception.UnauthorizedException;
import com.kefas.diaryblog.model.Album;
import com.kefas.diaryblog.model.Photo;
import com.kefas.diaryblog.model.role.Role;
import com.kefas.diaryblog.payload.PhotoPayload;
import com.kefas.diaryblog.repository.AlbumRepository;
import com.kefas.diaryblog.repository.PhotoRepository;
import com.kefas.diaryblog.response.ApiResponse;
import com.kefas.diaryblog.response.PagedResponse;
import com.kefas.diaryblog.response.PhotoResponse;
import com.kefas.diaryblog.security.UserPrincipal;
import com.kefas.diaryblog.service.PhotoService;
import com.kefas.diaryblog.utils.AppConstant;
import com.kefas.diaryblog.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class PhotoServiceImpl implements PhotoService {

    private PhotoRepository photoRepository;

    private AlbumRepository albumRepository;

    @Autowired
    public PhotoServiceImpl(PhotoRepository photoRepository, AlbumRepository albumRepository) {
        this.photoRepository = photoRepository;
        this.albumRepository = albumRepository;
    }

    @Override
    public PagedResponse<PhotoResponse> getAllPhotos(int page, int size) {

        AppUtils.validatePageNumberAndSize(page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC,"Created At");

        Page<Photo> photos = photoRepository.findAll(pageable);

        List<PhotoResponse> photoResponses = new ArrayList<>(photos.getContent().size());
        for(Photo photo : photos.getContent()){
            photoResponses.add(new PhotoResponse(photo.getId(), photo.getTitle(), photo.getUrl(), photo.getThumbnailUrl(), photo.getAlbum().getId()));
        }
        if(photos.getNumberOfElements() == 0){
            return new PagedResponse<>(Collections.emptyList(), photos.getNumber(), photos.getSize(), photos.getTotalElements(), photos.getTotalPages(), photos.isLast());
        }

        return new PagedResponse<>(photoResponses, photos.getNumber(), photos.getSize(), photos.getTotalElements(), photos.getTotalPages(), photos.isLast());
    }

    @Override
    public PhotoResponse getPhoto(Long id) {

        Photo photo = photoRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Photo Id could not be found")
        );

        return new PhotoResponse(photo.getId(), photo.getTitle(), photo.getUrl(), photo.getThumbnailUrl(), photo.getAlbum().getId());
    }

    @Override
    public PhotoResponse updatePhoto(Long id, PhotoPayload photoRequest, UserPrincipal currentUser) {
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Album Id could not be found"));
        Photo photo = photoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Photo Id could not be found"));
        if (photo.getAlbum().getUsers().equals(currentUser.getId())
                || currentUser.getAuthorities().contains(new SimpleGrantedAuthority(Role.ADMIN.toString()))) {
            photo.setTitle(photoRequest.getTitle());
            photo.setThumbnailUrl(photoRequest.getThumbnailUrl());
            photo.setAlbum(album);
            Photo updatedPhoto = photoRepository.save(photo);
            return new PhotoResponse(updatedPhoto.getId(), updatedPhoto.getTitle(),
                    updatedPhoto.getUrl(), updatedPhoto.getThumbnailUrl(), updatedPhoto.getAlbum().getId());
        }
        ApiResponse apiResponse = new ApiResponse("You don't have permission to update this photo", LocalDateTime.now());

        throw new UnauthorizedException(apiResponse);
    }

    @Override
    public PhotoResponse addPhoto(PhotoPayload photoRequest, UserPrincipal currentUser) {
        Album album = albumRepository.findById(photoRequest.getAlbumId())
                .orElseThrow(()-> new ResourceNotFoundException("Album Id could not be found"));
        if(album.getUsers().getId().equals(currentUser.getId())) {
            Photo photo = new Photo(photoRequest.getTitle(), photoRequest.getUrl(), photoRequest.getThumbnailUrl(), album);
            Photo newPhoto = photoRepository.save(photo);
            return new PhotoResponse(newPhoto.getId(), newPhoto.getTitle(),
                    newPhoto.getUrl(), newPhoto.getThumbnailUrl(), newPhoto.getAlbum().getId());
        }
        ApiResponse apiResponse = new ApiResponse("You don't have permission to add photo in this album", LocalDateTime.now());

        throw new UnauthorizedException(apiResponse);
    }

    @Override
    public ApiResponse deletePhoto(Long id, UserPrincipal currentUser) {

        Photo photo = photoRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Photo Id could not be found")
        );
        if(photo.getAlbum().getUsers().getId().equals(currentUser.getId()) || currentUser.getAuthorities().contains(new SimpleGrantedAuthority(Role.ADMIN.toString()))) {
            photoRepository.deleteById(id);

            return new ApiResponse("Photo is deleted Sucessful", LocalDateTime.now());
            }
        ApiResponse apiResponse = new ApiResponse("You don't have permission to perform this Operation", LocalDateTime.now());

        throw new UnauthorizedException(apiResponse);
        }


    @Override
    public PagedResponse<PhotoResponse> getAllPhotosByAlbum(Long albumId, int page, int size) {

        AppUtils.validatePageNumberAndSize(page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, AppConstant.CREATED_AT);

        Page<Photo> photos = photoRepository.findByAlbumId(albumId, (java.awt.print.Pageable) pageable);

       List<PhotoResponse> photoResponses = new ArrayList<>(photos.getContent().size());
       for(Photo photo : photos.getContent()){
           photoResponses.add(new PhotoResponse(photo.getId(), photo.getTitle(), photo.getUrl(), photo.getThumbnailUrl(), photo.getAlbum().getId()));
       }
        return new PagedResponse<>(photoResponses, photos.getNumber(), photos.getSize(), photos.getTotalElements(), photos.getTotalPages(), photos.isLast());
    }
}
