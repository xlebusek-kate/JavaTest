package org.skypro.javatest.controller;

import org.skypro.javatest.obj.Avatar;
import org.skypro.javatest.service.AvatarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/avatar")
public class AvatarController {

    @Autowired
    private AvatarService avatarService;

    @GetMapping("/avatars")
    public ResponseEntity<List<Avatar>> getAvatars(@RequestParam Integer pageNumber, @RequestParam Integer pageSize) {
        if (pageNumber <= 0 || pageSize <= 0) ResponseEntity.badRequest().build();
        return ResponseEntity.ok(avatarService.getAvatars(pageNumber, pageSize));

    }
}
