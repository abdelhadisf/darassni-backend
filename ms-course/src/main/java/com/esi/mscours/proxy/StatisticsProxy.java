package com.esi.mscours.proxy;

import com.esi.mscours.DTO.PayLecturesDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="ms-statistics")
@LoadBalancerClient(name = "ms-statistics")
public interface StatisticsProxy {

    @PostMapping("/api/v1/stat/addGroupe/{idGroupe}")
    ResponseEntity<?> addGroupe(@PathVariable Long idGroupe, @RequestParam String name, @RequestParam Long idTeacher);

    @PostMapping("/api/v1/stat/addLecture/{idLecture}")
   ResponseEntity<?> addlECTURE(@PathVariable Long idLecture, @RequestParam String name, @RequestParam double lecturePrice, @RequestParam Long idGroupe);

    @PostMapping("/api/v1/stat/addStudent/{idStudent}")
    ResponseEntity<?> addStuednt(@PathVariable Long idStudent, @RequestParam Long idGroupe);

    @PostMapping("/api/v1/stat/addPayment/{idPayment}")
   ResponseEntity<?> addPyment(@PathVariable Long idPayment, @RequestParam Long idLecture);


}
