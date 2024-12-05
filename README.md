<img src="https://github.com/user-attachments/assets/abec3805-cd45-4d3d-99ae-638e38b00fcb" width="200px">


## TeamPu : 팀프실 야간잔류 신청 시스템

컴퓨터공학전공 팀프로젝트실 야간잔류 신청이 번거롭지 않으셨나요?<br>
이제는 TeamPu로 편하게 예약하고 사용하세요! 🎉

## 🖥️  프로젝트 개요

오픈소스SW실습 팀프로젝트로 출발한 이 프로젝트는,<br>
TeamPu는 학우들의 편의성과 효율성을 극대화하기 위해 설계된<br>
컴퓨터공학전공 팀프로젝트실 위한 야간잔류 신청 시스템입니다.


## 🚀 주요 기능

| **기능**      | **설명**                          |
|--------------------|--------------------------------------|
| **야간잔류 신청**       | 간단한 절차를 통해 야간 잔류를 신청할 수 있습니다.                          |
| **실시간 신청 상태 확인**| 현재 신청한 팀의 수를 쉽게 확인할 수 있습니다.         |
| **나의 신청 내역 확인**          | 내가 예약한 내역을 쉽게 확인할 수 있습니다.                  |
| **[관리자] 신청 내역 확인**          | 신청된 야간잔류 상세를 확인할 수 있습니다.                  |
| **[관리자] 야간잔류 승인 및 거부**          | 신청된 야간잔류를 승인, 또는 거부할 수 있습니다.              |


## 📂 사용 기술

### Front-end(Client)

| **Category**      | **Details**                          |
|--------------------|--------------------------------------|
| **Language**       | JavaScript                          |
| **Library / Package**| React, TailwindCSS, Axios           |
| **Tools**          | Git, VS Code, Figma, Discord, Vercel                |

### Back-end(Server)

| **Category**      | **Details**                          |
|--------------------|--------------------------------------|
| **Language**       | Java                         |
| **Framework / Tech Stack**| Spring, JPA, MySQL, Redis           |
| **Tools**          | Git, InteliJ, Figma, Discord               |

### Infrastructure

| **Category**      | **Details**                          |
|--------------------|--------------------------------------|
| **Server**          | GCP, GCS |
| **CI / CD**          | Github Actions, K3S, JACOCO |
| **Database**          | Flyway        |

## 👥 개발 참여 인원

| **이름**           | **역할**                            |
|--------------------|--------------------------------------|
| 19 정재우         | `Backend Lead`          |
| 20 이유성         | `Backend` `Infrastructure`              |
| 20 김진형         | `Backend` `Infrastructure`               |
| 20 이정근         | `Infrastructure Lead`             |
| 21 김아현         | `Backend` `Infrastructure`              |
| 23 한유진         | `Frontend`              |

## ⚙️ 전체 시스템 실행 방법

### 로컬 서버 실행

- `git clone https://github.com/TeamPu/TeamPu-Server.git` 명령어를 통해 원하는 디렉토리에 소스 코드를 다운로드 받습니다.
- `IntelliJ` 등의 IDE를 통해 프로젝트를 열고, 터미널 루트 경로에서 `docker-compose up -d` 명령어를 통해 DB 환경을 세팅합니다.
- 이후 `TeamPuApplication.java` 클래스를 실행하면 `localhost:8080` 경로를 통해 다양한 API에 접근할 수 있습니다.

### 배포 서버 실행

- 현재 저희 배포 서버는 [해당 링크](https://teampu.shop)를 통해 상시 접속할 수 있습니다.
- 현재 Github Actions을 통한 CI/CD, GCP, K3S를 통한 무중단 배포를 적용한 상태입니다. 24시간 중단 없이 서버를 구동 중에 있습니다.
- 서버 환경설정 및 조작은 GCE 관리자 계정을 통해 가능합니다.

## 🌟 미리보기

<table>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/f70426cb-7679-4a88-9e6d-3159a7ba94b5"></td>
    <td><img src="https://github.com/user-attachments/assets/0bb0f598-9e9b-468c-8d66-d90bd02474b3"></td>
    <td><img src="https://github.com/user-attachments/assets/0a342635-c68f-4b0b-87e4-ed4bc0c951ca"></td>
  </tr>
</table>






