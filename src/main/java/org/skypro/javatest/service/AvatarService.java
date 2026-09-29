package org.skypro.javatest.service;

import lombok.extern.slf4j.Slf4j;
import org.skypro.javatest.obj.Avatar;
import org.skypro.javatest.repository.AvatarRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

import java.util.logging.Logger;

@Service
@Slf4j
public class AvatarService {

    @Autowired
    AvatarRepository avatarRepository;


    public List<Avatar> getAvatars(Integer pageNumber, Integer pageSize) {
        log.debug("Метод getAvatars с параметрами {},{}", pageNumber, pageSize);
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize);
        return avatarRepository.findAll(pageRequest).getContent();
    }
}