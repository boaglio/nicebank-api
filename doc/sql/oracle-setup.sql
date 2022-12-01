
create table bank_account
(account_id NUMBER CONSTRAINT pk_bank_account PRIMARY key, balance number, user_account_id number);

create table user_account
(id NUMBER CONSTRAINT pk_user_account PRIMARY key, 
 name varchar(255) constraint nn_user_account_name NOT null);

alter table bank_account 
  add constraint fk_bank_Account_ua 
  foreign key (user_account_id) references user_account(id);
 
CREATE SEQUENCE seq_id_user_account START WITH 10001;

CREATE SEQUENCE seq_id_bank_account START WITH 50001;
