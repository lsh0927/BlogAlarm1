    package com.example.blogalarm.service;
    import com.example.blogalarm.domain.Member;


    import com.example.blogalarm.form.LoginForm;
    import com.example.blogalarm.repository.MemberRepositoryImpl;
    import jakarta.servlet.http.HttpServletRequest;
    import jakarta.servlet.http.HttpSession;
    import lombok.RequiredArgsConstructor;
    import org.springframework.stereotype.Service;
    import org.springframework.transaction.annotation.Transactional;

    import java.util.List;
    import java.util.Objects;
    import java.util.Optional;

    @Service
    @Transactional
    @RequiredArgsConstructor
    public class MemberService {

        private final MemberRepositoryImpl memberRepository;

        /*
        * 회원가입 (API 적용전)
        */
        @Transactional
        public Long join(Member member){

            validateDuplicateMember(member);
            memberRepository.save(member);
            return member.getId();
        }

        @Transactional
        public Long createMemberWithUserInfo(String email, String nickname, int password) {
            // Member 객체 생성
            Member member = new Member();
            member.setNickname(nickname);
            member.setPassword(password);
            member.setEmail(email);

            // 중복 회원 검증
            validateDuplicateMember(member);

            // 회원 저장
            memberRepository.save(member);
            return member.getId();
        }

        //중복회원 검증
        private void validateDuplicateMember(Member member) {
            Optional<Member> findMembers = memberRepository.findByUsername(member.getNickname());
            if (!findMembers.isEmpty()) {
                throw new IllegalStateException("이미 존재하는 회원입니다.");
            }
        }
        //회원 전체 조회
        public List<Member> findMembers() {
            return memberRepository.findAll();
        }

        //회원 조회
        public Member findOne(Long memberId) {
            return memberRepository.findOne(memberId);
        }

        /*
         * 회원 수정
         */
        @Transactional
        public void update(Long id, String nickname, int password) {
            Member member = memberRepository.findOne(id);
            member.setNickname(nickname);
            member.setPassword(password);
            memberRepository.save(member);
        }
        //void 대신 그냥 멤버를 반환했을때, 커맨드와 쿼리를 철저히 분리하지 못하는 경우가 생김
        //영속성 컨텍스트를 생각해보자.
        // MemberService의 login 메서드 변경


        public Member login(LoginForm loginForm, HttpServletRequest request) {
            String nickname = loginForm.getNickname();
            int password = loginForm.getPassword();

            //loginForm의 닉네임 패스워드와 저장된 정보가 같은지 확인
            Optional<Member> members = memberRepository.findByUsername(nickname);

            if (!members.isEmpty()) {
                Member member = members.get();
                System.out.println("Found member: " + member);

                if (Objects.equals(password, member.getPassword())) {
                    // 로그인 성공 시 세션에 사용자 정보 저장
                    HttpSession session = request.getSession(true);
                    session.setAttribute("loggedInMember", member);
                    return member;
                }
            }

            System.out.println("멤버가 없는데욤?");
            return null;
        }


        public Member getLoggedInMember(HttpServletRequest request) {
            HttpSession session = request.getSession(false); // 세션이 없으면 새로 생성하지 않음

            if (session != null) {
                Member loggedInMember = (Member) session.getAttribute("loggedInMember");
                if (loggedInMember != null) {
                    return loggedInMember;
                }
            }

            return null; // 로그인되지 않은 상태
        }

        //닉네임으로 멤버 반환
        public Member getMemberByNickname(String nickname) {
            Optional<Member> members = memberRepository.findByUsername(nickname);

            if (!members.isEmpty()) {
                return members.get();
            } else {
                throw new RuntimeException("사용자를 찾을 수 없습니다: " + nickname);
            }
        }

        public Member getMemberById(Long memberId) {
            return memberRepository.findOne(memberId);
        }

        public void addEmailInfo(Member member,String userEmailInfo) {
            member.setEmail(userEmailInfo);
        }
    }
