package ua.kpi.report;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static ua.kpi.report.constants.ConstantsClass.TEMPLATE_DIR;

@SpringBootApplication
public class ReportApplication {

	public static void main(String[] args) throws IOException {
		Files.createDirectories(Paths.get(TEMPLATE_DIR));
		SpringApplication.run(ReportApplication.class, args);
	}

}
