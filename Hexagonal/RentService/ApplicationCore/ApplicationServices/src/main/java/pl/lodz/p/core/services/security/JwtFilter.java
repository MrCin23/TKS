//package pl.lodz.p.core.services.security;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.NoArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//import pl.lodz.p.core.domain.user.Role;
//import pl.lodz.p.core.services.service.ClientService;
//
//import java.io.IOException;
//import java.util.Collection;
//import java.util.Collections;
//
//@Component
//public class JwtFilter extends OncePerRequestFilter {
//
//    @Autowired
//    private JwtTokenProvider jwtTokenProvider;
//    @Autowired
//    private ClientService userService;
//
//    public JwtFilter(JwtTokenProvider jwtTokenProvider, ClientService userService) {
//        this.jwtTokenProvider = jwtTokenProvider;
//        this.userService = userService;
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//            throws ServletException, IOException {
//        String token = getTokenFromRequest(request);
//        System.out.println("AAAAAAAAAAAAAAAAAAAA"+ '\n'+ '\n'+ '\n'+ token + '\n'+ '\n'+ '\n'+ '\n' + jwtTokenProvider.validateToken(token));
//        if (token != null && jwtTokenProvider.validateToken(token)) {
//            if (userService.checkToken(token)) {
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                response.getWriter().write("Token has been invalidated");
//                return;
//            }
//            String login = jwtTokenProvider.getLogin(token);
//            String roleString = jwtTokenProvider.getRole(token);
//            Role role = Role.valueOf(roleString);
//            UserDetails userDetails = userService.loadUserByUsername(login);
//            Collection<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.name()));
//            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//        }
//        filterChain.doFilter(request, response);
//    }
//
//    private String getTokenFromRequest(HttpServletRequest request) {
//        String bearerToken = request.getHeader("Authorization");
//        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
//            return bearerToken.substring(7);
//        }
//        return null;
//    }
//}