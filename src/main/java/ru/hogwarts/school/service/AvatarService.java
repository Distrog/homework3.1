package ru.hogwarts.school.service;


import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface AvatarService {
    void upload(Long studentId, MultipartFile avatarFile) throws IOException;
}
