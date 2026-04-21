package studentsystem.service;

import org.w3c.dom.*;
import studentsystem.exception.FileException;
import studentsystem.model.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class XmlParser {
    public SystemData load(String filePath) {
        try {
            File file = new File(filePath);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file);
            doc.getDocumentElement().normalize();

            SystemData data = new SystemData();
            parseSpecialties(doc, data);
            parseStudents(doc, data);
            return data;

        } catch (Exception e) {
            throw new FileException("Грешка при четене на файл: " + e.getMessage());
        }
    }

    public void save(SystemData db, String filePath) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            Element root = doc.createElement("university");
            doc.appendChild(root);

            Element specialtiesEl = doc.createElement("specialties");
            root.appendChild(specialtiesEl);
            for (Specialty s : db.getSpecialties()) {
                specialtiesEl.appendChild(buildSpecialtyElement(doc, s));
            }

            Element studentsEl = doc.createElement("students");
            root.appendChild(studentsEl);
            for (Student s : db.getStudents()) {
                studentsEl.appendChild(buildStudentElement(doc, s));
            }

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.transform(new DOMSource(doc), new StreamResult(new File(filePath)));

        } catch (Exception e) {
            throw new FileException("Грешка при запис на файл: " + e.getMessage());
        }
    }

    private void parseSpecialties(Document doc, SystemData db) {
        NodeList specialtyNodes = doc.getElementsByTagName("specialty");
        for (int i = 0; i < specialtyNodes.getLength(); i++) {
            Element el = (Element) specialtyNodes.item(i);
            String name = el.getAttribute("name");
            int totalYears = Integer.parseInt(el.getAttribute("totalYears"));
            int minCredits = el.hasAttribute("minElectiveCredits")
                    ? Integer.parseInt(el.getAttribute("minElectiveCredits")) : 0;

            Specialty specialty = new Specialty(name, totalYears, minCredits);

            NodeList yearNodes = el.getElementsByTagName("year");
            for (int j = 0; j < yearNodes.getLength(); j++) {
                Element yearEl = (Element) yearNodes.item(j);
                int yearNum = Integer.parseInt(yearEl.getAttribute("number"));

                NodeList disciplines = yearEl.getElementsByTagName("discipline");
                for (int k = 0; k < disciplines.getLength(); k++) {
                    Element dEl = (Element) disciplines.item(k);
                    String dName = dEl.getAttribute("name");
                    DisciplineType type = DisciplineType.valueOf(dEl.getAttribute("type"));
                    int credits = dEl.hasAttribute("credits")
                            ? Integer.parseInt(dEl.getAttribute("credits")) : 0;

                    Discipline discipline = new Discipline(dName, type, credits);

                    NodeList availYears = dEl.getElementsByTagName("year");
                    for (int m = 0; m < availYears.getLength(); m++) {
                        int ay = Integer.parseInt(availYears.item(m).getTextContent().trim());
                        discipline.addAvailableYear(ay);
                    }

                    specialty.addDiscipline(yearNum, discipline);
                }
            }
            db.addSpecialty(specialty);
        }
    }

    private void parseStudents(Document doc, SystemData db) {
        NodeList studentNodes = doc.getElementsByTagName("student");
        for (int i = 0; i < studentNodes.getLength(); i++) {
            Element el = (Element) studentNodes.item(i);
            String fn = el.getAttribute("facultyNumber");
            String name = el.getAttribute("name");
            String program = el.getAttribute("program");
            String group = el.getAttribute("group");
            int year = Integer.parseInt(el.getAttribute("year"));
            StudentStatus status = StudentStatus.valueOf(el.getAttribute("status"));

            Student student = new Student(fn, name, program, group, year);
            student.setStatus(status);

            NodeList gradeNodes = el.getElementsByTagName("grade");
            for (int j = 0; j < gradeNodes.getLength(); j++) {
                Element gEl = (Element) gradeNodes.item(j);
                String discipline = gEl.getAttribute("discipline");
                double value = Double.parseDouble(gEl.getAttribute("value"));
                student.addGrade(new Grade(discipline, value));
            }
            db.addStudent(student);
        }
    }

    private Element buildSpecialtyElement(Document doc, Specialty s) {
        Element el = doc.createElement("specialty");
        el.setAttribute("name", s.getName());
        el.setAttribute("totalYears", String.valueOf(s.getTotalYears()));
        el.setAttribute("minElectiveCredits", String.valueOf(s.getMinElectiveCredits()));

        for (int year = 1; year <= s.getTotalYears(); year++) {
            Element yearEl = doc.createElement("year");
            yearEl.setAttribute("number", String.valueOf(year));
            for (Discipline d : s.getDisciplinesForYear(year)) {
                Element dEl = doc.createElement("discipline");
                dEl.setAttribute("name", d.getName());
                dEl.setAttribute("type", d.getType().name());
                dEl.setAttribute("credits", String.valueOf(d.getCredits()));
                if (!d.getAvailableInYears().isEmpty()) {
                    Element availEl = doc.createElement("availableInYears");
                    for (int ay : d.getAvailableInYears()) {
                        Element ayEl = doc.createElement("year");
                        ayEl.setTextContent(String.valueOf(ay));
                        availEl.appendChild(ayEl);
                    }
                    dEl.appendChild(availEl);
                }
                yearEl.appendChild(dEl);
            }
            el.appendChild(yearEl);
        }
        return el;
    }

    private Element buildStudentElement(Document doc, Student s) {
        Element el = doc.createElement("student");
        el.setAttribute("facultyNumber", s.getFacultyNumber());
        el.setAttribute("name", s.getName());
        el.setAttribute("program", s.getProgram());
        el.setAttribute("group", s.getGroup());
        el.setAttribute("year", String.valueOf(s.getYear()));
        el.setAttribute("status", s.getStatus().name());

        Element gradesEl = doc.createElement("grades");
        for (Grade g : s.getGrades()) {
            Element gEl = doc.createElement("grade");
            gEl.setAttribute("discipline", g.getDisciplineName());
            gEl.setAttribute("value", String.valueOf(g.getValue()));
            gradesEl.appendChild(gEl);
        }
        el.appendChild(gradesEl);
        return el;
    }
}
