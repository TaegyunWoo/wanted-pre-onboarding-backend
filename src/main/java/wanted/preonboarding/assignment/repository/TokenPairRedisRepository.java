/**
 * packageName    : wanted.preonboarding.assignment.repository
 * fileName       : TokenPairRedisRepository
 * author         : 우태균
 * date           : 2023/08/07
 * description    : 토큰 Redis Repository
 */
package wanted.preonboarding.assignment.repository;

import org.springframework.data.repository.CrudRepository;
import wanted.preonboarding.assignment.domain.TokenPair;

public interface TokenPairRedisRepository extends CrudRepository<TokenPair, String> {
}
