package com.esi.mscours.API;


import com.esi.mscours.DTO.LectureDTO;
import com.esi.mscours.entities.Document;

import com.esi.mscours.entities.Groupe;
import com.esi.mscours.entities.Lecture;
import com.esi.mscours.model.Debited;
import com.esi.mscours.proxy.PaymentProxy;
import com.esi.mscours.repository.DocumentRepository;
import com.esi.mscours.repository.GroupeRepository;
import com.esi.mscours.repository.LectureRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


@RestController
@RequestMapping("api/v1")
public class LectureController {



    @Autowired
    private LectureRepository lectureRepository;
    @Autowired
    private GroupeRepository groupeRepository;
    @Autowired
    private  PaymentProxy paymentProxy;
    @Autowired
    private DocumentRepository documentRepository;


    @GetMapping("/lectures")
    public List<Lecture> getAllLectures() {
        return lectureRepository.findAll();
    }

    @PreAuthorize("hasRole('ROLE_TEACHER')")
    @PostMapping("/add-Lecture")
    public Lecture addLecture(@RequestBody LectureDTO lectureDTO) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH); // Updated date format
        Groupe groupe = groupeRepository.findById(lectureDTO.getIdGroupe()).orElse(null); // Use orElse(null) to avoid NoSuchElementException

        if (groupe != null && groupe.getIdTeacher().equals(lectureDTO.getIdTeacher())) {
            Lecture lecture = new Lecture();
            lecture.setGroupe(groupe);
            lecture.setDate(formatter.parse(lectureDTO.getDate())); // Parsing the combined date and time string
            lecture.setTitle(lectureDTO.getTitle());
            List<Document> documents = new ArrayList<>();
            System.out.println(lectureDTO.getDocs());
            List<String> docs = lectureDTO.getDocs();
            docs.forEach(doc -> {
                Document document = documentRepository.getById(Long.valueOf(doc));
                documents.add(document);
            });
            lecture.setDocumentList(documents);
            return lectureRepository.save(lecture);

        }
        return null;
    }




    @GetMapping("/student-get-lecture/{id}")

    public Lecture getLecture(@PathVariable(value="id") Long idLecture
    ,@RequestParam(value = "idStudent") Long idStudent) {
        System.out.println(idLecture);
        Lecture lecture = lectureRepository.findById(idLecture).get();
        System.out.println(lecture);

        if(lecture != null) {
            Debited debited = paymentProxy.getPayment(idLecture, idStudent);
            if(debited!=null){
               return lecture;
           }
           else {
               return null;
           }
        }
        else return null;

    }

    @GetMapping("/lecture/{id}")

    public Lecture getAllGroupe(@PathVariable(value="id") Long idLecture) {
        return lectureRepository.findById(idLecture).get();
    }


    @PreAuthorize("hasRole('ROLE_TEACHER')")
    @GetMapping("/teacher-groupe-lectures/{id}" )
    public  List<Lecture> getTeacherGroupeLectures(@PathVariable(value="id") Long idGroupe, @RequestParam(value = "idTeacher") Long idTeacher) {
        if(groupeRepository.findById(idGroupe).get()!=null && groupeRepository.findById(idGroupe).get().getIdTeacher()==idTeacher){
            List<Lecture> lectures=lectureRepository.findLectureByGroupe(groupeRepository.findById(idGroupe).get());
            return lectures;
        }else return null;
    }

    @PreAuthorize("hasRole('ROLE_TEACHER')")
    @PatchMapping ("/insert-roomId/{idLecture}")
            public ResponseEntity<?> insertIdRoom(@PathVariable(value = "idLecture") Long idLecture,@RequestParam(value = "roomId") int roomId) {

        Lecture lecture = lectureRepository.findById(idLecture).get();
        if (lecture != null) {
            lecture.setRoomId(roomId);
            return ResponseEntity.ok(lectureRepository.save(lecture));
        } else return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

    }





}
