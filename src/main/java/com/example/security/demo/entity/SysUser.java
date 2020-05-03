package com.example.security.demo.entity;


import lombok.*;
import java.util.List;

@Data
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
public class SysUser {
    private Long id;
    private String username;
    private String password;

    private List<SysPermission> sysPermissions;
}
