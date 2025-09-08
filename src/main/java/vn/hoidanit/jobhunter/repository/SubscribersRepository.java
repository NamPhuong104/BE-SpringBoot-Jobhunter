package vn.hoidanit.jobhunter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import vn.hoidanit.jobhunter.domain.Subscriber;

@Repository
public interface SubscribersRepository extends JpaRepository<Subscriber, Long>, JpaSpecificationExecutor<SubscribersRepository> {
    boolean existsByEmail(String email);

    Subscriber findByEmail(String email);
}
