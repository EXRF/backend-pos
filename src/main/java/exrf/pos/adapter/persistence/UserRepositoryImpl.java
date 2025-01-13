package exrf.pos.adapter.persistence;

import exrf.pos.domain.User;
import exrf.pos.repository.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepositoryImpl extends JpaRepository<User, Long>, UserRepository {
    @Override
    default Optional<User> findById(Long id) {
        return findById(id);
    }

    @Override
    default User save(User user) {
        return save(user);
    }

    @Override
    default void deleteById(Long id) {
        deleteById(id);
    }
}
