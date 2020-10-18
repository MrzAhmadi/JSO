package ir.mrzahmadi.jso.Utils.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import ir.mrzahmadi.jso.model.Response.ErrorResponse;
import ir.mrzahmadi.jso.model.User;
import ir.mrzahmadi.jso.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private JwtUtil jwtUtil;
    private UserService userService;
    private ObjectMapper mapper;


    @Autowired
    public JwtFilter(JwtUtil jwtUtil, UserService userService, ObjectMapper mapper) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.mapper = mapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String jwt = request.getHeader("Authorization");

        if (jwt != null) {
            String phoneNumber = null;
            try {
                phoneNumber = jwtUtil.getPhoneNumber(jwt);
            } catch (Exception e) {
                unAuthorized(response);
                return;
            }

            if (phoneNumber != null && SecurityContextHolder.getContext().getAuthentication() == null) {


                User user = userService.findByPhoneNumber(phoneNumber);
                UserDetails userDetails = userService.loadUserByUsername(phoneNumber);

                long tokenExpirationTime = jwtUtil.getExpirationByTime(jwt);

                if (user != null && jwtUtil.validateToken(jwt, userDetails) && tokenExpirationTime == user.getTokeExpirationDate()) {

                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

                } else
                    unAuthorized(response);

            } else
                unAuthorized(response);
        } else {
            String url = UriComponentsBuilder.fromHttpRequest(new ServletServerHttpRequest(request)).build().toUriString();
            if (!url.contains("/auth/"))
                unAuthorized(response);
        }
        filterChain.doFilter(request, response);
    }

    private void unAuthorized(HttpServletResponse response) throws IOException {
        String errorMessage = "Not Allowed to Access. Please try with valid Origin.";
        mapper.writeValue(response.getWriter(), new ErrorResponse(HttpServletResponse.SC_UNAUTHORIZED, errorMessage));
    }

}
