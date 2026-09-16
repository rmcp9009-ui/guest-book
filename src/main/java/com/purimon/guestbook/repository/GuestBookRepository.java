package com.purimon.guestbook.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.purimon.guestbook.domain.GuestBook;

public interface GuestBookRepository extends JpaRepository<GuestBook, Integer> {

}
