package ru.hogwarts.school.dto;

import ru.hogwarts.school.model.Avatar;

import java.util.Objects;

public class AvatarDTO {
private long id;
private String url;
public static AvatarDTO convertAvatarToDTO(Avatar avatar){
    AvatarDTO avatarDTO = new AvatarDTO();
    avatarDTO.setId(avatar.getId());
    avatarDTO.setUrl("http://localhost:8080/students/"+avatarDTO.getId()+"/avatar-from-db");
    return avatarDTO;
}

    public long getId() {
        return id;
    }

    private void setId(long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    private void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AvatarDTO avatarDTO = (AvatarDTO) o;
        return id == avatarDTO.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
