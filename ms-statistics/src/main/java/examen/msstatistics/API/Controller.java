package examen.msstatistics.API;

import examen.msstatistics.dao.*;
import examen.msstatistics.enities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.bouncycastle.asn1.x500.style.RFC4519Style.name;

@RestController
@RequestMapping("api/v1/stat")
public class Controller {

    @Autowired
    private GroupeRepository groupeRepository;
    @Autowired
    private TeacherRepository teacherRepository;

    @PostMapping("/addTeacher/{idTeacher}")
    public ResponseEntity<?> addTeacher(@PathVariable Long idTeacher) {

        Teacher teacher = new Teacher() ;
        teacher.setIdTeacher(idTeacher);
        teacher.setGroupes(new ArrayList<Groupe>());

        return ResponseEntity.ok(teacherRepository.save(teacher));
    }


    @PostMapping("/addGroupe/{idGroupe}")
    public ResponseEntity<?> addGroupe(@PathVariable Long idGroupe, @RequestParam String name, @RequestParam Long idTeacher) {
        Teacher teacher = teacherRepository.findTeacherByIdTeacher(idTeacher);

        if (teacher == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Teacher not found.");
        }

        Groupe groupe = new Groupe();
        groupe.setIdGroupe(idGroupe);
        groupe.setName(name);
        groupe.setDateGroupe(new Date());
        groupe.setStudents(new ArrayList<Student>());
        // Save the Groupe first to ensure it has an ID
        groupe.setLectures(new ArrayList<Lecture>());
        groupe = groupeRepository.save(groupe);

        // Now add this groupe to the teacher's list of groupes
        List<Groupe> groupes = teacher.getGroupes();



        groupes.add(groupe);
        teacher.setGroupes(groupes);


        // Save the teacher with the new groupe
        teacherRepository.save(teacher);
        return ResponseEntity.ok(groupe);
    }


    @Autowired
    private LectureRepository lectureRepository;

    @PostMapping("/addLecture/{idLecture}")
    public ResponseEntity<?> addLecture(@PathVariable Long idLecture, @RequestParam String name, @RequestParam double lecturePrice, @RequestParam Long idGroupe) {
        Groupe groupe = groupeRepository.findGroupeByIdGroupe(idGroupe);
        if (groupe == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Group not found.");
        }

        Lecture lecture = new Lecture();
        lecture.setIdLecture(idLecture);
        lecture.setLecturePrice(lecturePrice);
        lecture.setName(name);
        lecture.setPayments(new ArrayList<Payment>());

        // Save the Lecture first to ensure it has an ID
        lecture = lectureRepository.save(lecture);

        List<Lecture> lectures = groupe.getLectures();
            lectures.add(lecture);
            groupe.setLectures(lectures);
        groupeRepository.save(groupe);

        return ResponseEntity.ok(lecture);
    }


    @Autowired
    private StudentRepository studentRepository;





    @PostMapping("/addStudent/{idStudent}")
    public ResponseEntity<?> addStudent(@PathVariable Long idStudent, @RequestParam Long idGroupe) {
        Groupe groupe = groupeRepository.findGroupeByIdGroupe(idGroupe);
        if (groupe == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Group not found.");
        }

        Student student = new Student();
        student.setIdStudent(idStudent);

        // Save the Student first to ensure it has an ID
        student = studentRepository.save(student);
        List<Student> students = groupe.getStudents();

            students.add(student);
            groupe.setStudents(students);



        // Now add this student to the groupe's students
        groupeRepository.save(groupe);

        return ResponseEntity.ok(student);
    }


    @Autowired
    private PaymentRepository paymentRepository;

    @PostMapping("/addPayment/{idPayment}")
    public ResponseEntity<?> addPayment(@PathVariable Long idPayment, @RequestParam Long idLecture) {
        Lecture lecture = lectureRepository.findLectureByIdLecture(idLecture);
        if (lecture == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Lecture not found.");
        }

        Payment payment = new Payment();
        payment.setIdPayment(idPayment);
        payment.setDatePayment(new Date());
        // Save the Payment first to ensure it has an ID
        payment = paymentRepository.save(payment);

        List<Payment> payments = lecture.getPayments();
            payments.add(payment);
            lecture.setPayments(payments);



        lectureRepository.save(lecture);

        return ResponseEntity.ok(payment);
    }





    @GetMapping("/nbGroupes/{idTeacher}")
    public int getNbGroupes(@PathVariable Long idTeacher) {

        return teacherRepository.findTeacherByIdTeacher(idTeacher).getGroupes().size();

    }

    @GetMapping("/nbtLectures/{idTeacher}")
    public int getNbtLectures(@PathVariable Long idTeacher) {
        List<Groupe> groupes = teacherRepository.findTeacherByIdTeacher(idTeacher).getGroupes();

        int count = 0;
        for (Groupe group : groupes){

            count += group.getLectures().size() ;
        }
        return count;
    }


    @GetMapping("/nbtStudents/{idTeacher}")
    public int getNbtStudents(@PathVariable Long idTeacher) {
        List<Groupe> groupes = teacherRepository.findTeacherByIdTeacher(idTeacher).getGroupes();

        int count = 0;
        for (Groupe group : groupes){

            count += group.getStudents().size() ;
        }
        return count;
    }

    @GetMapping("/nbGStudents/{idGroupe}")
    public int getGStudents(@PathVariable Long idGroupe) {
       return groupeRepository.findGroupeByIdGroupe(idGroupe).getStudents().size();
    }


    @GetMapping("/totalLecture/{idLecture}")

    public double getTotalLectures(@PathVariable Long idLecture) {
        return lectureRepository.findLectureByIdLecture(idLecture).getPayments().size() * lectureRepository.findLectureByIdLecture(idLecture).getLecturePrice();
    }

    @GetMapping("/totalGroupe/{idGroupe}")

    public double getTotalGroupe(@PathVariable Long idGroupe) {
       double total = 0;
        List<Lecture> lectures = groupeRepository.findGroupeByIdGroupe(idGroupe).getLectures();

        for (Lecture lecture : lectures){

            total+= lecture.getLecturePrice() * lecture.getPayments().size();
        }
return total;
    }

    @GetMapping("/totalTeacher/{idTeacher}")

    public double getTotalTeacher(@PathVariable Long idTeacher) {
        List<Groupe> groupes = teacherRepository.findTeacherByIdTeacher(idTeacher).getGroupes();

        double total=0;

        for (Groupe group : groupes){

            List<Lecture> lectures = group.getLectures() ;

            for (Lecture lecture : lectures){

                total+= lecture.getLecturePrice() * lecture.getPayments().size();
            }

        }
        return total;
    }


    @GetMapping("/nbLectures/{idGroupe}")

    public int getNblectures(@PathVariable Long idGroupe) {
        return groupeRepository.findGroupeByIdGroupe(idGroupe).getLectures().size();
    }


    @GetMapping("/nbStudents/{idGroupe}")

    public int getNbStudents(@PathVariable Long idGroupe) {
        groupeRepository.findGroupeByIdGroupe(idGroupe).getStudents();

        return groupeRepository.findGroupeByIdGroupe(idGroupe).getStudents().size();
    }



}