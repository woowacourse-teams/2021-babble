# 🌎 babble

<div align="center">
  <img src="./front/public/readme/github_readme.png" />
</div>

## 🚀 1. Introduction

키워드를 기반으로 한 빠른 게임 팀 매칭 서비스입니다.

> 함께 게임할 사람이 필요한 사용자들이 키워드를 통해 빠르게 팀을 찾고, 게임을 할 수 있습니다.

## 💚 2. About Us

### Back-End

- [루트](https://github.com/Junroot)
- [와일더](https://github.com/lns13301)
- [포츈](https://github.com/unluckyjung)
- [현구막](https://github.com/Hyeon9mak)

### Front-End

- [그루밍](https://github.com/ddongule)
- [피터](https://github.com/iborymagic)

## 🔫 3. Team Culture

<details> 
<summary><b>🍚 Work Life Balance</b></summary>
<div markdown="1">

#### Babble에서 일하는 시간

- 월: 13:00 - 18:00
- 화~금: 10:00 - 18:00
- 토~일: 자유(필요하면)
- 점심시간: 12:00 - 13:30
- 일간 감정회고 : 17:30 - 18:00
- 필요하면 팀에 요청해서 야근 가능(주말 포함)

</div>
</details>

<details> 
<summary><b>🤔 Daily Scrum</b></summary>
<div markdown="1">

- 월요일 1시, 그외 오전 10시 부터 **최대** 30분간 데일리 스크럼 진행
- 스크럼 내용은 전날 무엇을 했는지에 대한 공유
- 전날 발생했던 이슈 및 일정의 수정이 필요할 경우 요청
  (만약 긴 논의(10분이상)가 필요한 경우는 별도의 회의를 만들어 진행)
- **스크럼 마스터**는 매일 돌아가면서 함
  - 현구막 (07.02 금 기준 시작)
  - 포츈
  - 와일더
  - 그루밍
  - 피터
  - 루트

</div>
</details>

<details> 
<summary><b>❤️ Emotional Retrospective</b></summary>
<div markdown="1">

- 매일 오후 5시 30분에 감정 회고
- 각자 오늘 하루동안 있었던 일에 대한 회고

</div>
</details>

<details> 
<summary><b>📋 About Discussion</b></summary>
<div markdown="1">

- 회의 시작전에는 **회의 목표**를 명확하게 작성
- **회의 진행자**는 회의를 개최한 사람이 됨 (정기 회의는 **스크럼 마스터**가 맡음)
- 회의 중에 **다른 회의 주제**가 발생하면 별도의 회의를 계획하고 현재는 이야기 하지 않음
- 최소한의 **강제성**을 두고, 그 속에서 **유연성**을 가지기
- 각자 맡은 파트에 대해 기획 → 회의 때 발표 → 이의 제기 / 수용 → 적용
- 회의는 **시간 제한** 무조건 필요(마라톤 회의 X, 최대 30분)
- 최대한 미리미리 회의 내용에 대해 준비해오기
- 아니면 아니라고 확실하게 말하기
  - 대신 끝까지 다 듣고! 말 끊지 말기
- 일정 시간동안 토론 하다가 (토론 하기 전에 정하기) 결론이 안나면 투표로 하기

</div>
</details>

<details> 
<summary><b>⏱️ Don't be Late!</b></summary>
<div markdown="1">

- 10시 1분은 10시가 아니다!
- 기본 지각비(10시 1분 - 10시 10분): 10,000원
- 추가 10분 당: 1,000원

</div>
</details>
<br />

## 💊 4. Conventions

### Commit Message Format

#### [Angular JS Git Commit Message Conventions](https://docs.google.com/document/d/1QrDFcIiPjSLDn3EL15IJygNPiHORgU1_OOAqWjiDU5Y/edit)

<details> 
<summary><b>🪜 Items</b></summary>
<div markdown="1">

- `feat`: 새로운 기능
- `fix`: 버그를 수정
- `refactor`: 이미 있는 코드에 대한 리팩토링
- `css`: CSS 관련 수정
- `style`: 코드 포매팅에 관한 스타일 변경
- `docs`: Document 변경 사항
- `test`: Test Code에 대한 commit
- `build`: 빌드 관련 파일 수정 (예시 scope: gulp, broccoli, npm)
- `perf`: 성능 개선사항
- `ci`: CI 설정 파일 수정 (예시 scope: Circle, BrowserStack, SauceLabs)
- `chore`: 그 외의 작은 수정들

</div>
</details>
<br />

- Commit Message는 한국어를 사용
- Angular JS Git Commit Message Conventions를 따르되, Scope는 명시하지 않음

### Issue

- 해야 할 Task를 미리 Issue에 등록 후 개발

### Pull Request

- Issue에 올라온 Task를 끝내면, Pull Request를 통해 팀원들의 Review를 받은 후, develop 브랜치에 merge 함

### Branch Strategy

#### `main`

- 제품을 최종적으로 배포하는 브랜치 (develop 브랜치로부터 merge만 받는 브랜치)
- 배포에 사용됨

#### `develop`

- 아직 배포되지 않은 공용 브랜치
- feature 브랜치로부터 merge를 받는다. 개발 중에 버그를 발견하면 이 브랜치에 직접 commit함

#### `feature`

- 새로운 기능 개발을 하는 브랜치
  - 반드시 develop 로부터 시작되고, develop 브랜치에 머지함
  - **feature/기능이름**
    ex) `feature/new-feature`

#### `hotfix`

- 다음 배포 전까지 급하게 고쳐야되는 버그를 처리하는 브랜치
  - 배포 버전 심각한 버그 수정이 필요한경우, 버그 수정을 진행한뒤 main, develop 브랜치에 merge함
  - **hotfix/버그이름**
    ex) `hotfix/bugs`

## 💻 5. Tech Stacks

- `Java 11`
- `Nginx`
- `Maria DB`
- `Docker`
- `Spring Boot`
- `Javascript`
- `React.js`
