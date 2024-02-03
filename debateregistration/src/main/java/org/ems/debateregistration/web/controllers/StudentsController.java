package org.ems.debateregistration.web.controllers;

import com.lowagie.text.DocumentException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.ems.debateregistration.models.Student;
import org.ems.debateregistration.repositories.StudentRepository;
import org.ems.debateregistration.utility.PDFGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


@Controller
public class StudentsController {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentsController(StudentRepository studentRepository){
        this.studentRepository = studentRepository;
    }

    @GetMapping("/students")
    public String students(Model model) {
        List<Student> students = (List<Student>) studentRepository.findAll();
        if(!students.isEmpty()){
            model.addAttribute("students", studentRepository.findAll());
        }
        return "index";
    }

    @GetMapping("/students/view/{id}")
    public String viewStudent(@PathVariable("id") long id, Model model){
        Student student = studentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid student id: " + id));
        model.addAttribute("student", student);
        return "view";
    }

    @GetMapping("/students/create")
    public String createStudent(Student student){
        return "create";
    }

    @PostMapping("/students/create")
    public String createStudent(@Valid Student student, BindingResult result, Model model) {
        if(result.hasErrors()){
            return "create";
        }
        student.setNew(true);
        studentRepository.save(student);
        return "redirect:/students";
    }

    @GetMapping("/students/edit/{id}")
    public String editStudent(@PathVariable("id") long id, Model model){
        Student student = studentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid student id: " + id));;
        model.addAttribute("student", student);
        return "edit";
    }

    @PostMapping("/students/update/{id}")
    public String updateStudent(@PathVariable("id") long id, @Valid Student student, BindingResult result, Model model){
        if(result.hasErrors()){
            student.setId(id);
            return "edit";
        }
        student.setNew(false);
        studentRepository.save(student);
        return "redirect:/students";
    }

    @GetMapping("/students/delete/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String delete(@PathVariable("id") long id, Model model){
        Student ticket = studentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid student id: " + id));
        studentRepository.delete(ticket);
        model.addAttribute("tickets", studentRepository.findAll());
        return "redirect:/students";
    }

    @GetMapping("/students/print")
    public void generatePdf(HttpServletResponse response) throws DocumentException, IOException {

        response.setContentType("application/pdf");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd:HH:mm:ss");
        String currentDateTime = dateFormat.format(new Date());
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=pdf_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);
        Iterable<Student> studentIterator = studentRepository.findAll();
        List<Student> students = new ArrayList<>();
        studentIterator.forEach(students::add);

        PDFGenerator generator = new PDFGenerator();
        generator.setStudentList(students);
        generator.generate(response);

    }
}
