package waa.miu.finalproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import waa.miu.finalproject.entity.User;
import waa.miu.finalproject.enums.OwnerStatusEnum;

import java.util.Collection;
import java.util.List;

public interface UserRepo extends JpaRepository<User, Long> {
    List<User> findAll();

//    User findById(long id);

    User findByEmail(String email);

    void deleteById(long id);

    Collection<User> findByStatus(OwnerStatusEnum statusEnum);
}
