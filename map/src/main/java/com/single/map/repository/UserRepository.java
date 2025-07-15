package com.single.map.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.single.map.model.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String>{

	Optional<UserEntity> findByUserId(String userId);

	boolean existsByUserId(String userId);
	boolean existsByNickName(String nickName);

	Optional<UserEntity> findByNameAndEmail(String name, String email);

	List<UserEntity> findAllByNameAndEmail(String name, String email);

	Optional<UserEntity> findByUserIdAndEmail(String userId, String email);
}
