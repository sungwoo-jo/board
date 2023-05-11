create table t_user (
                        id int auto_increment primary key not null,
                        name varchar(100) not null,
                        username varchar(100) not null,
                        password varchar(130) not null,
                        email varchar(50) not null,
                        nickname varchar(20) not null,
                        join_date datetime not null,
                        serial_num varchar(100),
                        oauth varchar(10)
);