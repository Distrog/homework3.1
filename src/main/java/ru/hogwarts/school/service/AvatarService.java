package ru.hogwarts.school.service;


import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;

import java.io.IOException;
import java.util.Collection;

public interface AvatarService {
    void upload(Long studentId, MultipartFile avatarFile) throws IOException;

    Avatar findAvatar(Long id);

    Collection<Avatar> getAllAvatars(Integer pageNumber,Integer pageSize);
}
