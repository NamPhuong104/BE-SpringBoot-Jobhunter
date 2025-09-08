package vn.hoidanit.jobhunter.service;

import org.springframework.stereotype.Service;
import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.Subscriber;
import vn.hoidanit.jobhunter.domain.response.ResEmailJob;
import vn.hoidanit.jobhunter.repository.JobRepository;
import vn.hoidanit.jobhunter.repository.SubscribersRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SubscriberService {
    private final SubscribersRepository subscribersRepository;
    private final JobRepository jobRepository;
    private final EmailService emailService;

    public SubscriberService(SubscribersRepository subscribersRepository, JobRepository jobRepository, EmailService emailService) {
        this.subscribersRepository = subscribersRepository;
        this.jobRepository = jobRepository;
        this.emailService = emailService;
    }

    public ResEmailJob convertJobToSendEmail(Job job) {
        ResEmailJob res = new ResEmailJob();
        res.setName(job.getName());
        res.setSalary(job.getSalary());
        res.setCompany(new ResEmailJob.CompanyEmail(job.getCompany().getName()));
        List<Skill> skills = job.getSkills();
        List<ResEmailJob.SkillEmail> s = skills.stream().map(x -> new ResEmailJob.SkillEmail(x.getName())).collect(Collectors.toList());
        res.setSkills(s);
        return res;
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

    public Subscriber findByEmail(String email) {
        return this.subscribersRepository.findByEmail(email);
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


    public void sendSubscribersEmailJobs() {
        List<Subscriber> listSubs = this.subscribersRepository.findAll();
        if (listSubs != null && listSubs.size() > 0) {
            for (Subscriber sub : listSubs) {
                List<Skill> listSkills = sub.getSkills();
                if (listSkills != null && listSkills.size() > 0) {
                    List<Job> listJobs = this.jobRepository.findBySkillsIn(listSkills);
                    if (listJobs != null && listJobs.size() > 0) {

                        List<ResEmailJob> arr = listJobs.stream().map(
                                job -> this.convertJobToSendEmail(job)).collect(Collectors.toList());

                        this.emailService.sendEmailFromTemplateSync(
                                sub.getEmail(),
                                "Cơ hội việc làm hot đang chờ đón bạn, khám phá ngay",
                                "job",
                                sub.getName(),
                                arr);
                    }
                }
            }
        }
    }
}
