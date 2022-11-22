package ua.kpi.report.controller;

//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ua.kpi.report.model.Employer;
import ua.kpi.report.model.list.RowList;
import ua.kpi.report.model.reader.excel.ExcelReader;
import ua.kpi.report.model.reader.excel.list.ExcelRowList;
import ua.kpi.report.model.writer.word.WordWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static ua.kpi.report.constants.ConstantsClass.*;

@Controller
public class HomePage {
    private static String STATUS;
    private ArrayList<Employer> employers = new ArrayList<>();
    private List<String> titles = new ArrayList<>();
    private List<String> templates = new ArrayList<>();
    private File file;
    private ExcelReader fileReader;
    private String template;
    private String wordFileName;

    @GetMapping
    public String hello(Model model) {
        return "index";
    }

    @PostMapping("uploadFile")
    public String loadFile(@RequestParam("file") MultipartFile multipartFile, Model model) throws IOException {
        file = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        file.createNewFile();

        try (OutputStream os = new FileOutputStream(file)) {
            os.write(multipartFile.getBytes());
        }
        uploadExcelFile();
        templates = getTemplatesName();

        model.addAttribute("employers", employers);
        model.addAttribute("title", titles);
        model.addAttribute("template", templates);
        return "main";
    }

    @PostMapping("generateWord")
    public String generateWord(@RequestParam("template") ArrayList template, @RequestParam("select") ArrayList select, Model model) throws IOException {
        this.template = ((String) template.get(0));
        createWord(select);
        model.addAttribute("employers", employers);
        model.addAttribute("title", titles);
        model.addAttribute("template", templates);
        return "redirect:resources/" + URLEncoder.encode(this.template, "UTF-8") + "/" + URLEncoder.encode(wordFileName, "UTF-8") + WORD_DOCUMENT;
    }

    private void createWord(ArrayList select) {
        String templateFilePath = TEMPLATE_DIR + template + WORD_DOCUMENT;
        WordWriter wordWriter = new WordWriter(templateFilePath);

        Map<Integer, List<String>> listOfRowData = getListOfRowData(select);
        wordWriter.fillWordDocument(listOfRowData);
        wordFileName = listOfRowData.get(NUMBER_NAME_COLUMN).get(NUMBER_NAME_COLUMN).replace(" ", "");
        String reportFilePath = REPORT_DIR + template + "/" + wordFileName + WORD_DOCUMENT;
        wordWriter.createNewWordDocument(reportFilePath);
    }

    private Map<Integer, List<String>> getListOfRowData(ArrayList select) {
        for (int i = 0; i < select.size(); i++) {
            select.set(i, ((String) select.get(i)).replace("\t", ", "));
        }
//        ObservableList<ObservableList<String>> selectedRow = FXCollections.observableArrayList();
        employers
                .stream()
                .filter(employer -> select.contains(employer.getFields().toString().replace("[", "").replace("]", "")))
                .forEach(employer -> selectedRow.add(employer.getFields()));
        Map<Integer, List<String>> rowIndexToRowCells = new HashMap<>();


        int rowIndex = 0;
//        for (ObservableList<String> listOfRowData : selectedRow) {
//            rowIndexToRowCells.put(rowIndex++, listOfRowData);
//        }

        return rowIndexToRowCells;
    }

    @PostMapping("loadNewTemplate")
    public String loadNewTemplate(@RequestParam("word") List<MultipartFile> multipartFiles, Model model) throws IOException {
        for (MultipartFile multipartFile : multipartFiles) {
            file = new File(TEMPLATE_DIR + Objects.requireNonNull(multipartFile.getOriginalFilename()).replace(" ", ""));
            file.createNewFile();

            try (OutputStream os = new FileOutputStream(file)) {
                os.write(multipartFile.getBytes());
            }
        }

        templates = getTemplatesName();

        model.addAttribute("employers", employers);
        model.addAttribute("title", titles);
        model.addAttribute("template", templates);
        return "main";
    }

    private List<String> getTemplatesName() throws IOException {
        List<String> allTemplates = new ArrayList<>();
        File file = new File(TEMPLATE_DIR);

        if (file.listFiles() != null) {
            allTemplates.addAll(createTemplatesName(file));
            createReportFolder(allTemplates);
        }

        return allTemplates;
    }

    private List<String> createTemplatesName(File file) {
        List<String> allTemplates = new ArrayList<>();

        for (File template : file.listFiles()) {
            allTemplates.add(template.getName().replace(WORD_DOCUMENT, ""));
        }

        return allTemplates;
    }

    private void createReportFolder(List<String> allTemplates) throws IOException {
        for (String fileName : allTemplates) {
            Files.createDirectories(Paths.get(REPORT_DIR + fileName));
        }
    }

    private void uploadExcelFile() {
        titles.clear();
        employers.clear();
        loadExcelFile();

        if (fileReader != null) {
            fillTable();
        }
    }

    private void loadExcelFile() {
        if (file != null) {
            fileReader = new ExcelReader(file.getPath());
            System.out.println("file = " + file);
            STATUS = SUCCESS;
        }
    }

    private void fillTable() {
        createColumnsWithTitle();
        fillCell();
    }

    private void createColumnsWithTitle() {
        titles.addAll(fileReader.getTitles());
    }

    private void fillCell() {
        ExcelRowList excelRowList = new ExcelRowList(fileReader);

        excelRowList.fillRows();
        excelRowList.getRowList().getRows().forEach(fields -> employers.add(new Employer(fields)));
    }

}