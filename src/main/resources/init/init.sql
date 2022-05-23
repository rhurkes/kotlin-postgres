set timezone = 'America/Chicago';

create table if not exists data (
   id serial primary key,
   text1 varchar(50) not null,
   text2 varchar(50) not null,
   text3 varchar(255) not null,
   create_ts timestamp not null,
   last_ts timestamp not null,
   jsonb_column json not null
);

insert into data (text1, text2, text3, create_ts, last_ts, jsonb_column) values(
    'This is the first',
    'This is the second',
    'This is the third, and boy is there a lot of stuff in this one because there is so much to say',
    '2022-05-01',
    '2022-06-01',
    '{"id": 1,"text1": "This is the first","text2": "This is the second","text3": "This is the third, and boy is there a lot of stuff in this one because there is so much to say","create_ts": "2022-05-01 00:00:00","last_ts": "2022-06-01 00:00:00"}'
)