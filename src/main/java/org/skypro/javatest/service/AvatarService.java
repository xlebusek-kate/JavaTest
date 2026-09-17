package org.skypro.javatest.service;

import org.skypro.javatest.obj.Avatar;
import org.skypro.javatest.repository.AvatarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public class AvatarService {
    @Autowired
    AvatarRepository avatarRepository;

    public List<Avatar> getAvatars(Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize);
        return avatarRepository.findAll(pageRequest).getContent();
    }
}
