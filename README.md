![header](https://capsule-render.vercel.app/api?type=wave&color=auto&height=300&section=header&text=graphqlserver&fontSize=90)

# graphqlserver

간단한 GraphQL 기반 도서-저자 관리 샘플 프로젝트

## 요약
Spring Boot + Spring GraphQL, JPA(MySQL)로 구현된 간단한 도서관리(GraphQL API) 샘플 애플리케이션입니다.

## 주요 기능
- Author, Book 엔티티 CRUD (GraphQL Query / Mutation)
- 저자와 도서 간 연관 관계 조회 (fetch join 제공)
- 제목/저자/가격 등 조건 검색
- GraphiQL 인터페이스 제공 (개발용)

## 기술 스택
- Java 21
- Spring Boot 4.0.2
- Spring for GraphQL
- Spring Data JPA (Hibernate)
- MySQL 8 (runtime)
- Lombok
- Gradle(Wrapper)

## 요구사항
- JDK 21
- Docker (로컬 MySQL을 도커로 띄울 경우)

## 빠른 시작 (개발환경 추천)
1. Docker로 MySQL 띄우기 (repo의 `infra/docker-compose.yml` 사용):

   Shell에서:

   ```shell
   cd .\infra
   docker-compose up -d
   ```

   - 기본 DB 설정은 `devdb`, 사용자 `devuser`, 비밀번호 `devpassword` 입니다.
   - `infra/mysql/init.sql` 파일이 초기화 스크립트로 마운트되어 있습니다.

2. 애플리케이션 실행

   프로젝트 루트에서 Gradle Wrapper로 실행합니다 (Windows PowerShell):

   ```shell
   .\gradlew bootRun
   ```

   또는 빌드 후 실행:

   ```shell
   .\gradlew build
   java -jar build\libs\graphqlserver-0.0.1-SNAPSHOT.jar
   ```

3. GraphiQL 접속

   - 기본 GraphQL 엔드포인트: `http://localhost:8080/graphql`
   - GraphiQL UI: `http://localhost:8080/graphiql` (application.yml에서 `spring.graphql.graphiql.enabled: true`로 설정됨)

## 설정
- 데이터베이스 및 포트 설정은 `src/main/resources/application.yml`에서 변경하세요.
  - url: `jdbc:mysql://localhost:3306/devdb`
  - user: `devuser`
  - password: `devpassword`.
- 개발시 Hibernate DDL: `spring.jpa.hibernate.ddl-auto: update` (개발용)

## GraphQL 스키마 요약
스키마 파일: `src/main/resources/graphql/schema.graphqls`

- 타입: `Author`, `Book`
- 입력: `AuthorInput`, `AuthorUpdateInput`, `BookInput`, `BookUpdateInput`
- Query 예시:
  - `getAllAuthors`, `getAuthorById(id: ID!)`, `getAllBooks`, `getBookById(id: ID!)`, `searchBooks(keyword: String!)` 등
- Mutation 예시:
  - `createAuthor(authorInput: AuthorInput!)`, `updateAuthor(authorUpdateInput: AuthorUpdateInput!)`, `deleteAuthor(id: ID!): Boolean!`
  - `createBook(bookInput: BookInput!)`, `updateBook(bookUpdateInput: BookUpdateInput!)`, `deleteBook(id: ID!): Boolean!`

> 날짜/시간 입력 포맷: 컨트롤러 및 서비스에서 `DateTimeFormatter.ISO_LOCAL_DATE_TIME` (예: `2023-12-31T15:30:00`) 형태를 사용합니다.

## 주요 클래스 및 파일
- `src/main/java/com/example/graphqlserver/GraphqlserverApplication.java` - 애플리케이션 진입점

컨트롤러 (GraphQL 핸들러):
- `src/main/java/com/example/graphqlserver/controller/AuthorController.java`
- `src/main/java/com/example/graphqlserver/controller/BookController.java`

서비스:
- `src/main/java/com/example/graphqlserver/service/AuthorService.java`
- `src/main/java/com/example/graphqlserver/service/BookService.java`

엔티티:
- `src/main/java/com/example/graphqlserver/entity/Author.java` (fields: id, name, email, bio, birthDate, nationality, books, createdAt, updatedAt)
- `src/main/java/com/example/graphqlserver/entity/Book.java` (fields: id, title, isbn, description, price, publishedDate, pageCount, createdAt, updatedAt, author)

레포지토리 (JPA):
- `src/main/java/com/example/graphqlserver/repository/AuthorRepository.java`
- `src/main/java/com/example/graphqlserver/repository/BookRepository.java`

리소스:
- GraphQL 스키마: `src/main/resources/graphql/schema.graphqls`
- 애플리케이션 설정: `src/main/resources/application.yml`
- Docker compose: `infra/docker-compose.yml` (MySQL 개발용)

## 예제 쿼리 / 뮤테이션
- 저자 생성
```graphql
mutation {
    createAuthor(authorInput: {
        name: "dean",
        email: "dean@example.com",
        bio: "다양한 책 저자",
        birthDate: "1990-04-03T00:00:00",
        nationality: "Korea"
    }) {
        id
        name
        email
    }
}
```

- 도서 생성(저자 연결)
```graphql
mutation {
    createBook(bookInput: {
        title: "이것이 mysql 이다.",
        isbn: "111-123123123",
        description: "이것이 시리즈 1번째 책",
        price: 29900,
        pageCount: 500,
        publishedDate: "2026-02-01T00:00:00",
        authorId: 1
    }) {
        id
        title
        author {
            name
        }
    }
}
```

- 특정 저자와 그의 모든 책 조회
```graphql
query {
  getAuthorWithBooks(id: 1) {
    id
    name
    email
    nationality
    books {
      id
      title
      price
    }
  }
}
```

- 특정 책과 저자 정보 함께 조회
```graphql
query {
  getBookWithAuthor(id: 1) {
    id
    title
    price
    author {
      id
      name
      email
      nationality
    }
  }
}
```

- 특정 국적의 저자가 쓴 책 조회
```graphql
query {
  getBooksByAuthorNationality(nationality: "korea") {
    id
    title
    price
    author {
      name
      nationality
    }
  }
}
```

- 모든 저자와 그들의 책 조회
```graphql
query {
  getAuthorsWithBooks {
    id
    name
    books {
      id
      title
    }
  }
}
```

- 모든 책과 저자 정보 조회
```graphql
query {
  getBooksWithAuthors {
    id
    title
    price
    author {
      name
      email
    }
  }
}
```

- 특정 저자 이름으로 책 검색
```graphql
query {
  getBooksByAuthorName(authorName: "dean") {
    id
    title
    publishedDate
    author {
      name
      nationality
    }
  }
}
```

- 키워드로 도서 검색
```graphql
query {
    searchBooks(keyword: "이것이") {
        id
        title
        description
    }
}
```

