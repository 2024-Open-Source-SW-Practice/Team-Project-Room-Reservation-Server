-- V1__init.sql

-- 1. members 테이블 생성
CREATE TABLE IF NOT EXISTS members (
    is_admin BIT NOT NULL,
    member_id BIGINT NOT NULL AUTO_INCREMENT,
    college VARCHAR(255),
    department VARCHAR(255),
    email VARCHAR(255),
    login_id VARCHAR(255) NOT NULL,
    name VARCHAR(255),
    password VARCHAR(255),
    phone_number VARCHAR(255),
    type ENUM('GRADUATE', 'PROFESSOR', 'UNDERGRADUATE') NOT NULL,
    PRIMARY KEY (member_id)
) ENGINE=InnoDB;

-- 2. applications 테이블 생성
CREATE TABLE IF NOT EXISTS applications (
    application_id BIGINT NOT NULL AUTO_INCREMENT,
    start_time TIMESTAMP(0) NOT NULL,
    end_time TIMESTAMP(0) NOT NULL,
    applied_date DATE NOT NULL,
    status VARCHAR(255) NOT NULL DEFAULT 'PENDING',
    applicant_count INTEGER,
    member_id BIGINT,
    PRIMARY KEY (application_id),
    FOREIGN KEY (member_id) REFERENCES members(member_id)
) ENGINE=InnoDB;

-- 3. applicant 테이블 생성
CREATE TABLE IF NOT EXISTS applicant (
    id BIGINT NOT NULL AUTO_INCREMENT,
    member_id BIGINT,
    application_id BIGINT,
    PRIMARY KEY (id),
    FOREIGN KEY (member_id) REFERENCES members(member_id),
    FOREIGN KEY (application_id) REFERENCES applications(application_id)
) ENGINE=InnoDB;

-- 4. application_co_participants 테이블 생성
CREATE TABLE application_co_participants (
    application_id BIGINT NOT NULL,  -- 외래키로 Application 테이블 참조
    name VARCHAR(255),               -- CoParticipant의 이름
    phone_number VARCHAR(255),       -- CoParticipant의 전화번호
    FOREIGN KEY (application_id) REFERENCES applications(application_id)
);