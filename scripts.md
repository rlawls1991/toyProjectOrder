# Scripts

DB Table 및 Query

## MySQL

### Run MySQL Container

```
 docker run -p 3306:3306 --name mysql_boot -e MYSQL_ROOT_PASSWORD=1 -e MYSQL_DATABASE=springboot -e MYSQL_USER=kjs -e MYSQL_PASSWORD=pass -d  mysql –character-set-server=utf8 –collation-server=utf8_unicode_ci

```

MySQL 접속 정보
* database: test
* username: kjs
* password: pass
* post: 3306

### Getting into the MySQL container

```
docker exec -it kjs-mysql bash
```

## 이미 만들어진 DB의 경우 인코딩 변경 
```
ALTER DATABASE springboot CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;
```

## 테이블 생셩 쿼리

```
create table item_order (
   order_id bigint not null auto_increment,
    order_num varchar(255),
    payment_dt datetime(6),
    member_id bigint,
    primary key (order_id)
) engine=InnoDB
       
create table member (
   member_id bigint not null auto_increment,
    create_dt datetime(6),
    email varchar(255),
    name varchar(255),
    nickname varchar(255),
    password varchar(255),
    phone varchar(255),
    update_dt datetime(6),
    primary key (member_id)
) engine=InnoDB           
          
alter table item_order 
   add constraint FKhppj9i27v3bj2rpc3rje2da85 
   foreign key (member_id) 
   references member (member_id)
```


## application.properties

### Datasource

```
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/test?serverTimezone=UTC&characterEncoding=UTF-8
spring.datasource.username=kjs
spring.datasource.password=password
```
