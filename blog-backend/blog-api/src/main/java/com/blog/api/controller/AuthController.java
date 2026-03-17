package com.blog.api.controller;

import com.blog.application.service.UserService;
import com.blog.common.result.Result;
import com.blog.common.result.ResultCode;
import com.blog.common.utils.JwtUtil;
import com.blog.domain.dto.LoginDTO;
import com.blog.domain.dto.UserDTO;
import com.blog.domain.vo.UserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "用户认证", description = "用户登录相关接口")
public class AuthController {

    private final UserService userService;

    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "用户登录获取 Token")
    public Result<Map<String, Object>> login(
            @Parameter(description = "登录请求参数" , required = true) @Valid @RequestBody LoginDTO loginDTO) {
        UserVO userVO = userService.login(loginDTO);
        String token = JwtUtil.generateToken(userVO.getId(), userVO.getUsername(), userVO.getRole().name());

        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("user", userVO);

        return Result.success(result);
    }

    @GetMapping("/me")
    @Operation(summary = "获取当前用户信息", description = "获取当前登录用户信息")
    public Result<UserVO> getCurrentUser(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Result.error(ResultCode.UNAUTHORIZED);
        }
        
        String token = authHeader.substring(7);
        try {
            Long userId = JwtUtil.getUserId(token);
            UserVO userVO = userService.getCurrentUser(userId);
            return Result.success(userVO);
        } catch (Exception e) {
            return Result.error(ResultCode.UNAUTHORIZED);
        }
    }
}
