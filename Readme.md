# :credit_card: PrePay [선결제 관리 어플]

2025.01.06 ~ 2025.02.21 / 6명

![Image](https://github.com/user-attachments/assets/f977abd4-3632-4829-b2a2-8e20c680c4c4)

## 프로젝트 소개

- PrePay는 여러 사람이 함께 사용하는 가게에 금액을 **미리 선결제하고**, QR 코드를 통해 간편하게 결제할 수 있도록 돕는 어플리케이션입니다.

- 사용자들은 그룹을 **생성하고 참여**할 수 있으며 그룹에 등록된 가게에서 **선결제된 금액을 사용하여 결제**할 수 있습니다.

- 그룹은 공개 그룹과 비공개 그룹으로 나누어져 있습니다.
  
   **공개 그룹** : 특정 사용자가 기부형식으로 가게에 금액을 선결제 하며 누구나 금액을 사용가능
  
   **비공개 그룹** : 회사나, 동아리 등 특정 사용자들만 사용 가능한 그룹으로 비밀번호를 통해 입장 가능  

## 팀원 구성

<table>
  <tr>
    <td align="center" width="150px">
      <a href="https://github.com/kimgihun1234" target="_blank">
        <img src="https://github.com/user-attachments/assets/73daf29c-815f-4935-ac6c-acbe89fcb72d" alt="김기훈 프로필" />
      </a>
    </td>
    <td align="center" width="150px">
      <a href="https://github.com/SWisdom1108" target="_blank">
        <img src="https://github.com/user-attachments/assets/bc37fd2b-8cb3-483e-9703-87cec7865664" alt="차현우 프로필" />
      </a>
    </td>
    <td align="center" width="150px">
      <a href="https://github.com/SeongyunGit" target="_blank">
        <img src="https://github.com/user-attachments/assets/009ca227-ce71-4300-87b8-b6c30b970caf" alt="조성윤 프로필" />
      </a>
    </td>
    <td align="center" width="150px">
      <a href="https://github.com/jacob4824" target="_blank">
        <img src="https://github.com/user-attachments/assets/650fc990-bdd2-4d72-b55e-8fb85ef52839" alt="서현석 프로필" />
      </a>
    </td>
    <td align="center" width="150px">
      <a href="https://github.com/Dufrane-S" target="_blank">
        <img src="https://github.com/user-attachments/assets/d792bd2e-8e01-4004-9599-14e541a2fbfd" alt="김성수 프로필" />
      </a>
    </td>
    <td align="center" width="150px">
      <a href="https://github.com/KyungYiHyun" target="_blank">
        <img src="https://github.com/user-attachments/assets/d6f6a979-7125-4b29-8ba1-06c7ec13c7a4" alt="경이현 프로필" />
      </a>
    </td>
  </tr>
  <tr>
    <td align="center">
      <a href="https://github.com/kimgihun1234" target="_blank">
        김기훈<br />(Android & 팀장)
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/SWisdom1108" target="_blank">
        차현우<br />(Android)
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/SeongyunGit" target="_blank">
        조성윤<br />(Android)
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/jacob4824" target="_blank">
        서현석<br />(Android)
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/Dufrane-S" target="_blank">
        김성수<br />(Back-end)
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/KyungYiHyun">
        경이현<br />(Back-end)
      </a>
    </td>
  </tr>
</table>

## 1. 개발환경

- Front-End : Kotlin, Android Studio

- Back-End : Java17, Spring boot, Spring Data JPA, Spring Security

- Infra : Amazon EC2, Docker, Jenkins, Nginx

- DB : Mysql

- Tools : Git, Jira, Notion

## 2. 프로젝트 구조

```
C:.
├─.gradle
│  ├─8.11.1
│  │  ├─checksums
│  │  ├─executionHistory
│  │  ├─expanded
│  │  ├─fileChanges
│  │  ├─fileHashes
│  │  └─vcsMetadata
│  ├─buildOutputCleanup
│  └─vcs-1
├─.idea
│  ├─dataSources
│  │  └─4063f9e9-6a34-4c40-b20e-12ee78d77031
│  │      └─storage_v2
│  │          └─_src_
│  │              └─schema
│  └─modules
├─build
│  ├─classes
│  │  └─java
│  │      ├─main
│  │      │  └─com
│  │      │      └─d111
│  │      │          └─PrePay
│  │      │              ├─aop
│  │      │              ├─bootpay
│  │      │              │  ├─request
│  │      │              │  ├─response
│  │      │              │  └─util
│  │      │              ├─config
│  │      │              ├─controller
│  │      │              ├─dto
│  │      │              │  ├─request
│  │      │              │  └─respond
│  │      │              ├─exception
│  │      │              ├─global
│  │      │              ├─model
│  │      │              ├─repository
│  │      │              ├─schedule
│  │      │              ├─security
│  │      │              │  ├─config
│  │      │              │  ├─controller
│  │      │              │  ├─dto
│  │      │              │  ├─entity
│  │      │              │  ├─jwt
│  │      │              │  ├─oauth2
│  │      │              │  ├─repository
│  │      │              │  └─service
│  │      │              ├─service
│  │      │              └─value
│  │      └─test
│  │          └─com
│  │              └─d111
│  │                  └─PrePay
│  ├─generated
│  │  ├─querydsl
│  │  │  └─com
│  │  │      └─d111
│  │  │          └─PrePay
│  │  │              ├─model
│  │  │              └─security
│  │  │                  └─entity
│  │  └─sources
│  │      └─headers
│  │          └─java
│  │              ├─main
│  │              └─test
│  ├─libs
│  ├─reports
│  │  ├─problems
│  │  └─tests
│  │      └─test
│  │          ├─classes
│  │          ├─css
│  │          ├─js
│  │          └─packages
│  ├─resources
│  │  └─main
│  ├─test-results
│  │  └─test
│  │      └─binary
│  └─tmp
│      ├─bootJar
│      ├─compileJava
│      │  └─compileTransaction
│      │      ├─backup-dir
│      │      └─stash-dir
│      ├─compileTestJava
│      │  └─compileTransaction
│      │      ├─backup-dir
│      │      └─stash-dir
│      ├─jar
│      └─test
├─gradle
│  └─wrapper
└─src
    ├─main
    │  ├─java
    │  │  └─com
    │  │      └─d111
    │  │          └─PrePay
    │  │              ├─aop
    │  │              ├─bootpay
    │  │              │  ├─request
    │  │              │  ├─response
    │  │              │  └─util
    │  │              ├─config
    │  │              ├─controller
    │  │              ├─dto
    │  │              │  ├─request
    │  │              │  └─respond
    │  │              ├─exception
    │  │              ├─global
    │  │              ├─model
    │  │              ├─repository
    │  │              ├─schedule
    │  │              ├─security
    │  │              │  ├─config
    │  │              │  ├─controller
    │  │              │  ├─dto
    │  │              │  ├─entity
    │  │              │  ├─jwt
    │  │              │  ├─oauth2
    │  │              │  ├─repository
    │  │              │  └─service
    │  │              ├─service
    │  │              └─value
    │  └─resources
    └─test
        └─java
            └─com
                └─d111
                    └─PrePay
```

## 3. 역할 분담

![Image](https://github.com/user-attachments/assets/e9d65ce7-a773-4140-b11f-12c01a5f0017)

## 4. 핵심 기능 소개

#### [메인페이지]

<img src = "https://github.com/user-attachments/assets/74314c36-bac3-44cf-b6b8-9529c05cee61" width="400" height="711" >

#### [그룹 생성]

<img src = "https://github.com/user-attachments/assets/c376c359-81b2-4786-a6af-a725605ea5c9" width="400" height="711" >

#### [공개 그룹 조회]

<img src = "https://github.com/user-attachments/assets/e4e12709-5185-40f9-9a40-1bb25b113abb" width="400" height="711" >

#### [QR 결제]

<img src = "https://github.com/user-attachments/assets/2aefd194-b5ac-44b3-84ba-5aa566f277ad" width="400" height="711" >

#### [결제 내역 조회]

<img src = "https://github.com/user-attachments/assets/2dc4a7c2-3b24-48d4-9f38-9c56b286cfe9" width="400" height="711" >

## 5. ERD

<img src = "https://github.com/user-attachments/assets/eccfa011-e3f7-4232-878c-06fb4cbc9cf0" alt="ERD 이미지">

## 6. 시스템 아키텍쳐

![Image](https://github.com/user-attachments/assets/23ae6ef7-89a1-44f2-8abe-28af51c9fe89)

## 7 . 차별점

1️⃣ **영수증 제공 및 결제 내역 공유**

- 모든 결제에 대해 **영수증을 자동 제공**하여 투명한 거래를 보장합니다.
- 모임원 전체가 **실시간으로 결제 내역을 확인**할 수 있어 관리가 용이합니다.

2️⃣ **공개 선결제 모임 지원**

- 누구나 사용할 수 있는 **공개 선결제 모임**을 지원합니다.
- 기부 형식의 선결제가 가능하며, **안전하고 투명한 운영**이 가능합니다.

3️⃣ **편리한 결제 내역 관리**

- 기존의 **수기 관리 방식의 불편함을 해소**하여 보다 체계적인 관리가 가능합니다.
- 결제 내역을 디지털화하여 **빠르고 정확한 기록 확인**이 가능합니다.

4️⃣ **유연한 금액 사용 방식**

- 선결제된 금액을 **자유롭게 설정 및 관리**할 수 있어 효율적인 비용 운용이 가능합니다.

## 8. 프로젝트 후기

**김기훈** :  선결제 앱을 개발하면서 프로젝트에서 MVVM(Model-View-ViewModel) 패턴을 사용하여 코드의 유지보수성과 확장성을 높였습니다. 팀원들간의 협업과 Jira를 활용하여 일정 관리를 진행하여 프로젝트를 잘 마무리하였습니다. 이 프로젝트를 통해 협업과 Jira, 안드로이드 디자인 패턴에 대해 학습할 수 있어 좋았습니다.  

**차현우** :  코틀린을 처음 접하면서 어려움을 많이 겪었지만 MVVM 패턴을 학습하고, 모바일 XML 디자인에서 어떤 부분을 신경 써야 하는지 알 수 있었습니다. 코틀린의 동작구조(Model-View, Adapter, RecyclerView 등)를 학습하고 모바일 프로젝트 협업의 기틀을 다질 수 있었습니다.  

**조성윤** :  코틀린을 처음 사용해봐서 초기에 어려웠지만, MVVM 패턴에 적응하면서 이걸 왜 쓰는지에 대한 이해를 하였습니다. 또한 디자인 패턴을 학습하며 어떻게 하면 가독성과 유지보수성을 높일 수 있는지 배웠고, 안드로이드 환경에서 어떻게 코드를 작성할지 배울 수 있었습니다.  

**서현석** :  안드로이드 개발을 하며 코틀린을 배우면서 모바일 앱이 어떤 식으로 구현되는지 학습할 수 있었고, 개발자로서의 저의 역량이 많이 향상된 것도 느꼈습니다. 또한 모바일 UI에 대해서 깊이 고민해 볼 수 있는 좋은 기회였습니다.  

**김성수** :  6인 프로젝트를 진행하며 Hibernate를 학습하고 도입하는 과정에서 많은 것을 배울 수 있었습니다. 뿐만 아니라 Docker, Jenkins를 이용해 EC2에 배포를 직접 해보면서 배포의 흐름을 익힐 수 있었습니다.  

**경이현** :  스프링 프레임워크의 동작 방식을 이해할 수 있었던 프로젝트였습니다. 또한, JPA가 내부적으로 어떻게 작동하는지 학습할 수 있었고, 특히 N+1 문제를 직접 경험하며 이를 해결하는 과정에서 효율적인 JPA 활용법을 익힐 수 있었습니다.  
