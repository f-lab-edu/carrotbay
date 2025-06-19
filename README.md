# carrotbay
- 이베이를 벤치마킹한 경매 시스템 API 서버 토이 프로젝트입니다.
- 일정 : 2024.11 ~ 2025.05
- 개발 인원 : 1명
- Blog : https://bugglebuggle.tistory.com/category/당근베이

## 프로젝트 목표
- 단순히 기능을 구현하는 것을 넘어서, **객체지향 원칙**에 따라 유지보수하기 좋은 코드를 작성하고자 했습니다.
- **코드 리뷰**를 통해 코드의 품질을 지속적으로 개선했고, 단위 및 통합 테스트, CI/CD 자동화를 통해 안정성과 생산성을 높이는 개발 환경을 구축했습니다.

----

## 사용 기술 및 개발환경
Java, Spring Boot, IntelliJ, Gradle, SpringJPA, MySQL, Redis, GitHub Actions, Docker

----

## 기술적 이슈와 해결 과정
- 경매 기능 개발 중 비관적 락 기반 동시성 제어 도입 과정에서 발생한 데드락 문제 해결
    - [락을 건 자원은 하나인데 왜 데드락이 걸리지? 🧐 경매 기능에서 동시에 입찰이 발생할때 어떻게 해결해야할까?](https://bugglebuggle.tistory.com/36 "tistory 링크")

- 여러 도메인에서 전역적으로 쓰일 수 있는 history 생성 기능 구현
    - [ 💻 history 내역을 남기는 방법은? Trigger, AOP, @EntityListener ](https://bugglebuggle.tistory.com/37 "tistory 링크")
    - [ history 내역 생성 기능을 리팩토링해보자 😤 ](https://bugglebuggle.tistory.com/38 "tistory 링크")

----

## 프로젝트 아키텍처 
![Image](https://github.com/user-attachments/assets/73d55fa6-e666-4840-86ce-630903f0974e)

----

## 주요 기능
1. 회원가입 / 탈퇴
2. 로그인 로그아웃
3. 회원 정보 수정
4. 경매글 작성 / 수정 / 삭제 / 조회
5. 경매 입찰 / 경매 취소 / 경매 확정
6. 후기 작성 / 수정 / 삭제 / 조회
7. 데이터 update, delete시 작성 시 history 내역 기록

----

## Use Case 

https://github.com/f-lab-edu/carrotbay/wiki
