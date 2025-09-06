package vn.hoidanit.jobhunter.service;

import org.springframework.stereotype.Service;
import vn.hoidanit.jobhunter.domain.Subscriber;
import vn.hoidanit.jobhunter.repository.SubscribersRepository;

import java.util.Optional;

@Service
public class SubscriberService {
    private final SubscribersRepository subscribersRepository;

    public SubscriberService(SubscribersRepository subscribersRepository) {
        this.subscribersRepository = subscribersRepository;
    }

    public Boolean handleCheckExistEmail(String email) {
        return subscribersRepository.existsByEmail(email);
    }

    public Subscriber handleFindById(long id) {
        Optional<Subscriber> currentSubs = this.subscribersRepository.findById(id);
        if (currentSubs.isPresent()) {
            return currentSubs.get();
        }
        return null;
    }

    public Subscriber handleCreateSubs(Subscriber subs) {
        return this.subscribersRepository.save(subs);
    }

    public Subscriber handleUpdateSubs(Subscriber subs) {
        return this.subscribersRepository.save(subs);
    }

    public void handleDeleteSubs(Long id) {
        subscribersRepository.deleteById(id);
    }
}
