# Intro
---
Spring Security + JWT를 이용한 로그인 인증 구현 프로젝트입니다. 
로그인 인증과정이 서버 내부에서 어떤 흐름으로 동작이 이루어지는지 자세히 공부하기 위해 만든 프로젝트 입니다.

# Tech Stack
---
Back-end
- SpringBoot 2.7.12
- Java 11
- Spring Security + JWT
- JPA
- Mysql
- Redis

# 내부 동작 과정
---
### 로그인

1. email과 password를 가지고 UsernamePasswordAuthenticationToken 타입의 '임의의 인증 객체'를 생성한다.

2. AuthenticationManagerBuilder의 getObject() 메소드로 AuthenticationManager 객체를 가져와 내부 메소드인 authenticate()를 위에서 생성한 '임의의 인증 객체'를 인자로 넣어 호출한다.

3. AuthenticationManager는 등록된 AuthenticationProvider 목록을 순차적으로 확인하여 적합한 AuthenticationProvider를 찾아 AuthenticationProvider 내부의 authenticate()를 호출한다. (AuthenticationProvider를 구현한 클래스가 있을 경우 그 클래스가 choice됨)

4. 이 때, AuthenticationProvider의 authenticate() 메소드에서는 파라미터로 '임의의 인증 객체'를 받게 되는데 이 인증 객체를 이용하여 CustomUserDetailsService 내부의 loadUserByUsername()이라는 메소드에 인증 객체에 담은 email을 인자로 넘겨 주어 호출한다.

5. loadUserByUsername()에서 레포지토리에 email과 일치하는 사용자 객체를 가져와 CustomUserDetails 객체로 변환하여 반환한다.

6. AuthenticationProvider에서 반환받은 CustomUserDetails의 password와 '임의의 인증 객체' 담긴 password가 일치한지 검사한다. (PasswordEncoder의 matchs() 메소드 사용)

7. 비밀번호가 일치한다면 UsernamePasswordAuthenticationToken에 CustomUserDetails와 password 그리고 customUserDetails의 권한 정보를 담아 '실질적인 인증 객체'를 생성하여 리턴한다.

8. 이제 tokenProvider라는, jwt 토큰에 대한 다양한 작업(인증 객체로 jwt 토큰 생성, jwt 토큰으로부터 인증 객체 빼오기 등)이 가능한 유틸성 클래스의 createToken() 메소드에 '실질적인 인증 객체'를 인자로 넣어 호출하여 인증 객체의 사용자 정보와 유효기간을 설정하여 생성한 access 토큰을 리턴 받는다.

9. 이 jwt 토큰과 함께 클라이언트에게 응답한다. (클라이언트는 이 토큰을 내부에 저장하여 이 후 요청을 보낼 때마다 http header에 함께 보내어 토큰 인증을 받게 됨)

### 토큰 인증

1. 클라이언트로부터 http 요청이 들어오면 JwtFilter에서 헤더에 담긴 access 토큰을 jwt 토큰 형식에 맞게 풀어 저장한다.

2. 저장한 토큰의 유효성을 검사한다.

3. 그리고 저장한 토큰을 위에서 언급한 토큰의 다양한 작업이 이루어지는 TokenProvider 클래스의 getAuthentication() 메소드 인자에 담아 호출하여 '인증 객체'를 추출한다.

4. 리턴 받은 '인증 객체'를 SecurityContext에 setAuthentication() 메소드의 인자로 담아 호출하여 저장한다.

5. controller로 넘어와 @PreAuthorize에 설정된 권한과 일치하는지 검증받고 @AuthenticationPrincipal을 CustomUserDetails 객체로 받아와 결과적으로 현재 로그인한 유저에 대한 작업이 이루어지게 된다.
