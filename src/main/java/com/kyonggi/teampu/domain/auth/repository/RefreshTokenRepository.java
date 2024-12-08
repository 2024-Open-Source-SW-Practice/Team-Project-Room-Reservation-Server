package com.kyonggi.teampu.domain.auth.repository;

import com.kyonggi.teampu.domain.auth.domain.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken,String> {
}
