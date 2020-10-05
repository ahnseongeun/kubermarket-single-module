package com.example.kubermarket.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User,Long> {

    Optional<User> findBynickNameOrEmail(String nickName, String Email);

    Optional<User> findByEmail(String email);

    User findByNickName(String userId);
}
