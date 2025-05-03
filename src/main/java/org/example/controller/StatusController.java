
package org.example.controller;

import jakarta.transaction.Transactional;
import org.example.entity.Status;
import org.example.repo.StatusRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/status")
@Controller
public class StatusController {
    private final StatusRepository statusRepository;

    public StatusController(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    @PostMapping("/create")
    public String createStatus(@ModelAttribute Status status) {
        status.setName(status.getName().toUpperCase());
        statusRepository.save(status);
        return "redirect:/";
    }

    @GetMapping("/update")
    public String getStatusUpdatePage(Model model) {
        List<Status> statuses = statusRepository.findAllByOrderByPositionNumberAsc();
        List<Integer> positionNumbers = new ArrayList<>();
        for (Status status : statuses) {
            positionNumbers.add(status.getPositionNumber());
        }
        model.addAttribute("statuses", statuses);
        model.addAttribute("positionNumbers", positionNumbers);
        return "update-status-order";

    }

    @Transactional
    @PostMapping("/update")
    public String updateStatusPositionOrder(
            @RequestParam("name") String[] names,
            @RequestParam("id") Long[] ids,
            @RequestParam("newPositionNumber") Integer[] newPositionNumbers,
            @RequestParam(value = "isActive") String[] isActive
    ) {
        List<Status> editedStatuses = new ArrayList<>();
        for (int i = 0; i < ids.length; i++) {
            Boolean active = false;
            if (isActive[i].equals("true")) active = true;
            Status newStatus = Status.builder()
                    .id(ids[i].intValue())
                    .name(names[i])
                    .positionNumber(newPositionNumbers[i])
                    .isActive(active)
                    .build();
            editedStatuses.add(newStatus);
        }
        statusRepository.saveAll(editedStatuses);
        return "redirect:/";
    }
}
