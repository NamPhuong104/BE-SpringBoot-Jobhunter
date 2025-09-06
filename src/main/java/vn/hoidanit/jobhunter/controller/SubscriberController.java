package vn.hoidanit.jobhunter.controller;

import jakarta.validation.Valid;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.Subscriber;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.repository.SkillRepository;
import vn.hoidanit.jobhunter.service.SubscriberService;
import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

import java.util.List;

@RequestMapping("/api/v1")
@RestController
public class SubscriberController {
    private final SubscriberService subscriberService;
    private final SkillRepository skillRepository;

    public SubscriberController(SubscriberService subscriberService, SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
        this.subscriberService = subscriberService;
    }

    @PostMapping("/subscribers")
    @ApiMessage("Create subscriber")
    public ResponseEntity<Subscriber> createSubs(@Valid @RequestBody Subscriber sub) throws IdInvalidException {
        Boolean isExist = this.subscriberService.handleCheckExistEmail(sub.getEmail());
        if (isExist) throw new IdInvalidException("Email đã tồn tại !!!");

        List<Long> skillIds = sub.getSkills().stream().map(Skill::getId).toList();
        List<Skill> listSkill = this.skillRepository.findAllByIdIn(skillIds);

        sub.setSkills(listSkill);

        Subscriber res = this.subscriberService.handleCreateSubs(sub);

        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PutMapping("/subscribers")
    @ApiMessage("Update subscriber")
    public ResponseEntity<Subscriber> updateSub(@RequestBody Subscriber sub) throws IdInvalidException {
        Subscriber currentSubs = this.subscriberService.handleFindById(sub.getId());
        if (currentSubs == null) throw new IdInvalidException("Id không tồn tại !!!");

        List<Long> skillIds = sub.getSkills().stream().map(Skill::getId).toList();
        List<Skill> listSkill = this.skillRepository.findAllByIdIn(skillIds);

        currentSubs.setSkills(listSkill);

        return ResponseEntity.ok().body(this.subscriberService.handleUpdateSubs(currentSubs));
    }

    @DeleteMapping("/subscribers/{id}")
    @ApiMessage("Delete subscriber")
    public ResponseEntity<Void> deleteSubs(@PathVariable("id") Long id) throws IdInvalidException {
        Subscriber currentSubs = this.subscriberService.handleFindById(id);
        if (currentSubs == null) throw new IdInvalidException("Id không tồn tại !!!");
        this.subscriberService.handleDeleteSubs(currentSubs.getId());
        return ResponseEntity.ok(null);
    }
}
