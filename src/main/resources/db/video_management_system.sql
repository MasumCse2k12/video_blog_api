create database videodb;
create user video with encrypted password 'v!d@o';
grant all privileges on database videodb to video;


create table if not exists users (
    id serial primary key,
    username varchar(50) not null,
	name varchar(100) ,
	email varchar(50),
	phone varchar(20),
    password varchar(255) not null,
    status smallint default 1,
	UNIQUE(username)
);

INSERT INTO users (USERNAME, PASSWORD, NAME, email, STATUS) VALUES
('admin', '$2a$10$/yPpo36b4XBRvOyKu4lF.OJXbzLaFYQpcd/LWLnSPj0W3Q87iIKBC', 'Admin','test1@gmail.com', 1),
('user', '$2a$10$/yPpo36b4XBRvOyKu4lF.OJXbzLaFYQpcd/LWLnSPj0W3Q87iIKBC',  'User','test2@gmail.com', 2);

create table if not exists videos (
    id serial primary key,
    url varchar(255) not null,
    description text,
    view_count integer default 0,
    is_deleted boolean default false,
    uploaded_at timestamp not null default current_timestamp,
    uploaded_by smallint not null,
    CONSTRAINT fk_video_uploaded_by
      FOREIGN KEY(uploaded_by)
	  REFERENCES users(id)
);

create table if not exists video_request (
    id serial primary key,
    video_id smallint not null,
    reaction_status smallint not null,
    created_at timestamp not null default current_timestamp,
    updated_at timestamp,
    reaction_by smallint not null,
    CONSTRAINT fk_video_id
      FOREIGN KEY(video_id)
	  REFERENCES videos(id),
	CONSTRAINT fk_reaction_by
      FOREIGN KEY(reaction_by)
	  REFERENCES users(id)
);

