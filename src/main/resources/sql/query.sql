create table memo
(
    id   int primary key auto_increment,
    text varchar(50) not null
);

create table diary
(
    id          int primary key auto_increment,
    weather     varchar(50)  not null,
    icon        varchar(50)  not null,
    temperature double       not null,
    text        varchar(500) not null,
    date        date         not null
);

create table weather
(
    date        date primary key,
    weather     varchar(50) not null,
    icon        varchar(50) not null,
    temperature double      not null
);