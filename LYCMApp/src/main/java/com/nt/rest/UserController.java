package com.nt.rest;

import com.nt.entity.User;
import com.nt.service.IUserMgmtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private IUserMgmtService userMgmtService;

    @GetMapping("/all")
    public List<User> getAllTheUser(){
        return userMgmtService.getAll();
    }

}
