package com.kent.gmail.com.runtime.security;

import static org.springframework.util.ObjectUtils.isEmpty;

import com.kent.gmail.com.runtime.model.AppUser;
import com.kent.gmail.com.runtime.model.AppUser_;
import com.kent.gmail.com.runtime.service.AppUserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

  private final JwtTokenUtils jwtTokenUtils;
  private final AppUserService appUserService;
  private final RequestAttributeSecurityContextRepository
      requestAttributeSecurityContextRepository = new RequestAttributeSecurityContextRepository();

  public JwtTokenFilter(JwtTokenUtils jwtTokenUtils, AppUserService appUserService) {
    this.jwtTokenUtils = jwtTokenUtils;
    this.appUserService = appUserService;
  }

  public String extractToken(HttpServletRequest request) {
    final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (isEmpty(header) || !header.startsWith("Bearer ")) {
      return null;
    }

    // Get jwt token and validate
    return header.split(" ")[1].trim();
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws ServletException, IOException {
    // Get authorization header and validate
    String token = extractToken(request);
    if (token == null) {
      chain.doFilter(request, response);
      return;
    }
    Jws<Claims> claimsJws = jwtTokenUtils.getClaims(token);
    if (claimsJws == null) {
      chain.doFilter(request, response);
      return;
    }
    String id = jwtTokenUtils.getId(claimsJws);

    if (id != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      AppUser appUser = appUserService.getByIdOrNull(AppUser.class, AppUser_.id, id);
      if (appUser != null) {
        UserSecurityContext userSecurityContext = new UserSecurityContext(appUser);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
            new UsernamePasswordAuthenticationToken(
                userSecurityContext, null, userSecurityContext.getAuthorities());
        usernamePasswordAuthenticationToken.setDetails(
            new WebAuthenticationDetailsSource().buildDetails(request));
        // After setting the Authentication in the context, we specify
        // that the current user is authenticated. So it passes the
        // Spring Security Configurations successfully.
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        this.requestAttributeSecurityContextRepository.saveContext(
            SecurityContextHolder.getContext(), request, response);
      }
    }

    chain.doFilter(request, response);
  }
}
