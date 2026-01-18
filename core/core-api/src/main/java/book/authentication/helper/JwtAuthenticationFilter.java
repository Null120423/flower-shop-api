package book.authentication.helper;

import book.authentication.service.JwtService;
import book.core.domain.user.impl.UserService;
import book.core.support.error.ErrorType;
import book.core.support.response.ApiResponse;
import book.storage.db.core.entity.UserEntity;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserService userService;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            UserService userService
    ) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        if (!jwtService.isTokenValid(token)) {
            sendErrorResponse(response, ErrorType.INVALID_TOKEN);
            return;
        }

        String userId = jwtService.extractUserId(token);
        //optional
//        Optional<UserEntity> found = userService.findById(userId);

//        if (found.isEmpty()) {
//            sendErrorResponse(response, ErrorType.USER_NOT_FOUND);
//            return;
//        }
        // needed check user id match with action
        // set Authentication cho SecurityContext
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(
                        userId,
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))

                );
        authToken.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)
        );
        SecurityContextHolder.getContext().setAuthentication(authToken);
        filterChain.doFilter(request, response);
    }

    // Helper method serialize ApiResponse ra JSON và trả về
    private void sendErrorResponse(HttpServletResponse response, ErrorType error) throws IOException {
        response.setStatus(error.getStatus().value()); // vd: 401, 404
        response.setContentType("application/json;charset=UTF-8");
        // Convert ApiResponse -> JSON
        ApiResponse<?> apiResponse = ApiResponse.fail(error);
        String json = new com.fasterxml.jackson.databind.ObjectMapper()
                .writeValueAsString(apiResponse);
        response.getWriter().write(json);
    }

}

