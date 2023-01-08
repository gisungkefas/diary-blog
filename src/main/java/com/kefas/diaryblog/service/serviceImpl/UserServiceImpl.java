package com.kefas.diaryblog.service.serviceImpl;

import com.kefas.diaryblog.model.Album;
import com.kefas.diaryblog.payload.AlbumPayload;
import com.kefas.diaryblog.repository.AlbumRepository;
import com.kefas.diaryblog.repository.UsersRepository;
import com.kefas.diaryblog.response.AlbumResponse;
import com.kefas.diaryblog.response.ApiResponse;
import com.kefas.diaryblog.response.PagedResponse;
import com.kefas.diaryblog.security.UserPrincipal;
import com.kefas.diaryblog.service.AlbumService;
import com.kefas.diaryblog.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class UserServiceImpl {

}